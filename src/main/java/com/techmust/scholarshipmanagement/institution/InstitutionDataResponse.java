package com.techmust.scholarshipmanagement.institution;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;
import com.techmust.scholarshipmanagement.chequeFavourOf.ChequeInFavourOf;

public class InstitutionDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<InstitutionInformationData> m_arrInstitutionInformationData;
	public ArrayList<ChequeInFavourOf> m_arrChequeInFavourOf;
	public Object m_nRowCount;
    public InstitutionDataResponse ()
	{
    	m_arrInstitutionInformationData = new ArrayList<InstitutionInformationData> ();
    	m_nRowCount = 0;
    	m_arrChequeInFavourOf = new ArrayList<ChequeInFavourOf>();
	}	
}
