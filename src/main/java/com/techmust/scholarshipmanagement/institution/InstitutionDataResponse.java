package com.techmust.scholarshipmanagement.institution;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class InstitutionDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<InstitutionInformationData> m_arrInstitutionInformationData ;
	public Object m_nRowCount;
    public InstitutionDataResponse ()
	{
    	m_arrInstitutionInformationData = new ArrayList<InstitutionInformationData> ();
    	m_nRowCount = 0;
	}
	
}
