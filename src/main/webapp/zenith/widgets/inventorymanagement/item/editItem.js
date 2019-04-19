navigate ("item", "widgets/inventorymanagement/item/item.js");

function item_loaded ()
{
	m_oItemMemberData.m_nItemId = m_oItemListMemberData.m_nSelectedItemId;
	loadPage ("inventorymanagement/item/item.html", "secondDialog", "item_edit ()");
}


