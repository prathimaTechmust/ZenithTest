var prepareChequeInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',	
	'widgets/scholarshipmanagement/scholarshipdetails/ScholarshipAccountsInformationData.js',
	'widgets/scholarshipmanagement/academicyear/AcademicYear.js'
];

includeDataObjects (prepareChequeInfo_includeDataObjects, "prepareChequeInfo_Loaded()");

function prepareChequeInfo_MemberData ()
{
	this.m_nStudentId = -1;
	this.m_nUID = -1;
	this.m_StudentName = "";
}

var m_oPrepareChequeInfoMemberData = new prepareChequeInfo_MemberData();

function prepareChequeInfo_new ()
{
	prepareCheque_init();
	m_oPrepareChequeInfoMemberData.m_nStudentId = m_oPrepareChequeInfoMemberData.m_oStudentInformationData.m_nStudentId;
	$("#student_input_studentUIDNumber").val(m_oPrepareChequeInfoMemberData.m_oStudentInformationData.m_nUID);
	$("#studentInfo_inputStudentName").val(m_oPrepareChequeInfoMemberData.m_oStudentInformationData.m_strStudentName);
	$("#accountInfo_input_AccountPayeeName").val(m_oPrepareChequeInfoMemberData.m_oStudentInformationData.m_oAcademicDetails[0].m_oInstitutionInformationData.m_strInstitutionName);
	$("#accountInfo_input_SanctionedAmount").val(m_oPrepareChequeInfoMemberData.m_oStudentInformationData.m_oZenithScholarshipDetails[0].m_fApprovedAmount);
	var approveDate = convertTimestampToDate(m_oPrepareChequeInfoMemberData.m_oStudentInformationData.m_oZenithScholarshipDetails[0].m_dApprovedDate);
	$("#accountInfo_input_SanctionedDate").val(approveDate);
	initFormValidateBoxes ("prepareChequeInfo_form_id");

}

function prepareCheque_init ()
{
	createPopup("dialog", "#accountInfo_button_submit", "#accountInfo_button_cancel", true);
	dropdownacademicyear();
}

function dropdownacademicyear ()
{
	var oAcademicYear = new AcademicYear();
	AcademicYearProcessor.list(oAcademicYear,"m_strAcademicYear","asc",0,0,academicyearResponse);	
}

function academicyearResponse(oYearResponse)
{
	populateYear("selectAcademicYear",oYearResponse);
}

function populateYear(academicyear,oYearResponse)
{
	var arrAcademicYears = new Array();
	for(var nIndex = 0; nIndex < oYearResponse.m_arrAcademicYear.length; nIndex++)
	{
		arrAcademicYears.push(CreateOption(oYearResponse.m_arrAcademicYear[nIndex].m_strAcademicYear,oYearResponse.m_arrAcademicYear[nIndex].m_strAcademicYear));		
	}
	PopulateDD(academicyear,arrAcademicYears);	
}

function prepareChequeInfo_submit ()
{
	if (chequeInfo_validate())
		loadPage ("include/process.html", "ProcessDialog", "cheque_progressbarLoaded ()");
	else
	{
		alert("Please Fill Mandiatory Fields");
		$('#prepareChequeInfo_form_id').focus();
	}
	
}

function cheque_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	var chequeInfoData = chequeDetails_getFormData();
	StudentScholarshipAccountsProcessor.create(chequeInfoData,chequeCreatedResponse);
}

function studentUIDResponseLoad()
{
	var oStudentInformationData = new StudentInformationData();
	oStudentInformationData.m_nUID = $("#StudentInfo_input_uid").val();
	StudentInformationDataProcessor.getStudentUID (oStudentInformationData, studentUIDInformationLoad);	
}

