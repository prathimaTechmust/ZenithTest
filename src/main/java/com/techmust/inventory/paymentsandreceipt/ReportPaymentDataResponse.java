package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class ReportPaymentDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<ReportPaymentData> m_arrReportPaymentData;
	
	public ReportPaymentDataResponse ()
	{
		m_arrReportPaymentData = new ArrayList<ReportPaymentData> ();
	}
}
