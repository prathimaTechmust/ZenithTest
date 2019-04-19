package com.techmust.generic.email;

public class EMailSettingsData 
{
	private String m_strEmailAddress;
	private String m_strPassword;
	private String m_strReceiveEmailHost;
	private String m_strReceiveEmailPort;
	private String m_strReceiveEmailProtocol;
	private String m_strSendEmailHost;
	private String m_strSendEmailPort;
	private String m_strSendEmailProtocol;
	
	public  EMailSettingsData()
	{
	}
	public void setM_strEmailUserAddress(String strEmailAddress)
	{
		this.m_strEmailAddress = strEmailAddress;
	}
	public String getM_strEmailUserAddress()
	{
		return m_strEmailAddress;
	}
	public void setM_strPassword(String strPassword)
	{
		this.m_strPassword = strPassword;
	}
	public String getM_strPassword() 
	{
		return m_strPassword;
	}
	public void setM_strReceiveEmailHost(String strEmailHost) 
	{
		this.m_strReceiveEmailHost = strEmailHost;
	}
	public String getM_strReceiveEmailHost()
	{
		return m_strReceiveEmailHost;
	}
	public void setM_strReceiveEmailPort(String strEmailPort)
	{
		this.m_strReceiveEmailPort = strEmailPort;
	}
	public String getM_strReceiveEmailPort() 
	{
		return m_strReceiveEmailPort;
	}
	public void setM_strReceiveEmailProtocol(String strEmailProtocol) 
	{
		this.m_strReceiveEmailProtocol = strEmailProtocol;
	}
	public String getM_strReceiveEmailProtocol() 
	{
		return m_strReceiveEmailProtocol;
	}

	public void setM_strSendEmailHost(String strSendEmailHost)
	{
		this.m_strSendEmailHost = strSendEmailHost;
	}

	public String getM_strSendEmailHost() 
	{
		return m_strSendEmailHost;
	}

	public void setM_strSendEmailPort(String strSendEmailPort)
	{
		this.m_strSendEmailPort = strSendEmailPort;
	}

	public String getM_strSendEmailPort()
	{
		return m_strSendEmailPort;
	}

	public void setM_strSendEmailProtocol(String strSendEmailProtocol) 
	{
		this.m_strSendEmailProtocol = strSendEmailProtocol;
	}

	public String getM_strSendEmailProtocol() 
	{
		return m_strSendEmailProtocol;
	}
}
