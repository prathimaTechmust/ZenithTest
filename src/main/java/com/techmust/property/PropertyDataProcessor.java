package com.techmust.property;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
@Controller
public class PropertyDataProcessor extends GenericIDataProcessor<PropertyData> 
{
	@RequestMapping(value="/propertyDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(PropertyData oPropertyData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oPropertyData [IN] : " + oPropertyData);
		PropertyDataResponse oPropertyDataResponse = new PropertyDataResponse ();
		try
		{
			oPropertyDataResponse.m_bSuccess = oPropertyData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oPropertyDataResponse.m_bSuccess [OUT] : " + oPropertyDataResponse.m_bSuccess);
		return oPropertyDataResponse;
	}

	@RequestMapping(value="/propertyDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(PropertyData oPropertyData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oPropertyData.getM_nPropertyId() [IN] : " + oPropertyData.getM_nPropertyId());
		PropertyDataResponse oPropertyDataResponse = new PropertyDataResponse ();
		try
		{
			oPropertyDataResponse.m_bSuccess = oPropertyData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oPropertyDataResponse.m_bSuccess [OUT] : " + oPropertyDataResponse.m_bSuccess);
		return oPropertyDataResponse;
	}

	@RequestMapping(value="/propertyDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(PropertyData oPropertyData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oPropertyData.getM_nPropertyId() [IN] :" +oPropertyData.getM_nPropertyId());
		PropertyDataResponse oPropertyDataResponse = new PropertyDataResponse ();
		try 
		{
			oPropertyData = (PropertyData) populateObject (oPropertyData);
			oPropertyDataResponse.m_arrProperty.add (oPropertyData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oPropertyDataResponse;
	}

	@RequestMapping(value="/propertyDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(PropertyData oPropertyData) throws Exception 
	{
		return null;
	}
	
//	@RequestMapping(value="/propertyDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
//	@ResponseBody
//	public GenericResponse list(TradeMustHelper oData) throws Exception 
//	{
//		return list (oData.getM_oPropertyData(), oData.getM_strColumn(), oData.getM_strOrderBy(), 0, 0);
//	}
	
	public GenericResponse list(PropertyData oPropertyData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oPropertyData [IN] : " +oPropertyData);
		PropertyDataResponse oPropertyDataResponse = new PropertyDataResponse ();
		try 
		{
			oPropertyDataResponse.m_nRowCount = getRowCount(oPropertyData);
			oPropertyDataResponse.m_arrProperty = new ArrayList (oPropertyData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oPropertyDataResponse;
	}

	@RequestMapping(value="/propertyDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(PropertyData oPropertyData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oPropertyData.getM_nPropertyId() [IN] : " + oPropertyData.getM_nPropertyId());
		PropertyDataResponse oPropertyDataResponse = new PropertyDataResponse ();
		try
		{
			oPropertyDataResponse.m_bSuccess = oPropertyData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oPropertyDataResponse.m_bSuccess [OUT] : " + oPropertyDataResponse.m_bSuccess);
		return oPropertyDataResponse;
	}

	@Override
	public GenericResponse list(PropertyData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
