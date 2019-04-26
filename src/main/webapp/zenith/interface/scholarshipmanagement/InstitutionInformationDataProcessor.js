var InstitutionInformationDataProcessor = (function __InstitutionInformationDataProcessor ()
{
	function create (oInstitutionData,callback)
	{
		ajaxCall(oInstitutionData, "/institutionInfoCreate", callback);
				
	}
	
	function get(oInstitutionData,callback)
	{
		ajaxCall(oInstitutionData, "/institutionInfoGet", callback);
		
	}
	
	function list(oInstitutionData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oInstitutionData:oInstitutionData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oInstitutionData, "/institutionInfoList", callback);
		 
	}
	
	function update (oInstitutionData,callback)
	{
		ajaxCall(oInstitutionData, "/institutionInfoUpdate", callback);
		
	}
	
	function deleteData (oInstitutionData,callback)
	{
		ajaxCall(oInstitutionData, "/institutionInfoDelete", callback);
		
	}
	
	function getImagePreview (oInstitutionData, callback)
	{
		ajaxCall(oInstitutionData, "/institutionInfoGetImagePreview", callback);
		
	}
	
	function getXML (oInstitutionData, callback)
	{
		ajaxXMLCall(oInstitutionData, "/institutionInfoGetXML", callback);
		
	}
	
	function getInstitutionSuggesstions (oInstitutionData, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oInstitutionData:oInstitutionData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper, "/institutionInfoGetSuggestions", callback);
		
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getImagePreview : getImagePreview,
		getXML : getXML,
		getInstitutionSuggesstions :getInstitutionSuggesstions
	};
})();
