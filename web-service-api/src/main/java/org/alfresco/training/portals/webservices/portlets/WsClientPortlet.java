package org.alfresco.training.portals.webservices.portlets;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
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

import org.alfresco.training.portals.webservices.portlets.vo.ResultVO;
import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Query;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

/**
 * This portlet shows how to implement a standard JSR-286 portlet
 * that invoke remote calls against an Alfresco repository using the 
 * Alfresco Web Services API
 * 
 * @author Piergiorgio Lucidi
 * 
 */
public class WsClientPortlet extends GenericPortlet {

	private WsClientConfig wsClientConfig;
	private List<ResultVO> resultsList = null;
	
	@Override
	protected void doHeaders(RenderRequest request, RenderResponse response) {
		super.doHeaders(request, response);
        Element jsLink = response.createElement("script");
        jsLink.setAttribute("type", "text/javascript");
        jsLink.setAttribute("src", response.encodeURL((request.getContextPath() + "/js/paging.js")));
        response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, jsLink);
        
        Element cssElement = response.createElement("link");
		cssElement.setAttribute("href", response.encodeURL((request.getContextPath() + "/css/paging.css")));
		cssElement.setAttribute("rel", "stylesheet");
		cssElement.setAttribute("type", "text/css");
		response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, cssElement);
	}
	
	@Override
	public void init() throws PortletException {
		wsClientConfig = new WsClientConfig();
	}
	
	@Override
	protected void doEdit(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		if(wsClientConfig.isEmpty()){
			wsClientConfig = Utils.getConfigFromPrefs(request);
		}
		
		String forward = request.getParameter(WsClientConstants.FORWARD_PARAM);
		String forwardJsp = WsClientConstants.CONFIG_FORM_JSP_PATH;
		
		if(StringUtils.isEmpty(forward)){
			forwardJsp = WsClientConstants.CONFIG_FORM_JSP_PATH;
		} else if(forward.equals(WsClientConstants.EDIT_OK_VIEW)){
			forwardJsp = WsClientConstants.CONFIG_OK_JSP_PATH;
		}
		
		request.setAttribute(WsClientConfig.ATTRIBUTE_NAME, wsClientConfig);
		
		PortletRequestDispatcher requestDispacther = getPortletContext().getRequestDispatcher(forwardJsp);
		requestDispacther.include(request, response);
		
	}
	
	@RenderMode(name = "view")
    public void display(RenderRequest request, RenderResponse response)
	    throws PortletException, IOException {
		response.setContentType("text/html");
		
		if(wsClientConfig.isEmpty()){
			wsClientConfig = Utils.getConfigFromPrefs(request);
			if(wsClientConfig.isEmpty()){
				request.setAttribute(WsClientConstants.CONFIGURE_PARAM, Boolean.TRUE);
			}
		}
		
		String forward = request.getParameter(WsClientConstants.FORWARD_PARAM);
		String forwardJsp = WsClientConstants.HOME_VIEW_JSP_PATH;
		
		if(StringUtils.isNotEmpty(forward)){
			if(forward.equals(WsClientConstants.ERROR_VIEW)){
				forwardJsp = WsClientConstants.ERROR_JSP_PATH;
				String errorMessage = (String) request.getParameter(WsClientConstants.ERROR_MESSAGE_PARAM);
				request.setAttribute(WsClientConstants.ERROR_MESSAGE_PARAM, errorMessage);
			} else if(forward.equals(WsClientConstants.RESULTS_VIEW)){
				forwardJsp = WsClientConstants.RESULTS_JSP_PATH;
				String keyword = request.getParameter(WsClientConstants.KEYWORD_PARAM);
				request.setAttribute(WsClientConstants.RESULTS_PARAM, resultsList);
				request.setAttribute(WsClientConstants.KEYWORD_PARAM, keyword);
			}
		}
		
		PortletRequestDispatcher requestDispacther = getPortletContext().getRequestDispatcher(forwardJsp);
		requestDispacther.include(request, response);
    }

	@ProcessAction(name = "editAction")
	public void editAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		
		String endpoint = request.getParameter(WsClientConstants.ENDPOINT_PARAM);
		String username = request.getParameter(WsClientConstants.USERNAME_PARAM);
		String password = request.getParameter(WsClientConstants.PASSWORD_PARAM);
		
		//configure the Web Service client
		wsClientConfig.setEndpoint(endpoint);
		wsClientConfig.setUsername(username);
		wsClientConfig.setPassword(password);
		
		PortletPreferences prefs = request.getPreferences();
		prefs.setValue(WsClientConstants.ENDPOINT_PARAM, endpoint);
		prefs.setValue(WsClientConstants.USERNAME_PARAM, username);
		prefs.setValue(WsClientConstants.PASSWORD_PARAM, password);
		
		try {
			prefs.store();
		} catch (IOException e) {
			response.setRenderParameter(WsClientConstants.ERROR_MESSAGE_PARAM, e.getMessage());
			response.setRenderParameter(WsClientConstants.FORWARD_PARAM, WsClientConstants.ERROR_VIEW);
		}
		
		response.setRenderParameter(WsClientConstants.FORWARD_PARAM, WsClientConstants.EDIT_OK_VIEW);
		
	}
	
	@ProcessAction(name = "returnAction")
	public void returnAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		response.setRenderParameter(WsClientConstants.FORWARD_PARAM, StringUtils.EMPTY);
	}
	
	@ProcessAction(name = "searchAction")
	public void searchAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		
		String keyword = request.getParameter(WsClientConstants.KEYWORD_PARAM);
		String luceneQuery = "TEXT:\""+keyword+"\"";

		try{
			AuthenticationUtils.startSession(wsClientConfig.getUsername(), wsClientConfig.getPassword());
			Store storeRef = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
			Query query = new Query(Constants.QUERY_LANG_LUCENE, luceneQuery);
			RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
			QueryResult queryResult = repositoryService.query(storeRef, query, false);
			ResultSet resultSet = queryResult.getResultSet();
	        ResultSetRow[] rows = resultSet.getRows();
	        String ticket = AuthenticationUtils.getTicket();
	        
	        if (rows == null) {
	            resultsList = null;
	        } else {
	        	resultsList = new ArrayList<ResultVO>();
	        	for(int i=0; i<rows.length; i++){
	        		ResultSetRow result = rows[i];
	        		NamedValue[] props = result.getColumns();
	        		String name = StringUtils.EMPTY;
	        		
	        		for (NamedValue prop : props) {
	        			if(Constants.PROP_NAME.equals(prop.getName())){
	        				name = prop.getValue();
	        			}
	        		}
	        		
	        		String id = result.getNode().getId();
	        		String downloadUrl = 
	        				StringUtils.replace(wsClientConfig.getEndpoint(), "/api", "/d/d/workspace/SpacesStore/") +
	        				id + "/"+name+"?ticket="+ticket;
	        		
	        		ResultVO resultVO = new ResultVO();
	        		resultVO.setDownloadUrl(downloadUrl);
	        		resultVO.setName(name);
	        		resultsList.add(resultVO);
	        	}
	        }
	        
		} catch (AuthenticationFault e) {
			response.setRenderParameter(WsClientConstants.ERROR_MESSAGE_PARAM, e.getMessage());
			response.setRenderParameter(WsClientConstants.FORWARD_PARAM, WsClientConstants.ERROR_VIEW);
		} catch (RuntimeException e) {
			response.setRenderParameter(WsClientConstants.ERROR_MESSAGE_PARAM, e.getMessage());
			response.setRenderParameter(WsClientConstants.FORWARD_PARAM, WsClientConstants.ERROR_VIEW);
		} catch (RepositoryFault e) {
			response.setRenderParameter(WsClientConstants.ERROR_MESSAGE_PARAM, e.getMessage());
			response.setRenderParameter(WsClientConstants.FORWARD_PARAM, WsClientConstants.ERROR_VIEW);
		} catch (RemoteException e) {
			response.setRenderParameter(WsClientConstants.ERROR_MESSAGE_PARAM, e.getMessage());
			response.setRenderParameter(WsClientConstants.FORWARD_PARAM, WsClientConstants.ERROR_VIEW);
		} finally {
			//AuthenticationUtils.endSession();
		}

		response.setRenderParameter(WsClientConstants.KEYWORD_PARAM, keyword);
		response.setRenderParameter(WsClientConstants.FORWARD_PARAM, WsClientConstants.RESULTS_VIEW);
	}
	
	@Override
	public void destroy() {
		AuthenticationUtils.endSession();
	}

}
