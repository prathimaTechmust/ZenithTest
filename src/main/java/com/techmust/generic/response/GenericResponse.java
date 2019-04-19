package com.techmust.generic.response;

import java.io.Serializable;

public abstract class GenericResponse extends Object implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String m_strError_Desc;
	public boolean m_bSuccess;
	public int m_nErrorID;
	
	public GenericResponse ()
	{
		m_strError_Desc = "";
		m_bSuccess = false;
		m_nErrorID = -1;
	}
}
