package org.alfresco.training.portals.cmis.portlets;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;

import org.alfresco.training.portals.cmis.portlets.vo.DocumentVO;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.bindings.spi.atompub.AbstractAtomPubService;
import org.apache.chemistry.opencmis.client.bindings.spi.atompub.AtomPubParser;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;


public class CmisUtils {
	
	public static final String BINDING_ATOM_VALUE = "atom";
	public static final String BINDING_SOAP_VALUE = "soap";
	
	private static final String REPLACER = "?";
	private static final String CMIS_FULL_TEXT_QUERY = "SELECT cmis:objectId, cmis:name FROM cmis:document WHERE CONTAINS('"+REPLACER+"')";
	
	public static Session getCmisSession(CmisClientConfig cmisClientConfig) throws RuntimeException{
		try {
			SessionFactory factory = SessionFactoryImpl.newInstance();
			Map<String, String> parameters = new HashMap<String, String>();
	        // Create a session
	        parameters.clear();

	        // user credentials
	        parameters.put(SessionParameter.USER, cmisClientConfig.getUsername());
	        parameters.put(SessionParameter.PASSWORD, cmisClientConfig.getPassword());
	        
	        // connection settings
	        if(cmisClientConfig.getBinding().equals(BINDING_ATOM_VALUE)){
	          //AtomPub protocol
	          parameters.put(SessionParameter.ATOMPUB_URL, cmisClientConfig.getEndpoint());
	          parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
	        } else if(cmisClientConfig.getBinding().equals(BINDING_SOAP_VALUE)){
	          String endpoint = cmisClientConfig.getEndpoint();
	          //Web Services - SOAP - protocol
	          parameters.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());
	          parameters.put(SessionParameter.WEBSERVICES_ACL_SERVICE, endpoint+"/ACLService?wsdl");
	          parameters.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, endpoint+"/DiscoveryService?wsdl");
	          parameters.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, endpoint+"/MultiFilingService?wsdl");
	          parameters.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, endpoint+"/NavigationService?wsdl");
	          parameters.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, endpoint+"/ObjectService?wsdl");
	          parameters.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, endpoint+"/PolicyService?wsdl");
	          parameters.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, endpoint+"/RelationshipService?wsdl");
	          parameters.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, endpoint+"/RepositoryService?wsdl");
	          parameters.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, endpoint+"/VersioningService?wsdl");
	        }
	        // create session
	        if (StringUtils.isEmpty(cmisClientConfig.getRepositoryId())) {
	          // get a session from the first CMIS repository exposed
	          List<Repository> repos = null;
	          try {
	            repos = factory.getRepositories(parameters);
	            return repos.get(0).createSession();
	          } catch (Exception e) {
	        	  throw new RuntimeException("Error during the creation of the CMIS session", e);
	          }
	          
	        } else {
	          // get a session from a specific repository
	          parameters.put(SessionParameter.REPOSITORY_ID, cmisClientConfig.getRepositoryId());
	          try {
	            return factory.createSession(parameters);
	          } catch (Exception e) {
	        	  throw new RuntimeException("Error during the creation of the CMIS session", e);
	          }
	        }

	      } catch (Throwable e) {
	    	  throw new RuntimeException("Error during the creation of the CMIS session", e);
	      }
		
	}
	
	/**
	 * This method create a new document in the CMIS repository
	 * @param session
	 * @param fileItem
	 * @return the primary path reference for the new document
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public static Document createDocument(Session session, FileItem fileItem) throws IOException {
		String fileName = fileItem.getName();
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties.put(PropertyIds.NAME, fileName);

		// content
		InputStream is = fileItem.getInputStream();
		String mimetype = fileItem.getContentType();
		ContentStream contentStream = new ContentStreamImpl(fileName, BigInteger.valueOf(fileItem.getSize()), mimetype, is);

		// create a major version
		Folder parent = session.getRootFolder();
		Document document = parent.createDocument(properties, contentStream, VersioningState.MAJOR);
		return document;
	}
	
	/**
	 * This method invokes a search in the CMIS repository
	 * @param CMIS session
	 * @param CMIS query
	 * @return results list using OpenCMIS
	 */
	public static List<DocumentVO> fullTextSearch(Session session, String keyword){
		String statement = StringUtils.replace(CMIS_FULL_TEXT_QUERY, REPLACER, keyword);
		ItemIterable<QueryResult> searchResults = session.query(statement, false);
		List<DocumentVO> results = new ArrayList<DocumentVO>();
		for(QueryResult hit: searchResults) {
			String name = hit.getPropertyById(PropertyIds.NAME).getValues().get(0).toString();
			String objectId = hit.getPropertyById(PropertyIds.OBJECT_ID).getValues().get(0).toString();
			Document document = (Document) session.getObject(objectId);
			String url = CmisUtils.getDocumentURL(session, document);
			DocumentVO documentVo = new DocumentVO();
			documentVo.setName(name);
			documentVo.setUrl(url);
			results.add(documentVo);
		}
		return results;
	}
	
	public static final String getDocumentURL(final Session session, final Document document) {
	    String link = null;
	    try {
	        Method loadLink = AbstractAtomPubService.class.getDeclaredMethod("loadLink", 
	            new Class[] { String.class, String.class, String.class, String.class });
	        
	        loadLink.setAccessible(true);
	        
	        link = (String) loadLink.invoke(session.getBinding().getObjectService(), session.getRepositoryInfo().getId(),
	            document.getId(), AtomPubParser.LINK_REL_CONTENT, null);
	    } catch (Exception e) {
	       e.printStackTrace();
	    }
	    return link;
	}
	
	public static CmisClientConfig getConfigFromPrefs(ActionRequest request){
		PortletPreferences prefs = request.getPreferences();
		CmisClientConfig cmisClientConfig = new CmisClientConfig();
		cmisClientConfig.setEndpoint(prefs.getValue(CmisPortletConstants.ENDPOINT_PARAM, StringUtils.EMPTY));
		cmisClientConfig.setUsername(prefs.getValue(CmisPortletConstants.USERNAME_PARAM, StringUtils.EMPTY));
		cmisClientConfig.setPassword(prefs.getValue(CmisPortletConstants.PASSWORD_PARAM, StringUtils.EMPTY));
		cmisClientConfig.setRepositoryId(prefs.getValue(CmisPortletConstants.REPO_ID_PARAM, StringUtils.EMPTY));
		cmisClientConfig.setBinding(prefs.getValue(CmisPortletConstants.BINDING_PARAM, StringUtils.EMPTY));
		return cmisClientConfig;
	}
	
}
