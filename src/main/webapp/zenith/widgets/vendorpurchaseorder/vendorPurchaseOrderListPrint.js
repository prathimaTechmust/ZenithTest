navigate ('printVendorPurchaseOrder','widgets/vendorpurchaseorder/printVendorPurchaseOrder.js');

function printVendorPurchaseOrder_loaded ()
{
	m_oPrintVendorPurchaseOrderMemberData.m_strXMLData =m_oVendorPurchaseOrderListMemberData.m_strXMLData;
	m_oPrintVendorPurchaseOrderMemberData.m_strEmailAddress = m_oVendorPurchaseOrderListMemberData.m_strEmailAddress;
	m_oPrintVendorPurchaseOrderMemberData.m_strSubject = m_oVendorPurchaseOrderListMemberData.m_strSubject;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "printVendorPurchaseOrder_purchaseOrderPrint ()");
}