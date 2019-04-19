navigate ("articleCustomize", "widgets/inventorymanagement/sales/articleCustomize.js");

function articleCustomize_loaded ()
{
	m_oArticleCustomizeMemberData.m_nSelectedClientId = m_oClientTransactionMemeberData.m_oSelectedClientId;
	m_oArticleCustomizeMemberData.m_oCustomizedItemData = m_oClientTransactionMemeberData.m_oCustomizedItemData;
	loadPage ("inventorymanagement/sales/articleCustomize.html", "thirdDialog", "articleCustomize_edit()");
}