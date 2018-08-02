package ir.fassih.workshopautomation.manager.test;

import org.springframework.stereotype.Repository;

import ir.fassih.workshopautomation.repository.AbstractRepository;

@Repository
public interface SampleRepository extends AbstractRepository<SampleEntity, Long> {

}
