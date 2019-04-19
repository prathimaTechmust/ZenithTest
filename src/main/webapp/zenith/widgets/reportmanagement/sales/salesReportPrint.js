salesReportPrint_loaded ();
function salesReportPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "salesReportPrint_print ()");
}

function salesReportPrint_print ()
{
	salesReportPrint_init (m_oSalesReportMemberData.m_strXMLData);
}

function salesReportPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	if(!m_oSalesReportMemberData.bIsClientSelected)
		populateXMLData (strXml, "reportmanagement/sales/salesReportPrint.xslt", "print_div_listDetail");
	else
		populateXMLData (strXml, "reportmanagement/sales/salesClientReportPrint.xslt", "print_div_listDetail");
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