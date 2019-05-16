var facilitatorInfo_includeDataObjects = 
[	
	'widgets/usermanagement/facilitator/FacilitatorInformationData.js'
];

 includeDataObjects (facilitatorInfo_includeDataObjects, "facilitatorInfo_loaded()");

function facilitatorInfo_memberData ()
{	
	this.m_buffImage = null;
	this.m_FacilitatorId = -1; 
}

var m_oFacilitatorInfoMemberData = new facilitatorInfo_memberData ();

function facilitatorInfo_new ()
{
	facilitatorInfo_init();
	initFormValidateBoxes ("facilitatorInfo_form_id");
}

function facilitatorInfo_init ()
{
	createPopup("dialog", "#userInfo_button_submit", "#userInfo_button_cancel", true);
}
function facilitatorInfo_submit ()
{
	if (facilitatorInfo_validate())
		loadPage ("include/process.html", "ProcessDialog", "facilitator_progressbarLoaded ()");
}

function facilitator_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	oFacilitatorInformationData = facilitatorInfo_getFormData ();
	if(document.getElementById("facilitatorInfo_button_submit").getAttribute('update') == "false")
		FacilitatorInformationDataProcessor.create(oFacilitatorInformationData, facilitatorInfo_created);
	else
		FacilitatorInformationDataProcessor.update (oFacilitatorInformationData,facilitatorInfo_updated);
}

function facilitatorInfo_edit ()
{
	facilitatorInfo_init();
	var oFacilitatorInformationData = new FacilitatorInformationData ();
	oFacilitatorInformationData.m_nFacilitatorId = m_oFacilitatorInfoListMemberData.m_nSelectedFacilitatorId;
	document.getElementById("facilitatorInfo_button_submit").setAttribute('update', true);
	document.getElementById("facilitatorInfo_button_submit").innerHTML = "Update";
	FacilitatorInformationDataProcessor.get (oFacilitatorInformationData, facilitatorInfo_gotData);
}

function facilitatorInfo_getFormData ()
{
	var oFacilitatorInformationData = new FacilitatorInformationData ();
	oFacilitatorInformationData.m_nFacilitatorId = m_oFacilitatorInfoMemberData.m_FacilitatorId;
	oFacilitatorInformationData.m_strFacilitatorName = $("#facilitatorInfo_input_facilitatorName").val();
	oFacilitatorInformationData.m_strEmail = $("#facilitatorInfo_input_email").val();
	oFacilitatorInformationData.m_strPhoneNumber = $("#facilitatorInfo_input_phoneNumber").val();
	oFacilitatorInformationData.m_strCity = $("#facilitatorInfo_input_city").val();
	return oFacilitatorInformationData;
}

function facilitatorInfo_created (oFacilitatorInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oFacilitatorInfoResponse.m_bSuccess && oFacilitatorInfoResponse.m_strError_Desc != "kLoginIdExist")
		facilitatorInfo_displayInfo ("facilitator created successfully");
	else
		facilitatorInfo_displayInfo ("facilitatorinfo_loginidalreadyexists");
}

function facilitatorInfo_updated (oFacilitatorInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oFacilitatorInfoResponse.m_bSuccess && oFacilitatorInfoResponse.m_strError_Desc != "kLoginIdExist")
		facilitatorInfo_displayInfo ("facilitator updated successfully");
	else
		facilitatorInfo_displayInfo ("facilitatorinfo_loginid already exists");
}

function facilitatorInfo_validate ()
{
	return validateForm("facilitatorInfo_form_id");
}

function facilitatorInfo_displayInfo (strMessage)
{
	HideDialog ("dialog");
	informUser(strMessage, "kSuccess");
	navigate("userList", "widgets/usermanagement/facilitator/listFacilitatorInfo.js")
}

function facilitatorInfo_gotData (oFacilitatorInfoResponse)
{	
	var oFacilitatorInfoData = oFacilitatorInfoResponse.m_arrFacilitatorInformationData[0];
	oFacilitatorInfoData.m_nFaclitatorId = oFacilitatorInfoData.m_nFaclitatorId;
	$("#facilitatorInfo_input_facilitatorName").val(oFacilitatorInfoData.m_strFacilitatorName);
	$("#facilitatorInfo_input_phoneNumber").val(oFacilitatorInfoData.m_strPhoneNumber);
	$("#facilitatorInfo_input_email").val(oFacilitatorInfoData.m_strEmail);
	$("#facilitatorInfo_input_city").val(oFacilitatorInfoData.m_strCity);
	initFormValidateBoxes ("facilitatorInfo_form_id");
}

function facilitatorInfo_cancel()
{
	HideDialog ("dialog");
}
