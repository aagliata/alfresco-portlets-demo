<#assign id=args.htmlid?html>
<script type="text/javascript">//<![CDATA[
   new Alfresco.TestSearch("${args.htmlid?js_string}").setOptions(
   {      
   }).setMessages(
      ${messages}
   );
//]]></script>
<div class="test-search">
   <h2 id="${id}-h2">Test Search</h2>
   <form>
   	<input id="${id}-queryText"></input>
   	<button id="${id}-searchButton">Search</button>
   </form>
   <ol id="${id}-results" class="results">
   </ol>
</div>
