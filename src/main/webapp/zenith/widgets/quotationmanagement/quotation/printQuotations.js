function printQuotations_memberData ()
{
	this.strXml = "";
	this.m_strEmailAddress = "";
	this.m_strSubject = "";
	this.m_strHTML = "";
}

var m_oPrintQuotationsMemberData = new printQuotations_memberData ();

function printQuotations_QuotationPrint ()
{
	printQuotations_init (m_oQuotationMemberData.m_strXMLData);
}

function printQuotations_quotationPrint_list ()
{
	printQuotations_init (m_oQuotationListMemberData.m_strXMLData);
}

function printQuotations_printQuotationDetails ()
{
	printQuotations_init (m_oPrintQuotationsMemberData.strXml);
}

function printQuotations_init (strXml)
{
	if (m_oPrintQuotationsMemberData.m_strEmailAddress != "" && m_oPrintQuotationsMemberData.m_strEmailAddress != null)
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
	TraderpUtil.sendEmail(m_oPrintQuotationsMemberData.m_strEmailAddress, m_oPrintQuotationsMemberData.m_strSubject, m_oPrintQuotationsMemberData.strXml, "printQuotations.xslt", sales_sentMail);
}

function sales_sentMail (bSuccess)
{
	if(bSuccess == true)
		informUser ("Mail Sent Successfully.", "kSuccess");
	else
		informUser ("Mail Failed to Send.", "kError");
}

printQuotations_loaded ();