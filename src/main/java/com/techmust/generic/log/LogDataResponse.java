package com.techmust.generic.log;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class LogDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<LogData> m_arrLogData;
	public String m_strXMLData;
	public long m_nRowCount;
	
	public LogDataResponse ()
	{
		 m_arrLogData = new ArrayList<LogData> ();
		 m_strXMLData = "";
		 m_nRowCount=0;
	}
}
