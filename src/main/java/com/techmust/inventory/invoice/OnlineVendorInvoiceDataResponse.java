package com.techmust.inventory.invoice;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class OnlineVendorInvoiceDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<OnlineVendorInvoiceData> m_arrOnlineVendorInvoice ;
	public long m_nRowCount;
	public String m_strXMLData;
	public OnlineVendorInvoiceDataResponse ()
	{
		m_arrOnlineVendorInvoice = new ArrayList<OnlineVendorInvoiceData> ();
		m_nRowCount = 0;
		m_strXMLData = "";
	}
}
