navigate ('challan invoice','widgets/inventorymanagement/invoice/invoice.js');

function invoice_loaded ()
{
	m_oInvoiceMemberData.m_strXMLData = m_oInvoiceFromChallanMemberData.m_strXMLData;
	m_oInvoiceMemberData.m_oInvoiceData = m_oInvoiceFromChallanMemberData.m_oInvoiceData;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "invoice_print ()");
}

