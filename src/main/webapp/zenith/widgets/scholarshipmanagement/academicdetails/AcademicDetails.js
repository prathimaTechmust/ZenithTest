function AcademicDetails ()
{
	this.m_nAcademicId = -1;
	this.m_nInstitutionName = -1;
	this.m_nCourseName = -1;	
	this.m_strStudentScore = "";
	this.m_strSpecialization = "";
	this.m_fAnnualFee = 0;
	this.m_fPaidFee = 0;
	this.m_fBalanceFee = 0;	
	this.m_nAcademicYearId = "";
	this.m_arrScholarshipDetails = new Array();
	
}
dataObjectLoaded ();