navigate ("ReceiptList", "widgets/inventorymanagement/paymentsandreceipt/receiptList.js");

function receiptList_loaded ()
{
	loadPage("inventorymanagement/detailsPopup.html","secondDialog","clientTransactionReceiptInfo_init ()")
}

function clientTransactionReceiptInfo_init ()
{
	createPopup ("secondDialog", "", "", true);
	receiptList_showReceiptDetails (m_oClientTransactionMemeberData.m_oSelectedReceiptRowData, "detaisPopup_div_details");
}

function detaisPopup_cancel ()
{
	HideDialog ("secondDialog");
}