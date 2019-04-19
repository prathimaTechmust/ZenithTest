var ChallanDataProcessor = (function __ChallanDataProcessor ()
{
	function create (oChallanData, callback)
	{
		ajaxCall(oChallanData,"/challanCreate",callback);
	}
	
	function deleteData (oChallanData, callback)
	{
		ajaxCall(oChallanData,"/challanDeleteData",callback);	
	}
	
	function get (oChallanData, callback)
	{
		ajaxCall(oChallanData,"/challanGet",callback);
		
	}
	
	function getXML (oChallanData, callback)
	{
		ajaxXMLCall(oChallanData,"/challanGetXML",callback);
	}
	
	function update (oChallanData, callback)
	{
		ajaxCall(oChallenData,"/challanUpdate",callback);
	}
	
	function list(oChallanData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oChallanData:oChallanData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper,"/challanList",callback);
	}
	
	return {
		list:list,
		create:create,
		deleteData:deleteData,
		get:get,
		getXML:getXML,
		update:update
	}
})();