package ir.fassih.workshopautomation.entity.rowmaterialcategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ir.fassih.workshopautomation.core.entity.LogicallyDeletable;
import lombok.Data;

@Data
@Entity
@Table(name="DASH_ROW_MATERIAL_CATEGORY")
public class RowMaterialCategoryEntity implements LogicallyDeletable {

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="DELETED")
	private boolean deleted = false;
	
}
