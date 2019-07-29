package com.techmust.scholarshipmanagement.transaction;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;


public class TransactionResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<Transaction> m_arrTransaction ;
	public Object m_nRowCount;
    public TransactionResponse ()
	{
    	m_arrTransaction = new ArrayList<Transaction> ();
    	m_nRowCount = 0;
	}

}
