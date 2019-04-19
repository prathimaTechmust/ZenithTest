function PurchaseData ()
{
	this.m_strFrom = "";
	this.m_strInvoiceNo = "";
	this.m_dDate = "";
	this.m_strDate = "";
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_oVendorData = new VendorData ();
	this.m_oCreatedBy = new UserInformationData ();
	this.m_oUserCredentialsData = new UserInformationData ();
}

dataObjectLoaded ();