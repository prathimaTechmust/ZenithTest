navigate ('purchaselist', 'widgets/inventorymanagement/purchase/purchaseList.js');

function purchaseList_loaded ()
{
	loadPage ("inventorymanagement/detailsPopup.html", "secondDialog", "vendorTransactionPurchaseInfo_init ()");
}

function vendorTransactionPurchaseInfo_init ()
{
	createPopup ("secondDialog", "", "", true);
	purchaseList_showPurchaseDetails (m_oVendorTransactionMemeberData.m_oSelectedVendorPurchaseData, "detaisPopup_div_details");
}

function detaisPopup_cancel ()
{
	HideDialog ("secondDialog");
}

