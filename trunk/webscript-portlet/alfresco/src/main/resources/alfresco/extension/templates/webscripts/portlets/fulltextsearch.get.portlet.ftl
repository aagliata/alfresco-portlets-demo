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