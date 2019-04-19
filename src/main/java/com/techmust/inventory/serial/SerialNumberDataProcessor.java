package com.techmust.inventory.serial;

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
public class SerialNumberDataProcessor extends GenericIDataProcessor<SerialNumberData> 
{
	
	@RequestMapping(value="/SerialNumberCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody SerialNumberData oData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oData);
		SerialNumberDataResponse oSerialNumberDataResponse = new SerialNumberDataResponse ();
		try
		{
			oSerialNumberDataResponse.m_bSuccess = oData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oSerialNumberDataResponse.m_bSuccess [OUT] : " + oSerialNumberDataResponse.m_bSuccess);
		return oSerialNumberDataResponse;
	}

	@RequestMapping(value="/SerialNumberDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody SerialNumberData oData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " + oData);
		SerialNumberDataResponse oSerialNumberDataResponse = new SerialNumberDataResponse ();
		try
		{
			oSerialNumberDataResponse.m_bSuccess = oData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oSerialNumberDataResponse.m_bSuccess [OUT] : " + oSerialNumberDataResponse.m_bSuccess);
		return oSerialNumberDataResponse;
		
	}

	@Override
	@RequestMapping(value="/SerialNumberGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody SerialNumberData oData) throws Exception 
	{
		m_oLogger.info ("get");
		SerialNumberDataResponse oSerialNumberDataResponse = new SerialNumberDataResponse ();
		try 
		{
			oData = (SerialNumberData) populateObject (oData);
			oSerialNumberDataResponse.m_arrSerialNumber.add(oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oSerialNumberDataResponse;
	}

	@Override
	@RequestMapping(value="/SerialNumberGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody SerialNumberData oData) throws Exception 
	{
		oData = (SerialNumberData) populateObject(oData);
	    return oData != null ? oData.generateXML () : "";
	}

	@RequestMapping(value="/SerialNumberList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		return list(oTradeMustHelper.getM_oSerialNumber(), oOrderBy);
	}
	
	public GenericResponse list(SerialNumberData oData, HashMap<String, String> arrOrderBy)throws Exception 
	{
		m_oLogger.info("list");
		m_oLogger.debug("list - oData[IN] :" + oData);      
		SerialNumberDataResponse oSerialNumberDataResponse = new SerialNumberDataResponse ();
		try 
		{
			oSerialNumberDataResponse.m_arrSerialNumber = new ArrayList (oData.list (arrOrderBy));
			setSerialName(oSerialNumberDataResponse.m_arrSerialNumber);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oSerialNumberDataResponse;
	}

	private void setSerialName(ArrayList<SerialNumberData> arrSerialNumber)
	{
		for(int nIndex = 0; nIndex < arrSerialNumber.size(); nIndex++)
		{
			arrSerialNumber.get(nIndex).setM_strSerialTypeName(arrSerialNumber.get(nIndex).getM_nSerialType().name());
		}
	}

	@Override
	@RequestMapping(value="/SerialNumberUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody SerialNumberData oData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData [IN] : " + oData);
		SerialNumberDataResponse oSerialNumberDataResponse = new SerialNumberDataResponse ();
		try
		{
			oSerialNumberDataResponse.m_bSuccess = oData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oSerialNumberDataResponse.m_bSuccess [OUT] : " + oSerialNumberDataResponse.m_bSuccess);
		return oSerialNumberDataResponse;
	}
	
	public GenericResponse updateSerialNumbers(SerialNumberData [] arrSerialNumbers) throws Exception
	{
		m_oLogger.info ("updateSerialNumbers");
		m_oLogger.debug ("updateSerialNumbers - oSerialNumberData [IN] : " + arrSerialNumbers);
		SerialNumberDataResponse oSerialNumberDataResponse = new SerialNumberDataResponse ();
		try
		{
			for (int nIndex=0 ; nIndex < arrSerialNumbers.length ; nIndex++)
				oSerialNumberDataResponse = (SerialNumberDataResponse) update (arrSerialNumbers[nIndex]);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateSerialNumbers - oException : " + oException);
			throw oException;
		}
		return oSerialNumberDataResponse;
	}
	
	public SerialNumberData getSerialNumberData (SerialType oSerialType)
	{
		SerialNumberDataProcessor oSerialNumberDataProcessor = new SerialNumberDataProcessor ();
		SerialNumberData oSerialNumberData = new SerialNumberData ();
		SerialNumberDataResponse oSerialNumberDataResponse = new SerialNumberDataResponse ();
		SerialNumberData oNumberData = null;
		try 
		{
			oSerialNumberData.setM_nSerialType(oSerialType);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oSerialNumberDataResponse = (SerialNumberDataResponse) oSerialNumberDataProcessor.list(oSerialNumberData, oOrderBy);
			if(oSerialNumberDataResponse.m_arrSerialNumber.size() > 0 )
				 oNumberData = oSerialNumberDataResponse.m_arrSerialNumber.get(0);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getInvoiceNumber - oException : " + oException);
		}
		return oNumberData;
	}
	 
	public String getInvoiceNumber(SerialType oSerialType) throws Exception
    {
    	m_oLogger.info ("getInvoiceNumber");
    	String strSerialNumber= "";
       	try 
    	{
    			SerialNumberData oSerialNumberData = getSerialNumberData (oSerialType);
    			strSerialNumber = oSerialNumberData.getM_strPrefix() + ":" + oSerialNumberData.getM_nSerialNumber();
    			oSerialNumberData.setM_nSerialNumber(oSerialNumberData.getM_nSerialNumber() +1);
    			oSerialNumberData.updateObject();
    	}
    	catch (Exception oException)
		{
    		m_oLogger.error ("getInvoiceNumber - oException : " + oException);
    		throw oException;
		}
		return strSerialNumber;
	}
}
