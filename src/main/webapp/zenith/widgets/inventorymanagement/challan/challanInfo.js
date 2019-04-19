var challanInfo_includeDataObjects = 
[
	'widgets/inventorymanagement/challan/ChallanData.js',
	'widgets/inventorymanagement/sales/SalesData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

includeDataObjects (challanInfo_includeDataObjects, "challanInfo_loaded ()");

function challanInfo_memberData ()
{
	this.m_strXMLData = "";
	this.m_nChallanId = -1;
	this.m_nInvoiceId = -1;
}

var m_oChallanInfoMemberData = new challanInfo_memberData ();

function challanInfo_getChallanInfo(nChallanId, nIvoiceId)
{
	assert.isNumber(nChallanId, "nChallanId expected to be a Number.");
	assert.isNumber(nIvoiceId, "nIvoiceId expected to be a Number.");
	m_oChallanInfoMemberData.m_nChallanId = nChallanId;
	m_oChallanInfoMemberData.m_nInvoiceId = nIvoiceId;
	var oChallanData = new ChallanData ();
	oChallanData.m_nChallanId = nChallanId;
	ChallanDataProcessor.getXML (oChallanData,	{
		async:false, 
		callback: function (strXMLData)
		{
			m_oChallanInfoMemberData.m_strXMLData = strXMLData;
			navigate ('challan','widgets/inventorymanagement/challan/challan.js');
		}
	});
}

function challan_loaded ()
{
	m_oChallanMemberData.m_strXMLData = m_oChallanInfoMemberData.m_strXMLData;
	m_oChallanMemberData.m_nChallanId = m_oChallanInfoMemberData.m_nChallanId;
	m_oChallanMemberData.m_nInvoiceId = m_oChallanInfoMemberData.m_nInvoiceId;
	loadPage ("inventorymanagement/sales/print.html", "secondDialog", "challan_print ()");
}