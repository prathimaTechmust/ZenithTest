package com.techmust.inventory.vatreport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.techmust.traderp.util.TraderpUtil;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.invoice.OnlineVendorInvoiceData;
import com.techmust.inventory.invoice.OnlineVendorInvoiceDataProcessor;
import com.techmust.inventory.invoice.OnlineVendorInvoiceDataResponse;
import com.techmust.inventory.paymentsandreceipt.OnlineReceiptDataProcessor;
import com.techmust.inventory.purchase.NonStockPurchaseLineItem;
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.inventory.purchase.PurchaseLineItem;
import com.techmust.inventory.purchase.PurchaseType;
import com.techmust.inventory.sales.NonStockSalesLineItemData;
import com.techmust.inventory.sales.SalesData;
import com.techmust.inventory.sales.SalesLineItemData;

public class VatReportDataProcessor extends GenericIDataProcessor<VatReportData> 
{

	@Override
	public GenericResponse create(VatReportData oData) throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse deleteData(VatReportData oData) throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse get(VatReportData oData) throws Exception 
	{
		return null;
	}

	@Override
	public String getXML(VatReportData oData) throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse list(VatReportData oData, HashMap<String, String> arrOrderBy)throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse update(VatReportData oData) throws Exception 
	{
		return null;
	}
	
	public GenericResponse generateVatReportXML (int nMonth, int nYear)
	{
		VatReportDataResponse oVatReportDataResponse = new VatReportDataResponse ();
		VatReportData oVatReportData = new VatReportData ();
		try 
		{
			buildSalesVatReport (oVatReportData, nMonth, nYear);
			buildPurchaseVatReport (oVatReportData, nMonth, nYear);
			oVatReportDataResponse.m_strXMLData = oVatReportData.generateXML();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("generateVatReportXML - oException : " + oException);
		}
		return oVatReportDataResponse;
	}

	private void buildSalesVatReport(VatReportData oVatReportData, int nMonth, int nYear) throws Exception 
	{
		ArrayList<InvoiceData> arrInvoices = getInvoicesForPeriod(nMonth, nYear);
		for(int nIndex = 0; nIndex < arrInvoices.size(); nIndex++)
			buildSalesReport (oVatReportData, arrInvoices.get(nIndex));
	}

	private void buildSalesReport(VatReportData oVatReportData, InvoiceData oIvoiceData) 
	{
		
		Set<SalesData> oSalesSet = oIvoiceData.getM_oSalesSet();
		Iterator<SalesData> oIterator = oSalesSet.iterator();
		while (oIterator.hasNext()) 
		{
			SalesData oSalesData = (SalesData) oIterator.next();
			buildLineItemReport(oVatReportData, oSalesData);
		}
	}

	private void buildLineItemReport(VatReportData oVatReportData,SalesData oSalesData) 
	{
		buildSalesStockLineItem(oVatReportData, oSalesData);
		buildsalesNonStockLineItem(oVatReportData, oSalesData);
	}
	
	private void buildSalesStockLineItem(VatReportData oVatReportData, SalesData oSalesData) 
	{
		Set<SalesLineItemData> oSalesLineItems = oSalesData.getM_oSalesLineItems();
		Iterator<SalesLineItemData> oIterator = oSalesLineItems.iterator();
		while (oIterator.hasNext()) 
		{
			SalesLineItemData oSalesLineItemData = (SalesLineItemData) oIterator.next();
			if (oSalesData.getM_oClientData().isM_bOutstationClient ())
				buildInterStateSalesArray (oVatReportData, oSalesLineItemData, oSalesData.isM_bIsCFormProvided ());
			else
				buildLocalSalesArray (oVatReportData, oSalesLineItemData);
		}
	}

	private void buildInterStateSalesArray(VatReportData oVatReportData, SalesLineItemData oSalesLineItemData, boolean bIsCFormProvided) 
	{
		VatInterStateSalesReport oInterStateSalesReport = new VatInterStateSalesReport ();
		oInterStateSalesReport.setM_nTax(oSalesLineItemData.getM_nTax());
		oInterStateSalesReport.setM_nTaxableAmount(getTaxableAmount(oSalesLineItemData.getM_nPrice(), oSalesLineItemData.getM_nDiscount(), oSalesLineItemData.getM_nQuantity()));
		oInterStateSalesReport.setM_nTaxAmount(getTaxAmount(oInterStateSalesReport.getM_nTaxableAmount(), oSalesLineItemData.getM_nTax()));
		oInterStateSalesReport.setM_bIsCFormProvided(bIsCFormProvided);
		if (isInterStateInstanceFound(oVatReportData.getM_arrVatInterStateSalesData(), oSalesLineItemData.getM_nTax(), bIsCFormProvided))
			updateToInterStateSalesArray (oVatReportData, oInterStateSalesReport);
		else if (oInterStateSalesReport.getM_nTax() > 0)
			oVatReportData.getM_arrVatInterStateSalesData().add(oInterStateSalesReport);
	}

