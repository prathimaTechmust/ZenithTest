package com.techmust.property.propertytype;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class PropertyTypeDataResponse extends GenericResponse 
{
	public ArrayList<PropertyTypeData> m_arrPropertyTypeData;
	public long m_nRowCount;
	private static final long serialVersionUID = 1L;
	
	public PropertyTypeDataResponse ()
	{
		m_arrPropertyTypeData = new ArrayList<PropertyTypeData> ();
		m_nRowCount = 0;
	}
}
