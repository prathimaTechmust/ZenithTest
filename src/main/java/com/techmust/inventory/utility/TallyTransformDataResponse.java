package com.techmust.inventory.utility;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class TallyTransformDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<TallyTransformData> m_arrTallyTransformData;
	public long m_nRowCount;
	
	public TallyTransformDataResponse ()
	{
		m_arrTallyTransformData = new ArrayList<TallyTransformData> ();
	}

}
