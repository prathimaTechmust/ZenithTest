package com.techmust.usermanagement.actionarea;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class ActionAreaResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<ActionAreaData> m_arrActionArea;
	public long m_nRowCount;
	
	public ActionAreaResponse ()
	{
		m_arrActionArea = new ArrayList<ActionAreaData> ();
		m_nRowCount = 0;
	}
	
}
