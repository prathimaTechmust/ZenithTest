function quotationDetails_memberData ()
{
	this.strXml = "";
	this.m_strEmailAddress = "";
	this.m_strSubject = "";
	this.m_strHTML = "";
}

var m_oQuotationDetailsMemberData = new quotationDetails_memberData ();

function quotationDetails_print ()
{
	quotationDetails_init (m_oQuotationDetailsMemberData.strXml);
}

function quotationDetails_init (strXml)
{
	if (m_oQuotationDetailsMemberData.m_strEmailAddress != "" && m_oQuotationDetailsMemberData.m_strEmailAddress != null)
		document.getElementById ("print_button_sendMail").style.visibility="visible";
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	populateXMLData (strXml, "quotationmanagement/quotation/printQuotations.xslt", "print_div_listDetail");
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
	TraderpUtil.sendEmail(m_oQuotationDetailsMemberData.m_strEmailAddress, m_oQuotationDetailsMemberData.m_strSubject, m_oQuotationDetailsMemberData.strXml, "printQuotations.xslt", sales_sentMail);
}

function sales_sentMail (bSuccess)
{
	if(bSuccess == true)
		informUser ("Mail Sent Successfully.", "kSuccess");
	else
		informUser ("Mail Failed to Send.", "kError");
}

QuotationDetails_loaded ();
