package com.techmust.inventory.quotation.logs;

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
public class QuotationLogDataProcessor extends GenericIDataProcessor<QuotationLogData> 
{
	@Override
	@RequestMapping(value="/quotationLogDatacreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody QuotationLogData oQuotationLogData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oQuotationLogData [IN] : " + oQuotationLogData);
		QuotationLogDataResponse oQuotationLogDataResponse = new QuotationLogDataResponse ();
		try
		{
			oQuotationLogDataResponse.m_bSuccess = oQuotationLogData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oQuotationLogDataResponse.m_bSuccess [OUT] : " + oQuotationLogDataResponse.m_bSuccess);
		return oQuotationLogDataResponse;
	}

	@Override
	public GenericResponse deleteData(QuotationLogData oQuotationLogData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oQuotationLogData.getM_nId() [IN] : " + oQuotationLogData.getM_nId());
		QuotationLogDataResponse oQuotationLogDataResponse = new QuotationLogDataResponse ();
		try
		{
			oQuotationLogDataResponse.m_bSuccess = oQuotationLogData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oQuotationLogDataResponse.m_bSuccess [OUT] : " + oQuotationLogDataResponse.m_bSuccess);
		return oQuotationLogDataResponse;
	}

	@Override
	public GenericResponse get(QuotationLogData oQuotationLogData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oQuotationLogData.getM_nId() [IN] :" +oQuotationLogData.getM_nId());
		QuotationLogDataResponse oQuotationLogDataResponse = new QuotationLogDataResponse ();
		try 
		{
			oQuotationLogData = (QuotationLogData) populateObject (oQuotationLogData);
			oQuotationLogDataResponse.m_arrQuotationLogs.add (oQuotationLogData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oQuotationLogDataResponse;
	}

	@Override
	public String getXML(QuotationLogData oQuotationLogData) throws Exception 
	{
		oQuotationLogData = (QuotationLogData) populateObject(oQuotationLogData);
	    return oQuotationLogData != null ? oQuotationLogData.generateXML () : "";
	}
	
	@RequestMapping(value="/quotationLogDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oQuotationLogData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}

	@Override
	public GenericResponse list(QuotationLogData oQuotationLogData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oQuotationLogData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
    public GenericResponse list(QuotationLogData oQuotationLogData,HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
            throws Exception
    {
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oQuotationLogData [IN] : " +oQuotationLogData);
		QuotationLogDataResponse oQuotationLogDataResponse = new QuotationLogDataResponse ();
		try 
		{
			oQuotationLogDataResponse.m_nRowCount = getRowCount(oQuotationLogData);
			oQuotationLogDataResponse.m_arrQuotationLogs = new ArrayList (oQuotationLogData.list (arrOrderBy, nPageNumber, nPageSize));
			oQuotationLogDataResponse.m_arrQuotationLogs = buildQuotationLogData (oQuotationLogDataResponse.m_arrQuotationLogs);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oQuotationLogDataResponse;
    }

	@Override
	public GenericResponse update(QuotationLogData oQuotationLogData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oQuotationLogData.getM_nId() [IN] : " + oQuotationLogData.getM_nId());
		QuotationLogDataResponse oQuotationLogDataResponse = new QuotationLogDataResponse ();
		try
		{
			oQuotationLogDataResponse.m_bSuccess = oQuotationLogData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oTaxDataResponse.m_bSuccess [OUT] : " + oQuotationLogDataResponse.m_bSuccess);
		return oQuotationLogDataResponse;
	}
	
	private ArrayList<QuotationLogData> buildQuotationLogData(ArrayList<QuotationLogData> arrQuotationLogData) 
	{
		m_oLogger.info("buildQuotationLogData");
		for (int nIndex=0; nIndex < arrQuotationLogData.size(); nIndex++)
		{
			arrQuotationLogData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrQuotationLogData.get(nIndex).getM_dDate()));
			arrQuotationLogData.get(nIndex).setM_strTime(arrQuotationLogData.get(nIndex).getM_dDate());
		}
		return arrQuotationLogData;
	}
}
