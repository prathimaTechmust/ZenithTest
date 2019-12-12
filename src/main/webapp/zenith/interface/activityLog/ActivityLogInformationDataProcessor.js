var ActivityLogInformationDataProcessor = (function __ActivityLogInformationDataProcessor ()
{
	
	function list(oActivityLogData,strSortColumn, strSortOrder, nPageNo, nPageSize,callback)
	{
		var oZenithHelper = new ZenithHelper ();
		oZenithHelper.m_oActivityLog = oActivityLogData;
		oZenithHelper.m_nPageNo = nPageNo;
		oZenithHelper.m_nPageSize = nPageSize;
		oZenithHelper.m_strSortColumn = strSortColumn;
		oZenithHelper.m_strOrderBy = strSortOrder;
		ajaxCallList(oZenithHelper, "/activityLogListInfo", callback);
	}
	
	function sortingList(oZenithHelperData,callback)
	{
		ajaxCall(oZenithHelperData, "/sortActivityLogListInfo", callback);
	}
	
	function getXML (oActivityLogData, callback)
	{
		ajaxXMLCall(oActivityLogData, "/activityLogInfoGetXML", callback);
		
	}
	
	function getLoginUsers (oLoginUsersData,callback)
	{
		ajaxCall(oLoginUsersData,"/getLoginUsersList",callback);
	}
	
	function getFilteredActivityLog(oFilteredActivityLog,callback)
	{
		ajaxCall(oFilteredActivityLog,"/getFilteredActivityLog",callback);
	}
	return { 	
		sortingList : sortingList,
		list:list,
		getXML:getXML,
		getLoginUsers :getLoginUsers,
		getFilteredActivityLog :getFilteredActivityLog
	};
})();
