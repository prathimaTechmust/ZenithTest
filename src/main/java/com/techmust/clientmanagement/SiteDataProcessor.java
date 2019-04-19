package com.techmust.clientmanagement;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

public class SiteDataProcessor extends GenericIDataProcessor<SiteData> 
{

	@Override
	public GenericResponse create(SiteData oSiteData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse deleteData(SiteData oData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " + oData);
		SiteDataResponse oSiteDataResponse = new SiteDataResponse ();
		try 
		{
			oSiteDataResponse.m_bSuccess = oData.deleteObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		}
		return oSiteDataResponse;
	}

	@Override
	public GenericResponse get(SiteData oData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData [IN] : " + oData);
		SiteData oSiteData = new SiteData ();
		SiteDataResponse oSiteDataResponse = new SiteDataResponse ();
		
		try 
		{
			oSiteData = (SiteData) populateObject (oData);
			oSiteDataResponse.m_arrSiteData.add (oSiteData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : " + oException);
		}
		return oSiteDataResponse;
	}

	@Override
	public String getXML(SiteData oSiteData) throws Exception 
	{
		m_oLogger.debug("getXML - oSiteData [IN] : " + oSiteData);
		try
		{
			oSiteData = (SiteData) populateObject(oSiteData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " + oException);
		}
		return oSiteData != null ?oSiteData.generateXML () : ""; 
	}

	@Override
	public GenericResponse list(SiteData oData, HashMap<String, String> arrOrderBy) throws Exception 
		{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " + oData);
		SiteDataResponse oSiteDataResponse = new SiteDataResponse ();
		try 
		{
			oSiteDataResponse.m_arrSiteData = new ArrayList (oData.list (arrOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("list - oException : " + oException);
		}
		return oSiteDataResponse;
	}

	@Override
	public GenericResponse update(SiteData oData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData [IN] : " + oData);
		SiteDataResponse oSiteDataResponse = new SiteDataResponse ();
		try 
		{
			oSiteDataResponse.m_bSuccess = oData.updateObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		return oSiteDataResponse;
	}

}
