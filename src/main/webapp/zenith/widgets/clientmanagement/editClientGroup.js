navigate("clientGroup", "widgets/clientmanagement/clientGroup.js");

function clientGroup_loaded ()
{
	m_oClientGroupMemberData.m_nGroupId = m_oClientGroupListMemberData.m_nSelectedClientGroupId;
	loadPage("clientmanagement/clientGroup.html", "dialog", "clientGroup_edit ()")
}