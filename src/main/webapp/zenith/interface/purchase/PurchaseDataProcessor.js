var PurchaseDataProcessor = (function _PurchaseDataProcessor ()
{
	function create (oPurchaseData,callback)
	{
		ajaxCall(oPurchaseData, "/PurchaseDataCreate", callback);
		
	}
	
	function get(oPurchaseData,callback)
	{
		ajaxCall(oPurchaseData, "/PurchaseDataGet", callback);
		
	}
	
	
	function list(oPurchaseData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oPurchaseData:oPurchaseData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/PurchaseDataList", callback);
		
	}
	
	function updatePurchase (oPurchaseData, callback)
	{
		ajaxCall(oPurchaseData, "/PurchaseDataUpdate", callback);
		
	}
	
	function deleteData (oPurchaseData, callback)
	{
		ajaxCall(oPurchaseData, "/PurchaseDataDelete", callback);
		
	}
	
	function getXML (oPurchaseData, callback)
	{
		ajaxXMLCall(oPurchaseData, "/PurchaseDataGetXML", callback);
		                               
	}
	
	function enterInvoice(oPurchaseData,callback)
	{
		ajaxCall(oPurchaseData, "/PurchaseDataEnterInvoice", callback);
		
	}
	
	function createWithLocation(oPurchaseData, oLocationData, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oPurchaseData:oPurchaseData,
				 m_oLocationData:oLocationData
		} 
		 ajaxCall(oTradeMustHelper, "/PurchaseDataCreateWithLocation", callback);
		
	}
	
	function updatePurchaseTable(oPurchaseData,callback)
	{
		ajaxCall(oPurchaseData, "/PurchaseDataUpdatePurchaseTable", callback);
		
	}
	
	function getVendorReport (oPurchaseData, strColumn, strOrderBy, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oPurchaseData:oPurchaseData,
				 m_strColumn:strColumn, 
				 m_strOrderBy:strOrderBy
		} 
		 ajaxCall(oTradeMustHelper, "/getVendorReport", callback);
		
	}
	
	function getAgeWiseInvoices(oPurchaseData,callback)
	{
		ajaxCall(oPurchaseData, "/getAgeWiseInvoices", callback);
		
	}
	
	function getVendorDetails(nVendorID,callback)
	{
		ajaxXMLCall(nVendorID, "/getVendorDetails", callback);
		
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		updatePurchase :updatePurchase,
		deleteData : deleteData, 
		getXML : getXML,
		enterInvoice : enterInvoice,
		createWithLocation : createWithLocation,
		updatePurchaseTable : updatePurchaseTable,
		getVendorReport:getVendorReport,
		getAgeWiseInvoices:getAgeWiseInvoices,
		getVendorDetails:getVendorDetails
		
	};
})(); 