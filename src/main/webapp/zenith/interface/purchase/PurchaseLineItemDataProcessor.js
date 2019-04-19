var PurchaseLineItemDataProcessor = (function _PurchaseLineItemDataProcessor ()
{
function list(oPurchaseLineItem, strColumn, strOrderBy, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oPurchaseLineItem:oPurchaseLineItem,
				 m_strColumn:strColumn, 
				 m_strOrderBy:strOrderBy
		} 
		 ajaxCall(oTradeMustHelper, "/PurchaseLineItemDataList", callback);
		
	}
return { 
	list : list
};
})();