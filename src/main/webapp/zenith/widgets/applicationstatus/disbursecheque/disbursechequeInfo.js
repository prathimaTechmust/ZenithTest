var disburseStudentChequeInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/zenithscholarship/ZenithScholarshipDetails.js'
];

includeDataObjects (disburseStudentChequeInfo_includeDataObjects, "disburseStudentChequeInfo_loaded()");

function disburseStudentChequeInfo_MemberData()
{
	this.m_strStatus = "";
	this.m_nStudentId = -1;
	this.m_nUID = -1;
	this.m_nChequeNumber = -1;
	this.m_strStudentName = "";
}

var m_oDisburseStudentChequeInfo_MemberData = new disburseStudentChequeInfo_MemberData ();

function disburseCheque_new ()
{
	disburseStudentChequeInfo_init ();
	disburseStudentChequeInfo_Loaded();
	
}

function disburseStudentChequeInfo_Loaded ()
{
	$("#student_input_studentUIDNumber").val(m_oDisburseStudentChequeInfo_MemberData.m_nUID);
	$("#studentInfo_inputStudentName").val(m_oDisburseStudentChequeInfo_MemberData.m_strStudentName);
	$("#studentInfo_inputStudentcheque_dd").val(m_oDisburseStudentChequeInfo_MemberData.m_nChequeNumber);
}

function disburseStudentChequeInfo_init ()
{
	createPopup("dialog", "#chequeInfo_button_submit", "#chequeInfo_button_cancel", true);	
	initFormValidateBoxes ("disburseStudentChequeInfo_form_id");
}

function disburseChequeInfo_submit()
{
	if (disburseChequeInfo_validate())
		loadPage ("include/process.html", "ProcessDialog", "disburseCheque_progressbarLoaded ()");
	else
	{
		alert("Please Fill Mandiatory Fields");
		$('#disburseStudentChequeInfo_form_id').focus();
	}	
}

function disburseCheque_progressbarLoaded ()
{
	var disburseChequeData = getDisburseChequeFormData ();	
	ZenithStudentInformationDataProcessor.issueCheque(disburseChequeData,disburseChequeResponse);
}

function getDisburseChequeFormData ()
{
	var oZenithScholarshipDetails = new ZenithScholarshipDetails ();
	oZenithScholarshipDetails.m_strReceiverName = $("#chequeInfo_inputReceivername").val();
	oZenithScholarshipDetails.m_strReceiverContactNumber = $("#chequeInfo_inputReceiverContactNumber").val();
	oZenithScholarshipDetails.m_dChequeIssueDate = convertDateToTimeStamp($("#chequeInfo_input_IssueDate").val());
	oZenithScholarshipDetails.m_nStudentId = m_oDisburseStudentChequeInfo_MemberData.m_nStudentId;
	return oZenithScholarshipDetails;
}

function disburseChequeResponse(oDisburseChequeResponse)
{
	if(oDisburseChequeResponse.m_bSuccess)
	{
		informUser("cheque dibursed successfully","kSuccess");
		HideDialog ("dialog");
		navigate("disbursechequelist","widgets/applicationstatus/disbursecheque/disbursechequelist.js");
	}		
	else
		informUser("cheque disbursed Failed","kError");
}

function searchStudentUID ()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nUID = $("#StudentInfo_input_uid").val();
	if(oStudentInformationData.m_nUID != "")
		StudentInformationDataProcessor.getStudentUID (oStudentInformationData, studentInfo_StudentUIDData);
	else
		alert("Please Enter Valid UID Number");
}

function studentInfo_StudentUIDData (oStudentUIDResponse)
{
	
	HideDialog ("ProcessDialog");
	if(oStudentUIDResponse.m_bSuccess)
	{
		disburseStudentChequeInfo_init ();
		m_oDisburseStudentChequeInfo_MemberData.m_nStudentId = oStudentUIDResponse.m_arrStudentInformationData[0].m_nStudentId;
		$("#student_input_studentUIDNumber").val(oStudentUIDResponse.m_arrStudentInformationData[0].m_nUID);
		$("#studentInfo_inputStudentName").val(oStudentUIDResponse.m_arrStudentInformationData[0].m_strStudentName);
		$("#studentInfo_inputStudentcheque_dd").val(oStudentUIDResponse.m_arrStudentInformationData[0].m_oAcademicDetails[0].m_oStudentScholarshipAccount[0].m_nChequeNumber);
		validateForm("disburseStudentChequeInfo_form_id");
	}
}

function disburseChequeInfo_validate ()
{
	return validateForm("disburseStudentChequeInfo_form_id");
}

function disburseChequeInfo_cancel ()
{
	HideDialog ("dialog");
}
