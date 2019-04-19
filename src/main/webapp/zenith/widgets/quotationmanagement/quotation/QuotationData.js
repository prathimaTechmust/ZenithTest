function QuotationData ()
{
	this.m_nQuotationId = -1;
	this.m_strDate = "";
	this.m_oClientData = new ClientData ();
	this.m_oSiteData = new SiteData ();
	this.m_oContactData = new ContactData ();
	this.m_oCreatedBy = new UserInformationData ();
	this.m_oUserCredentialsData = new UserInformationData ();
//	this.m_arrQuotationLineItems = new Array ();
	this.m_strTermsAndCondition = "";
//	this.m_arrQuotationAttachments = new Array();
	this.m_strQuotationNumber = "";
	this.m_strFromDate = "";
	this.m_strToDate = "";
	this.m_bIsArchived = false;
	this.m_bIsForAllList = false;
	this.m_dDate = new Date ();
//	this.m_arrPurcahseOrder = new Array();
	this.m_bIsCFormProvided = false;
}

dataObjectLoaded ();