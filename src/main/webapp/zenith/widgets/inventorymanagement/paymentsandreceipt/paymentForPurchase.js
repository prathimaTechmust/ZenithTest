navigate ("", "widgets/inventorymanagement/paymentsandreceipt/paymentPurchase.js");

function paymentPurchase_loaded ()
{
	m_oPaymentPurchaseMemberData.m_nPurchaseId = m_oPurchaseListMemberData.m_nSelectedPurchaseId;
	paymentPurchase_getPurchaseData ();
}