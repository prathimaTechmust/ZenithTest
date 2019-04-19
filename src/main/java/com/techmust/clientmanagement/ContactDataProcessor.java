package com.techmust.clientmanagement;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

public class ContactDataProcessor extends GenericIDataProcessor<ContactData>
{
	Logger m_oLogger = Logger.getLogger (ContactDataProcessor.class);
	
	@Override
	public GenericResponse create (ContactData oData) 
	{
		return null;
	}

	@Override
	public ContactDataResponse get (ContactData oData) 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData [IN] : " + oData);
		ContactData oContactData = new ContactData ();
		ContactDataResponse oContactResponse = new ContactDataResponse ();
		try 
		{
			oContactData.setM_nContactId (oData.getM_nContactId ());
			oContactData = (ContactData) populateObject (oContactData);
			oContactResponse.m_arrContactData.add (oContactData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : " + oException);
		}
		return oContactResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ContactDataResponse list (ContactData oData, HashMap<String, String> arrOrderBy) 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " + oData);
		ContactDataResponse oContactResponse = new ContactDataResponse ();
		try 
		{
			oContactResponse.m_arrContactData = new ArrayList (oData.list (arrOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("list - oException : " + oException);
		}
		return oContactResponse;
	}

	@Override
	public ContactDataResponse update (ContactData oData)
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData [IN] : " + oData);
		ContactDataResponse oContactResponse = new ContactDataResponse ();
		try 
		{
			oContactResponse.m_bSuccess = oData.updateObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		return oContactResponse;
	}
	
	@Override
	public ContactDataResponse deleteData (ContactData oData) 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " + oData);
		ContactDataResponse oContactResponse = new ContactDataResponse ();
		try 
		{
			oContactResponse.m_bSuccess = oData.deleteObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		}
		return oContactResponse;
	}

	@Override
	public String getXML (ContactData oData)
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oContactData [IN] : " + oData);
		String strXml = "";
		try
		{
			oData = (ContactData) populateObject (oData);
			strXml = oData.generateXML ();
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getXML - oException " + oException);
		}
		m_oLogger.debug("getXML - strXml [OUT] : " + strXml);
		return strXml;
	}
}