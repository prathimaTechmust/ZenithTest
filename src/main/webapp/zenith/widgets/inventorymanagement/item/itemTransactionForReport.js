navigate ("ItemTransaction", "widgets/inventorymanagement/item/itemTransaction.js");

function itemTransaction_loaded ()
{
	m_oItemTransactionData.m_strFromDate = m_oStockMovementReportMemberData.m_strFromDate;
	m_oItemTransactionData.m_strToDate = m_oStockMovementReportMemberData.m_strToDate;
	m_oItemTransactionData.m_nSelectedItemId = m_oStockMovementReportMemberData.m_nSelectedItemId;
	loadPage ("inventorymanagement/item/itemTransaction.html", "dialog", "itemTransaction_init ()");
}
