navigate ("publicise", "widgets/crmanagement/publicise.js");

function publicise_loaded ()
{
	m_oPubliciseMemberData.m_nItemGroupId = m_oItemGroupListMemberData.m_nSelectedItemGroupId;
	loadPage ("crmanagement/publicise.html", "secondDialog", "publicise_new()");
}