navigate ("newstudent", "widgets/scholarshipmanagement/studentlist/studentInfo.js");
function studentInfo_loaded ()
{
	m_oStudentInfoMemberData.m_nStudentId = m_oStudentInfoListMemberData.m_nSelectedStudentId;
	loadPage ("scholarshipmanagement/student/studentInfo.html", "dialog", "studentInfo_edit()");
}