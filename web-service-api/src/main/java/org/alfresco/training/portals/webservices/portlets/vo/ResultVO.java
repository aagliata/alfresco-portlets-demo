package org.alfresco.training.portals.webservices.portlets.vo;

import org.apache.commons.lang3.StringUtils;

public class ResultVO {

	private String name = StringUtils.EMPTY;
	private String downloadUrl = StringUtils.EMPTY;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
		
}