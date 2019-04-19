var UserInformationDataProcessor = (function __UserInformationDataProcessor ()
{
	function create (oUserData,callback)
	{
		ajaxCall(oUserData, "/userInfoCreate", callback);
				
	}
	
	function get(oUserData,callback)
	{
		ajaxCall(oUserData, "/userInfoGet", callback);
		
	}
	
	function list(oUserData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oUserData:oUserData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/userInfoList", callback);
		 
	}
	
	function update (oUserData,callback)
	{
		ajaxCall(oUserData, "/userInfoUpdate", callback);
		
	}
	
	function deleteData (oUserData,callback)
	{
		ajaxCall(oUserData, "/userInfoDelete", callback);
		
	}
	
	function getImagePreview (oUserData, callback)
	{
		ajaxCall(oUserData, "/userInfoGetImagePreview", callback);
		
	}
	
	function getXML (oUserData, callback)
	{
		ajaxXMLCall(oUserData, "/userInfoGetXML", callback);
		
	}
	
	function getUserSuggesstions (oUserData, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oUserData:oUserData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper, "/userInfoGetUserSuggestions", callback);
		
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getImagePreview : getImagePreview,
		getXML : getXML,
		getUserSuggesstions :getUserSuggesstions
	};
})();
