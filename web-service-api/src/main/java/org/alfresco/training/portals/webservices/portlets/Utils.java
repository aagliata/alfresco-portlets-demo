package org.alfresco.training.portals.webservices.portlets;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * This is a utility class for managing generic methods used many times inside the portlet
 * @author Piergiorgio Lucidi
 *
 */
public class Utils {
	
	public static WsClientConfig getConfigFromPrefs(PortletRequest request){
		PortletPreferences prefs = request.getPreferences();
		WsClientConfig wsClientConfig = new WsClientConfig();
		wsClientConfig.setEndpoint(prefs.getValue(WsClientConstants.ENDPOINT_PARAM, StringUtils.EMPTY));
		wsClientConfig.setUsername(prefs.getValue(WsClientConstants.USERNAME_PARAM, StringUtils.EMPTY));
		wsClientConfig.setPassword(prefs.getValue(WsClientConstants.PASSWORD_PARAM, StringUtils.EMPTY));
		return wsClientConfig;
	}
	
}