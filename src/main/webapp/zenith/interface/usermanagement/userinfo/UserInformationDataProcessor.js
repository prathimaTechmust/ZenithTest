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
	
	function setImagetoS3bucket(oUserData,callback)
	{
		
		multipartAjaxCall(oUserData,"/userImageCreate",callback);
	}
	
	function list(oUserData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oZenithHelper = new ZenithHelper ();
		 var oZenithHelper = {
				 m_oUserData:oUserData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCallList(oZenithHelper, "/userInfoList", callback);
		 
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
		var oZenithHelper = new ZenithHelper ();
		 var oZenithHelper = {
				 m_oUserData:oUserData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oZenithHelper, "/userInfoGetUserSuggestions", callback);
		
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getImagePreview : getImagePreview,
		getXML : getXML,
		getUserSuggesstions :getUserSuggesstions,
		setImagetoS3bucket :setImagetoS3bucket
	};
})();
