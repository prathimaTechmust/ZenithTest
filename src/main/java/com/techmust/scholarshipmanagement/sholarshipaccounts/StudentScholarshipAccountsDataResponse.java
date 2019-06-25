package com.techmust.scholarshipmanagement.sholarshipaccounts;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class StudentScholarshipAccountsDataResponse extends GenericResponse
{

	private static final long serialVersionUID = 1L;	
	public ArrayList<StudentScholarshipAccount> m_arrStudentAccountInformationData ;
	public Object m_nRowCount;
    public StudentScholarshipAccountsDataResponse ()
	{
    	m_arrStudentAccountInformationData = new ArrayList<StudentScholarshipAccount> ();
    	m_nRowCount = 0;
	}

}
