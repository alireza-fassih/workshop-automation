package ir.fassih.workshopautomation.entity.goodsrawmaterial;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import groovy.transform.EqualsAndHashCode;
import ir.fassih.workshopautomation.core.entity.AbstractBaseEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import lombok.Data;

@Data
@Entity
@Table(name="DASH_GOODS_RAW_MATERIAL")
@MappedSuperclass
@EqualsAndHashCode(callSuper=true)
public class GoodsRawMaterialEntity extends AbstractBaseEntity  {

	private GoodsRawMaterialEntity goods;
	private RawMaterialEntity material;
	private RawMaterialCategoryEntity category;
	private boolean changeAble;
	private Float importFactor;
}
