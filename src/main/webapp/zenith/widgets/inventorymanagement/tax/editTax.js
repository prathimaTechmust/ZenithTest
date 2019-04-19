navigate ("tax", "widgets/inventorymanagement/tax/tax.js");

function tax_loaded ()
{
	m_oTaxMemberData.m_nTaxId = m_oTaxList_MemberData.m_nTaxId;
	loadPage ("inventorymanagement/tax/tax.html", "dialog", "tax_edit ()");
}
