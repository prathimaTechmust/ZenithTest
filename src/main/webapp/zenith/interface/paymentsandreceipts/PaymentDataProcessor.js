var PaymentDataProcessor = (function __PaymentDataProcessor ()
{
	function create (oPaymentData,callback)
	{
		ajaxCall(oPaymentData, "/paymentDataCreate", callback);
	}
	
	function get(oPaymentData,callback)
	{
		ajaxCall(oPaymentData, "/paymentDataGet", callback);	
	}
	
	function list(oPaymentData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		 var oTradeMustHelper = {
				 m_oPaymentData:oPaymentData,
				 m_strColumn:strSortColumn, 
				 m_strOrderBy:strSortOrder,
				 m_nPageNo:nPageNo,
				 m_nPageSize:nPageSize
		} 
		 ajaxCall(oTradeMustHelper, "/paymentDataList", callback);	
	}
	
	function update (oPaymentData, callback)
	{
		 ajaxCall(oPaymentData, "/paymentDataUpdate", callback);	
	}
	
	function deleteData (oPaymentData, callback)
	{
		 ajaxCall(oPaymentData, "/paymentDataDelete", callback);
	}
	
	function getXML (oPaymentData, callback)
	{
		ajaxXMLCall(oPaymentData, "/paymentDataGetXML", callback);
	}
	
	function saveAndPrint(oPaymentData,callback)
	{
		ajaxCall(oPaymentData, "/paymentDataSaveAndPrint", callback);
	}
	function getReports(oPaymentData,callback)
	{
		ajaxCall(oPaymentData, "/paymentDataGetReports", callback);
	}
	
	return { 
		create : create,
		get : get,
		list : list,
		update :update,
		deleteData : deleteData, 
		getXML : getXML,
		saveAndPrint : saveAndPrint,
		getReports : getReports
	};
})();