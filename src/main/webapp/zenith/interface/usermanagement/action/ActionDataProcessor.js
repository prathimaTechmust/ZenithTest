var ActionDataProcessor = (function __ActionDataProcessor ()
{
	function create (oActionData,callback)
	{
		ajaxCall(oActionData,"/actionCreate",callback);
	}
	
	function get(oActionData,callback)
	{
		ajaxCall(oActionData,"/actionGet",callback);
	}
	
	function list(oActionData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oAction:oActionData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper,"/actionList",callback);
	}
	
	function update (oActionData, callback)
	{
		 ajaxCall(oActionData,"/actionUpdate",callback);
		
	}
	
	function deleteData (oActionData, callback)
	{
		 ajaxCall(oActionData,"/actionDelete",callback);
		
	}
	
	function getXML (oActionData, callback)
	{
		ajaxXMLCall(oActionData,"/actionGetXML",callback);
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