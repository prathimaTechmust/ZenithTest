package com.techmust.inventory.purchase;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class PurchaseDataResponse extends GenericResponse
{
    private static final long serialVersionUID = 1L;
    public ArrayList<PurchaseData> m_arrPurchase ;
    public static long m_nRowCount;
    public PurchaseDataResponse ()
	{
    	m_arrPurchase = new ArrayList<PurchaseData> ();
    	m_nRowCount=0;
	}
}
