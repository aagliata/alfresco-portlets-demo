<%@page import="org.alfresco.training.portals.webservices.portlets.WsClientConfig"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%
	WsClientConfig wsClientConfig = (WsClientConfig) request.getAttribute(WsClientConfig.ATTRIBUTE_NAME);
%>
<div class="portlet-section-header">Alfresco Web Service Client portlet - Configuration</div>
<br/>
<div class="portlet-section-body">
<p><strong>WS Client configuration updated correctly</strong></p>
<form action="<portlet:actionURL name="returnAction" />" method="POST">
	<table>
		<tr>
			<td><span class="portlet-form-label">Endpoint:</span></td>
			<td><%=wsClientConfig.getEndpoint()%></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Username:</span></td>
			<td><%=wsClientConfig.getUsername()%></td>
		</tr>
		<tr>
			<td><span class="portlet-form-label">Password:</span></td>
			<td>***********</td>
		</tr>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Edit"/></td>
		</tr>
	</table>
</form>
</div>