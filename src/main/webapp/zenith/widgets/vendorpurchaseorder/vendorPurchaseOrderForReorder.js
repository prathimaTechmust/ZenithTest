navigate ("vendorPurchaseOrderNew", "widgets/vendorpurchaseorder/vendorPurchaseOrder.js");

function vendorPurchaseOrder_loaded ()
{
	m_oVendorPurchaseOrderMemberData.m_bIsForReorder = true;
	m_oVendorPurchaseOrderMemberData.m_oItemData = m_oReorderListMemberData.m_arrSelectedData;
	loadPage ("vendorpurchaseorder/vendorPurchaseOrder.html", "dialog", "vendorPurchaseOrder_new ()");
}
