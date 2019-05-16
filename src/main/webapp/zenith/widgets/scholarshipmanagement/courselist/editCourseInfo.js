navigate ("newcourse", "widgets/scholarshipmanagement/courselist/courseInfo.js");
function courseInfo_loaded ()
{
	m_oCourseInfoMemberData.m_nCourseId = m_oCourseInfoListMemberData.m_nSelectedCourseId;
	loadPage ("scholarshipmanagement/course/courseInfo.html", "dialog", "courseInfo_edit()");
}