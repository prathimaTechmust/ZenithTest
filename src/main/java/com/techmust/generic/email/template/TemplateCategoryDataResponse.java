package com.techmust.generic.email.template;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class TemplateCategoryDataResponse extends GenericResponse
{
	private static final long serialVersionUID = 1L;
	public ArrayList<TemplateCategoryData> m_arrTemplateCategories ;
	public long m_nRowCount;
	
	public TemplateCategoryDataResponse ()
	{
		m_arrTemplateCategories = new ArrayList<TemplateCategoryData> ();
		m_nRowCount = 0;
	}
}
