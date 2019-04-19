navigate ("publicise", "widgets/crmanagement/publicise.js");

function publicise_loaded ()
{
	m_oPubliciseMemberData.m_nClientGroupId = m_oClientGroupListMemberData.m_nSelectedClientGroupId;
	loadPage ("crmanagement/publicise.html", "secondDialog", "publicise_new()");
}