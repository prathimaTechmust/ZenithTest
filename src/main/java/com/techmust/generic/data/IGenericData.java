package com.techmust.generic.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.student.StudentInformationData;

public interface IGenericData
{
	boolean saveObject() throws Exception;
	boolean updateObject() throws Exception;
	boolean deleteObject() throws Exception;
	@SuppressWarnings("unchecked")
    Collection list(HashMap<String, String> arrOrderBy) throws Exception;	
	StudentInformationData getStudentDetails (StudentInformationData oStudentData);
	
}
