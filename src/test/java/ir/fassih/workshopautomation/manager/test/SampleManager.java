package ir.fassih.workshopautomation.manager.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ir.fassih.workshopautomation.manager.AbstractManager;

@Component
public class SampleManager extends AbstractManager<SampleEntity,  Long> {

	@Autowired
	public SampleManager(SampleRepository repository) {
		super(repository, SampleEntity.class);
		log.info( "WOOOOOOOOOOOOOOOOOOOOOOW" );
	}
	
}