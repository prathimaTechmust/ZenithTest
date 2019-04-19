var ClientGroupDataProcessor = (function __ClientGroupDataProcessor()
{
	function create(oClientGroupData,callback)
	{
		ajaxCall(oClientGroupData,"/clientGroupDataCreate",callback);
	}
	
	function get(oClientGroupData,callback)
	{
		ajaxCall(oClientGroupData,"/clientGroupDataGet",callback);
	}

	function list(oClientGroupData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oClientGroup:oClientGroupData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper,"/clientGroupDataList",callback);
	}

	function update(oClientGroupData,callback)
	{
		ajaxCall(oClientGroupData,"/clientGroupDataUpdate",callback);
	}
	
	function getXML(oClientGroupData,callback)
	{
		ajaxXMLCall(oClientGroupData,"/clientGroupDataGetXML",callback);
	}
	
	function deleteData(oClientGroupData,callback)
	{
		ajaxCall(oClientGroupData,"/clientGroupDataDelete",callback);
	}
	 return {
		     create :create,
		     list:list,
		     get:get,
		     update:update,
		     getXML:getXML,
		     deleteData:deleteData
        }; 

})();