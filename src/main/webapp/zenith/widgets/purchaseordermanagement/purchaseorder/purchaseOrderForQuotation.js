navigate ("purchaseOrder", "widgets/purchaseordermanagement/purchaseorder/purchaseOrder.js");

function purchaseOrder_loaded ()
{
	m_oPurchaseOrderMemberData.m_bIsForQuotation = true;
	m_oPurchaseOrderMemberData.m_oQuotationData = m_oQuotationListMemberData.m_oQuotationData;
	loadPage ("purchaseordermanagement/purchaseorder/purchaseOrder.html", "dialog", "purchaseOrder_new ()");
}
