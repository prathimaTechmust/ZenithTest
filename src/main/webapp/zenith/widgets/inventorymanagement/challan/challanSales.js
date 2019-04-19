navigate ('challan','widgets/inventorymanagement/challan/challan.js');

function challan_loaded ()
{
	m_oChallanMemberData.m_strXMLData = m_oSalesMemberData.m_strXMLData;
	m_oChallanMemberData.m_nChallanId = m_oSalesMemberData.m_nChallanId;
	m_oChallanMemberData.m_nInvoiceId = m_oSalesMemberData.m_nInvoiceId;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "challan_print ()");
}
