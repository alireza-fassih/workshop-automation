package ir.fassih.workshopautomation.entity.goodsmetadata;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import groovy.transform.EqualsAndHashCode;
import ir.fassih.workshopautomation.entity.core.AbstractBaseEntity;
import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import lombok.Data;

@Data
@Table(name="DASH_GOODS_METADATA")
@MappedSuperclass
@EqualsAndHashCode(callSuper=true)
public class GoodsMetadataEntity extends AbstractBaseEntity implements LogicallyDeletable {

	@Column(name = "TITLE")
	private String title;

	@Column(name = "DELETED")
	private boolean deleted = false;
	
}
