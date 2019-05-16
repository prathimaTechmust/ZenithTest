navigate ("newfacilitator", "widgets/usermanagement/facilitator/facilitatorInfo.js");
function facilitatorInfo_loaded ()
{
	m_oFacilitatorInfoMemberData.m_FacilitatorId = m_oFacilitatorInfoListMemberData.m_nSelectedFacilitatorId;
	loadPage ("usermanagement/facilitator/facilitatorInfo.html", "dialog", "facilitatorInfo_edit()");
}
