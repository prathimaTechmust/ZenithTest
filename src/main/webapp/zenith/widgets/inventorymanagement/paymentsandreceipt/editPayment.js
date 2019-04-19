navigate ("payment", "widgets/inventorymanagement/paymentsandreceipt/payment.js");

function payment_loaded ()
{
	m_oPaymentMemberData.m_nPaymentId = m_oPaymentListMemberData.m_nSelectedPaymentId;
	loadPage ("inventorymanagement/paymentsandreceipt/payment.html", "dialog", "payment_edit ()");
}
