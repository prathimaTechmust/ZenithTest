package com.techmust.property;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class PropertyDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<PropertyData> m_arrProperty ;
	public String m_strXMLData;
	public long m_nRowCount;
	 
	public PropertyDataResponse ()
	 {
		m_arrProperty = new ArrayList<PropertyData> (); 
    	m_strXMLData = "";
    	m_nRowCount = 0;
	 }
}
