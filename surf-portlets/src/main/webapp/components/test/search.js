(function()
{
   /**
    * YUI Library aliases
    */
   var Dom = YAHOO.util.Dom,
       Event = YAHOO.util.Event,
       DDM = YAHOO.util.DragDropMgr;

   Alfresco.TestSearch = function TestSearch_constructor(htmlId)
   {
      Alfresco.TestSearch.superclass.constructor.call(this, "Alfresco.TestSearch", htmlId, ["json"]);
      return this;
	};
	
   YAHOO.extend(Alfresco.TestSearch, Alfresco.component.Base,
   {
      /**
       * Object container for initialization options
       */
      options:
      {
      },
      
      /**
       * Fired by YUI when parent element is available for scripting
       * @method onReady
       */
      onReady: function TestSearch_onReady()
      {
         // Reference to self - used in inline functions
         var me = this;
         
         this.queryTextEl = Dom.get(this.id+'-queryText');
         this.resultsEl = Dom.get(this.id+'-results');
         
         Alfresco.util.createYUIButton(this, "searchButton", me.onSearchClick);
         
         
    	},
    	
    	presentResults: function TestSearch_presentResults(json){
    		//clear list
    		this.resultsEl.innerHTML = "";
    		
    		//populate list
    		var items = json.items;
    		for (var i=0; i<items.length; i++){
    			var item = items[i];
    			var liEl = document.createElement("li");
    			var p = document.createTextNode(item.name);
    			liEl.appendChild(p);
    			
    			this.resultsEl.appendChild(liEl)
    			//alert(item.name);
    		}
    	},
    	
    	onSearchClick: function TestSearch_onSearchClick(){
    		// Reference to self - used in inline functions
         	var me = this;
         
    		var queryText = this.queryTextEl.value;
    		//alert(queryText);
    		
    		Alfresco.util.Ajax.jsonRequest({
         	url: "/share/proxy/alfresco/slingshot/search?site=&term="+encodeURIComponent(queryText)+"&tag=&maxResults=251&sort=&query=&repo=false",
         	successCallback: {
         		fn: function(oResponse){
         			me.presentResults(oResponse.json);
         		},
         		scope: me
         	},
         	failureCallback: {
         		fn: function(oResponse){},
         		scope: me
         	}
         });
    	}
    });
})();
	