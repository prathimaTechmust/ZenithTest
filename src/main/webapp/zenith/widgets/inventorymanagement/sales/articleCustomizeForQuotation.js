navigate ("articleCustomize", "widgets/inventorymanagement/sales/articleCustomize.js");

function articleCustomize_loaded ()
{
	m_oArticleCustomizeMemberData.m_strArticleNumber = m_oQuotationMemberData.m_strClientArticleNumber;
	m_oArticleCustomizeMemberData.m_nSelectedClientId = m_oQuotationMemberData.m_nSelectedClientId;
	loadPage ("inventorymanagement/sales/articleCustomize.html", "thirdDialog", "articleCustomize_new()");
}