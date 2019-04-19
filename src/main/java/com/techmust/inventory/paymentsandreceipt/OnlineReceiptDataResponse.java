package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class OnlineReceiptDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<OnlineReceiptData> m_arrOnlineReceipts ;
	public long m_nRowCount;
	public String m_strXMLData;
	public OnlineReceiptDataResponse ()
	{
		m_arrOnlineReceipts = new ArrayList<OnlineReceiptData> ();
		m_nRowCount = 0;
		m_strXMLData = "";
	}

}
