package com.techmust.master.businesstype;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;

public class BusinessTypeDataProcessor extends GenericIDataProcessor<BusinessTypeData> 
{
	Logger m_oLogger = Logger.getLogger (BusinessTypeDataProcessor.class);
	
	@Override
	public BusinessTypeResponse create (BusinessTypeData oData) 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oData);
		BusinessTypeResponse oBusinessTypeResponse = new BusinessTypeResponse ();
		try
		{
			oBusinessTypeResponse.m_bSuccess = oData.saveObject ();
			//oBusinessTypeResponse.m_arrBusinessType.add (oData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
		}
		return oBusinessTypeResponse;
	}

	@Override
	public BusinessTypeResponse get (BusinessTypeData oData)
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData [IN] : " + oData);
		BusinessTypeResponse oBusinessTypeResponse = new BusinessTypeResponse ();
		BusinessTypeData oBusinessTypeData = new BusinessTypeData ();
		try
		{
			oBusinessTypeData.setM_nBusinessTypeId (oData.getM_nBusinessTypeId ());
			oBusinessTypeData = (BusinessTypeData)populateObject (oBusinessTypeData);
			oBusinessTypeResponse.m_arrBusinessType.add (oBusinessTypeData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("get - oException : " + oException);
		}
		return oBusinessTypeResponse;
	}

	@Override
	public BusinessTypeResponse list(BusinessTypeData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public BusinessTypeResponse list (BusinessTypeData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " + oData);
		BusinessTypeResponse oBusinessTypeResponse = new BusinessTypeResponse ();
		try
		{
			oBusinessTypeResponse.m_nRowCount = getRowCount(oData);
			oBusinessTypeResponse.m_arrBusinessType = new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("list - oException : " + oException);
		}
		return oBusinessTypeResponse;
	}

	@Override
	public BusinessTypeResponse update (BusinessTypeData oData) 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData [IN] : " + oData);
		BusinessTypeResponse oBusinessTypeResponse = new BusinessTypeResponse ();
		try
		{
			oBusinessTypeResponse.m_bSuccess = oData.updateObject ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		return oBusinessTypeResponse;
	}
	
	@Override
	public BusinessTypeResponse deleteData (BusinessTypeData oData)
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " + oData);
		BusinessTypeResponse oBusinessTypeResponse = new BusinessTypeResponse ();
		try
		{
			oBusinessTypeResponse.m_bSuccess = oData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		}
		return oBusinessTypeResponse;
	}
	
	public boolean isBusinessTypeTableEmpty ()
	{
		m_oLogger.info ("isBusinessTypeTableEmpty");
		boolean bEmpty = false;
		BusinessTypeData oBusinessTypeData = new BusinessTypeData ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ArrayList<GenericData> arrBusinessTypeData = oBusinessTypeData.list (oOrderBy);
			if (arrBusinessTypeData.size () == 0)
				bEmpty = true;
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("isBusinessTypeTableEmpty - oException : " + oException);
		}
		m_oLogger.debug ("isBusinessTypeTableEmpty - bEmpty [OUT] : " + bEmpty);
		return bEmpty;
	}

	@Override
	public String getXML (BusinessTypeData oData) 
	{
		String strXML = "";
		try
		{
			oData = (BusinessTypeData)populateObject (oData);
			strXML = oData.generateXML ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getXML - oException : " + oException);
		}
		m_oLogger.debug ("getXML - strXML [OUT] : " + strXML);
		return strXML;
	}
}