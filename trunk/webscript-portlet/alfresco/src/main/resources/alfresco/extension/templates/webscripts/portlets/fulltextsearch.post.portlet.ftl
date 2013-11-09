<p>Results for the keyword: <strong>${keyword}</strong></p>
<p>Click on an item to download the document</p>

<#if searchResults?exists>

<table id="results">

	<#list searchResults as hit>
		<tr>
			<td><a style="text-decoration: underline;" href="/alfresco${hit.url}" target="_blank">${hit.properties.name}</a></td>
		</tr>
	</#list>

</table>
<#else>

<p>No results found.</p>

</#if>
<br />
<a href="#" onClick="viewSearchForm();">Return to the search form</a>
<br />