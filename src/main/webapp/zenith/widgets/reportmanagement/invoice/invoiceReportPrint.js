invoiceReportPrint_loaded ();
function invoiceReportPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "invoiceReportPrint_print ()");
}

function invoiceReportPrint_print ()
{
	invoiceReportPrint_init (m_oInvoiceReportMemberData.m_strXMLData);
}

function invoiceReportPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "reportmanagement/invoice/invoiceReportPrint.xslt", "print_div_listDetail");
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