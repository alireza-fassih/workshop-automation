import React from 'react';
import AbstractList from '../Core/AbstractList'
import moment from 'jalali-moment'
import  { Button, Table, Input,Label, Alert, Tooltip,
    Modal ,ModalHeader, ModalBody, ModalFooter, FormGroup }  from 'reactstrap';

import currency from '../../utils/currency';

const ButtonStyle = {
    marginLeft: "10px"
}

String.prototype.trunc = String.prototype.trunc ||
	function(n){
			return (this.length > n) ? this.substr(0, n-1) + ' ...' : this;
	};

export default class AllOrderList extends AbstractList {


	constructor(props) {
		super(props);
		this.renderCustomElement = this.renderCustomElement.bind(this);
		this.dismissDialog = this.dismissDialog.bind(this);
	}

	getEntityName() {
		return "allOrder";
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
			{ id: "EQ:creator.id", type: 'combo', label: 'سفارش دهنده',
				values: this.state.options ? this.state.options.users : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "GE:createDate", type: 'date', label: 'زمان ایجاد از' },
			{ id: "LE:createDate", type: 'date', label: 'زمان ایجاد تا' }
		]
	}

	getTableHead() {
		return ["کد سفارش", "عنوان" ,"دست", "زمان ایجاد", "وضعیت", "سفارش دهنده", "قیمت", ""];
	}

	hasXslExport(){
		return true;
	}

	createAble() {
    return false;
	}

	gotToNextState(id) {
		this.rest.postCustom(id + "/nextState")
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
						<FormGroup  hidden={!this.state.shownOrder.extraDescription}>
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
		} else if( this.state.discount ) {
			return (
				<Modal  isOpen={true} size="lg"  >
					<ModalHeader>تخفیف</ModalHeader>
					<ModalBody>
						<FormGroup>
							<Label>تخفیف</Label>
							<Input type="number" name="discount" id="discount" value={this.state.discount.discount}
								onChange={ ev => {
									let o = this.state.discount;
									o.discount = ev.target.value;
									this.setState({ discount : o });
								}}  />
						</FormGroup>
					</ModalBody>
					<ModalFooter>
						<FormGroup>
							<Button type="submit" size="md" style={ButtonStyle} onClick={this.dismissDialog}>بازگشت</Button>
							<Button type="submit" size="md" color="success" style={ButtonStyle} onClick={this.saveDiscount.bind(this)}>اعمال تخفیف</Button>
						</FormGroup>
					</ModalFooter>
				</Modal>);
		} else {
			return (
				<span>
					{this.state && this.state.notify && <Alert color="success">{this.state.notify}</Alert>}
					<Button onClick={this.recalculateUnits.bind(this)}>محاسبه دست ها</Button>
				</span>
			);
		}
	}

	recalculateUnits() {
		this.rest.postCustom("recalculate-units")
			.then(resp => { 
				this.doSearch(); 
				this.setState({notify: resp.data.message});
				setTimeout(() => { this.setState( { notify: null } )}, 2000);
			});
	}

	saveDiscount() {
		this.rest.postCustom("discount", { id : this.state.discount.id , discount: this.state.discount.discount } )
			.then( rest => {
				this.dismissDialog();
				this.doSearch();
			});

	}

	showOrder(order) {
		this.setState({ shownOrder: order });
	}

	dismissDialog() {
		this.setState({ shownOrder: null, discount : null });
	}

	setDiscount(order) {
		this.setState({ discount : order });
	}


	toggleToolTip(i) {
		let tool = this.state.toolTips || {};
		let oldVal = tool[i] || false;
		tool[i] = !oldVal;
		this.setState({ toolTips : tool });
	}


	convertToTableRow( data ) {
    let style = {};
    if(data.currentState && data.currentState.color) {
      style.backgroundColor = data.currentState.color;
    }
		return (
			<tr key={"item_" + data.id} style={style} className={data.currentState.code === "REJECTED" ? "deleted-row" : ""}
					id={"item_row_" + data.id} >

				<td>{data.id}</td>
				<td>{data.title}</td>
				<td>{data.unit}</td>
				<td>{moment(data.createDate).locale('fa').format('YYYY/MM/DD')}</td>
				<td>{data.currentState ? data.currentState.title : ""}</td>
				<td>{data.creator ? data.creator.username : ""}</td>
				<td>{currency.format( data.totlaPrice )}</td>
				<td>
					<a style={ButtonStyle} href="javascript:void(0);" onClick={this.gotToNextState.bind(this, data.id)}>
						<i className="fas fa-angle-double-right" aria-hidden="true"></i>
					</a>
					<a style={ButtonStyle} href="javascript:void(0);" onClick={this.showOrder.bind(this, data)}>
						<i className="fa fa-eye" aria-hidden="true"></i>
					</a>
					<a style={ButtonStyle} href="javascript:void(0);" onClick={this.deleteEntity.bind(this, data.id)}>
						<i className="fa fa-remove"  aria-hidden="true"></i>
					</a>
					<a style={ButtonStyle} href="javascript:void(0);" onClick={this.setDiscount.bind(this, data)}>
						<i className="fas fa-vote-yea" aria-hidden="true"></i>
					</a>
				</td>
			</tr>
		);
	}

}
