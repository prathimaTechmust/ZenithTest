package com.techmust.usermanagement.facilitator;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class FacilitatorDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<FacilitatorInformationData> m_arrFacilitatorInformationData ;
	public Object m_nRowCount;
    public FacilitatorDataResponse ()
	{
		m_arrFacilitatorInformationData = new ArrayList<FacilitatorInformationData> ();
		m_nRowCount = 0;
	}    
}
