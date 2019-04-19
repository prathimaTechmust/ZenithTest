navigate ("articleCustomize", "widgets/inventorymanagement/sales/articleCustomize.js");

function articleCustomize_loaded ()
{
	m_oArticleCustomizeMemberData.m_strArticleNumber = m_oSalesMemberData.m_strArticleNumber;
	m_oArticleCustomizeMemberData.m_nSelectedClientId = m_oSalesMemberData.m_nSelectedClientId;
	loadPage ("inventorymanagement/sales/articleCustomize.html", "thirdDialog", "articleCustomize_new()");
}