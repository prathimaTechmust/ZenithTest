navigate ("TemplateCategory", "widgets/crmanagement/template/templateCategory.js");

function templateCategory_loaded ()
{
	m_oTemplateCategoryMemberData.m_nCategoryId = m_oTemplateCategoryList_MemberData.m_nCategoryId;
	loadPage ("crmanagement/template/templateCategory.html", "dialog", "templateCategory_edit ()");
}
