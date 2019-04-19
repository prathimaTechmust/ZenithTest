var LocationDataProcessor = (function __LocationDataProcessor ()
{
	function create (oLocationData,callback)
	{
		ajaxCall(oLocationData, "/LocationDataCreate", callback);
	}
	
	function get(oLocationData,callback)
	{
		ajaxCall(oLocationData, "/LocationDataGet", callback);
	}
	
	function list(oLocationdata, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oLocationData:oLocationdata,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper, "/LocationDataList", callback);
	}
	
	function update (oLocationData,callback)
	{
		ajaxCall(oLocationData, "/LocationDataUpdate", callback);
	}
	
	function getXML (oLocationData,callback)
	{
		ajaxXMLCall(oLocationData, "/LocationDataGetXML", callback);
	}
	
	function deleteData (oLocationData,callback)
	{
		ajaxCall(oLocationData, "/LocationDataDelete", callback);
	}
	
	function setDefaultLocation (oLocationData,callback)
	{
		ajaxCall(oLocationData, "/LocationDataSetDefaultLocation", callback);
	}
	
	function getLocationSuggesstions(oLocationdata, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oLocationData:oLocationdata,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder
		} 
		ajaxCall(oTradeMustHelper, "/LocationDataGetLocationSuggesstions", callback);
	}
	
	return {
		create:create,
		update:update,
		list:list,
		get:get,
		getXML:getXML,
		deleteData:deleteData,
		setDefaultLocation:setDefaultLocation,
		getLocationSuggesstions:getLocationSuggesstions
	};
})();