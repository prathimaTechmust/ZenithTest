package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class ReceiptDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<ReceiptData> m_arrReceiptData;

	public String m_strXMLData;
	public static long m_nRowCount;
	
	public ReceiptDataResponse ()
	{
		m_arrReceiptData = new ArrayList<ReceiptData> ();
		m_nRowCount=0;
		m_strXMLData = "";
	}
}
