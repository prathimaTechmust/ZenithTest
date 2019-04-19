package com.techmust.inventory.stocktransfer;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class StockTransferMemoDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
    public ArrayList<StockTransferMemoData> m_arrStockTransferMemo ;
    public long m_nRowCount;
    
    public StockTransferMemoDataResponse ()
	{
    	m_arrStockTransferMemo = new ArrayList<StockTransferMemoData> ();
    	m_nRowCount = 0;
	}
}
