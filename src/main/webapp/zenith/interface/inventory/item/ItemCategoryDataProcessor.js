var ItemCategoryDataProcessor = (function __ItemCategoryDataProcessor ()
{
	function create (oItemCategoryData,callback)
	{
		ajaxCall(oItemCategoryData, "/ItemCategoryDataCreate", callback);
	}
	
	function get(oItemCategoryData,callback)
	{
		ajaxCall(oItemCategoryData, "/ItemCategoryDataGet", callback);
	}
	
	function list(oItemCategoryData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oItemCategoryData:oItemCategoryData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper, "/itemCategoryDataList", callback);
	}
	
function getCategorySuggesstions(oItemCategoryData,strSortColumn, strSortOrder,callback)
{
	var oTradeMustHelper = new TradeMustHelper ();
	var oTradeMustHelper = {
			m_oItemCategoryData:oItemCategoryData,
			m_strColumn:strSortColumn, 
			m_strOrderBy:strSortOrder
	} 
	ajaxCall(oTradeMustHelper, "/itemCategoryDatagetCategorySuggesstions", callback);
}

	function update (oItemCategoryData,callback)
	{
		ajaxCall(oItemCategoryData, "/itemCategoryDataUpdate", callback);
	}
	
	function getXML (oItemCategoryData,callback)
	{
		ajaxXMLCall(oItemCategoryData, "/itemCategoryDataGetXML", callback);
	}
	
	function deleteData (oItemCategoryData,callback)
	{
		ajaxCall(oItemCategoryData, "/itemCategoryDataDelete", callback);
	}
	
	return {
		create:create,
		update:update,
		list:list,
		getCategorySuggesstions: getCategorySuggesstions,
		get:get,
		getXML:getXML,
		deleteData:deleteData
	};
})();