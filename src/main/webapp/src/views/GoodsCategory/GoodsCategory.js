import React from 'react';
import AbstractList from '../Core/AbstractList'

class GoodsCategory extends AbstractList {

	constructor( props ) {
		super( props );

		this.getCreatePanel = this.getCreatePanel.bind( this );
	}

	getEntityName() {
		return "goodsCategory";
	}

	getSearchPanel() {
		return [
			{ id: "LIKE:title", type: 'text', label: 'عنوان' },
			{ id: "EQ:deleted", type: 'combo', label: 'حذف شده', value: false,
				values: [ { id: true, title: 'باشد' }, { id: false, title: 'نباشد' }],
				convertToVal: it => it.id, convertToStr: it => it.title }
		]
	}

	getTableHead() {
		return ["شناسه", "عنوان" , "حذف شده" , ""];
	}

	getCreatePanel() {
		return [
			{ id: "title", type: 'text', label: 'عنوان' }
		];
	}


	convertToTableRow( data ) {
		return (
			<tr key={"item_" + data.id}>
				<td>{data.id}</td>
				<td>{data.title}</td>
				<td>
					<i className="fa fa-times" aria-hidden="true" hidden={data.deleted}></i>
					<i className="fa fa-check" aria-hidden="true" hidden={!data.deleted}></i>
				</td>
				<td>
					{this.createControleIcons(data)}
				</td>
			</tr>
		);
	}

}


export default GoodsCategory;