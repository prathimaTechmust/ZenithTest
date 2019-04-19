var organizationInfo_includeDataObjects = 
[
	'widgets/organization/organizationInformationData.js'
];

 includeDataObjects (organizationInfo_includeDataObjects, "organizationInfo_loaded()");

function organizationInfo_memberData ()
{
}

var m_oorganizationInfoMemberData = new organizationInfo_memberData ();

function organizationInfo_new ()
{
	organizationInfo_init();
	initFormValidateBoxes ("organizationInfo_form_id");
}

function organizationInfo_init ()
{
	createPopup("dialog", "#organizationInfo_button_submit", "#organizationInfo_button_cancel", true);
}



function organizationInfo_submit ()
{
	if (organizationInfo_validate())
		loadPage ("include/process.html", "ProcessDialog", "organization_progressbarLoaded ()");
}

function organization_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	var oorganizationInformationData = organizationInfo_getFormData ();
	if(document.getElementById("organizationInfo_button_submit").getAttribute('update') == "false")
		OrganizationDataProcessor.create(oorganizationInformationData, organizationInfo_created);
	else
		organizationInfo_updateorganizationInfo (oorganizationInformationData);
}

function organizationInfo_validate ()
{
	return validateForm("organizationInfo_form_id");
}

function organizationInfo_getFormData ()
{
	var oorganizationInformationData = new organizationInformationData ();
	oorganizationInformationData.m_strLoginID = $("#organizationInfo_input_loginId").val();
	oorganizationInformationData.m_strPassword = CryptoJS.SHA3($("#organizationInfo_input_password").val()).toString();
	oorganizationInformationData.m_strOrganizationName = $("#organizationInfo_input_organizationName").val();
	oorganizationInformationData.m_strAddress = $("#organizationInfo_textarea_address").val();
	oorganizationInformationData.m_strPhoneNumber = $("#organizationInfo_input_phoneNumber").val();
	oorganizationInformationData.m_strEmailAddress = $("#organizationInfo_input_email").val();
	oorganizationInformationData.m_nStatus = "kActive";
	return oorganizationInformationData;
}

function organizationInfo_created (oorganizationInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oorganizationInfoResponse.m_bSuccess && oorganizationInfoResponse.m_strError_Desc != "kLoginIdExist")
		organizationInfo_displayInfo ("usermessage_organizationInfo_organizationcreatedsuccessfully");
	else
		organizationInfo_displayErrorInfo ("usermessage_organizationInfo_loginidalreadyexists");
}

function organizationInfo_updateorganizationInfo (oorganizationInformationData)
{
	oorganizationInformationData.m_nUserId = m_oorganizationInfoListMemberData.m_nSelectedUserId;
	oorganizationInformationData.m_nUID = m_oorganizationInfoListMemberData.m_nSelectedUID;
	oorganizationInformationData.m_strPassword = m_oorganizationInfoMemberData.m_strPassword;
	oorganizationInformationData.m_strNewPassword = m_oorganizationInfoMemberData.m_strNewPassword;
	organizationInformationDataProcessor.update(oorganizationInformationData, organizationInfo_updated);
}

function organizationInfo_updated (oorganizationInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oorganizationInfoResponse.m_bSuccess)
		organizationInfo_displayInfo ("usermessage_organizationInfo_userupdatedsuccessfully")
	else if(oorganizationInfoResponse.m_nErrorID)
		organizationInfo_displayErrorInfo ("usermessage_organizationInfo_loginidalreadyexistspleaseenteranewloginid")
}

function organizationInfo_displayInfo (strMessage)
{
	HideDialog ("dialog");
	informUser(strMessage, "kSuccess");
	navigate("userList", "widgets/usermanagement/organizationInfo/listorganizationInfo.js")
}

function organizationInfo_displayErrorInfo (strErrorMessage)
{
	informUser(strErrorMessage, "kError");
	document.getElementById("organizationInfo_input_loginId").value = "";
}

function organizationInfo_cancel ()
{
	HideDialog ("dialog");
}

