package com.techmust.generic.email;

import javax.mail.*;

public class PopupAuthenticator extends Authenticator
{
	String m_strLoginName;
	String m_strPassWord;
	public PopupAuthenticator (String strLogin, String strPassWord)
	{
		m_strLoginName = strLogin;
		m_strPassWord = strPassWord;
	}
	
	public PasswordAuthentication getPasswordAuthentication() 
	{
		return new PasswordAuthentication (m_strLoginName, m_strPassWord);
	}
	
}
