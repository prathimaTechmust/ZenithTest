var invoice_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js'
];

includeDataObjects (invoice_includeDataObjects, "invoice_loaded ()");

function invoice_memberData ()
{
	this.m_strXMLData = "";
	this.m_oInvoiceData = null;
}

var m_oInvoiceMemberData = new invoice_memberData ();

function invoice_print ()
{
	invoice_init (m_oInvoiceMemberData.m_strXMLData);
}

function invoice_init (strXml)
{
	//it accepts xml file
	
	document.getElementById ("print_icon_addRemarks").style.visibility="visible";
	document.getElementById ("print_button_addRemarks").style.visibility="visible";
	document.getElementById ("print_icon_sendMail").style.visibility="visible";
	document.getElementById ("print_button_sendMail").style.visibility="visible";
	if(m_oInvoiceMemberData.m_oInvoiceData != null && m_oInvoiceMemberData.m_oInvoiceData.m_nBalanceAmount > 0)
	{
		document.getElementById ("print_button_makeReceiptTop").style.display="inline";
		document.getElementById ("print_button_makeReceipt").style.display="inline";
	}
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "inventorymanagement/invoice/invoice.xslt", "print_div_listDetail");
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
	navigate ("emailClientInvoice", "widgets/inventorymanagement/invoice/emailClientInvoice.js");
}

function sales_sentMail (bSuccess)
{
	assert.isBoolean(bSuccess, "bSuccess should be a boolean value");
	if(bSuccess == true)
		informUser ("Mail Sent Successfully.", "kSuccess");
	else
		informUser ("Mail Failed to Send.", "kError");
}

function invoice_getFormData ()
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_nInvoiceId = m_oInvoiceMemberData.m_oInvoiceData.m_nInvoiceId;
	return oInvoiceData
}

function sales_addRemarks ()
{
	var oInvoiceData = invoice_getFormData ();
	InvoiceDataProcessor.get (oInvoiceData,	invoice_gotInvoiceData);
}

function invoice_gotInvoiceData (oResponse)
{
	m_oInvoiceMemberData.m_oInvoiceData  = oResponse.m_arrInvoice[0];
	navigate ('invoice','widgets/inventorymanagement/invoice/remarksInvoice.js');
	window.scrollTo(0,0);
}

function sales_makeReceipt ()
{
	var oInvoiceData = invoice_getFormData ();
	InvoiceDataProcessor.get (oInvoiceData,	invoice_gotInvoiceDataForReceipt);
}

function invoice_gotInvoiceDataForReceipt (oResponse)
{
	m_oInvoiceMemberData.m_oInvoiceData  = oResponse.m_arrInvoice[0];
	navigate ("Receipt", "widgets/inventorymanagement/paymentsandreceipt/receipt.js");
	window.scrollTo(0,0);
}

function receipt_loaded ()
{
	m_oReceiptMemberData.m_oInvoiceData = m_oInvoiceMemberData.m_oInvoiceData;
	loadPage ("inventorymanagement/paymentsandreceipt/receipt.html", "thirdDialog", "receipt_makeReceiptForInvoice()");
}