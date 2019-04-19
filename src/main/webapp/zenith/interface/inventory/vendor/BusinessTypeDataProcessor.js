var BusinessTypeDataProcessor = (function __BusinessTypeDataProcessor ()
{
	function create (oBusinessTypeData,callback)
	{
		 ajaxCall(oBusinessTypeData, "/businessTypeDataCreate", callback);	
	}
	
	function get(oBusinessTypeData,callback)
	{
		ajaxCall(oBusinessTypeData, "/businessTypeDataGet", callback);
		
	}
	
	function list(oBusinessTypeData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oBusinessTypeData:oBusinessTypeData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper, "/businessTypeDataList", callback);
		
	}

	function update (oBusinessTypeData,callback)
	{
		ajaxCall(oBusinessTypeData, "/businessTypeDataUpdate", callback);
		
	}
	
	function getXML (oBusinessTypeData,callback)
	{
		ajaxXMLCall(oBusinessTypeData, "/businessTypeDataGetXML", callback);
		
	}
	
	function deleteData (oBusinessTypeData,callback)
	{
		ajaxCall(oBusinessTypeData, "/businessTypeDataDelete", callback);
		
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