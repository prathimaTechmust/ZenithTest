var ItemLocationDataProcessor = (function __ItemLocationDataProcessor ()
{
	function create (oItemLocationData,callback)
	{
		ajaxCall(oItemLocationData, "/itemLocationCreate", callback);
	}
	
	function list(oItemLocationData, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oItemLocation:oItemLocationData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder
		} 
		ajaxCall(oTradeMustHelper, "/itemLocationList", callback);
	}
	
	function getInventoryReport(oItemLocationData,callback)
	{
		ajaxCall(oItemLocationData, "/getInventorReport", callback);
	}
	
	return {
		create:create,
		list:list,
		getInventoryReport:getInventoryReport
	}
})();