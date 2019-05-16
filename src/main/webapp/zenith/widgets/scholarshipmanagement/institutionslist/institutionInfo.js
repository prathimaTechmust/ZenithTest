var institutionInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/institutionslist/InstitutionInformationData.js'
];

 includeDataObjects (institutionInfo_includeDataObjects, "institutionInfo_loaded()");

function institutionInfo_memberData ()
{
	this.m_nInstitutionId = -1;
}

var m_oInstitutionInfoMemberData = new institutionInfo_memberData ();

function institutionInfo_new ()
{
	institutionInfo_init();
	initFormValidateBoxes ("institutionInfo_form_id");
}

function institutionInfo_init ()
{
	createPopup("dialog", "#institutionInfo_button_submit", "#institutionInfo_button_cancel", true);
}
function institutionInfo_submit ()
{
	if (institutionInfo_validate())
		loadPage ("include/process.html", "ProcessDialog", "institution_progressbarLoaded ()");
}

function institution_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	oInstitutionInformationData = institutionInfo_getFormData ();
	if(document.getElementById("institutionInfo_button_submit").getAttribute('update') == "false")
		InstitutionInformationDataProcessor.create(oInstitutionInformationData, institutionInfo_created);
	else
		InstitutionInformationDataProcessor.update (oInstitutionInformationData,institutionInfo_updated);
}

function institutionInfo_edit ()
{
	institutionInfo_init();
	var oInstitutionInformationData = new InstitutionInformationData ();
	oInstitutionInformationData.m_nInstitutionId = m_oInstitutionsInfoListMemberData.m_nSelectedInstitutionId;
	document.getElementById("institutionInfo_button_submit").setAttribute('update', true);
	document.getElementById("institutionInfo_button_submit").innerHTML = "Update";
	InstitutionInformationDataProcessor.get (oInstitutionInformationData, institutionInfo_gotData);
}

function institutionInfo_getFormData ()
{
	var oInstitutionInformationData = new InstitutionInformationData ();
	oInstitutionInformationData.m_nInstitutionId = m_oInstitutionInfoMemberData.m_nInstitutionId;
	oInstitutionInformationData.m_strInstitutionName = $("#institutionInfo_input_institutionName").val();
	oInstitutionInformationData.m_strInstitutionEmailAddress = $("#institutionInfo_input_institutionemail").val();
	oInstitutionInformationData.m_strInstitutionAddress = $("#institutionInfo_textarea_address").val();
	oInstitutionInformationData.m_strContactPersonName = $("#institutionInfo_input_contactpersonname").val();
	oInstitutionInformationData.m_strContactPersonEmail = $("#institutionInfo_input_contactpersonemail").val();
	oInstitutionInformationData.m_strPhoneNumber = $("#institutionInfo_input_phoneNumber").val();
	oInstitutionInformationData.m_strCity = $("#institutionInfo_input_cityName").val();
	oInstitutionInformationData.m_strState = $("#institutionInfo_input_stateName").val();
	oInstitutionInformationData.m_nPincode = $("#institutionInfo_input_pincode").val();	
	return oInstitutionInformationData;
}

function institutionInfo_created (oInstitutionInfoResponse)
{
	HideDialog ("ProcessDialog");
	if (oInstitutionInfoResponse.m_bSuccess)
	{
		institutionInfo_displayInfo("institution created successfully", "kSuccess");
	}
	else
		institutionInfo_displayInfo("institution creation failed", "kError");
}

function institutionInfo_updated (oInstitutionInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oInstitutionInfoResponse.m_bSuccess)
		institutionInfo_displayInfo ("institution updated successfully");
	else
		institutionInfo_displayInfo ("institution updation failed");
	
}

function institutionInfo_validate ()
{
	return validateForm("institutionInfo_form_id");
}

function institutionInfo_displayInfo (strMessage)
{
	HideDialog ("dialog");
	informUser(strMessage, "kSuccess");
	navigate("institutionList", "widgets/scholarshipmanagement/institutionslist/listInstitution.js")
}

function institutionInfo_gotData (oInstitutionInfoResponse)
{	
	var oInstitutionInfoData = oInstitutionInfoResponse.m_arrInstitutionInformationData[0];
	m_oInstitutionInfoMemberData.m_nInstitutionId = oInstitutionInfoData.m_nInstitutionId;	
	 $("#institutionInfo_input_institutionName").val(oInstitutionInfoData.m_strInstitutionName);
	 $("#institutionInfo_input_institutionemail").val(oInstitutionInfoData.m_strInstitutionEmailAddress);
	 $("#institutionInfo_textarea_address").val(oInstitutionInfoData.m_strInstitutionAddress);
	 $("#institutionInfo_input_contactpersonname").val(oInstitutionInfoData.m_strContactPersonName);
	 $("#institutionInfo_input_contactpersonemail").val(oInstitutionInfoData.m_strContactPersonEmail);
	 $("#institutionInfo_input_phoneNumber").val(oInstitutionInfoData.m_strPhoneNumber);
	 $("#institutionInfo_input_cityName").val(oInstitutionInfoData.m_strCity);
	 $("#institutionInfo_input_stateName").val(oInstitutionInfoData.m_strState);
	 $("#institutionInfo_input_pincode").val(oInstitutionInfoData.m_nPincode);
	 initFormValidateBoxes ("institutionInfo_form_id");	
}

function institutionInfo_cancel()
{
	HideDialog ("dialog");
}
