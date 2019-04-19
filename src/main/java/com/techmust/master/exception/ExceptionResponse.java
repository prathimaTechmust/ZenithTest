package com.techmust.master.exception;


public class ExceptionResponse
{
	private int m_nStatus;
	private String m_strMessage;
	private long m_lTimeStamp;
	
	
	public ExceptionResponse()
	{
		
	}	

	public ExceptionResponse(int nStatus, String strMessage, long lTimeStamp)
	{
		this.m_nStatus = nStatus;
		this.m_strMessage = strMessage;
		this.m_lTimeStamp = lTimeStamp;
	}

	public int getStatus()
	{
		return m_nStatus;
	}

	public void setStatus(int nStatus)
	{
		this.m_nStatus = nStatus;
	}

	public String getMessage()
	{
		return m_strMessage;
	}

	public void setMessage(String strMessage) 
	{
		this.m_strMessage = strMessage;
	}

	public long getTimeStamp()
	{
		return m_lTimeStamp;
	}

	public void setTimeStamp(long lTimeStamp)
	{
		this.m_lTimeStamp = lTimeStamp;
	}	
}
