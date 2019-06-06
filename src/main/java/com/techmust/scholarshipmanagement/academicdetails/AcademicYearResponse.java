package com.techmust.scholarshipmanagement.academicdetails;

import java.util.ArrayList;
import java.util.List;

import com.techmust.generic.response.GenericResponse;

public class AcademicYearResponse extends GenericResponse
{	
	private static final long serialVersionUID = 1L;
	public List<AcademicYear> m_arrAcademicYear;
	public Object m_nRowCount;
    public AcademicYearResponse ()
	{
    	m_arrAcademicYear = new ArrayList<AcademicYear> ();
    	m_nRowCount = 0;
	}

}
