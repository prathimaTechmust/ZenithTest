package com.techmust.inventory.sales;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.purchaseorder.PurchaseOrderLineItemData;

public class NonStockSalesLineItemResponse extends GenericResponse 
{
	 private static final long serialVersionUID = 1L;
	 public ArrayList<NonStockSalesLineItemData> m_arrNonStockSalesLineItems ;
	 public ArrayList<PurchaseOrderLineItemData> m_arrPurchaseOrderLineItems ;
	 public long m_nRowCount;
	 
	 public NonStockSalesLineItemResponse ()
	 {
		 m_nRowCount = 0;
		 m_arrNonStockSalesLineItems = new ArrayList<NonStockSalesLineItemData> ();
	 }
}
