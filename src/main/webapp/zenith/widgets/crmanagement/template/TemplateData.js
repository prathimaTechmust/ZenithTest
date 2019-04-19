function TemplateData ()
{
	this.m_nTemplateId = -1;
	this.m_oCreatedBy = new UserInformationData ();
	this.m_strTemplateName = "";
//	this.oFile = null;
	this.m_strTemplateFileName = "";
	this.m_oCategoryData = new TemplateCategoryData ();
}

dataObjectLoaded ();