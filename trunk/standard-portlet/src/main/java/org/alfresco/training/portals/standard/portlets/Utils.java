package org.alfresco.training.portals.standard.portlets;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * This is a utility class for managing generic methods used many times inside the portlet
 * @author Piergiorgio Lucidi
 *
 */
public class Utils {
	
	public static HelloWorldConfig getConfigFromPrefs(PortletRequest request){
		PortletPreferences prefs = request.getPreferences();
		HelloWorldConfig helloWorldConfig = new HelloWorldConfig();
		helloWorldConfig.setWelcomeMessage(prefs.getValue(HelloWorldPortletConstants.WELCOME_MESSAGE_PARAM, StringUtils.EMPTY));
		return helloWorldConfig;
	}
	
}