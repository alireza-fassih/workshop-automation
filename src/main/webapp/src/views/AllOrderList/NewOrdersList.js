import React from 'react';
import AbstractList from '../Core/AbstractList'
import moment from 'jalali-moment'
import  { Button, Table, Input, Label,
    Modal ,ModalHeader, ModalBody, ModalFooter, FormGroup }  from 'reactstrap';


const ButtonStyle = {
    marginLeft: "10px"
}

export default class NewOrdersList extends AbstractList {


	constructor(props) {
		super(props);
		this.renderCustomElement = this.renderCustomElement.bind(this);
		this.dismissDialog = this.dismissDialog.bind(this);
		this.accept = this.accept.bind(this);
		this.reject = this.reject.bind(this);
	}

	getEntityName() {
		return "newOrders";
	}

	getSearchPanel() {
		return [
			{ id: "LIKE:title", type: 'text', label: 'عنوان' },
			{ id: "EQ:goods.id", type: 'combo', label: 'کالا',
				values: this.state.options ? this.state.options.products : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "EQ:creator.id", type: 'combo', label: 'سفارش دهنده',
				values: this.state.options ? this.state.options.users : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "EQ:currentState.id", type: 'combo', label: 'وضعیت',
				values: this.state.options ? this.state.options.states : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "GE:createDate", type: 'date', label: 'زمان ایجاد از' },
			{ id: "LE:createDate", type: 'date', label: 'زمان ایجاد تا' }
		]
	}

	getTableHead() {
		return ["کد سفارش", "عنوان" , "زمان ایجاد", "وضعیت", "سفارش دهنده", "قیمت", ""];
	}


	createAble() {
        return false;
	}

	accept() {
		let id = this.state.shownOrder.id;
		this.setState({ shownOrder: null , extraDescription : null });

		this.rest.postCustom("accept", { id: id, description : this.state.extraDescription })
			.then( resp => this.doSearch() );
	}


	reject() {
		let id = this.state.shownOrder.id;
		this.setState({ shownOrder: null , extraDescription : null });

		this.rest.postCustom("reject", { id: id, description : this.state.extraDescription })
			.then( resp => this.doSearch() );
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
			let isVerified = ( this.state.shownOrder.currentState.code === "REGISTRATION" );
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
						<FormGroup hidden={!isVerified && !this.state.shownOrder.extraDescription}>
							<Label>توضیحات تایید کننده</Label>
							{ isVerified && <Input type="textarea" name="extraDescription" id="orderExtraDescription"
								value={this.state.extraDescription}
									onChange={ ev => this.setState({ extraDescription : ev.target.value }) }  /> }

							{ !isVerified && <Input type="textarea" disabled value={this.state.shownOrder.extraDescription} /> }
						</FormGroup>
					</ModalBody>
					<ModalFooter>
						<FormGroup>
							<Button type="submit" size="md" style={ButtonStyle} onClick={this.dismissDialog}>بازگشت</Button>
							<Button hidden={!isVerified} type="submit" size="md" color="success" style={ButtonStyle} onClick={this.accept}>تایید سفارش</Button>
							<Button hidden={!isVerified} type="submit" size="md" color="danger" style={ButtonStyle} onClick={this.reject}>رد سفارش</Button>
						</FormGroup>
					</ModalFooter>
				</Modal>);
		}
	}

	showOrder(order) {
		this.setState({ shownOrder: order });
	}

	dismissDialog() {
		this.setState({ shownOrder: null , extraDescription : null });
	}

	hasXslExport(){
		return true;
	}

	gotToNextState(id) {
		this.rest.postCustom(id + "/nextState")
			.then( resp => this.doSearch() );
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
				<td>{data.creator ? data.creator.username : ""}</td>
				<td>{data.totlaPrice}</td>
				<td>
					<a style={ButtonStyle} href="javascript:void(0);" hidden={data.currentState.code === "REGISTRATION" || data.currentState.code === "REJECTED"}
						onClick={this.gotToNextState.bind(this, data.id)}>
						<i className="fas fa-angle-double-right" aria-hidden="true"></i>
					</a>
					<a style={ButtonStyle} href="javascript:void(0);" onClick={this.deleteEntity.bind(this, data.id)}>
						<i className="fa fa-remove"  aria-hidden="true"></i>
					</a>
					<a style={ButtonStyle} href="javascript:void(0);" onClick={this.showOrder.bind(this, data)}>
						<i className="fa fa-eye" aria-hidden="true"></i>
					</a>
				</td>
			</tr>
		);
	}

}
