navigate ("ItemGroupTransaction", "widgets/inventorymanagement/item/itemGroupTransaction.js");

function itemGroupTransaction_loaded ()
{
	m_oItemGroupTransactionData.m_nSelectedItemId = m_oItemGroupListMemberData.m_nSelectedItemGroupId;
	loadPage ("inventorymanagement/item/itemGroupTransaction.html", "dialog", "itemGroupTransaction_init ()");
}
