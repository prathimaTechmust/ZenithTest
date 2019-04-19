navigate ("newSales", "widgets/inventorymanagement/sales/sales.js");

function sales_loaded ()
{
	m_oSalesMemberData.m_bIsForSales = true;
	m_oSalesMemberData.m_oSalesData = m_oQuotationListMemberData.m_oQuotationData;
	loadPage ("inventorymanagement/sales/sales.html", "dialog", "sales_new()");
}
