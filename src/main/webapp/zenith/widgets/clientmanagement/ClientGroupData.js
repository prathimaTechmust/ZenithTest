function ClientGroupData ()
{
	this.m_nGroupId = -1;
	this.m_strGroupName = "";
	this.m_oCreatedBy = new UserInformationData ();
	this.m_arrClientData = new Array ();
}

dataObjectLoaded ();