navigate ('challan','widgets/inventorymanagement/challan/challanInfo.js');

function challanInfo_loaded ()
{
	challanInfo_getChallanInfo (m_oPurchaseOrderListMemberData.m_nChallanId, m_oPurchaseOrderListMemberData.m_nInvoiceId);
}