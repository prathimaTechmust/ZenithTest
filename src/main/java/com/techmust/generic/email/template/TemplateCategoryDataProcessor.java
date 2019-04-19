package com.techmust.generic.email.template;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

public class TemplateCategoryDataProcessor extends GenericIDataProcessor<TemplateCategoryData> 
{

	@Override
	public GenericResponse create(TemplateCategoryData oData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oData);
		TemplateCategoryDataResponse oCategoryDataResponse = new TemplateCategoryDataResponse ();
		try
		{
			oCategoryDataResponse.m_bSuccess = oData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oCategoryDataResponse.m_bSuccess [OUT] : " + oCategoryDataResponse.m_bSuccess);
		return oCategoryDataResponse;
	}

	@Override
	public GenericResponse deleteData(TemplateCategoryData oData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData.getM_nCategoryId() [IN] : " + oData.getM_nCategoryId());
		TemplateCategoryDataResponse oCategoryDataResponse = new TemplateCategoryDataResponse ();
		try
		{
			oCategoryDataResponse.m_bSuccess = oData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oCategoryDataResponse.m_bSuccess [OUT] : " + oCategoryDataResponse.m_bSuccess);
		return oCategoryDataResponse;
	}

	@Override
	public GenericResponse get(TemplateCategoryData oData) throws Exception
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData.getM_nCategoryId() [IN] :" +oData.getM_nCategoryId());
		TemplateCategoryDataResponse oCategoryDataResponse = new TemplateCategoryDataResponse ();
		try 
		{
			oData = (TemplateCategoryData) populateObject (oData);
			oCategoryDataResponse.m_arrTemplateCategories.add (oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oCategoryDataResponse;
	}

	@Override
	public String getXML(TemplateCategoryData oData) throws Exception
	{
		oData = (TemplateCategoryData) populateObject(oData);
	    return oData != null ? oData.generateXML () : "";
	}
	
	@Override
	public GenericResponse list(TemplateCategoryData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse list(TemplateCategoryData oData,	HashMap<String, String> arrOrderBy,  int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " +oData);
		TemplateCategoryDataResponse oCategoryDataResponse = new TemplateCategoryDataResponse ();
		try 
		{
			oCategoryDataResponse.m_nRowCount = getRowCount(oData);
			oCategoryDataResponse.m_arrTemplateCategories = new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oCategoryDataResponse;
	}

	@Override
	public GenericResponse update(TemplateCategoryData oData)	throws Exception
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData.getM_nCategoryId() [IN] : " + oData.getM_nCategoryId());
		TemplateCategoryDataResponse oCategoryDataResponse = new TemplateCategoryDataResponse ();
		try
		{
			oCategoryDataResponse.m_bSuccess = oData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oCategoryDataResponse.m_bSuccess [OUT] : " + oCategoryDataResponse.m_bSuccess);
		return oCategoryDataResponse;
	}

}
