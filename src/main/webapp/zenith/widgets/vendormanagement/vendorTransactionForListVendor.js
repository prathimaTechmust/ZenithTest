navigate ("vendorTransaction", "widgets/vendormanagement/vendorTransaction.js");

function vendorTransaction_loaded ()
{
	m_oVendorTransactionMemeberData.m_oVendorData = m_oVendorListMemberData.m_oVendorData;
	m_oVendorTransactionMemeberData.m_nVendorId = m_oVendorListMemberData.m_oSelectedVendorId;
	loadPage ("vendormanagement/vendorTransaction.html", "dialog", "vendorTransaction_init()");
}