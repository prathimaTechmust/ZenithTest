package com.techmust.inventory.quotation.logs;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class QuotationLogDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<QuotationLogData> m_arrQuotationLogs ;
	public long m_nRowCount;
    public QuotationLogDataResponse ()
	{
    	m_arrQuotationLogs = new ArrayList<QuotationLogData> ();
    	m_nRowCount = 0;
	} 
}
