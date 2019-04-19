package com.techmust.inventory.sales;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class SalesLineItemDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<SalesLineItemData> m_arrSalesLineItem ;
	public SalesLineItemDataResponse ()
	{
		m_arrSalesLineItem = new ArrayList<SalesLineItemData>();
	}
	
}
