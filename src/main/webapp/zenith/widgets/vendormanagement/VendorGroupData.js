function VendorGroupData ()
{
	this.m_nGroupId = -1;
	this.m_strGroupName = "";
	this.m_oCreatedBy = new UserInformationData ();
	this.m_arrVendorData = new Array ();
}
dataObjectLoaded ();