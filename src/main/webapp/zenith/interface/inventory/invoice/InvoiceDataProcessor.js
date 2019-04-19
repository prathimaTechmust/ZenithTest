var InvoiceDataProcessor = (function __InvoiceDataProcessor ()
{
	
	function getClientDetails(oInvoiceData,callback)
	{
		ajaxXMLCall(oInvoiceData, "/invoiceDatagetClientDetails", callback);
	}
	function create (oInvoiceData,callback)
	{
		ajaxCall(oInvoiceData, "/invoiceDataCreate", callback);
	}
	function deleteData (oInvoiceData,callback)
	{
		ajaxCall(oInvoiceData, "/invoiceDatadeleteData", callback);
	}
	function get (oInvoiceData,callback)
	{
		ajaxCall(oInvoiceData, "/invoiceDataget", callback);
	}
	function getXML (oInvoiceData,callback)
	{
		ajaxXMLCall(oInvoiceData, "/invoiceDatagetXML", callback);
	}
	function update (oInvoiceData,callback)
	{
		ajaxCall(oInvoiceData, "/invoiceDataupdate", callback);
	}
	function getAgeWiseInvoices (oInvoiceData,callback)
	{
		ajaxCall(oInvoiceData, "/invoiceDatagetAgeWiseInvoices", callback);
	}
	
	function list(oData, strSortColumn, strSortOrder, nPageNo, nPageSize, callback)
	{
		var oTradeMustHelper = new TradeMustHelper ();
		var oTradeMustHelper = {
				m_oInvoiceData:oData,
				m_strColumn:strSortColumn, 
				m_strOrderBy:strSortOrder,
				m_nPageNo: nPageNo,
				m_nPageSize: nPageSize
		} 
		ajaxCall(oTradeMustHelper, "/invoiceList", callback);
	}
	
	return {
		getClientDetails:getClientDetails,
		list:list,
		create: create,
		deleteData: deleteData,
		get: get,
		getXML: getXML,
		getAgeWiseInvoices:getAgeWiseInvoices,
		update: update
		};
	
})();