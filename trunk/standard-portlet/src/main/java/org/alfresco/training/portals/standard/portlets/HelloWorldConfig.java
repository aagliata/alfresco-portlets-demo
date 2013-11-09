package org.alfresco.training.portals.standard.portlets;

import org.apache.commons.lang3.StringUtils;

/**
 * This is the JavaBean dedicated to manage the Hello World portlet configuration
 * @author Piergiorgio Lucidi
 *
 */
public class HelloWorldConfig {

	public static final String ATTRIBUTE_NAME = "helloWorldConfig";
	
	private String welcomeMessage = StringUtils.EMPTY;
	
	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	public boolean isEmpty(){
		if(StringUtils.isEmpty(welcomeMessage)){
			return true;
		} else
			return false;
	}
	
}
