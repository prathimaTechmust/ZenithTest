function VendorCounterData ()
{
	this.m_nId = -1;
	this.m_oVendorData = new VendorData ();
	this.m_nSerialNumber =1;
	this.m_strPrefix = "";
	this.m_strSuffix = "";
	this.m_strKey = "";
	this.m_oCreatedBy = new UserInformationData ();
}
dataObjectLoaded ();