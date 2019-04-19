var ClientDataProcessor = (function __ClientDataProcessor()
{
	function create(oClientData,callback)
	{
		ajaxCall(oClientData,"/clientDataCreate",callback);
	}

	function list(oClientData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oClientData:oClientData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper,"/clientDataList",callback);
	}
	
	function getClientSuggesstions(oClientData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oClientData:oClientData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper,"/clientDataList",callback);
	}
	
	function get(oClientData,callback)
	{
		 ajaxCall(oClientData,"/clientDataGet",callback);
	}
	
	function update(oClientData,callback)
	{
		ajaxCall(oClientData,"/clientDataUpdate",callback);
	}
	
	function deleteData (oClientData,callback)
	{
		ajaxCall(oClientData,"/clientDeleteData",callback);
	}
	
	function updateClientBalanceData (oClientData,callback)
	{
		ajaxCall(oClientData,"/updateClientBalanceData",callback);
	}
	
	 return {
		     create : create,
		     list:list,
		     getClientSuggesstions:getClientSuggesstions,
		     get:get,
		     update:update,
		     deleteData:deleteData,
		     updateClientBalanceData:updateClientBalanceData
        }; 

})();