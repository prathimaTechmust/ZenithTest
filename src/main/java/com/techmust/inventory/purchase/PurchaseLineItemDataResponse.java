package com.techmust.inventory.purchase;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class PurchaseLineItemDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<PurchaseLineItem> m_arrPurchaseLineItem ;
	
	public PurchaseLineItemDataResponse ()
	{
		
	}
}
