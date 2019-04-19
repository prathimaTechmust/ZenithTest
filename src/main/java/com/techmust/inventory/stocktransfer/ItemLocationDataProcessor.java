package com.techmust.inventory.stocktransfer;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.items.ItemDataResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class ItemLocationDataProcessor extends GenericIDataProcessor<ItemLocationData> 
{
	@Override
	@RequestMapping(value="/itemLocationCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody ItemLocationData oData) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oData);
		ItemLocationDataResponse oItemLocationDataResponse = new ItemLocationDataResponse ();
		try
		{
			createLog(oData, "ItemLocationDataProcessor.create");
			oItemLocationDataResponse.m_bSuccess = oData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oItemLocationDataResponse.m_bSuccess [OUT] : " + oItemLocationDataResponse.m_bSuccess);
		return oItemLocationDataResponse;
	}

	@Override
	public GenericResponse deleteData(ItemLocationData oData) throws Exception
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData.getM_nId () [IN] : " + oData.getM_nId());
		ItemLocationDataResponse oItemLocationDataResponse = new ItemLocationDataResponse ();
		try
		{
			createLog(oData, "ItemLocationDataProcessor.update");
			oItemLocationDataResponse.m_bSuccess = oData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oItemLocationDataResponse.m_bSuccess [OUT] : " + oItemLocationDataResponse.m_bSuccess);
		return oItemLocationDataResponse;
	}

	@Override
	public GenericResponse get(ItemLocationData oData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData.getM_nId() [IN] :" + oData.getM_nId());
		ItemLocationDataResponse oItemLocationDataResponse = new ItemLocationDataResponse ();
		try 
		{
			oData = (ItemLocationData) populateObject (oData);
			oItemLocationDataResponse.m_arrItemLocation.add(oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oItemLocationDataResponse;
	}

	@Override
	public String getXML(ItemLocationData oData) throws Exception 
	{
	    m_oLogger.debug ("getXML - oData [IN] : "+ oData);
		try
		{
			oData = (ItemLocationData) populateObject(oData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " , oException);
		}
		return oData != null ? oData.generateXML () : "";
	}
	
	@RequestMapping(value="/itemLocationList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData) throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list(oData.getM_oItemLocation(), oOrderBy);
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericResponse list(ItemLocationData oData, HashMap<String, String> arrOrderBy) throws Exception
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " +oData);
		ItemLocationDataResponse oItemLocationDataResponse = new ItemLocationDataResponse ();
		try 
		{
			oItemLocationDataResponse.m_arrItemLocation = new ArrayList (oData.list (arrOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oItemLocationDataResponse;
	}

	@Override
	public GenericResponse update(ItemLocationData oData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData.getM_nId() [IN] : " + oData.getM_nId());
		ItemLocationDataResponse oItemLocationDataResponse = new ItemLocationDataResponse ();
		try
		{
			createLog(oData, "ItemLocationDataProcessor.update");
			oItemLocationDataResponse.m_bSuccess = oData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oItemLocationDataResponse.m_bSuccess [OUT] : " + oItemLocationDataResponse.m_bSuccess);
		return oItemLocationDataResponse;
	}
	
	@RequestMapping(value="/getInventorReport", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ItemDataResponse getInventoryReport (@RequestBody ItemLocationData oData)
	{
		m_oLogger.info ("getInventoryReport");
		m_oLogger.debug ("getInventoryReport - oData [IN] : " + oData);
		ItemDataResponse oResponse = new ItemDataResponse ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ItemLocationDataResponse oItemLocationDataResponse = (ItemLocationDataResponse) list(oData, oOrderBy);
			for (int nIndex = 0; nIndex < oItemLocationDataResponse.m_arrItemLocation.size(); nIndex++)
				oResponse.m_arrItems.add(oItemLocationDataResponse.m_arrItemLocation.get(nIndex).getM_oItemData());
		}
		catch (Exception oException)
		{
			m_oLogger.error("getInventoryReport - oException : " , oException);
		}
		return oResponse;
	}
	
	private void createLog (ItemLocationData oData, String strFunctionName) throws Exception
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
			throw oException;
		}
	}
}
