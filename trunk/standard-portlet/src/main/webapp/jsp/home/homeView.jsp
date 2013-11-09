<%@page import="org.alfresco.training.portals.standard.portlets.vo.HomeVO"%>
<%@page import="org.alfresco.training.portals.standard.portlets.HelloWorldPortletConstants"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%
	HomeVO home = (HomeVO) request.getAttribute(HelloWorldPortletConstants.HOME_VO_PARAM);
%>
<div class="portlet-section-header">HelloWorld</div>
<br/>
<div class="portlet-section-body">
<p><strong>HelloWorld</strong></p>
	<table>
		<tr>
			<td><span class="portlet-form-label">Welcome to your HelloWorld portlet!</span></td>
		</tr>
		<tr>
			<td>Hi <strong><%=home.getUsername()%></strong>, <%=home.getWelcomeMessage()%></td>
		</tr>
	</table>
</div>