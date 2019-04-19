package com.techmust.inventory.sales;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.techmust.generic.response.GenericResponse;

public class SalesDataResponse extends GenericResponse 
{
	 private static final long serialVersionUID = 1L;
	 
	 Logger m_oLogger = Logger.getLogger(SalesDataResponse.class.getName());
	 
	 public ArrayList<SalesData> m_arrSales ;
	 public String m_strXMLData;
	 public long m_nRowCount;
	 public SalesDataResponse ()
	 {
    	m_arrSales = new ArrayList<SalesData> ();
    	m_strXMLData = "";
    	m_nRowCount=0;
	 }
}
