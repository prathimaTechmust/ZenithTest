navigate ('printReceipt','widgets/inventorymanagement/paymentsandreceipt/printReceipt.js');

function printReceipt_loaded ()
{
	m_oPrintReceiptMemberData.m_strXMLData =m_oReceiptMemberData.m_strXMLData;
	m_oPrintReceiptMemberData.m_strEmailAddress = m_oReceiptMemberData.m_strEmailAddress;
	m_oPrintReceiptMemberData.m_strSubject = m_oReceiptMemberData.m_strSubject;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "printReceipt_print ()");
}