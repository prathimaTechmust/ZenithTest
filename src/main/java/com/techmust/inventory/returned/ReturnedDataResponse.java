package com.techmust.inventory.returned;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class ReturnedDataResponse extends GenericResponse 
{
	 private static final long serialVersionUID = 1L;
	 public ArrayList<ReturnedData> m_arrReturnedData ;
	 public String m_strXMLData;
	 public long m_nRowCount;
	 
	 public ReturnedDataResponse ()
	 {
		 m_arrReturnedData = new ArrayList<ReturnedData> ();
		 m_strXMLData = "";
		 m_nRowCount = 0;
	 }
}
