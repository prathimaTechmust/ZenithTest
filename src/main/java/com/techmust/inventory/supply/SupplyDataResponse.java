package com.techmust.inventory.supply;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.purchase.PurchaseData;

public class SupplyDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList <PurchaseData> m_arrPurchase;
	public SupplyDataResponse ()
	{
		m_arrPurchase = new ArrayList <PurchaseData> ();
	}
}
