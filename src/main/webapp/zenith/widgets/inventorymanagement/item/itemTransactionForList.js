navigate ("ItemTransaction", "widgets/inventorymanagement/item/itemTransaction.js");

function itemTransaction_loaded ()
{
	m_oItemTransactionData.m_nSelectedItemId = m_oItemListMemberData.m_nSelectedItemId;
	loadPage ("inventorymanagement/item/itemTransaction.html", "dialog", "itemTransaction_init ()");
}
