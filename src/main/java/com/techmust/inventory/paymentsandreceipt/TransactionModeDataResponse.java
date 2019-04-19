package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class TransactionModeDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<TransactionMode> m_arrTransactionMode;
	
	public TransactionModeDataResponse ()
	{
		m_arrTransactionMode = new ArrayList<TransactionMode> ();
	}

}
