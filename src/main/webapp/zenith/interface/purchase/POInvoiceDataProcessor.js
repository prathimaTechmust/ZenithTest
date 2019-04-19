var POInvoiceDataProcessor = (function _POInvoiceDataProcessor ()
{
	function createInvoice (oPOInvoiceData,callback)
	{
		ajaxCall(oPOInvoiceData, "/oPOInvoiceDatacreateInvoice", callback);
		
	}
	
	function updateAndMakeInvoice(oPOInvoiceData,callback)
	{
		ajaxCall(oPOInvoiceData, "/oPOInvoiceDataupdateAndMakeInvoice", callback);
	
	}
	function getInvoices(oPOInvoiceData,callback)
	{
		ajaxCall(oPOInvoiceData, "/oPOInvoiceDatagetInvoices", callback);
		
	}
	
	return { 
		createInvoice : createInvoice,
		updateAndMakeInvoice : updateAndMakeInvoice,
		getInvoices : getInvoices
	};
})(); 