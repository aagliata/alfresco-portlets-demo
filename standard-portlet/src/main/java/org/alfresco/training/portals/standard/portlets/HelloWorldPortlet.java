package org.alfresco.training.portals.standard.portlets;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.ProcessAction;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.alfresco.training.portals.standard.portlets.vo.HomeVO;
import org.apache.commons.lang3.StringUtils;

/**
 * This portlet shows how to implement a standard JSR-286 portlet
 * 
 * @author Piergiorgio Lucidi
 * 
 */
public class HelloWorldPortlet extends GenericPortlet {

	private HelloWorldConfig helloWorldConfig;
	
	@Override
	public void init() throws PortletException {
		helloWorldConfig = new HelloWorldConfig();
	}
	
	@Override
	protected void doEdit(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		if(helloWorldConfig.isEmpty()){
			helloWorldConfig = Utils.getConfigFromPrefs(request);
		}
		
		String forward = request.getParameter(HelloWorldPortletConstants.FORWARD_PARAM);
		String forwardJsp = HelloWorldPortletConstants.CONFIG_FORM_JSP_PATH;
		
		if(StringUtils.isEmpty(forward)){
			forwardJsp = HelloWorldPortletConstants.CONFIG_FORM_JSP_PATH;
			
		} else if(forward.equals(HelloWorldPortletConstants.EDIT_OK_VIEW)){
			forwardJsp = HelloWorldPortletConstants.CONFIG_OK_JSP_PATH;
		}
		
		request.setAttribute(HelloWorldConfig.ATTRIBUTE_NAME, helloWorldConfig);
		
		PortletRequestDispatcher requestDispacther = getPortletContext().getRequestDispatcher(forwardJsp);
		requestDispacther.include(request, response);
		
	}
	
	@RenderMode(name = "view")
    public void display(RenderRequest request, RenderResponse response)
	    throws PortletException, IOException {
		response.setContentType("text/html");
		
		if(helloWorldConfig.isEmpty()){
			helloWorldConfig = Utils.getConfigFromPrefs(request);
		}
		
		String forward = request.getParameter(HelloWorldPortletConstants.FORWARD_PARAM);
		String forwardJsp = HelloWorldPortletConstants.HOME_VIEW_JSP_PATH;
		
		if(StringUtils.isEmpty(forward) || !forward.equals(HelloWorldPortletConstants.ERROR_VIEW)){
			
			String welcomeMessage = helloWorldConfig.getWelcomeMessage();
			String username = request.getRemoteUser();
			
			HomeVO homeVO = new HomeVO();
			homeVO.setWelcomeMessage(welcomeMessage);
			homeVO.setUsername(username);
			
			request.setAttribute(HelloWorldPortletConstants.HOME_VO_PARAM, homeVO);
			
		} else {
			forwardJsp = HelloWorldPortletConstants.ERROR_JSP_PATH;
			String errorMessage = (String) request.getParameter(HelloWorldPortletConstants.ERROR_MESSAGE_PARAM);
			request.setAttribute(HelloWorldPortletConstants.ERROR_MESSAGE_PARAM, errorMessage);
		}
		
		PortletRequestDispatcher requestDispacther = getPortletContext().getRequestDispatcher(forwardJsp);
		requestDispacther.include(request, response);
    }

	@ProcessAction(name = "editAction")
	public void editAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		
		String welcomeMessage = request.getParameter(HelloWorldPortletConstants.WELCOME_MESSAGE_PARAM);
		
		//configure the welcome message
		helloWorldConfig.setWelcomeMessage(welcomeMessage);
		
		PortletPreferences prefs = request.getPreferences();
		prefs.setValue(HelloWorldPortletConstants.WELCOME_MESSAGE_PARAM, welcomeMessage);
		
		try {
			prefs.store();
		} catch (IOException e) {
			response.setRenderParameter(HelloWorldPortletConstants.ERROR_MESSAGE_PARAM, e.getMessage());
			response.setRenderParameter(HelloWorldPortletConstants.FORWARD_PARAM, HelloWorldPortletConstants.ERROR_VIEW);
		}
		
		response.setRenderParameter(HelloWorldPortletConstants.FORWARD_PARAM, HelloWorldPortletConstants.EDIT_OK_VIEW);
		
	}
	
	@ProcessAction(name = "returnEditAction")
	public void returnHomeAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		response.setRenderParameter(HelloWorldPortletConstants.FORWARD_PARAM, StringUtils.EMPTY);
	}

}
