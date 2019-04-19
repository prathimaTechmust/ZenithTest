navigate ('challanInfo','widgets/inventorymanagement/challan/challanInfo.js');

function challanInfo_loaded ()
{
	challanInfo_getChallanInfo (m_oQuotationListMemberData.m_nChallanId, m_oQuotationListMemberData.m_nInvoiceId)
}