var printReceipt_includeDataObjects = 
[
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js',
	'widgets/inventorymanagement/paymentsandreceipt/TransactionModeData.js',
	'widgets/inventorymanagement/paymentsandreceipt/InvoiceReceiptData.js',
	'widgets/inventorymanagement/paymentsandreceipt/ReceiptData.js'
];

includeDataObjects (printReceipt_includeDataObjects, "printReceipt_loaded ()");

function printReceipt_memberData ()
{
	this.m_strXMLData = "";
	this.m_strEmailAddress = "";
	this.m_strSubject = "";
	this.m_strHTML = "";
}

var m_oPrintReceiptMemberData = new printReceipt_memberData ();

function printReceipt_print ()
{
	printReceipt_init (m_oPrintReceiptMemberData.m_strXMLData);
}

function printReceipt_init (strXml)
{
	if (m_oPrintReceiptMemberData.m_strEmailAddress != "" && m_oPrintReceiptMemberData.m_strEmailAddress != null)
	{
		document.getElementById ("print_icon_sendMail").style.visibility="visible";
		document.getElementById ("print_button_sendMail").style.visibility="visible";
	}
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "inventorymanagement/paymentsandreceipt/receiptPrint.xslt", "print_div_listDetail");
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
	TraderpUtil.sendEmail(m_oPrintReceiptMemberData.m_strEmailAddress, m_oPrintReceiptMemberData.m_strSubject, m_oPrintReceiptMemberData.m_strXMLData, "receipt.xslt", sales_sentMail);
}

function sales_sentMail (bSuccess)
{
	assert.isBoolean(bSuccess, "bSuccess should be a boolean value");
	if(bSuccess == true)
		informUser ("Mail Sent Successfully.", "kSuccess");
	else
		informUser ("Mail Failed to Send.", "kError");
}

