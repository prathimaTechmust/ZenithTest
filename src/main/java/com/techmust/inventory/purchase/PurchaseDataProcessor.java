package com.techmust.inventory.purchase;

import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.directwebremoting.io.FileTransfer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.ItemDataProcessor;
import com.techmust.inventory.items.ItemDataResponse;
import com.techmust.inventory.location.LocationData;
import com.techmust.inventory.location.LocationDataResponse;
import com.techmust.inventory.paymentsandreceipt.PaymentData;
import com.techmust.inventory.stocktransfer.StockTransferData;
import com.techmust.inventory.vendorpurchaseorder.VendorPurchaseOrderData;
import com.techmust.inventory.vendorpurchaseorder.VendorPurchaseOrderDataProcessor;
import com.techmust.inventory.vendorpurchaseorder.VendorPurchaseOrderDataResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;
import com.techmust.vendormanagement.VendorDataProcessor;
import com.techmust.vendormanagement.VendorDataResponse;

public class PurchaseDataProcessor extends GenericIDataProcessor<PurchaseData> 
{
	public static final String kVendornotexist = "Vendor not Exist!";
	@RequestMapping(value="/PurchaseDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse createPurchase (@RequestBody PurchaseData oPurchaseData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oPurchaseData.getM_strFrom() [IN] : " + oPurchaseData.getM_strFrom());
		m_oLogger.debug ("create - oPurchaseData.getM_strInvoiceNo() [IN] : " + oPurchaseData.getM_strInvoiceNo());
	
