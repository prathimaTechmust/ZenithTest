var emailClientInvoice_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/invoice/InvoiceData.js'
];


includeDataObjects (emailClientInvoice_includeDataObjects, "emailClientInvoice_loaded()");

function emailClientInvoice_memberData ()
{
    this.m_strFileName = "invoice.xslt";
	this.m_strSubject = "invoice";
	this.m_oSelectedContact = null;
	this.m_arrContacts = new Array ();
	this.m_nClientId = -1;
}

var m_oEmailClientInvoiceMemberData = new emailClientInvoice_memberData ();

function emailClientInvoice_loaded ()
{
	var oInvoiceData = new InvoiceData ();
	oInvoiceData.m_nInvoiceId = m_oInvoiceMemberData.m_oInvoiceData.m_nInvoiceId;
	InvoiceDataProcessor.get (oInvoiceData,	emailClientInvoice_gotInvoiceData);
}

function emailClientInvoice_gotInvoiceData (oResponse)
{
	m_oEmailClientInvoiceMemberData.m_arrContacts = emailClientInvoice_buildContacts (oResponse.m_arrInvoice[0].m_oSalesSet[0]);
	navigate ('emailClient','widgets/crmanagement/emailClient.js');
}

function emailClientInvoice_buildContacts(oSalesData)
{
	var arrContacts = new Array ();
	m_oEmailClientInvoiceMemberData.m_nClientId = oSalesData.m_oClientData.m_nClientId;
	if(oSalesData.m_oClientData.m_strEmail.trim() != "")
	{
		var oContactData = new ContactData ();
		oContactData.m_strContactName = oSalesData.m_oClientData.m_strCompanyName;
		oContactData.m_strEmail = oSalesData.m_oClientData.m_strEmail;
		arrContacts.push(oContactData);
	}
	if(oSalesData.m_oContactData != null)
		m_oEmailClientInvoiceMemberData.m_oSelectedContact = oSalesData.m_oContactData;
	for (var nIndex  = 0; nIndex < oSalesData.m_oClientData.m_oContacts.length; nIndex++)
			arrContacts.push(oSalesData.m_oClientData.m_oContacts[nIndex]);
	return arrContacts;
}

function emailClient_loaded ()
{
	m_oEmailClientMemberData.m_strXMLData = m_oInvoiceMemberData.m_strXMLData;
	m_oEmailClientMemberData.m_strFileName = m_oEmailClientInvoiceMemberData.m_strFileName;
	m_oEmailClientMemberData.m_strSubject = m_oEmailClientInvoiceMemberData.m_strSubject;
	m_oEmailClientMemberData.m_oSelectedContact = m_oEmailClientInvoiceMemberData.m_oSelectedContact;
	m_oEmailClientMemberData.m_arrContacts = m_oEmailClientInvoiceMemberData.m_arrContacts;
	m_oEmailClientMemberData.m_nClientId = m_oEmailClientInvoiceMemberData.m_nClientId;
	loadPage ("crmanagement/emailClient.html", "print_div_dialog", "emailClient_new ()");
	window.scrollTo(0,0);
}