import React from 'react';
import AbstractList from '../Core/AbstractList'
import moment from 'jalali-moment'
import uuidv4 from 'uuid/v4';
import  { Button, Table, Input, Label,
    Modal ,ModalHeader, ModalBody, ModalFooter, FormGroup }  from 'reactstrap';


const ButtonStyle = {
    marginLeft: "10px"
}

export default class PriceList extends AbstractList {



	getEntityName() {
		return "price-list";
	}

	getSearchPanel() {
		return [
			{ id: "LIKE:title", type: 'text', label: 'عنوان' },
			{ id: "GE:createDate", type: 'date', label: 'زمان ایجاد از' },
			{ id: "LE:createDate", type: 'date', label: 'زمان ایجاد تا' }
		]
	}

	getTableHead() {
		return [ "کد", "عنوان", ""];
	}


  getCreatePanel() {
    let uid = uuidv4();
		return [
			{ id: "title", type: 'text', label: 'عنوان' },
      { id: "contentId", type: 'file', label: 'فایل', uid:  uid, value: uid }
		];
  }


	convertToTableRow( data ) {
		return (
			<tr key={"item_" + data.id} >
				<td>{data.id}</td>
				<td>{data.title}</td>
				<td>
					<a style={ButtonStyle} href="javascript:void(0);" onClick={this.deleteEntity.bind(this, data.id)}>
						<i className="fa fa-remove"  aria-hidden="true"></i>
					</a>
				</td>
			</tr>
		);
	}

}
