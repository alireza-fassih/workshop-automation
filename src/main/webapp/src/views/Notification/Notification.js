import React from 'react';
import AbstractList from '../Core/AbstractList'


export default class Notification extends AbstractList {


	getEntityName() {
		return "notifications";
	}

	getSearchPanel() {
		return [
			{ id: "GE:createDate", type: 'date', label: 'زمان ایجاد از' },
            { id: "LE:createDate", type: 'date', label: 'زمان ایجاد تا' },
            { id: "EQ:deleted", type: 'combo', label: 'حذف شده', value: false,
				values: [ { id: true, title: 'باشد' }, { id: false, title: 'نباشد' }],
				convertToVal: it => it.id, convertToStr: it => it.title }
		]
	}

	getTableHead() {
		return ["شناسه", "محتوا", "حذف شده", ""];
	}

	getCreatePanel() {
		return [
			{ id: "content", type: 'textArea', label: 'محتوا' }
		];
	}

	convertToTableRow( data ) {
        return (
			<tr key={"item_" + data.id} className={data.deleted?"deleted-row":""}>
				<td>{data.id}</td>
				<td>{ this.elipseString(data.content, 20)}</td>
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
