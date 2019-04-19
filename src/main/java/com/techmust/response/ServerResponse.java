package com.techmust.response;

public class ServerResponse
{
	private boolean m_bIsSuccess = false;
	private String  m_strResponseMessage = "";
	
	public boolean isM_bIsSuccess() 
	{
		return m_bIsSuccess;
	}
	
	public void setM_bIsSuccess(boolean bIsSuccess) 
	{
		this.m_bIsSuccess = bIsSuccess;
	}
	
	public String getM_strResponseMessage()
	{
		return m_strResponseMessage;
	}
	
	public void setM_strResponseMessage(String strResponseMessage) 
	{
		this.m_strResponseMessage = strResponseMessage;
	}
}