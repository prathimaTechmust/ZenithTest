navigate ("applicabletax", "widgets/inventorymanagement/tax/applicableTax.js");

function applicableTax_loaded ()
{
	m_oApplicableTaxMemberData.m_nApplicableTaxId = m_oApplicableTaxListMemberData.m_nId;
	loadPage ("inventorymanagement/tax/applicableTax.html", "dialog", "applicableTax_edit ()");
}
