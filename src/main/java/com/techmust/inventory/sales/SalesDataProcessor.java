package com.techmust.inventory.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ClientDataProcessor;
import com.techmust.clientmanagement.ClientDataResponse;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.ItemDataResponse;
import com.techmust.inventory.purchaseorder.PurchaseOrderData;
import com.techmust.inventory.returned.ReturnedData;
import com.techmust.master.tax.Tax;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class SalesDataProcessor extends GenericIDataProcessor<SalesData> 
{
	public static final String kClientnotexist = "Client not Exist!";
	
	Logger oLogger = Logger.getLogger(SalesDataProcessor.class.getName());

	public GenericResponse create(SalesData oSalesData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oSalesData [IN] : " + oSalesData);
		SalesDataResponse oSalesDataResponse = new SalesDataResponse ();
		try
		{
			createLog(oSalesData, "SalesDataProcessor.create : ");
			isvalidUser (oSalesData);
			isValidClient (oSalesData);
			isCreditLimitExceeded(oSalesData);
			ArrayList<ItemData> arrSalesItems = new ArrayList<ItemData>();
			prepareSalesData(oSalesData, arrSalesItems);
			oSalesDataResponse.m_bSuccess = oSalesData.saveObject();
			updateInventory (arrSalesItems);
			oSalesData = (SalesData) populateObject(oSalesData);
			oSalesDataResponse.m_arrSales.add(oSalesData);
		}
		catch (Exception oException)
		{
			oSalesDataResponse.m_strError_Desc = oException.getMessage();
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug("create - oSalesDataResponse [OUT] : " + oSalesDataResponse.m_bSuccess);
		return oSalesDataResponse;
	}

	@Override
	public GenericResponse deleteData(SalesData oSalesData) throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse get(SalesData oSalesData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oSalesData.getM_nId() [IN] :" + oSalesData.getM_nId());
		SalesDataResponse oSalesDataResponse = new SalesDataResponse ();
		try 
		{
			isvalidUser (oSalesData);
			oSalesData = (SalesData) populateObject (oSalesData);
			oSalesData.customizeSalesLineItems ();
			oSalesData.setM_strDate (getClientCompatibleFormat (oSalesData.getM_dDate()));
//			oSalesData.setM_strTime(oSalesData.getM_dCreatedOn());
			oSalesDataResponse.m_arrSales.add(oSalesData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oSalesDataResponse;
	}

	@Override
	public String getXML(SalesData oSalesData) throws Exception
	{
		m_oLogger.debug ("getXML - oSalesData [IN] : "+ oSalesData);
		try
		{
			isvalidUser (oSalesData);
			oSalesData = (SalesData) populateObject(oSalesData);
			oSalesData.setM_strDate(getClientCompatibleFormat(oSalesData.getM_dDate()));
//			oSalesData.setM_strTime(oSalesData.getM_dCreatedOn());
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " , oException);
		}
		return oSalesData != null ?oSalesData.generateXML () : ""; 
	}

    public GenericResponse list(SalesData oSalesData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oSalesData [IN] : " +oSalesData);
		SalesDataResponse oSalesDataResponse = new SalesDataResponse ();
		try 
		{
			isvalidUser (oSalesData);
			oSalesDataResponse.m_nRowCount = getRowCount(oSalesData);
			oSalesDataResponse.m_arrSales = new ArrayList (oSalesData.list (arrOrderBy, nPageNumber, nPageSize));
			oSalesDataResponse.m_arrSales = buildSalesData (oSalesDataResponse.m_arrSales);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oSalesDataResponse;
	}
	
    public GenericResponse listClientSales(SalesData oSalesData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		m_oLogger.info ("listClentSales");
		m_oLogger.debug ("listClentSales - oSalesData [IN] : " +oSalesData);
		SalesDataResponse oSalesDataResponse = new SalesDataResponse ();
		try 
		{
			isvalidUser (oSalesData);
			oSalesDataResponse.m_nRowCount = getRowCount(oSalesData);
			oSalesDataResponse.m_arrSales = new ArrayList (oSalesData.list(arrOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("listClentSales - oException : " +oException);
		}
		return oSalesDataResponse;
	}
	
	public ArrayList <ReportClientData> getClientReport (SalesData oSalesData, HashMap<String, String> arrOrderBy) throws Exception
	{
		ArrayList <ReportClientData> arrReportClientData = new ArrayList<ReportClientData> ();
		SalesDataResponse oResponse = (SalesDataResponse) list (oSalesData, arrOrderBy);
		for (int nIndex = 0; nIndex < oResponse.m_arrSales.size(); nIndex ++)
		{
			SalesData oReportSalesData = oResponse.m_arrSales.get(nIndex);
			if (!isAdded(arrReportClientData, oReportSalesData))
					addToReportArray (arrReportClientData, oReportSalesData);
			else
				updateReportArray (arrReportClientData, oReportSalesData);
			
		}
		return arrReportClientData;
	}

	private void updateReportArray(ArrayList<ReportClientData> arrReportClientData, SalesData oReportSalesData)
	{
		for (int nIndex = 0; nIndex < arrReportClientData.size(); nIndex++)
		{
			if (arrReportClientData.get(nIndex).m_oClientData.getM_nClientId() == oReportSalesData.getM_oClientData().getM_nClientId())
			{
				arrReportClientData.get(nIndex).m_arrSalesData.add(oReportSalesData);
				break;
			}
		}
	}

	private void addToReportArray(ArrayList<ReportClientData> arrReportClientData, SalesData oReportSalesData)
	{
		ReportClientData oReportClientData = new ReportClientData ();
		oReportClientData.m_oClientData = oReportSalesData.getM_oClientData();
		oReportClientData.m_arrSalesData.add(oReportSalesData);
		arrReportClientData.add(oReportClientData);
	}

	private boolean isAdded(ArrayList<ReportClientData> arrReportClientData, SalesData oReportSalesData) 
	{
		boolean bIsAdded = false;
		for (int nIndex = 0; !bIsAdded && nIndex < arrReportClientData.size(); nIndex++)
		{
			if (arrReportClientData.get(nIndex).m_oClientData.getM_nClientId() == oReportSalesData.getM_oClientData().getM_nClientId())
				bIsAdded = true;
		}
		return bIsAdded;
	}

	@Override
	public GenericResponse update(SalesData oSalesData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oSalesData.getM_nId() [IN] : " + oSalesData.getM_nId());
		SalesDataResponse oSalesDataResponse = new SalesDataResponse ();
		try
		{
			createLog(oSalesData, "SalesDataProcessor.update : ");
			isvalidUser (oSalesData);
			isValidClient (oSalesData);
			isCreditLimitExceeded(oSalesData);
			ArrayList<ItemData> arrPreviousSalesItems = getPreviousSalesItems (oSalesData);
			updateInventory (arrPreviousSalesItems);
			removePreviousChildren (oSalesData);
			ArrayList<ItemData> arrSalesItems = new ArrayList<ItemData>();
			prepareSalesData(oSalesData, arrSalesItems);
			oSalesDataResponse.m_bSuccess = oSalesData.updateObject();
			oSalesData = (SalesData) populateObject(oSalesData);
			oSalesDataResponse.m_arrSales.add(oSalesData);
			updateInventory (arrSalesItems);
			if(oSalesData.getM_oInvoiceData() != null && oSalesData.getM_oInvoiceData ().getM_nInvoiceId() > 0)
				oSalesData.updateInvoiceAmount ();
		}
		catch (Exception oException)
		{
			oSalesDataResponse.m_strError_Desc = oException.getMessage();
			m_oLogger.error ("update - oException : " + oException);
		}
		m_oLogger.debug("update- oSalesDataResponse.m_bSuccess [OUT] :" +oSalesDataResponse.m_bSuccess );
		return oSalesDataResponse;
	}
	
	public GenericResponse saveAndPrint(SalesData oSalesData) throws Exception
	{
		m_oLogger.info("saveAndPrint");
		m_oLogger.debug("saveAndPrint - oSalesData [IN] : " + oSalesData);
		SalesDataResponse oSalesDataResponse = new SalesDataResponse ();
		try
		{
			oSalesDataResponse = prepareForPrint((SalesDataResponse) create(oSalesData));
		}
		catch (Exception oException)
		{
			oSalesDataResponse.m_strError_Desc = oException.getMessage();
			m_oLogger.error ("saveAndPrint - oException : " + oException);
		}
		return oSalesDataResponse;
	}
	
	public GenericResponse updateAndPrint(SalesData oSalesData) throws Exception
	{
		m_oLogger.info("updateAndPrint");
		m_oLogger.debug("updateAndPrint - oSalesData [IN] : " + oSalesData);
		SalesDataResponse oSalesDataResponse = new SalesDataResponse ();
		try
		{
			oSalesDataResponse = prepareForPrint ((SalesDataResponse) update(oSalesData));
		}
		catch (Exception oException)
		{
			
			m_oLogger.error ("updateAndPrint - oException : " + oException);
			
		}
		return oSalesDataResponse;
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse mergeClient(int nClientId, ArrayList<ClientData> arrClientData) throws Exception
	{
		m_oLogger.info("mergeClient");
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try
		{
			for (int nIndex = 0; nIndex < arrClientData.size(); nIndex++)
			{
				// For Sales
				SalesData oSalesData = new SalesData (); //SalesData.getInstance(arrClientData.get(nIndex).getM_nClientId());
				HashMap<String, String> oOrderBy = new HashMap<String, String> ();
				mergeSalesClient(new ArrayList (oSalesData.list(oOrderBy)), nClientId);
				
				// For Purchase Order
				PurchaseOrderData oPurchaseOrderData = new PurchaseOrderData ();
				oPurchaseOrderData.setM_oClientData(arrClientData.get(nIndex));
				mergePOClient(oPurchaseOrderData.getList (), nClientId);
				
				//For Returned 
				ReturnedData oReturnedData = new ReturnedData ();
				oReturnedData.setM_oClientData(arrClientData.get(nIndex));
				mergeReturnClient(new ArrayList (oReturnedData.list(oOrderBy)), nClientId);
				
				//To Delete the Client
				ClientDataProcessor oClientDataProcessor = new ClientDataProcessor ();
				oClientDataResponse = oClientDataProcessor.deleteData(arrClientData.get(nIndex));
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateAndPrint - oException : " + oException);
		}
		return oClientDataResponse;
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse getClientItem(SalesData oSalesData) throws Exception
	{
		m_oLogger.info("getClientItem");
		m_oLogger.debug ("getClientItem - oSalesData.getM_nId() [IN] : " + oSalesData.getM_nId());
		SalesDataResponse oSalesDataResponse = new SalesDataResponse ();
		ClientItemDataResponse oClientItemDataResponse = new ClientItemDataResponse ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oSalesDataResponse.m_arrSales = new ArrayList (oSalesData.list (oOrderBy));
			oClientItemDataResponse = getItems(oSalesDataResponse.m_arrSales);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getClientItem - oException : " + oException);
		}
		return oClientItemDataResponse;
	}
	
	private ClientItemDataResponse getItems(ArrayList<SalesData> arrSales) throws Exception 
	{
		ClientItemDataResponse oClientItemDataResponse = new ClientItemDataResponse ();
		for (int nIndex = 0; nIndex < arrSales.size(); nIndex++)
			getItem (arrSales.get(nIndex), oClientItemDataResponse);
		return oClientItemDataResponse;
	}

	private void getItem(SalesData oSalesData, ClientItemDataResponse oClientItemDataResponse) 
	{
		Iterator<SalesLineItemData> oIterator = oSalesData.getM_oSalesLineItems().iterator();
		while(oIterator.hasNext())
		{ 
			SalesLineItemData oSalesLineItemData = oIterator.next();
			ClientItemData oClientItemData = new ClientItemData ();
			if(!isItemAdded (oSalesLineItemData, oClientItemDataResponse.m_arrClientItemData))
				oClientItemDataResponse.m_arrClientItemData.add(oClientItemData.setClientItemData(oSalesLineItemData));
			else
				updateClientItemData (oClientItemDataResponse.m_arrClientItemData, oSalesLineItemData);
		}
	}

	private void updateClientItemData(ArrayList<ClientItemData> arrClientItemData, SalesLineItemData oSalesLineItemData) 
	{
		for (int nIndex = 0; nIndex < arrClientItemData.size(); nIndex++)
		{
			if(oSalesLineItemData.getM_oItemData().getM_nItemId() == arrClientItemData.get(nIndex).getM_oItemData().getM_nItemId())
			{
				arrClientItemData.get(nIndex).updateClientItemData(oSalesLineItemData);
				break;
			}
		}
	}

	private boolean isItemAdded(SalesLineItemData oSalesLineItemData, ArrayList<ClientItemData> arrClientItemData) 
	{
		boolean bItemAdded = false;
		for (int nIndex = 0; nIndex < arrClientItemData.size(); nIndex++)
		{
			if(oSalesLineItemData.getM_oItemData().getM_nItemId() == arrClientItemData.get(nIndex).getM_oItemData().getM_nItemId())
			{
				bItemAdded = true;
				break;
			}
		}
		return bItemAdded;
	}

	private void mergeReturnClient(ArrayList<ReturnedData> arrReturnedData, int nClientId) throws Exception 
	{
		for (int nIndex = 0; nIndex < arrReturnedData.size(); nIndex++)
		{
			ClientData oClientData = new ClientData ();
			oClientData.setM_nClientId(nClientId);
			oClientData = (ClientData) populateObject(oClientData);
			arrReturnedData.get(nIndex).mergeClient(oClientData);
		}
	}

	private void mergePOClient(ArrayList<PurchaseOrderData> arrPurchaseOrderData, int nClientId) throws Exception
	{
		for (int nIndex = 0; nIndex < arrPurchaseOrderData.size(); nIndex++)
		{
			ClientData oClientData = new ClientData ();
			oClientData.setM_nClientId(nClientId);
			oClientData = (ClientData) populateObject(oClientData);
			arrPurchaseOrderData.get(nIndex).mergeClient(oClientData);
		}
	}

	private void mergeSalesClient(ArrayList<SalesData> arrSalesData, int nClientId) throws Exception 
	{
		for (int nIndex = 0; nIndex < arrSalesData.size(); nIndex++)
		{
			ClientData oClientData = new ClientData ();
			oClientData.setM_nClientId(nClientId);
			oClientData = (ClientData) populateObject(oClientData);
			arrSalesData.get(nIndex).mergeClient(oClientData);
		}
	}

	protected SalesDataResponse prepareForPrint(SalesDataResponse oSalesDataResponse) throws Exception
    {
		SalesData oResponseSalesData = oSalesDataResponse.m_arrSales.get(0);
		ClientData oClientData = (ClientData) populateObject(oResponseSalesData.getM_oClientData());
		oResponseSalesData.setM_oClientData(oClientData);
		oSalesDataResponse.m_strXMLData = oResponseSalesData.generateXML();	
		oSalesDataResponse.m_arrSales.add(oResponseSalesData);
		return oSalesDataResponse;
    }

	private void removePreviousChildren (SalesData oSalesData) throws Exception
    {
		m_oLogger.info ("removePreviousChilds");
		m_oLogger.debug ("removePreviousChilds - oSalesData [IN] : " + oSalesData);
		try
		{
			oSalesData = (SalesData) populateObject(oSalesData);
			Iterator<SalesLineItemData> oIterator = oSalesData.getM_oSalesLineItems().iterator();
			while (oIterator.hasNext())
			{
				SalesLineItemData oSalesLineItem = (SalesLineItemData) oIterator.next();
				oSalesLineItem.deleteObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("removePreviousChilds - oException : " , oException);
			throw oException;
		}
    }
	
	private ArrayList<ItemData> getPreviousSalesItems(SalesData oSalesData) throws Exception
	{
		m_oLogger.debug("getPreviousSalesItems - oSalesData [IN] : " + oSalesData);
		ArrayList<ItemData> arrSalesItems = new ArrayList<ItemData>();
		try
		{
			SalesData oData = new SalesData ();
			oData.setM_nId(oSalesData.getM_nId());
			oData = (SalesData) populateObject(oData);
			Iterator<SalesLineItemData> oIterator = oData.getM_oSalesLineItems().iterator();
			while (oIterator.hasNext())
			{
				SalesLineItemData oSalesLineItemData = oIterator.next();
				ItemData oItemData = oSalesLineItemData.getM_oItemData();
				try
				{
					if(oItemData.getM_oChildItems().size() > 0)
						oItemData.updateIssuedForChildItems(-oSalesLineItemData.getM_nQuantity());
					else
						oItemData.issued(-oSalesLineItemData.getM_nQuantity());
					arrSalesItems.add(oItemData);
				}
				catch (Exception oException)
				{
					m_oLogger.error ("getPreviousSalesItems - oException : " + oException);
					throw oException;
				}
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
		}
		return arrSalesItems;
	}
	
	private void updateInventory(ArrayList<ItemData> arrSalesItems) throws Exception
    {
	   m_oLogger.info("updateInventory");
	   try
	   {
		   for (int nIndex = 0; nIndex < arrSalesItems.size(); nIndex++)
			   arrSalesItems.get(nIndex).updateObject();
	   }
	   catch (Exception oException)
	   {
		   throw oException;
	   }
    }
	
	private ArrayList<SalesData> buildSalesData(ArrayList<SalesData> arrSalesData) 
	{
		m_oLogger.info("buildSalesData");
		for (int nIndex=0; nIndex < arrSalesData.size(); nIndex++)
			arrSalesData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrSalesData.get(nIndex).getM_dDate())); 
		return arrSalesData;
	}
	
	private void prepareSalesData(SalesData oSalesData, ArrayList<ItemData> arrSalesItems) throws Exception
    {
		m_oLogger.info("prepareSalesData");
		ClientData oClientData = new ClientData ();
		oClientData.setM_nClientId(oSalesData.getM_oClientData().getM_nClientId());
		oClientData = (ClientData) populateObject(oClientData);
		oSalesData.setM_strTo(oClientData.getM_strCompanyName());
		oSalesData.setM_dDate(getDBCompatibleDateFormat(oSalesData.getM_strDate()));
		if(oSalesData.getM_nId() > 0)
		{
			SalesData oData = (SalesData) populateObject(oSalesData);
			oSalesData.setM_oInvoiceData(oData.getM_oInvoiceData());
		}
//		oSalesData.getM_oSalesLineItems().clear();
		for (int nIndex = 0 ; nIndex < oSalesData.m_arrSalesLineItem.length; nIndex++ )
		{
			SalesLineItemData oSalesLineItemData = oSalesData.m_arrSalesLineItem[nIndex];
			try
			{
				ClientArticleData oItemData = ClientArticleData.getInstance(oClientData, oSalesLineItemData.getM_strArticleNumber(), oSalesData.getM_oUserCredentialsData());
				if (isArticleAdded (oSalesLineItemData.getM_strArticleNumber(), oSalesData))
					continue;
				ItemData oInventoryItem = new ItemData ((ItemData)oItemData);
				if(oItemData.getM_oChildItems().size() > 0)
					oInventoryItem.updateIssuedForChildItems(oSalesLineItemData.getM_nQuantity());
				else
					oInventoryItem.issued(oSalesLineItemData.getM_nQuantity());
				oInventoryItem.setM_oItemCategoryData(oItemData.getM_oItemCategoryData());
				arrSalesItems.add(oInventoryItem);
				
				ItemData oSalesItem = new ItemData ();
				oSalesItem.setM_nItemId(oItemData.getM_nItemId());
				
				oSalesLineItemData.setM_strArticleDescriptions(oItemData);
				oSalesLineItemData.setM_oItemData(oSalesItem);
				oSalesLineItemData.setM_nCreatedBy(oSalesData.getM_nCreatedBy());
				oSalesLineItemData.setM_strTaxName(oItemData.getTaxName(oClientData.isM_bOutstationClient()));
				oSalesData.addLineItem(oSalesLineItemData);
			}
			catch (Exception oException)
			{
				m_oLogger.error("prepareSalesData - oException : ", oException);
				throw oException;
			}
		}
    }
	
	private boolean isArticleAdded(String strArticleNumber, SalesData oSalesData) 
	{
		m_oLogger.debug("isArticleAdded - strArticleNumber [IN] : " + strArticleNumber);
		boolean bIsArticleAdded = false;
		Iterator<SalesLineItemData> oIterator = oSalesData.getM_oSalesLineItems().iterator();
		while (oIterator.hasNext())
		{
			SalesLineItemData oData = (SalesLineItemData) oIterator.next();
			if (strArticleNumber == oData.getM_strArticleNumber())
			{
				bIsArticleAdded = true;
				break;
			}
		}
		m_oLogger.debug("isArticleAdded - bIsArticleAdded [OUT] : " + bIsArticleAdded);
		return bIsArticleAdded;
	}

	private void isvalidUser (SalesData oSalesData) throws Exception
	{
		m_oLogger.info("isvalidUser");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oSalesData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oSalesData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}
	
	private void isValidClient(SalesData oSalesData) throws Exception
	{
		m_oLogger.info("isValidClient");
		try
		{
			
			ClientData oClientData = new ClientData ();
			oClientData.setM_nClientId(oSalesData.getM_oClientData().getM_nClientId());
			oClientData = (ClientData) populateObject(oClientData);
			if (oClientData == null)
				throw new Exception (kClientnotexist);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isValidClient - oException : " + oException);
			throw oException;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void isCreditLimitExceeded(SalesData oSalesData) throws Exception 
	{
		m_oLogger.debug("isCreditLimitExceeded - oSalesData : " + oSalesData);
		InvoiceData oInvoiceData = new InvoiceData ();
		oInvoiceData.setM_oClientData(oSalesData.getM_oClientData());
		oInvoiceData.setM_bIsForClientOutstanding(true);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		ArrayList arrInvoice = new ArrayList (oInvoiceData.list (oOrderBy));
		m_oLogger.debug("isCreditLimitExceeded - arrInvoice.size() : " + arrInvoice.size());
		if (arrInvoice.size() > 0)
		  checkCreditExceeded (arrInvoice, oSalesData);
	}
	
	@SuppressWarnings("unchecked")
	private void checkCreditExceeded(ArrayList arrInvoice, SalesData oSalesData) throws Exception
    {
		m_oLogger.info("checkCreditExceeded");
		Object[] arrInvoiceObject = (Object[])arrInvoice.get(0);
		ClientData oClientData = (ClientData) arrInvoiceObject[0];
		if(oClientData.getM_nCreditLimit() > 0)
		{
			double nBalanceAmount = (Double) arrInvoiceObject[3];
			double nSalesAmount = oSalesData.calculateSalesTotal(oSalesData.m_arrSalesLineItem);
			double nExceededAmount = (nBalanceAmount + nSalesAmount) - oClientData.getM_nCreditLimit();
			m_oLogger.debug("checkCreditExceeded - nExceededAmount : " + nExceededAmount);
			if(nExceededAmount > 0)
						throw new Exception ("Client Credit Limit Exceeded! \n" +
						"Client Credit limit - " + oClientData.getM_nCreditLimit()+ "\n" +
 						"Client Balance Amount - " + nBalanceAmount + "\n" +
						"This Sales Amount - " + nSalesAmount + "\n" +
						"Exceeded Amount - " + nExceededAmount);
		}
    }

	private void createLog (SalesData oSalesData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			ClientData oClientData = new ClientData ();
			oClientData.setM_nClientId(oSalesData.getM_oClientData().getM_nClientId());
			oClientData = (ClientData) populateObject(oClientData);
			oSalesData.setM_strTo(oClientData.getM_strCompanyName());
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oSalesData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oSalesData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName + oSalesData.getM_strTo());
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
	
public String getXML(@RequestBody Tax oTax) throws Exception
{
	oTax = (Tax) populateObject(oTax);
    return oTax != null ? oTax.generateXML () : "";
}

@Override
public GenericResponse list(SalesData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
	// TODO Auto-generated method stub
	return null;
}
}
