package com.techmust.scholarshipmanagement.scholarshipdetails;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class ScholarshipDetailsResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<ScholarshipDetails> m_arrScholarshipDetailsData;
	public Object m_nRowCount;
    public ScholarshipDetailsResponse ()
	{
    	m_arrScholarshipDetailsData = new ArrayList<ScholarshipDetails> ();
    	m_nRowCount = 0;
	}


}
