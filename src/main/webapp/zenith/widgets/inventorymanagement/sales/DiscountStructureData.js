function DiscountStructureData ()
{
	this.m_oClientGroupData = new ClientGroupData ();
	this.m_oItemGroupData = new ItemGroupData ();
	this.m_nDiscount = 0;
	this.m_oCreatedBy = new UserInformationData ();
	this.m_oUserCredentialsData = new UserInformationData ();
}

dataObjectLoaded ();