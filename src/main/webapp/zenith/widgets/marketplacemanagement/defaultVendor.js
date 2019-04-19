var defaultVendor_includeDataObjects = 
[
	'widgets/vendormanagement/VendorData.js',
	'widgets/vendormanagement/VendorDemographyData.js',
	'widgets/vendormanagement/VendorContactData.js',
	'widgets/vendormanagement/VendorBusinessTypeData.js',
	'widgets/marketplacemanagement/ConfigurationData.js'
];



includeDataObjects (defaultVendor_includeDataObjects, "defaultVendor_loaded ()");

function defaultVendor_MemberData ()
{
	this.m_nDefaultVendorId = -1;
}

var m_oDefaultVendorMemberData = new defaultVendor_MemberData ();

function defaultVendor_loaded ()
{
	loadPage ("marketplacemanagement/defaultVendor.html", "dialog", "defaultVendor_init()");
}

function defaultVendor_init ()
{
	createPopup("dialog", "#defaultVendor_button_set", "#defaultVendor_button_cancel", true);
	defaultVendor_getDefaultVendor ();
	defaultVendor_populateVendors ();
}

function defaultVendor_getDefaultVendor ()
{
	var oConfigurationData = new ConfigurationData ();
	oConfigurationData.m_strKey = "kDefaultVendor";
	ConfigurationDataProcessor.get(oConfigurationData, defaultVendor_gotDefaultVendor)
}

function defaultVendor_gotDefaultVendor (oResponse)
{
	if( oResponse.m_arrConfigurationData[0] != null)
		m_oDefaultVendorMemberData.m_nDefaultVendorId = oResponse.m_arrConfigurationData[0].m_nIntValue;
}

function defaultVendor_populateVendors ()
{
	var oVendorData = new  VendorData ();
	VendorDataProcessor.listVendor (oVendorData, "", "", "", "", function(oResponse)
			{
				defaultVendor_prepareVendorDD ("defaultVendor_select_vendor", oResponse);
			}		
	);
}

function defaultVendor_prepareVendorDD (strVendorID, oResponse)
{
	var arrOptions = new Array ();
	if(m_oDefaultVendorMemberData.m_nDefaultVendorId == -1)
		arrOptions.push (CreateOption(-1, "Select"));
	else
		defaultVendor_setDefaultVendor (arrOptions, oResponse);
	defaultVendor_selectDefaultVendor (arrOptions, strVendorID, oResponse);	
}

function defaultVendor_selectDefaultVendor (arrOptions, strVendorID, oResponse)
{
	for (var nIndex = 0; nIndex < oResponse.m_arrVendorData.length; nIndex++)
	{
		arrOptions.push (CreateOption (oResponse.m_arrVendorData [nIndex].m_nClientId,
					oResponse.m_arrVendorData[nIndex].m_strCompanyName));
	}
	PopulateDD (strVendorID, arrOptions);
}

function defaultVendor_setDefaultVendor (arrOptions, oResponse)
{
	for (var nIndex = 0; nIndex < oResponse.m_arrVendorData.length; nIndex++)
	{
		if(m_oDefaultVendorMemberData.m_nDefaultVendorId == oResponse.m_arrVendorData [nIndex].m_nClientId)
			arrOptions.push (CreateOption (oResponse.m_arrVendorData [nIndex].m_nClientId,
					oResponse.m_arrVendorData[nIndex].m_strCompanyName));
	}
}

function defaultVendor_cancel ()
{
	HideDialog("dialog");
}

function defaultVendor_setVendor ()
{
	if(defaultVendor_selectValidate ())
	{
		var oConfigurationData = defaultVendor_getFormData () 
		ConfigurationDataProcessor.setDefaultVendor (oConfigurationData, defaultVendor_selectedVendor)
	}
}

function defaultVendor_getFormData ()
{
	var oConfigurationData = new ConfigurationData ();
	oConfigurationData.m_strKey = "kDefaultVendor";
	oConfigurationData.m_nIntValue = $("#defaultVendor_select_vendor").val();
	oConfigurationData.m_nDoubleValue = -1;
	oConfigurationData.m_strStringValue = "";	
	return oConfigurationData;
}

function defaultVendor_selectValidate ()
{
	var bIsSelectFieldValid = true;
	if($("#defaultVendor_select_vendor")== -1)
	{
		informUser("Please Select Vendor", "kWarning");
		bIsSelectFieldValid = false;
	}
	return bIsSelectFieldValid;
}

function defaultVendor_selectedVendor (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Default Vendor Set Successfully", "kSuccess");
		HideDialog ("dialog");
	}
}
