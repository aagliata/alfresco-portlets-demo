package org.alfresco.training.portals.webservices.portlets;

import org.apache.commons.lang3.StringUtils;

/**
 * This is the JavaBean dedicated to manage the Web Services Client configuration
 * @author Piergiorgio Lucidi
 *
 */
public class WsClientConfig {

	public static final String ATTRIBUTE_NAME = "wsClientConfig";
	
	private String endpoint = StringUtils.EMPTY;
	private String username = StringUtils.EMPTY;
	private String password = StringUtils.EMPTY;
	
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isEmpty(){
		if(StringUtils.isEmpty(endpoint)
				&& StringUtils.isEmpty(username)
				&& StringUtils.isEmpty(password)){
			return true;
		} else
			return false;
	}

}