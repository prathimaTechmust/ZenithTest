navigate ("editTransactionMode", "widgets/inventorymanagement/paymentsandreceipt/transactionMode.js");

function transactionMode_loaded ()
{
	m_oTransactionModeMemberData.m_nSelectedTransactionModeId = m_oTransactionModeListMemberData.m_nSelectedTransactionModeId;
	loadPage ("inventorymanagement/paymentsandreceipt/transactionMode.html", "dialog", "transactionMode_edit()");
}