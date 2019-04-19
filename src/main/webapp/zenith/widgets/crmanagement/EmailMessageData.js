function EmailMessageData ()
{
	this.m_nId = -1;
	this.m_oCreatedBy = new UserInformationData ();
	this.m_strSubject = "";
	this.m_strContent = ""; 
	this.m_arrContactData = new Array ();
	this.m_arrAttachmentData = new Array ();
	this.m_oUserCredentialsData = new UserInformationData ();
}

dataObjectLoaded ();