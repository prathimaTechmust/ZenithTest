package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class ReportReceiptDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<ReportReceiptData> m_arrReportReceiptData;
	
	public ReportReceiptDataResponse ()
	{
		m_arrReportReceiptData = new ArrayList<ReportReceiptData> ();
	}
}
