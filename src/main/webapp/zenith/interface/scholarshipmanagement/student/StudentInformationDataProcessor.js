var StudentInformationDataProcessor = (function __StudentInformationDataProcessor ()
{
	function create (oStudentData,callback)
	{
		ajaxCall(oStudentData, "/studentInfoCreate", callback);
		//multipartAjaxCall(oStudentData, "/studentInfoCreate", callback);				
	}
	
	function createandprint(oStudentData,callback)
    {
        ajaxCall(oStudentData, "/studentInfoCreateAndPrint", callback);
    }
	
	function get(oStudentData,callback)
	{
		ajaxCall(oStudentData, "/studentInfoGet", callback);
		
	}
	
	function getStudentUID(oStudentData,callback)
	{
		
		ajaxCall(oStudentData, "/studentInfoGetUIDData", callback);
	}
	
	function setImagetoS3bucket(oStudentData,callback)
	{
		multipartAjaxCall(oStudentData, "/uploadStudentImageData", callback);   
	}
	
	function list(oStudentData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{		
		 ajaxCall(oStudentData, "/studentInfoList", callback);		 
	}
	
	function update (oStudentData,callback)
	{
		ajaxCall(oStudentData, "/studentInfoUpdate", callback);
		
	}
	
	function deleteData (oStudentData,callback)
	{
		ajaxCall(oStudentData, "/studentInfoDelete", callback);
		
	}
	
	function getImagePreview (oStudentData, callback)
	{
		ajaxCall(oStudentData, "/studentInfoGetImagePreview", callback);
		
	}
	
	function getXML (oStudentData, callback)
	{
		ajaxXMLCall(oStudentData, "/studentInfoGetXML", callback);
		
	}
	
	function getStudentSuggesstions (oStudentData, strSortColumn, strSortOrder, callback)
	{
		ajaxCall(oStudentData, "/studentInfoGetSuggestions", callback);		
	}
	
	function checkAadharExist (oStudentData,callback)
	{
		ajaxCall(oStudentData, "/isAadharnumberExist", callback);
	}
	
	function getStudentStatuslist(oStudentData,callback)
	{
		ajaxCall(oStudentData,"/studentStatusInfoList",callback);
	}
	
	function getStudentDataUID (oStudentFormUIDData,callback)
	{
		ajaxCall(oStudentFormUIDData,"/getStudentUIDData",callback);
	}

	function getFacilitatorWiseStudent(oFacilitatorStudentData, callback) 
	{
	   ajaxCall(oFacilitatorStudentData,"/getFacilitatorWiseData", callback)	
	}

	function filterStudentData(oStudentFilterData,callback)
	{
		ajaxCall(oStudentFilterData,"/getStudentFilterData",callback);		
	}
	
	function studentTatkalList(oStudentData,callback)
	{
		ajaxCall(oStudentData,"/getTatkalStudentList",callback);
	}
	
	function updateTatkalPriority (oStudentTakalData,callback)
	{
		ajaxCall(oStudentTakalData,"/updateApplicationPriority",callback);
		
	}	
	return { 
		create : create,
		createandprint:createandprint,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getImagePreview : getImagePreview,
		getXML : getXML,
		getStudentSuggesstions :getStudentSuggesstions,
		setImagetoS3bucket :setImagetoS3bucket,
		getStudentUID :getStudentUID,
		checkAadharExist:checkAadharExist,
		getStudentStatuslist : getStudentStatuslist,
		getStudentDataUID:getStudentDataUID,
		getFacilitatorWiseStudent:getFacilitatorWiseStudent,
		filterStudentData:filterStudentData,
		studentTatkalList:studentTatkalList,
		updateTatkalPriority:updateTatkalPriority
	};
})();
