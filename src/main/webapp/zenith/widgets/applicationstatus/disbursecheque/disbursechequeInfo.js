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
	$("#student_input_studentUIDNumber").val(m_oDisburseStudentChequeInfo_MemberData.m_oStudentInformationData.m_nUID);
	$("#studentInfo_inputStudentName").val(m_oDisburseStudentChequeInfo_MemberData.m_oStudentInformationData.m_strStudentName);
	$("#studentInfo_inputStudentcheque_dd").val(m_oDisburseStudentChequeInfo_MemberData.m_oStudentInformationData.m_oAcademicDetails[0].m_oStudentScholarshipAccount.m_nChequeNumber);
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

function disburseChequeInfo_validate ()
{
	return validateForm("disburseStudentChequeInfo_form_id");
}

function disburseChequeInfo_cancel ()
{
	HideDialog ("dialog");
}


function reIssueCheque_new() 
{
	reIssueCheque_init();
}

function reIssueCheque_init()
{	
	createPopup('dialog','#chequeRemarkInfo_button_submit','chequeRemarkInfo_button_cancel', true);
	initFormValidateBoxes('chequeRemarkForm');	
}

function chequeRemarkInfo_submit()
{
	
	if(chequeRemarkValidate ())
		loadPage ("include/process.html", "ProcessDialog", "chequeRemark_progressbarLoaded ()");
	else
	{
		alert("Please Enter Remarks");
		$("#chequeRemarkForm").focus();
	}	
}
function chequeRemarkValidate ()
{
	return validateForm("chequeRemarkForm");
}

function chequeRemarkInfo_cancel() {
	
	HideDialog("dialog");
}

function chequeRemark_progressbarLoaded() {
	
	createPopup('dialog','','',true);
	var oZenith = new ZenithScholarshipDetails ();		
	oZenith.m_nStudentId = m_oDisburseStudentChequeInfo_MemberData.m_nStudentId;
	oZenith.m_strChequeRemarks = $("#chequeRemarkInfo_input_Remark").val();
	ZenithStudentInformationDataProcessor.reIssueChequeStatusUpdate(oZenith,reIssueChequeResponse);
}

function reIssueChequeResponse (oResponse)
{
		
		if(oResponse.m_bSuccess)
		{
			informUser("Application Status Sent to prepareCheque Successfully","kSuccess");
			HideDialog("dialog");
			navigate("reIssueCheque","widgets/applicationstatus/disbursecheque/disbursechequelist.js");
		}
		else
			informUser("Application Status Sent to prepareCheque Failed","kError");
		
	}



