package com.techmust.inventory.utility;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class TallyTransformKeyDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<TallyTransformKeyData> m_arrTallyTransformKeyData;
	public long m_nRowCount;
	
	public TallyTransformKeyDataResponse ()
	{
		m_arrTallyTransformKeyData = new ArrayList<TallyTransformKeyData> ();
	}
}
