reorderListPrint_loaded ();
function reorderListPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "reorderListPrint_print ()");
}

function reorderListPrint_print ()
{
	reorderListPrint_init (m_oReorderListMemberData.m_strXMLData);
}

function reorderListPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "inventorymanagement/item/reorderListPrint.xslt", "print_div_listDetail");
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