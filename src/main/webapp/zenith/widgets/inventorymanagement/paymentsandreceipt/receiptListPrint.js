navigate ('printReceipt','widgets/inventorymanagement/paymentsandreceipt/printReceipt.js');

function printReceipt_loaded ()
{
	m_oPrintReceiptMemberData.m_strXMLData = m_oReceiptListMemberData.m_strXMLData;
	m_oPrintReceiptMemberData.m_strEmailAddress = m_oReceiptListMemberData.m_strEmailAddress;
	m_oPrintReceiptMemberData.m_strSubject = m_oReceiptListMemberData.m_strSubject;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "printReceipt_print ()");
}