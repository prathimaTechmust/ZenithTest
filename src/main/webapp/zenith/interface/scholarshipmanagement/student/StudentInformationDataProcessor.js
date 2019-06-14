var StudentInformationDataProcessor = (function __StudentInformationDataProcessor ()
{
	function create (oStudentData,callback)
	{
		ajaxCall(oStudentData, "/studentInfoCreate", callback);
				
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
		multipartAjaxCall(oStudentData, "/createStudentImageData", callback);   
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
		ajaxCall(oStudentData,"/studentVerifiedInfoList",callback);
	}
	
	return { 
		create : create,
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
		getStudentStatuslist : getStudentStatuslist
	};
})();
