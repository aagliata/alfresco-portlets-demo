<html>
<head>
<style type="text/css">
  .pg-normal {
	color: black;
	font-weight: normal;
	text-decoration: none;
	cursor: pointer;
}

.pg-selected {
	color: black;
	font-weight: bold;
	text-decoration: underline;
	cursor: pointer;
}
  </style>
<script type="text/javascript">

function makeGETRequest(url, parameters) {

	document.getElementById('alfrescoSearch').innerHTML = "Searching...";

	    http_request = false;
	    if (window.XMLHttpRequest) {
	       http_request = new XMLHttpRequest();
	       if (http_request.overrideMimeType) {
	          http_request.overrideMimeType('text/html');
	       }
	    } else if (window.ActiveXObject) { // IE
	       try {
	          http_request = new ActiveXObject("Msxml2.XMLHTTP");
	       } catch (e) {
	          try {
	             http_request = new ActiveXObject("Microsoft.XMLHTTP");
	          } catch (e) {}
	       }
	    }
	    if (!http_request) {
	       alert('Cannot create XMLHTTP instance');
	       return false;
	    }

	    http_request.onreadystatechange = alertForm;
	    http_request.open('GET', url, true);
	    http_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	    http_request.setRequestHeader("Content-length", parameters.length);
	    http_request.setRequestHeader("Connection", "close");
	    http_request.send(parameters);
}

function makePOSTRequestSearch(url, parameters) {

	document.getElementById('alfrescoSearch').innerHTML = "Searching...";

	    http_request = false;
	    if (window.XMLHttpRequest) {
	       http_request = new XMLHttpRequest();
	       if (http_request.overrideMimeType) {
	          http_request.overrideMimeType('text/html');
	       }
	    } else if (window.ActiveXObject) { // IE
	       try {
	          http_request = new ActiveXObject("Msxml2.XMLHTTP");
	       } catch (e) {
	          try {
	             http_request = new ActiveXObject("Microsoft.XMLHTTP");
	          } catch (e) {}
	       }
	    }
	    if (!http_request) {
	       alert('Cannot create XMLHTTP instance');
	       return false;
	    }

	    http_request.onreadystatechange = alertSearch;
	    http_request.open('POST', url, true);
	    http_request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	    http_request.setRequestHeader("Content-length", parameters.length);
	    http_request.setRequestHeader("Connection", "close");
	    http_request.send(parameters);
}

function alertForm() {

	    if (http_request.readyState == 4) {
	       if (http_request.status == 200) {
	          result = http_request.responseText;

		document.getElementById('alfrescoSearch').innerHTML = "";
	       document.getElementById('alfrescoSearch').innerHTML = result;
			}
	    }
}

function alertSearch() {

	    if (http_request.readyState == 4) {
	       if (http_request.status == 200) {
	          result = http_request.responseText;

		document.getElementById('alfrescoSearch').innerHTML = "";
	       document.getElementById('alfrescoSearch').innerHTML = result;
	       
		        pager.init(); 
		        pager.showPageNav('pager', 'pageNavPosition'); 
		        pager.showPage(1);
			}
	    }
}

function executeSearch(url, ticket, keyword){
	   makePOSTRequestSearch(url,
			   'alf_ticket='+ticket+'&keyword='+keyword+'&math='+Math.random());
   }
   
function submitAlfrescoSearch(){
	var form = document.alfrescoSearchForm;
	var keyword = form.keyword.value;
	executeSearch('${url.service}','${session.ticket}',keyword);
	return true;
}

function viewSearchForm(ticket){
	makeGETRequest('${url.service}.portlet','alf_ticket=${session.ticket}');
	document.getElementById('pageNavPosition').innerHTML= "";
}

function Pager(tableName, itemsPerPage) {
    this.tableName = tableName;
    this.itemsPerPage = itemsPerPage;
    this.currentPage = 1;
    this.pages = 0;
    this.inited = false;
    
    this.showRecords = function(from, to) {        
        var rows = document.getElementById(tableName).rows;
        // i starts from 1 to skip table header row
        for (var i = 0; i < rows.length; i++) {
            if (i < from || i > to)  
                rows[i].style.display = 'none';
            else
                rows[i].style.display = '';
        }
    }
    
    this.showPage = function(pageNumber) {
    	if (! this.inited) {
    		alert("not inited");
    		return;
    	}

        var oldPageAnchor = document.getElementById('pg'+this.currentPage);
        oldPageAnchor.className = 'pg-normal';
        
        this.currentPage = pageNumber;
        var newPageAnchor = document.getElementById('pg'+this.currentPage);
        newPageAnchor.className = 'pg-selected';
        
        var from = (pageNumber - 1) * itemsPerPage + 1;
        var to = from + itemsPerPage - 1;
        this.showRecords(from, to);
    }   
    
    this.prev = function() {
        if (this.currentPage > 1)
            this.showPage(this.currentPage - 1);
    }
    
    this.next = function() {
        if (this.currentPage < this.pages) {
            this.showPage(this.currentPage + 1);
        }
    }                        
    
    this.init = function() {
        var rows = document.getElementById(tableName).rows;
        var records = (rows.length - 1); 
        this.pages = Math.ceil(records / itemsPerPage);
        this.inited = true;
    }

    this.showPageNav = function(pagerName, positionId) {
    	if (! this.inited) {
    		alert("not inited");
    		return;
    	}
    	var element = document.getElementById(positionId);
    	
    	var pagerHtml = '<span onclick="' + pagerName + '.prev();" class="pg-normal"> &#171 Prev </span> | ';
        for (var page = 1; page <= this.pages; page++) 
            pagerHtml += '<span id="pg' + page + '" class="pg-normal" onclick="' + pagerName + '.showPage(' + page + ');">' + page + '</span> | ';
        pagerHtml += '<span onclick="'+pagerName+'.next();" class="pg-normal"> Next &#187;</span>';            
        
        element.innerHTML = pagerHtml;
    }
}
</script>	
</head>
<div class="portlet-section-header">Alfresco - Full Text Search</div>
<br/>
<div class="portlet-section-body">
<div id="alfrescoSearch">
	<form name="alfrescoSearchForm" onSubmit="return submitAlfrescoSearch();">
	<table>
		<tr>
			<td<span class="portlet-form-label">Keyword:</span></td>
			<td>
				<input class="portlet-form-input-field" type="text" name="keyword" />
			</td>
			<td>
				<input class="portlet-form-button" type="submit" value="Search"/>
			</td>
		</tr>
	</table>
	</form>
	<br />
	<table id="results">
	<tr>
		<td></td>
	</tr>
	</table>
</div>
</div>
<div id="pageNavPosition"></div>
<script type="text/javascript"><!--
        var pager = new Pager('results', 5); 
        pager.init(); 
        pager.showPageNav('pager', 'pageNavPosition'); 
        pager.showPage(1);
    //--></script>
<br />
</html>