import React from 'react';
import AbstractList from '../Core/AbstractList'
import moment from 'jalali-moment'
import { Button, Table, Input, Label,
    Modal ,ModalHeader, ModalBody, ModalFooter, FormGroup }  from 'reactstrap';

import currency from '../../utils/currency';

const ButtonStyle = {
    marginLeft: "10px"
}

export default class MyOrderList extends AbstractList {

	constructor(props) {
		super(props);
		this.renderCustomElement = this.renderCustomElement.bind(this);
		this.dismissDialog = this.dismissDialog.bind(this);
	}

	getEntityName() {
		return "myOrder";
	}

	getSearchPanel() {
		return [
      { id: "EQ:id", type: 'text', label: 'کد سفارش' },
			{ id: "LIKE:items.goods.title", type: 'text', label: 'عنوان' },

			{ id: "EQ:items.goods.id", type: 'combo', label: 'کالا',
				values: this.state.options ? this.state.options.products : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "EQ:currentState.id", type: 'combo', label: 'وضعیت',
				values: this.state.options ? this.state.options.states : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "GE:createDate", type: 'date', label: 'زمان ایجاد از' },
			{ id: "LE:createDate", type: 'date', label: 'زمان ایجاد تا' }
		]
	}

	hasXslExport(){
		return true;
	}

	getTableHead() {
		return ["کد سفارش", "عنوان" , "زمان ایجاد", "وضعیت", "قیمت", ""];
	}

	createAble() {
        return false;
	}

	renderCustomElement() {
		if( this.state.shownOrder ) {

			let tableBody = [];
            this.state.shownOrder.items.forEach( order => {
                tableBody.push(  <tr><th>{order.goodsTitle} {order.count} عدد</th><th></th></tr> );
                tableBody.push( ... order.items.map( item => {
                    let itemTitle = item.title + ( item.selectable ? ": " + item.rawMaterialTitle : "");
                    return ( <tr><td>{itemTitle}</td><td>{item.price}</td></tr> )
				}));
			})

			tableBody.push(  <tr><th>جمع کل</th><th>{this.state.shownOrder.totlaPrice}</th></tr> );

			return (
				<Modal  isOpen={true} size="lg"  >
					<ModalHeader>مشاهده سفارش</ModalHeader>
					<ModalBody>
						<FormGroup>
							<Table responsive style={ { marginTop : '35px' } }>
								<thead><tr><th>کالا</th><th>قیمت‌</th></tr></thead>
								<tbody>{tableBody}</tbody>
							</Table>
						</FormGroup>
						<FormGroup hidden={!this.state.shownOrder.discount}>
							<Label>تخفیف</Label>
							<Input type="number" disabled value={this.state.shownOrder.discount}  />
						</FormGroup>
						<FormGroup hidden={!this.state.shownOrder.description}>
							<Label>توضیحات سفارش دهنده</Label>
							<Input type="textarea" name="description" id="orderDescription" disabled
								value={this.state.shownOrder.description}  />
						</FormGroup>
						<FormGroup hidden={!this.state.shownOrder.extraDescription}>
							<Label>توضیحات تایید کننده</Label>
							<Input type="textarea" name="extraDescription" id="orderExtraDescription" disabled
								value={this.state.shownOrder.extraDescription} />
						</FormGroup>
					</ModalBody>
					<ModalFooter>
						<FormGroup>
							<Button type="submit" size="md" style={ButtonStyle} onClick={this.dismissDialog}>بازگشت</Button>
						</FormGroup>
					</ModalFooter>
				</Modal>);
		}
	}


	showOrder(order) {
		this.setState({ shownOrder: order });
	}

	dismissDialog() {
		this.setState({ shownOrder: null });
	}

	editOrder(id) {
		this.props.history.push('/edit-order/' + id);
	}

	convertToTableRow( data ) {
    let style = {};
    if(data.currentState && data.currentState.color) {
      style.backgroundColor = data.currentState.color;
    }
		return (
			<tr key={"item_" + data.id} style={style} className={data.currentState.code === "REJECTED" ? "deleted-row" : ""}>
				<td>{data.id}</td>
				<td>{data.title}</td>
				<td>{moment(data.createDate).locale('fa').format('YYYY/MM/DD')}</td>
				<td>{data.currentState ? data.currentState.title : ""}</td>
				<td>{currency.format(data.totlaPrice)}</td>
				<td>
					{ data.editable && <a style={ButtonStyle} href="javascript:void(0);" onClick={this.deleteEntity.bind(this, data.id)}><i className="fa fa-remove"  aria-hidden="true"></i></a> }
					{ data.editable && <a style={ButtonStyle} href="javascript:void(0);" onClick={this.editOrder.bind(this, data.id)}><i className="fa fa-pencil"  aria-hidden="true"></i></a> }


					<a style={ButtonStyle} href="javascript:void(0);" onClick={this.showOrder.bind(this, data)}>
						<i className="fa fa-eye" aria-hidden="true"></i>
					</a>
				</td>
			</tr>
		);
	}

}
