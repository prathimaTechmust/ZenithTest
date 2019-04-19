navigate ('POChallanList','widgets/purchaseordermanagement/purchaseorder/POChallanList.js');

function POChallanList_loaded ()
{
	m_oPOChallanListMemberData.m_nPurchaseOrderId = m_oPurchaseOrderMemberData.m_nPurchaseOrderId;
	loadPage ("purchaseordermanagement/purchaseorder/POChallanList.html", "secondDialog", "POChallanList_init ()");
}