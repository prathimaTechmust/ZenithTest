navigate ('invoice','widgets/inventorymanagement/invoice/invoice.js');

function invoice_loaded ()
{
	m_oInvoiceMemberData.m_strXMLData = m_oClientTransactionMemeberData.m_strXmlData;
	m_oInvoiceMemberData.m_oInvoiceData = m_oClientTransactionMemeberData.m_oSelectedInvoiceRowData;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "invoice_print ()");
}