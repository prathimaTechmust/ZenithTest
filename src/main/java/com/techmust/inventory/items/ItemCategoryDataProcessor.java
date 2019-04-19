package com.techmust.inventory.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

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

@Controller
public class ItemCategoryDataProcessor extends GenericIDataProcessor<ItemCategoryData> 
{

	Logger oLogger = Logger.getLogger(ItemCategoryDataProcessor.class.getName());
	@RequestMapping(value="/ItemCategoryDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody ItemCategoryData oItemCategoryData) throws Exception  
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oItemCategoryData [IN] : " + oItemCategoryData);
		ItemCategoryDataResponse oItemCategoryDataResponse = new ItemCategoryDataResponse ();
		try
		{
			oItemCategoryDataResponse.m_bSuccess = oItemCategoryData.saveObject ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException :: " + oException);
		}
		return oItemCategoryDataResponse;
	}

	@RequestMapping(value="/itemCategoryDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody ItemCategoryData oItemCategoryData) throws Exception  
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oItemCategoryData [IN] : " + oItemCategoryData);
		ItemCategoryDataResponse oItemCategoryDataResponse = new ItemCategoryDataResponse ();
		try
		{
			oItemCategoryDataResponse.m_bSuccess = oItemCategoryData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		return oItemCategoryDataResponse;
	}

	@RequestMapping(value="/ItemCategoryDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody ItemCategoryData oItemCategoryData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oItemCategoryData [IN] : " + oItemCategoryData);
		ItemCategoryDataResponse ItemCategoryDataResponse = new ItemCategoryDataResponse ();
		try
		{
			oItemCategoryData = (ItemCategoryData)populateObject (oItemCategoryData);
			ItemCategoryDataResponse.m_arrItemCategory.add (oItemCategoryData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("get - oException : " + oException);
		}
		return ItemCategoryDataResponse;
	}

	@RequestMapping(value="/itemCategoryDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody ItemCategoryData oItemCategoryData) throws Exception 
	{
		String strXML = "";
		try
		{
			oItemCategoryData = (ItemCategoryData)populateObject (oItemCategoryData);
			strXML = oItemCategoryData.generateXML ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getXML - oException : " + oException);
		}
		m_oLogger.debug ("getXML - strXML [OUT] : " + strXML);
		return strXML;
	}

	@RequestMapping(value="/itemCategoryDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		return list (oTradeMustHelper.getM_oItemCategoryData(),oOrderBy,oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
	}
	
	public ItemCategoryDataResponse list (ItemCategoryData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " + oData);
		ItemCategoryDataResponse oItemCategoryDataResponse = new ItemCategoryDataResponse ();
		try
		{
			oItemCategoryDataResponse.m_nRowCount = getRowCount(oData);
			oItemCategoryDataResponse.m_arrItemCategory = new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize));
			oItemCategoryDataResponse.m_arrItemCategory = buildCategoryData (oItemCategoryDataResponse.m_arrItemCategory);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("list - oException : " + oException);
		}
		return oItemCategoryDataResponse;
	}

	private ArrayList<ItemCategoryData> buildCategoryData(ArrayList<ItemCategoryData> arrItemCategory) 
	{
		m_oLogger.info("buildCategoryData");
		for (int nIndex=0; nIndex < arrItemCategory.size(); nIndex++)
			arrItemCategory.get(nIndex).setM_strDate(getClientCompatibleFormat(arrItemCategory.get(nIndex).getM_dCreatedOn())); 
		return arrItemCategory;
	}

	@RequestMapping(value="/itemCategoryDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody ItemCategoryData oItemCategoryData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oItemCategoryData [IN] : " + oItemCategoryData);
		ItemCategoryDataResponse oItemCategoryDataResponse = new ItemCategoryDataResponse ();
		try
		{
			oItemCategoryDataResponse.m_bSuccess = oItemCategoryData.updateObject ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		return oItemCategoryDataResponse;
	}
	
	@RequestMapping(value="/itemCategoryDatagetCategorySuggesstions", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ItemCategoryDataResponse getCategorySuggesstions (@RequestBody TradeMustHelper oData) throws Exception
	{
		return getCategorySuggesstions(oData.getM_oItemCategoryData(), oData.getM_strColumn(), oData.getM_strOrderBy());
	}
	
	public ItemCategoryDataResponse getCategorySuggesstions (ItemCategoryData oItemCategoryData, String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getCategorySuggesstions");
		m_oLogger.debug ("getCategorySuggesstions - oItemCategoryData [IN] : " + oItemCategoryData);
		m_oLogger.debug ("getCategorySuggesstions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getCategorySuggesstions - strOrderBy [IN] : " + strOrderBy);
		ItemCategoryDataResponse oItemCategoryDataResponse = new ItemCategoryDataResponse ();
		try 
		{
			oItemCategoryDataResponse.m_arrItemCategory = new ArrayList (oItemCategoryData.listCustomData(this));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getCategorySuggesstions - oException : " +oException);
			throw oException;
		}
		return oItemCategoryDataResponse;
	}
	
	@Override
	public Criteria prepareCustomCriteria(Criteria oCriteria, ItemCategoryData oItemCategoryData) throws RuntimeException 
	{
		oCriteria.add (Restrictions.ilike ("m_strCategoryName", oItemCategoryData.getM_strCategoryName().trim(), MatchMode.START));
		oCriteria.setMaxResults(50);
		return oCriteria;
	}

	@Override
	public GenericResponse list(ItemCategoryData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
}
