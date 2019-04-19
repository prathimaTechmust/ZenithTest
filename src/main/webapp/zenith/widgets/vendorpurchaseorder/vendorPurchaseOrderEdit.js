navigate ("vendorPurchaseOrder", "widgets/vendorpurchaseorder/vendorPurchaseOrder.js");

function vendorPurchaseOrder_loaded ()
{
	m_oVendorPurchaseOrderMemberData.m_nPurchaseOrderId = m_oVendorPurchaseOrderListMemberData.m_nSelectedPurchaseOrderId;
	loadPage ("vendorpurchaseorder/vendorPurchaseOrder.html", "dialog", "vendorPurchaseOrder_executePurchaseOrder ()");
}