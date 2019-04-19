package com.techmust.inventory.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;
import org.directwebremoting.io.FileTransfer;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataexchange.DataExchangeResponse;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.exportimport.ExportImportProviderData;
import com.techmust.generic.listener.ITradeMustEventListener;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.purchase.PurchaseLineItem;
import com.techmust.inventory.purchasereturned.PurchaseReturnedLineItemData;
import com.techmust.inventory.returned.ReturnedLineItemData;
import com.techmust.inventory.sales.CustomizedItemData;
import com.techmust.inventory.sales.CustomizedItemDataProcessor;
import com.techmust.inventory.sales.SalesLineItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class ItemDataProcessor extends GenericIDataProcessor<ItemData>
{
	private static final int kArticleAlreadyExists = 1;
	private static final int kImgHeight = 150;
	private static final int kImgWidth = 150;
	public int m_nLoggedInUserId = -1;
	
	Logger oLogger = Logger.getLogger(ItemDataProcessor.class.getName());

	public GenericResponse create(ItemData oItemData) throws Exception
    {
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oItemData - m_strArticleNumber [IN] : " + oItemData.getM_strArticleNumber());
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try
		{	
			createLog(oItemData, "ItemDataProcessor.create : " + oItemData.getM_strItemName());
			isvalidUser (oItemData);
			if (!(oItemData.isArticleExists (oItemData)))
			{
				oItemData.setM_oCompressedItemPhoto(getBlob (resizeImage(compressImage(oItemData.getM_buffImgPhoto()))));
				oItemData.setM_oItemPhoto(getBlob (oItemData.getM_buffImgPhoto()));
				oItemData.prepareChildItemSet();
				oItemData.buildItemImages ();
				oItemDataResponse.m_bSuccess = oItemData.saveObject();
				oItemDataResponse.m_arrItems.add(oItemData);
				ItemGroupData.addToGroup (oItemData);
				ItemGroupData oItemGroupData = new ItemGroupData ();
				oItemGroupData.notifyEventListeners (ITradeMustEventListener.kNew);
			}
			else
			{
				oItemDataResponse.m_nErrorID = kArticleAlreadyExists;
				oItemDataResponse.m_strError_Desc = "Article already exists";
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oItemDataResponse.m_bSuccess [OUT] : " + oItemDataResponse.m_bSuccess);
		return oItemDataResponse;
    }

	@Override
    public GenericResponse deleteData(ItemData oItemData) throws Exception
    {
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oItemData.getM_nItemId() [IN] : " + oItemData.getM_nItemId());
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try
		{
			createLog(oItemData, "ItemDataProcessor.deleteData : " + oItemData.getM_strItemName());
			isvalidUser (oItemData);
			oItemData = (ItemData) populateObject (oItemData);
			oItemDataResponse.m_bSuccess = oItemData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oItemDataResponse.m_bSuccess [OUT] : " + oItemDataResponse.m_bSuccess);
		return oItemDataResponse;
    }

	@RequestMapping(value="/newitemDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public ItemDataResponse get(@RequestBody ItemData oItemData) throws Exception
    {
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oItemData.getM_nItemId() [IN] :" + oItemData.getM_nItemId());
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try 

		{
			oItemData = (ItemData) populateObject (oItemData);
			oItemData.setM_buffImgPhoto(getBufferedImage (oItemData.getM_oItemPhoto()));
			oItemDataResponse.m_arrItems.add (oItemData);
			oItemDataResponse.m_arrItems = buildItemImagesSetData (oItemDataResponse.m_arrItems);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oItemDataResponse;
    }

	@RequestMapping(value="/itemDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public String getXML(@RequestBody ItemData oItemData) throws Exception
    {
		m_oLogger.info ("getXML : ");
		m_oLogger.debug ("getXML - oItemData [IN] : " +oItemData);
		String strXml = "";
		try 
		{
			isvalidUser (oItemData);
			oItemData = (ItemData) populateObject(oItemData);
			strXml = oItemData != null ? oItemData.generateXML () : "";
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
    }

	@RequestMapping(value="/itemDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		return list (oTradeMustHelper.getM_oItemData(),oOrderBy,oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
	}
	
    public GenericResponse list(ItemData oItemData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception
    {
		int nStartTime = getTimeInSeconds ( new Date ());
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oItemData [IN] : " + oItemData);
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try 
		{
			oItemDataResponse.m_nRowCount = getRowCount(oItemData);
			oItemDataResponse.m_arrItems = new ArrayList (oItemData.list (arrOrderBy, nPageNumber, nPageSize));
			oItemDataResponse.m_arrItems = buildItemData (oItemDataResponse.m_arrItems);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
			throw oException;
		}
		int nEndTime = getTimeInSeconds ( new Date ());
		m_oLogger.debug ("list - nTimeReQuired : "+ (nEndTime-nStartTime));
		return oItemDataResponse;
    }
	
	private int getTimeInSeconds(Date dDate)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dDate);
		int nSeconds = calendar.get(Calendar.SECOND);
		return nSeconds;
	}

	private ArrayList<ItemData> buildItemImagesSetData(ArrayList<ItemData> arrItems) 
	{
		m_oLogger.info("buildItemImagesSetData");
		for (int nIndex=0; nIndex < arrItems.size(); nIndex++)
		{
			if(arrItems.get(nIndex).getM_oItemImage()!=null)
			{
				Iterator<ItemImagesData> oIterator = arrItems.get(nIndex).getM_oItemImage().iterator();
				while (oIterator.hasNext())
				{
					ItemImagesData oItemImagesData = (ItemImagesData) oIterator.next();
					oItemImagesData.setM_buffImgPhoto(getBufferedImage (oItemImagesData.getM_oImages()));
				}
			}
		}
		return arrItems;
	}

	@SuppressWarnings("unchecked")
	public GenericResponse generateThumbnails (ItemData oItemData) throws Exception
    {
		m_oLogger.info ("generateThumbnails");
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			isvalidUser (oItemData);
			oItemDataResponse.m_arrItems = new ArrayList (oItemData.list (oOrderBy));
			for (int nIndex = 0; nIndex < oItemDataResponse.m_arrItems.size(); nIndex++)
			{
				ItemData oData = oItemDataResponse.m_arrItems.get(nIndex);
				Blob oItemPhoto = oItemDataResponse.m_arrItems.get(nIndex).getM_oItemPhoto();
				createThumbnail (oItemPhoto, oData , oItemDataResponse);
			}
			oItemDataResponse.m_bSuccess = true;
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("generateThumbnails - oException : " +oException);
			throw oException;
		}
		return oItemDataResponse;
    }
	
	private void createThumbnail(Blob oItemPhoto, ItemData oData, ItemDataResponse oItemDataResponse) 
	{
		m_oLogger.info ("createThumbnail");
		try 
		{
			if (oItemPhoto != null)
			{
				int blobLength = (int) oItemPhoto.length();  
				byte[] nBlobAsBytes = oItemPhoto.getBytes(1, blobLength);  
				BufferedImage oOriginalbuffImage = ImageIO.read( new ByteArrayInputStream(nBlobAsBytes) ); 
				oData.setM_oCompressedItemPhoto(getBlob (resizeImage(compressImage(oOriginalbuffImage))));
				oData.updateObject();
				oItemDataResponse.m_nRowCount++;
			}
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("createThumbnail - oException : " +oException);
		}
	}

	@SuppressWarnings("unchecked")
	public GenericResponse getBrandName (ItemData oItemData) throws Exception
	{
		m_oLogger.info ("getBrandList");
		m_oLogger.debug ("getBrandList - oItemData [IN] : " + oItemData);
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oItemDataResponse.m_arrItems = new ArrayList (oItemData.list (oOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getBrandList - oException : " +oException);
			throw oException;
		}
		return oItemDataResponse;
	}
	
	private ArrayList<ItemData> buildItemData(ArrayList<ItemData> arrItemData) 
	{
		m_oLogger.info("buildItemData");
		for (int nIndex=0; nIndex < arrItemData.size(); nIndex++)
			arrItemData.get(nIndex).setM_buffImgPhoto(getBufferedImage (arrItemData.get(nIndex).getM_oCompressedItemPhoto()));
		return arrItemData;
	}
	
	@RequestMapping(value="/getArticalSuggestion", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getArticleSuggesstions (@RequestBody TradeMustHelper oData) throws Exception
	{
		return this.getArticleSuggesstions(oData.getM_oItemData(), oData.getM_strColumn(), oData.getM_strOrderBy());
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse getArticleSuggesstions (ItemData oItemData, String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getArticleSuggesstions");
		m_oLogger.debug ("getArticleSuggesstions - oItemData [IN] : " + oItemData);
		m_oLogger.debug ("getArticleSuggesstions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getArticleSuggesstions - strOrderBy [IN] : " + strOrderBy);
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try 
		{
			isvalidUser (oItemData);
			oItemDataResponse.m_arrItems = new ArrayList (oItemData.listCustomData(this));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getArticleSuggesstions - oException : " +oException);
			throw oException;
		}
		return oItemDataResponse;
	}
	
	@RequestMapping(value="/getClientArticalSuggestion", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getClientArticleSuggesstions (@RequestBody TradeMustHelper oData) throws Exception
	{
		return getClientArticleSuggesstions (oData.getM_oItemData(), oData.getM_nClientId(), oData.getM_strColumn(), oData.getM_strOrderBy());
	}
	
	public GenericResponse getClientArticleSuggesstions (ItemData oItemData, int nClientId, String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getClientArticleSuggesstions");
		m_oLogger.debug ("getClientArticleSuggesstions - oItemData [IN] : " + oItemData);
		m_oLogger.debug ("getClientArticleSuggesstions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getClientArticleSuggesstions - strOrderBy [IN] : " + strOrderBy);
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try 
		{
			ItemDataResponse oArticleList = (ItemDataResponse) getArticleSuggesstions (oItemData, strColumn, strOrderBy);
			ArrayList<ItemData> m_arrItems = oArticleList.m_arrItems;
			if(nClientId > 0)
			{
				ArrayList<ItemData> m_arrClientArticleData = getClientArticleList (nClientId, oItemData);
				oItemDataResponse.m_arrItems.addAll(m_arrClientArticleData);
			}
			oItemDataResponse.m_arrItems.addAll(m_arrItems);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getClientArticleSuggesstions - oException : " +oException);
			throw oException;
		}
		return oItemDataResponse;
	}
	
	@RequestMapping(value="/newitemDatagetItemData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ItemData getItemData (@RequestBody ItemData oItemData, int nClientId) throws Exception
	{
		m_oLogger.info ("getItemData");
		m_oLogger.debug ("getItemData - nClientId [IN] : " + nClientId);
		m_oLogger.debug ("getItemData - oItemData [IN] : " + oItemData);
		ItemData oData = new ItemData ();
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oData = (ItemData) populateObject (oItemData);
			CustomizedItemData oCustomizedItemData = new CustomizedItemData ();
			oCustomizedItemData.getM_oClientData().setM_nClientId(nClientId);
			oCustomizedItemData.getM_oItemData().setM_nItemId(oData.getM_nItemId());
			ArrayList<CustomizedItemData> arrCustomizedItemData = new ArrayList(oCustomizedItemData.list(oOrderBy));
			CustomizedItemDataProcessor oCustomizedItemDataProcessor = new CustomizedItemDataProcessor ();
			ArrayList<ItemData> arrItems = oCustomizedItemDataProcessor.extractItems(arrCustomizedItemData);
			if(arrItems.size() > 0)
				oData = arrItems.get(0);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getClientArticleSuggesstions - oException : " +oException);
			throw oException;
		}
		return oData;
	}
	
	private ArrayList<ItemData> getClientArticleList(int nClientId, ItemData oItemData) 
	{
		ArrayList<ItemData> arrItems = new ArrayList<ItemData>();
		CustomizedItemDataProcessor oDataProcessor = new CustomizedItemDataProcessor ();
		try 
		{
			arrItems = oDataProcessor.getArticleSuggestions (nClientId, oItemData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getClientArticleList - oException : " , oException);
		}
		return arrItems;
	}
	
	@RequestMapping(value="/newitemDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
    public GenericResponse update(@RequestBody ItemData oItemData) throws Exception
    {
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oItemData.getM_nItemId() [IN] : " + oItemData.getM_nItemId());
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try
		{
			createLog(oItemData, "ItemDataProcessor.update : " + oItemData.getM_strItemName());
			isvalidUser (oItemData);
			setItemPhoto (oItemData);
			removePreviousChildren (oItemData);
			oItemData.prepareChildItemSet();
			oItemData.buildItemImages ();
			oItemDataResponse.m_bSuccess = oItemData.updateObject();
			ItemGroupData.removeFromGroup (oItemData);
			ItemGroupData.addToGroup (oItemData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oItemDataResponse.m_bSuccess [OUT] : " + oItemDataResponse.m_bSuccess);
		return oItemDataResponse;
    }

	public void removePreviousChildren (ItemData oItemData) throws Exception 
	{
		m_oLogger.info ("removePreviousChildren");
		m_oLogger.debug ("removePreviousChildren - oItemData [IN]: " + oItemData);
		try
		{
			oItemData = (ItemData) populateObject(oItemData);
			Iterator<ChildItemData> oIterator = oItemData.getM_oChildItems().iterator();
			while (oIterator.hasNext())
			{
				ChildItemData oChildItemData = (ChildItemData) oIterator.next();
				oChildItemData.deleteObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("removePreviousChildren - oException : " , oException);
			throw oException;
		}
	}
	
	public GenericResponse updatePublishOnline (ArrayList<ItemData> arrSelectedItemData , ArrayList<ItemData> arrRemovedItemData) throws Exception
    {
		m_oLogger.info ("updatePublishOnline");
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try
		{
			UserInformationData oUserInformationData = new UserInformationData();
			oUserInformationData.setM_nUserId(arrSelectedItemData.get(0).getM_nCreatedBy());
			oUserInformationData = (UserInformationData) populateObject(oUserInformationData);
			createLog(arrSelectedItemData.get(0), "ItemDataProcessor.updatePublishOnline : " + oUserInformationData.getM_strUserName());
			isvalidUser (arrSelectedItemData.get(0));
			setPublishOnline (arrSelectedItemData);
			resetPublishOnline (arrRemovedItemData);
			oItemDataResponse.m_bSuccess = true;
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updatePublishOnline - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("updatePublishOnline - oItemDataResponse.m_bSuccess [OUT] : " + oItemDataResponse.m_bSuccess);
		return oItemDataResponse;
    }
	
	private void resetPublishOnline(ArrayList<ItemData> arrRemovedItemData) throws Exception 
	{
		for (int nIndex=0 ; nIndex < arrRemovedItemData.size() ; nIndex++)
			updatePublishOnline (arrRemovedItemData.get(nIndex), false);
	}

	private void setPublishOnline(ArrayList<ItemData> arrSelectedItemData) throws Exception 
	{
		for (int nIndex=0 ; nIndex < arrSelectedItemData.size() ; nIndex++)
			updatePublishOnline (arrSelectedItemData.get(nIndex), true);
	}

	public ItemDataResponse updatePublishOnline(ItemData oItemData, boolean bPublishOnline) 
	{
		m_oLogger.info ("updatePublishOnline");
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try
		{
			oItemData = (ItemData) populateObject(oItemData);
			oItemData.setM_bPublishOnline(bPublishOnline);
			oItemDataResponse.m_bSuccess = oItemData.updateObject();
			oItemDataResponse.m_arrItems.add(oItemData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updatePublishOnline - oException : " + oException);
		}
		m_oLogger.debug ("updatePublishOnline - oItemDataResponse.m_bSuccess [OUT] : " + oItemDataResponse.m_bSuccess);
		return oItemDataResponse;
	}
	
	@RequestMapping(value="/getImagePreview", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ItemData getImagePreview (@RequestBody ItemData oData)
	{
		return oData;
	}
	
	@RequestMapping(value="/updateStockEntries", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse updateStockEntries(@RequestBody TradeMustHelper oData) throws Exception
	{
		return updateStockEntries(oData.getM_oStockEntriesData(), oData.getM_oItemData());
	}
	
	public GenericResponse updateStockEntries(StockEntriesData oStockEntriesData, ItemData oData) throws Exception
	{
		m_oLogger.info ("updateStockEntries");
		m_oLogger.debug ("updateStockEntries - oStockEntriesData [IN] : " + oStockEntriesData);
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try
		{
			isvalidUser (oData);
			for (int nIndex=0 ; nIndex < oStockEntriesData.m_arrStockEntries.length ; nIndex++)
			{
				ItemData oItemData = new ItemData ();
				oItemData.setM_nItemId(oStockEntriesData.m_arrStockEntries[nIndex].getM_nItemId());
				oItemData = (ItemData) populateObject(oItemData);
				oItemData.setM_nCostPrice(oStockEntriesData.m_arrStockEntries[nIndex].getM_nCostPrice());
				oItemData.setM_nOpeningStock(oStockEntriesData.m_arrStockEntries[nIndex].getM_nOpeningStock());
				oItemData.setM_nReorderLevel(oStockEntriesData.m_arrStockEntries[nIndex].getM_nReorderLevel());
				oItemData.setM_nSellingPrice(oStockEntriesData.m_arrStockEntries[nIndex].getM_nSellingPrice());
				oItemData.setM_oUserCredentialsData(oData.getM_oUserCredentialsData());
				//oItemDataResponse = (ItemDataResponse) update (oItemData);
				oItemDataResponse.m_bSuccess = oItemData.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateStockEntries - oException : " + oException);
			throw oException;
		}
		return oItemDataResponse;
	}
	
	@RequestMapping(value="/getStockMovementReport", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody	
	public StockMovementDataResponse getStockMovementReport (@RequestBody TradeMustHelper oData)throws Exception
	{
		StockMovementDataResponse oStockMovementDataResponse = new StockMovementDataResponse ();
		try
		{
			oStockMovementDataResponse = getStockMovementReport (oData.getM_strFromDate(), oData.getM_strToDate(), oData.getM_oItemData(),oData.isM_bIncludeZeroMovement());
		}
		catch(Exception oException)
		{
			m_oLogger.error ("getStockMovementReport - oException : " + oException);
		}
		return oStockMovementDataResponse;
	} 
	
	public StockMovementDataResponse getStockMovementReport (String strFromDate, String strToDate, ItemData oItemData, boolean bIncludeZeroMovement) throws Exception
	{
		m_oLogger.info ("getStockMovementReport");
		m_oLogger.debug ("getStockMovementReport - oItemData [IN] : " + oItemData);
		StockMovementDataResponse oStockMovementDataResponse = new StockMovementDataResponse ();
		try
		{
			isvalidUser (oItemData);
			if(bIncludeZeroMovement)
				oStockMovementDataResponse = (StockMovementDataResponse) getAllItemsStockMovementReport (strFromDate, strToDate, oItemData);
			else
				oStockMovementDataResponse =  (StockMovementDataResponse) getNonZeroStockMovementReport (strFromDate, strToDate, oItemData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getStockMovementReport - oException : " , oException);
		}
		return oStockMovementDataResponse;
	}
	
	@Override
	public Criteria prepareCustomCriteria(Criteria oCriteria, ItemData oItem) throws RuntimeException 
	{
		if(oItem.getM_strArticleNumber().trim().length() > 0 && oItem.getM_strItemName().trim().length() > 0)
 		oCriteria.add(Restrictions.disjunction().add(Restrictions.ilike("m_strArticleNumber", oItem.getM_strArticleNumber().trim(), MatchMode.ANYWHERE))
 												.add(Restrictions.ilike("m_strItemName", oItem.getM_strItemName().trim(), MatchMode.ANYWHERE)));
		else if(oItem.getM_strArticleNumber().trim() != "")
			oCriteria.add(Restrictions.ilike("m_strArticleNumber", oItem.getM_strArticleNumber().trim(), MatchMode.ANYWHERE));
		else if(oItem.getM_strItemName().trim() != "")
			oCriteria.add(Restrictions.ilike("m_strItemName", oItem.getM_strItemName().trim(), MatchMode.ANYWHERE));
 		oCriteria.setMaxResults(10);
		return oCriteria;
	}
	
	public static ItemData getItem (int nItemId, UserInformationData oCredentials) throws Exception
	{
		if (nItemId <= 0)
			throw new IllegalArgumentException ("ItemDataProcessor:getItem - ItemId must be greater than zero!");		
		ItemData oItemData = new ItemData ();
		oItemData.setM_nItemId(nItemId);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		return (ItemData) oItemData.list(oOrderBy).get(0);
	}
	
	public FileTransfer exportItemData (ItemData oData, ExportImportProviderData oProviderData) throws Exception
    {
		m_oLogger.info ("exportItemData");
		m_oLogger.debug ("exportItemData - oData [IN] : " + oData);
    	FileTransfer oFile = null;
    	ItemDataResponse oItemDataResponse  = new ItemDataResponse ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
	    	 oItemDataResponse = (ItemDataResponse) list(oData, oOrderBy);
	    	 oFile = oProviderData.export (oItemDataResponse.m_arrItems);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("exportItemData - oException : " + oException);
		}
    	return oFile;

    }
	
	public GenericResponse importItemData (ItemData oItemData, ExportImportProviderData oProviderData, FileTransfer oFileTransfer)
	{
		m_oLogger.info ("importItemData");
		m_oLogger.debug ("importItemData - oFileTransfer [IN] : " + oFileTransfer);
		DataExchangeResponse oDataExchangeResponse = new DataExchangeResponse ();
		try 
		{
			UserInformationData oUserInformationData = new UserInformationData();
			oUserInformationData.setM_nUserId(oItemData.getM_nCreatedBy());
			oDataExchangeResponse = (DataExchangeResponse) oProviderData.importData (oFileTransfer, oItemData.getClass().getName(), oUserInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("importItemData - oException : " + oException);
		}
		return oDataExchangeResponse;
	}
	
	@SuppressWarnings("unchecked")
	public StockMovementDataResponse getNonZeroStockMovementReport (String strFromDate, String strToDate, ItemData oItemData) throws Exception
	{
		m_oLogger.info ("getNonZeroStockMovementReport");
		m_oLogger.debug ("getNonZeroStockMovementReport - strFromDate [IN] : " + strFromDate);
		m_oLogger.debug ("getNonZeroStockMovementReport - strToDate [IN] : " + strToDate);
		m_oLogger.debug ("getNonZeroStockMovementReport - oItemData [IN] : " + oItemData);
		StockMovementDataResponse oDataResponse = new StockMovementDataResponse ();
		try
		{
			ArrayList arrSalesStockMovementData = SalesLineItemData.getStockMovementForPeriod (strFromDate,  strToDate, oItemData);
			ArrayList arrPurchasesStockMovementData = PurchaseLineItem.getStockMovementForPeriod (strFromDate, strToDate, oItemData);
			ArrayList arrSalesReturnedStockMovementData = ReturnedLineItemData.getReturnedStockForPeriod (strFromDate, strToDate, oItemData);
			ArrayList arrPurchaseReturnedStockMovement = PurchaseReturnedLineItemData.getReturnedStockForPeriod (strFromDate, strToDate, oItemData);
			mergeSalesAndPurchaseReturns (arrSalesStockMovementData, arrPurchaseReturnedStockMovement);
			mergePurchaseAndSalesReturn  (arrPurchasesStockMovementData, arrSalesReturnedStockMovementData);
			oDataResponse.m_arrStockMovementData = new ArrayList (buildStockMovementReport (arrSalesStockMovementData, arrPurchasesStockMovementData));
		}
		catch (Exception oException)
		{
			m_oLogger.error("getNonZeroStockMovementReport - oException : " , oException);
		}
		return oDataResponse;
	}
	
	@SuppressWarnings("unchecked")
	private void mergePurchaseAndSalesReturn (ArrayList arrPurchasesStockMovementData, ArrayList arrSalesReturnedStockMovementData) 
	{
		for (int nIndex = 0; nIndex < arrPurchasesStockMovementData.size(); nIndex++)
		{
			ItemData oItemData = new ItemData ();
			Object [] arrPurchaseStockData = (Object [])arrPurchasesStockMovementData.get(nIndex);
			oItemData = (ItemData) arrPurchaseStockData[0];
			double nReceived = (Double) arrPurchaseStockData[1] + getSalesReturn (oItemData.getM_nItemId(), arrSalesReturnedStockMovementData);
			arrPurchaseStockData[1] = nReceived;
			arrPurchasesStockMovementData.set(nIndex, arrPurchaseStockData);
		}
		addRemainigReturn  (arrPurchasesStockMovementData, arrSalesReturnedStockMovementData);
	}

	@SuppressWarnings("unchecked")
	private Double getSalesReturn (int nItemId, ArrayList arrSalesReturnedStockMovementData) 
	{
		double nIssued = 0;
		for (int nIndex = 0; nIndex < arrSalesReturnedStockMovementData.size(); nIndex++)
		{
			Object [] arrSalesReturnedData = (Object [])arrSalesReturnedStockMovementData.get(nIndex);
			ItemData oItemData = (ItemData) arrSalesReturnedData[0];
			if (oItemData.getM_nItemId() ==  nItemId)
			{
				nIssued = (Double) arrSalesReturnedData[1];
				arrSalesReturnedStockMovementData.remove(nIndex);
				break;
			}
		}
		return nIssued;
	}

	@SuppressWarnings("unchecked")
	private void mergeSalesAndPurchaseReturns(ArrayList arrSalesStockMovementData, ArrayList arrPurchaseReturnedStockMovement) 
	{
		for (int nIndex = 0; nIndex < arrSalesStockMovementData.size(); nIndex++)
		{
			ItemData oItemData = new ItemData ();
			Object [] arrSalesStockData = (Object [])arrSalesStockMovementData.get(nIndex);
			oItemData = (ItemData) arrSalesStockData[0];
			double nIssued = (Double) arrSalesStockData[1] + getPurchaseReturn (oItemData.getM_nItemId(), arrPurchaseReturnedStockMovement);
			arrSalesStockData[1] = nIssued;
			arrSalesStockMovementData.set(nIndex, arrSalesStockData);
		}
		addRemainigReturn  (arrSalesStockMovementData, arrPurchaseReturnedStockMovement);
	}

	@SuppressWarnings("unchecked")
	private void addRemainigReturn (ArrayList arrStockMovementData, ArrayList arrReturnedStockMovement) 
	{
		for (int nIndex = 0; nIndex < arrReturnedStockMovement.size(); nIndex++)
			arrStockMovementData.add(arrReturnedStockMovement.get(nIndex));
	}

	@SuppressWarnings("unchecked")
	private double getPurchaseReturn(int nItemId, ArrayList arrPurchaseReturnedStockMovement) 
	{
		double nReturned = 0;
		for (int nIndex = 0; nIndex < arrPurchaseReturnedStockMovement.size(); nIndex++)
		{
			Object [] arrPurchaseReturnedData = (Object [])arrPurchaseReturnedStockMovement.get(nIndex);
			ItemData oItemData = (ItemData) arrPurchaseReturnedData[0];
			if (oItemData.getM_nItemId() ==  nItemId)
			{
				nReturned = (Double) arrPurchaseReturnedData[1];
				arrPurchaseReturnedStockMovement.remove(nIndex);
				break;
			}
		}
		return nReturned;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<StockMovementData> buildStockMovementReport(ArrayList arrSalesStockMovementData, ArrayList arrPurchasesStockMovementData) 
	{
		ArrayList<StockMovementData> arrStockMovementData = new ArrayList<StockMovementData> ();
		for (int nIndex = 0; nIndex < arrSalesStockMovementData.size(); nIndex++)
		{
			StockMovementData oStockMovementData = new StockMovementData ();
			ItemData oItemData = new ItemData ();
			Object [] arrReportData = (Object [])arrSalesStockMovementData.get(nIndex);
			oItemData = (ItemData) arrReportData[0];
			double nIssued = (Double) arrReportData[1];
			float nReceived =  getReceived(oItemData.getM_nItemId(), arrPurchasesStockMovementData);
			oStockMovementData.setM_oItemData(oItemData);
			oStockMovementData.setM_nReceivedQuantity(nReceived);
			oStockMovementData.setM_nIssuedQuantity((float)nIssued);
			arrStockMovementData.add(oStockMovementData);
		}
		addRemainingPurchases (arrStockMovementData, arrPurchasesStockMovementData);
		return arrStockMovementData;
	}
	
	@SuppressWarnings("unchecked")
	private float getReceived(int nItemId, ArrayList arrPurchasesStockMovementData) 
	{
		float nReceived = 0;
		for (int nIndex = 0; nIndex < arrPurchasesStockMovementData.size(); nIndex++)
		{
			Object [] arrReportData = (Object [])arrPurchasesStockMovementData.get(nIndex);
			ItemData oItemData = (ItemData) arrReportData[0];
			if (oItemData.getM_nItemId() ==  nItemId)
			{
				double nReceivedValue = (Double) arrReportData[1];
				nReceived = (float)nReceivedValue;
				arrPurchasesStockMovementData.remove(nIndex);
				break;
			}
		}
		return nReceived;
	}

	@SuppressWarnings("unchecked")
	private void addRemainingPurchases(ArrayList<StockMovementData> arrStockMovementData, ArrayList arrPurchasesStockMovementData) 
	{
		for (int nIndex = 0; nIndex < arrPurchasesStockMovementData.size(); nIndex++)
		{
			StockMovementData oStockMovementData = new StockMovementData ();
			Object [] arrReportData = (Object [])arrPurchasesStockMovementData.get(nIndex);
			oStockMovementData.setM_oItemData((ItemData) arrReportData[0]);
			double nReceived = (Double) arrReportData[1];
			oStockMovementData.setM_nReceivedQuantity((float)nReceived);
			oStockMovementData.setM_nIssuedQuantity(0);
			arrStockMovementData.add(oStockMovementData);
		}
	}
	
	private StockMovementDataResponse getAllItemsStockMovementReport (String strFromDate, String strToDate, ItemData oItemData) throws Exception
	{
		m_oLogger.info ("getAllItemsStockMovementReport");
		m_oLogger.debug ("getAllItemsStockMovementReport - oItemData [IN] : " + oItemData);
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		StockMovementDataResponse oNonZeroMovementDataResponse = new StockMovementDataResponse ();
		StockMovementDataResponse oDataResponse = new StockMovementDataResponse ();
		try
		{
			isvalidUser (oItemData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOrderBy.put("m_strItemName",  "asc");
			oItemDataResponse = (ItemDataResponse) list(oItemData, oOrderBy);
			oNonZeroMovementDataResponse = (StockMovementDataResponse) getNonZeroStockMovementReport (strFromDate, strToDate, oItemData);
			buildStockMovementData(oItemDataResponse, oNonZeroMovementDataResponse, oDataResponse);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getAllItemsStockMovementReport - oException : " , oException);
		}
		return oDataResponse; 
	}
	
	private void buildStockMovementData (ItemDataResponse oItemDataResponse, StockMovementDataResponse oNonZeroMovementDataResponse, StockMovementDataResponse oStockMovementDataResponse) throws Exception
	{
		for (int nIndex = 0; nIndex < oItemDataResponse.m_arrItems.size(); nIndex++)
		{
			StockMovementData oStockMovementData = new StockMovementData ();
			ItemData oItemData = oItemDataResponse.m_arrItems.get(nIndex);
			oStockMovementData.setM_oItemData(oItemData);
			try
			{
				StockMovementData oNonZeroMovementData = getItemInstance (oItemData.getM_nItemId(), oNonZeroMovementDataResponse);
				if(oNonZeroMovementData != null)
					oStockMovementDataResponse.m_arrStockMovementData.add(oNonZeroMovementData);
				else
				{
					oStockMovementData.setM_nIssuedQuantity(0);
					oStockMovementData.setM_nReceivedQuantity(0);
					oStockMovementDataResponse.m_arrStockMovementData.add(oStockMovementData);
				}
			}
			catch (Exception oException)
			{
				m_oLogger.error("buildStockMovementData - oException : " + oException);
				throw oException;
			}
		}
	}
	
	private StockMovementData getItemInstance(int nItemId, StockMovementDataResponse oNonZeroMovementDataResponse) 
	{
		StockMovementData oStocMovementData = null;
		for (int nIndex = 0; nIndex < oNonZeroMovementDataResponse.m_arrStockMovementData.size(); nIndex++)
		{
			StockMovementData oData = oNonZeroMovementDataResponse.m_arrStockMovementData.get(nIndex);
			if(oData.getM_oItemData().getM_nItemId() == nItemId)
			{
				oStocMovementData = oData;
				oNonZeroMovementDataResponse.m_arrStockMovementData.remove(nIndex);
				break;
			}
		}
		return oStocMovementData;
	}

	private void setItemPhoto (ItemData oData)
	{
		m_oLogger.info ("setItemPhoto");
		m_oLogger.debug ("setItemPhoto - oData [IN] : " + oData);
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try 
		{
			if (oData.getM_buffImgPhoto () != null)
			{
				oData.setM_oCompressedItemPhoto(getBlob (resizeImage(compressImage(oData.getM_buffImgPhoto()))));
				oData.setM_oItemPhoto(getBlob (oData.getM_buffImgPhoto()));
				setItemSubImages (oData);
			}
			else
			{
				oItemDataResponse = get (oData);
				oData.setM_oCompressedItemPhoto(oItemDataResponse.m_arrItems.get(0).getM_oCompressedItemPhoto());
				oData.setM_oItemPhoto(oItemDataResponse.m_arrItems.get(0).getM_oItemPhoto());
				oData.setM_buffImgPhoto(getBufferedImage(oData.getM_oItemPhoto()));
				if((oData.m_arrItemImages.length <= 0) && (oData.getM_strImageName().equals(oItemDataResponse.m_arrItems.get(0).getM_strImageName()))&& !(oData.getM_strImageName().equals("")))
					oData.setDefaultChildImage(oData);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("setItemPhoto - oException" + oException);
		}
	}
	
	private void setItemSubImages(ItemData oData) throws Exception
	 {
		if(oData.m_arrItemImages.length>0)
		{
			ItemDataResponse oItemDataResponse = new ItemDataResponse ();
			oItemDataResponse = get (oData);
			if(!(oData.getM_strImageName().equals(oItemDataResponse.m_arrItems.get(0).getM_strImageName())))
				buildItemSubImages (oData,oItemDataResponse);
		}
		else
			oData.setDefaultChildImage(oData);
	}

	private void buildItemSubImages(ItemData oData,ItemDataResponse oResponse) throws Exception 
	{
		String strImage = oResponse.m_arrItems.get(0).getM_strImageName();
		for(int nIndex = 0; nIndex < oData.m_arrItemImages.length; nIndex++)
		{
			if(strImage.equals(oData.m_arrItemImages[nIndex].getM_strFileName()))
			{
				if(IsImageExist(oData))
				{
					ItemImagesData oImageData = new ItemImagesData ();
					oImageData.setM_nId(oData.m_arrItemImages[nIndex].getM_nId());
					oImageData.deleteObject();
					oData.m_arrItemImages[nIndex].setM_nId(-1);
				}
				else
				{
					oData.m_arrItemImages[nIndex].setM_buffImgPhoto(oData.getM_buffImgPhoto());
					oData.m_arrItemImages[nIndex].setM_strFileName(oData.getM_strImageName());
				}
			}
		}
	}

	private boolean IsImageExist(ItemData oData) 
	{
		boolean bIsExist = false;
		String strImageName = oData.getM_strImageName();
		for(int nIndex = 0; nIndex < oData.m_arrItemImages.length; nIndex++)
		{
			if(strImageName.equals(oData.m_arrItemImages[nIndex].getM_strFileName()))
			{
				bIsExist=true;
				break;
			}
		}
		return bIsExist;
	}

	private void isvalidUser (ItemData oItemData) throws Exception
	{
		m_oLogger.info("isvalidUser");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oItemData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oItemData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}
	
	private void createLog (ItemData oItemData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oItemData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oItemData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
	
	protected BufferedImage compressImage(BufferedImage oOriginalImage) 
	{
		m_oLogger.info("compressImage");
		m_oLogger.debug ("compressImage - oOriginalImage [IN] : " + oOriginalImage);
		BufferedImage oImageFromConvert = oOriginalImage;
		try 
		{
			ByteArrayOutputStream oByteArrayOutputStream = new ByteArrayOutputStream();
			ImageOutputStream oImageOutputStream = ImageIO.createImageOutputStream(oByteArrayOutputStream);
			Iterator<ImageWriter> oWriter = ImageIO.getImageWritersByFormatName("jpg");
			ImageWriter oImageWriter = oWriter.next();
			ImageWriteParam oImageWriteParam = oImageWriter.getDefaultWriteParam();
			oImageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			oImageWriteParam.setCompressionQuality(0.3F);
			oImageWriter.setOutput(oImageOutputStream);
			oImageWriter.write(null, new IIOImage(oOriginalImage, null, null), oImageWriteParam);
			byte[] data = oByteArrayOutputStream.toByteArray();
			oImageWriter.dispose();
			InputStream oInputStream = new ByteArrayInputStream(data);
			oImageFromConvert = ImageIO.read(oInputStream);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("compressImage - oException : " + oException);
		}
		return oImageFromConvert;
	}
/*	
	private BufferedImage resizeImage(BufferedImage oOriginalImage)
	{
		m_oLogger.info("resizeImage");
		m_oLogger.debug ("resizeImage - oOriginalImage [IN] : " + oOriginalImage);
		BufferedImage oScaledImage = null;
		try 
		{
			float nOriginalImageWidth = oOriginalImage.getWidth();
			float nOriginalImageHeight = oOriginalImage.getHeight();
			double nScaleFactor = 1.0;
			if (nOriginalImageWidth > nOriginalImageHeight && nOriginalImageWidth > kImgWidth)
				nScaleFactor = kImgWidth / nOriginalImageWidth;
			else if (nOriginalImageHeight > kImgHeight)
				nScaleFactor = kImgHeight / nOriginalImageHeight;
			oScaledImage = new BufferedImage ((int)(nOriginalImageWidth * nScaleFactor), (int)(nOriginalImageHeight * nScaleFactor), BufferedImage.TYPE_INT_ARGB);
			AffineTransform oScale = new AffineTransform ();
			oScale.scale(nScaleFactor, nScaleFactor);
			AffineTransformOp oScaleOp = new AffineTransformOp (oScale, AffineTransformOp.TYPE_BILINEAR);
			oScaledImage = oScaleOp.filter(oOriginalImage, oScaledImage);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("resizeImage - oException : " + oException);
		}
		return oScaledImage;
	 }
*/
	protected BufferedImage resizeImage(BufferedImage oOriginalImage)
	{
		m_oLogger.info("resizeImage");
		m_oLogger.debug ("resizeImage - oOriginalImage [IN] : " + oOriginalImage);
		BufferedImage oResizedImage = oOriginalImage;
		try 
		{
			float nOriginalImageWidth = oOriginalImage.getWidth();
			float nOriginalImageHeight = oOriginalImage.getHeight();
			if (nOriginalImageWidth > nOriginalImageHeight && nOriginalImageWidth > kImgWidth)
				oResizedImage = resizeImage (oOriginalImage, nOriginalImageWidth, nOriginalImageHeight, true);
			else if (nOriginalImageHeight > kImgHeight)
				oResizedImage = resizeImage (oOriginalImage, nOriginalImageWidth, nOriginalImageHeight, false);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("resizeImage - oException : " + oException);
		}
		return oResizedImage;
	 }

	private BufferedImage resizeImage(BufferedImage oOriginalImage, float nOriginalImageWidth, float nOriginalImageHeight, boolean bIsForWidth) 
	{
		m_oLogger.info("resizeImage");
		m_oLogger.debug ("resizeImage - oOriginalImage [IN] : " + oOriginalImage);
		m_oLogger.debug ("resizeImage - nOriginalImageWidth [IN] : " + nOriginalImageWidth);
		m_oLogger.debug ("resizeImage - nOriginalImageHeight [IN] : " + nOriginalImageHeight);
		int nType = oOriginalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : oOriginalImage.getType();
		BufferedImage oResizedImage = oOriginalImage;
		if(bIsForWidth)
		{
			int nImgHeight = (int)((kImgWidth / nOriginalImageWidth) * nOriginalImageHeight);
			oResizedImage = new BufferedImage(kImgWidth, nImgHeight, nType);
			Graphics2D oGraphics2D = oResizedImage.createGraphics();
			oGraphics2D.drawImage(oOriginalImage, 0, 0, kImgWidth, nImgHeight, null);
			oGraphics2D.dispose();
		}
		else
		{
			int nImgwidth = (int)((kImgHeight / nOriginalImageHeight) * nOriginalImageWidth);
			oResizedImage = new BufferedImage(nImgwidth, kImgHeight, nType);
			Graphics2D oGraphics2D = oResizedImage.createGraphics();
			oGraphics2D.drawImage(oOriginalImage, 0, 0, nImgwidth, kImgHeight, null);
			oGraphics2D.dispose();
		}
		return oResizedImage;
	}

	@Override
	public GenericResponse list(ItemData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
