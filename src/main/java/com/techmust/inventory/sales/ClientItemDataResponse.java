package com.techmust.inventory.sales;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class ClientItemDataResponse extends GenericResponse 
{
	 private static final long serialVersionUID = 1L;
	 public ArrayList<ClientItemData> m_arrClientItemData ;
	 
	 public ClientItemDataResponse ()
	 {
		 m_arrClientItemData = new ArrayList<ClientItemData> ();
	 }
}
