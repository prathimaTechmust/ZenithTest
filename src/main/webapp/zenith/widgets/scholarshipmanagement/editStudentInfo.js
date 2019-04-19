navigate ("newstudent", "widgets/scholarshipmanagement/studentInfo.js");
function studentInfo_loaded ()
{
	m_oStudentInfoListMemberData.m_StudentId = m_oStudentInfoListMemberData.m_nSelectedStudentId;
	loadPage ("scholarshipmanagement/studentInfo.html", "dialog", "studentInfo_edit()");
}