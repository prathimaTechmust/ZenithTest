receiptReportPrint_loaded ();
function receiptReportPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "receiptReportPrint_print ()");
}

function receiptReportPrint_print ()
{
	receiptReportPrint_init (m_oReceiptReportMemberData.m_strXMLData);
}

function receiptReportPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "reportmanagement/receipt/receiptReportPrint.xslt", "print_div_listDetail");
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