var TemplateCategoryDataProcessor = (function __TemplateCategoryDataProcessor ()
{
	function create (oTemplateCategoryData,callback)
	{
		ajaxCall(oTemplateCategoryData, "/templateCategoryDatacreate", callback);
	}

	
	function list(oTemplateCategoryData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oTemplateCategoryData:oTemplateCategoryData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/templateCategoryDatalist", callback);
	}
	
	function get(oTemplateCategoryData,callback)
	{
		ajaxCall(oTemplateCategoryData, "/templateCategoryDataget", callback);
	}
	function getXML (oTemplateCategoryData, callback)
	{
		ajaxXMLCall(oTemplateCategoryData, "/templateCategoryDatagetXML", callback);
		
	}
	function update (oTemplateCategoryData,callback)
	{
		ajaxCall(oTemplateCategoryData, "/templateCategoryDataupdate", callback);
	}
	function deleteData (oTemplateCategoryData,callback)
	{
		ajaxCall(oTemplateCategoryData, "/templateCategoryDatadeleteData", callback);
	}
	
	return { 
		create : create,
		list : list,
		get:get,
		getXML:getXML,
		update:update,
		deleteData:deleteData
	};
})();