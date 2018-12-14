import React, { Component } from 'react';
import {  Button, Table, Modal ,ModalHeader, ModalBody, ModalFooter, FormGroup } from 'reactstrap';


export default class OrderDialog extends Component {


    render() {
        let order = this.props.order;
        let tableBody = [];
        order.items.forEach( order => {
            tableBody.push(  <tr><th>{order.goodsTitle} {order.count} عدد</th><th></th></tr> );
            tableBody.push( ... order.items.map( item => {
                let itemTitle = item.title + ( item.selectable ? ": " + item.rawMaterialTitle : "");
                return ( <tr><td>{itemTitle}</td><td>{item.price}</td></tr> )
            }));
        })
        tableBody.push(  <tr><th>جمع کل</th><th>{order.totlaPrice}</th></tr> );
        return(
            <Modal isOpen={true} size="lg"  >
                <ModalHeader>{this.props.title}</ModalHeader>
                <ModalBody>
                    <Table responsive style={ { marginTop : '35px' } }>
                        <thead><tr><th>کالا</th><th>قیمت‌</th></tr></thead>
                        <tbody>{tableBody}</tbody>
                    </Table>
                </ModalBody>
                <ModalFooter>
                    <FormGroup>
                        <Button type="submit" size="sm" color="primary" onClick={this.props.dismiss}>بازگشت</Button>
                        { this.props.register && <Button type="submit" size="sm" color="primary" onClick={this.props.register}>ثبت سفارش</Button> }
                    </FormGroup>
                </ModalFooter>
            </Modal>);
    }

}