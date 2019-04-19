package com.techmust.inventory.vatreport;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class VatReportDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<VatReportData> m_arrVatReportData;
    public String m_strXMLData;
	public VatReportDataResponse ()
	{
		 m_arrVatReportData = new ArrayList<VatReportData> ();
		 m_strXMLData = "";
	}
}
