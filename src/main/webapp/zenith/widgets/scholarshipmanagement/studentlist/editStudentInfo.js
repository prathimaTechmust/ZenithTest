navigate ("newstudent", "widgets/scholarshipmanagement/studentlist/studentInfo.js");
function studentInfo_loaded ()
{
	m_oStudentInfoMemberData.m_nStudentId = m_oStudentInfoListMemberData.m_nSelectedStudentId;
	m_oStudentInfoMemberData.m_nAcademicYearId = m_oStudentInfoListMemberData.m_nAcademicYearId;
	loadPage ("scholarshipmanagement/student/studentInfo.html", "dialog", "studentInfo_edit()");
}