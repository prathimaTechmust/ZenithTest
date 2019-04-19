var StockTransferDataProcessor = (function __StockTransferDataProcessor ()
{
	function list(oStockTransferData, strColumn, strOrderBy, nPageNumber, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oStockTransferData:oStockTransferData,
				m_strColumn:strColumn, 
				m_strOrderBy:strOrderBy,
				m_nPageNo: nPageNumber,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper, "/stockTransferList", callback);
	}
	
	function transfer (oStockTransferData, callback)
	{
		ajaxCall(oStockTransferData, "/stockTransferCreate", callback);
	}

	
	return {
		transfer:transfer,
		list:list
	};
})();