navigate ('paymentlist', 'widgets/inventorymanagement/paymentsandreceipt/paymentList.js');

function paymentList_loaded ()
{
	loadPage ("inventorymanagement/detailsPopup.html", "secondDialog", "vendorTransactionPaymentInfo_init ()");
}

function vendorTransactionPaymentInfo_init ()
{
	createPopup ("secondDialog", "", "", true);
	paymentList_showPaymentDetails (m_oVendorTransactionMemeberData.m_oSelectedVendorPaymentData, "detaisPopup_div_details"); 
}

function detaisPopup_cancel ()
{
	HideDialog ("secondDialog");
}