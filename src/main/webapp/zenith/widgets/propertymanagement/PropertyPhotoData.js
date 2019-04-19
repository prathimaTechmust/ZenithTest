function PropertyPhotoData ()
{
	this.$dwrClassName = "PropertyPhotoData";
	this.m_nImageId = -1;
	this.m_strFileName = "";
	this.m_strDescription = "";
	this.m_oBlob = null;
	this.m_dCreationDate = "";
	this.m_dUpdationDate = "";
	this.m_oPropertyData = PropertyData ();
}

dataObjectLoaded ();