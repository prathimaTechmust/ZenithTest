package com.techmust.master.tax;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class TaxDataResponse extends GenericResponse
{
    private static final long serialVersionUID = 1L;
	public ArrayList<Tax> m_arrTax ;
	public long m_nRowCount;
    public TaxDataResponse ()
	{
    	m_arrTax = new ArrayList<Tax> ();
    	m_nRowCount = 0;
	}    
}
