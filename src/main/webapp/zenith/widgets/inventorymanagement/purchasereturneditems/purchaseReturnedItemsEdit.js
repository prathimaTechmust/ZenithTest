navigate ("", "widgets/inventorymanagement/purchasereturneditems/purchaseReturnedItems.js");

function purchaseReturnedItems_loaded ()
{
	m_oPurchaseReturnedItemsMemberData.m_nPurchaseReturnedId = m_oPurchaseReturnedItemsListMemberData.m_nSelectedId;
	loadPage ("inventorymanagement/purchasereturneditems/purchaseReturnedItems.html", "dialog", "purchaseReturnedItems_edit()");
}
