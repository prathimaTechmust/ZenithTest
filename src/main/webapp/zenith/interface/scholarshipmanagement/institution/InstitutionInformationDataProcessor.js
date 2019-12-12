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
		var oZenithHelper = new ZenithHelper ();
		oZenithHelper.m_oInstitutionInformationData = oInstitutionData;
		oZenithHelper.m_nPageNo = nPageNo;
		oZenithHelper.m_nPageSize = nPageSize;
		oZenithHelper.m_strSortColumn = strSortColumn;
		oZenithHelper.m_strOrderBy = strSortOrder;
		ajaxCallList(oZenithHelper, "/institutionInfoList", callback);		 
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
		 ajaxCall(oInstitutionData, "/institutionInfoGetSuggestions", callback);		
	}
	
	function institutionFilterData (oInstitutionFilterData,callback)
	{
		 ajaxCall(oInstitutionFilterData, "/getInstitutionInfoFilterData", callback);
	}
	
	function getChequeInFavourOf (oChequeFavourData,callback)
	{
		
		ajaxCall(oChequeFavourData,"/getChequeFavourData",callback);
	}
	
	function populateDropDownList(oInstitutionData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oZenithHelper = new ZenithHelper ();
		oZenithHelper.m_oInstitutionInformationData = oInstitutionData;
		oZenithHelper.m_nPageNo = nPageNo;
		oZenithHelper.m_nPageSize = nPageSize;
		oZenithHelper.m_strSortColumn = strSortColumn;
		oZenithHelper.m_strOrderBy = strSortOrder;
		ajaxCall(oZenithHelper, "/institutionInfoList", callback);		 
	}
	
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getImagePreview : getImagePreview,
		getXML : getXML,
		getInstitutionSuggesstions :getInstitutionSuggesstions,
		institutionFilterData :institutionFilterData,
		getChequeInFavourOf :getChequeInFavourOf,
		populateDropDownList:populateDropDownList
	};
})();
