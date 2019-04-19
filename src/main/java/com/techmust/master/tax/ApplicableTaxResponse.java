package com.techmust.master.tax;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class ApplicableTaxResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<ApplicableTax> m_arrApplicableTax ;
	public ArrayList<Tax> m_arrTax;
	public long m_nRowCount;
    public ApplicableTaxResponse ()
	{
    	m_arrApplicableTax = new ArrayList<ApplicableTax> ();
    	m_arrTax = new ArrayList<Tax> ();
    	m_nRowCount = 0;
	}
}
