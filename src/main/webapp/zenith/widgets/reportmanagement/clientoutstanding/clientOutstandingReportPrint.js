printClientOutstandingReport_loaded ();

function printClientOutstandingReport_loaded ()
{
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "printClientOutstandingReport_Print ()");
}

function printClientOutstandingReport_Print ()
{
	
	printClientOutstandingReport_init (m_oClientOutstandingMemberData.m_strXMLData );
}

function printClientOutstandingReport_init (strXml)
{
	createPopup ("secondDialog", "#print_button_print", "#print_button_cancel", true);
	if(m_oClientOutstandingMemberData.bIsforDetails)
		populateXMLData (strXml, "reportmanagement/clientoutstanding/clientOutstandingDetails.xslt", "print_div_listDetail");
	else
		populateXMLData (strXml, "reportmanagement/clientoutstanding/clientOutstandingReportPrint.xslt", "print_div_listDetail");
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
	TraderpUtil.sendEmail(m_oClientOutstandingMemberData.m_strEmailAddress, m_oClientOutstandingMemberData.m_strSubject, m_oClientOutstandingMemberData.strXml, "clientOutstanding.xslt", sales_sentMail);
}

function sales_sentMail (bSuccess)
{
	if(bSuccess == true)
		informUser ("Mail Sent Successfully.", "kSuccess");
	else
		informUser ("Mail Failed to Send.", "kError");
}