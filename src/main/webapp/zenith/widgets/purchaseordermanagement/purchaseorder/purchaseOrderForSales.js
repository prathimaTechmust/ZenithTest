navigate ("purchaseOrder", "widgets/purchaseordermanagement/purchaseorder/purchaseOrder.js");

function purchaseOrder_loaded ()
{
	m_oPurchaseOrderMemberData.m_nPurchaseOrderId = m_oSalesListMemberData.m_nPurchaseOrderId;
	loadPage ("purchaseordermanagement/purchaseorder/purchaseOrder.html", "dialog", "purchaseOrder_executePurchaseOrder ()");
}
