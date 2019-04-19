package com.techmust.inventory.items;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.techmust.generic.response.GenericResponse;

public class ItemDataResponse extends GenericResponse
{
    private static final long serialVersionUID = 1L;
    
    Logger m_oLogger = Logger.getLogger (ItemDataResponse.class);
    
    public ArrayList<ItemData> m_arrItems ;
    public ArrayList<VendorItemData> m_arrVendorItems ;
    public long m_nRowCount;
    public ItemDataResponse ()
	{
    	m_arrItems = new ArrayList<ItemData> ();
    	m_arrVendorItems = new ArrayList<VendorItemData> ();
    	m_nRowCount=0;
	}
}
