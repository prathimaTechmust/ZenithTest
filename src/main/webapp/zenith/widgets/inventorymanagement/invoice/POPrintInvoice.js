navigate ('invoice','widgets/inventorymanagement/invoice/invoice.js');

function invoice_loaded ()
{
	m_oInvoiceMemberData.m_strXMLData = m_oPurchaseOrderListMemberData.m_strXMLData;
	m_oInvoiceMemberData.m_oInvoiceData = m_oPurchaseOrderListMemberData.m_oSelectedInvoiceData;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "invoice_print ()");
}