package com.techmust.generic.email.template;

import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;

import org.directwebremoting.io.FileTransfer;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

public class TemplateDataProcessor extends GenericIDataProcessor<TemplateData> 
{

	public GenericResponse createTemplate (TemplateData oTemplateData, FileTransfer oFileTransfer) throws Exception 
	{
		m_oLogger.info ("createTemplate");
		m_oLogger.debug ("createTemplate - oTemplateData [IN] : " + oTemplateData);
		m_oLogger.debug ("createTemplate - oFileTransfer [IN] : " + oFileTransfer);
		TemplateDataResponse oTemplateDataResponse = new TemplateDataResponse ();
		try
		{ 	
			InputStream oInputStream = oFileTransfer.getInputStream();
			oTemplateData.setM_oTemplateFile(getBlob (oInputStream));
			oTemplateDataResponse.m_bSuccess = oTemplateData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("createTemplate - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("createTemplate - oTemplateDataResponse.m_bSuccess [OUT] : " + oTemplateDataResponse.m_bSuccess);
		return oTemplateDataResponse;
	}

	@Override
	public GenericResponse deleteData (TemplateData oTemplateData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oTemplateData.getM_nTemplateId() [IN] : " + oTemplateData.getM_nTemplateId());
		TemplateDataResponse oTemplateDataResponse = new TemplateDataResponse ();
		try
		{
			oTemplateDataResponse.m_bSuccess = oTemplateData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oTemplateDataResponse.m_bSuccess [OUT] : " + oTemplateDataResponse.m_bSuccess);
		return oTemplateDataResponse;
	}

	@Override
	public GenericResponse get (TemplateData oTemplateData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oTemplateData.getM_nTemplateId() [IN] :" +oTemplateData.getM_nTemplateId());
		TemplateDataResponse oTemplateDataResponse = new TemplateDataResponse ();
		try 
		{
			oTemplateData = (TemplateData) populateObject (oTemplateData);
			oTemplateDataResponse.m_arrTemplates.add (oTemplateData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oTemplateDataResponse;
	}

	@Override
	public String getXML (TemplateData oTemplateData) throws Exception 
	{
		m_oLogger.debug ("getXML - oTemplateData [IN] : "+ oTemplateData);
		try
		{
		oTemplateData = (TemplateData) populateObject(oTemplateData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " , oException);
		}
		return oTemplateData != null ?oTemplateData.generateXML () : "";
	}

	@Override
	public GenericResponse list(TemplateData oTemplateData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oTemplateData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse list (TemplateData oTemplateData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oTemplateData [IN] : " +oTemplateData);
		TemplateDataResponse oTemplateDataResponse = new TemplateDataResponse ();
		try 
		{
			oTemplateDataResponse.m_nRowCount = getRowCount(oTemplateData);
			oTemplateDataResponse.m_arrTemplates = new ArrayList (oTemplateData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oTemplateDataResponse;
	}

	public GenericResponse updateTemplate (TemplateData oTemplateData, FileTransfer oFileTransfer) throws Exception 
	{
		m_oLogger.info ("updateTemplate");
		m_oLogger.debug ("updateTemplate - oTemplateData.getM_nTemplateId() [IN] : " + oTemplateData.getM_nTemplateId());
		m_oLogger.debug ("updateTemplate - oFileTransfer [IN] : " + oFileTransfer);
		TemplateDataResponse oTemplateDataResponse = new TemplateDataResponse ();
		try
		{
			updateTemplateFile ( oTemplateData, oFileTransfer);
			oTemplateDataResponse.m_bSuccess = oTemplateData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateTemplate - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("updateTemplate - oTemplateDataResponse.m_bSuccess [OUT] : " + oTemplateDataResponse.m_bSuccess);
		return oTemplateDataResponse;
	}
	
	
	private void updateTemplateFile (TemplateData oTemplateData, FileTransfer oFileTransfer)
	{
		m_oLogger.info ("updateTemplateFile");
		m_oLogger.debug ("updateTemplateFile - oTemplateData [IN] : " + oTemplateData);
		m_oLogger.debug ("updateTemplateFile - oFileTransfer [IN] : " + oFileTransfer);
		TemplateDataResponse oTemplateDataResponse = new TemplateDataResponse ();
		try 
		{
//			if (oFileTransfer.getSize() > 0)
			if(oFileTransfer != null)
			{
				InputStream oInputStream = oFileTransfer.getInputStream();
				oTemplateData.setM_oTemplateFile(getBlob (oInputStream));
			}
			else
			{
				oTemplateDataResponse = (TemplateDataResponse) get (oTemplateData);
				Blob oBlob = oTemplateDataResponse.m_arrTemplates.get(0).getM_oTemplateFile();
				oTemplateData.setM_oTemplateFile (oBlob);
				oTemplateData.setM_strTemplateFileName(oTemplateDataResponse.m_arrTemplates.get(0).getM_strTemplateFileName());
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateTemplateFile - oException" + oException);
		}
	}

	@Override
	public GenericResponse create(TemplateData arg0) throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse update(TemplateData arg0) throws Exception 
	{
		return null;
	}
}
