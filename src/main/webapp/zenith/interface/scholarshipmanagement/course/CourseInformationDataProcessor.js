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
		var oZenithHelper = new ZenithHelper ();
		oZenithHelper.m_nPageNo = nPageNo;
		oZenithHelper.m_nPageSize = nPageSize;
		oZenithHelper.m_strSortColumn = strSortColumn;
		oZenithHelper.m_strOrderBy = strSortOrder;
		oZenithHelper.m_oCourseInformationData = oCourseData;
		ajaxCallList(oZenithHelper, "/courseInfoList", callback);		 
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
		 ajaxCall(oCourseData, "/courseInfoGetSuggestions", callback);		
	}
	
	function courseFilterData(oCourseFilterData,callback)
	{		
		 ajaxCall(oCourseFilterData, "/courseFilterInfoData", callback);
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getImagePreview : getImagePreview,
		getXML : getXML,
		getCourseSuggesstions :getCourseSuggesstions,
		courseFilterData :courseFilterData
	};
})();
