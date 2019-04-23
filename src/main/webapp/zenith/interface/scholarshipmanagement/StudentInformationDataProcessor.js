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
	
	function setImagetoS3bucket(oStudentData,callback)
	{
		multipartAjaxCall(oStudentData, "/createStudentImageData", callback);   
	}
	
	function list(oStudentData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oStudentData:oStudentData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
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
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oStudentData:oStudentData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper, "/studentInfoGetSuggestions", callback);
		
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
		setImagetoS3bucket :setImagetoS3bucket
	};
})();
