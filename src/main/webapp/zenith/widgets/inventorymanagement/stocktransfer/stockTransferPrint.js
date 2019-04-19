stockTransferPrint_loaded ();

function stockTransferPrint_stockTransferMemoPrint ()
{
	stockTransferPrint_init (m_oStockTransferMemberData.m_strXMLData);
}

function stockTransferPrint_stockTransferMemoListPrint ()
{
	stockTransferPrint_init (m_oStockTransferMemoListMemberData.m_strXMLData);
}

function stockTransferPrint_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "inventorymanagement/stocktransfer/stockTransferMemo.xslt", "print_div_listDetail");
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
