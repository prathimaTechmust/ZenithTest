package com.techmust.inventory.purchaseorder;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class PurchaseOrderResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList <PurchaseOrderData> m_arrPurchaseOrder;
	public ArrayList <PurchaseOrderStockLineItemData> m_arrPOStockLineItemData;
	public long m_nRowCount;
	public PurchaseOrderResponse ()
	{
		m_arrPurchaseOrder = new ArrayList <PurchaseOrderData> ();
		m_arrPOStockLineItemData =  new ArrayList <PurchaseOrderStockLineItemData> ();
		m_nRowCount = 0;
	}
}
