package com.techmust.vendormanagement;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class VendorGroupResponse extends GenericResponse 
{

	private static final long serialVersionUID = 1L;
	public ArrayList<VendorGroupData> m_arrGroupData ;
	public long m_nRowCount;
	public VendorGroupResponse ()
		{
	    	m_arrGroupData = new ArrayList<VendorGroupData> ();
	    	m_nRowCount=0;
		}
}
