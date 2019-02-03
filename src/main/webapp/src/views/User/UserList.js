import React from 'react';
import AbstractList from '../Core/AbstractList'

const ButtonStyle = {
    marginLeft: "10px"
}


class UserList extends AbstractList {

	constructor( props ) {
		super( props );

		this.getCreatePanel = this.getCreatePanel.bind( this );
	}

	getEntityName() {
		return "user";
	}

	getSearchPanel() {
		return [
			{ id: "EQ:username", type: 'text', label: 'نام کاربری' }
		]
	}

	getTableHead() {
		return ["شناسه", "نام کاربری" , ""];
	}

	getCreatePanel() {
    let value = ( this.state.data && this.state.data.priceList ? this.state.data.priceList.id : undefined );
		return [
			{ id: "username", type: 'text', label: 'نام کاربری' },
			{ id: "newPassword", type:'password', label: "گذرواژه" },
			{ id: "prisePercentage", type: "text", label: "ضریب محاسبه قیمت"},
			{ id: "authorities", type: "multiSelect", values: (this.state.options === undefined ? []: this.state.options.roles) ,label: "نقش" },
      { id: "priceList", type: "combo", label: 'لیست قیمت', value: value, values: this.state.options ? this.state.options.priceList : [],
				convertToVal: it => it.id , convertToStr: it => it.title }
		];
	}

  saveEntity(entity) {
    console.log(entity);
		this.refineComboBox( "priceList", entity );
		super.saveEntity( entity );
	}


	banUser( userId ) {
		this.rest.postCustom( userId + "/disable" )
			.then( resp => this.doSearch() );
	}

	activeUser( userId ) {
		this.rest.postCustom( userId + "/enable" )
			.then( resp => this.doSearch() );
	}

	convertToTableRow( data ) {
		return (
			<tr key={"item_" + data.id}>
				<td>{data.id}</td>
				<td>{data.username}</td>
				<td>
					<a style={ButtonStyle} href="javascript:void(0);"><i className="fa fa-ban" aria-hidden="true" hidden={!data.enabled} onClick={this.banUser.bind(this, data.id)}></i></a>
					<a style={ButtonStyle} href="javascript:void(0);"><i className="fa fa-check" aria-hidden="true" hidden={data.enabled} onClick={this.activeUser.bind(this, data.id)}></i></a>
					<a style={ButtonStyle} href="javascript:void(0);"><i className="fa fa-pencil" aria-hidden="true" onClick={this.editEntity.bind(this, data.id)}></i></a>
				</td>
			</tr>
		);
	}

}


export default UserList;
