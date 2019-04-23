navigate ("newcourse", "widgets/scholarshipmanagement/courseInfo.js");
function courseInfo_loaded ()
{
	m_oCourseInfoMemberData.m_nCourseId = m_oCourseInfoListMemberData.m_nSelectedCourseId;
	loadPage ("scholarshipmanagement/courseInfo.html", "dialog", "courseInfo_edit()");
}