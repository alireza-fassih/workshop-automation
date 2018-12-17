import React, { Component } from 'react';
import { Col, Label, Input, FormGroup } from 'reactstrap';
import { DatePicker } from 'react-persian-datepicker';
import Autosuggest from 'react-autosuggest';


let DATE_PICKER_STYLES = {
    "calendarContainer": "calendarContainer",
    "dayPickerContainer" : "dayPickerContainer",
    "monthsList" : "monthsList",
    "daysOfWeek" : "daysOfWeek",
    "dayWrapper" : "dayWrapper",
    "heading"    : "heading",
    "next": "next",
    "prev": "prev",
    "title": "title",
    "currentMonth": "currentMonth",
    "selected": "selected"
};

export let fillState = function ( items ) {
    let state = {};
    items.forEach(element => {
        state[ element.id ] = element.value;
    });
    return state;
}

class AbstractFormCreator extends Component {

    

    constructor( props ) {
        super(props);
       
        if( props.items ) {
            this.state = fillState( props.items );
        }

        this.createDateInput = this.createDateInput.bind( this );
        this.createTextInput = this.createTextInput.bind(this);
        this.createMultiSelect = this.createMultiSelect.bind(this);
        this.changeValue = this.changeValue.bind( this );
        this.setVal = this.setVal.bind(this);
        this.refineProps = this.refineProps.bind(this);
        this.wrapIntoFormGroup = this.wrapIntoFormGroup.bind(this);
        this.handleMultiSelectChange = this.handleMultiSelectChange.bind( this );
        this.createSuggestInput = this.createSuggestInput.bind( this );
        this.refineInternal = this.refineInternal.bind( this );
        this.createComboBox = this.createComboBox.bind( this );
        this.createCheckBox = this.createCheckBox.bind( this );
    }



    refineProps() {
        return this.refineInternal( this.props.items );
    }

    refineInternal( items ) {
        let props = {};
        items.forEach(element => {
            if( this.state[ element.id ] !== undefined && this.state[ element.id ] !== "" ) {
                if( element.type === "date" ) {
                    props[ element.id ] = this.state[ element.id ]._d.getTime();
                } else {
                    props[ element.id ] = this.state[ element.id ];
                }
            }
        });
        return props;
    }


    createMultiSelect( xs, md, idPreFix, item  ) {
        let htmlId = idPreFix + "_" + item.id;
        let options = item.values.map( it => <option key={htmlId + "_" + it} value={it}>{it}</option> );
        let element = (
            <Input type="select" multiple value={this.state[ item.id ]} 
                id={htmlId} onChange={ val => this.handleMultiSelectChange( item.id, val ) }>
                {options}
            </Input> 
        );
        return this.wrapIntoFormGroup( xs, md, htmlId, item.label, element );
    }

    createTextInput( xs, md, idPreFix, item ) {
        let htmlId = idPreFix + "_" + item.id;
        let element = (
            <Input type={item.type} id={htmlId} 
                value={this.state[ item.id ]} 
                onChange={ val => this.changeValue( item.id, val ) }/> );
        return this.wrapIntoFormGroup( xs, md, htmlId, item.label, element );
    }

    createCheckBox( xs, md, idPreFix, item ) {
        let htmlId = idPreFix + "_" + item.id;
        let element = (
            <Input type="checkbox" id={htmlId} checked={this.state[ item.id ]}
                onChange={ val => this.setVal( item.id, val.target.checked ) }/> );
        return (
            <FormGroup>
                <Label htmlFor={htmlId}>
                    {item.label}{element}
                </Label>
            </FormGroup>
        );
    }

    createComboBox( xs, md, idPreFix, item ){
        let htmlId = idPreFix + "_" + item.id;
        let options = item.values.map( it => <option key={htmlId + "_" + it} value={item.convertToVal(it)}>{item.convertToStr(it)}</option> );
        options.unshift( <option key={htmlId + "_NULL"}></option> );
        let element = (
            <Input type="select" value={this.state[ item.id ]} 
                id={htmlId} onChange={ val => this.handleComboSelectChange( item.id, val ) }>
                {options}
            </Input> 
        );
        return this.wrapIntoFormGroup( xs, md, htmlId, item.label, element );
    }

    createSuggestInput( xs, md, idPreFix, item ) {
        let htmlId = idPreFix + "_" + item.id;
        let inputProps = {
            placeholder: item.placeholder,
            value: this.state[ item.id ] ,
            onChange: (e, { newValue }) => this.setVal( item.id , newValue )
        };
      
        let element = (
            <Autosuggest id={htmlId}
                suggestions={item.values}
                onSuggestionsFetchRequested={item.fetch}
                onSuggestionsClearRequested={item.clear}
                getSuggestionValue={item.convertVal}
                renderSuggestion={item.renderSuggestion}
                shouldRenderSuggestions={ () => true }
                inputProps={inputProps} /> );

        return this.wrapIntoFormGroup( xs, md, htmlId, item.label, element );
    }

    createDateInput( xs, md, idPreFix, item ) {
        let htmlId = idPreFix + "_" + item.id;
        let datePicker = ( 
            <DatePicker calendarStyles={DATE_PICKER_STYLES} id={htmlId}
                value={this.state[ item.id ]}
                onChange={ val => this.setVal( item.id, val ) }  />);
        return this.wrapIntoFormGroup( xs, md, htmlId, item.label, datePicker, "datePicker" );
    }

    handleComboSelectChange( itemId, e ) {
        let options = e.target.options;
        for (let i = 0, l = options.length; i < l; i++) {
          if (options[i].selected) {
            this.setVal(itemId, options[i].value)
          }
        }
    }
    
    handleMultiSelectChange( itemId, e ) {
        let options = e.target.options;
        let value = [];
        for (let i = 0, l = options.length; i < l; i++) {
          if (options[i].selected) {
            value.push(options[i].value);
          }
        }
        this.setVal(itemId, value);
    }

    

    wrapIntoFormGroup(xs, md, itemId, label, element, wrapperClass = "") {
        return(
            <Col xs={xs} md={md} key={"formGroup_" + itemId} className={wrapperClass}>
                <FormGroup>
                    <Label htmlFor={itemId}>{label}</Label>
                    {element}
                </FormGroup>
            </Col>
        );
    }
  
    changeValue(itemId, event) {
        this.setVal( itemId, event.target.value );
    }
    
    setVal( itemId, val ) {
        let item = {};
        item[ itemId ] = val;
        this.setState( item );
    }
}

export default AbstractFormCreator;