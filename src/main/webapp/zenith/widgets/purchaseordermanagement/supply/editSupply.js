navigate("supply", "widgets/purchaseordermanagement/supply/supply.js");

function supply_loaded ()
{
	m_oSupplyMemberData.m_nSupplyId = m_oSupplyListMemberData.m_nSelectedSupplyId;
	loadPage("purchaseordermanagement/supply/supply.html", "dialog", "supply_edit ()")
}