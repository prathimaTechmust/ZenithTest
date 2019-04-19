package com.techmust.organization;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class OrganizationDataResponse extends GenericResponse
{
	 private static final long serialVersionUID = 1L;
		public ArrayList<OrganizationInformationData> m_arrOrganizationInformationData ;
		public Object m_nRowCount;
	    public OrganizationDataResponse ()
		{
	    	m_arrOrganizationInformationData = new ArrayList<OrganizationInformationData> ();
	    	m_nRowCount = 0;
		}    
}
