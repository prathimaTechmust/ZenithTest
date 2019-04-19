package com.techmust.usermanagement.action;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

/**
 * holds the response of ActionData
 * @author Techmust software pvt ltd
 *
 */
public class ActionResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Holds the array of ActionData
	 */
	public ArrayList<ActionData> m_arrActionData;

	public long m_nRowCount;
	
	/**
	 * Default Constuctor
	 */
	public ActionResponse () 
	{
		m_arrActionData = new ArrayList<ActionData> ();
		m_nRowCount = 0;
	}
}
