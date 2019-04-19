package com.techmust.generic;

import java.io.InputStream;
import java.util.ArrayList;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class MessageDataWrapper 
{
	public static final Logger m_oLogger = Logger.getLogger(MessageDataWrapper.class);
	private Message m_oMessage;
	private int m_nJobProcessId = -1;
	public MessageDataWrapper (final Message oMessage, int nJobProcessId)
	{	
		m_nJobProcessId = nJobProcessId;
		m_oMessage = oMessage;
	}
	
	public String getSubject () 
	{
		m_oLogger.info ("getSubject");
		String strSubject = "";
		try 
		{	
			m_oMessage.getFolder().open(Folder.READ_WRITE);
			strSubject = m_oMessage.getSubject();
			m_oMessage.getFolder().close(false);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getSubject - oException" +oException);
		}
		m_oLogger.debug("getSubject - strSubject [OUT] : " + strSubject);
		return strSubject;
	}

	public String getContent ()
	{
		String strContent ="";
		try 
		{
			if(!m_oMessage.getFolder().isOpen())
				m_oMessage.getFolder().open(Folder.READ_ONLY);
			MimeMultipart oMultipart = (MimeMultipart) m_oMessage.getContent();
			for (int nPartIndex = 0; nPartIndex < oMultipart.getCount(); nPartIndex++)
			{
				BodyPart oPart = oMultipart.getBodyPart (nPartIndex);
				String strContentType = oPart.getContentType();
				if (oPart.isMimeType("text/plain"))
				{
					strContent = oPart.getContent().toString();
					break;
				}
				if (strContentType.contains ("multipart"))
					strContent = getBodyTextOfMultiPart (oPart);
				if (!strContent.isEmpty())
					break;
				
			}
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getContent - oException" + oException);
		} 
		finally 
		{
			try 
			{
				if(m_oMessage.getFolder().isOpen())
					m_oMessage.getFolder().close(false);
			} 
			catch (Exception oException) 
			{
				// TODO: handle exception
			}
		}
		return strContent;
	}
	
	public ArrayList<AttachmentData> getAttachments ()
	{
		ArrayList<AttachmentData> arrAttachments = new ArrayList<AttachmentData> ();
		try 
		{
			m_oMessage.getFolder().open(Folder.READ_ONLY);
			Multipart oMultipart =  (Multipart) m_oMessage.getContent();
			for (int nPartIndex = 0; nPartIndex < oMultipart.getCount(); nPartIndex++)
			{
				BodyPart oPart = oMultipart.getBodyPart (nPartIndex);
				if ((Part.ATTACHMENT).equalsIgnoreCase(oPart.getDisposition()))
					arrAttachments.add(getAttachment (oPart));
			}
			m_oMessage.getFolder().close(false);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getAttachments - oException" + oException);
		}
		m_oLogger.debug("getAttachments - arrAttachments [OUT] :" + arrAttachments);
		return arrAttachments;
	}
	
	public String getFromAddress () throws Exception
	{	
		m_oLogger.info("getFromAddres");
		String strFromAddress = null;
		try 
		{
			m_oMessage.getFolder().open(Folder.READ_ONLY);
			InternetAddress[] arrAddress = (InternetAddress[]) m_oMessage.getFrom();
			strFromAddress = arrAddress [0].getAddress();
			m_oMessage.getFolder().close(false);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getFromAddress - oException" + oException);
			throw oException;
		}
		m_oLogger.debug("getFromAddress - strFromAddress [OUT] :" +strFromAddress);
		return strFromAddress;
	}
	
	public void setM_nJobProcessId (int nJobProcessId)
	{
		this.m_nJobProcessId = nJobProcessId;
	}

	public int getM_nJobProcessId ()
	{
		return m_nJobProcessId;
	}
	
	private AttachmentData getAttachment (BodyPart oPart) 
	{
		m_oLogger.info("getAttachment");
		m_oLogger.debug("getAttachment - oPart [IN] : " + oPart);
		AttachmentData oAttachmentData = new AttachmentData ();
		try 
		{
			oAttachmentData.setM_strFileName(oPart.getFileName());
			InputStream oInputStream = (InputStream) oPart.getInputStream();
			byte[] arrBytes = IOUtils.toByteArray(oInputStream) ;
			oAttachmentData.setM_oAttachment(new SerialBlob(arrBytes));
		}
		catch (Exception oException)
		{
			m_oLogger.error("getAttachment - oException" +oException);
		}
		m_oLogger.debug("getAttachment - oAttachmentData [OUT] :" +oAttachmentData);
		return oAttachmentData;
	}

	private String  getBodyTextOfMultiPart (BodyPart oPart) throws Exception 
	{
		m_oLogger.info ("getBodyTextOfMultiPart");
		m_oLogger.debug ("getBodyTextOfMultiPart - oPart [IN] : " +oPart);
		String strContent = "";
		try 
		{	
			MimeMultipart oMultiBodyPart = new MimeMultipart (oPart.getDataHandler().getDataSource());
			for (int nSubpart = 0; nSubpart < oMultiBodyPart.getCount(); nSubpart++)
			{
				BodyPart oInternalPart = oMultiBodyPart.getBodyPart(nSubpart);
				String strDisposition = oInternalPart.getDisposition();
				strContent = strDisposition == null ? oInternalPart.getContent().toString() : "";
				if (!strContent.isEmpty())
					break;
			}
		}
		catch (Exception oException) 
		{
			m_oLogger.error("getBodyTextOfMultiPart - oException" + oException);
			throw oException;
		}
		return strContent;
	}

}
