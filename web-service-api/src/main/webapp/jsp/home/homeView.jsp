<%@page import="org.alfresco.training.portals.webservices.portlets.WsClientConstants"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%
Boolean configure = (Boolean) request.getAttribute(WsClientConstants.CONFIGURE_PARAM);
if(configure==null){
	configure = Boolean.FALSE;
}
 %>
<div class="portlet-section-header">Web Services Client - Search portlet</div>
<br/>
<div class="portlet-section-body">
<p><strong>Search</strong></p>
<%if(configure){%>
<p><strong><u>Please configure the Web Service client using the EDIT mode</u></strong></p>
<%}%>
<form action="<portlet:actionURL name="searchAction"/>" method="POST">
	<table>
		<tr>
			<td><span class="portlet-form-label">Keyword:</span></td>
			<td><input class="portlet-form-input-field" type="text" name="<%=WsClientConstants.KEYWORD_PARAM%>" value="" size="40"/></td>
		</tr>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Search"/></td>
			<td><input class="portlet-form-button" type="reset" name="reset" title="Reset" value="Reset"/></td>
		</tr>
	</table>
</form>
</div>