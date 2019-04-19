package com.techmust.vendormanagement;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class VendorCounterDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<VendorCounterData> m_arrVendorCounterData;
	public long m_nRowCount;
	
	public VendorCounterDataResponse ()
	{
		m_arrVendorCounterData = new ArrayList<VendorCounterData> ();
		m_nRowCount = 0;
	}
}
