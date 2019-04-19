package com.techmust.inventory.quotation;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class QuotationDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<QuotationData> m_arrQuotations ;
	 public String m_strXMLData;
	public long m_nRowCount;
	
	public QuotationDataResponse ()
	{
		m_arrQuotations = new ArrayList<QuotationData> ();
    	m_nRowCount=0;
	}
}
