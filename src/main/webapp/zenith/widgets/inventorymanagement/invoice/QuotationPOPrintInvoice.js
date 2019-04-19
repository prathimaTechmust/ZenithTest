navigate ('invoice','widgets/inventorymanagement/invoice/invoice.js');

function invoice_loaded ()
{
	m_oInvoiceMemberData.m_strXMLData = m_oQuotationListMemberData.m_strXMLData;
	m_oInvoiceMemberData.m_oInvoiceData = m_oQuotationListMemberData.m_oInvoiceData;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "invoice_print ()");
}