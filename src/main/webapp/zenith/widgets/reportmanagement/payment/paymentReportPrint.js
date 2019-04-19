paymentReportPrint_loaded ();
function paymentReportPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "paymentReportPrint_print ()");
}

function paymentReportPrint_print ()
{
	paymentReportPrint_init (m_oPaymentReportMemberData.m_strXMLData);
}

function paymentReportPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "reportmanagement/payment/paymentReportPrint.xslt", "print_div_listDetail");
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