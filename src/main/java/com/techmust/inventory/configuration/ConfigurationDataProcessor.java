package com.techmust.inventory.configuration;

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

@Controller
public class ConfigurationDataProcessor extends GenericIDataProcessor<ConfigurationData> 
{
	@Override
	public GenericResponse create(ConfigurationData oConfigurationData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oConfigurationData - m_strKey [IN] : " + oConfigurationData.getM_strKey());
		ConfigurationDataResponse oDataResponse = new ConfigurationDataResponse ();
		try
		{	
			oDataResponse.m_bSuccess = oConfigurationData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		return oDataResponse;
	}

	@Override
	public GenericResponse deleteData(ConfigurationData oConfigurationData) throws Exception 
	{
		return null;
	}

	@RequestMapping(value="/configurationDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody ConfigurationData oConfigurationData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oConfigurationData - m_strKey [IN] : " + oConfigurationData.getM_strKey());
		ConfigurationDataResponse oDataResponse = new ConfigurationDataResponse ();
		try
		{	
			oConfigurationData = (ConfigurationData) populateObject (oConfigurationData);
			oDataResponse.m_arrConfigurationData.add(oConfigurationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("get - oException : " + oException);
			throw oException;
		}
		return oDataResponse;
	}

	@Override
	public String getXML(ConfigurationData oConfigurationData) throws Exception 
	{
		return null;
	}

	@RequestMapping(value="/configurationDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		return list(oTradeMustHelper.getM_oConfigurationData(),oOrderBy);
		
	}
	
	public GenericResponse list(ConfigurationData oConfigurationData,HashMap<String, String> arrOrderBy) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oConfigurationData - m_strKey [IN] : " + oConfigurationData.getM_strKey());
		ConfigurationDataResponse oDataResponse = new ConfigurationDataResponse ();
		try
		{	
			oDataResponse.m_arrConfigurationData = new ArrayList (oConfigurationData.list (arrOrderBy));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("list - oException : " + oException);
			throw oException;
		}
		return oDataResponse;
	}

	@Override
	public GenericResponse update(ConfigurationData oConfigurationData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oConfigurationData - m_strKey [IN] : " + oConfigurationData.getM_strKey());
		ConfigurationDataResponse oDataResponse = new ConfigurationDataResponse ();
		try
		{	
			oDataResponse.m_bSuccess = oConfigurationData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		return oDataResponse;
	}
	
	@RequestMapping(value="/configurationDataSetDefaultVendor", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse setDefaultVendor(@RequestBody ConfigurationData oConfigurationData) throws Exception 
	{
		m_oLogger.info ("setDefaultVendor");
		m_oLogger.debug ("setDefaultVendor - oConfigurationData - m_strKey [IN] : " + oConfigurationData.getM_strKey());
		ConfigurationDataResponse oDataResponse = new ConfigurationDataResponse ();
		try
		{	
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oDataResponse = (ConfigurationDataResponse) list(oConfigurationData, oOrderBy);
			if(!(oDataResponse.m_arrConfigurationData.size() > 0))
				oDataResponse = (ConfigurationDataResponse) create(oConfigurationData);
			else
			{
				ConfigurationData oData = oDataResponse.m_arrConfigurationData.get(0);
				oData.setM_nIntValue(oConfigurationData.getM_nIntValue());
				oDataResponse = (ConfigurationDataResponse) update(oData);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("setDefaultVendor - oException : " + oException);
			throw oException;
		}
		return oDataResponse;
	}
}
