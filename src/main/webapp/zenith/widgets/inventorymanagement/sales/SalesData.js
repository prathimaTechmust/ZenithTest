function SalesData ()
{
	this.m_nID = -1;
	this.m_strTo = "";
	this.m_strInvoiceNo = "";
	this.m_dDate = new Date ();
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_strDate = "";
	this.m_oCreatedBy = new UserInformationData ();
	this.m_arrSalesLineItem = new Array ();
	this.m_oClientData = new ClientData ();
	this.m_oContactData = new ContactData ();
	this.m_oSiteData = new SiteData ();
	this.m_oUserCredentialsData = new UserInformationData ();
	this.m_strChallanNumber = "";
	this.m_bIsForClientwise = false;
	this.m_nItemId = -1;
}

dataObjectLoaded ();