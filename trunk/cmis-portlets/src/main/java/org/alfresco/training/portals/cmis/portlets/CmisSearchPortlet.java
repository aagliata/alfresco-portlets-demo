package org.alfresco.training.portals.cmis.portlets;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.ProcessAction;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.alfresco.training.portals.cmis.portlets.vo.DocumentVO;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;


/**
 * This portlet shows how to implement a standard search feature using OpenCMIS 
 * against any CMIS-compliant repository
 * 
 * @author Piergiorgio Lucidi
 *
 */
public class CmisSearchPortlet extends GenericPortlet {

	private CmisClientConfig cmisClientConfig;
	private Session session = null;
	private List<DocumentVO> searchResults = null;
	
	@Override
	public void init() throws PortletException {
		cmisClientConfig = new CmisClientConfig();
	}
	
	@Override
	protected void doHeaders(RenderRequest request, RenderResponse response) {
		super.doHeaders(request, response);
        Element jsLink = response.createElement("script");
        jsLink.setAttribute("type", "text/javascript");
        jsLink.setAttribute("src", response.encodeURL((request.getContextPath() + "/js/paging.js")));
        //jsLink.setTextContent(" ");
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, jsLink);
        
        Element cssElement = response.createElement("link");
		cssElement.setAttribute("href", response.encodeURL((request.getContextPath() + "/css/paging.css")));
		cssElement.setAttribute("rel", "stylesheet");
		cssElement.setAttribute("type", "text/css");
		response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, cssElement);
	}
	
	@RenderMode(name = "view")
    public void display(RenderRequest request, RenderResponse response)
	    throws PortletException, IOException {
		response.setContentType("text/html");
		
		String forward = request.getParameter(CmisPortletConstants.FORWARD_PARAM);
		String forwardJsp = StringUtils.EMPTY;
		
		if(StringUtils.isEmpty(forward) 
				|| forward.equals(CmisPortletConstants.SEARCH_FORM_VIEW)){
			forwardJsp = CmisPortletConstants.SEARCH_FORM_JSP_PATH;
		} else if(forward.equals(CmisPortletConstants.EDIT_OK_VIEW)){
			forwardJsp = CmisPortletConstants.CONFIG_OK_JSP_PATH;
			request.setAttribute(CmisClientConfig.ATTRIBUTE_NAME, cmisClientConfig);
		} else if(forward.equals(CmisPortletConstants.SEARCH_RESULTS_VIEW)){
			forwardJsp = CmisPortletConstants.SEARCH_RESULTS_JSP_PATH;
			String keyword = request.getParameter(CmisPortletConstants.SEARCH_KEYWORD_PARAM);
			request.setAttribute(CmisPortletConstants.SEARCH_KEYWORD_PARAM, keyword);
			request.setAttribute(CmisPortletConstants.SEARCH_RESULTS_PARAM, searchResults);
		} else if(forward.equals(CmisPortletConstants.EDIT_VIEW)){
			forwardJsp = CmisPortletConstants.CONFIG_FORM_JSP_PATH;
			request.setAttribute(CmisClientConfig.ATTRIBUTE_NAME, cmisClientConfig);
		} else if(forward.equals(CmisPortletConstants.ERROR_VIEW)){
			forwardJsp = CmisPortletConstants.ERROR_JSP_PATH;
			String errorMessage = (String) request.getParameter(CmisPortletConstants.ERROR_MESSAGE_PARAM);
			request.setAttribute(CmisPortletConstants.ERROR_MESSAGE_PARAM, errorMessage);
		}
		
		PortletRequestDispatcher requestDispacther = getPortletContext()
				.getRequestDispatcher(forwardJsp);
		requestDispacther.include(request, response);
    }
	
	@ProcessAction(name = "editFormAction")
	public void editFormAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		if(cmisClientConfig.isEmpty()){
			//try to get configuration from preferences
			cmisClientConfig = CmisUtils.getConfigFromPrefs(request);
		}
		request.setAttribute(CmisClientConfig.ATTRIBUTE_NAME, cmisClientConfig);
		response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.EDIT_VIEW);	
	}

	@ProcessAction(name = "editAction")
	public void editAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		
		String endpoint = request.getParameter(CmisPortletConstants.ENDPOINT_PARAM);
		String username = request.getParameter(CmisPortletConstants.USERNAME_PARAM);
		String password = request.getParameter(CmisPortletConstants.PASSWORD_PARAM);
		String repositoryId = request.getParameter(CmisPortletConstants.REPO_ID_PARAM);
		String binding = request.getParameter(CmisPortletConstants.BINDING_PARAM);
		
		//configure the CMIS client
		cmisClientConfig.setEndpoint(endpoint);
		cmisClientConfig.setUsername(username);
		cmisClientConfig.setPassword(password);
		cmisClientConfig.setRepositoryId(repositoryId);
		cmisClientConfig.setBinding(binding);
		
		PortletPreferences prefs = request.getPreferences();
		prefs.setValue(CmisPortletConstants.ENDPOINT_PARAM, endpoint);
		prefs.setValue(CmisPortletConstants.USERNAME_PARAM, username);
		prefs.setValue(CmisPortletConstants.PASSWORD_PARAM, password);
		prefs.setValue(CmisPortletConstants.REPO_ID_PARAM, repositoryId);
		prefs.setValue(CmisPortletConstants.BINDING_PARAM, binding);
		
		try {
			prefs.store();
		} catch (IOException e) {
			response.setRenderParameter(CmisPortletConstants.ERROR_MESSAGE_PARAM, e.getMessage());
			response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.ERROR_VIEW);
		}
		
		//setup CMIS client
		try {
			session = CmisUtils.getCmisSession(cmisClientConfig);
			request.setAttribute(CmisClientConfig.ATTRIBUTE_NAME, cmisClientConfig);
			response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.EDIT_OK_VIEW);
		} catch (RuntimeException e) {
			response.setRenderParameter(CmisPortletConstants.ERROR_MESSAGE_PARAM, e.getMessage());
			response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.ERROR_VIEW);
		}
	}
	
	@ProcessAction(name = "searchFormAction")
	public void searchFormAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.SEARCH_FORM_VIEW);
	}
	
	@ProcessAction(name = "searchResultsAction")
	public void searchResultsAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		String keyword = (String) request.getParameter(CmisPortletConstants.SEARCH_KEYWORD_PARAM);
		if(session==null){
			cmisClientConfig = CmisUtils.getConfigFromPrefs(request);
			if(cmisClientConfig.isEmpty()){
				editFormAction(request, response);
			} else {
				session = CmisUtils.getCmisSession(cmisClientConfig);
			}
		}
		
		if(session!=null){
			searchResults = CmisUtils.fullTextSearch(session, keyword);
			request.setAttribute(CmisPortletConstants.SEARCH_RESULTS_PARAM, searchResults);
			response.setRenderParameter(CmisPortletConstants.SEARCH_KEYWORD_PARAM, keyword);
			response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.SEARCH_RESULTS_VIEW);
		}
		
		if(StringUtils.isEmpty(keyword)){
			response.setRenderParameter(CmisPortletConstants.ERROR_MESSAGE_PARAM, "Please insert a keyword to execute the full text search");
			response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.ERROR_VIEW);
		}
	}
	
	@ProcessAction(name = "returnHomeAction")
	public void returnHomeAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.SEARCH_FORM_VIEW);
	}
	
}
