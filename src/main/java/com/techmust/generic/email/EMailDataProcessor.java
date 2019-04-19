package com.techmust.generic.email;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;

public class EMailDataProcessor extends GenericIDataProcessor<MessageData>
{
	Logger m_oLogger = Logger.getLogger (EMailDataProcessor.class);
	@Override
    public GenericResponse create (MessageData oData)
    {
		EMailResponse oResponse = new EMailResponse ();
	    try
        {
	    	oResponse.m_bSuccess = oData.saveObject();
        } 
	    catch (Exception oException)
        {
	        m_oLogger.error("create - oException " + oException);
        }
	   return oResponse;
    }

	@Override
    public GenericResponse deleteData (MessageData oData)
    {
		 // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GenericResponse get (MessageData oData)
    {
	    // TODO Auto-generated method stub
	    return null;
    }

	@SuppressWarnings("unchecked")
	@Override
    public GenericResponse list (MessageData oData, HashMap<String, String> arrOrderBy)
    {
		m_oLogger.info("list");
		EMailResponse oResponse = new EMailResponse ();
		try
		{
			oResponse.m_arrMessageData = new ArrayList(oData.list(arrOrderBy));
		}
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException" +oException);
		}
		m_oLogger.debug("list - oResponse [OUT] : " + oResponse);
	    return oResponse;
    }

	@Override
    public GenericResponse update (MessageData oData)
    {
		EMailResponse oResponse = new EMailResponse ();
		try 
		{
			oResponse.m_bSuccess  = oData.updateObject();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException "+oException);
		}
		return oResponse;
    }
	
	@Override
    public String getXML (MessageData oData)
    {
	    // TODO Auto-generated method stub
	    return null;
    }
	
	public void initiateSendEMailThread ()
	{
		m_oLogger.info ("initiateSendEMailThread");
		try 
		{
			EMailProcessorThread oMailProcessorThread = new EMailProcessorThread ();
			Timer oTimer = new Timer ();
			oTimer.scheduleAtFixedRate (oMailProcessorThread, getDelayTime(), getTimerInterval());
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("initiateSendEMailThread - oException" +oException);
		}
	}

	public class EMailProcessorThread extends TimerTask
	{
		@Override
		public void run ()
		{
			MessageData oMailData = new MessageData ();
			try 
			{	
				EMailDataProcessor oMailDataProcessor = new EMailDataProcessor ();
				oMailData.setM_nStatus(EMailStatus.kToSend);
				HashMap<String, String> oOrderBy = new HashMap<String, String> ();
				EMailResponse oResponse = (EMailResponse) oMailDataProcessor.list (oMailData, oOrderBy);
				SendEMail(oResponse);
			} 
			catch (Exception oException) 
			{
				m_oLogger.error("EMailProcessorThread - oException" +oException);
			}
		}

		private void SendEMail (EMailResponse oResponse)
		{
			m_oLogger.info ("SendEMail");
			m_oLogger.debug ("SendEMail - oResponse" +oResponse);
			try 
			{
				MessageData oMailData = new MessageData ();
				for (int nIndex = 0 ; oResponse.m_arrMessageData .size() > 0 && nIndex < oResponse.m_arrMessageData .size()  ; nIndex++)
				{
					oMailData = oResponse.m_arrMessageData.get (nIndex);
					if(oMailData.getM_oEmailMessageData() != null && oMailData.getM_oEmailMessageData().getM_nId() > 0)
						GenericUtil.sendMail(oMailData.getM_strMessageFrom(), oMailData.getM_strSendTo(), oMailData.getM_strSubject (), oMailData.getM_strContent (), "", oMailData.getM_oEmailMessageData());
					else
						GenericUtil.sendMail(oMailData.getM_strMessageFrom(), oMailData.getM_strSendTo(), oMailData.getM_strSubject (), oMailData.getM_strContent (), "", oMailData.getM_strAttachments());
					oMailData.setM_nStatus (EMailStatus.kSent);
					oMailData.updateObject ();
				}
			}
			catch (Exception oException) 
			{
				m_oLogger.error("SendEMail - oException : " + oException);
			} 
		 }
	  }
	
	private long getTimerInterval () 
	{
		long nTimeInterval = 60000;
		try 
		{
			nTimeInterval = Integer.parseInt(GenericUtil.getProperty ("kMAILSENDINTERVALTIME"));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getTimerInterval - oException : " +oException);
		}
		return nTimeInterval;
	}

	private long getDelayTime () 
	{
		long nTimeInterval = 60000;
		try 
		{
			nTimeInterval = Integer.parseInt(GenericUtil.getProperty ("kMAILSENDDELAYTIME"));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getDelayTime - oException : " +oException);
		}
		return nTimeInterval;
	}
}
