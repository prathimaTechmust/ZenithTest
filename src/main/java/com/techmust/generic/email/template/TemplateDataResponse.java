package com.techmust.generic.email.template;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class TemplateDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	public ArrayList<TemplateData> m_arrTemplates ;
	public String m_strXMLData;
	public long m_nRowCount;
	
	public TemplateDataResponse ()
	{
		m_arrTemplates = new ArrayList<TemplateData> ();
		m_nRowCount=0;
	}

}
