package com.techmust.inventory.sales;

import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.items.ItemData;
@Controller
public class CustomizedItemDataProcessor extends GenericIDataProcessor<CustomizedItemData> 
{
	@Override
	public GenericResponse create(CustomizedItemData oCustomizedItemData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oCustomizedItemData [IN] : " + oCustomizedItemData);
		CustomizedItemDataResponse oCustomizedDataResponse = new CustomizedItemDataResponse ();
		try
		{
			oCustomizedItemData.prepareCustomizedData ();
			oCustomizedDataResponse.m_bSuccess = oCustomizedItemData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oCustomizedDataResponse.m_bSuccess [OUT] : " + oCustomizedDataResponse.m_bSuccess);
		return oCustomizedDataResponse;
	}

	@Override
	public GenericResponse deleteData(CustomizedItemData oCustomizedItemData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oCustomizedItemData.getM_nCustomizeId() [IN] : " + oCustomizedItemData.getM_nCustomizeId());
		CustomizedItemDataResponse oCustomizedItemDataResponse = new CustomizedItemDataResponse ();
		try
		{
			oCustomizedItemDataResponse.m_bSuccess = oCustomizedItemData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oCustomizedItemDataResponse.m_bSuccess [OUT] : " + oCustomizedItemDataResponse.m_bSuccess);
		return oCustomizedItemDataResponse;
	}

	@Override
	public GenericResponse get(CustomizedItemData oCustomizedItemData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oCustomizedItemData.getM_nCustomizeId() [IN] :" +oCustomizedItemData.getM_nCustomizeId());
		CustomizedItemDataResponse oCustomizedItemDataResponse = new CustomizedItemDataResponse ();
		try 
		{
			oCustomizedItemData = (CustomizedItemData) populateObject (oCustomizedItemData);
			oCustomizedItemDataResponse.m_arrCustomizeItemData.add (oCustomizedItemData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oCustomizedItemDataResponse;
	}

	@Override
	public String getXML(CustomizedItemData oCustomizedItemData) throws Exception 
	{
		oCustomizedItemData = (CustomizedItemData) populateObject(oCustomizedItemData);
	    return oCustomizedItemData != null ? oCustomizedItemData.generateXML () : "";
	}

//	@RequestMapping(value="/CustomizedItemDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
//	@ResponseBody
//	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
//	{
//		GenericResponse oCustomizedItemDataResponse = list(oTradeMustHelper.getM_oCustomizedItemData(), oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
//		return oCustomizedItemDataResponse;
//	}
	
	public GenericResponse list(CustomizedItemData oCustomizedItemData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oCustomizedItemData [IN] : " +oCustomizedItemData);
		CustomizedItemDataResponse oCustomizedItemDataResponse = new CustomizedItemDataResponse ();
		try 
		{
			oCustomizedItemDataResponse.m_arrCustomizeItemData = new ArrayList (oCustomizedItemData.list (arrOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oCustomizedItemDataResponse;
	}

	@Override
	public GenericResponse update(CustomizedItemData oCustomizedItemData) throws Exception
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oCustomizedItemData.getM_nCustomizeId() [IN] : " + oCustomizedItemData.getM_nCustomizeId());
		CustomizedItemDataResponse oCustomizedItemDataResponse = new CustomizedItemDataResponse ();
		try
		{
			oCustomizedItemData.prepareCustomizedData ();
			CustomizedItemData oCustomItemData = CustomizedItemData.getItemInstance(oCustomizedItemData.getM_strClientArticleNumber(), oCustomizedItemData.getM_oClientData().getM_nClientId(), oCustomizedItemData.getM_oItemData().getM_nItemId());
			if(oCustomItemData != null)
				oCustomizedItemDataResponse.m_bSuccess = oCustomizedItemData.updateObject();
			else
			{
				oCustomItemData = CustomizedItemData.getItemInstance(oCustomizedItemData.getM_strClientArticleNumber(), oCustomizedItemData.getM_oClientData().getM_nClientId(), -1);
				if(oCustomItemData != null)
				{
					oCustomItemData.deleteObject();
					oCustomizedItemDataResponse.m_bSuccess = oCustomizedItemData.saveObject();
				}
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oCustomizedItemDataResponse.m_bSuccess [OUT] : " + oCustomizedItemDataResponse.m_bSuccess);
		return oCustomizedItemDataResponse;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ItemData> getArticleSuggestions(int nClientId,	ItemData oItemData) throws Exception 
	{
		CustomizedItemData oCustomizedItemData = new CustomizedItemData ();
		oCustomizedItemData.getM_oClientData().setM_nClientId(nClientId);
		oCustomizedItemData.setM_strClientArticleNumber(oItemData.getM_strArticleNumber());
		oCustomizedItemData.setM_strClientArticleDescription(oItemData.getM_strItemName());
		ArrayList<CustomizedItemData> arrCustomizedItemData =  new ArrayList (oCustomizedItemData.listCustomData(this));
		return extractItems(arrCustomizedItemData);
	}
	
	public ArrayList<ItemData> extractItems(ArrayList<CustomizedItemData> arrCustomizedItemData) 
	{
		ArrayList<ItemData> arrItems = new ArrayList<ItemData> ();
		for(int nIndex = 0; nIndex < arrCustomizedItemData.size(); nIndex++)
		{
			CustomizedItemData oCustomizedItemData = arrCustomizedItemData.get(nIndex);
			ItemData oItemData = oCustomizedItemData.getM_oItemData();
			oItemData.setM_strArticleNumber(oCustomizedItemData.getM_strClientArticleNumber());
			oItemData.setM_strItemName(CustomizedItemData.getArticleDescription(oCustomizedItemData.getM_strClientArticleDescription(), 0, oItemData.getM_strItemName()));
			oItemData.setM_strDetail(CustomizedItemData.getArticleDescription(oCustomizedItemData.getM_strClientArticleDescription(), 1, oItemData.getM_strDetail ()));
			arrItems.add(oItemData);
		}
		return arrItems;
	}
	
	@Override
	public Criteria prepareCustomCriteria(Criteria oCriteria, CustomizedItemData oCustomizedItemData) throws RuntimeException 
	{
		if(oCustomizedItemData.getM_oClientData() != null && oCustomizedItemData.getM_oClientData().getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oClientData", oCustomizedItemData.getM_oClientData()));
		if(oCustomizedItemData.getM_strClientArticleNumber().trim().length() > 0 && oCustomizedItemData.getM_strClientArticleDescription().trim().length() > 0)
	 		oCriteria.add(Restrictions.disjunction().add(Restrictions.ilike("m_strClientArticleNumber", oCustomizedItemData.getM_strClientArticleNumber().trim(), MatchMode.ANYWHERE))
	 												.add(Restrictions.ilike("m_strClientArticleDescription", oCustomizedItemData.getM_strClientArticleDescription().trim(), MatchMode.ANYWHERE)));
		else if(oCustomizedItemData.getM_strClientArticleNumber().trim() != "")
			oCriteria.add(Restrictions.ilike("m_strClientArticleNumber", oCustomizedItemData.getM_strClientArticleNumber().trim(), MatchMode.ANYWHERE));
		else if(oCustomizedItemData.getM_strClientArticleDescription().trim() != "")
			oCriteria.add(Restrictions.ilike("m_strClientArticleDescription", oCustomizedItemData.getM_strClientArticleDescription().trim(), MatchMode.ANYWHERE));
	 	oCriteria.setMaxResults(10);
		return oCriteria;
	}
}
