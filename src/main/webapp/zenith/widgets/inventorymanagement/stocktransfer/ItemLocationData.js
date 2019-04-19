function ItemLocationData ()
{
	this.m_nId = -1;
	this.m_oItemData = new ItemData ();
	this.m_oLocationData = new LocationData ();
	this.m_nReceived = 0;
	this.m_nIssued = 0;
	this.m_oUserCredentialsData = new UserInformationData ();
}
dataObjectLoaded ();
