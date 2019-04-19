printReturnedItems_loaded ();

function printReturnedItems_printReturned ()
{
	printReturnedItems_init (m_oReturnedItemsMemberData.m_strXMLData);
}

function printReturnedItems_printReturnedList ()
{
	printReturnedItems_init (m_oReturnedItemsListMemberData.m_strXMLData);
}

function printReturnedItems_init (strXML)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXML, "inventorymanagement/returneditems/printReturnedItems.xslt", "print_div_listDetail");
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