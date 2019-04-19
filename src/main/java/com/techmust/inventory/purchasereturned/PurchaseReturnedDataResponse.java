package com.techmust.inventory.purchasereturned;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class PurchaseReturnedDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<PurchaseReturnedData> m_arrPurchaseReturnedData ;
	public String m_strXMLData;
	public long m_nRowCount;
	 
	public PurchaseReturnedDataResponse() 
	{
		m_arrPurchaseReturnedData = new ArrayList<PurchaseReturnedData> ();
		m_strXMLData = "";
		m_nRowCount = 0;
	}
}
