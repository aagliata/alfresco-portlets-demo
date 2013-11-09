<%@page import="org.alfresco.training.portals.cmis.portlets.CmisPortletConstants"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<div class="portlet-section-header">CMIS - Error</div>
<br />
<div class="portlet-section-body">
	<p>Error during the operation:</p>
	<%
		String errorMessage = (String) request.getAttribute(CmisPortletConstants.ERROR_MESSAGE_PARAM);

			if (errorMessage != null) {
	%>

	<p><%=errorMessage%></p>

	<%
		}
	%>
	<form action="<portlet:actionURL name="returnHomeAction"/>" method="POST">
	<table>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Home"></td>
		</tr>
	</table>
</form>
</div>
