package com.techmust.scholarshipmanagement.student;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class StudentDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<StudentInformationData> m_arrStudentInformationData ;
	public Object m_nRowCount;
    public StudentDataResponse ()
	{
    	m_arrStudentInformationData = new ArrayList<StudentInformationData> ();
    	m_nRowCount = 0;
	}
}
