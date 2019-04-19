function challan_memberData ()
{
	this.m_strXMLData = "";
	this.m_nInvoiceId = -1;
	this.m_nChallanId = -1;
}

var m_oChallanMemberData = new challan_memberData ();

function challan_print ()
{
	challan_init (m_oChallanMemberData.m_strXMLData);
}

function challan_init (strXMLData)
{
	document.getElementById ("print_button_sendMail").style.visibility="visible";
	document.getElementById ("print_icon_sendMail").style.visibility="visible";
	if(m_oChallanMemberData.m_nInvoiceId <= 0)
	{
		document.getElementById ("print_button_makeInvoice").style.visibility="visible";
		document.getElementById ("print_icon_makeInvoice").style.visibility="visible";
	}
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXMLData, "inventorymanagement/challan/challan.xslt", "print_div_listDetail");
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

function sales_sendMail ()
{
	navigate ("makeInvoiceFromChallan", "widgets/inventorymanagement/challan/emailClientChallan.js");
}

function sales_makeInvoiceFromChallan ()
{
	navigate ("makeInvoiceFromChallan", "widgets/inventorymanagement/challan/invoiceFromChallan.js");
}

challan_loaded ();
