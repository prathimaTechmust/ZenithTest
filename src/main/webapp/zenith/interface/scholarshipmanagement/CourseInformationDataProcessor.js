var CourseInformationDataProcessor = (function __CourseInformationDataProcessor ()
{
	function create (oCourseData,callback)
	{
		ajaxCall(oCourseData, "/courseInfoCreate", callback);
				
	}
	
	function get(oCourseData,callback)
	{
		ajaxCall(oCourseData, "/courseInfoGet", callback);
		
	}
	
	function list(oCourseData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oCourseData:oCourseData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oCourseData, "/courseInfoList", callback);
		 
	}
	
	function update (oCourseData,callback)
	{
		ajaxCall(oCourseData, "/courseInfoUpdate", callback);
		
	}
	
	function deleteData (oCourseData,callback)
	{
		ajaxCall(oCourseData, "/courseInfoDelete", callback);
		
	}
	
	function getImagePreview (oCourseData, callback)
	{
		ajaxCall(oCourseData, "/courseInfoGetImagePreview", callback);
		
	}
	
	function getXML (oCourseData, callback)
	{
		ajaxXMLCall(oCourseData, "/courseInfoGetXML", callback);
		
	}
	
	function getCourseSuggesstions (oCourseData, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oCourseData:oCourseData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper, "/courseInfoGetSuggestions", callback);
		
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getImagePreview : getImagePreview,
		getXML : getXML,
		getCourseSuggesstions :getCourseSuggesstions
	};
})();
