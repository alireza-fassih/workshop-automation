import React, { Component } from 'react';
import { Line } from 'react-chartjs-2';
import { CustomTooltips } from '@coreui/coreui-plugin-chartjs-custom-tooltips';
import RestProvider from '../Core/RestProvider';
import FormGenerator, { JalaliCalander } from '../../utils/FormGenerator';
import { clone , merge } from 'lodash';
import { Card, CardBody, Col, Row , Button, Badge, Label,
    Modal ,ModalHeader, ModalBody, ModalFooter, FormGroup } from 'reactstrap';
import { SketchPicker } from 'react-color';
import moment from 'jalali-moment';
import AbstractFormCreator from '../Core/AbstractFormCreator';


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


let totalFilters = 1;


class AddNewFilter extends AbstractFormCreator {

    constructor(props) {
        super(props);
        this.state = merge( {
            aggBy: "DAY",
            title: "فیلتر" + totalFilters++,
            "GE:createDate": moment().add(-1, 'jMonth').startOf('jMonth'),
            "LE:createDate": moment().startOf('jMonth'),
            "color": 'rgba( 0, 0, 0, 1 )'
        }, props.model);

        this.handleChange = this.handleChange.bind(this);
    }

    handleChange( event ) {
        this.setState({[event.target.name]: event.target.value});
    }

    apply() {
        this.props.onChange( this.state );
        this.setState( { id: undefined } );
    }

    render() {

        return (
            <Modal  isOpen={true} size="lg"  >
                <ModalHeader>{this.state.title}</ModalHeader>
                <ModalBody>
                    <Row>
                        <Col xs="12" md="7">
                            <Row>
                                { this.createTextInput(12, 12, "filter", { id: 'title', value: this.state.title , label: "نام فیلتر" }) }
                                { this.createComboBox(12, 12, 'filter', { id: 'aggBy', value: this.state.aggBy, label: "تجمیع شده براساس",
                                    values: [ { id: "MONTH", title: "ماه" }, { id: "DAY", title: "روز" } ],
                                    convertToVal: it => it.id, convertToStr: it => it.title  }) }
                                { this.createDateInput(12, 12, "filter", { id: "GE:createDate", value: this.state['GE:createDate'], label: "تاریخ شروع" } ) }
                                { this.createDateInput(12, 12, "filter", { id: "LE:createDate", value: this.state['LE:createDate'], label: "تاریخ پایان" } ) }
                                { this.createComboBox(12, 12, 'filter', { id: 'EQ:creator.id', value: this.state['EQ:creator.id'], label: 'سفارش دهنده',
                                    values: this.props.users,
                                    convertToVal: it => it.id, convertToStr: it => it.title  }) }
                                { this.createComboBox(12, 12, 'filter', { id: 'EQ:items.goods.id', value: this.state['EQ:creator.id'], label: 'کالا',
                                    values: this.props.products,
                                    convertToVal: it => it.id, convertToStr: it => it.title  }) }
                            </Row>
                        </Col>
                        <Col xs="12" md="5">
                            <FormGroup>
                                <Label for="filter-color">رنگ</Label>
                                <SketchPicker name="color" id="filter-color" color={this.state.color} 
                                    onChangeComplete={ (e) => this.setState({color: `rgba( ${e.rgb.r}, ${e.rgb.g}, ${e.rgb.b}, ${e.rgb.a} )`}) }/>
                            </FormGroup>
                        </Col>
                    </Row>
                </ModalBody>
                <ModalFooter>
                    <FormGroup>
                        <Button type="submit" size="md"  onClick={this.props.cancel}>بازگشت</Button>
                        <Button type="submit" size="md" color="success" onClick={this.apply.bind(this)}>اعمال</Button>
                    </FormGroup>
                </ModalFooter>
            </Modal>
        );
    }
}



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



export default class AdvanceReport extends Component {


    state = {
        filters : [],
        filterIndex : 0,
        options: null,
        labels: []
    };


    componentDidMount() {
        let temporalRest = new RestProvider("allOrder");
        temporalRest.options()
            .then(resp => this.setState({ options: resp.data }));
    }


    onSubmit(data) {
        let requestData = clone(data.formData, true);
        requestData.startDate = requestData.startDate.toISOString();
        requestData.endDate = requestData.endDate.toISOString();
        
    }

    /*
    <ColWithCard sm="12" md="12">
        <FormGenerator
            validate={validate}
            schema={schema}
            uiSchema={uiSchema}
            submitBtn={{ title : "اعمال"}}
            onSubmit={this.onSubmit.bind(this)}/>
    </ColWithCard>


     { this.state && this.state.line &&
    <ColWithCard sm="12" md="12">
        <div className="chart-wrapper">
            <Line data={this.state.line} options={options} /> 
        </div>
    </ColWithCard> }
    */

    createNewFilter( model ) {
        this.setState( { modal: model } )
    }

    cancel() {
        this.setState( { modal: null } )
    }

    addNewFilter( filter ) {
        let { filters, filterIndex } = this.state;
        if( filter.id === undefined ) {
            filter.id = filterIndex++;
        } 
        filter.color = filter.color.replace(/\s+/g, '');

        rest.postCustom("unit-by-time", filter)
            .then( resp => {
                let newLabels  =  new Set( this.state.labels );
                resp.data.forEach( item => {
                    newLabels.add(item.date);
                });
                let arr = Array.from(newLabels).sort();
                filter.fetchedData = resp.data;
                filters[ filter.id ] = filter;
                filters = filters.map( f => this.refineFilter(f, arr) )
                this.setState({ filters: filters, filterIndex: filterIndex, modal: null, labels: arr })
            });
    }



    refineFilter( filter , newLabels ) {
        let shownData = [];
        newLabels.forEach( l => {
            let ok = false;
            filter.fetchedData.forEach( d => {
                if( !ok && d.date === l ) {
                    shownData.push( d.data );
                    ok = true;
                }
            })
            if( !ok ) {
                shownData.push( 0 );
            }
        })
        filter.shownData = shownData;
        return filter;
    }

    charData() {
        let data = {
            labels : this.state.labels.map( l => moment(l).locale('fa').format("DD/MM/YYYY") ),
            datasets: this.state.filters.map( f => ({
                label: f.title,
                backgroundColor: f.color,
                strokeColor: f.color,
                pointColor: f.color,
                pointHighlightStroke: f.color,
                data: f.shownData,
                pointStrokeColor:  f.color,
                pointHighlightFill:  f.color,
            }))
        };
        return data;
    }

    render() {
        let charData = this.charData();
        return (
            <div className="animated fadeIn">  
                {this.state.modal ? <AddNewFilter {...this.state.options} model={this.state.modal} cancel={this.cancel.bind(this)} onChange={this.addNewFilter.bind(this)} /> : null}            
                <Row>  
                    <ColWithCard sm="12" md="12">
                        <Button color="primary" size="sm" outline onClick={this.createNewFilter.bind(this, {})}>اضافه کردن</Button>
                        { this.state.filters.map( f => 
                                <Button key={`btn-filter-${f.id}`} size="sm" style={{backgroundColor : f.color}} outline onClick={this.createNewFilter.bind(this, f)} >
                                    {f.title}
                                </Button>
                            )}
                    </ColWithCard>
                </Row>
                <Row>
                    <ColWithCard sm="12" md="12">
                    <div className="chart-wrapper">
                        <Line data={charData} options={options} /> 
                    </div>
                    </ColWithCard>
                </Row>
            </div>
        );
    }
}