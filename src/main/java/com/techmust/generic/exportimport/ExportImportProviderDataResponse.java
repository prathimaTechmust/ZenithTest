package com.techmust.generic.exportimport;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class ExportImportProviderDataResponse extends GenericResponse 
{

	private static final long serialVersionUID = 1L;
	public ArrayList<ExportImportProviderData> m_arrExportImportProvider ;
    public long m_nRowCount;
    public ExportImportProviderDataResponse ()
	{
    	m_arrExportImportProvider = new ArrayList<ExportImportProviderData> ();
    	m_nRowCount=0;
	}
}
