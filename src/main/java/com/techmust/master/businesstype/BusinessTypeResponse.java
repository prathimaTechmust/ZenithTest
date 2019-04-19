package com.techmust.master.businesstype;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class BusinessTypeResponse extends GenericResponse
{
	public ArrayList<BusinessTypeData> m_arrBusinessType;
	public long m_nRowCount;
	private static final long serialVersionUID = 1L;
	
	public BusinessTypeResponse ()
	{
		m_arrBusinessType = new ArrayList<BusinessTypeData> ();
		m_nRowCount = 0;
	}
}
