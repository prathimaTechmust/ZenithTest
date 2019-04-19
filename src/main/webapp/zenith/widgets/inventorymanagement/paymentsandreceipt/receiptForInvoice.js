navigate ("Receipt", "widgets/inventorymanagement/paymentsandreceipt/receiptInvoice.js");
function receiptInvoice_loaded ()
{
	m_oReceiptInvoiceMemberData.m_nInvoiceId = m_oInvoiceListMemberData.m_nInvoiceId;
	receiptInvoice_getInvoiceData ();
}