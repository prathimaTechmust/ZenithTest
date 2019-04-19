navigate ('printPayment','widgets/inventorymanagement/paymentsandreceipt/printPayment.js');

function printPayment_loaded ()
{
	m_oPrintPaymentMemberData.m_strXMLData = m_oPaymentMemberData.m_strXMLData;
	m_oPrintPaymentMemberData.m_strEmailAddress = m_oPaymentMemberData.m_strEmailAddress;
	m_oPrintPaymentMemberData.m_strSubject = m_oPaymentMemberData.m_strSubject;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "printPayment_PaymentPrint ()");
}
