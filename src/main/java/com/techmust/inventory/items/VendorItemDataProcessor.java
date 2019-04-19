package com.techmust.inventory.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

public class VendorItemDataProcessor extends ItemDataProcessor 
{
	@Override
	 public GenericResponse create(ItemData oItemData) throws Exception
    {
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oItemData - m_strArticleNumber [IN] : " + oItemData.getM_strArticleNumber());
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		VendorItemData oVendorItemData = new VendorItemData ();
		try
		{	
			oVendorItemData.getM_oVendorData().setM_nClientId((oItemData.getM_nCreatedBy()));
			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oItemData.setM_nCreatedBy(oItemData.getM_oUserCredentialsData().getM_nUserId());
			oItemDataResponse = (ItemDataResponse) super.create(oItemData);
			oVendorItemData.getM_oItemData().setM_nItemId(oItemDataResponse.m_arrItems.get(0).getM_nItemId());
			oItemDataResponse.m_bSuccess = oVendorItemData.saveObject();
			if(oItemData.m_arrItemImages.length>0)
			{
				oItemData.setM_oItemPhoto(getBlob(oItemData.m_arrItemImages[0].getM_buffImgPhoto()));
				oItemData.setM_oCompressedItemPhoto(getBlob (resizeImage(compressImage(oItemData.m_arrItemImages[0].getM_buffImgPhoto()))));
				oItemData.setM_strImageName(oItemData.m_arrItemImages[0].getM_strFileName());
				oItemData.updateObject();
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
	 public GenericResponse update(ItemData oItemData) throws Exception
    {
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oItemData - m_strArticleNumber [IN] : " + oItemData.getM_strArticleNumber());
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		VendorItemData oVendorItemData = new VendorItemData ();
		try
		{	
			removeItemImagesSet(oItemData);
			setDefaultMainImage(oItemData);
			oVendorItemData.getM_oVendorData().setM_nClientId((oItemData.getM_nCreatedBy()));
 			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oItemData.setM_nCreatedBy(oItemData.getM_oUserCredentialsData().getM_nUserId());
			oItemDataResponse = (ItemDataResponse) super.update(oItemData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oItemDataResponse.m_bSuccess [OUT] : " + oItemDataResponse.m_bSuccess);
		return oItemDataResponse;
    }
	
	private void removeItemImagesSet(ItemData oItemData) throws Exception 
	{
		m_oLogger.info ("removePreviousChildren");
		m_oLogger.debug ("removePreviousChildren - oItemData [IN]: " + oItemData);
		try
		{
			ItemData oData = new ItemData();
			oData.setM_nItemId(oItemData.getM_nItemId());
			oData = (ItemData) populateObject(oItemData);
			Iterator<ItemImagesData> oIterator = oData.getM_oItemImage().iterator();
			while (oIterator.hasNext())
			{
				ItemImagesData oItemImagesData = oIterator.next();
				if(!(checkForItemImages(oItemData,oItemImagesData.getM_nId())))
					oItemImagesData.deleteObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("removePreviousChildren - oException : " , oException);
			throw oException;
		}
	}

	private boolean checkForItemImages(ItemData oItemData, int nId) throws Exception
	{
		boolean bIsForRemoveImage = false;
		for (int nIndex = 0; nIndex < oItemData.m_arrItemImages.length; nIndex++)
		{
			if((oItemData.m_arrItemImages[nIndex].getM_nId()== nId ))
			{
				bIsForRemoveImage = true;
				break;
			}
		}
		return bIsForRemoveImage;
	}
	
	private void setDefaultMainImage(ItemData oItemData) throws Exception 
	{
		ItemData oData = (ItemData) populateObject (oItemData);
		if(oItemData.m_arrItemImages.length>0)
		{
			if(!IsItemImageExist(oData,oItemData))
				buildChildImagesList(oItemData);
		}
		else
		{
			oItemData.setM_oItemPhoto(null);
			oItemData.setM_oCompressedItemPhoto(null);
			oItemData.setM_strImageName("");
			oItemData.updateObject();
		}
	}

	private void buildChildImagesList(ItemData oItemData) throws Exception 
	{
		if(oItemData.m_arrItemImages[0].getM_nId()>-1)
		{
			ItemImagesData oImagesData = (ItemImagesData) populateObject(oItemData.m_arrItemImages[0]);
			oItemData.setM_buffImgPhoto(getBufferedImage(oImagesData.getM_oImages()));
			oItemData.setM_strImageName(oImagesData.getM_strFileName());
		}
		else
		{
			oItemData.setM_buffImgPhoto(oItemData.m_arrItemImages[0].getM_buffImgPhoto());
			oItemData.setM_strImageName(oItemData.m_arrItemImages[0].getM_strFileName());
		}
	}

	private boolean IsItemImageExist(ItemData oData,ItemData oItemData) 
	{
		boolean bIsForImageExist = false;
		for(int nIndex = 0; nIndex < oItemData.m_arrItemImages.length;nIndex++)
		{
			if(oData.getM_strImageName().equals(oItemData.m_arrItemImages[nIndex].getM_strFileName()))
			{
				oItemData.setM_buffImgPhoto(getBufferedImage(oData.getM_oItemPhoto()));
				oItemData.setM_strImageName(oData.getM_strImageName());
				bIsForImageExist = true;
				break;
			}
		}
		return bIsForImageExist;
	}

	
	@RequestMapping(value="/vendoritemDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeHelper.getM_strColumn(), oTradeHelper.getM_strOrderBy());
		return list (oTradeHelper.getM_oItemData(),oOrderBy,oTradeHelper.getM_nPageNo(),oTradeHelper.getM_nPageSize());
	}
	
	public GenericResponse list(ItemData oItemData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		VendorItemData oVendorItemData = new VendorItemData ();
		try
		{	
			oVendorItemData.getM_oVendorData().setM_nClientId((oItemData.getM_nCreatedBy()));
			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oItemData.setM_nCreatedBy(oItemData.getM_oUserCredentialsData().getM_nUserId());
			oVendorItemData.setM_oItemData(oItemData);
			oItemDataResponse.m_nRowCount = getRowCount(oVendorItemData);
			oItemDataResponse.m_arrVendorItems = new ArrayList (oVendorItemData.list (arrOrderBy, nPageNumber, nPageSize));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("list - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("list - oItemDataResponse.m_bSuccess [OUT] : " + oItemDataResponse.m_bSuccess);
		return oItemDataResponse;
	}
	
	public String getXML(ItemData oItemData) throws Exception
    {
		m_oLogger.info ("getXML : ");
		m_oLogger.debug ("getXML - oItemData [IN] : " +oItemData);
		String strXml = "";
		VendorItemData oVendorItemData = new VendorItemData ();
		try 
		{
			oVendorItemData.getM_oVendorData().setM_nClientId((oItemData.getM_nCreatedBy()));
			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oItemData.setM_nCreatedBy(oItemData.getM_oUserCredentialsData().getM_nUserId());
			strXml = super.getXML(oItemData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
    }
	
	@Override
    public GenericResponse deleteData(ItemData oItemData) throws Exception
    {
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oItemData.getM_nItemId() [IN] : " + oItemData.getM_nItemId());
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		VendorItemData oVendorItemData = new VendorItemData ();
		try
		{
			oVendorItemData.getM_oVendorData().setM_nClientId((oItemData.getM_nCreatedBy()));
			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oItemData.setM_nCreatedBy(oItemData.getM_oUserCredentialsData().getM_nUserId());
			oItemDataResponse = (ItemDataResponse) super.deleteData(oItemData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oItemDataResponse.m_bSuccess [OUT] : " + oItemDataResponse.m_bSuccess);
		return oItemDataResponse;
    }
	
	@Override
	public GenericResponse getArticleSuggesstions (ItemData oItemData, String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getArticleSuggesstions");
		m_oLogger.debug ("getArticleSuggesstions - oItemData [IN] : " + oItemData);
		m_oLogger.debug ("getArticleSuggesstions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getArticleSuggesstions - strOrderBy [IN] : " + strOrderBy);
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		VendorItemData oVendorItemData = new VendorItemData ();
		try 
		{
			oVendorItemData.getM_oVendorData().setM_nClientId((oItemData.getM_nCreatedBy()));
			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oItemData.setM_nCreatedBy(oItemData.getM_oUserCredentialsData().getM_nUserId());
			oVendorItemData.setM_oItemData(oItemData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOrderBy.put(strColumn, strOrderBy);
			oItemDataResponse.m_arrVendorItems = new ArrayList (oVendorItemData.list (oOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getArticleSuggesstions - oException : " +oException);
			throw oException;
		}
		return oItemDataResponse;
	}
	
	public GenericResponse getBrandSuggesstions (ItemData oItemData, String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getBrandSuggesstions");
		m_oLogger.debug ("getBrandSuggesstions - oItemData [IN] : " + oItemData);
		m_oLogger.debug ("getBrandSuggesstions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getBrandSuggesstions - strOrderBy [IN] : " + strOrderBy);
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		VendorItemData oVendorItemData = new VendorItemData ();
		try 
		{
			oVendorItemData.getM_oVendorData().setM_nClientId((oItemData.getM_nCreatedBy()));
			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oItemData.setM_nCreatedBy(oItemData.getM_oUserCredentialsData().getM_nUserId());
			oVendorItemData.setM_oItemData(oItemData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOrderBy.put(strColumn, strOrderBy);
			oItemDataResponse.m_arrVendorItems = new ArrayList (oVendorItemData.list (oOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getBrandSuggesstions - oException : " +oException);
			throw oException;
		}
		return oItemDataResponse;
	}
	
	public GenericResponse listPublishedItems(ItemData oItemData, String strColumn, String strOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("listPublishedItems");
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		VendorItemData oVendorItemData = new VendorItemData ();
		try
		{	
			oVendorItemData.getM_oVendorData().setM_bVerified(true);
			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oItemData.setM_nCreatedBy(oItemData.getM_oUserCredentialsData().getM_nUserId());
			oItemData.setM_bPublishOnline(true);
			oVendorItemData.setM_oItemData(oItemData);
			oItemDataResponse.m_nRowCount = getRowCount(oVendorItemData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOrderBy.put(strColumn, strOrderBy);
			oItemDataResponse.m_arrVendorItems = new ArrayList (oVendorItemData.list(oOrderBy, nPageNumber, nPageSize));
			oItemDataResponse.m_arrVendorItems = buildItemImagesSetData (oItemDataResponse.m_arrVendorItems);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("listPublishedItems - oException : " + oException);
			throw oException;
		}
		return oItemDataResponse;
	}
	
	private ArrayList<VendorItemData> buildItemImagesSetData(ArrayList<VendorItemData> arrVendorItems) 
	{
		m_oLogger.info("buildItemImagesSetData");
		for (int nIndex=0; nIndex < arrVendorItems.size(); nIndex++)
		{
			ItemData oData = arrVendorItems.get(nIndex).getM_oItemData();
			arrVendorItems.get(nIndex).getM_oItemData().setM_buffImgPhoto(getBufferedImage (oData.getM_oCompressedItemPhoto()));
			if(arrVendorItems.get(nIndex).getM_oItemData().getM_oItemImage()!=null)
			{
				Iterator<ItemImagesData> oIterator = arrVendorItems.get(nIndex).getM_oItemData().getM_oItemImage().iterator();
				while (oIterator.hasNext())
				{
					ItemImagesData oItemImagesData = (ItemImagesData) oIterator.next();
					oItemImagesData.setM_buffImgPhoto(getBufferedImage (oItemImagesData.getM_oCompressedPhoto()));
				}
			}
		}
		return arrVendorItems;
	}
	
	public GenericResponse listSellerWiseItems(VendorItemData oVendorItemData, String strColumn, String strOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("listGroupItems");
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		ItemData oItemData = new ItemData ();
		try
		{	
			oVendorItemData.getM_oVendorData().setM_bVerified(true);
			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oItemData.setM_nCreatedBy(oItemData.getM_oUserCredentialsData().getM_nUserId());
			oItemData.setM_bPublishOnline(true);
			oVendorItemData.setM_oItemData(oItemData);
			oItemDataResponse.m_nRowCount = getRowCount(oVendorItemData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOrderBy.put(strColumn,strOrderBy );
			oItemDataResponse.m_arrVendorItems = new ArrayList (oVendorItemData.list (oOrderBy, nPageNumber, nPageSize));
			oItemDataResponse.m_arrVendorItems = buildItemImagesSetData (oItemDataResponse.m_arrVendorItems);
		}
		catch(Exception oException)
		{
			m_oLogger.error ("listGroupItems - oException : " + oException);
			throw oException;
		}
		return oItemDataResponse;
	}
	
	public StockMovementDataResponse getStockMovementReport ( String strFromDate, String strToDate, ItemData oItemData, boolean bIncludeZeroMovement) throws Exception
	{
		m_oLogger.info ("getStockMovementReport");
		StockMovementDataResponse oStockMovementDataResponse = new StockMovementDataResponse ();
		try
		{
			VendorItemData oVendorItemData = new VendorItemData ();
			oVendorItemData .getM_oVendorData().setM_nClientId((oItemData.getM_nCreatedBy()));
			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oStockMovementDataResponse = (StockMovementDataResponse) super.getStockMovementReport(strFromDate, strToDate, oItemData, bIncludeZeroMovement);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getStockMovementReport - oException : " + oException);
			throw oException;
		}
		return oStockMovementDataResponse;
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse searchExtended (VendorItemData oVendorItemData) throws Exception
	{
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try
		{	
			oVendorItemData.prepareItemCategorySet();
			oVendorItemData.prepareVendorSet();
			oVendorItemData.prepareGroupSet();
			oItemDataResponse.m_arrVendorItems = new ArrayList (oVendorItemData.listCustomDataForSearch(this));
			if(oVendorItemData.m_arrItemGroups.length>0)
				oItemDataResponse.m_arrVendorItems = CheckForItemGroup(oItemDataResponse.m_arrVendorItems, oVendorItemData);
			oItemDataResponse.m_arrVendorItems = buildVendorItemData (oItemDataResponse.m_arrVendorItems);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("listPublishedItems - oException : " + oException);
			throw oException;
		}
		return oItemDataResponse;
	}
	
	private ArrayList<VendorItemData> buildVendorItemData(ArrayList<VendorItemData> arrVendorItems) 
	{
		m_oLogger.info("buildItemData");
		for (int nIndex=0; nIndex < arrVendorItems.size(); nIndex++)
			arrVendorItems.get(nIndex).getM_oItemData().setM_buffImgPhoto(getBufferedImage (arrVendorItems.get(nIndex).getM_oItemData().getM_oCompressedItemPhoto()));
		return arrVendorItems;
	}

	private ArrayList<VendorItemData> CheckForItemGroup(ArrayList<VendorItemData> arrVendorItems, VendorItemData oVendorItemData)
	{
		ArrayList<VendorItemData> arrVendorItemData = new ArrayList<VendorItemData> ();
		Iterator<ItemGroupData>oIterator = oVendorItemData.getM_oItemGroups().iterator();
		while (oIterator.hasNext())
		{
			ItemGroupData oItemGroupData = oIterator.next();
			arrVendorItemData = addItemsToArray (arrVendorItemData,oItemGroupData,arrVendorItems);
		}
		return arrVendorItemData;
	}

	private ArrayList<VendorItemData> addItemsToArray(ArrayList<VendorItemData> arrVendorItemData, ItemGroupData oItemGroupData,ArrayList<VendorItemData> arrVendorItems) 
	{
		for (int nIndex = 0; nIndex < arrVendorItems.size(); nIndex++)
		{
			Iterator<ItemGroupData>oItemGroupIterator = arrVendorItems.get(nIndex).m_oItemData.getM_oItemGroups().iterator();
			while (oItemGroupIterator.hasNext())
			{
				ItemGroupData oItemGroup = oItemGroupIterator.next();
				if((oItemGroupData.getM_nItemGroupId()== oItemGroup.getM_nItemGroupId())&&!(isItemExists (arrVendorItemData,arrVendorItems.get(nIndex))))
						arrVendorItemData.add(arrVendorItems.get(nIndex));
			}
		}
		return arrVendorItemData;
	}

	private boolean isItemExists(ArrayList<VendorItemData> arrVendorItemData,VendorItemData oVendorItemData)
	{
		  boolean bIsItemExists = false;
		   m_oLogger.info("isItemExists");
		   try
		   {
			   for (int nIndex = 0; nIndex < arrVendorItemData.size(); nIndex++)
				{
				   if(arrVendorItemData.get(nIndex).getM_oItemData().getM_nItemId()==oVendorItemData.getM_oItemData().getM_nItemId())
				   		bIsItemExists = true;
				}
		   }
		   catch (Exception oException)
		   {
			   m_oLogger.error("isItemExists - oException : " + oException);
		   }
		   m_oLogger.debug("isItemExists - bIsItemExists [OUT] : " + bIsItemExists);
		   return bIsItemExists;
	}

	private UserInformationData getAdminUserInfo() 
	{
		m_oLogger.info ("getAdminUserInfo");
		UserInformationData oUserData = new UserInformationData ();
		try
		{	
			oUserData.setM_strUserName("admin");
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oUserData = (UserInformationData) oUserData.list(oOrderBy).get(0);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getAdminUserInfo - oException : " + oException);
		}
		return oUserData;
	}
	
	public Criteria prepareSearchCustomCriteria(Criteria oCriteria,VendorItemData oVendorItemData) 
	{
		Criteria oItemDataCriteria = oCriteria.createCriteria ("m_oItemData" );
		if (oVendorItemData.getM_oItemData().isM_bPublishOnline())
			oItemDataCriteria.add (Restrictions.like ("m_bPublishOnline", oVendorItemData.getM_oItemData().isM_bPublishOnline()));
		if (oVendorItemData.getM_oItemData().isM_bIsForSearch() && !oVendorItemData.getM_oItemData().getM_strKeyword().isEmpty() && (oVendorItemData.getM_oItemData().getM_strKeyword().toLowerCase().contains(oVendorItemData.getM_oItemData().getM_strItemName().toLowerCase())
				|| oVendorItemData.getM_oItemData().getM_strKeyword().toLowerCase().contains(oVendorItemData.getM_oItemData().getM_strBrand().toLowerCase())
				|| oVendorItemData.getM_oItemData().getM_strKeyword().toLowerCase().contains(oVendorItemData.getM_oItemData().getM_strArticleNumber().toLowerCase())
				|| oVendorItemData.getM_oItemData().getM_strKeyword().toLowerCase().contains(oVendorItemData.getM_oItemData().getM_strDetail().toLowerCase())))
				oItemDataCriteria.add(Restrictions.disjunction().add(Restrictions.ilike("m_strItemName", oVendorItemData.getM_oItemData().getM_strKeyword(), MatchMode.ANYWHERE))
						         .add(Restrictions.ilike("m_strBrand", oVendorItemData.getM_oItemData().getM_strKeyword(), MatchMode.ANYWHERE))
	 							 .add(Restrictions.ilike("m_strArticleNumber", oVendorItemData.getM_oItemData().getM_strKeyword(), MatchMode.ANYWHERE))
	 							 .add(Restrictions.ilike("m_strDetail", oVendorItemData.getM_oItemData().getM_strKeyword(), MatchMode.ANYWHERE)));
		if (oVendorItemData.getM_nMinimumPrice()>= 0)
			oItemDataCriteria.add(Restrictions.ge("m_nSellingPrice", oVendorItemData.getM_nMinimumPrice()));
		if (oVendorItemData.getM_nMaximumPrice()>= 500)
			oItemDataCriteria.add(Restrictions.le("m_nSellingPrice", oVendorItemData.getM_nMaximumPrice()));
		oCriteria.add(addItemVendorCriteria(oVendorItemData));
		oItemDataCriteria.add(addItemCategoryCriteria(oVendorItemData));
		return oCriteria;
	}

	private Disjunction addItemCategoryCriteria(VendorItemData oVendorItemData) 
	{
		Disjunction oDisjunction = Restrictions.disjunction();
		Iterator<ItemCategoryData> oIterator =oVendorItemData.getM_oItemCategories().iterator();
		while (oIterator.hasNext())
		{
			ItemCategoryData oItemCategoryData = oIterator.next();
			if (oItemCategoryData!= null && oItemCategoryData.getM_nCategoryId() > 0)
				oDisjunction.add (Restrictions.like ("m_oItemCategoryData", oItemCategoryData));
		}
		return oDisjunction;
	}
	
	private Disjunction addItemVendorCriteria(VendorItemData oVendorItemData) 
	{
		Disjunction oDisjunction = Restrictions.disjunction();
		Iterator<VendorData> oIterator =oVendorItemData.getM_oVendors().iterator();
		while (oIterator.hasNext())
		{
			VendorData oVendorData = oIterator.next();
			oVendorItemData.setM_oVendorData(oVendorData);
			if (oVendorData!= null && oVendorData.getM_nClientId() > 0)
				oDisjunction.add (Restrictions.like ("m_oVendorData", oVendorItemData.getM_oVendorData()));
		}
		return oDisjunction;
	}

	public GenericResponse getGroupWiseOnlineItems(ItemGroupData oGroupData) throws Exception
	{
		m_oLogger.info ("getGroupItems");
		m_oLogger.debug ("getGroupItems - oGroupData [IN] : " + oGroupData);
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		ItemData oItemData = new ItemData ();
        VendorItemData oVendorItemData = new VendorItemData ();
		try 
		{
			oVendorItemData.getM_oVendorData().setM_bVerified(true);
			oItemData.setM_oUserCredentialsData(getAdminUserInfo ());
			oItemData.setM_nCreatedBy(oItemData.getM_oUserCredentialsData().getM_nUserId());
			oItemData.setM_bPublishOnline(true);
			oVendorItemData.setM_oItemData(oItemData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oItemDataResponse.m_arrVendorItems = new ArrayList (oVendorItemData.list (oOrderBy));
			oItemDataResponse.m_arrVendorItems = buildItemForGroup (oItemDataResponse.m_arrVendorItems, oGroupData);
			oItemDataResponse.m_arrVendorItems = buildVendorItemData (oItemDataResponse.m_arrVendorItems);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getGroupItems - oException : " +oException);
			throw oException;
		}
		return oItemDataResponse;
	}

	private ArrayList<VendorItemData> buildItemForGroup(ArrayList<VendorItemData> arrVendorItems, ItemGroupData oGroupData) 
	{
		ArrayList<VendorItemData> arrVendorItemData = new ArrayList<VendorItemData> ();
		for (int nIndex=0; nIndex < arrVendorItems.size(); nIndex++)
		{
			Iterator<ItemGroupData>oIterator = arrVendorItems.get(nIndex).getM_oItemData().getM_oItemGroups().iterator();
			while (oIterator.hasNext())
			{
				ItemGroupData oItemGroupData = oIterator.next();
				if(oItemGroupData.getM_nItemGroupId()== oGroupData.getM_nItemGroupId())
					arrVendorItemData.add(arrVendorItems.get(nIndex));
			}
		}
		return arrVendorItemData;
	}	

}
