import React from 'react';
import { Button } from 'reactstrap';
import AbstractList from '../Core/AbstractList';

export default class GoodsListForOrder extends AbstractList {

	constructor(props) {
		super(props);
		this.state.temp = [];
		this.renderCustomElement = this.renderCustomElement.bind(this);
	}

	getEntityName() {
		return "createOrder";
	}

	getSearchPanel() {
		return [
			{ id: "LIKE:title", type: 'text', label: 'عنوان' },
			{ id: "EQ:category.id", type: 'combo', label: 'دسته بندی کالا',
				values: this.state.options ? this.state.options.categories : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
		]
	}

	getTableHead() {
		return ["نام کالا", ""];
	}

    createAble() {
        return false;
	}
	
	navigateToCreateOrder() {
		let elements = this.state.temp.map( item => item.count + "X" + item.id );
		this.props.history.push('/create-order/' + elements.join("-"));
	}

	addOne( id ) {
		let all = this.state.temp;
		let elements = all.filter( item => item.id === id );
		if( elements.length !== 0 ) {
			elements[0].count++;
		} else {
			all.push({ id: id, count: 1});
		}
		this.setState({ temp: all })
	}

	remoeOne( id ) {
		let all = this.state.temp;
		let elements = all.filter( item => item.id === id );
		if( elements.length !== 0 ) {
			elements[0].count--;
			if(elements[0].count === 0) {
				all = this.state.temp.filter( item => item.id !== id );
			}
		}
	
		this.setState({ temp: all })
	}

	renderCustomElement() {
		return (
			<Button type="submit" hidden={ this.state.temp.length === 0 } size="sm" color="primary" onClick={this.navigateToCreateOrder.bind(this)}>
				<i className="fa fa-plus" aria-hidden="true"></i> ثبت درخواست
			</Button>);
	}

	convertToTableRow(data) {
		let elements = this.state.temp.filter( item => item.id === data.id );
		let count = 0;
		if( elements.length !== 0 ) {
			count = elements[0].count;
		}
		return (
			<tr key={"item_" + data.id}>
				<td>{data.title}</td>
				<td>
                    <Button type="submit" size="sm" color="primary" onClick={this.addOne.bind(this, data.id)}>
                        {count !== 0 ? count : "" }<i className="fa fa-plus" aria-hidden="true"></i> اضافه کردن
                    </Button>

					<Button type="submit" size="sm" hidden={count===0} color="error" onClick={this.remoeOne.bind(this, data.id)}> کم کردن
                    </Button>
				</td>
			</tr>
		);
	}

}