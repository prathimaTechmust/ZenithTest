package com.techmust.inventory.paymentsandreceipt;

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
import com.techmust.inventory.purchase.PurchaseDataResponse;
@Controller
public class TransactionModeDataProcessor extends GenericIDataProcessor<TransactionMode> 
{

	@RequestMapping(value="/TransactionModeCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody TransactionMode oTransactionMode) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oTransactionMode [IN] : " + oTransactionMode);
		TransactionModeDataResponse oModeDataResponse = new TransactionModeDataResponse ();
		try
		{	
			oModeDataResponse.m_bSuccess = oTransactionMode.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oModeDataResponse.m_bSuccess [OUT] : " + oModeDataResponse.m_bSuccess);
		return oModeDataResponse;
	}
	
	@RequestMapping(value="/TransactionModeDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody TransactionMode oTransactionMode) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oTransactionMode.getM_nModeId () [IN] : " + oTransactionMode.getM_nModeId());
		TransactionModeDataResponse oModeDataResponse = new TransactionModeDataResponse ();
		try
		{
			oTransactionMode = (TransactionMode) populateObject (oTransactionMode);
			oModeDataResponse.m_bSuccess = oTransactionMode.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oModeDataResponse.m_bSuccess [OUT] : " + oModeDataResponse.m_bSuccess);
		return oModeDataResponse;
	}

	@RequestMapping(value="/TransactionModeGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody TransactionMode oTransactionMode) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oTransactionMode.getM_nModeId() [IN] :" + oTransactionMode.getM_nModeId());
		TransactionModeDataResponse oModeDataResponse = new TransactionModeDataResponse ();
		try 
		{
			oTransactionMode = (TransactionMode) populateObject (oTransactionMode);
			oModeDataResponse.m_arrTransactionMode.add (oTransactionMode);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oModeDataResponse;
	}

	@RequestMapping(value="/TransactionModeGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody TransactionMode oTransactionMode) throws Exception 
	{
		m_oLogger.info ("getXML : ");
		m_oLogger.debug ("getXML - oTransactionMode [IN] : " +oTransactionMode);
		String strXml = "";
		try 
		{
			oTransactionMode = (TransactionMode) populateObject(oTransactionMode);
			strXml = oTransactionMode != null ? oTransactionMode.generateXML () : "";
		}
		catch (Exception oException)
		{                                                      
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}

	@RequestMapping(value="/TransactionModeList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		GenericResponse oModeDataResponse = list(oTradeMustHelper.getM_oTransactionMode(), oOrderBy);
		return oModeDataResponse;
	}
	public GenericResponse list(TransactionMode oTransactionMode, HashMap<String, String> arrOrderBy) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oTransactionMode [IN] : " + oTransactionMode);
		TransactionModeDataResponse oModeDataResponse = new TransactionModeDataResponse ();
		try 
		{
			oModeDataResponse.m_arrTransactionMode = new ArrayList (oTransactionMode.list (arrOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
			throw oException;
		}
		return oModeDataResponse;
	}

	@RequestMapping(value="/TransactionModeUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody TransactionMode oTransactionMode) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oTransactionMode.getM_nModeId() [IN] : " + oTransactionMode.getM_nModeId());
		TransactionModeDataResponse oModeDataResponse = new TransactionModeDataResponse ();
		try
		{
			oModeDataResponse.m_bSuccess = oTransactionMode.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oModeDataResponse.m_bSuccess [OUT] : " + oModeDataResponse.m_bSuccess);
		return oModeDataResponse;
	}

}
