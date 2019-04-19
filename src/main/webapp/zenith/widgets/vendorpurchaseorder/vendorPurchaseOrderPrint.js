navigate ('printVendorPurchaseOrder','widgets/vendorpurchaseorder/printVendorPurchaseOrder.js');

function printVendorPurchaseOrder_loaded ()
{
	m_oPrintVendorPurchaseOrderMemberData.m_strXMLData =m_oVendorPurchaseOrderMemberData.m_strXMLData;
	m_oPrintVendorPurchaseOrderMemberData.m_strEmailAddress = m_oVendorPurchaseOrderMemberData.m_strEmailAddress;
	m_oPrintVendorPurchaseOrderMemberData.m_strSubject = m_oVendorPurchaseOrderMemberData.m_strSubject;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "printVendorPurchaseOrder_purchaseOrderPrint ()");
}