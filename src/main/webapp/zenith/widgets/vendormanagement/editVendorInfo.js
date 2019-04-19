navigate ("vendorInfo", "widgets/vendormanagement/vendorInfo.js");
function vendorInfo_loaded ()
{
	m_ovendorInfoMemberData.m_nVendorId = m_oVendorListMemberData.m_oSelectedVendorId;
	loadPage ("vendormanagement/vendorInfo.html", "secondDialog", "vendorInfo_edit()");
}

