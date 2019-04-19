package com.techmust.clientmanagement;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class ClientGroupDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
    public ArrayList<ClientGroupData> m_arrGroupData ;
    public ArrayList<ClientData> m_arrClientData ;
	public long m_nRowCount;
    public ClientGroupDataResponse ()
	{
    	m_arrGroupData = new ArrayList<ClientGroupData> ();
    	m_arrClientData = new ArrayList<ClientData> ();
    	m_nRowCount = 0;
	}
}