	private void updateToInterStateSalesArray(VatReportData oVatReportData, VatInterStateSalesReport oInterStateSalesReport) 
	{
		for(int nIndex = 0; nIndex < oVatReportData.getM_arrVatInterStateSalesData().size(); nIndex++)
		{
			if(oVatReportData.getM_arrVatInterStateSalesData().get(nIndex).getM_nTax() == oInterStateSalesReport.getM_nTax() && oVatReportData.getM_arrVatInterStateSalesData().get(nIndex).isM_bIsCFormProvided() == oInterStateSalesReport.isM_bIsCFormProvided())
			{
				oVatReportData.getM_arrVatInterStateSalesData().get(nIndex).setM_nTaxableAmount((oVatReportData.getM_arrVatInterStateSalesData().get(nIndex).getM_nTaxableAmount() + oInterStateSalesReport.getM_nTaxableAmount()));
				oVatReportData.getM_arrVatInterStateSalesData().get(nIndex).setM_nTaxAmount((oVatReportData.getM_arrVatInterStateSalesData().get(nIndex).getM_nTaxAmount() + oInterStateSalesReport.getM_nTaxAmount()));
				break;
			}
		}
	}

	private boolean isInterStateInstanceFound(ArrayList<VatInterStateSalesReport> arrVatInterStateSalesData, float nTaxPercent, boolean bIsCFormProvided) 
	{
		boolean bisInstanceFound = false;
		for (int nIndex = 0; nIndex < arrVatInterStateSalesData.size(); nIndex++)
		{
			if(arrVatInterStateSalesData.get(nIndex).getM_nTax() == nTaxPercent && arrVatInterStateSalesData.get(nIndex).isM_bIsCFormProvided() == bIsCFormProvided)
			{
				bisInstanceFound = true;
				break;
			}
		}
		return bisInstanceFound;
	}

	private void buildLocalSalesArray(VatReportData oVatReportData, SalesLineItemData oSalesLineItemData)
	{
		VatLocalSalesReportData oLocalSalesReportData = new VatLocalSalesReportData ();
		oLocalSalesReportData.setM_nTax(oSalesLineItemData.getM_nTax());
		oLocalSalesReportData.setM_nTaxableAmount(getTaxableAmount(oSalesLineItemData.getM_nPrice(), oSalesLineItemData.getM_nDiscount(), oSalesLineItemData.getM_nQuantity()));
		oLocalSalesReportData.setM_nTaxAmount(getTaxAmount(oLocalSalesReportData.getM_nTaxableAmount(), oSalesLineItemData.getM_nTax()));
		if (isLocalSaleInstanceFound(oVatReportData.getM_arrVatLocalSalesData(), oSalesLineItemData.getM_nTax()))
			updateToLocalSalesArray (oVatReportData, oLocalSalesReportData);
		else if (oLocalSalesReportData.getM_nTax() > 0)
			oVatReportData.getM_arrVatLocalSalesData().add(oLocalSalesReportData);
	}
	
	private void updateToLocalSalesArray(VatReportData oVatReportData, VatLocalSalesReportData oLocalSalesReportData) 
	{
		for(int nIndex = 0; nIndex < oVatReportData.getM_arrVatLocalSalesData().size(); nIndex++)
		{
			if(oVatReportData.getM_arrVatLocalSalesData().get(nIndex).getM_nTax() == oLocalSalesReportData.getM_nTax())
			{
				oVatReportData.getM_arrVatLocalSalesData().get(nIndex).setM_nTaxableAmount((oVatReportData.getM_arrVatLocalSalesData().get(nIndex).getM_nTaxableAmount() + oLocalSalesReportData.getM_nTaxableAmount()));
				oVatReportData.getM_arrVatLocalSalesData().get(nIndex).setM_nTaxAmount((oVatReportData.getM_arrVatLocalSalesData().get(nIndex).getM_nTaxAmount() + oLocalSalesReportData.getM_nTaxAmount()));
				break;
			}
		}
	}
	
