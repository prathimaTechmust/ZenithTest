package com.techmust.usermanagement.role;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;
import com.techmust.usermanagement.action.ActionData;

public class RoleResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<RoleData> m_arrRoleData;
	public ArrayList<ActionData> m_arrActionData;
	public long m_nRowCount;
	
	public RoleResponse ()
	{
		m_arrRoleData = new ArrayList<RoleData> ();
		m_arrActionData = new ArrayList<ActionData> ();
		m_nRowCount = 0;
	}
}
