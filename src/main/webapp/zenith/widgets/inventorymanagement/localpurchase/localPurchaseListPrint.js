LocalPurchaseListPrint_loaded ();
function LocalPurchaseListPrint_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "localPurchaseListPrint_print ()");
}

function localPurchaseListPrint_print ()
{
	localPurchaseListPrint_init (m_oLocalPurchaseListMemberData.m_strXMLData);
}

function localPurchaseListPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "inventorymanagement/localpurchase/localPurchaseListPrint.xslt", "print_div_listDetail");
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