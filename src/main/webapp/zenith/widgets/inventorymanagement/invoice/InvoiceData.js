function InvoiceData ()
{
	this.m_nInvoiceId = -1;
	this.m_strInvoiceNo = "";
	this.m_dDate = "";
	this.m_oCreatedBy = new UserInformationData ();
	this.m_oUserCredentialsData = new UserInformationData ();
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.strRemarks = "";
	this.m_strLRNumber = "";
	this.m_strESugamNumber = "";
	this.m_oClientData = new ClientData ();
	this.m_nClientId = -1;
	this.m_nContactId = -1;
	this.m_nSiteId = -1;
}

dataObjectLoaded ();