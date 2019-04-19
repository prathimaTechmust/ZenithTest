var VendorPurchaseOrderDataProcessor = (function __VendorPurchaseOrderDataProcessor ()
{
	function create (oData,callback)
	{
		 ajaxCall(oData, "/vendorPOCreate", callback);
		
	}
	
	function list(oData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oVPOrderData:oData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/vendorPOList", callback);
		
	}
	
	function getXML (oData,callback)
	{
		ajaxXMLCall(oData, "/vendorPOGetXML", callback);
		
	}
	
	function get(oData,callback)
	{
		 ajaxCall(oData, "/vendorPOGet", callback);
		
	}
	
	function update (oData,callback)
	{
		 ajaxCall(oData, "/vendorPOUpdate", callback);
		
	}
	
	function deleteData (oData,callback)
	{
		 ajaxCall(oData, "/vendorPODelete", callback);
		
	}
	
	function saveAndPrint (oData,callback)
	{
		 ajaxCall(oData, "/vendorPOSaveAndPrint", callback);
		
	}
	
	return {
		create:create,
		list:list,
		get:get,
		getXML:getXML,
		update:update,
		deleteData:deleteData,
		saveAndPrint:saveAndPrint
	}
	
})();