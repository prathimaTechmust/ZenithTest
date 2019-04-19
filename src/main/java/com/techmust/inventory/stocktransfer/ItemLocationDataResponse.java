package com.techmust.inventory.stocktransfer;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class ItemLocationDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<ItemLocationData> m_arrItemLocation ;
	
    public ItemLocationDataResponse ()
	{
    	m_arrItemLocation = new ArrayList<ItemLocationData> ();
	}    
}
