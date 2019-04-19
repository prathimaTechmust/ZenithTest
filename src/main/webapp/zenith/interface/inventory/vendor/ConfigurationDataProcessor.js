var ConfigurationDataProcessor = (function __ConfigurationDataProcessor()
{
	function get(oConfigurationData,callback)
	{
		 ajaxCall(oConfigurationData, "/configurationDataGet", callback);		
	}
	
	
	function setDefaultVendor(oConfigurationData , callback)
	{
		 ajaxCall(oConfigurationData, "/configurationDataSetDefaultVendor", callback);
	}
	
	function list(oConfigurationData, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oConfigurationData:oConfigurationData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/configurationDataList", callback);
		
	}
    return {
	        get :get,
	        list : list,
	        setDefaultVendor:setDefaultVendor
           }; 

})();