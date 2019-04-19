package com.techmust.inventory.items;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class ItemCategoryDataResponse extends GenericResponse 
{
	public ArrayList<ItemCategoryData> m_arrItemCategory;
	public long m_nRowCount;
	private static final long serialVersionUID = 1L;
	
	public ItemCategoryDataResponse ()
	{
		m_arrItemCategory = new ArrayList<ItemCategoryData> ();
		m_nRowCount = 0;
	}
}
