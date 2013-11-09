<%@page import="org.alfresco.training.portals.webservices.portlets.WsClientConstants"%>
<%@page import="org.alfresco.training.portals.webservices.portlets.WsClientConfig"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%
	WsClientConfig wsClientConfig = (WsClientConfig) request.getAttribute(WsClientConfig.ATTRIBUTE_NAME);
%>

<div class="portlet-section-header">Alfresco Web Service Client portlet - Configuration</div>
<br/>
<div class="portlet-section-body">
<form action="<portlet:actionURL name="editAction"/>" method="POST">
	<table>
		<tr>
			<td><span class="portlet-form-label">Endpoint:</span></td>
			<td><input class="portlet-form-input-field" type="text" name="<%=WsClientConstants.ENDPOINT_PARAM%>" value="<%=wsClientConfig.getEndpoint()%>" size="60"/></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Username:</span></td>
			<td><input class="portlet-form-input-field" type="text" name="<%=WsClientConstants.USERNAME_PARAM%>" value="<%=wsClientConfig.getUsername()%>" size="60"/></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Password:</span></td>
			<td><input class="portlet-form-input-field" type="password" name="<%=WsClientConstants.PASSWORD_PARAM%>" value="<%=wsClientConfig.getPassword()%>" size="60"/></td>
		</tr>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Save"/></td>
			<td><input class="portlet-form-button" type="reset" name="reset" title="Reset" value="Reset"/></td>
		</tr>
	</table>
</form>
</div>