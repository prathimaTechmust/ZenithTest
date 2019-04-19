package com.techmust.inventory.items;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class ItemGroupDataResponse extends GenericResponse 
{
	 private static final long serialVersionUID = 1L;
	    public ArrayList<ItemGroupData> m_arrItemGroupData ;
	    public long m_nRowCount;
	    public ItemGroupDataResponse ()
		{
	    	m_arrItemGroupData = new ArrayList<ItemGroupData> ();
	    	m_nRowCount=0;
		}
}
