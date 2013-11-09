package org.alfresco.training.portals.webservices.portlets;

/**
 * Interface for exposing some constants related to the params and the flow of the portlet
 * @author Piergiorgio Lucidi
 *
 */
public interface WsClientConstants {

	public final static String ENDPOINT_PARAM = "welcomeMessage";
	public final static String USERNAME_PARAM = "username";
	public final static String PASSWORD_PARAM = "password";
	
	public final static String KEYWORD_PARAM = "keyword";
	public final static String CONFIGURE_PARAM = "configure";
	
	public final static String ERROR_MESSAGE_PARAM = "errorMessage";
	public final static String HOME_VO_PARAM = "homeVO";
	public final static String RESULTS_PARAM = "resultsList";
	
	public final static String RESULTS_VIEW = "results";
	public final static String HOME_VIEW = "home";
	public final static String FORWARD_PARAM = "forward";
	public final static String EDIT_VIEW = "edit";
	public final static String EDIT_OK_VIEW = "editOk";
	public final static String EDIT_ACTION = "editAction";
	
	public final static String ERROR_VIEW = "error";
	
	public static final String RESULTS_JSP_PATH = "/jsp/search/resultsView.jsp";
	
	public static final String CONFIG_FORM_JSP_PATH = "/jsp/config/configForm.jsp";
	public static final String CONFIG_OK_JSP_PATH = "/jsp/config/configOk.jsp";
	
	public static final String HOME_VIEW_JSP_PATH = "/jsp/home/homeView.jsp";
	
	public static final String ERROR_JSP_PATH = "/jsp/error.jsp";
	
	
}