	private void buildsalesNonStockLineItem(VatReportData oVatReportData, SalesData oSalesData) 
	{
		Set<NonStockSalesLineItemData> oNonStockSalesLineItems = oSalesData.getM_oNonStockSalesLineItems();
		Iterator<NonStockSalesLineItemData> oIterator = oNonStockSalesLineItems.iterator();
		while (oIterator.hasNext()) 
		{
			NonStockSalesLineItemData oNonStockSalesLineItemData = (NonStockSalesLineItemData) oIterator.next();
			if (oSalesData.getM_oClientData().isM_bOutstationClient ())
				buildInterStateSalesArray (oVatReportData, oNonStockSalesLineItemData, oSalesData.isM_bIsCFormProvided ());
			else
				buildLocalSalesArray (oVatReportData, oNonStockSalesLineItemData);
		}
	}
	
	private void buildInterStateSalesArray(VatReportData oVatReportData, NonStockSalesLineItemData oNonStockSalesLineItemData, boolean bIsCFormProvided) 
	{
		VatInterStateSalesReport oInterStateSalesReport = new VatInterStateSalesReport ();
		oInterStateSalesReport.setM_nTax(oNonStockSalesLineItemData.getM_nTax());
		oInterStateSalesReport.setM_nTaxableAmount(getTaxableAmount(oNonStockSalesLineItemData.getM_nPrice(), oNonStockSalesLineItemData.getM_nDiscount(), oNonStockSalesLineItemData.getM_nQuantity()));
		oInterStateSalesReport.setM_nTaxAmount(getTaxAmount(oInterStateSalesReport.getM_nTaxableAmount(), oNonStockSalesLineItemData.getM_nTax()));
		oInterStateSalesReport.setM_bIsCFormProvided(bIsCFormProvided);
		if (isInterStateInstanceFound(oVatReportData.getM_arrVatInterStateSalesData(), oNonStockSalesLineItemData.getM_nTax(), bIsCFormProvided))
			updateToInterStateSalesArray (oVatReportData, oInterStateSalesReport);
		else if (oInterStateSalesReport.getM_nTax() > 0)
			oVatReportData.getM_arrVatInterStateSalesData().add(oInterStateSalesReport);
	}
	
	private void buildLocalSalesArray(VatReportData oVatReportData, NonStockSalesLineItemData oNonStockSalesLineItemData)
	{
		VatLocalSalesReportData oLocalSalesReportData = new VatLocalSalesReportData ();
		oLocalSalesReportData.setM_nTax(oNonStockSalesLineItemData.getM_nTax());
		oLocalSalesReportData.setM_nTaxableAmount(getTaxableAmount(oNonStockSalesLineItemData.getM_nPrice(), oNonStockSalesLineItemData.getM_nDiscount(), oNonStockSalesLineItemData.getM_nQuantity()));
		oLocalSalesReportData.setM_nTaxAmount(getTaxAmount(oLocalSalesReportData.getM_nTaxableAmount(), oNonStockSalesLineItemData.getM_nTax()));
		if (isLocalSaleInstanceFound(oVatReportData.getM_arrVatLocalSalesData(), oNonStockSalesLineItemData.getM_nTax()))
			updateToLocalSalesArray (oVatReportData, oLocalSalesReportData);
		else if (oLocalSalesReportData.getM_nTax() > 0)
			oVatReportData.getM_arrVatLocalSalesData().add(oLocalSalesReportData);
	}
	
	private float getTaxableAmount(float nPrice, float nDiscPercent, float nQuantity) 
	{
		return (nPrice * ((100-nDiscPercent)/100)) * nQuantity;
	}
	
	private float getTaxAmount(float nTaxableAmount, float nTaxPercent) 
	{
		return nTaxableAmount * (nTaxPercent/100);
	}
	
