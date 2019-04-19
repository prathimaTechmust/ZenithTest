function PurchaseOrderData ()
{
	this.m_nPurchaseOrderId = -1;
	this.m_oClientData = new ClientData ();
	this.m_oSiteData = new SiteData ();
	this.m_oContactData = new ContactData ();
	this.m_strPurchaseOrderNumber = "";
	this.m_strPurchaseOrderDate = "";
	this.m_oCreatedBy = new UserInformationData ();
	this.m_oUserCredentialsData = new UserInformationData ();
	this.m_arrChallans = new Array ();
}
dataObjectLoaded ();
