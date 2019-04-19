package com.techmust.inventory.sales;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class DiscountStructureDataResponse extends GenericResponse 
{
	 private static final long serialVersionUID = 1L;
	 public ArrayList<DiscountStructureData> m_arrDiscountStructureData ;
	 public String m_strXMLData;
	 public long m_nRowCount;
	 
	 public DiscountStructureDataResponse ()
	 {
    	m_arrDiscountStructureData = new ArrayList<DiscountStructureData> ();
    	m_strXMLData = "";
    	m_nRowCount=0;
	 }
}
