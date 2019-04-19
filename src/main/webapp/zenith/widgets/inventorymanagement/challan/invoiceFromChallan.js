var invoiceFromChallan_includeDataObjects = 
[
	'widgets/inventorymanagement/challan/ChallanData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

includeDataObjects (invoiceFromChallan_includeDataObjects, "invoiceFromChallan_loaded()");

function invoiceFromChallan_memberData ()
{
    this.m_nChallanId = -1;
    this.m_strXMLData = "";
    this.m_strEmailAddress = "";
    this.m_strSubject = "";
    this.m_oInvoiceData = null;
}

var m_oInvoiceFromChallanMemberData = new invoiceFromChallan_memberData ();

function invoiceFromChallan_loaded ()
{
	processConfirmation ('Yes', 'No', 'Do you want to make invoice ?', invoiceFromChallan_gotConfirmation);
}

function invoiceFromChallan_gotConfirmation (bConfirm)
{
	assert.isBoolean(bConfirm, "bConfirm should be a boolean value");
	if(bConfirm)
	{
		var oChallanData = new ChallanData ();
		oChallanData.m_nChallanId = m_oChallanMemberData.m_nChallanId;
		var oCreatedby = new UserInformationData ();
		oCreatedby.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oCreatedby.m_nUID = m_oTrademustMemberData.m_nUID;
		ChallanDataProcessor.makeInvoice (oChallanData, oCreatedby, invoiceFromChallan_madeInvoice);
	}
}

function invoiceFromChallan_madeInvoice (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		m_oInvoiceFromChallanMemberData.m_strXMLData = oResponse.m_strXMLData;
		m_oInvoiceFromChallanMemberData.m_oInvoiceData = oResponse.m_arrInvoice[0];
		//m_oInvoiceFromChallanMemberData.m_strEmailAddress = m_oChallanListMemberData.m_oRowData.m_oSalesData.m_oContactData != null ? m_oChallanListMemberData.m_oRowData.m_oSalesData.m_oContactData.m_strEmail : "";
		//m_oInvoiceFromChallanMemberData.m_strSubject = "Invoice";
		navigate ('challan','widgets/inventorymanagement/challan/challanInvoice.js');
		try
		{
			challanList_list ("m_dCreatedOn", "desc", 1, 10);
		}catch(oException){}
	}
	else
		informUser (" Invoice generation failed! ", "kSuccess");
}