package ir.fassih.workshopautomation.manager.test;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Table(name="TEST_SAMPLE")
@Builder
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
public class SampleEntity {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	@Column(name="USER_NAME")
	private String username;
	
	@Column(name="AMOUNT")
	private Long amount;
	
	@Column(name="CREATION_TIME")
	private Date creationTime;
	
}
