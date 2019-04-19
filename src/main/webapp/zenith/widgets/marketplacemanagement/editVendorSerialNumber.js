navigate ("vendorSerialNumber", "widgets/marketplacemanagement/vendorSerialNumber.js");

function vendorSerialNumber_loaded ()
{
	m_oVendorSerialNumberMemberData.m_nId = m_oVendorSerialNumberListMemberData.m_nId;
	loadPage ("marketplacemanagement/vendorSerialNumber.html", "dialog", "vendorSerialNumber_edit()");
}