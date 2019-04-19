navigate ("itemCategory", "widgets/inventorymanagement/item/itemCategory.js");

function itemCategory_loaded ()
{
	m_oItemCategoryMemberData.m_nCategoryId = m_oItemCategoryListMemberData.m_nSelectedCategoryId;
	loadPage ("inventorymanagement/item/itemCategory.html", "thirdDialog", "itemCategory_edit ()");
}
