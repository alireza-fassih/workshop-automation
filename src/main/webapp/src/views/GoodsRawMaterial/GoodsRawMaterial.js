import React from 'react';
import AbstractList from '../Core/AbstractList'


class GoodsRawMaterial extends AbstractList {

	constructor( props ) {
		super( props );
		this.getCreatePanel = this.getCreatePanel.bind( this );
	}

	getEntityName() {
		return "goodsRawMaterial";
	}

	getSearchPanel() {
		return [
			{ id: "LIKE:title", type: 'text', label: 'عنوان' },
			{ id: "EQ:category.id", type: 'combo', label: 'نوع ماده اولیه',
				values: this.state.options ? this.state.options.categories : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "EQ:material.id", type: 'combo', label: 'ماده اولیه',
				values: this.state.options ? this.state.options.materials : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "EQ:goods.id", type: 'combo', label: 'کالا',
				values: this.state.options ? this.state.options.products : [],
				convertToVal: it => it.id, convertToStr: it => it.title },
			{ id: "EQ:deleted", type: 'combo', label: 'حذف شده', value: false,
				values: [ { id: true, title: 'باشد' }, { id: false, title: 'نباشد' }],
				convertToVal: it => it.id, convertToStr: it => it.title }
		]
	}

	getTableHead() {
		return ["شناسه", "عنوان" , "نوع", "ضریب تاثیر", "کلا", ""];
	}

	getCreatePanel() {
		return [
			{ id: "title", type: 'text', label: 'عنوان' },
			{ id: "importFactor", type: 'text', label: 'ضریب تاثیر' },
			this.createCombo( "category", 'نوع', "categories" ),
			this.createCombo( "material", 'ماده اولیه', "materials" ),
			this.createCombo( "goods", 'کالا', "products" ),
			
		];
	}

	saveEntity(entity) {
		this.refineComboBox("category", entity);
		this.refineComboBox("goods", entity);
		this.refineComboBox("material", entity);
		super.saveEntity( entity );
	}

	renderTitle( entity ) {
		return ( entity ? entity.title : '');
	}

	convertToTableRow( data ) {
		return (
			<tr key={"item_" + data.id} className={data.deleted?"deleted-row":""}>
				<td>{data.id}</td>
				<td>{data.title}</td>
				<td>{data.selectAble ? this.renderTitle( data.category ) : this.renderTitle( data.material ) }</td>
				<td>{data.importFactor}</td>
				<td>{data.goods ? data.goods.title: ""}</td>
				<td>
					{this.createControleIcons(data)}
				</td>
			</tr>
		);
	}

}


export default GoodsRawMaterial;