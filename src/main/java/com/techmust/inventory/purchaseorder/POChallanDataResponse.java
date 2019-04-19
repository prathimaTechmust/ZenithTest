package com.techmust.inventory.purchaseorder;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.challan.ChallanData;

public class POChallanDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList <ChallanData> m_arrChallanData;
	public POChallanDataResponse ()
	{
		m_arrChallanData = new ArrayList <ChallanData> ();
	}
}
