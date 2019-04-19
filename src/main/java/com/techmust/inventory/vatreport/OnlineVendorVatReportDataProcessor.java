package com.techmust.inventory.vatreport;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.invoice.OnlineVendorInvoiceData;
import com.techmust.traderp.util.TraderpUtil;
import com.techmust.vendormanagement.VendorData;

public class OnlineVendorVatReportDataProcessor extends VatReportDataProcessor 
{
	public GenericResponse generateVendroVatReportXML (int nMonth, int nYear,OnlineVendorInvoiceData oData) throws Exception
	{
		VatReportDataResponse oVatReportDataResponse = new VatReportDataResponse ();
		VatReportData oVatReportData = new VatReportData ();
		try 
		{
			buildVendroSalesVatReport (oVatReportData, nMonth, nYear,oData);
			//buildPurchaseVatReport (oVatReportData, nMonth, nYear);
			oVatReportDataResponse.m_strXMLData = oVatReportData.generateXML();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("generateVatReportXML - oException : " + oException);
		}
		return oVatReportDataResponse;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<OnlineVendorInvoiceData> getVendorInvoicesForPeriod(int nMonth, int nYear,OnlineVendorInvoiceData oData) throws Exception 
	{
		oData.getM_oInvoiceData().setM_strFromDate(TraderpUtil.getFirstDayOfMonth(nMonth, nYear));
		oData.getM_oInvoiceData().setM_strToDate(TraderpUtil.getLastDayOfMonth(nMonth, nYear));
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		return new ArrayList(oData.list(oOrderBy));
	}

	private void buildVendroSalesVatReport(VatReportData oVatReportData,int nMonth, int nYear,OnlineVendorInvoiceData oData) throws Exception
	{
		ArrayList<OnlineVendorInvoiceData> arrVendorInvoices =getVendorInvoicesForPeriod(nMonth,nYear,oData);
		/*for(int nIndex = 0; nIndex < arrVendorInvoices.size(); nIndex++)
			buildSalesReport (oVatReportData, arrVendorInvoices.get(nIndex).getM_oInvoiceData());*/
	}
	
	
}
