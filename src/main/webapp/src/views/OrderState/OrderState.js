import React from 'react';
import AbstractList from '../Core/AbstractList'


export default class OrderState extends AbstractList {

	constructor( props ) {
		super( props );
		this.refreshOptions = this.refreshOptions.bind( this );
		this.getCreatePanel = this.getCreatePanel.bind( this );
	}

	refreshOptions() {
		this.rest.options()
			.then(resp => this.setState({ options: resp.data }));
	}

	getEntityName() {
		return "orderState";
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
		return ["شناسه", "عنوان", "بعد از", ""];
	}

	getCreatePanel() {
		let value = ( this.state.data ? this.state.data.parentId : undefined );
		return [
			{ id: "title", type: 'text', label: 'عنوان' },
			{ id: "parent", type: "combo", label: 'بعد از', value: value, values: this.state.options ? this.state.options.states : [],
				convertToVal: it => it.id , convertToStr: it => it.title },
		];
	}

	saveEntity(entity) {
		this.refineComboBox( "parent", entity );
		super.saveEntity( entity )
			.then( res => this.refreshOptions() );
	}


	convertToTableRow( data ) {
		return (
			<tr key={"item_" + data.id} className={data.deleted?"deleted-row":""}>
				<td>{data.id}</td>
				<td>{data.title}</td>
				<td>{data.parentTitle}</td>
				<td>
					{this.createControleIcons(data)}
				</td>
			</tr>
		);
	}

}
