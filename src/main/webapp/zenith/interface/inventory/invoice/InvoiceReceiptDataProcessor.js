var InvoiceReceiptDataProcessor = (function _InvoiceReceiptDataProcessor ()
{
	function list(oInvoiceReceiptData, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oInvoiceReceiptData:oInvoiceReceiptData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper,"/InvoiceReceiptList",callback);
	}
	return { 
		list : list
	};
})();