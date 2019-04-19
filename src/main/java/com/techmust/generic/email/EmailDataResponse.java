package com.techmust.generic.email;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class EmailDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<EmailMessageData> m_arrEmailMessage;
	public long m_nRowCount;
	
	public EmailDataResponse ()
	{
		m_arrEmailMessage = new ArrayList<EmailMessageData> ();
		m_nRowCount = 0;
	}
}
