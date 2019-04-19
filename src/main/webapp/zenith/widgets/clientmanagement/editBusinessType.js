navigate ("businessType", "widgets/clientmanagement/businessType.js");
function businessType_loaded ()
{
	m_obusinessType_memberData.m_nBusinessId = m_olistBusinessType_memberData.m_nSelectedBusinessTypeId;
	loadPage ("clientmanagement/businessType.html", "dialog", "businessType_edit ()");
}

