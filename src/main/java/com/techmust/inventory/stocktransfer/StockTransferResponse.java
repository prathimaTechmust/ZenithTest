package com.techmust.inventory.stocktransfer;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class StockTransferResponse extends GenericResponse {

    private static final long serialVersionUID = 1L;
    public ArrayList<StockTransferData> m_arrStockTransfer ;
    public long m_nRowCount;
    public String strStockTransferXML;
    public StockTransferResponse ()
	{
    	m_arrStockTransfer = new ArrayList<StockTransferData> ();
    	m_nRowCount = 0;
    	strStockTransferXML = "";
	}

}
