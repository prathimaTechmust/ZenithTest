var receiptInvoice_includeDataObjects = 
[
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/clientmanagement/ClientData.js'
];

includeDataObjects (receiptInvoice_includeDataObjects, "receiptInvoice_loaded()");

function receiptInvoice_memberData ()
{
	this.m_nInvoiceId = -1;
	this.m_oInvoiceData = null;
}

var m_oReceiptInvoiceMemberData = new receiptInvoice_memberData ();

function receiptInvoice_getInvoiceData ()
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_nInvoiceId = m_oReceiptInvoiceMemberData.m_nInvoiceId;
	InvoiceDataProcessor.get (oInvoiceData,	receiptInvoice_gotInvoiceData);
}

function receiptInvoice_gotInvoiceData (oResponse)
{
	m_oReceiptInvoiceMemberData.m_oInvoiceData  = oResponse.m_arrInvoice[0];
	navigate ("Receipt", "widgets/inventorymanagement/paymentsandreceipt/receipt.js");
}

function receipt_loaded ()
{
	m_oReceiptMemberData.m_oInvoiceData = m_oReceiptInvoiceMemberData.m_oInvoiceData;
	loadPage ("inventorymanagement/paymentsandreceipt/receipt.html", "thirdDialog", "receipt_makeReceiptForInvoice()");
}