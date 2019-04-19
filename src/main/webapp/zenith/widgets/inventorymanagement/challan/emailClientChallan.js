var emailClientChallan_includeDataObjects = 
[
	'widgets/inventorymanagement/challan/ChallanData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js',
];

includeDataObjects (emailClientChallan_includeDataObjects, "emailClientChallan_loaded()");

function emailClientChallan_memberData ()
{
    this.m_strFileName = "challan.xslt";
	this.m_strSubject = "challan";
	this.m_oSelectedContact = null;
	this.m_arrContacts = new Array ();
	this.m_nClientId = -1;
}

var m_oEmailClientChallanMemberData = new emailClientChallan_memberData ();

function emailClientChallan_loaded ()
{
	var oChallanData = new ChallanData ();
	oChallanData.m_nChallanId = m_oChallanMemberData.m_nChallanId;
	ChallanDataProcessor.get (oChallanData, emailClientChallan_gotChallanData);
}

function emailClientChallan_gotChallanData (oResponse)
{
	m_oEmailClientChallanMemberData.m_arrContacts = emailClientChallan_buildContacts (oResponse.m_arrChallan[0].m_oSalesData);
	navigate ('emailClient','widgets/crmanagement/emailClient.js');
}

function emailClientChallan_buildContacts(oSalesData)
{
	assert.isObject(oSalesData, "oSalesData expected to be an Object.");
	assert( Object.keys(oSalesData).length >0 , "oSalesData cannot be an empty .");// checks for non emptyness 
	var arrContacts = new Array ();
	m_oEmailClientChallanMemberData.m_nClientId = oSalesData.m_oClientData.m_nClientId;
	if(oSalesData.m_oClientData.m_strEmail.trim() != "")
	{
		var oContactData = new ContactData ();
		oContactData.m_strContactName = oSalesData.m_oClientData.m_strCompanyName;
		oContactData.m_strEmail = oSalesData.m_oClientData.m_strEmail;
		arrContacts.push(oContactData);
	}
	if(oSalesData.m_oContactData != null)
		m_oEmailClientChallanMemberData.m_oSelectedContact = oSalesData.m_oContactData;
	for (var nIndex  = 0; nIndex < oSalesData.m_oClientData.m_oContacts.length; nIndex++)
			arrContacts.push(oSalesData.m_oClientData.m_oContacts[nIndex]);
	return arrContacts;
}

function emailClient_loaded ()
{
	m_oEmailClientMemberData.m_strXMLData = m_oChallanMemberData.m_strXMLData;
	m_oEmailClientMemberData.m_strFileName = m_oEmailClientChallanMemberData.m_strFileName;
	m_oEmailClientMemberData.m_strSubject = m_oEmailClientChallanMemberData.m_strSubject;
	m_oEmailClientMemberData.m_oSelectedContact = m_oEmailClientChallanMemberData.m_oSelectedContact;
	m_oEmailClientMemberData.m_arrContacts = m_oEmailClientChallanMemberData.m_arrContacts;
	m_oEmailClientMemberData.m_nClientId = m_oEmailClientChallanMemberData.m_nClientId;
	loadPage ("crmanagement/emailClient.html", "print_div_dialog", "emailClient_new ()");
	window.scrollTo(0,0);
}