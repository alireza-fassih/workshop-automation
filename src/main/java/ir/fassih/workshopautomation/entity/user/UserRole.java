package ir.fassih.workshopautomation.entity.user;

import ir.fassih.workshopautomation.security.PortalRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

	private PortalRole role;
	
	public String getAuthority() {
		if( role == null ) {
			log.error( "role should not be null" );
			throw new IllegalStateException("role should not be null");
		}
		return role.name();
	}

	
}
