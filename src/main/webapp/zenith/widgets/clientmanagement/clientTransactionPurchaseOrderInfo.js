navigate("purchaseOrderList","widgets/purchaseordermanagement/purchaseorder/purchaseOrderList.js");

function purchaseOrderList_loaded ()
{
	loadPage("inventorymanagement/detailsPopup.html","secondDialog","clientTransactionPurchaseOrderInfo_init ()");
}

function clientTransactionPurchaseOrderInfo_init ()
{
	createPopup ("secondDialog", "", "", true);
	purchaseOrderList_showPoData (m_oClientTransactionMemeberData.m_oSelectedPurchaseOrderRowData, "detaisPopup_div_details");
}

function detaisPopup_cancel ()
{
	HideDialog ("secondDialog");
}
