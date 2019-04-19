inventoryReportPrint_loaded ();
function inventoryReportPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "inventoryReportPrint_print ()");
}

function inventoryReportPrint_print ()
{
	inventoryReportPrint_init (m_oInventoryReportMemberData.m_strXMLData);
}

function inventoryReportPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "reportmanagement/inventory/inventoryReportPrint.xslt", "print_div_listDetail");
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