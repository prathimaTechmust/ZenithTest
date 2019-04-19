var printVendorPurchaseOrder_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/vendorpurchaseorder/VendorPurchaseOrderData.js',
	'widgets/vendorpurchaseorder/VendorPOLineItemData.js'
];

 includeDataObjects (printVendorPurchaseOrder_includeDataObjects, "printVendorPurchaseOrder_loaded ()");

function printVendorPurchaseOrder_memberData ()
{
	this.m_strXMLData = "";
	this.m_strEmailAddress = "";
	this.m_strSubject = "";
	this.m_strHTML = "";
}

var m_oPrintVendorPurchaseOrderMemberData = new printVendorPurchaseOrder_memberData ();

function printVendorPurchaseOrder_purchaseOrderPrint ()
{
	printVendorPurchaseOrder_init (m_oPrintVendorPurchaseOrderMemberData.m_strXMLData);
}

function printVendorPurchaseOrder_init (strXml)
{
	if (m_oPrintVendorPurchaseOrderMemberData.m_strEmailAddress != "" && m_oPrintVendorPurchaseOrderMemberData.m_strEmailAddress != null)
	{
		document.getElementById ("print_icon_sendMail").style.visibility="visible";
		document.getElementById ("print_button_sendMail").style.visibility="visible";
	}
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "vendorpurchaseorder/printVendorPurchaseOrder.xslt", "print_div_listDetail");
}

function sales_sendMail ()
{
	TraderpUtil.sendEmail(m_oPrintVendorPurchaseOrderMemberData.m_strEmailAddress, m_oPrintVendorPurchaseOrderMemberData.m_strEmailAddress.m_strXMLData, "printVendorPurchaseOrder.xslt", sales_sentMail);
}

function sales_sentMail (bSuccess)
{
	if(bSuccess == true)
		informUser ("Mail Sent Successfully.", "kSuccess");
	else
		informUser ("Mail Failed to Send.", "kError");
}

function sales_closePrint ()
{
	HideDialog ("secondDialog");
}

function sales_print ()
{
	window.print();
}