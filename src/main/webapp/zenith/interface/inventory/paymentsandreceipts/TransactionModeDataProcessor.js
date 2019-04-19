var TransactionModeDataProcessor = (function __TransactionModeDataProcessor ()
{
	function create (oTransactionMode,callback)
	{
		ajaxCall(oTransactionMode, "/TransactionModeCreate", callback);
	}
	
	function get(oTransactionMode,callback)
	{
		ajaxCall(oTransactionMode, "/TransactionModeGet", callback);
	}
	
	
	function list(oTransactionMode, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oTransactionMode:oTransactionMode,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper, "/TransactionModeList", callback);
	}
	
	function update (oTransactionMode, callback)
	{
		ajaxCall(oTransactionMode, "/TransactionModeUpdate", callback);
	}
	
	function deleteData (oTransactionMode, callback)
	{
		ajaxCall(oTransactionMode, "/TransactionModeDelete", callback);
	}
	
	function getXML (oTransactionMode, callback)
	{
		ajaxXMLCall(oTransactionMode, "/TransactionModeGetXML", callback);
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