function studentUIDInformationLoad (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		document.getElementById("StudentInfo_input_uid").value = "";
		createPopup("dialog", "#prepareChequeInfo_button_submit", "#prepareChequeInfo_button_cancel", true);
		dropdownacademicyear();
		m_oPrepareChequeInfoMemberData.m_nStudentId = oResponse.m_arrStudentInformationData[0].m_nStudentId;
		$("#student_input_studentUIDNumber").val(oResponse.m_arrStudentInformationData[0].m_nUID);
		$("#studentInfo_inputStudentName").val(oResponse.m_arrStudentInformationData[0].m_strStudentName);
		$("#accountInfo_input_AccountPayeeName").val(oResponse.m_arrStudentInformationData[0].m_oAcademicDetails[0].m_oInstitutionInformationData.m_strInstitutionName);
		$("#accountInfo_input_SanctionedAmount").val(oResponse.m_arrStudentInformationData[0].m_oZenithScholarshipDetails[0].m_fApprovedAmount);
		var approveDate = convertTimestampToDate(oResponse.m_arrStudentInformationData[0].m_oZenithScholarshipDetails[0].m_dApprovedDate);
		$("#accountInfo_input_SanctionedDate").val(approveDate);
		initFormValidateBoxes ("prepareChequeInfo_form_id");
	}
	else
		alert("Student UID does not exist");
}

function chequeInfo_validate ()
{
	return validateForm("prepareChequeInfo_form_id");
}

function addChequeLabel ()
{
	document.getElementById("cheque_dd").innerText = "Cheque Number*";
}

function addDDLabel ()
{
	document.getElementById("cheque_dd").innerText = "DD Number*";	
}

function chequeCreatedResponse(oChequeResponse)
{
	if(oChequeResponse.m_bSuccess)
	{
		HideDialog ("ProcessDialog");
		informUser("Cheque Prepared Successfully","kSuccess");
		HideDialog ("dialog");
		sendSMSAndMail(oChequeResponse.m_nStudentId);		
		navigate("chequelist","widgets/applicationstatus/preparecheque/prepareChequelist.js");
	}
	else
		informUser("Cheque Preparation  Failed","kError");	
}

function sendSMSAndMail (nStudentId)
{
	var oScholarshipAccountsInformationData = new ScholarshipAccountsInformationData();
	oScholarshipAccountsInformationData.m_nStudentId = nStudentId;
	StudentScholarshipAccountsProcessor.sendSMSAndMail(oScholarshipAccountsInformationData,smsAndEmailSentResponse);
}

function smsAndEmailSentResponse (oSMSSentResponse)
{
	if(oSMSSentResponse.m_bSuccess)
		informUser("SMS And Email Sent Successfully","kSuccess");	
	else
		informUser("SMS And Email Sent Failed","kError");
}

function chequeDetails_getFormData ()
{
	var oScholarshipAccountsInformationData = new ScholarshipAccountsInformationData();
	oScholarshipAccountsInformationData.m_nStudentId = m_oPrepareChequeInfoMemberData.m_nStudentId;
	oScholarshipAccountsInformationData.m_strPayeeName = $("#accountInfo_input_AccountPayeeName").val();
	oScholarshipAccountsInformationData.m_fSanctionedAmount = convertDateToTimeStamp($("#accountInfo_input_SanctionedAmount").val());
	oScholarshipAccountsInformationData.m_dSanctionDate = $("#accountInfo_input_SanctionedDate").val();
	if(document.getElementById("selectapplication_fresh").checked)
		oScholarshipAccountsInformationData.m_strApplicationType = document.getElementById("selectapplication_fresh").value;
	else
		oScholarshipAccountsInformationData.m_strApplicationType = document.getElementById("selectapplication_reissue").value;
	if(document.getElementById("select_radio_Cheque").checked)
		oScholarshipAccountsInformationData.m_strPaymentType = document.getElementById("select_radio_Cheque").value;
	else
		oScholarshipAccountsInformationData.m_strPaymentType = document.getElementById("select_radio_DD").value;
	oScholarshipAccountsInformationData.m_strAcademicYear = $("#selectAcademicYear").val();
	if(document.getElementById("select_radio_Cheque").checked == true)
		oScholarshipAccountsInformationData.m_nChequeNumber = $("#chequeddInfo_input_ChequeDDNumber").val();
	else
		oScholarshipAccountsInformationData.m_nDDNumber = $("#chequeddInfo_input_ChequeDDNumber").val();	
	return oScholarshipAccountsInformationData;
}

function prepareChequeInfo_cancel ()
{
	HideDialog ("dialog");
}

















