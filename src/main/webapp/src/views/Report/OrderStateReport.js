import React, { Component } from 'react';
import { Line } from 'react-chartjs-2';
import { CustomTooltips } from '@coreui/coreui-plugin-chartjs-custom-tooltips';
import RestProvider from '../Core/RestProvider';
import FormGenerator, { JalaliCalander } from '../../utils/FormGenerator';
import { clone } from 'lodash';
import { Card, CardBody, Col, Row } from 'reactstrap';
import moment from 'jalali-moment';


const line = {
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    datasets: [
        
    ]
};

const options = {
    tooltips: {
        enabled: false,
        custom: CustomTooltips
    },
    maintainAspectRatio: false
}
  

const schema = {
    title: "تنظیم گزارش",
    type: "object",
    required: ["startDate", "endDate", "state"],
    properties: {
        startDate: {
            type: "string",
            title: "شروع بازه گزارش گیری"
        },
        endDate: {
            type: "string",
            title: "انتهای بازه گزارش گیری"
        },
        user: {
            type: "number",
            title: "کاربر",
            anyOf: [
                {
                  type: "number",
                  enum: [
                    1
                  ],
                  title: "Root"
                },
            ]
        },
        state: {
            type: "number",
            title: "مرحله",
            anyOf: [
                {
                  type: "number",
                  enum: [
                    1
                  ],
                  title: "ثبت محصول"
                },
            ]
        }
    }
};

const uiSchema = {
    startDate: {
        "ui:widget" : JalaliCalander
    },
    endDate: {
        "ui:widget" : JalaliCalander
    },
    reportType: {
        "ui:widget": "radio"
    }
};


const ColWithCard = ({ xs, md , children}) => (
    <Col xs={xs} md={md}>
        <Card>
            <CardBody>
                {children}
            </CardBody>
        </Card>
    </Col>
);

ColWithCard.defaultProps = {
    xs: 12,
    md: 12
};

function validate(formData, errors) {
    if (formData.startDate._d.getTime() >= formData.endDate._d.getTime()) {
        errors.addError("شروع بازه باید کوچکتر از انتهای بازه باشد");
    }
    return errors;
}

let rest = new RestProvider("report");

function mapToAnyOf(data) {
    return data.map( d => ({
        type: "number",
        enum: [ d.id ],
        title: d.title
    }));
}

export default class OrderStateReport extends Component {


    componentDidMount() {
        rest.options()
            .then(resp => { 
                let sch =  clone( schema, true );
                sch.properties.user.anyOf = mapToAnyOf(resp.data.users);
                sch.properties.state.anyOf = mapToAnyOf(resp.data.states);
                this.setState({ schema: sch })
            });
    }



    onSubmit(data) {
        let requestData = clone(data.formData, true);
        requestData.startDate = requestData.startDate.toISOString();
        requestData.endDate = requestData.endDate.toISOString();
        rest.postCustom("byState", requestData)
            .then( resp => {
                let temporal = clone(line, true);
                temporal.labels = [];
                temporal.datasets = [];
                let dataSet = {
                    label: 'سفارش',
                    data: []
                };
                resp.data.forEach( item => {
                    temporal.labels.push( 
                        moment(item.groupedDate,"YYYY-MM-DD")
                        .locale('fa')
                        .format("DD/MM/YYYY"));
                    dataSet.data.push(item.count);
                });
                temporal.datasets.push(dataSet);
                console.log(  temporal )
                this.setState({ line: temporal });
            });
    }

    render() {
        return (
            <div className="animated fadeIn">  
                <Row>
                    <ColWithCard sm="12" md="5">
                        { this.state && this.state.schema  && <FormGenerator
                            validate={validate}
                            schema={this.state.schema}
                            uiSchema={uiSchema}
                            submitBtn={{ title : "اعمال"}}
                            onSubmit={this.onSubmit.bind(this)}/>}
                    </ColWithCard>
                    { this.state && this.state.line &&
                    <ColWithCard sm="12" md="6">
                        <div className="chart-wrapper">
                            <Line data={this.state.line} options={options} /> 
                        </div>
                    </ColWithCard> }
                </Row>
            </div>
        );
    }
}