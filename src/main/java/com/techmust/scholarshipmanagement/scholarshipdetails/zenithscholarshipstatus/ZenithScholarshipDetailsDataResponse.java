package com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class ZenithScholarshipDetailsDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<ZenithScholarshipDetails> m_arrZenithScholarshipDetails;
	public Object m_nRowCount;
    public ZenithScholarshipDetailsDataResponse ()
	{
    	m_arrZenithScholarshipDetails = new ArrayList<ZenithScholarshipDetails> ();
    	m_nRowCount = 0;
	}
}
