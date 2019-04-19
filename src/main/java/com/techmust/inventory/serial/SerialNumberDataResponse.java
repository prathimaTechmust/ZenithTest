package com.techmust.inventory.serial;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class SerialNumberDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList <SerialNumberData> m_arrSerialNumber;
	public SerialNumberDataResponse ()
	{
		m_arrSerialNumber = new ArrayList <SerialNumberData> ();
	}
}
