package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class InvoiceReceiptDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L; 
	public ArrayList<InvoiceReceiptData> m_arrInvoiceReceiptData;
	
	public InvoiceReceiptDataResponse ()
	{
		m_arrInvoiceReceiptData = new ArrayList<InvoiceReceiptData> ();
	}

}
