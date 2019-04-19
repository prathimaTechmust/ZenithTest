function VendorPurchaseOrderData ()
{
	this.m_nPurchaseOrderId = -1;
	this.m_oVendorData = new VendorData ();
	this.m_strPurchaseOrderNumber = "";
	this.m_strPurchaseOrderDate = "";
	this.m_oCreatedBy = new UserInformationData ();
	this.m_oUserCredentialsData = new UserInformationData ();
	this.m_strFromDate = "";
	this.m_strToDate = "";
}
dataObjectLoaded ();
