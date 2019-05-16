navigate ("newinstitution", "widgets/scholarshipmanagement/institutionslist/institutionInfo.js");
function institutionInfo_loaded ()
{
	m_oInstitutionInfoMemberData.m_nInstitutionId = m_oInstitutionsInfoListMemberData.m_nSelectedInstitutionId;
	loadPage ("scholarshipmanagement/institution/institutionInfo.html", "dialog", "institutionInfo_edit()");
}