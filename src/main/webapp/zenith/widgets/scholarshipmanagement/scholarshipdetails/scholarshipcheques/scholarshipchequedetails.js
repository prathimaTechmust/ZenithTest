var scholarshipChequeInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/studentlist/StudentInformationData.js',
	'widgets/scholarshipmanagement/scholarshipdetails/scholarshipcheques/ScholarshipChequeInformationData.js'
];

includeDataObjects (scholarshipChequeInfo_includeDataObjects, "scholarshipChequeInfo_loaded()");

function scholarshipChequeInfo_MemberData()
{
	this.m_strStatus = "";
}

var m_oScholarshipChequeMemberData = new scholarshipChequeInfo_MemberData ();

function scholarshipChequeInfo_loaded ()
{
	loadPage ("scholarshipmanagement/scholarship/accountsandcheques/scholarshipcheque.html", "dialog", "scholarshipChequeInfo_init ()");
}

function scholarshipChequeInfo_init ()
{
	createPopup("dialog", "#chequeInfo_button_submit", "#chequeInfo_button_cancel", true);	
}

function chequeInfo_submit()
{
	
}

function searchStudentUID ()
{
	var oStudentInformationData = new StudentInformationData ();
	oStudentInformationData.m_nUID = $("#student_input_studentUIDNumber").val();
	StudentInformationDataProcessor.getStudentUID (oStudentInformationData, studentInfo_StudentUIDData);
}

function studentInfo_StudentUIDData (oStudentUIDResponse)
{
	
	HideDialog ("ProcessDialog");
	if(!oStudentUIDResponse.m_bSuccess)
	{
		HideDialog("dialog");
	}
}

function chequeInfo_cancel ()
{
	HideDialog ("dialog");
}
