package com.techmust.inventory.stocktransfer;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.ItemDataProcessor;
import com.techmust.inventory.items.ItemDataResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class StockTransferDataProcessor extends GenericIDataProcessor<StockTransferData> 
{
	Logger m_oLogger = Logger.getLogger(StockTransferDataProcessor.class.getName());
	public GenericResponse create (@RequestBody StockTransferData oStockTransferData) 
	{
		m_oLogger.debug ("create - oStockTransferData.getM_oTransferredFrom() [IN] :" +oStockTransferData.getM_oTransferredFrom());
		m_oLogger.debug ("create - oStockTransferData.getM_oTransferredTo() [IN] :" +oStockTransferData.getM_oTransferredTo());
		StockTransferResponse oResponse = new StockTransferResponse ();
		try 
		{
			createLog(oStockTransferData, "StockTransferDataProcessor.create");
			oResponse.m_bSuccess = oStockTransferData.saveObject();
		} 
		catch (Exception oException)
		{
			m_oLogger.error("create -oException " + oException);
		}
		m_oLogger.error("create - oResponse [OUT] :  " + oResponse);
		return oResponse;
	}

	@Override
	public GenericResponse deleteData (StockTransferData oStockTransferData) 
	{
		m_oLogger.debug("deleteData - oStockTransferData [IN] : " + oStockTransferData.getM_nStockTransferID());
		
		StockTransferResponse oResponse = new StockTransferResponse ();
		try 
		{
			createLog(oStockTransferData, "StockTransferDataProcessor.deleteData");
			oResponse.m_bSuccess = oStockTransferData.deleteObject();
		} 
		catch (Exception oException)
		{
			m_oLogger.error("deleteData -oException " + oException);
		}
		m_oLogger.error("deleteData - oResponse [OUT] :  " + oResponse);
		return oResponse;
	}

	@Override
	public GenericResponse get (StockTransferData oStockTransferData) throws Exception 
	{
		m_oLogger.debug("get - oStockTransferData.getM_nStockTransferID () [IN] : " +oStockTransferData.getM_nStockTransferID());
		
		StockTransferResponse oResponse = new StockTransferResponse ();
		try 
		{
			oStockTransferData = (StockTransferData) populateObject(oStockTransferData);
			oResponse.m_arrStockTransfer.add(oStockTransferData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("get -oException " + oException);
		}
		m_oLogger.error("get - oResponse [OUT] :  " + oResponse);
		return oResponse;
	}

	@Override
	public String getXML(StockTransferData oStockTransferData)
	{
		m_oLogger.debug ("getXML - oStockTransferData [IN] : "+ oStockTransferData);
		try
		{
			oStockTransferData = (StockTransferData) populateObject(oStockTransferData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " , oException);
		}
		return oStockTransferData != null ? oStockTransferData.generateXML () : ""; 
	}
	
	@RequestMapping(value="/stockTransferList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData) throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list(oData.getM_oStockTransferData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}

	@Override
	public GenericResponse list(StockTransferData oStockTransferData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oStockTransferData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse list(StockTransferData oStockTransferData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception
	 {
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oStockTransferData [IN] : " +oStockTransferData);
		StockTransferResponse oResponse = new StockTransferResponse ();
		try 
		{
			oResponse.m_nRowCount = getRowCount(oStockTransferData);
			oResponse.m_arrStockTransfer = new ArrayList (oStockTransferData.list (arrOrderBy, nPageNumber, nPageSize));
			oResponse.m_arrStockTransfer = setClientCompatibleDate (oResponse.m_arrStockTransfer);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
			throw oException;
		}
		return oResponse;
	}

	private ArrayList<StockTransferData> setClientCompatibleDate(ArrayList<StockTransferData> arrStockTransfer) 
	{
		m_oLogger.info("setClientCompatibleDate");
		for (int nIndex=0; nIndex < arrStockTransfer.size(); nIndex++)
			arrStockTransfer.get(nIndex).setM_strDate(getClientCompatibleFormat(arrStockTransfer.get(nIndex).getM_dDate())); 
		return arrStockTransfer;
	}

	@Override
	public GenericResponse update(StockTransferData oStockTransferData) 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oStockTransferData.getM_nStockTransferID() [IN] : " + oStockTransferData.getM_nStockTransferID());
		StockTransferResponse oResponse = new StockTransferResponse ();
		try
		{
			createLog(oStockTransferData, "StockTransferDataProcessor.update");
			oResponse.m_bSuccess = oStockTransferData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		m_oLogger.debug ("update - oResponse.m_bSuccess [OUT] : " + oResponse.m_bSuccess);
		return oResponse;
	}
	
	@RequestMapping(value="/stockTransferCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse transfer (@RequestBody StockTransferData oStockTransferData) throws Exception
	{
		m_oLogger.info("transfer");
		m_oLogger.debug ("transfer - oStockTransferData [IN]" +oStockTransferData);
		StockTransferResponse oResponse = new StockTransferResponse ();
		UserInformationData oUserData = new UserInformationData ();
		oUserData.setM_nUserId(oStockTransferData.getM_nTransferredBy());
		if (!isValidUser(oUserData))
			throw new Exception (kUserCredentialsFailed);
		StockTransferMemoData oMemoData = new StockTransferMemoData();
		oMemoData.setM_oTransferredFrom(oStockTransferData.getM_oTransferredFrom());
		oMemoData.setM_oTransferredTo(oStockTransferData.getM_oTransferredTo());
		oMemoData.setM_dTransferredOn(oStockTransferData.getM_dTransferredOn());
		
		for (int nIndex = 0 ; nIndex < oStockTransferData.m_arrStockTransferLineItem.length; nIndex++ )
		{
			StockTransferLineItemData oStockTransferLineItemData = oStockTransferData.m_arrStockTransferLineItem[nIndex];
			ItemData oItemData = getItem(oStockTransferLineItemData, oUserData);
			try
			{
				StockTransferData oCreateData = new StockTransferData ();
				oCreateData.setM_oItemData(oItemData);
				oCreateData.setM_oTransferredFrom(oStockTransferData.getM_oTransferredFrom());
				oCreateData.setM_oTransferredTo(oStockTransferData.getM_oTransferredTo());
				oCreateData.setM_nQuantity(oStockTransferLineItemData.getM_nQuantity());
				oCreateData.setM_nTransferredBy(oStockTransferData.getM_nTransferredBy());
				oCreateData.setM_dDate(getDBCompatibleDateFormat(oStockTransferData.getM_strDate()));
				oCreateData.saveObject();
				oMemoData.addStockTransfer(oCreateData);
				updateItemLocationTable (oCreateData);
				oResponse.m_bSuccess = true;
			}
			catch (Exception oException)
			{
				m_oLogger.error("transfer - oException : ", oException);
				throw oException;
			}
		}
		oMemoData.saveObject();
		oMemoData = (StockTransferMemoData) populateObject(oMemoData);
		oResponse.strStockTransferXML = oMemoData.generateXML();
		return oResponse;
	}
	
	private void updateItemLocationTable(StockTransferData oStockTransferData) throws Exception
	{
		m_oLogger.info("updateItemLocationTable");
		m_oLogger.debug ("updateItemLocationTable - oStockTransferData [IN]" +oStockTransferData);
		updateTranferFromData (oStockTransferData);
		updateTransferToData (oStockTransferData);
	}

	private void updateTransferToData(StockTransferData oStockTransferData) throws Exception
	{
		m_oLogger.info("updateTransferToData");
		m_oLogger.debug ("updateTransferToData - oStockTransferData [IN]" +oStockTransferData);
		if (isItemLocationExists (oStockTransferData, false))
			updateItemLocation (oStockTransferData, false);
		else
			insertToItemLocation (oStockTransferData, false);
	}

	private void insertToItemLocation(StockTransferData oStockTransferData, boolean bIsFrom) throws Exception
	{
		m_oLogger.info("insertToItemLocation");
		m_oLogger.debug ("insertToItemLocation - oStockTransferData [IN]" +oStockTransferData);
		m_oLogger.debug ("insertToItemLocation - bIsFrom [IN] : " +bIsFrom);
		try
		{
			ItemLocationData oItemLocationData = new ItemLocationData ();
			oItemLocationData.setM_oItemData(oStockTransferData.getM_oItemData());
			oItemLocationData.setM_oLocationData(bIsFrom ? oStockTransferData.getM_oTransferredFrom() : oStockTransferData.getM_oTransferredTo());
			if (bIsFrom)
				oItemLocationData.issued(oStockTransferData.getM_nQuantity());
			else
				oItemLocationData.received(oStockTransferData.getM_nQuantity());
			oItemLocationData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error("insertToItemLocation - oException : " , oException);
			throw oException;
		}
	}

	private void updateItemLocation(StockTransferData oStockTransferData, boolean bIsFrom) throws Exception 
	{
		m_oLogger.info ("updateItemLocation");
		m_oLogger.debug ("updateItemLocation - oStockTransferData [IN]" +oStockTransferData);
		m_oLogger.debug ("updateItemLocation - bIsFrom [IN] : " +bIsFrom);
		try
		{
			ItemLocationData oItemLocationData = new ItemLocationData ();
			oItemLocationData.setM_oItemData(oStockTransferData.getM_oItemData());
			oItemLocationData.setM_oLocationData(bIsFrom ? oStockTransferData.getM_oTransferredFrom() : oStockTransferData.getM_oTransferredTo());
			oItemLocationData = (ItemLocationData)populateObject(oItemLocationData);
			if (bIsFrom)
				oItemLocationData.issued(oStockTransferData.getM_nQuantity());
			else
				oItemLocationData.received(oStockTransferData.getM_nQuantity());
			oItemLocationData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateItemLocation - oException : " , oException);
			throw oException;
		}
	}

	private boolean isItemLocationExists(StockTransferData oStockTransferData, boolean bIsFrom) 
	{
		m_oLogger.debug ("isItemLocationExists -oStockTransferData [IN]: " + oStockTransferData);
		m_oLogger.debug ("isItemLocationExists -bIsFrom [IN]: " +bIsFrom);
		boolean bIsItemLocationExists = false;
		ItemLocationDataProcessor oItemLocationDataProcessor = new ItemLocationDataProcessor ();
		ItemLocationData oItemLocationData = new ItemLocationData ();
		try
		{
			oItemLocationData.setM_oItemData(oStockTransferData.getM_oItemData());
			oItemLocationData.setM_oLocationData(bIsFrom ? oStockTransferData.getM_oTransferredFrom() : oStockTransferData.getM_oTransferredTo());
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ItemLocationDataResponse oItemLocationDataResponse = (ItemLocationDataResponse) oItemLocationDataProcessor.list(oItemLocationData, oOrderBy);
			bIsItemLocationExists = oItemLocationDataResponse.m_arrItemLocation.size() > 0;
		}
		catch (Exception oException)
		{
			m_oLogger.error("isItemLocationExists - oException : " + oException);
		}
		m_oLogger.debug("isItemLocationExists - bIsItemLocationExists [OUT] : " + bIsItemLocationExists);
		return bIsItemLocationExists;
	}

	private void updateTranferFromData(StockTransferData oStockTransferData) throws Exception
	{
		m_oLogger.info ("updateTranferFromData");
		m_oLogger.debug ("updateTranferFromData - oStockTransferData [IN]" +oStockTransferData);
		if (isItemLocationExists (oStockTransferData, true))
			updateItemLocation (oStockTransferData, true);
		else
			insertToItemLocation (oStockTransferData, true);
	}

	private ItemData getItem(StockTransferLineItemData oStockTransferLineItemData, UserInformationData oUserData)
	{
		m_oLogger.info ("getItem");
		m_oLogger.debug ("getItem - oStockTransferLineItemData [IN]" + oStockTransferLineItemData);
		m_oLogger.debug ("getItem - oUserData [IN]" + oUserData);
		ItemData oItemData = new ItemData ();
		try
		{
			ItemDataProcessor oItemDataProcessor = new ItemDataProcessor ();
			ItemDataResponse oItemDataResponse = new ItemDataResponse ();
			String strArticleNumber = oStockTransferLineItemData.getM_strArticleNumber();
			oItemData.setM_strArticleNumber(strArticleNumber);
			oItemData.setM_oUserCredentialsData(oUserData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oItemDataResponse = (ItemDataResponse) oItemDataProcessor.list(oItemData, oOrderBy);
			if (strArticleNumber != null && strArticleNumber.length() > 0 && oItemDataResponse.m_arrItems.size() > 0)
				oItemData = oItemDataResponse.m_arrItems.get(0);
			else
				oItemData = null;
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getItem - oException : " + oException);
		}
		m_oLogger.debug("getItem - oItemData [OUT]:" + oItemData);
		return oItemData;
	}
	
	private void createLog (StockTransferData oData, String strFunctionName)
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
}
