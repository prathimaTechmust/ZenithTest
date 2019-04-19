package com.techmust.inventory.utility;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

public class TallyTranformDataProcessor extends GenericIDataProcessor<TallyTransformData> 
{

	@Override
	public GenericResponse create(TallyTransformData oTallyTransformData) throws Exception 
	{
		m_oLogger.info("create");
		m_oLogger.debug("create - oTallyTransformData [IN] : " + oTallyTransformData);
		TallyTransformDataResponse oTallyTransformDataResponse = new TallyTransformDataResponse ();
		try 
		{
			oTallyTransformDataResponse.m_bSuccess = oTallyTransformData.saveObject();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("create - oException : " + oException);
		}
		m_oLogger.debug ("create - oTallyTransformDataResponse.m_bSuccess [OUT] : " + oTallyTransformDataResponse.m_bSuccess);
		return oTallyTransformDataResponse;
	}

	@Override
	public GenericResponse deleteData(TallyTransformData oTallyTransformData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oTallyTransformData.getM_nTallyTranformId() [IN] : " + oTallyTransformData.getM_nTallyTranformId());
		TallyTransformDataResponse oTallyTransformDataResponse = new TallyTransformDataResponse ();
		try
		{
			oTallyTransformDataResponse.m_bSuccess = oTallyTransformData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oTallyTransformDataResponse.m_bSuccess [OUT] : " + oTallyTransformDataResponse.m_bSuccess);
		return oTallyTransformDataResponse;
	}

	@Override
	public GenericResponse get(TallyTransformData oTallyTransformData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oTallyTransformData.getM_nTallyTranformId() [IN] :" + oTallyTransformData.getM_nTallyTranformId());
		TallyTransformDataResponse oTallyTransformDataResponse = new TallyTransformDataResponse ();
		try 
		{
			oTallyTransformData = (TallyTransformData) populateObject (oTallyTransformData);
			oTallyTransformDataResponse.m_arrTallyTransformData.add (oTallyTransformData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  + oException);
			throw oException;
		}
		return oTallyTransformDataResponse;
	}

	@Override
	public String getXML(TallyTransformData oTallyTransformData) throws Exception 
	{
		oTallyTransformData = (TallyTransformData) populateObject(oTallyTransformData);
	    return oTallyTransformData != null ? oTallyTransformData.generateXML () : "";
	}

	public GenericResponse list(TallyTransformData oTallyTransformData, HashMap<String, String> arrOrderBy) throws Exception
	{
		return list (oTallyTransformData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse list(TallyTransformData oTallyTransformData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oTallyTransformData [IN] : " + oTallyTransformData);
		m_oLogger.debug ("list - nPageNumber [IN] : " +nPageNumber);
		m_oLogger.debug ("list - nPageSize [IN] : " +nPageSize);
		TallyTransformDataResponse oTallyTransformDataResponse = new TallyTransformDataResponse ();
		try 
		{
			oTallyTransformDataResponse.m_nRowCount = getRowCount(oTallyTransformData);
			oTallyTransformDataResponse.m_arrTallyTransformData = new ArrayList (oTallyTransformData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oTallyTransformDataResponse;
	}

	@Override
	public GenericResponse update(TallyTransformData oTallyTransformData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oTallyTransformData.getM_nTallyTranformId() [IN] : " + oTallyTransformData.getM_nTallyTranformId());
		TallyTransformDataResponse oTallyTransformDataResponse = new TallyTransformDataResponse ();
		try
		{
			oTallyTransformDataResponse.m_bSuccess = oTallyTransformData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oTallyTransformDataResponse.m_bSuccess [OUT] : " + oTallyTransformDataResponse.m_bSuccess);
		return oTallyTransformDataResponse;
	}

}
