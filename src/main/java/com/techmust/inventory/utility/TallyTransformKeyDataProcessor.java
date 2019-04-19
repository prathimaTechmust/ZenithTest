package com.techmust.inventory.utility;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

public class TallyTransformKeyDataProcessor extends GenericIDataProcessor<TallyTransformKeyData> 
{

	@Override
	public GenericResponse create(TallyTransformKeyData oTallyTransformKeyData) throws Exception 
	{
		m_oLogger.info("create");
		m_oLogger.debug("create - oTallyTransformKeyData [IN] : " + oTallyTransformKeyData);
		TallyTransformKeyDataResponse oTallyTransformKeyDataResponse = new TallyTransformKeyDataResponse ();
		try 
		{
			oTallyTransformKeyDataResponse.m_bSuccess = oTallyTransformKeyData.saveObject();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("create - oException : " + oException);
		}
		m_oLogger.debug ("create - oTallyTransformKeyDataResponse.m_bSuccess [OUT] : " + oTallyTransformKeyDataResponse.m_bSuccess);
		return oTallyTransformKeyDataResponse;
	}

	@Override
	public GenericResponse deleteData(TallyTransformKeyData oTallyTransformKeyData) throws Exception
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oTallyTransformKeyData.getM_nTallyTranformKeyId() [IN] : " + oTallyTransformKeyData.getM_nTallyTranformKeyId());
		TallyTransformKeyDataResponse oTallyTransformKeyDataResponse = new TallyTransformKeyDataResponse ();
		try
		{
			oTallyTransformKeyDataResponse.m_bSuccess = oTallyTransformKeyData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oTallyTransformKeyDataResponse.m_bSuccess [OUT] : " + oTallyTransformKeyDataResponse.m_bSuccess);
		return oTallyTransformKeyDataResponse;
	}

	@Override
	public GenericResponse get(TallyTransformKeyData oTallyTransformKeyData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("deleteData - oTallyTransformKeyData.getM_nTallyTranformKeyId() [IN] : " + oTallyTransformKeyData.getM_nTallyTranformKeyId());
		TallyTransformKeyDataResponse oTallyTransformKeyDataResponse = new TallyTransformKeyDataResponse ();
		try 
		{
			oTallyTransformKeyData = (TallyTransformKeyData) populateObject (oTallyTransformKeyData);
			oTallyTransformKeyDataResponse.m_arrTallyTransformKeyData.add (oTallyTransformKeyData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  + oException);
			throw oException;
		}
		return oTallyTransformKeyDataResponse;
	}

	@Override
	public String getXML(TallyTransformKeyData oTallyTransformKeyData) throws Exception 
	{
		oTallyTransformKeyData = (TallyTransformKeyData) populateObject(oTallyTransformKeyData);
	    return oTallyTransformKeyData != null ? oTallyTransformKeyData.generateXML () : "";
	}

	@Override
	public GenericResponse list(TallyTransformKeyData oTallyTransformKeyData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oTallyTransformKeyData, arrOrderBy, 0, 0);
	}
	@SuppressWarnings("unchecked")
	public GenericResponse list(TallyTransformKeyData oTallyTransformKeyData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oTallyTransformKeyData [IN] : " + oTallyTransformKeyData);
		m_oLogger.debug ("list - nPageNumber [IN] : " +nPageNumber);
		m_oLogger.debug ("list - nPageSize [IN] : " +nPageSize);
		TallyTransformKeyDataResponse oTallyTransformKeyDataResponse = new TallyTransformKeyDataResponse ();
		try 
		{
			oTallyTransformKeyDataResponse.m_nRowCount = getRowCount(oTallyTransformKeyData);
			oTallyTransformKeyDataResponse.m_arrTallyTransformKeyData = new ArrayList (oTallyTransformKeyData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oTallyTransformKeyDataResponse;
	}

	@Override
	public GenericResponse update(TallyTransformKeyData oTallyTransformKeyData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oTallyTransformKeyData.getM_nTallyTranformKeyId() [IN] : " + oTallyTransformKeyData.getM_nTallyTranformKeyId());
		TallyTransformKeyDataResponse oTallyTransformKeyDataResponse = new TallyTransformKeyDataResponse ();
		try
		{
			oTallyTransformKeyDataResponse.m_bSuccess = oTallyTransformKeyData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oTallyTransformKeyDataResponse.m_bSuccess [OUT] : " + oTallyTransformKeyDataResponse.m_bSuccess);
		return oTallyTransformKeyDataResponse;
	}

}
