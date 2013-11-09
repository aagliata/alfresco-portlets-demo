<%@page import="org.alfresco.training.portals.cmis.portlets.CmisUtils"%>
<%@page import="org.alfresco.training.portals.cmis.portlets.CmisClientConfig"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%
	CmisClientConfig cmisClientConfig = (CmisClientConfig) request.getAttribute(CmisClientConfig.ATTRIBUTE_NAME);

boolean atomSelected = true;

if(cmisClientConfig.getBinding().equals(CmisUtils.BINDING_SOAP_VALUE)){
	atomSelected = false;
}
%>
<portlet:defineObjects/>

<div class="portlet-section-header">CMIS - Repository configuration</div>
<br/>
<div class="portlet-section-body">
<form action="<portlet:actionURL name="editAction"/>" method="POST">
	<table>
		<tr>
			<td><span class="portlet-form-label">Repository endpoint:</span></td>
			<td><input class="portlet-form-input-field" type="text" name="endpoint" value="<%=cmisClientConfig.getEndpoint()%>" size="60"/></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Repository Id:</span></td>
			<td><input class="portlet-form-input-field" type="text" name="repositoryId" value="<%=cmisClientConfig.getRepositoryId()%>" size="30"/></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Binding:</span></td>
			<td>
				<%if(atomSelected){%>
				<select name="binding">
					<option value="atom" selected="selected">Atom</option>
					<option value="soap">SOAP</option>
				</select>
				<%} else {%>
				<select name="binding">
					<option value="atom">Atom</option>
					<option value="soap" selected="selected">SOAP</option>
				</select>
				<%}%>
			</td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Username:</span></td>
			<td><input class="portlet-form-input-field" type="text" name="username" value="<%=cmisClientConfig.getUsername()%>"/></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Password:</span></td>
			<td><input class="portlet-form-input-field" type="password" name="password" value="<%=cmisClientConfig.getPassword()%>" /></td>
		</tr>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" Value="Save"/></td>
			<td><input class="portlet-form-button" type="reset" name="reset" title="Reset"/></td>
		</tr>
	</table>
</form>
<br />
<form action="<portlet:actionURL name="returnHomeAction"/>" method="POST">
	<table>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Home"></td>
		</tr>
	</table>
</form>
</div>