var ItemGroupDataProcessor = (function __ItemGroupDataProcessor ()
{
	function create (oItemGroupData,callback)
	{
		ajaxCall(oItemGroupData, "/itemGroupDataCreate", callback);
	}

	function get(oItemGroupData,callback)
	{
		ajaxCall(oItemGroupData, "/itemGroupDataGet", callback);
	}
	
	function list(oItemGroupData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oItemGroupData:oItemGroupData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		console.log( JSON.stringify(oTradeMustHelper));
		ajaxCall(oTradeMustHelper, "/itemGroupDataList", callback);
	}
	
	function update (oItemGroupData,callback)
	{
		ajaxCall(oItemGroupData, "/itemGroupDataUpdate", callback);
	}
	
	function getXML (oItemGroupData,callback)
	{
		ajaxXMLCall(oItemGroupData, "/itemGroupDataGetXML", callback);
	}
	
	function deleteData (oItemGroupData,callback)
	{
		ajaxCall(oItemGroupData, "/itemGroupDataDelete", callback);
	}
	
	return {
		create:create,
		update:update,
		list:list,
		get:get,
		getXML:getXML,
		deleteData:deleteData
	};
})();