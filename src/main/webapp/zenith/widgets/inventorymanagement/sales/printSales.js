printSales_loaded ();

function printSales_salesPrint ()
{
	printSales_init (m_oSalesMemberData.m_strXMLData);
}

function printSales_salesPrint_list ()
{
	printSales_init (m_oSalesListMemberData.m_strXMLData);
}

function printSales_clientTransaction ()
{
	printSales_init (m_oClientTransactionMemeberData.m_strXmlData);
}

function printSales_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "inventorymanagement/sales/printSales.xslt", "print_div_listDetail");
}

function sales_closePrint ()
{
	HideDialog ("secondDialog");
}

function sales_print ()
{
	window.print();
}