package ir.fassih.workshopautomation.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserInfo {
	
	private boolean locked = false;
	private boolean expired = false;
	private boolean enabled = true;


}
