var vendorSerialNumber_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorCounterData.js'
];



 includeDataObjects (vendorSerialNumber_includeDataObjects, "vendorSerialNumber_loaded()");

function vendorSerialNumber_memberData ()
{
	this.m_nId = -1;
}

var m_oVendorSerialNumberMemberData = new vendorSerialNumber_memberData ();

function vendorSerialNumber_edit ()
{
	vendorSerialNumber_init ();
	document.getElementById("vendorSerialNumber_button_create").setAttribute('update', true);
	document.getElementById("vendorSerialNumber_button_create").innerHTML = "Update";
	var oVendorCounterData = new VendorCounterData ();
	oVendorCounterData.m_nId = m_oVendorSerialNumberMemberData.m_nId;
	VendorCounterDataProcessor.get(oVendorCounterData,vendorSerialNumber_gotData);
}

function vendorSerialNumber_init ()
{
	createPopup ("dialog", "#vendorSerialNumber_button_create", "#vendorSerialNumber_button_cancel", true);
	initFormValidateBoxes ("vendorSerialNumber_form_id");
}

function vendorSerialNumber_gotData (oResponse)
{
	var oVendorCounterData = oResponse.m_arrVendorCounterData[0];
	m_oVendorSerialNumberMemberData.m_nVendorId= oVendorCounterData.m_oVendorData.m_nClientId;
	m_oVendorSerialNumberMemberData.m_strCurrentPrefix = oVendorCounterData.m_strprefix;
	dwr.util.setValue("vendorSerialNumber_select_vendorName", oVendorCounterData.m_oVendorData.m_strCompanyName);
	dwr.util.setValue("vendorSerialNumber_input_prefix", oVendorCounterData.m_strPrefix);
	dwr.util.setValue("vendorSerialNumber_input_serialNumber", oVendorCounterData.m_nSerialNumber);
	dwr.util.setValue("vendorSerialNumber_input_suffix", oVendorCounterData.m_strSuffix);
}

function vendorSerialNumber_validate ()
{
	return validateForm("vendorSerialNumber_form_id");
}

function vendorSerialNumber_submit()
{
	if (vendorSerialNumber_validate())
	{
		var oVendorCounterData = vendorSerialNumber_getFormData ();
		oVendorCounterData.m_nId = m_oVendorSerialNumberMemberData.m_nId;
		VendorCounterDataProcessor.update(oVendorCounterData, vendorSerialNumber_updated);
	}
}

function vendorSerialNumber_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("vendor serial number updated successfully", "kSuccess");
		vendorSerialNumberList_list("", "", 1, 20);
	}
	HideDialog("dialog");
}

function vendorSerialNumber_cancel()
{
	 HideDialog("dialog");
} 

function vendorSerialNumber_getFormData ()
{
	var oVendorCounterData = new VendorCounterData ();
	oVendorCounterData.m_strPrefix = dwr.util.getValue ("vendorSerialNumber_input_prefix");
	oVendorCounterData.m_nSerialNumber = dwr.util.getValue ("vendorSerialNumber_input_serialNumber");
	oVendorCounterData.m_strSuffix = dwr.util.getValue ("vendorSerialNumber_input_suffix");
	oVendorCounterData.m_oVendorData.m_nClientId = m_oVendorSerialNumberMemberData.m_nVendorId;
	return oVendorCounterData;
}

