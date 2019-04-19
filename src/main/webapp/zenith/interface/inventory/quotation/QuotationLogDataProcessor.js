var QuotationLogDataProcessor = (function __QuotationLogDataProcessor ()
{
	function create (oQuotationLogData, callback)
	{
		ajaxCall(oQuotationLogData, "/quotationLogDatacreate", callback);
	}
	
	function list(oQuotationLogData, strSortColumn, strSortColumn, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oQuotationLogData:oQuotationLogData,
				 m_strColumn:strSortColumn,
				 m_strOrderBy:strSortColumn,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/quotationLogDataList", callback);
	}
	
	return {
		create:create,
		list:list
	}
})();