package org.alfresco.training.portals.standard.portlets.vo;

import org.apache.commons.lang3.StringUtils;

public class HomeVO {

	private String welcomeMessage = StringUtils.EMPTY;
	private String username = StringUtils.EMPTY;
	
	public String getWelcomeMessage() {
		return welcomeMessage;
	}
	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
		
}