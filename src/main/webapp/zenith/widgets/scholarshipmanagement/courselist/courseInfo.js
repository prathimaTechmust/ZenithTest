var courseInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/courselist/CourseInformationData.js'
];

 includeDataObjects (courseInfo_includeDataObjects, "courseInfo_loaded()");

function courseInfo_memberData ()
{
	this.m_nCourseId = -1;
}

var m_oCourseInfoMemberData = new courseInfo_memberData ();

function courseInfo_new ()
{
	courseInfo_init();
	initFormValidateBoxes ("courseInfo_form_id");
}

function courseInfo_init ()
{
	createPopup("dialog", "#courseInfo_button_submit", "#courseInfo_button_cancel", true);
}
function courseInfo_submit ()
{
	if (courseInfo_validate())
		loadPage ("include/process.html", "ProcessDialog", "course_progressbarLoaded ()");
}

function course_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	oCourseInformationData = courseInfo_getFormData ();
	if(document.getElementById("courseInfo_button_submit").getAttribute('update') == "false")
		CourseInformationDataProcessor.create(oCourseInformationData, courseInfo_created);
	else
		CourseInformationDataProcessor.update (oCourseInformationData,courseInfo_updated);
}

function courseInfo_edit ()
{
	courseInfo_init();
	var oCourseInformationData = new CourseInformationData ();
	oCourseInformationData.m_nCourseId = m_oCourseInfoListMemberData.m_nSelectedCourseId;
	document.getElementById("courseInfo_button_submit").setAttribute('update', true);
	document.getElementById("courseInfo_button_submit").innerHTML = "Update";
	CourseInformationDataProcessor.get (oCourseInformationData, courseInfo_gotData);
}

function courseInfo_getFormData ()
{
	var oCourseInformationData = new CourseInformationData ();
	oCourseInformationData.m_nCourseId = m_oCourseInfoMemberData.m_nCourseId;
	oCourseInformationData.m_strShortCourseName = $("#courseInfo_input_shortcourseName").val();
	oCourseInformationData.m_strLongCourseName = $("#courseInfo_input_longcourseName").val();
	if(document.getElementById("courseInfo_input_finalYear_yes").checked)
		oCourseInformationData.m_strFinalYear = $("#courseInfo_input_finalYear_yes").val();
	else
		oCourseInformationData.m_strFinalYear = $("#courseInfo_input_finalYear_no").val();
	var oUserData = getLoginUserData ()
	if(m_oCourseInfoMemberData.m_nCourseId != -1)
	{
		oCourseInformationData.m_dCreatedOn = m_oCourseInfoMemberData.dCreatedOn;
		oCourseInformationData.m_oUserCreatedBy = m_oCourseInfoMemberData.oUserCreatedBy;
		oCourseInformationData.m_oUserUpdatedBy = oUserData;
	}
	else
	{
		oCourseInformationData.m_oUserCreatedBy = oUserData;
		oCourseInformationData.m_oUserUpdatedBy = oUserData;		
	}
	return oCourseInformationData;	
}

function courseInfo_created (oCourseInfoResponse)
{
	HideDialog ("ProcessDialog");
	if (oCourseInfoResponse.m_bSuccess)
	{
		courseInfo_displayInfo("course created successfully", "kSuccess");
	}
	else
		courseInfo_displayInfo("course creation failed", "kError");
}

function courseInfo_updated (oCourseInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oCourseInfoResponse.m_bSuccess)
		courseInfo_displayInfo ("course updated successfully");
	else
		courseInfo_displayInfo ("course updation failed");
	
}

function courseInfo_validate ()
{
	return validateForm("courseInfo_form_id");
}

function courseInfo_displayInfo (strMessage)
{
	HideDialog ("dialog");
	informUser(strMessage, "kSuccess");
	navigate("courseList", "widgets/scholarshipmanagement/courselist/listCourses.js");
}

function courseInfo_gotData (oCourseInfoResponse)
{	
	 var oCourseInfoData = oCourseInfoResponse.m_arrCourseInformationData[0];
	 m_oCourseInfoMemberData.m_nCourseId = oCourseInfoData.m_nCourseId;
	 $("#courseInfo_input_shortcourseName").val(oCourseInfoData.m_strShortCourseName);
	 $("#courseInfo_input_longcourseName").val(oCourseInfoData.m_strLongCourseName);
	 if(oCourseInfoData.m_bFinalYear)
	 {
		 var radiobutton = document.getElementById("courseInfo_input_finalYear_yes");
		 radiobutton.checked = true;
	 }
	 else
	 {
		 
		 var radiobutton = document.getElementById("courseInfo_input_finalYear_no");
		 radiobutton.checked = true;
	 }
	m_oCourseInfoMemberData.dCreatedOn = oCourseInfoData.m_dCreatedOn;
	m_oCourseInfoMemberData.dUpdatedOn = oCourseInfoData.m_dUpdatedOn;
	m_oCourseInfoMemberData.oUserCreatedBy = oCourseInfoData.m_oUserCreatedBy;
	m_oCourseInfoMemberData.oUserUpdatedBy = oCourseInfoData.m_oUserUpdatedBy;
	$("#courseInfo_input_shortcourseName").val(oCourseInfoData.m_strShortCourseName);
	$("#courseInfo_input_longcourseName").val(oCourseInfoData.m_strLongCourseName);
	initFormValidateBoxes ("courseInfo_form_id");
}

function courseInfo_cancel()
{
	HideDialog ("dialog");
}
