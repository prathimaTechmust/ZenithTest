package com.techmust.generic.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.persistence.EntityManager;

import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus.ZenithScholarshipDetails;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.scholarshipmanagement.studentdocuments.StudentDocuments;

public interface IGenericData
{
	public abstract EntityManager _getEntityManager ();
	//CRUD Operations
	boolean saveObject() throws Exception;
	boolean updateObject() throws Exception;
	boolean deleteObject() throws Exception;
	@SuppressWarnings("unchecked")
    Collection list(HashMap<String, String> arrOrderBy) throws Exception;	
	//Get Particular Student Data
	StudentInformationData getStudentDetails (StudentInformationData oStudentData);
	StudentInformationData getSearchUIDStudentData(StudentInformationData oStudentData);	
	StudentDocuments getStudentUploadDocuments(AcademicDetails oAcademicDetails);
	StudentInformationData getUIDAndAadharFormData(StudentInformationData oData);
	boolean doesCourseHaveAcademic(int nCourseId);
	boolean doesInstitutionHaveAcademic(int nInstitutionId);
	//Get Student List Based on Status
	ArrayList<StudentInformationData> getStatusStudentsList(StudentInformationData oStudentData);
	//Student Application Status Update
	boolean updateStudentApplicationVerifiedStatus(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception;
	boolean updateStudentApplicationApprovedStatus(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception;	
	boolean updateStudentApplicationRejectedStatus(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception;
	boolean disburseCheque (ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception;
	boolean applicationStatusUpdate(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception;
	boolean reVerifyStudentApplication(ZenithScholarshipDetails oZenithData) throws Exception;
	boolean reIssueCheckDetails(ZenithScholarshipDetails oZenithData) throws Exception;
	boolean checkChequePrepared(Set<AcademicDetails> oAcademicDetails);
	boolean claimCheque(ZenithScholarshipDetails oZenithData) throws Exception;	
}
