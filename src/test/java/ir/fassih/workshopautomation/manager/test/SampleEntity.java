package ir.fassih.workshopautomation.manager.test;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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
	@GeneratedValue(
			strategy= GenerationType.AUTO,
			generator="native"
	)
	@GenericGenerator(
			name = "native",
			strategy = "native"
	)
	private Long id;
	
	
	@Column(name="USER_NAME")
	private String username;
	
	@Column(name="AMOUNT")
	private Long amount;

	@Builder.Default
	@Column(name="CREATION_TIME")
	@Temporal(TemporalType.DATE)
	private Date creationTime = new Date();
	
}
