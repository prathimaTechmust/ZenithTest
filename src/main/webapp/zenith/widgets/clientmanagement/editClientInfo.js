navigate ("clientInfo", "widgets/clientmanagement/clientInfo.js");
function clientInfo_loaded ()
{
	m_oClientInfoMemberData.m_nClientId = m_oClientListMemberData.m_oSelectedClientId
	loadPage ("clientmanagement/clientInfo.html", "thirdDialog", "clientInfo_edit()");
}

