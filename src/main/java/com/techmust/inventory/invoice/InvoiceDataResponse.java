package com.techmust.inventory.invoice;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class InvoiceDataResponse extends GenericResponse
{
    private static final long serialVersionUID = 1L;
    public ArrayList<InvoiceData> m_arrInvoice ;
    public String m_strXMLData;
    public long m_nRowCount;
	 public InvoiceDataResponse ()
	 {
		 m_arrInvoice = new ArrayList<InvoiceData> ();
		 m_strXMLData = "";
		 m_nRowCount = 0;
	 }
}
