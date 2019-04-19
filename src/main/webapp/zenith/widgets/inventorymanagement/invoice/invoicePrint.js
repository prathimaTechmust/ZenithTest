navigate ('invoice','widgets/inventorymanagement/invoice/invoice.js');

function invoice_loaded ()
{
	m_oInvoiceMemberData.m_strXMLData = m_oInvoiceListMemberData.m_strXMLData;
	m_oInvoiceMemberData.m_oInvoiceData = m_oInvoiceListMemberData.m_oRowData;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "invoice_print ()");
}