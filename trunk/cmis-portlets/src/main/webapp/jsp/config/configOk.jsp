<%@page import="org.alfresco.training.portals.cmis.portlets.CmisClientConfig"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%
	CmisClientConfig cmisClientConfig = (CmisClientConfig) request.getAttribute(CmisClientConfig.ATTRIBUTE_NAME);
%>
<div class="portlet-section-header">CMIS - Repository configuration</div>
<br/>
<div class="portlet-section-body">
<p><strong>CMIS Repository configuration updated correctly</strong></p>
<form action="<portlet:actionURL name="returnHomeAction" />" method="POST">
	<table>
		<tr>
			<td><span class="portlet-form-label">Repository endpoint:</span></td>
			<td><%=cmisClientConfig.getEndpoint()%></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Repository Id:</span></td>
			<td><%=cmisClientConfig.getRepositoryId()%></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Binding:</span></td>
			<td><%=cmisClientConfig.getBinding()%></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Username:</span></td>
			<td><%=cmisClientConfig.getUsername()%></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Password:</span></td>
			<td>********</td>
		</tr>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Home"/></td>
		</tr>
	</table>
</form>
</div>