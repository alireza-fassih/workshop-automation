package ir.fassih.workshopautomation.entity.goodsmetadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import groovy.transform.EqualsAndHashCode;
import ir.fassih.workshopautomation.core.entity.AbstractBaseEntity;
import ir.fassih.workshopautomation.core.entity.LogicallyDeletable;
import ir.fassih.workshopautomation.entity.goodscategory.GoodsCategoryEntity;
import lombok.Data;

@Data
@Entity
@Table(name="DASH_GOODS_METADATA")
@MappedSuperclass
@EqualsAndHashCode(callSuper=true)
public class GoodsMetadataEntity extends AbstractBaseEntity implements LogicallyDeletable {

	@Column(name = "TITLE")
	private String title;

	@Column(name = "DELETED")
	private boolean deleted = false;
	
}
