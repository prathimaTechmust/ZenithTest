navigate ("payment", "widgets/inventorymanagement/paymentsandreceipt/receipt.js");

function receipt_loaded ()
{
	m_oReceiptMemberData.m_nReceiptId = m_oReceiptListMemberData.m_nSelectedReceiptId;
	loadPage ("inventorymanagement/paymentsandreceipt/receipt.html", "thirdDialog", "receipt_edit ()");
}
