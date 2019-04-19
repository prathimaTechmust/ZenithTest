package com.techmust.inventory.purchase;

import java.util.ArrayList;

import com.techmust.vendormanagement.VendorData;

public class ReportVendorData 
{
	public VendorData m_oVendorData;
	public ArrayList<PurchaseData> m_arrPurchaseData;
	
	ReportVendorData ()
	{
		m_oVendorData = new VendorData ();
		m_arrPurchaseData = new ArrayList<PurchaseData> ();
	}
}
