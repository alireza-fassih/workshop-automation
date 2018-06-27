package ir.fassih.workshopautomation.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class UserManager implements UserDetailsService {

	private UserRepository repository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = repository.findByUsername(username);
		if( user == null ) {
			throw new UsernameNotFoundException( username + " not found" );
		} 
		return user;
	}

}
