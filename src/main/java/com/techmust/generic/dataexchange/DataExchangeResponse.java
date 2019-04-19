package com.techmust.generic.dataexchange;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class DataExchangeResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public int m_nDuplicateCount ;
	public int m_nInsertedCount ;
	public ArrayList<String> m_arrException;
	
	public DataExchangeResponse ()
	{
		m_nDuplicateCount = 0;
		m_nInsertedCount = 0;
		m_arrException = new  ArrayList<String>();
	}

}