	private boolean isLocalSaleInstanceFound(ArrayList<VatLocalSalesReportData> arrVatLocalSales, float nTaxPercent) 
	{
		boolean bisInstanceFound = false;
		for (int nIndex = 0; nIndex < arrVatLocalSales.size(); nIndex++)
		{
			if(arrVatLocalSales.get(nIndex).getM_nTax() == nTaxPercent)
			{
				bisInstanceFound = true;
				break;
			}
		}
		return bisInstanceFound;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<InvoiceData> getInvoicesForPeriod(int nMonth, int nYear) throws Exception
	{
		InvoiceData oInvoiceData = new InvoiceData ();
		oInvoiceData.setM_strFromDate(TraderpUtil.getFirstDayOfMonth(nMonth, nYear));
		oInvoiceData.setM_strToDate(TraderpUtil.getLastDayOfMonth(nMonth, nYear));
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		return new ArrayList (oInvoiceData.list (oOrderBy));
	}
	
	private void buildPurchaseVatReport(VatReportData oVatReportData, int nMonth, int nYear) throws Exception 
	{
		ArrayList<PurchaseData> arrPurchases = getPurchasesForPeriod(nMonth, nYear);
		for(int nIndex = 0; nIndex < arrPurchases.size(); nIndex++)
		{
			PurchaseData oPurchaseData = arrPurchases.get(nIndex);
			buildPurchaseStockLineItem(oVatReportData, oPurchaseData);
			buildPurchaseNonStockLineItem(oVatReportData, oPurchaseData);
		}
	}

	private void buildPurchaseStockLineItem(VatReportData oVatReportData, PurchaseData oPurchaseData) 
	{
		Set<PurchaseLineItem> oPurchaseLineItems = oPurchaseData.getM_oPurchaseLineItems();
		Iterator<PurchaseLineItem> oIterator = oPurchaseLineItems.iterator();
		while (oIterator.hasNext()) 
		{
			PurchaseLineItem oPurchaseLineItemData = (PurchaseLineItem) oIterator.next();
			buildVatPurchaseArray(oVatReportData, oPurchaseLineItemData);
		}
	}

	private void buildVatPurchaseArray(VatReportData oVatReportData, PurchaseLineItem oPurchaseLineItemData) 
	{
		VatPurchaseReportData oVatPurchaseReportData = new VatPurchaseReportData ();
		oVatPurchaseReportData.setM_nTax(oPurchaseLineItemData.getM_nTax());
		oVatPurchaseReportData.setM_nTaxableAmount(getTaxableAmount(oPurchaseLineItemData.getM_nPrice(), oPurchaseLineItemData.getM_nDiscount(), oPurchaseLineItemData.getM_nQuantity(), oPurchaseLineItemData.getM_nExcise(), oPurchaseLineItemData.getM_nOtherCharges()));
		oVatPurchaseReportData.setM_nTaxAmount(getTaxAmount(oVatPurchaseReportData.getM_nTaxableAmount(), oPurchaseLineItemData.getM_nTax()));
		if (isPurchaseInstanceFound(oVatReportData.getM_arrVatPurchaseData(), oPurchaseLineItemData.getM_nTax()))
			updateToVatPurchaseArray (oVatReportData, oVatPurchaseReportData);
		else if (oVatPurchaseReportData.getM_nTax() > 0)
			oVatReportData.getM_arrVatPurchaseData().add(oVatPurchaseReportData);
		
	}

	private void buildPurchaseNonStockLineItem(VatReportData oVatReportData, PurchaseData oPurchaseData) 
	{
		Set<NonStockPurchaseLineItem> oNonStockPurchaseLineItems = oPurchaseData.getM_oNonStockPurchaseLineItems();
		Iterator<NonStockPurchaseLineItem> oIterator = oNonStockPurchaseLineItems.iterator();
		while (oIterator.hasNext()) 
		{
			NonStockPurchaseLineItem oNonStockPurchaseLineItemData = (NonStockPurchaseLineItem) oIterator.next();
			buildVatPurchaseArray(oVatReportData, oNonStockPurchaseLineItemData);
		}
	}
	
	private void buildVatPurchaseArray(VatReportData oVatReportData, NonStockPurchaseLineItem oNonStockPurchaseLineItemData) 
	{
		VatPurchaseReportData oVatPurchaseReportData = new VatPurchaseReportData ();
		oVatPurchaseReportData.setM_nTax(oNonStockPurchaseLineItemData.getM_nTax());
		oVatPurchaseReportData.setM_nTaxableAmount(getTaxableAmount(oNonStockPurchaseLineItemData.getM_nPrice(), oNonStockPurchaseLineItemData.getM_nDiscount(), oNonStockPurchaseLineItemData.getM_nQuantity(), oNonStockPurchaseLineItemData.getM_nExcise(), oNonStockPurchaseLineItemData.getM_nOtherCharges()));
		oVatPurchaseReportData.setM_nTaxAmount(getTaxAmount(oVatPurchaseReportData.getM_nTaxableAmount(), oNonStockPurchaseLineItemData.getM_nTax()));
		if (isPurchaseInstanceFound(oVatReportData.getM_arrVatPurchaseData(), oNonStockPurchaseLineItemData.getM_nTax()))
			updateToVatPurchaseArray (oVatReportData, oVatPurchaseReportData);
		else if (oVatPurchaseReportData.getM_nTax() > 0)
			oVatReportData.getM_arrVatPurchaseData().add(oVatPurchaseReportData);
	}
	
	private boolean isPurchaseInstanceFound(ArrayList<VatPurchaseReportData> arrVatPurchaseData, float nTaxPercent) 
	{
		boolean bisInstanceFound = false;
		for (int nIndex = 0; nIndex < arrVatPurchaseData.size(); nIndex++)
		{
			if(arrVatPurchaseData.get(nIndex).getM_nTax() == nTaxPercent)
			{
				bisInstanceFound = true;
				break;
			}
		}
		return bisInstanceFound;
	}

	private void updateToVatPurchaseArray(VatReportData oVatReportData, VatPurchaseReportData oVatPurchaseReportData) 
	{
		for(int nIndex = 0; nIndex < oVatReportData.getM_arrVatPurchaseData().size(); nIndex++)
		{
			if(oVatReportData.getM_arrVatPurchaseData().get(nIndex).getM_nTax() == oVatPurchaseReportData.getM_nTax())
			{
				oVatReportData.getM_arrVatPurchaseData().get(nIndex).setM_nTaxableAmount((oVatReportData.getM_arrVatPurchaseData().get(nIndex).getM_nTaxableAmount() + oVatPurchaseReportData.getM_nTaxableAmount()));
				oVatReportData.getM_arrVatPurchaseData().get(nIndex).setM_nTaxAmount((oVatReportData.getM_arrVatPurchaseData().get(nIndex).getM_nTaxAmount() + oVatPurchaseReportData.getM_nTaxAmount()));
				break;
			}
		}
	}

	private float getTaxableAmount(float nPrice, float nDiscPercent, float nQuantity, float nExcisePercent, float nOtherCharges) 
	{
		float nAmount = 0;
		nAmount =  (nPrice * ((100-nDiscPercent)/100)) * nQuantity;
		nAmount += nAmount *(nExcisePercent/100);
		nAmount += nAmount *(nOtherCharges/100);
		return nAmount;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<PurchaseData> getPurchasesForPeriod(int nMonth, int nYear) throws Exception 
	{
		PurchaseData oPurchaseData = new PurchaseData ();
		oPurchaseData.setM_strFromDate(TraderpUtil.getFirstDayOfMonth(nMonth, nYear));
		oPurchaseData.setM_strToDate(TraderpUtil.getLastDayOfMonth(nMonth, nYear));
		oPurchaseData.setM_nPurchaseType(PurchaseType.kInvalid);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		return new ArrayList (oPurchaseData.list (oOrderBy));
	}
	
	
	public GenericResponse generateVendorVatReportXML (int nMonth, int nYear,OnlineVendorInvoiceData oData)
	{
		VatReportDataResponse oVatReportDataResponse = new VatReportDataResponse ();
		VatReportData oVatReportData = new VatReportData ();
		try 
		{
			buildSalesVatReport (oVatReportData, nMonth, nYear);
			buildPurchaseVatReport (oVatReportData, nMonth, nYear);
			oVatReportDataResponse.m_strXMLData = oVatReportData.generateXML();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("generateVatReportXML - oException : " + oException);
		}
		return oVatReportDataResponse;
	}
	
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
	private OnlineVendorInvoiceDataResponse getVendorInvoicesForPeriod(int nMonth, int nYear,OnlineVendorInvoiceData oData) throws Exception 
	{
		oData.getM_oInvoiceData().setM_strFromDate(TraderpUtil.getFirstDayOfMonth(nMonth, nYear));
		oData.getM_oInvoiceData().setM_strToDate(TraderpUtil.getLastDayOfMonth(nMonth, nYear));
		OnlineVendorInvoiceDataProcessor oProcessor = new OnlineVendorInvoiceDataProcessor ();
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		OnlineVendorInvoiceDataResponse oResponse = (OnlineVendorInvoiceDataResponse) oProcessor.list(oData, oOrderBy);
		return oResponse;
	}

	private void buildVendroSalesVatReport(VatReportData oVatReportData,int nMonth, int nYear,OnlineVendorInvoiceData oData) throws Exception
	{
		OnlineVendorInvoiceDataResponse onlineVendorInvoiceDataResponse=getVendorInvoicesForPeriod(nMonth,nYear,oData);
		for(int nIndex = 0; nIndex < onlineVendorInvoiceDataResponse.m_arrOnlineVendorInvoice.size(); nIndex++)
			buildSalesReport (oVatReportData, onlineVendorInvoiceDataResponse.m_arrOnlineVendorInvoice.get(nIndex).getM_oInvoiceData());
	}
}
