function PubliciseData ()
{
	this.m_oTemplateData = new TemplateData ();
	this.m_nNoOfDays = 0;
	this.m_bIsBought = false;
	this.m_nItemGroupId = -1;
	this.m_nClientGroupId = -1;
	this.m_nItemId = -1;
	this.m_oUserCredentialsData = new UserInformationData ();
	this.m_oCreatedBy = new UserInformationData ();
	this.m_arrAttachmentData = new Array ();
	this.m_arrClientGroup = new Array ();
}

dataObjectLoaded ();