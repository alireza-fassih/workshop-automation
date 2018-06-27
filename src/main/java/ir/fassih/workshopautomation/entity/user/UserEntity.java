package ir.fassih.workshopautomation.entity.user;

import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;

public class UserEntity {

	
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="INFO")
//	@Convert(converter=UserInfoConverter.class)
	private UserInfo info;
	
	@Column(name="ROLES")
//	@Convert(converter=UserRolesConverter.class)
	private Set< UserRole > authorities;
	

	public boolean isAccountNonExpired() {
		return !Optional.ofNullable( info )
			.map( UserInfo::isExpired ).orElse( Boolean.FALSE );
	}


	public boolean isAccountNonLocked() {
		return !Optional.ofNullable( info )
			.map( UserInfo::isLocked ).orElse( Boolean.FALSE );
	}


	public boolean isCredentialsNonExpired() {
		return isAccountNonExpired();
	}


	public boolean isEnabled() {
		return Optional.ofNullable( info )
			.map( UserInfo::isEnabled ).orElse( Boolean.TRUE );
	}

	
}
