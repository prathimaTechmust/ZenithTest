navigate ("newstudent", "widgets/scholarshipmanagement/studentInfo.js");
function studentInfo_loaded ()
{
	m_oStudentInfoMemberData.m_nStudentId = m_oStudentInfoListMemberData.m_nSelectedStudentId;
	loadPage ("scholarshipmanagement/studentInfo.html", "dialog", "studentInfo_edit()");
}