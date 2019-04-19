navigate ("", "widgets/inventorymanagement/paymentsandreceipt/paymentPurchase.js");

function paymentPurchase_loaded ()
{
	m_oPaymentPurchaseMemberData.m_nPurchaseId = m_oSupplyListMemberData.m_nSelectedSupplyId;
	paymentPurchase_getPurchaseData ();
}