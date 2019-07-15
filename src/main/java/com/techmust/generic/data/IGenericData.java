package com.techmust.generic.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus.ZenithScholarshipDetails;
import com.techmust.scholarshipmanagement.student.StudentInformationData;

public interface IGenericData
{
	boolean saveObject() throws Exception;
	boolean updateObject() throws Exception;
	boolean deleteObject() throws Exception;
	@SuppressWarnings("unchecked")
    Collection list(HashMap<String, String> arrOrderBy) throws Exception;	
	StudentInformationData getStudentDetails (StudentInformationData oStudentData);
	ArrayList<StudentInformationData> getStatusStudentsList(StudentInformationData oStudentData);
	boolean updateStudentApplicationVerifiedStatus(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception;
	boolean updateStudentApplicationApprovedStatus(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception;
	boolean doesCourseHaveAcademic(int nCourseId);
	boolean doesInstitutionHaveAcademic(int nInstitutionId);
	boolean updateStudentApplicationRejectedStatus(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception;
	boolean disburseCheque (ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception;
	boolean applicationStatusUpdate(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception;
	boolean reVerifyStudentApplication(ZenithScholarshipDetails oZenithData) throws Exception;
	boolean reIssueCheckDetails(ZenithScholarshipDetails oZenithData) throws Exception;
	boolean checkChequePrepared(Set<AcademicDetails> oAcademicDetails);
	StudentInformationData getSearchUIDStudentData(StudentInformationData oStudentData);
	
}
