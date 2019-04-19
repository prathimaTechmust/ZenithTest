package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class PaymentDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
    public ArrayList<PaymentData> m_arrPaymentData ;
    public long m_nRowCount;
    public String m_strXMLData;
    public PaymentDataResponse ()
	{
    	m_arrPaymentData = new ArrayList<PaymentData> ();
    	m_strXMLData = "";
    	m_nRowCount=0;
	}
}
