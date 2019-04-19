function PropertyData ()
{
	this.m_nPropertyId = -1;
	this.m_strAddress = "";
	this.m_strLocality = "";
	this.m_strCity = "";
	this.m_nPrice = 0;
	this.m_oPropertyType = new PropertyTypeData ();
	this.m_oPropertyDetails = new PropertyDetailData ();
	this.m_oClientData = new ClientData ();
	this.m_oCreatedBy = new UserInformationData ();
	this.m_arrPropertyPhoto = new Array ();
}
dataObjectLoaded ();