printPurchaseReturnedItems_loaded ();

function printPurchaseReturnedItems_printReturned ()
{
	printPurchaseReturnedItems_init (m_oPurchaseReturnedItemsMemberData.m_strXMLData);
}

function printPurchaseReturnedItems_printReturnedList ()
{
	printPurchaseReturnedItems_init (m_oPurchaseReturnedItemsListMemberData.m_strXMLData);
}

function printPurchaseReturnedItems_init (strXML)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXML, "inventorymanagement/purchasereturneditems/printPurchaseReturnedItems.xslt", "print_div_listDetail");
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