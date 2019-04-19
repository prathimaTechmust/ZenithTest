var FacilitatorInformationDataProcessor = (function __FacilitatorInformationDataProcessor ()
{
	function create (oFacilitatorData,callback)
	{
		ajaxCall(oFacilitatorData, "/facilitatorInfoCreate", callback);
				
	}
	
	function get(oFacilitatorData,callback)
	{
		ajaxCall(oFacilitatorData, "/facilitatorInfoGet", callback);
		
	}
	
	function list(oFacilitatorData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oFacilitatorData:oFacilitatorData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oFacilitatorData, "/facilitatorInfoList", callback);
		 
	}
	
	function update (oFacilitatorData,callback)
	{
		ajaxCall(oFacilitatorData, "/facilitatorInfoUpdate", callback);
		
	}
	
	function deleteData (oFacilitatorData,callback)
	{
		ajaxCall(oFacilitatorData, "/facilitatorInfoDelete", callback);
		
	}
	
	function getImagePreview (oFacilitatorData, callback)
	{
		ajaxCall(oFacilitatorData, "/facilitatorInfoGetImagePreview", callback);
		
	}
	
	function getXML (oFacilitatorData, callback)
	{
		ajaxXMLCall(oFacilitatorData, "/facilitatorInfoGetXML", callback);
		
	}
	
	function getFacilitatorSuggesstions (oFacilitatorData, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oFacilitatorData:oFacilitatorData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper, "/facilitatorInfoGetFacilitatorSuggestions", callback);
		
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getImagePreview : getImagePreview,
		getXML : getXML,
		getFacilitatorSuggesstions :getFacilitatorSuggesstions
	};
})();
