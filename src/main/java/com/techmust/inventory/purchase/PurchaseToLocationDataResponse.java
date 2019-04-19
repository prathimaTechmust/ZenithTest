package com.techmust.inventory.purchase;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class PurchaseToLocationDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
    public ArrayList<PurchaseToLocationData> m_arrPurchaseToLocationData ;
    public PurchaseToLocationDataResponse ()
	{
    	m_arrPurchaseToLocationData = new ArrayList<PurchaseToLocationData> ();
	}
}
