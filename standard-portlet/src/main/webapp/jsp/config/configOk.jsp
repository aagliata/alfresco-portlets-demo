<%@page import="org.alfresco.training.portals.standard.portlets.HelloWorldConfig"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%
	HelloWorldConfig helloWorldConfig = (HelloWorldConfig) request.getAttribute(HelloWorldConfig.ATTRIBUTE_NAME);
%>
<div class="portlet-section-header">HelloWorld - Configuration</div>
<br/>
<div class="portlet-section-body">
<p><strong>HelloWorld configuration updated correctly</strong></p>
<form action="<portlet:actionURL name="returnEditAction" />" method="POST">
	<table>
		<tr>
			<td><span class="portlet-form-label">Welcome message:</span></td>
			<td><%=helloWorldConfig.getWelcomeMessage()%></td>
		</tr>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Edit"/></td>
		</tr>
	</table>
</form>
</div>