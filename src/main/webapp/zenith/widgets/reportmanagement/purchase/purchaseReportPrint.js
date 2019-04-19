purchaseReportPrint_loaded ();
function purchaseReportPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "purchaseReportPrint_print ()");
}

function purchaseReportPrint_print ()
{
	purchaseReportPrint_init (m_oPurchaseReportMemberData.m_strXMLData);
}

function purchaseReportPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	if(!m_oPurchaseReportMemberData.bIsVendorSelected)
		populateXMLData (strXml, "reportmanagement/purchase/purchaseReportPrint.xslt", "print_div_listDetail");
	else
		populateXMLData (strXml, "reportmanagement/purchase/purchaseVendorReportPrint.xslt", "print_div_listDetail");
}

function sales_closePrint ()
{
	HideDialog ("secondDialog");
}

function sales_print ()
{
	document.getElementById("secondDialog").style.top = "0";
	document.getElementById("secondDialog").style.bottom = "0";
	document.getElementById("secondDialog").style.left = "0";
	document.getElementById("secondDialog").style.right = "0";
	window.print();
}