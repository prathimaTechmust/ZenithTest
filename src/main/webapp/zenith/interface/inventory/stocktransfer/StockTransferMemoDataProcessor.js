var StockTransferMemoDataProcessor = (function __StockTransferMemoDataProcessor ()
{
	function list(oData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oStockTransferMemoData:oData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper, "/stockTransferMemoList", callback);
	}
	
	function getXML (oData, callback)
	{
		ajaxXMLCall(oData, "/stockTransferMemoGetXML", callback);
	}
	
	function get (oData, callback)
	{
		ajaxCall(oData, "/stockTransferMemoGet", callback);
	}
	
	return {
		list:list,
		getXML:getXML,
		get:get
	}
})();