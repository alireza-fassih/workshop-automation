import React from 'react';
import { Modal , Button ,ModalHeader, ModalBody, ModalFooter, FormGroup } from 'reactstrap';
import AbstractFormCreator from './AbstractFormCreator';

const ButtonStyle = {
    marginLeft: "10px"
}

class CreateEntity extends AbstractFormCreator {

    constructor( props ) {
        super(props);
        this.save = this.save.bind( this );
        this.dismiss = this.dismiss.bind( this );
        this.getItems = this.getItems.bind( this );
    }

    save() {
        this.props.onSave( this.refineProps() );
    }

    dismiss() {
        this.props.dismiss();
    }

    getItems() {
        return this.props.items;
    }

    render() {
        let elements = this.getItems().map(item => {
            if ( item.type === "date" ) {
                return this.createDateInput( 12, 8 , "edit", item );
            } else if( item.type === "multiSelect" ) {
                return this.createMultiSelect(12, 8, "eidt", item);
            } else if ( item.type === "combo" ) {
                return this.createComboBox(12, 8, "edit", item);
            } else if( item.type === "suggest" ) {
                return this.createSuggestInput(12, 8, "eidt", item);
            } else if( item.type === "checkBox" ) {
                return this.createCheckBox( 2, 2, "edit", item );
            } else if( item.type === "file" ) {
              return this.createFile(12, 8, "edit", item);
            } else {
                return this.createTextInput( 12, 8, "edit", item );
            }
        });
        return (
            <Modal  isOpen={this.props.show} size="lg"  >
                <ModalHeader>{this.props.panelTitle ? this.props.panelTitle : 'فرم ایجاد' }</ModalHeader>
                <ModalBody>{elements}</ModalBody>
                <ModalFooter>
                    <FormGroup>
                        <Button type="submit" size="md" style={ButtonStyle} onClick={this.dismiss}>بازگشت</Button>
                        <Button type="submit" size="md" color="success" style={ButtonStyle} onClick={this.save}>ذخیره</Button>
                    </FormGroup>
                </ModalFooter>
            </Modal>
        );
    }

}


export default CreateEntity;
