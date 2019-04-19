var NonStockSalesLineItemDataProcessor = (function _NonStockSalesLineItemDataProcessor ()
{
	function list(oNonStockSalesLineItemData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oNonSalesLineItemData:oNonStockSalesLineItemData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/NonStockSalesLineItemDataList", callback);
		
	}
	function getUnbilledNonStockItemList (oNonStockSalesLineItemData,callback)
	{
		 ajaxCall(oNonStockSalesLineItemData, "/NonStockSalesLineItemDatagetUnbilledNonStockItemList", callback);
		
	}
	
	return { 
		list : list,
		getUnbilledNonStockItemList : getUnbilledNonStockItemList
	};
})(); 