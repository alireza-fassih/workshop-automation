package ir.fassih.workshopautomation.security;

import org.springframework.security.core.GrantedAuthority;

public enum PortalRole implements GrantedAuthority {
	PROGRAMMER, ADMIN, USER;

	@Override
	public String getAuthority() {
		return name();
	}

}
