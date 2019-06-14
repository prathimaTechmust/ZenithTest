package com.techmust.applicationstatus.verified;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;
import com.techmust.scholarshipmanagement.student.StudentInformationData;

public class VerifiedStudentDataResponse extends GenericResponse
{

	private static final long serialVersionUID = 1L;
	public ArrayList<StudentInformationData> m_arrToBeVerifiedStudentInformationData ;
	public Object m_nRowCount;
    public VerifiedStudentDataResponse ()
	{
    	m_arrToBeVerifiedStudentInformationData = new ArrayList<StudentInformationData> ();
    	m_nRowCount = 0;
	}

}
