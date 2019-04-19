var ItemDataProcessor = (function __ItemDataProcessor ()
{
	function create (oItemData,callback)
	{
		ajaxCall(oItemData, "/newitemDataCreate", callback);
	}
	
	function get(oItemData,callback)
	{
		ajaxCall(oItemData, "/newitemDataGet", callback);
	}
	
	function getItemData(oItemData,callback)
	{
		ajaxCall(oItemData, "/newitemDatagetItemData", callback);
	}
	
	function list(oItemData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
			var oTradeMustHelper = new TradeMustHelper ();
			var oTradeMustHelper = {
     			m_oItemData:oItemData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
			ajaxCall(oTradeMustHelper, "/itemDataList", callback);
	}
	
	function update (oItemData,callback)
	{
		ajaxCall(oItemData, "/newitemDataUpdate", callback);
	}
	
	function getXML (oItemData,callback)
	{
		ajaxXMLCall(oItemData, "/itemDataGetXML", callback);
	}
	
	function deleteData (oItemData,callback)
	{
		ajaxCall(oItemData, "/taxDelete", callback);
	}
	
	function getImagePreview (oItemData,callback)
	{
		ajaxCall(oItemData, "/getImagePreview", callback);
	}
	
	function getArticleSuggesstions(oItemData, strSortColumn, strSortOrder, callback)
	{
			var oTradeMustHelper = new TradeMustHelper ();
			var oTradeMustHelper = {
     			m_oItemData:oItemData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder
		} 
			ajaxCall(oTradeMustHelper, "/getArticalSuggestion", callback);
	}
	
	function updateStockEntries(oStockEntriesData, oData, callback)
	{
			var oTradeMustHelper = new TradeMustHelper ();
			var oTradeMustHelper = {
				m_oStockEntriesData:oStockEntriesData,
				m_oItemData:oData 
		} 
			ajaxCall(oTradeMustHelper, "/updateStockEntries", callback);
	}
	
	function getClientArticleSuggesstions(oItemData, nClientId, strSortColumn, strSortOrder, callback)
	{
			var oTradeMustHelper = new TradeMustHelper ();
			var oTradeMustHelper = {
     			m_oItemData:oItemData,
     			m_nClientId:nClientId,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder
		} 
			ajaxCall(oTradeMustHelper, "/getClientArticalSuggestion", callback);
	}
	
	function getStockMovementReport(strFromDate, strToDate, oItemData, bIsIncludeZeroMovementStockChecked, callback)
	{
			var oTradeMustHelper = new TradeMustHelper ();
			var oTradeMustHelper = {
				m_strFromDate:strFromDate,
				m_strToDate:strToDate,
				m_oItemData:oItemData, 
				m_bIncludeZeroMovement:bIsIncludeZeroMovementStockChecked
		} 
			ajaxCall(oTradeMustHelper, "/getStockMovementReport", callback);
	}
	
	return {
		create:create,
		update:update,
		list:list,
		get:get,
		getItemData:getItemData,
		getXML:getXML,
		deleteData:deleteData,
		getImagePreview:getImagePreview,
		getArticleSuggesstions:getArticleSuggesstions,
		updateStockEntries:updateStockEntries,
		getClientArticleSuggesstions:getClientArticleSuggesstions,
		getStockMovementReport:getStockMovementReport
	};
})();