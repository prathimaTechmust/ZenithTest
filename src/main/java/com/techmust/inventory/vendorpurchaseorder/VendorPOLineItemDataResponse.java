package com.techmust.inventory.vendorpurchaseorder;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class VendorPOLineItemDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<VendorPOLineItemData> m_arrVendorPOLineItemData ;
	public VendorPOLineItemDataResponse ()
	{
		m_arrVendorPOLineItemData = new ArrayList<VendorPOLineItemData>();
	}
}
