navigate ("purchase", "widgets/inventorymanagement/purchase/purchase.js");

function purchase_loaded ()
{
	m_oPurchasememberData.m_bIsForPucchase = true;
	m_oPurchasememberData.m_oVendorPurchaseOrderData = m_oVendorPurchaseOrderMemberData.m_oVendorPurchaseOrderData;
	loadPage ("inventorymanagement/purchase/purchase.html", "dialog", "purchase_new ()");
}
