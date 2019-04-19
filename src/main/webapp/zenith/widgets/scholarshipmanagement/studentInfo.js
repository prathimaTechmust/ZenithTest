var studentInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/StudentInformationData.js'
];

 includeDataObjects (studentInfo_includeDataObjects, "studentInfo_loaded()");

function studentInfo_memberData ()
{
	this.m_strNewPassword = "";
	this.m_strPassword = "";
	this.m_buffImage = null;
	this.m_nStudentId = -1; 
}

var m_oStudentInfoMemberData = new studentInfo_memberData ();

function studentInfo_new ()
{
	studentInfo_init();
	initFormValidateBoxes ("studentInfo_form_id");
}

function studentInfo_init ()
{
	createPopup("dialog", "#studentInfo_button_submit", "#studentInfo_button_cancel", true);
}
function studentInfo_submit ()
{
	if (studentInfo_validate())
		loadPage ("include/process.html", "ProcessDialog", "student_progressbarLoaded ()");
}

function student_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	oStudentInformationData = studentInfo_getFormData ();
	if(document.getElementById("studentInfo_button_submit").getAttribute('update') == "false")
		StudentInformationDataProcessor.create(oStudentInformationData, studentInfo_created);
	else
		StudentInformationDataProcessor.update (oStudentInformationData,studentInfo_updated);
}

function studentInfo_edit ()
{
	studentInfo_init();
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nFacilitatorId = oStudentInformationData.m_nSelectedStudentId;
	document.getElementById("studentInfo_button_submit").setAttribute('update', true);
	document.getElementById("studentInfo_button_submit").innerHTML = "Update";
	StudentInformationDataProcessor.get (oStudentInformationData, studentInfo_gotData);
}

function studentInfo_getFormData ()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nStudentId = m_oFacilitatorInfoMemberData.m_nStudentId;
	oStudentInformationData.m_strStudentName = $("#studentInfo_input_studentName").val();
	oStudentInformationData.m_strGender = $("#studentInfo_input_male").val();
	oStudentInformationData.m_strFatherName = $("#studentInfo_input_fathername").val();
	oStudentInformationData.m_strFatherOccupation = $("#studentInfo_input_fatheroccupation").val();
	oStudentInformationData.m_strMotherName = $("#studentInfo_input_mothername").val();
	oStudentInformationData.m_strMotherOccupation = $("#studentInfo_input_motheroccupation").val();
	oStudentInformationData.m_strPhoneNumber = $("#studentInfo_input_phoneNumber1").val();
	oStudentInformationData.m_strEmailAddress = $("#studentInfo_input_email").val();
	oStudentInformationData.m_strCurrentAddress = $("#studentInfo_textarea_address").val();
	oStudentInformationData.m_strCity = $("#studentInfo_select_cityName").val();
	oStudentInformationData.m_strState= $("#studentInfo_select_stateName").val();
	oStudentInformationData.m_strPincode = $("#studentInfo_select_pincode").val();
	return oStudentInformationData;
}

function studentInfo_created (oStudentInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oStudentInfoResponse.m_bSuccess && oStudentInfoResponse.m_strError_Desc != "kLoginIdExist")
		studentInfo_displayInfo ("studentcreatedsuccessfully");
	else
		studentInfo_displayInfo ("studentinfo_loginidalreadyexists");
}

function studentInfo_updated (oStudentInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oStudentInfoResponse.m_bSuccess && oStudentInfoResponse.m_strError_Desc != "kLoginIdExist")
		studentInfo_displayInfo ("studentupdatedsuccessfully");
	else
		studentInfo_displayInfo ("studentinfo_loginidalreadyexists");
}

function studentInfo_validate ()
{
	return validateForm("studentInfo_form_id");
}

function studentInfo_displayInfo (strMessage)
{
	HideDialog ("dialog");
	informUser(strMessage, "kSuccess");
	navigate("studentList", "widgets/scholarshipmanagement/listStudent.js")
}

function studentInfo_gotData (oStudentInfoResponse)
{	
	var oStudentInfoData = oStudentInfoResponse.m_arrStudentInformationData[0];
	oStudentInfoData.m_nStudentId = oStudentInfoData.m_nStudentId;
	$("#facilitatorInfo_input_facilitatorName").val(oFacilitatorInfoData.m_strFacilitatorName);
	$("#facilitatorInfo_input_phoneNumber").val(oFacilitatorInfoData.m_strPhoneNumber);
	$("#facilitatorInfo_input_email").val(oFacilitatorInfoData.m_strEmail);
	$("#facilitatorInfo_input_city").val(oFacilitatorInfoData.m_strCity);
	initFormValidateBoxes ("facilitatorInfo_form_id");
}

function studentInfo_cancel()
{
	HideDialog ("dialog");
}
