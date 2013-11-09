<%@page import="org.alfresco.training.portals.webservices.portlets.vo.ResultVO"%>
<%@page import="java.util.List"%>
<%@page import="org.alfresco.training.portals.webservices.portlets.WsClientConstants"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%
	List<ResultVO> resultsList = (List<ResultVO>)request.getAttribute(WsClientConstants.RESULTS_PARAM);
	String keyword = (String) request.getAttribute(WsClientConstants.KEYWORD_PARAM);
%>

<div class="portlet-section-header">Web Services Client - Search results</div>
<br/>
<div class="portlet-section-body">

<%if(resultsList!=null && keyword != null){%>
<p>Results for the keyword: <strong><%=keyword%></strong></p>
<p>Click on an item to download the content</p>
	<table id="results">
	
<%for(ResultVO hit: resultsList) {%>
	
	<tr>
		<td><a style="text-decoration: underline;" href="<%=hit.getDownloadUrl()%>" target="_blank"><%=hit.getName()%></a></td>
	</tr>

<%}%>
	</table>

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
<form action="<portlet:actionURL name="returnAction" />" method="POST">
	<table>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Return to the Search form"/></td>
		</tr>
	</table>
</form>
</div>