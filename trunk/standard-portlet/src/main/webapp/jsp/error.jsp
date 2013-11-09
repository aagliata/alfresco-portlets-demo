<%@page import="org.alfresco.training.portals.standard.portlets.HelloWorldPortletConstants"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<div class="portlet-section-header">HelloWorld - Error</div>
<br />
<div class="portlet-section-body">
	<p>Error during the operation:</p>
	<%
		String errorMessage = (String) request.getAttribute(HelloWorldPortletConstants.ERROR_MESSAGE_PARAM);

			if (errorMessage != null) {
	%>

	<p><%=errorMessage%></p>

	<%
		}
	%>
	<form action="<portlet:actionURL name="returnEditAction"/>" method="POST">
	<table>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Home"></td>
		</tr>
	</table>
</form>
</div>
