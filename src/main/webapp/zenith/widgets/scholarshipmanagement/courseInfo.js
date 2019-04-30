var courseInfo_includeDataObjects = 
[
	'widgets/scholarshipmanagement/CourseInformationData.js'
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
	return oCourseInformationData;
}

function courseInfo_created (oCourseInfoResponse)
{
	HideDialog ("ProcessDialog");
	if (oCourseInfoResponse.m_bSuccess)
	{
		courseInfo_displayInfo("coursecreatedsuccessfully", "kSuccess");
	}
	else
		courseInfo_displayInfo("coursecreationfailed", "kError");
}

function courseInfo_updated (oCourseInfoResponse)
{
	HideDialog ("ProcessDialog");
	if(oCourseInfoResponse.m_bSuccess)
		courseInfo_displayInfo ("courseupdatedsuccessfully");
	else
		courseInfo_displayInfo ("courseupdationfailed");
	
}

function courseInfo_validate ()
{
	return validateForm("courseInfo_form_id");
}

function courseInfo_displayInfo (strMessage)
{
	HideDialog ("dialog");
	informUser(strMessage, "kSuccess");
	navigate("courseList", "widgets/scholarshipmanagement/listCourses.js")
}

function courseInfo_gotData (oCourseInfoResponse)
{	
	var oCourseInfoData = oCourseInfoResponse.m_arrCourseInformationData[0];
	m_oCourseInfoMemberData.m_nCourseId = oCourseInfoData.m_nCourseId;	
	 $("#courseInfo_input_shortcourseName").val(oCourseInfoData.m_strShortCourseName);
	 $("#courseInfo_input_longcourseName").val(oCourseInfoData.m_strLongCourseName);
	 initFormValidateBoxes ("courseInfo_form_id");
}

function courseInfo_cancel()
{
	HideDialog ("dialog");
}
