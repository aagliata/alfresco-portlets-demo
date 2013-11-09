<%@page import="org.alfresco.training.portals.cmis.portlets.CmisPortletConstants"%>
<%@page import="org.apache.chemistry.opencmis.client.api.Document"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%
	Document document = (Document) request.getAttribute(CmisPortletConstants.DOC_PARAM);
String downloadUrl = (String) request.getAttribute(CmisPortletConstants.UPLOAD_OK_DOWNLOAD_URL_PARAM);
%>
<div class="portlet-section-header">
<%if(document!=null){%>
CMIS - Document uploaded correctly</div>
<br />
<div class="portlet-section-body">
	<p>The new document was uploaded correctly, here the details:</p>
	<p>Path: <%=document.getPaths().get(0)%></p>
	<p>Name: <%=document.getName()%></p>
	<p>Id: <%=document.getId()%></p>
	<p>Base type: <%=document.getBaseType().getQueryName()%></p>
	<p><a style="text-decoration: underline;" href="<%=downloadUrl%>" target="_blank">Download URL</a></p>
<%} else {%>
CMIS - Error during document upload</div>
<br />
<div class="portlet-section-body">
<%}%>
<form action="<portlet:actionURL name="uploadFormAction"/>" method="POST">
		<table>
			<tr>
				<td><input type="submit" name="submit" value="Upload another document" /></td>
			</tr>
		</table>
	</form>
</div>