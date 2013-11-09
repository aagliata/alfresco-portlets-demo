package org.alfresco.training.portals.cmis.portlets;

import org.apache.commons.lang3.StringUtils;

public class CmisClientConfig {

	public static final String ATTRIBUTE_NAME = "cmisClientConfig";
	
	private String endpoint = StringUtils.EMPTY;
	private String username = StringUtils.EMPTY;
	private String password = StringUtils.EMPTY;
	private String repositoryId = StringUtils.EMPTY;
	private String binding = StringUtils.EMPTY;
	
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}
	public String getBinding() {
		return binding;
	}
	public void setBinding(String binding) {
		this.binding = binding;
	}
	public boolean isEmpty(){
		if(StringUtils.isEmpty(endpoint)
				|| StringUtils.isEmpty(username)
				|| StringUtils.isEmpty(password)
				|| StringUtils.isEmpty(binding)){
			return true;
		} else
			return false;
	}
	
}
