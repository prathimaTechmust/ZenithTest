package com.techmust.inventory.purchase;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

public class PurchaseToLocationDataProcessor extends GenericIDataProcessor<PurchaseToLocationData> 
{

	@Override
	public GenericResponse create(PurchaseToLocationData oData) throws Exception 
	{
		m_oLogger.debug ("create - oData [IN] :" + oData);
		PurchaseToLocationDataResponse oResponse = new PurchaseToLocationDataResponse ();
		try 
		{
			oResponse.m_bSuccess = oData.saveObject();
		} 
		catch (Exception oException)
		{
			m_oLogger.error("create -oException " + oException);
		}
		m_oLogger.error("create - oResponse [OUT] :  " + oResponse);
		return oResponse;
	}

	@Override
	public GenericResponse deleteData(PurchaseToLocationData oData) throws Exception
	{
		m_oLogger.debug("deleteData - oData.getM_nPurchaseToLocationId() [IN] : " + oData.getM_nPurchaseToLocationId());
		PurchaseToLocationDataResponse oResponse = new PurchaseToLocationDataResponse ();
		try 
		{
			oResponse.m_bSuccess = oData.deleteObject();
		} 
		catch (Exception oException)
		{
			m_oLogger.error("deleteData -oException " + oException);
		}
		m_oLogger.error("deleteData - oResponse [OUT] :  " + oResponse);
		return oResponse;
	}

	@Override
	public GenericResponse get(PurchaseToLocationData oData) throws Exception 
	{
		PurchaseToLocationDataResponse oResponse = new PurchaseToLocationDataResponse ();
		try 
		{
			oData = (PurchaseToLocationData) populateObject(oData);
			oResponse.m_arrPurchaseToLocationData.add(oData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("get -oException " + oException);
		}
		m_oLogger.error("get - oResponse [OUT] :  " + oResponse);
		return oResponse;
	}

	@Override
	public String getXML(PurchaseToLocationData oData) throws Exception 
	{
		m_oLogger.debug ("getXML - oData [IN] : "+ oData);
		try
		{
			oData = (PurchaseToLocationData) populateObject(oData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " , oException);
		}
		return oData != null ? oData.generateXML () : ""; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericResponse list(PurchaseToLocationData oData,HashMap<String, String> arrOrderBy) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " +oData);
		PurchaseToLocationDataResponse oResponse = new PurchaseToLocationDataResponse ();
		try 
		{
			oResponse.m_arrPurchaseToLocationData = new ArrayList (oData.list (arrOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
			throw oException;
		}
		return oResponse;
	}

	@Override
	public GenericResponse update(PurchaseToLocationData oData)throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData.getM_nPurchaseToLocationId() [IN] : " + oData.getM_nPurchaseToLocationId());
		PurchaseToLocationDataResponse oResponse = new PurchaseToLocationDataResponse ();
		try
		{
			oResponse.m_bSuccess = oData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		m_oLogger.debug ("update - oResponse.m_bSuccess [OUT] : " + oResponse.m_bSuccess);
		return oResponse;
	}

}
