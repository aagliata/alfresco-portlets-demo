var keyword = "";
keyword = args.keyword;
var searchResults = null;
if(keyword != undefined && keyword!=""){
	var luceneQuery = "TEXT:\""+keyword+"\"";
	searchResults = search.luceneSearch(luceneQuery);
} else {
	status.code = 400;
	status.message = "Keyword not provided.";
	status.redirect = true;
}
model.keyword = keyword;
model.searchResults = searchResults;
