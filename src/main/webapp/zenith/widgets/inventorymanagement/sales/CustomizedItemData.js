function CustomizedItemData ()
{
	this.m_nCustomizeId = -1;
	this.m_strClientArticleNumber = "";
	this.m_strClientArticleDescription = "";
	this.m_strItemArticleNumber = "";
	this.m_oClientData = new ClientData ();
	this.m_oItemData = new ItemData ();
	this.m_oCreatedBy = new UserInformationData ();
}

dataObjectLoaded ()