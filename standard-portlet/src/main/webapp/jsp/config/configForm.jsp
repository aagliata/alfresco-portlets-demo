<%@page import="org.alfresco.training.portals.standard.portlets.HelloWorldPortletConstants"%>
<%@page import="org.alfresco.training.portals.standard.portlets.HelloWorldConfig"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%
	HelloWorldConfig helloWorldConfig = (HelloWorldConfig) request.getAttribute(HelloWorldConfig.ATTRIBUTE_NAME);
%>

<div class="portlet-section-header">HelloWorld - Configuration</div>
<br/>
<div class="portlet-section-body">
<form action="<portlet:actionURL name="editAction"/>" method="POST">
	<table>
		<tr>
			<td><span class="portlet-form-label">Welcome message:</span></td>
			<td><input class="portlet-form-input-field" type="text" name="<%=HelloWorldPortletConstants.WELCOME_MESSAGE_PARAM%>" value="<%=helloWorldConfig.getWelcomeMessage()%>" size="60"/></td>
		</tr>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Save"/></td>
			<td><input class="portlet-form-button" type="reset" name="reset" title="Reset" value="Reset"/></td>
		</tr>
	</table>
</form>
</div>