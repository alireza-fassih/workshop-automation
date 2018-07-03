package ir.fassih.workshopautomation.entity.rawmaterialcategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import groovy.transform.EqualsAndHashCode;
import ir.fassih.workshopautomation.core.entity.AbstractBaseEntity;
import ir.fassih.workshopautomation.core.entity.LogicallyDeletable;
import lombok.Data;

@Data
@Table(name="DASH_RAW_MATERIAL_CATEGORY")
@Entity
@MappedSuperclass
@EqualsAndHashCode(callSuper=true)
public class RawMaterialCategoryEntity extends AbstractBaseEntity 
									   implements LogicallyDeletable {
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="DELETED")
	private boolean deleted = false;
	
}
