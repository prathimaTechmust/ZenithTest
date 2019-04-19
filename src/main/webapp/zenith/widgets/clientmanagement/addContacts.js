navigate ("clientInfo", "widgets/clientmanagement/clientInfo.js");

function clientInfo_loaded ()
{
	m_oClientInfoMemberData.m_nClientId = m_oTrademustMemberData.m_nSelectedClientId;
	loadPage ("clientmanagement/clientInfo.html", "thirdDialog", "clientInfo_addContacts()");
}