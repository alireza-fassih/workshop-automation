import React from 'react';
import AbstractList from '../Core/AbstractList';


class Goods extends AbstractList {

	constructor(props) {
		super(props);
		this.getCreatePanel = this.getCreatePanel.bind(this);
	}

	getEntityName() {
		return "goods";
	}

	getSearchPanel() {
		return [
			{ id: "LIKE:title", type: 'text', label: 'عنوان' },
			{ id: "EQ:category.id", type: 'combo', label: 'نوع ماده اولیه',
				values: this.state.options ? this.state.options.categories : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "EQ:firstState.id", type: 'combo', label: 'مرحله شروع',
				values: this.state.options ? this.state.options.states : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "EQ:deleted", type: 'combo', label: 'حذف شده', value: false,
				values: [ { id: true, title: 'باشد' }, { id: false, title: 'نباشد' }],
				convertToVal: it => it.id, convertToStr: it => it.title }
		]
	}

	getTableHead() {
		return ["شناسه", "عنوان", "حذف شده", "نوع", ""];
	}


	getCreatePanel() {
		let value = ( this.state.data && this.state.data.category ? this.state.data.category.id : undefined );
		return [
			{ id: "title", type: 'text', label: 'عنوان' },
			{ id: "category", type: "combo", label: 'نوع', value: value, values: this.state.options ? this.state.options.categories : [],
				convertToVal: it => it.id , convertToStr: it => it.title },
			this.createCombo("firstState", "مرحله شروع", "states")
		];
	}


	saveEntity(entity) {
		this.refineComboBox( "category", entity );
		this.refineComboBox( "firstState", entity );
		super.saveEntity(entity);
	}



	convertToTableRow(data) {
		return (
			<tr key={"item_" + data.id} className={data.deleted?"deleted-row":""}>
				<td>{data.id}</td>
				<td>{data.title}</td>
				<td>
					<i className="fa fa-times" aria-hidden="true" hidden={data.deleted}></i>
					<i className="fa fa-check" aria-hidden="true" hidden={!data.deleted}></i>
				</td>
				<td>{data.category ? data.category.title : ""}</td>
				<td>
					{this.createControleIcons(data)}
				</td>
			</tr>
		);
	}

}


export default Goods;