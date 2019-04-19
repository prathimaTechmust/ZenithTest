navigate ('challan','widgets/inventorymanagement/challan/challan.js');

function challan_loaded ()
{
	m_oChallanMemberData.m_strXMLData = m_oChallanListMemberData.m_strXMLData;
	m_oChallanMemberData.m_nInvoiceId = m_oChallanListMemberData.m_nInvoiceId;
	m_oChallanMemberData.m_nChallanId = m_oChallanListMemberData.m_nChallanId;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "challan_print ()");
}