package com.techmust.scholarshipmanagement.activitylog;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;


public class ActivityLogResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<ActivityLog> m_arrActivityLog ;
	public Object m_nRowCount;
    public ActivityLogResponse ()
	{
    	m_arrActivityLog = new ArrayList<ActivityLog> ();
    	m_nRowCount = 0;
	}
}
