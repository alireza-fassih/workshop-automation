package ir.fassih.workshopautomation.security;

import org.springframework.security.core.GrantedAuthority;

public enum PortalRole implements GrantedAuthority {
	;

	@Override
	public String getAuthority() {
		return name();
	}

}
