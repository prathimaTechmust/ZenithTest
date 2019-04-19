package com.techmust.clientmanagement;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class ContactDataResponse extends GenericResponse 
{
	public ArrayList<ContactData> m_arrContactData;
	private static final long serialVersionUID = 1L;
	
	public ContactDataResponse ()
	{
		m_arrContactData = new ArrayList<ContactData> ();
	}
}