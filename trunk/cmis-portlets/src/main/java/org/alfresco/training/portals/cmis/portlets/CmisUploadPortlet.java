package org.alfresco.training.portals.cmis.portlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.lang3.StringUtils;

/**
 * This portlet shows how to implement a standard upload feature using OpenCMIS 
 * against any CMIS-compliant repository
 * 
 * @author Piergiorgio Lucidi
 * 
 */
public class CmisUploadPortlet extends GenericPortlet {

	private CmisClientConfig cmisClientConfig;
	private Session session = null;
	private Document currentDocument = null;
	
	@Override
	public void init() throws PortletException {
		cmisClientConfig = new CmisClientConfig();
	}
	
	@RenderMode(name = "view")
    public void display(RenderRequest request, RenderResponse response)
	    throws PortletException, IOException {
		response.setContentType("text/html");
		
		String forward = request.getParameter(CmisPortletConstants.FORWARD_PARAM);
		String forwardJsp = StringUtils.EMPTY;
		
		if(StringUtils.isEmpty(forward) 
				|| forward.equals(CmisPortletConstants.UPLOAD_VIEW)){
			forwardJsp = CmisPortletConstants.UPLOAD_FORM_JSP_PATH;
		} else if(forward.equals(CmisPortletConstants.EDIT_OK_VIEW)){
			forwardJsp = CmisPortletConstants.CONFIG_OK_JSP_PATH;
			request.setAttribute(CmisClientConfig.ATTRIBUTE_NAME, cmisClientConfig);
		} else if(forward.equals(CmisPortletConstants.UPLOAD_OK_VIEW)){
			forwardJsp = CmisPortletConstants.UPLOAD_OK_JSP_PATH;
			String downloadUrl = request.getParameter(CmisPortletConstants.UPLOAD_OK_DOWNLOAD_URL_PARAM);
			request.setAttribute(CmisPortletConstants.UPLOAD_OK_DOWNLOAD_URL_PARAM, downloadUrl);
			request.setAttribute(CmisPortletConstants.DOC_PARAM, currentDocument);
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
	
	@ProcessAction(name = "uploadFormAction")
	public void uploadFormAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.UPLOAD_VIEW);
		
	}
	
	@SuppressWarnings("rawtypes")
	@ProcessAction(name = "uploadAction")
	public void uploadAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
	    
		//retrieve a CMIS session
		if(session==null){	
			cmisClientConfig = CmisUtils.getConfigFromPrefs(request);
			if(cmisClientConfig.isEmpty()){
				editFormAction(request, response);
			} else {
				session = CmisUtils.getCmisSession(cmisClientConfig);
			}
		} else {
		
			//get content information from the upload form
			
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			PortletFileUpload portletFileUpload = new PortletFileUpload(
					diskFileItemFactory);
			try {
				
				List fileItemList = portletFileUpload.parseRequest(request);
				Iterator fileIt = fileItemList.iterator();
				Document document = null;
				while (fileIt.hasNext()) {
					FileItem fileItem = (FileItem) fileIt.next();
					if(!fileItem.isFormField()){
						document = CmisUtils.createDocument(session, fileItem);
						break;
					}
				}
				
				currentDocument = document;
				response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.UPLOAD_OK_VIEW);
				
				if(document!=null){
					String downloadUrl = CmisUtils.getDocumentURL(session, document);
					response.setRenderParameter(CmisPortletConstants.UPLOAD_OK_DOWNLOAD_URL_PARAM, downloadUrl);
				}
				
			} catch (Exception e){
				response.setRenderParameter(CmisPortletConstants.ERROR_MESSAGE_PARAM, e.getMessage());
				response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.ERROR_VIEW);
			}
			
			response.setRenderParameter(
					CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.UPLOAD_OK_VIEW);
		}
	}
	
	@ProcessAction(name = "returnHomeAction")
	public void returnHomeAction(ActionRequest request, ActionResponse response) 
			throws PortletException {
		response.setRenderParameter(CmisPortletConstants.FORWARD_PARAM, CmisPortletConstants.UPLOAD_VIEW);
	}

}
