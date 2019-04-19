var OrganizationDataProcessor = (function __OrganizationDataProcessor ()
{
	function create (oOrganizationData,callback)
	{
		ajaxCall(oOrganizationData, "/organizationInfoCreate", callback);
				
	}
	
	function get(oOrganizationData,callback)
	{
		ajaxCall(oOrganizationData, "/organizationInfoGet", callback);
		
	}
	
	function list(oOrganizationData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oOrganizationData:oOrganizationData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/organizationInfoList", callback);
		 
	}
	
	function update (oOrganizationData,callback)
	{
		ajaxCall(oOrganizationData, "/organizationInfoUpdate", callback);
		
	}
	
	function deleteData (oOrganizationData,callback)
	{
		ajaxCall(oOrganizationData, "/organizationInfoDelete", callback);
		
	}
	
	function getImagePreview (oOrganizationData, callback)
	{
		ajaxCall(oOrganizationData, "/organizationInfoGetImagePreview", callback);
		
	}
	
	function getXML (oOrganizationData, callback)
	{
		ajaxXMLCall(oOrganizationData, "/organizationInfoGetXML", callback);
		
	}
	
	function getorganizationSuggesstions (oOrganizationData, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oOrganizationData:oOrganizationData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper, "/organizationInfoGetorganizationSuggestions", callback);
		
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getImagePreview : getImagePreview,
		getXML : getXML,
		getorganizationSuggesstions :getorganizationSuggesstions
	};
})();
