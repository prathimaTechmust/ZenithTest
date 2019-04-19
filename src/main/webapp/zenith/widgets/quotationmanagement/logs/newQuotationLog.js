navigate ("QuotationLog", "widgets/quotationmanagement/logs/quotationLog.js");

function quotationLog_loaded ()
{
	m_oQuotationLogMemberData.m_nQuotationId = m_oQuotationListMemberData.m_nQuotationId;
	loadPage ("quotationmanagement/logs/quotationLog.html", "dialog", "quotationLog_new()");
}