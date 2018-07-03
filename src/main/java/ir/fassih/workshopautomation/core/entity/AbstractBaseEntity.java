package ir.fassih.workshopautomation.core.entity;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;

@Data
public abstract class AbstractBaseEntity {

	@Id
	@Column(name="ID")
	private Long id;
	
}
