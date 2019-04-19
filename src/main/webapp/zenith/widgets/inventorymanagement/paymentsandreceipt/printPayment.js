function printPayment_memberData ()
{
	this.strXml = "";
	this.m_strEmailAddress = "";
	this.m_strSubject = "";
	this.m_strHTML = "";
}

var m_oPrintPaymentMemberData = new printPayment_memberData ();

function printPayment_PaymentPrint ()
{
	printPayment_init (m_oPrintPaymentMemberData.m_strXMLData);
}

function printPayment_init (strXml)
{
	if (m_oPrintPaymentMemberData.m_strEmailAddress != "" && m_oPrintPaymentMemberData.m_strEmailAddress != null)
	{
		document.getElementById ("print_icon_sendMail").style.visibility="visible";
		document.getElementById ("print_button_sendMail").style.visibility="visible";
	}
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "inventorymanagement/paymentsandreceipt/paymentPrint.xslt", "print_div_listDetail");
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
	TraderpUtil.sendEmail(m_oPrintPaymentMemberData.m_strEmailAddress, m_oPrintPaymentMemberData.m_strSubject, m_oPrintPaymentMemberData.strXml, "payment.xslt", sales_sentMail);
}

function sales_sentMail (bSuccess)
{
	assert.isBoolean(bSuccess, "bSuccess should be a boolean value");
	if(bSuccess == true)
		informUser ("Mail Sent Successfully.", "kSuccess");
	else
		informUser ("Mail Failed to Send.", "kError");
}

printPayment_loaded ();