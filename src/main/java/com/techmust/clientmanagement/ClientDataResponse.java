package com.techmust.clientmanagement;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;
import com.techmust.master.businesstype.BusinessTypeData;

public class ClientDataResponse extends GenericResponse 
{
	public ArrayList<ClientData> m_arrClientData;
	public ArrayList<BusinessTypeData> m_arrBusinessType;
	public long m_nRowCount;
	public String m_strMenuHTML;
	public String m_strOnlineMenuHTML;
	
	private static final long serialVersionUID = 1L;
	
	public ClientDataResponse ()
	{
		m_arrClientData = new ArrayList<ClientData> ();
		m_arrBusinessType = new ArrayList<BusinessTypeData> ();
		m_nRowCount = 0;
		m_strMenuHTML = "";
		m_strOnlineMenuHTML ="";
	}
}