navigate ("articleCustomize", "widgets/inventorymanagement/sales/articleCustomize.js");

function articleCustomize_loaded ()
{
	m_oArticleCustomizeMemberData.m_nSelectedClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	loadPage ("inventorymanagement/sales/articleCustomize.html", "thirdDialog", "articleCustomize_newArticle()");
}