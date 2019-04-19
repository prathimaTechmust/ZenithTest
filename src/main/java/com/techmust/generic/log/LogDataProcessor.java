package com.techmust.generic.log;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

public class LogDataProcessor extends GenericIDataProcessor<LogData>
{
	@Override
	public LogDataResponse create(LogData oLogData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oLogData [IN] : " +oLogData);
		LogDataResponse oLogDataResponse = new LogDataResponse ();
		try 
		{
			oLogDataResponse.m_bSuccess = oLogData.saveObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("create - oException : "  +oException);
		}
		return oLogDataResponse;
	}

	@Override
	public LogDataResponse deleteData(LogData oLogData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oLogData [IN] : " + oLogData);
		LogDataResponse oLogDataResponse = new LogDataResponse ();
		try 
		{
			oLogDataResponse.m_bSuccess = oLogData.deleteObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("deleteData - oException : " +oException);
		}
		return oLogDataResponse;
	}

	@Override
	public LogDataResponse get(LogData oLogData) throws Exception
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oLogData [IN] : " +oLogData);
		LogDataResponse oLogDataResponse = new LogDataResponse ();
		LogData oData = new LogData ();
		try 
		{
			oData.setM_nId(oLogData.getM_nId());
			oData =  (LogData) populateObject (oData);
			oLogDataResponse.m_arrLogData.add (oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : " +oException);
		}
		return oLogDataResponse;
	}

	@Override
	public String getXML(LogData oLogData) throws Exception 
	{
		oLogData = (LogData) populateObject(oLogData);
	    return oLogData != null ? oLogData.generateXML () : "";
	}

	@Override
	public GenericResponse list(LogData oLogData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return (LogDataResponse) list(oLogData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
    public GenericResponse list(LogData oLogData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oLogData [IN] : " +oLogData);
		LogDataResponse oLogDataResponse = new LogDataResponse ();
		try 
		{
			oLogDataResponse.m_nRowCount = getRowCount(oLogData);
			oLogDataResponse.m_arrLogData = new ArrayList (oLogData.list (arrOrderBy, nPageNumber, nPageSize));
			oLogDataResponse.m_arrLogData = buildReturnedData (oLogDataResponse.m_arrLogData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oLogDataResponse;
	}

	@Override
	public GenericResponse update(LogData oLogData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oLogData [IN] : " + oLogData);
		LogDataResponse oLogDataResponse = new LogDataResponse ();
		try 
		{
			oLogDataResponse.m_bSuccess = oLogData.updateObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException : " +oException);
		}
		return oLogDataResponse;
	}
	
	private ArrayList<LogData> buildReturnedData (ArrayList<LogData> arrLogData) 
	{
		m_oLogger.info("buildReturnedData");
		for (int nIndex=0; nIndex < arrLogData.size(); nIndex++)
			arrLogData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrLogData.get(nIndex).getM_dCreatedOn()) + " " + getTime(arrLogData.get(nIndex).getM_dCreatedOn()));
		return arrLogData;
	}
}
