import React, { Component } from 'react';
import { Col , Button} from 'reactstrap';
import { DatePicker } from 'react-persian-datepicker';
import { DATE_PICKER_STYLES } from '../views/Core/AbstractFormCreator'; 
import Form from "react-jsonschema-form";

import PropTypes from 'prop-types';

export const JalaliCalander = ({value, required, onChange}) => (
    <DatePicker calendarStyles={DATE_PICKER_STYLES} 
        value={value}
        required={required}
        onChange={(val) => onChange(val)} />
);


export default class FormGenerator extends Component {


    submitForm() {
        this.formRef.submit();
    }

    transformErrors(errors) {
        return errors
        .filter(error => {
            return !error.property.includes( "Date");
        })
        .map(error => {
            if (error.name === "pattern") {
                error.message = this.props.errorMessages.pattern;
            } else if( error.name === "required") {
                error.message = this.props.errorMessages.required;
            }
            return error;
        });
    }

    render() {
        return (
            <Col { ...this.props.containerStyle } >

                <Form schema={this.props.schema}
                    validate={this.props.validate}
                    uiSchema={this.props.uiSchema}
                    showErrorList={false}
                    noHtml5Validate={true}
                    transformErrors={this.transformErrors.bind(this)}
                    onChange={this.props.onChange}
                    onSubmit={this.props.onSubmit}
                    onError={this.props.onError} 
                    ref={(form) => this.formRef = form}>

                    <div>
                        <Button {...this.props.submitBtn}>{this.props.submitBtn.title}</Button>
                    </div>

                </Form>

              
            </Col>
        );
    }
}

FormGenerator.propTypes = {
    schema:  PropTypes.object.isRequired,
    onSubmit: PropTypes.func.isRequired
};
  
FormGenerator.defaultProps = {
    uiSchema: { },
    onChange: undefined,
    onError : undefined,
    containerStyle : {
        xs : 12,
        md : 5
    },
    errorMessages : {
        required : "پر کردن این قسمت اجباری است",
        pattern  : "فرمت وارد شده صحیح نیست"
    },
    submitBtn: {
        color : "success",
        size : "md",
        title : "ثبت"
    }
};