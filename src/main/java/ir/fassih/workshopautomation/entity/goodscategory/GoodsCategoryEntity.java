package ir.fassih.workshopautomation.entity.goodscategory;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import groovy.transform.EqualsAndHashCode;
import ir.fassih.workshopautomation.entity.core.AbstractBaseEntity;
import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import lombok.Data;

@Data
@Table(name="DASH_GOODS_CATEGORY")
@MappedSuperclass
@EqualsAndHashCode(callSuper=true)
public class GoodsCategoryEntity extends AbstractBaseEntity implements LogicallyDeletable {

	@Column(name = "TITLE")
	private String title;

	@Column(name = "DELETED")
	private boolean deleted = false;

}
