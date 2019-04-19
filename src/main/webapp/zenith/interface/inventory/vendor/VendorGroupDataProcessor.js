var VendorGroupDataProcessor = (function __VendorGroupDataProcessor ()
{
	function create (oData,callback)
	{
		 ajaxCall(oData, "/vendorGroupCreate", callback);	
	}
	
	function get(oData,callback)
	{
		 ajaxCall(oData, "/vendorGroupGet", callback);	
		
	}
	
	function list(oData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oVendorGroupData:oData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/vendorGroupList", callback);	
		
	}
	
	function getXML (oData,callback)
	{
		ajaxXMLCall(oData, "/vendorGroupGetXML", callback);	
		
	}
	
	function deleteData (oData,callback)
	{
		 ajaxCall(oData, "/vendorGroupDelete", callback);	
		
	}
	
	function update (oData,callback)
	{
		 ajaxCall(oData, "/vendorGroupUpdate", callback);	
		
	}
	
	return {
		create:create,
		update:update,
		list:list,
		get:get,
		getXML:getXML,
		deleteData:deleteData
	}
})();