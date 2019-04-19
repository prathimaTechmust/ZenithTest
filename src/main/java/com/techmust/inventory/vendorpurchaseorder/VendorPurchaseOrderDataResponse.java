package com.techmust.inventory.vendorpurchaseorder;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class VendorPurchaseOrderDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList <VendorPurchaseOrderData> m_arrVendorPurchaseOrderData;
	public long m_nRowCount;
	public String m_strXMLData;
	
	public VendorPurchaseOrderDataResponse ()
	{
		m_arrVendorPurchaseOrderData = new ArrayList <VendorPurchaseOrderData> ();
		m_strXMLData = "";
		m_nRowCount = 0;
	}
}
