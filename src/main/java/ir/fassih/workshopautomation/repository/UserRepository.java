package ir.fassih.workshopautomation.repository;

import org.springframework.stereotype.Repository;

import ir.fassih.workshopautomation.entity.user.UserEntity;

@Repository
public interface UserRepository extends AbstractRepository<UserEntity, Long> {

	public UserEntity findByUsername(String username);
	
}
