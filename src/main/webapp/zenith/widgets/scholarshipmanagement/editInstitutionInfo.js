navigate ("newinstitution", "widgets/scholarshipmanagement/institutionInfo.js");
function institutionInfo_loaded ()
{
	m_oInstitutionInfoMemberData.m_nInstitutionId = m_oInstitutionsInfoListMemberData.m_nSelectedInstitutionId;
	loadPage ("scholarshipmanagement/institutionInfo.html", "dialog", "institutionInfo_edit()");
}