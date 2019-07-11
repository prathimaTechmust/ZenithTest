navigate ("newstudent", "widgets/scholarshipmanagement/studentlist/studentInfo.js");
function studentInfo_loaded ()
{
	m_oStudentInfoMemberData.m_nStudentId = m_oStudentInfoListMemberData.m_nSelectedStudentId;
	m_oStudentInfoMemberData.m_strAcademicYear = m_oStudentInfoListMemberData.m_strAcademicYear;
	loadPage ("scholarshipmanagement/student/studentInfo.html", "dialog", "studentInfo_edit()");
}