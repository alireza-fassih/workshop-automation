package ir.fassih.workshopautomation.entity.user;

import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.userdetails.UserDetails;

import ir.fassih.workshopautomation.core.datamanagment.AbstractJsonConverter;
import ir.fassih.workshopautomation.security.PortalRole;
import lombok.Data;

@Data
@Entity(name = "DASH_USER")
public class UserEntity implements UserDetails {

	private static final long serialVersionUID = 5681975715567546285L;
	
	

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="USER_NAME")
	private String username;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="INFO")
	@Convert(converter=UserInfoConverter.class)
	private UserInfo info;
	
	@Column(name="ROLES")
//	@Convert(converter=UserRolesConverter.class)
	private Set< PortalRole > authorities;
	

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

	
	
	public static class UserInfoConverter extends AbstractJsonConverter< UserInfo >{
		public UserInfoConverter() {
			super(UserInfo.class);
		}
	}

}
