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
    required: ["startDate", "endDate"],
    properties: {
        startDate: {
            type: "string",
            title: "شروع بازه گزارش گیری"
        },
        endDate: {
            type: "string",
            title: "انتهای بازه گزارش گیری"
        }
    }
};

const uiSchema = {
    startDate: {
        "ui:widget" : JalaliCalander
    },
    endDate: {
        "ui:widget" : JalaliCalander
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
    let hasError = false;
    ["startDate", "endDate"].forEach( elem => {
        if( formData[ elem ] === undefined ) {
            errors[ elem ].addError("پر کردن این قسمت اجباری است");
            hasError = true;
        }
    });
    if (!hasError && formData.startDate._d.getTime() >= formData.endDate._d.getTime()) {
        errors.addError("شروع بازه باید کوچکتر از انتهای بازه باشد");
    }
    return errors;
}

let rest = new RestProvider("report");



export default class OrderUnitReport extends Component {


    onSubmit(data) {
        let requestData = clone(data.formData, true);
        requestData.startDate = requestData.startDate.toISOString();
        requestData.endDate = requestData.endDate.toISOString();
        rest.postCustom("unit-by-time", requestData)
            .then( resp => {
                let temporal = clone(line, true);
                temporal.labels = [];
                temporal.datasets = [];
                let dataSet = {
                    label: 'دست',
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
                this.setState({ line: temporal });
            });
    }

    render() {
        return (
            <div className="animated fadeIn">  
                <Row>
                    <ColWithCard sm="12" md="4">
                        <FormGenerator
                            validate={validate}
                            schema={schema}
                            uiSchema={uiSchema}
                            submitBtn={{ title : "اعمال"}}
                            onSubmit={this.onSubmit.bind(this)}/>
                    </ColWithCard>
                    { this.state && this.state.line &&
                    <ColWithCard sm="12" md="7">
                        <div className="chart-wrapper">
                            <Line data={this.state.line} options={options} /> 
                        </div>
                    </ColWithCard> }
                </Row>
            </div>
        );
    }
}