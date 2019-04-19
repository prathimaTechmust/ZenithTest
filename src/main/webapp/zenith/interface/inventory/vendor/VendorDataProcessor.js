var VendorDataProcessor = (function __VendorDataProcessor()
{
	function listVendor(oVendorData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oVendorData:oVendorData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/vendorDataListVendor", callback);		
	}
	
	function createVendor(oVendorData,callback)
	{
		 ajaxCall(oVendorData, "/vendorDataCreate", callback);	
		
	}
	
	function updateVendor(oVendorData,callback)
	{
		 ajaxCall(oVendorData, "/vendorDataUpdate", callback);	
		
	}
	function getVendorSuggesstions(oVendorData, strColumn, strOrderBy, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oVendorData:oVendorData,
				 m_strColumn:strColumn, 
				 m_strOrderBy:strOrderBy
		} 
		 ajaxCall(oTradeMustHelper, "/getVendorSuggesstions", callback);	
		
	}
	
	function updateVendorBalanceData(oVendorData,callback)
	{
		 ajaxCall(oVendorData, "/updateVendorBalanceData", callback);	
		
	}

	function getVendor(oVendorData,callback)
	{
		 ajaxCall(oVendorData, "/getVendor", callback);	
		
	}
	
	function deleteVendorData(oVendorData,callback)
	{
		 ajaxCall(oVendorData, "/deleteVendorData", callback);	
		
	}
	
	 return {
		 listVendor : listVendor,
		 createVendor:createVendor,
		 updateVendor:updateVendor,
		 getVendorSuggesstions:getVendorSuggesstions,
		 updateVendorBalanceData:updateVendorBalanceData,
		 getVendor:getVendor,
		 deleteVendorData:deleteVendorData
        }; 

})();