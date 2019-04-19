function PaymentData ()
{
	this.m_nPaymentId = -1;
	this.m_oVendorData = new VendorData ();
	this.m_nAmount = 0;
	this.m_oMode = new TransactionModeData ();
	this.m_strDetails = "";
	this.m_oCreatedBy = new UserInformationData ();
	this.m_oUserCredentialsData = new UserInformationData ();
	this.m_strPaymentNumber = "";
}

dataObjectLoaded ();