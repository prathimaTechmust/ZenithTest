package com.techmust.inventory.sales;

import java.util.ArrayList;
import com.techmust.clientmanagement.ClientData;

public class ReportClientData 
{
	public ClientData m_oClientData;
	public ArrayList<SalesData> m_arrSalesData;
	
	ReportClientData ()
	{
		m_oClientData = new ClientData ();
		m_arrSalesData = new ArrayList<SalesData> ();
	}
}
