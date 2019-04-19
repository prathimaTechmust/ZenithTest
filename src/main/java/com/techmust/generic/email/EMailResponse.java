package com.techmust.generic.email;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class EMailResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<MessageData> m_arrMessageData;
	
	public EMailResponse ()
	{
		m_arrMessageData = new ArrayList<MessageData> ();
	}

}
