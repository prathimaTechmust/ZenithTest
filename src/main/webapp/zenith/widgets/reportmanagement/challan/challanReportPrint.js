challanReportPrint_loaded ();
function challanReportPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "challanReportPrint_print ()");
}

function challanReportPrint_print ()
{
	challanReportPrint_init (m_oChallanReportMemberData.m_strXMLData);
}

function challanReportPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "reportmanagement/challan/challanReportPrint.xslt", "print_div_listDetail");
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