package com.techmust.inventory.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class ItemGroupDataProcessor extends GenericIDataProcessor<ItemGroupData> 
{
	@RequestMapping(value="/itemGroupDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody ItemGroupData oItemGroupData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oItemGroupData - m_nItemGroupId [IN] : " + oItemGroupData.getM_nItemGroupId());
		ItemGroupDataResponse oItemGroupDataResponse = new ItemGroupDataResponse ();
		try
		{	
			createLog(oItemGroupData, "ItemGroupDataProcessor.create : " + oItemGroupData.getM_strGroupName());
			isvalidUser (oItemGroupData);
			HashSet<ItemData> oGroupItems = new HashSet<ItemData> ();
			oGroupItems.addAll (buildItemGroupList (oItemGroupData.m_arrGroupItems));
			oGroupItems.remove (null);
			oItemGroupData.setM_oGroupItems(oGroupItems) ;
			oItemGroupDataResponse.m_bSuccess = oItemGroupData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oItemGroupDataResponse.m_bSuccess [OUT] : " + oItemGroupDataResponse.m_bSuccess);
		return oItemGroupDataResponse;
	}

	@RequestMapping(value="/itemGroupDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody ItemGroupData oItemGroupData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oItemGroupData.getM_nItemId() [IN] : " + oItemGroupData.getM_nItemGroupId());
		ItemGroupDataResponse oItemGroupDataResponse = new ItemGroupDataResponse ();
		try
		{
			createLog(oItemGroupData, "ItemGroupDataProcessor.deleteData : " + oItemGroupData.getM_strGroupName());
			isvalidUser (oItemGroupData);
			oItemGroupData = (ItemGroupData) populateObject (oItemGroupData);
			oItemGroupDataResponse.m_bSuccess = oItemGroupData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oItemDataResponse.m_bSuccess [OUT] : " + oItemGroupDataResponse.m_bSuccess);
		return oItemGroupDataResponse;
	}

	@RequestMapping(value="/itemGroupDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody ItemGroupData oItemGroupData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oItemGroupData.getM_nItemGroupId() [IN] :" + oItemGroupData.getM_nItemGroupId());
		ItemGroupDataResponse oItemGroupDataResponse = new ItemGroupDataResponse ();
		try 
		{
			//isvalidUser (oItemGroupData);
			oItemGroupData = (ItemGroupData) populateObject (oItemGroupData);
			oItemGroupDataResponse.m_arrItemGroupData.add (oItemGroupData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oItemGroupDataResponse;
	}
	
	public GenericResponse getOnlineItems(ItemGroupData oItemGroupData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oItemGroupData.getM_nItemGroupId() [IN] :" + oItemGroupData.getM_nItemGroupId());
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		try 
		{
			oItemGroupData = (ItemGroupData) populateObject (oItemGroupData);
			oItemDataResponse.m_arrItems = buildGroupItemData (oItemGroupData.getM_oGroupItems());
			oItemDataResponse.m_nRowCount = oItemDataResponse.m_arrItems.size();
			
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oItemDataResponse;
	}
	
	private ArrayList<ItemData> buildGroupItemData(Set<ItemData> m_oItemData) 
	{
		ArrayList<ItemData> arrItemData = new ArrayList<ItemData>(m_oItemData);
		ArrayList<ItemData> arrPublishItemData = new ArrayList<ItemData> ();
		for (int nIndex=0; nIndex < arrItemData.size(); nIndex++)
		{
			if(arrItemData.get(nIndex).isM_bPublishOnline())
			{
				arrItemData.get(nIndex).setM_buffImgPhoto(getBufferedImage (arrItemData.get(nIndex).getM_oItemPhoto()));
				arrPublishItemData.add(arrItemData.get(nIndex));
				
			}
		}
		return arrPublishItemData;
	}
	
	@RequestMapping(value="/itemGroupDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody ItemGroupData oItemGroupData) throws Exception 
	{
		m_oLogger.info ("getXML : ");
		m_oLogger.debug ("getXML - oItemGroupData [IN] : " +oItemGroupData);
		String strXml = "";
		try 
		{
			isvalidUser (oItemGroupData);
			oItemGroupData = (ItemGroupData) populateObject(oItemGroupData);
			strXml = oItemGroupData != null ? oItemGroupData.generateXML () : "";
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}

	@RequestMapping(value="/itemGroupDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{

		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
       return list (oTradeMustHelper.getM_oItemGroupData(), oOrderBy, oTradeMustHelper.getM_nPageNo(), oTradeMustHelper.getM_nPageSize());
	}
	
	public GenericResponse list(ItemGroupData oItemGroupData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception
    {
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oItemData [IN] : " + oItemGroupData);
		ItemGroupDataResponse oItemGroupDataResponse = new ItemGroupDataResponse ();
		try 
		{
			//isvalidUser (oItemGroupData);
			oItemGroupDataResponse.m_nRowCount = getRowCount(oItemGroupData);
			oItemGroupDataResponse.m_arrItemGroupData = new ArrayList (oItemGroupData.list (arrOrderBy, nPageNumber, nPageSize));
			oItemGroupDataResponse.m_arrItemGroupData = buildItemGroupData (oItemGroupDataResponse.m_arrItemGroupData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
			throw oException;
		}
		return oItemGroupDataResponse;
    }
	
	@RequestMapping(value="/itemGroupDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody ItemGroupData oItemGroupData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oItemGroupData.getM_nItemId() [IN] : " + oItemGroupData.getM_nItemGroupId());
		ItemGroupDataResponse oItemGroupDataResponse = new ItemGroupDataResponse ();
		try
		{
			createLog(oItemGroupData, "ItemGroupDataProcessor.update : " + oItemGroupData.getM_strGroupName());
			isvalidUser (oItemGroupData);
			HashSet<ItemData> oGroupItems = new HashSet<ItemData> ();
			oGroupItems.addAll (buildItemGroupList (oItemGroupData.m_arrGroupItems));
			oGroupItems.remove (null);
			oItemGroupData.setM_oGroupItems(oGroupItems) ;
			oItemGroupDataResponse.m_bSuccess = oItemGroupData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oItemGroupDataResponse.m_bSuccess [OUT] : " + oItemGroupDataResponse.m_bSuccess);
		return oItemGroupDataResponse;
	}

	private Collection<ItemData> buildItemGroupList (ItemData [] arrItemData)
    {
		m_oLogger.info ("buildItemGroupList");
		m_oLogger.debug ("buildItemGroupList - arrActionData.length [IN] : " + arrItemData != null ? arrItemData.length : 0);
		ArrayList<ItemData> oArrayList = new ArrayList<ItemData> ();
		try
		{
			for (int nIndex = 0; arrItemData != null && nIndex < arrItemData.length; nIndex++)
				oArrayList.add(arrItemData [nIndex]);
		}
		catch(Exception oException)
		{
			m_oLogger.error ("buildItemGroupList - oException : " +oException);
		}
		return oArrayList;
    }
	
	private ArrayList<ItemGroupData> buildItemGroupData(ArrayList<ItemGroupData> arrItemGroupData) 
	{
		m_oLogger.info("buildSalesData");
		for (int nIndex=0; nIndex < arrItemGroupData.size(); nIndex++)
			arrItemGroupData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrItemGroupData.get(nIndex).getM_dCreatedOn())); 
		return arrItemGroupData;
	}
	
	private void isvalidUser (ItemGroupData oItemGroupData) throws Exception
	{
		m_oLogger.info("isvalidUser");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oItemGroupData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oItemGroupData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}
	
	private void createLog (ItemGroupData oItemGroupData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oItemGroupData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oItemGroupData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
	
	public GenericResponse getOnlineItemGroupName(ItemGroupData oItemGroupData) throws Exception 
	{
		m_oLogger.info ("getItems");
		m_oLogger.debug ("getItems - oItemGroupData.getM_nItemGroupId() [IN] :" + oItemGroupData.getM_nItemGroupId());
		ItemGroupDataResponse oItemGroupDataResponse = new ItemGroupDataResponse ();
		try 
		{
			oItemGroupDataResponse.m_arrItemGroupData =  new ArrayList (oItemGroupData.listCustomData(this));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oItemGroupDataResponse;
	}
	
	@SuppressWarnings("deprecation")
	public Criteria prepareCustomCriteria(Criteria oCriteria,ItemGroupData oItemGroupData) 
	{
		DetachedCriteria oSubquery = DetachedCriteria.forClass(VendorItemData.class)
		.createAlias("m_oItemData", "ItemData")
		.createAlias("ItemData.m_oItemGroups", "ItemGroups")
		.setProjection( Property.forName("ItemGroups.m_nItemGroupId"));
		oCriteria.add(Subqueries.propertyIn("m_nItemGroupId", oSubquery)); 
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("m_strGroupName"))
						.add(Projections.groupProperty("m_nItemGroupId"));
		oCriteria.setProjection(oProjectionList);
		return oCriteria;
	}

	@Override
	public GenericResponse list(ItemGroupData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}