package ir.fassih.workshopautomation.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractRepository<T, I extends Serializable> extends CrudRepository<T, I>, JpaSpecificationExecutor<T> {

}
