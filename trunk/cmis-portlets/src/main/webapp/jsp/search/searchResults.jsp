<%@page import="org.alfresco.training.portals.cmis.portlets.vo.DocumentVO"%>
<%@page import="java.util.List"%>
<%@page import="org.alfresco.training.portals.cmis.portlets.CmisPortletConstants"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%
	String keyword = (String) request.getAttribute(CmisPortletConstants.SEARCH_KEYWORD_PARAM);
List<DocumentVO> searchResults = (List<DocumentVO>) request.getAttribute(CmisPortletConstants.SEARCH_RESULTS_PARAM);
%>
<div class="portlet-section-header">CMIS - Search results</div>
<br/>
<div class="portlet-section-body">
<%if(searchResults!=null && keyword != null){%>
<p>Results for the keyword: <strong><%=keyword%></strong></p>
<p>Click on an item to download the document</p>
<form action="<portlet:actionURL name="searchFormAction" />" method="POST">
	<table id="results">
	
<%for(DocumentVO hit: searchResults) {%>
	
	<tr>
		<td><a style="text-decoration: underline;" href="<%=hit.getUrl()%>" target="_blank"><%=hit.getName()%></a></td>
	</tr>

<%}%>
	</table>
</form>
<br />
<div id="pageNavPosition"></div>
 <script type="text/javascript"><!--
        var pager = new Pager('results', 5); 
        pager.init(); 
        pager.showPageNav('pager', 'pageNavPosition'); 
        pager.showPage(1);
    //--></script>
<%} else {%>
<p>No results found</p>
<%}%>   
<br /> 
<form action="<portlet:actionURL name="searchFormAction" />" method="POST">
	<table>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Return to the search form"/></td>
		</tr>
	</table>
</form>
</div>
