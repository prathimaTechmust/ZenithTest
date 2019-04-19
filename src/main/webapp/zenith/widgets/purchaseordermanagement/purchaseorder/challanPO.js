navigate ('challan','widgets/inventorymanagement/challan/challan.js');

function challan_loaded ()
{
	m_oChallanMemberData.m_strXMLData = m_oPurchaseOrderMemberData.m_strXMLData;
	m_oChallanMemberData.m_nChallanId = m_oPurchaseOrderMemberData.m_nChallanId;
	m_oChallanMemberData.m_nInvoiceId = m_oPurchaseOrderMemberData.m_nInvoiceId;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "challan_print ()");
}
