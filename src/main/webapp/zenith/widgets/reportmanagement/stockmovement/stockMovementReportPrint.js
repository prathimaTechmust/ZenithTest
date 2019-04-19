stockMovementReportPrint_loaded ();
function stockMovementReportPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "stockMovementReportPrint_print ()");
}

function stockMovementReportPrint_print ()
{
	stockMovementReportPrint_init (m_oStockMovementReportMemberData.m_strXMLData);
}

function stockMovementReportPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "reportmanagement/stockmovement/stockMovementPrint.xslt", "print_div_listDetail");
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
