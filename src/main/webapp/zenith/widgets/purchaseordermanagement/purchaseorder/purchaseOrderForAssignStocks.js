navigate ("purchaseOrder", "widgets/purchaseordermanagement/purchaseorder/purchaseOrder.js");

function purchaseOrder_loaded ()
{
	m_oPurchaseOrderMemberData.m_nPurchaseOrderId = m_oPurchaseOrderListMemberData.m_nSelectedPurchaseOrderId;
	loadPage ("purchaseordermanagement/purchaseorder/purchaseOrder.html", "dialog", "purchaseOrder_executePurchaseOrder ()");
}
