var SalesLineItemDataProcessor = (function __SalesLineItemDataProcessor ()
{
	function getPreviousSales(strArticleNumber, nClientID, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_strArticleNumber:strArticleNumber,
				m_nClientId:nClientID
		} 
		ajaxCall(oTradeMustHelper,"/getPreviousSales",callback);
	}
	
	function list(oSalesData, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oSalesData:oSalesData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper,"/saleslineitemDataList",callback);
	}
	
	return {
		getPreviousSales:getPreviousSales,
		list:list
	}
})();