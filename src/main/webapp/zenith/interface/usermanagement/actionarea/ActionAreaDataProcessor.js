var ActionAreaDataProcessor = (function __ActionAreaDataProcessor ()
{
	function create (oActionAreaData,callback)
	{
		ajaxCall(oActionAreaData,"/actionAreaCreate",callback);
	}
	
	function get(oActionAreaData,callback)
	{
		ajaxCall(oActionAreaData,"/actionAreaGet",callback);
	}
	
	function list(oActionAreaData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oZenithHelper = new ZenithHelper ();
		 var oTradeMustHelper = {
				 m_oActionArea:oActionAreaData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oZenithHelper,"/actionAreaList",callback);
	}
	
	function update (oActionAreaData, callback)
	{
		ajaxCall(oActionAreaData,"/actionAreaUpdate",callback);
	}
	
	function deleteData (oActionAreaData, callback)
	{
		ajaxCall(oActionAreaData,"/actionAreaDelete",callback);
	}
	
	function getXML (oActionAreaData, callback)
	{
		ajaxXMLCall(oActionAreaData,"/actionAreaGetXML",callback);
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getXML : getXML
	};
})();