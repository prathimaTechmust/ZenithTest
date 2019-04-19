navigate ('printQuotations','widgets/quotationmanagement/quotation/printQuotations.js');

function printQuotations_loaded ()
{
	m_oPrintQuotationsMemberData.strXml = m_oClientTransactionMemeberData.m_strXMLData;
	m_oPrintQuotationsMemberData.m_strEmailAddress = m_oClientTransactionMemeberData.m_strEmailAddress;
	m_oPrintQuotationsMemberData.m_strSubject = m_oClientTransactionMemeberData.m_strSubject;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "printQuotations_printQuotationDetails ()");
}