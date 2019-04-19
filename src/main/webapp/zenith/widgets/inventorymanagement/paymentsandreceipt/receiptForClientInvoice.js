navigate ("Receipt", "widgets/inventorymanagement/paymentsandreceipt/receiptInvoice.js");
function receiptInvoice_loaded ()
{
	m_oReceiptInvoiceMemberData.m_nInvoiceId = m_oClientTransactionMemeberData.m_nSelectedInvoiceId;
	receiptInvoice_getInvoiceData ();
}