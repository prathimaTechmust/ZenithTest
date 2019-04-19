var PurchasePaymentDataProcessor = (function _PurchasePaymentDataProcessor ()
{
	function create (oPurchasePaymentData,callback)
	{
		ajaxCall(oPurchasePaymentData, "/PurchasePaymentDataCreate", callback);
		
	}
	
	function get(oPurchasePaymentData,callback)
	{
		ajaxCall(oPurchasePaymentData, "/PurchasePaymentDataGet", callback);
		
	}
	
	
	function list(oPurchasePaymentData, strSortColumn, strSortOrder, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oPurchasePaymentData:oPurchasePaymentData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder
		} 
		 ajaxCall(oTradeMustHelper, "/PurchasePaymentDataList", callback);
		
	}
	
	function update (oPurchasePaymentData, callback)
	{
		ajaxCall(oPurchasePaymentData, "/PurchasePaymentDataUpdate", callback);
		
	}
	
	function deleteData (oPurchasePaymentData, callback)
	{
		ajaxCall(oPurchasePaymentData, "/PurchasePaymentDataDelete", callback);
		
	}
	
	function getXML (oPurchasePaymentData, callback)
	{
		ajaxXMLCall(oPurchasePaymentData, "/PurchasePaymentDataGetXML", callback);
		                      
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