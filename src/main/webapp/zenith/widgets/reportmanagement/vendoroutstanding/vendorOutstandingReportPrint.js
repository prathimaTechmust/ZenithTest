vendorOutstandingReportPrint_loaded ();
function vendorOutstandingReportPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "vendorOutstandingReportPrint_print ()");
}

function vendorOutstandingReportPrint_print ()
{
	vendorOutstandingReportPrint_init (m_oVendorOutstandingReport_memberData.m_strXMLData);
}

function vendorOutstandingReportPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "reportmanagement/vendoroutstanding/vendorOutstandingReportPrint.xslt", "print_div_listDetail");
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