package com.techmust.usermanagement.userinfo;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class UserInformationResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<UserInformationData> m_arrUserInformationData;
	public Object m_nRowCount;
	
	public UserInformationResponse ()
	{
		m_arrUserInformationData = new ArrayList<UserInformationData> ();
		m_nRowCount = 0;
	}
}
