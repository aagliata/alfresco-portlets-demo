package org.alfresco.training.portals.standard.portlets;

/**
 * Interface for exposing some constants related to the flow of the portlet
 * @author Piergiorgio Lucidi
 *
 */
public interface HelloWorldPortletConstants {

	public final static String WELCOME_MESSAGE_PARAM = "welcomeMessage";
	public final static String ERROR_MESSAGE_PARAM = "errorMessage";
	public final static String HOME_VO_PARAM = "homeVO";
	
	public final static String HOME_VIEW = "home";
	public final static String FORWARD_PARAM = "forward";
	public final static String EDIT_VIEW = "edit";
	public final static String EDIT_OK_VIEW = "editOk";
	public final static String EDIT_ACTION = "editAction";
	
	public final static String ERROR_VIEW = "error";
	
	public static final String CONFIG_FORM_JSP_PATH = "/jsp/config/configForm.jsp";
	public static final String CONFIG_OK_JSP_PATH = "/jsp/config/configOk.jsp";
	
	public static final String HOME_VIEW_JSP_PATH = "/jsp/home/homeView.jsp";
	
	public static final String ERROR_JSP_PATH = "/jsp/error.jsp";
	
	
}
