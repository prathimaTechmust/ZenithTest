navigate ("", "widgets/inventorymanagement/returneditems/returnedItems.js");

function returnedItems_loaded ()
{
	m_oReturnedItemsMemberData.m_nReturnedId = m_oReturnedItemsListMemberData.m_nSelectedId;
	loadPage ("inventorymanagement/returneditems/returnedItems.html", "dialog", "returnedItems_edit()");
}
