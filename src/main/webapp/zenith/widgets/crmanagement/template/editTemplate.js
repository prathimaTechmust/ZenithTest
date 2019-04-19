navigate ("Template", "widgets/crmanagement/template/template.js");

function template_loaded ()
{
	m_oTemplateMemberData.m_nTemplateId = m_oTemplateList_MemberData.m_nTemplateId;
	loadPage ("crmanagement/template/template.html", "dialog", "template_edit ()");
}
