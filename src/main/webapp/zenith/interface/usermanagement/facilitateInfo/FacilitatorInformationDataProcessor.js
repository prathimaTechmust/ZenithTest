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
		 ajaxCall(oFacilitatorData, "/facilitatorInfoGetFacilitatorSuggestions", callback);		
	}
	
	function filterFacilitatorData (oFacilitatorFilterData,callback)
	{
		 ajaxCall(oFacilitatorFilterData, "/getFilterFacilitatorData", callback);	
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getImagePreview : getImagePreview,
		getXML : getXML,
		getFacilitatorSuggesstions :getFacilitatorSuggesstions,
		filterFacilitatorData :filterFacilitatorData
	};
})();
