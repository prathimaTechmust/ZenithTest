package com.techmust.inventory.invoice;

import java.util.ArrayList;

public class TaxDiscountInfo 
{
	public float m_nPercentage;
	public float m_nAmount;
	public int m_nSerialNumber;
	public ArrayList<Integer> m_arrSerials;
	
	public TaxDiscountInfo ()
	{
		m_nPercentage = 0;
		m_nAmount = 0;
		m_arrSerials = new ArrayList<Integer> ();
	}
}
