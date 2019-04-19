function ItemGroupData ()
{
	this.m_oCreatedBy = new UserInformationData ();
	this.m_nItemGroupId = -1;
	this.m_strGroupName = ""; 
	this.m_arrGroupItems = new Array ();
	this.m_oUserCredentialsData = new UserInformationData ();
}

dataObjectLoaded ();