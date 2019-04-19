var paymentPurchase_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/inventorymanagement/purchase/NonStockPurchaseLineItem.js',
	'widgets/purchaseordermanagement/purchaseorder/PurchaseOrderLineItemData.js',
	'widgets/inventorymanagement/purchase/PurchaseData.js'
];

includeDataObjects (paymentPurchase_includeDataObjects, "paymentPurchase_loaded()");

function paymentPurchase_memberData ()
{
	this.m_nPurchaseId = -1;
	this.m_oPurchaseData = null;
}

var m_oPaymentPurchaseMemberData = new paymentPurchase_memberData ();

function paymentPurchase_getPurchaseData ()
{
	var oPurchaseData = new PurchaseData ();
	oPurchaseData.m_nId = m_oPaymentPurchaseMemberData.m_nPurchaseId;
	oPurchaseData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
	oPurchaseData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
	PurchaseDataProcessor.get(oPurchaseData, paymentPurchase_gotPurchaseData);
}

function paymentPurchase_gotPurchaseData (oResponse)
{
	m_oPaymentPurchaseMemberData.m_oPurchaseData = oResponse.m_arrPurchase[0];
	navigate ("", "widgets/inventorymanagement/paymentsandreceipt/payment.js");
}

function payment_loaded ()
{
	m_oPaymentMemberData.m_oPurchaseData = m_oPaymentPurchaseMemberData.m_oPurchaseData;
	loadPage ("inventorymanagement/paymentsandreceipt/payment.html", "dialog", "payment_makePaymentPurchase()");
}
