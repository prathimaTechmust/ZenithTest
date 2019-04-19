function ReceiptData ()
{
	this.m_nReceiptId = -1;
	this.m_oClientData = new ClientData ();
	this.m_nAmount = 0;
	this.m_oMode = new TransactionModeData ();
	this.m_strDetails = "";
	this.m_oCreatedBy = new UserInformationData ();
	this.m_oUserCredentialsData = new UserInformationData ();
	this.m_nContactId = -1;
	this.m_nSiteId = -1;
}

dataObjectLoaded ();