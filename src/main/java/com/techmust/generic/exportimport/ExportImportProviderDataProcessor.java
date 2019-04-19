package com.techmust.generic.exportimport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

public class ExportImportProviderDataProcessor extends GenericIDataProcessor<ExportImportProviderData> 
{

	@Override
	public GenericResponse create(ExportImportProviderData oData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oData);
		ExportImportProviderDataResponse oDataResponse = new ExportImportProviderDataResponse ();
		try 
		{
			oData.addClasses();
			oDataResponse.m_bSuccess = oData.saveObject ();
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("create - oException : " +oException);
		}
		return oDataResponse;
	}

	private Collection<DataExchangeClasses> buildDataExchangeList(DataExchangeClasses[] arrDataExchangeClasses)
	{
		m_oLogger.info ("buildDataExchangeList");
		m_oLogger.debug ("buildDataExchangeList - arrDataExchangeClasses.length [IN] : " + arrDataExchangeClasses != null ? arrDataExchangeClasses.length : 0);
		ArrayList<DataExchangeClasses> oArrayList = new ArrayList<DataExchangeClasses> ();
		try
		{
			for (int nIndex = 0; arrDataExchangeClasses != null && nIndex < arrDataExchangeClasses.length; nIndex++)
				oArrayList.add(arrDataExchangeClasses [nIndex]);
		}
		catch(Exception oException)
		{
			m_oLogger.error ("buildDataExchangeList - oException : " +oException);
		}
		return oArrayList;
	}

	@Override
	public GenericResponse deleteData(ExportImportProviderData oData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " + oData);
		ExportImportProviderDataResponse oDataResponse = new ExportImportProviderDataResponse ();
		try 
		{
			ExportImportProviderData oExportImportProviderData = (ExportImportProviderData) populateObject(oData);
			oDataResponse.m_bSuccess = oExportImportProviderData.deleteObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("deleteData - oException : " + oException);
		}
		return oDataResponse;
	}

	@Override
	public GenericResponse get(ExportImportProviderData oData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData [IN] : " + oData);
		ExportImportProviderData oExportImportProviderData = new ExportImportProviderData ();
		ExportImportProviderDataResponse oDataResponse = new ExportImportProviderDataResponse ();
		try 
		{
			oExportImportProviderData.setM_nId(oData.getM_nId());
			oExportImportProviderData = (ExportImportProviderData) populateObject (oExportImportProviderData);
			oDataResponse.m_arrExportImportProvider.add (oExportImportProviderData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : " + oException);
		}
		return oDataResponse;
	}

	@Override
	public String getXML(ExportImportProviderData oData) throws Exception 
	{
		String strXml = "";
		try 
		{
			oData = (ExportImportProviderData) populateObject (oData);
			strXml = oData.generateXML ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getXML - oException : " + oException);
		}
		m_oLogger.debug("getXML - strXml [OUT] : " + strXml);
		return strXml;
	}

	@Override
	public GenericResponse list(ExportImportProviderData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public GenericResponse list(ExportImportProviderData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " + oData);
		ExportImportProviderDataResponse oDataResponse = new ExportImportProviderDataResponse ();
		try 
		{
			oDataResponse.m_nRowCount = getRowCount(oData);
			oDataResponse.m_arrExportImportProvider = new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("list - oException : " + oException);
		}
		return oDataResponse;
	}

	@Override
	public GenericResponse update(ExportImportProviderData oData) throws Exception 
	{
		m_oLogger.info("update");
		m_oLogger.debug("update - oData [IN] : " + oData);
		ExportImportProviderDataResponse oDataResponse = new ExportImportProviderDataResponse ();
		try 
		{
			removeClasses (oData);
			oData.addClasses();
			oDataResponse.m_bSuccess = oData.updateObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		return oDataResponse;
	}

	private void removeClasses (ExportImportProviderData oData) throws Exception 
	{
		m_oLogger.info ("removeClasses");
		m_oLogger.debug ("removeClasses - oData [IN]: " + oData);
		try
		{
			ExportImportProviderData oProviderData = (ExportImportProviderData) populateObject(oData);
			Iterator<DataExchangeClasses> oIterator = oProviderData.getM_oDataExchangeClasses().iterator();
			while (oIterator.hasNext())
			{
				DataExchangeClasses oDataExchangeClasses = (DataExchangeClasses) oIterator.next();
				oDataExchangeClasses.deleteObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("removePreviousChildren - oException : " , oException);
		}
	}
}
