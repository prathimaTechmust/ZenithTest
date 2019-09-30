package com.techmust.scholarshipmanagement.scholarshipdetails.scholarshiporganization;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class ScholarshipOrganizationDetailsResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<ScholarshipOrganizationDetails> m_arrScholarshipOrganizationDetailsData;
	public Object m_nRowCount;
    public ScholarshipOrganizationDetailsResponse ()
	{
    	m_arrScholarshipOrganizationDetailsData = new ArrayList<ScholarshipOrganizationDetails> ();
    	m_nRowCount = 0;
	}
}
