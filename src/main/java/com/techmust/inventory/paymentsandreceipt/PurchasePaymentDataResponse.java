package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class PurchasePaymentDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<PurchasePaymentData> m_arrPurchasePaymentData;
	public static long m_nRowCount;
	
	public PurchasePaymentDataResponse ()
	{
		m_arrPurchasePaymentData = new ArrayList<PurchasePaymentData> ();
		m_nRowCount=0;
	}
}
