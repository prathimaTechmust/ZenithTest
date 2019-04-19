navigate ("newfacilitator", "widgets/usermanagement/userinfo/facilitatorInfo.js");
function facilitatorInfo_loaded ()
{
	m_oFacilitatorInfoMemberData.m_FacilitatorId = m_oFacilitatorInfoListMemberData.m_nSelectedFacilitatorId;
	loadPage ("usermanagement/userinfo/facilitatorInfo.html", "dialog", "facilitatorInfo_edit()");
}