		PurchaseDataResponse oResponse = new PurchaseDataResponse ();
		try
		{
			createLog(oPurchaseData, "PurchaseDataProcessor.create : " + oPurchaseData.getM_strFrom());
			isvalidUser (oPurchaseData);
			ArrayList<ItemData> arrPurchaseItems = new ArrayList<ItemData>();
			isValidVendor (oPurchaseData);
			preparePurchaseData (oPurchaseData, arrPurchaseItems);
			oResponse.m_bSuccess = oPurchaseData.saveObject();
			oResponse.m_arrPurchase.add(oPurchaseData);
			updateInventory (arrPurchaseItems);
		}
		catch (Exception oException)
		{
			oResponse.m_strError_Desc = oException.getMessage();
			m_oLogger.error ("create - oException : " + oException);
		}
		m_oLogger.debug("create - oResponse.m_bSuccess" + oResponse.m_bSuccess);
		return oResponse;
	}

	@RequestMapping(value="/PurchaseDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData ( @RequestBody PurchaseData oPurchaseData) throws Exception 
	{
		return null;
	}

	@RequestMapping(value="/PurchaseDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get ( @RequestBody PurchaseData oPurchaseData) throws Exception
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oPurchaseData.getM_nId() [IN] :" + oPurchaseData.getM_nId());
		PurchaseDataResponse oPurchaseDataResponse = new PurchaseDataResponse ();
		try 
		{
			isvalidUser (oPurchaseData);
			oPurchaseData = (PurchaseData) populateObject (oPurchaseData);
			oPurchaseData.setM_strDate (getClientCompatibleFormat (oPurchaseData.getM_dDate()));
			oPurchaseDataResponse.m_arrPurchase.add(oPurchaseData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oPurchaseDataResponse;
	}

	@RequestMapping(value="/PurchaseDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML (@RequestBody PurchaseData oPurchaseData) 
	{
		m_oLogger.debug("getXML - oPurchaseData [IN] : " + oPurchaseData);
		try
		{
			isvalidUser (oPurchaseData);
			oPurchaseData = (PurchaseData) populateObject(oPurchaseData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " + oException);
		}
		return oPurchaseData != null ?oPurchaseData.generateXML () : ""; 
	}

	@RequestMapping(value="/PurchaseDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		GenericResponse oPurchaseDataResponse = list(oTradeMustHelper.getM_oPurchaseData(),oOrderBy, oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
		return oPurchaseDataResponse;
	}
	
	public PurchaseDataResponse list (PurchaseData oPurchaseData,HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oPurchaseData [IN] : " +oPurchaseData);		
		PurchaseDataResponse oResponse = new PurchaseDataResponse ();
		try 
		{
			//isvalidUser (oPurchaseData);
			PurchaseDataResponse.m_nRowCount = getRowCount(oPurchaseData);
			oResponse.m_arrPurchase = new ArrayList (oPurchaseData.list (arrOrderBy, nPageNumber, nPageSize));
			oResponse.m_arrPurchase = buildPurchaseData (oResponse.m_arrPurchase);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		m_oLogger.debug("list - oResponse.m_arrPurchase.size () [OUT] : " + oResponse.m_arrPurchase.size());
		return oResponse;
	}
	
	@RequestMapping(value="/getVendorReport", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ArrayList <ReportVendorData> getVendorReport (@RequestBody TradeMustHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return getVendorReport(oData.getM_oPurchaseData(),oOrderBy);
	}

	public ArrayList <ReportVendorData> getVendorReport (PurchaseData oPurchaseData, HashMap<String, String> arrOrderBy) throws Exception
	{
		ArrayList <ReportVendorData> arrReportVendorData = new ArrayList<ReportVendorData> ();
		PurchaseDataResponse oResponse = (PurchaseDataResponse) list (oPurchaseData, arrOrderBy);
		for (int nIndex = 0; nIndex < oResponse.m_arrPurchase.size(); nIndex ++)
		{
			PurchaseData oReportPurchaseData = oResponse.m_arrPurchase.get(nIndex);
			if (!isAdded(arrReportVendorData, oReportPurchaseData))
					addToReportArray (arrReportVendorData, oReportPurchaseData);
			else
				updateReportArray (arrReportVendorData, oReportPurchaseData);
			
		}
		return arrReportVendorData;
	}
	
	private boolean isAdded(ArrayList<ReportVendorData> arrReportVendorData, PurchaseData oReportPurchaseData) 
	{
		boolean bIsAdded = false;
		for (int nIndex = 0; !bIsAdded && nIndex < arrReportVendorData.size(); nIndex++)
		{
			if (arrReportVendorData.get(nIndex).m_oVendorData.getM_nClientId() == oReportPurchaseData.getM_oVendorData().getM_nClientId())
				bIsAdded = true;
		}
		return bIsAdded;
	}

	private void updateReportArray(	ArrayList<ReportVendorData> arrReportVendorData, PurchaseData oReportPurchaseData) 
	{
		for (int nIndex = 0; nIndex < arrReportVendorData.size(); nIndex++)
		{
			if (arrReportVendorData.get(nIndex).m_oVendorData.getM_nClientId() == oReportPurchaseData.getM_oVendorData().getM_nClientId())
			{
				arrReportVendorData.get(nIndex).m_arrPurchaseData.add(oReportPurchaseData);
				break;
			}
		}
	}

	private void addToReportArray(ArrayList<ReportVendorData> arrReportVendorData, PurchaseData oReportPurchaseData) 
	{
		ReportVendorData oReportVendorData = new ReportVendorData ();
		oReportVendorData.m_oVendorData = oReportPurchaseData.getM_oVendorData();
		oReportVendorData.m_arrPurchaseData.add(oReportPurchaseData);
		arrReportVendorData.add(oReportVendorData);
	}

	@RequestMapping(value="/PurchaseDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse updatePurchase (@RequestBody PurchaseData oPurchaseData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oPurchaseData.getM_nId() [IN] : " + oPurchaseData.getM_nId());
		PurchaseDataResponse oResponse = new PurchaseDataResponse ();
		try
		{
			createLog(oPurchaseData, "PurchaseDataProcessor.update : " + oPurchaseData.getM_strFrom());
			isvalidUser (oPurchaseData);
			ArrayList<ItemData> arrPreviousPurchaseItems = getPreviousPurchaseItems (oPurchaseData);
			updateInventory (arrPreviousPurchaseItems);
			removePreviousChilds (oPurchaseData);
			ArrayList<ItemData> arrPurchaseItems = new ArrayList<ItemData>();
			preparePurchaseData (oPurchaseData, arrPurchaseItems);
			oResponse.m_bSuccess = oPurchaseData.updateObject();
			updateInventory (arrPurchaseItems);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			oResponse.m_strError_Desc = oException.toString();
		}
		m_oLogger.debug ("update - oResponse.m_bSuccess [OUT] :" + oResponse.m_bSuccess);
		return oResponse;
	}
	
	@RequestMapping(value="/getVendorDetails", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getVendorDetails (@RequestBody int nVendorId)
	{
		String strXMLData = "<VendorDetails>";
		VendorData oVendorData = new VendorData ();
		oVendorData.setM_nClientId(nVendorId);
		try 
		{
			oVendorData = (VendorData) populateObject(oVendorData);
			strXMLData += oVendorData.generateXML();
			strXMLData += getVendorBusinessDetails (nVendorId);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getVendorDetails - oException : " + oException);
		}
		return strXMLData + "</VendorDetails>";
	}
	
	@RequestMapping(value="/PurchaseDataEnterInvoice", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse enterInvoice (@RequestBody PurchaseData oPurchaseData, VendorPurchaseOrderData oPOData) throws Exception 
	{
		m_oLogger.info ("enterInvoice");
		m_oLogger.debug ("enterInvoice - oPurchaseData : " + oPurchaseData);
		PurchaseDataResponse oPurchaseDataResponse = new PurchaseDataResponse ();
		VendorPurchaseOrderDataProcessor oPODataProcessor = new VendorPurchaseOrderDataProcessor ();
		try
		{
			VendorPurchaseOrderDataResponse oPODataResponse = oPOData.getM_nPurchaseOrderId() > 0 ? (VendorPurchaseOrderDataResponse) oPODataProcessor.update(oPOData) : (VendorPurchaseOrderDataResponse) oPODataProcessor.create(oPOData);
			VendorPurchaseOrderData oOrderData = new VendorPurchaseOrderData ();
			oOrderData.setM_nPurchaseOrderId(oPODataResponse.m_arrVendorPurchaseOrderData.get(0).getM_nPurchaseOrderId());
			oPurchaseData.setM_oVendorPurchaseOrderData(oOrderData);
			oPurchaseDataResponse = (PurchaseDataResponse) createPurchase(oPurchaseData);
			VendorPurchaseOrderData oPurchaseOrderData = (VendorPurchaseOrderData) populateObject(oPurchaseDataResponse.m_arrPurchase.get(0).getM_oVendorPurchaseOrderData());
			oPurchaseOrderData.setReceived (oPurchaseDataResponse.m_arrPurchase.get(0));
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("enterInvoice - oException : " + oException);
		}
		return oPurchaseDataResponse;
	}
	
	public FileTransfer exportToTally (PurchaseData oData) throws Exception
    {
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
    	PurchaseDataResponse oPurchaseDataResponse = (PurchaseDataResponse) list(oData, oOrderBy);
    	String strTallyXml = "";
    	strTallyXml = GenericUtil.buildHtml(oData.generateTallyDataXML(oPurchaseDataResponse.m_arrPurchase), GenericUtil.getProperty("kTallyPurchaseFormatXSLT"));
    	InputStream oInputStream = new ByteArrayInputStream(strTallyXml.getBytes());
    	FileTransfer oDwrXMLFile = new FileTransfer("tally_pur_"+System.currentTimeMillis()+".xml", "application/xml", oInputStream);
    	return oDwrXMLFile;
    }
	
	@RequestMapping(value="/PurchaseDataCreateWithLocation", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse createWithLocation(@RequestBody TradeMustHelper oData)throws Exception
	{
		return createWithLocation (oData.getM_oPurchaseData(), oData.getM_oLocationData());
	}
	
	public GenericResponse createWithLocation (PurchaseData oPurchaseData, LocationData oLocationData) throws Exception 
	{
		m_oLogger.info ("createWithLocation");
		m_oLogger.debug ("createWithLocation - oPurchaseData : " + oPurchaseData);
		PurchaseDataResponse oPurchaseDataResponse = new PurchaseDataResponse ();
		StockTransferData oStockTransferData = new StockTransferData ();
		try
		{
			oPurchaseDataResponse = (PurchaseDataResponse) createPurchase(oPurchaseData);
			LocationData oDefaultLocationData = LocationData.getDefaultLocation ();
			if (oDefaultLocationData.getM_nLocationId() != oLocationData.getM_nLocationId())
			{
				oStockTransferData.moveToLocation (oPurchaseData, oLocationData, oDefaultLocationData);
				PurchaseToLocationData oPurchaseToLocationData = new PurchaseToLocationData ();
				oPurchaseToLocationData.setM_oPurchaseData(oPurchaseData);
				oPurchaseToLocationData.setM_oLocationData(oLocationData);
				oPurchaseDataResponse.m_bSuccess = oPurchaseToLocationData.saveObject();
			}
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("createWithLocation - oException : " + oException);
		}
		return oPurchaseDataResponse;
	}
	
	private String getVendorBusinessDetails(int nVendorId) 
    {
	 	String strXMLData = "<BusinessDetails>";
    	strXMLData += "<nTotalBusiness>" + (getTotalPurchasesAmount (nVendorId) - getPurchaseReturnedAmount (nVendorId)) + "</nTotalBusiness>";
    	strXMLData += "<nTotalPaid>" + getTotalPaidAmount (nVendorId)+ "</nTotalPaid>";
		return strXMLData + "</BusinessDetails>";
	}

	private float getPurchaseReturnedAmount(int nVendorId) 
	{
		float nReturnedAmount = 0;
		PurchaseData oPurchaseData = new PurchaseData ();
		VendorData oVendorData = new VendorData ();
		oVendorData.setM_nClientId(nVendorId);
		oPurchaseData.setM_oVendorData(oVendorData);
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ArrayList<PurchaseData> arrPurchases = new ArrayList (oPurchaseData.list(oOrderBy));
			for (int nIndex = 0; nIndex < arrPurchases.size(); nIndex++) 
			{
				arrPurchases.get(nIndex).m_bIsForVendorOutstanding = true;
//				nReturnedAmount += arrPurchases.get(nIndex).getPurchaseAmount ();
			}
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getTotalPurchasesAmount - oException : " + oException);
		}
		return nReturnedAmount;
	}

	@SuppressWarnings("unchecked")
	private float getTotalPurchasesAmount(int nVendorId) 
	{
		float nAmount = 0;
		PurchaseData oPurchaseData = new PurchaseData ();
		VendorData oVendorData = new VendorData ();
		oVendorData.setM_nClientId(nVendorId);
		oPurchaseData.setM_oVendorData(oVendorData);
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ArrayList<PurchaseData> arrPurchases = new ArrayList (oPurchaseData.list(oOrderBy));
//			for (int nIndex = 0; nIndex < arrPurchases.size(); nIndex++) 
//				nAmount += arrPurchases.get(nIndex).getPurchaseAmount ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getTotalPurchasesAmount - oException : " + oException);
		}
		return nAmount;
	}
	
	private float getTotalPaidAmount(int nVendorId) 
	{
		float nAmount = 0;
		PaymentData oPaymentData = new PaymentData ();
		nAmount = oPaymentData.getVendorPaymentsAmount (nVendorId);
		return nAmount;
	}
	
	private void preparePurchaseData(PurchaseData oPurchaseData, ArrayList<ItemData> arrPurchaseItems) throws Exception
    {
		m_oLogger.info("preparePurchaseData");
		HashSet<PurchaseLineItem> oHashPurchaseLineItem = new HashSet<PurchaseLineItem> ();
		for (int nIndex = 0 ; nIndex < oPurchaseData.m_arrPurchaseLineItem.length; nIndex++ )
		{
			PurchaseLineItem oPurchaseLineItem = oPurchaseData.m_arrPurchaseLineItem[nIndex];
			ItemData oItemData = getItem(oPurchaseLineItem, oPurchaseData);
			try
			{
				oItemData.received(oPurchaseLineItem.getM_nQuantity());
				arrPurchaseItems.add(oItemData);
				ItemData oPurchaseItem = new ItemData ();
				oPurchaseItem.setM_nItemId(oItemData.getM_nItemId());
				oPurchaseLineItem.setM_oItemData(oPurchaseItem);
				oPurchaseLineItem.setM_nCreatedBy(oPurchaseData.getM_nCreatedBy());
				if (oPurchaseLineItem.getM_strArticleNo() != null && oPurchaseLineItem.getM_strArticleNo().length() > 0)
				{
					oPurchaseData.addLineItem(oPurchaseLineItem);
					oHashPurchaseLineItem.add(oPurchaseLineItem);
				}
			}
			catch (Exception oException)
			{
				m_oLogger.error ("preparePurchaseData - oException : " + oException);
			}
		}
		oPurchaseData.setM_oPurchaseLineItems(oHashPurchaseLineItem);
		oPurchaseData.setM_dDate(getDBCompatibleDateFormat(oPurchaseData.getM_strDate()));
		oPurchaseData.setM_nTotalAmount(Math.round(oPurchaseData.getPurchaseAmount ()));
		if(oPurchaseData.getM_nId() > 0 )
		{
			PurchaseData oData = (PurchaseData) populateObject(oPurchaseData);
			oPurchaseData.setM_nPaymentAmount(oData.getM_nPaymentAmount());
			oPurchaseData.setM_nBalanceAmount(oPurchaseData.getM_nTotalAmount() - oData.getM_nPaymentAmount());
		}
		else
			oPurchaseData.setM_nBalanceAmount(oPurchaseData.getM_nTotalAmount());
    }
	
	private void removePreviousChilds(PurchaseData oPurchaseData) throws Exception
    {
		m_oLogger.debug("removePreviousChilds - oPurchaseData [IN] : " + oPurchaseData);
		try
		{
			oPurchaseData = (PurchaseData) populateObject(oPurchaseData);
			Iterator<PurchaseLineItem> oIterator = oPurchaseData.getM_oPurchaseLineItems().iterator();
			while (oIterator.hasNext())
			{
				PurchaseLineItem oPurcahseLineItem = (PurchaseLineItem) oIterator.next();
				oPurcahseLineItem.deleteObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("removePreviousChilds - oException : " , oException);
			throw oException;
		}
    }

	private ArrayList<ItemData> getPreviousPurchaseItems(PurchaseData oPurchaseData) throws Exception
	{
		m_oLogger.debug("getPreviousPurchaseItems - oPurchaseData [IN] : " + oPurchaseData);
		ArrayList<ItemData> arrPurchaseItems = new ArrayList<ItemData>();
		try
		{
			PurchaseData oData = new PurchaseData ();
			oData.setM_nId(oPurchaseData.getM_nId());
			oData = (PurchaseData) populateObject(oData);
			Iterator<PurchaseLineItem> oIterator = oData.getM_oPurchaseLineItems().iterator();
			while (oIterator.hasNext())
			{
				PurchaseLineItem oPurchaseLineItem = oIterator.next();
				ItemData oItemData = oPurchaseLineItem.getM_oItemData();
				try
				{
					oItemData.received(-oPurchaseLineItem.getM_nQuantity());
					arrPurchaseItems.add(oItemData);
				}
				catch (Exception oException)
				{
					m_oLogger.error ("getPreviousPurchaseItems - oException : " + oException);
					throw oException;
				}
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
		}
		return arrPurchaseItems;
	}

	private ItemData getItem (PurchaseLineItem oPurchaseLineItem, PurchaseData oPurchaseData)
	{
		m_oLogger.debug("getItem - oPurchaseLineItem [IN] : " + oPurchaseLineItem);
		ItemData oItemData = new ItemData ();
		try
		{
			ItemDataProcessor oItemDataProcessor = new ItemDataProcessor ();
			ItemDataResponse oItemDataResponse = new ItemDataResponse ();
			String strArticleNumber = oPurchaseLineItem.getM_strArticleNo();
			oItemData.setM_strArticleNumber(strArticleNumber);
			oItemData.setM_oUserCredentialsData(oPurchaseData.getM_oUserCredentialsData());
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oItemDataResponse = (ItemDataResponse) oItemDataProcessor.list(oItemData,oOrderBy);
			if (strArticleNumber != null && strArticleNumber.length() > 0 && oItemDataResponse.m_arrItems.size() > 0)
				oItemData = oItemDataResponse.m_arrItems.get(0);
			else
				oItemData = null;
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getItem - oException : " + oException);
		}
		m_oLogger.error("getItem - oItemData [OUT]: " + oItemData);
		return oItemData;
	}
	
	private void updateInventory(ArrayList<ItemData> arrPurchaseItems) throws Exception
    {
	   m_oLogger.debug ("updateInventory - arrPurchaseItems [IN] :" + arrPurchaseItems);
	   try
	   {
		   for (int nIndex = 0; nIndex < arrPurchaseItems.size(); nIndex++)
			   arrPurchaseItems.get(nIndex).updateObject();
	   }
	   catch (Exception oException)
	   {
		   throw oException;
	   }
    }
	
	private ArrayList<PurchaseData> buildPurchaseData(ArrayList<PurchaseData> arrPurchaseData) 
	{	
		m_oLogger.debug("buildPurchaseData - arrPurchaseData [IN] : " + arrPurchaseData);
		for (int nIndex=0; nIndex < arrPurchaseData.size(); nIndex++)
			arrPurchaseData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrPurchaseData.get(nIndex).getM_dDate())); 
		return arrPurchaseData;
	}
	
	private void isvalidUser (PurchaseData oPurchaseData) throws Exception
	{
		m_oLogger.debug ("isvalidUser - oPurchaseData [IN] :" + oPurchaseData);
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oPurchaseData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oPurchaseData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}
	
	private void createLog (PurchaseData oPurchaseData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oPurchaseData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oPurchaseData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
	
	private void isValidVendor(PurchaseData oPurchaseData) throws Exception
	{
		m_oLogger.info("isValidVendor");
		try
		{
			
			VendorData oVendorData = new VendorData ();
			oVendorData.setM_strCompanyName(oPurchaseData.getM_strFrom());
			VendorDataProcessor oVendorDataProcessor = new VendorDataProcessor ();
			VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
			oVendorDataResponse = oVendorDataProcessor.listVendor(oVendorData, "", "");
			if (oVendorDataResponse.m_arrVendorData.size() <= 0)
				throw new Exception (kVendornotexist);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isValidVendor - oException : " + oException);
			throw oException;
		}
	}
	
	@RequestMapping(value="/PurchaseDataUpdatePurchaseTable", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse updatePurchaseTable () 
	    {
			m_oLogger.info ("updatePurchaseTable");
			PurchaseDataResponse oResponse = new PurchaseDataResponse ();
			try
			{
				PurchaseData oData = new PurchaseData ();
				oData.setM_nPurchaseType(PurchaseType.kInvalid);
				HashMap<String, String> oOrderBy = new HashMap<String, String> ();
				oResponse = (PurchaseDataResponse) list (oData,oOrderBy, 0, 0);
				oResponse.m_nRowCount = 0;
				for (int nIndex = 0; nIndex < oResponse.m_arrPurchase.size(); nIndex++) 
				{
					PurchaseData oPurchaseData = oResponse.m_arrPurchase.get(nIndex);
					oPurchaseData.setM_nTotalAmount(Math.round(oPurchaseData.getPurchaseAmount()));
					oPurchaseData.setM_nBalanceAmount(oPurchaseData.getM_nTotalAmount() - oPurchaseData.getM_nPaymentAmount());
					oPurchaseData.updateObject();
					oResponse.m_nRowCount++;
				}
				oResponse.m_bSuccess = true;
			}
			catch (Exception oException)
			{
				m_oLogger.error ("updatePurchaseTable - oException : " + oException);
			}
			return oResponse;
	    }
	 
	@RequestMapping(value="/getAgeWiseInvoices", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	 public Object[] getAgeWiseInvoices (@RequestBody PurchaseData oData )
	    {
	    	m_oLogger.info ("getAgeWiseInvoices");
	    	Object[] arrInvoice = new Object [7];
	    	try
			{
	    		String[] arrPeriod =  getAgeWiseArray ();
	    		for (int nIndex = 0; nIndex < arrPeriod.length; nIndex++) 
	    		{
	    			Object[] arrAgeWiseData = new Object [3];
	    			arrAgeWiseData[0] = arrPeriod[nIndex];
	    			String[] arrDates = arrPeriod[nIndex].split("-");
	    			PurchaseData oPurchaseData = new PurchaseData ();
	    			String strFromDate = arrDates.length > 1 ? getDate(Integer.parseInt(arrDates[1])) : "";
	    			oPurchaseData.setM_strFromDate(strFromDate);
	    			oPurchaseData.setM_strToDate(getDate(Integer.parseInt(arrDates[0])));
	    			oPurchaseData.m_bIsForAgeWise = true;
	    			oPurchaseData.setM_oVendorData(oData.getM_oVendorData());
	    			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
	    			ArrayList arrInvoiceData = oPurchaseData.list(oOrderBy);
	    			Object[] arrCountAndAmount = (Object[]) arrInvoiceData.get(0);
	    			arrAgeWiseData[1] = arrCountAndAmount[0];
	    			arrAgeWiseData[2] = arrCountAndAmount[1];
	    			arrInvoice[nIndex] = arrAgeWiseData;
	    		}
			}
			catch (Exception oException)
			{
				m_oLogger.error ("getAgeWiseInvoices - oException : " + oException);
			}
	    	return arrInvoice;
	    }

		private String[] getAgeWiseArray()
		{
			String[] arrAgeWise = new String[7];
			arrAgeWise[0] = "91-";
			arrAgeWise[1] = "61-90";
			arrAgeWise[2] = "46-60";
			arrAgeWise[3] = "31-45";
			arrAgeWise[4] = "21-30";
			arrAgeWise[5] = "11-20";
			arrAgeWise[6] = "1-10";
			return arrAgeWise;
		}
		
		private String getDate(int nNoOfDays) 
		{
			String strDate = "";
			if(nNoOfDays > 0)
			{
				Calendar oPreviousDate = Calendar.getInstance();
				oPreviousDate.add(Calendar.DATE, -nNoOfDays);
				strDate =  GenericIDataProcessor.getClientCompatibleFormat((oPreviousDate.getTime()));
			}
			return strDate;
		}

		@Override
		public GenericResponse create(PurchaseData genericData)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GenericResponse update(PurchaseData genericData)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public GenericResponse list(PurchaseData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
}
