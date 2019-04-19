package com.techmust.inventory.purchaseorder;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class PurchaseOrderLineItemDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<PurchaseOrderLineItemData> m_arrPurchaseOrderLineItem ;
	public PurchaseOrderLineItemDataResponse ()
	{
		m_arrPurchaseOrderLineItem = new ArrayList<PurchaseOrderLineItemData>();
	}
}
