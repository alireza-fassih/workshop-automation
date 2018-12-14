import React from 'react';
import AbstractList from '../Core/AbstractList'


class RawMaterial extends AbstractList {

	constructor( props ) {
		super( props );

		this.getCreatePanel = this.getCreatePanel.bind( this );
	}

	getEntityName() {
		return "rawMaterial";
	}

	getSearchPanel() {
		return [
			{ id: "LIKE:title", type: 'text', label: 'عنوان' },
			{ id: "EQ:category.id", type: 'combo', label: 'نوع ماده اولیه',
				values: this.state.options ? this.state.options.categories : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "EQ:deleted", type: 'combo', label: 'حذف شده', value: false,
				values: [ { id: true, title: 'باشد' }, { id: false, title: 'نباشد' }],
				convertToVal: it => it.id, convertToStr: it => it.title }
		]
	}

	getTableHead() {
		return ["شناسه", "عنوان" , "نوع", "قیمت واحد", ""];
	}

	getCreatePanel() {
		let value = ( this.state.data && this.state.data.category ? this.state.data.category.id : undefined );
		return [
			{ id: "title", type: 'text', label: 'عنوان' },
			{ id: "unitPrice", type: 'number', label: 'قیمت واحد' },
			{ id: "category", type: "combo", label: 'نوع', value: value, values: this.state.options ? this.state.options.categories : [],
				convertToVal: it => it.id , convertToStr: it => it.title }
		];
	}


	saveEntity(entity) {
		this.refineComboBox( "category", entity );
		super.saveEntity( entity );
	}


	convertToTableRow( data ) {
		return (
			<tr key={"item_" + data.id}>
				<td>{data.id}</td>
				<td>{data.title}</td>
				<td>{ data.category ? data.category.title : "" }</td>
				<td>{data.unitPrice}</td>
				<td>
					{this.createControleIcons(data)}
				</td>
			</tr>
		);
	}

}


export default RawMaterial;