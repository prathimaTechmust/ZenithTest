package com.techmust.inventory.items;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class StockMovementDataResponse extends GenericResponse
{
    private static final long serialVersionUID = 1L;
    public ArrayList<StockMovementData> m_arrStockMovementData;
    public StockMovementDataResponse ()
    {
    	m_arrStockMovementData = new ArrayList<StockMovementData>();
    }
}
