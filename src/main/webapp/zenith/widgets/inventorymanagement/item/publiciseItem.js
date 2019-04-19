navigate ("ItemPublicise", "widgets/inventorymanagement/item/itemPublicise.js");

function itemPublicise_loaded ()
{
	m_oItemPubliciseMemberData.m_nItemId = m_oItemListMemberData.m_nSelectedItemId;
	loadPage ("inventorymanagement/item/itemPublicise.html", "secondDialog", "itemPublicise_new()");
}