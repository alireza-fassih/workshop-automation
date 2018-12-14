package ir.fassih.workshopautomation.security;

import org.springframework.security.core.GrantedAuthority;

public enum PortalRole implements GrantedAuthority {
	PROGRAMMER, ADMIN, USER, VERIFIER;

	@Override
	public String getAuthority() {
		return name();
	}

}
