navigate ("purchase", "widgets/inventorymanagement/purchase/purchase.js");

function purchase_loaded ()
{
	m_oPurchasememberData.m_nPurchaseId = m_oPurchaseListMemberData.m_nSelectedPurchaseId;
	loadPage ("inventorymanagement/purchase/purchase.html", "dialog", "purchase_edit ()");
}
