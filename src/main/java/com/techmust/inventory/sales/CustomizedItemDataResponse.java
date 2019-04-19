package com.techmust.inventory.sales;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class CustomizedItemDataResponse extends GenericResponse 
{
	 private static final long serialVersionUID = 1L;
	 public ArrayList<CustomizedItemData> m_arrCustomizeItemData ;
	 
	 public CustomizedItemDataResponse ()
	 {
		 m_arrCustomizeItemData = new ArrayList<CustomizedItemData> ();
	 }
}
