navigate ("sales", "widgets/inventorymanagement/sales/sales.js");

function sales_loaded ()
{
	m_oSalesMemberData.m_nSalesId = m_oSalesListMemberData.m_nSelectedSalesId;
	loadPage ("inventorymanagement/sales/sales.html", "dialog", "sales_edit ()");
}
