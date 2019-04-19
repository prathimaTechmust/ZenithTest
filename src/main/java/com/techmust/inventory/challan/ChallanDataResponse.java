package com.techmust.inventory.challan;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class ChallanDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	 public ArrayList<ChallanData> m_arrChallan ;
	 public String m_strXMLData;
	 public long m_nRowCount;
	 public ChallanDataResponse ()
	 {
		 m_arrChallan = new ArrayList<ChallanData> ();
		 m_strXMLData = "";
		 m_nRowCount = 0;
	 }
}
