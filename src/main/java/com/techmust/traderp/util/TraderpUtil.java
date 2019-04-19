package com.techmust.traderp.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.clientmanagement.ContactData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.email.EMailStatus;
import com.techmust.generic.email.EmailMessageData;
import com.techmust.generic.util.GenericUtil;

public class TraderpUtil extends GenericUtil
{
	public static String getTime(Date oDate) 
	{
		String strTime = "";
		DateFormat oDateFormat = new SimpleDateFormat("hh:mm a");
		strTime = oDateFormat.format(oDate.getTime());
		return strTime;
	}
	
	public static boolean sendEMails (String[] arrEmailIds, String strSubject, String strXMLContent, String strXSLTFileName)
	{
		boolean bSuccess = false;
		for (int nIndex = 0; nIndex < arrEmailIds.length; nIndex++)
			bSuccess =  sendEmail (arrEmailIds[nIndex], strSubject, strXMLContent, strXSLTFileName);
		return bSuccess;
	}
	
	public static boolean sendEmail (String strDestEmailId, String strSubject, String strXMLContent, String strXSLTFileName)
	{
		boolean bSuccess = false;
		String strMessageFrom = GenericUtil.getProperty("kAPPLICATIONNAME");
		String strHTMLContent = GenericUtil.buildHtml(strXMLContent, GenericUtil.getProperty("kXSLT_FOLDER") + strXSLTFileName);
		String strAttachments = GenericUtil.getProperty("kATTACHMENT_FILENAMES");
		bSuccess = GenericIDataProcessor.sendEmail(strMessageFrom, strDestEmailId, strSubject, strHTMLContent, "", EMailStatus.kToSend, strAttachments);
		return bSuccess;
	}
	
	public void sendCRMail (EmailMessageData oEmailMessageData) throws Exception
	{
		super.sendCRMail (oEmailMessageData);
	}
	
	public void publicise (EmailMessageData oEmailMessageData) throws Exception
	{
		Iterator<ContactData> oContacts = oEmailMessageData.getM_oContact().iterator();
		String strXsltpath = getCRMXSLT (oEmailMessageData.getM_oTemplateData());
		Document oXmlDocument = getXmlDocument (oEmailMessageData.getM_strHTML());
		Element oRootElement = oXmlDocument.getDocumentElement();
		while (oContacts.hasNext())
		{
			String strMessageFrom = GenericUtil.getProperty("kAPPLICATIONNAME");
			ContactData oContactData = oContacts.next();
			String strDestEmailId = oContactData.getM_strEmail();
			Document oContactXmlDoc = getXmlDocument (oContactData.generateXML());
			Node oContactNode = oXmlDocument.importNode (oContactXmlDoc.getFirstChild (), true);
			oRootElement.appendChild (oContactNode);
			String strMailXML = getXmlString (oXmlDocument);
			String strHTMLContent = GenericUtil.buildHtml(strMailXML, strXsltpath);
			GenericIDataProcessor.sendEmail(strMessageFrom, strDestEmailId, oEmailMessageData.getM_strSubject(), strHTMLContent, "", EMailStatus.kToSend, oEmailMessageData);
		}
		deleteTempXslt (strXsltpath);
	}
	
	public static float getFloatValue(String strValue) 
	{
		float nFloatValue = 0;
		try
		{
			nFloatValue = (float) Double.parseDouble(strValue);
		}
		catch (Exception oException)
		{
			
		}
		return nFloatValue;
	}

	public static int getIntValue(String strValue) 
	{
		int nIntValue = 0;
		try
		{
			nIntValue = Integer.parseInt(strValue);
		}
		catch (Exception oException)
		{
			
		}
		return nIntValue;
	}
	
	public static String getTallyCompatibleFormat(Date oDateTime) 
	{
		String strSimpleDate = "";
		try
		{
		    if (oDateTime != null)
		    {
				SimpleDateFormat oDateFormat = new SimpleDateFormat("yyyyMMdd");
				strSimpleDate = oDateFormat.format(oDateTime);
		    }
		}
		catch (Exception oException)
		{
		}
		return strSimpleDate;
	}
	
	public static String formatNumber (float nValue)
	{
		String strValue = "";
		NumberFormat formatter = new DecimalFormat("#0.00");  
		strValue = formatter.format(nValue);
		return strValue;  
	}
	
	public static String getLastDayOfMonth (int nMonth, int nYear) 
	{
	    Calendar oCalendar = Calendar.getInstance();
	    oCalendar.set(nYear, nMonth - 1, 1);
	    oCalendar.set(Calendar.DATE, oCalendar.getActualMaximum(Calendar.DATE));
	    Date dDate = oCalendar.getTime();
	    DateFormat oDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    return oDateFormat.format(dDate);
	}
	
	public static String getFirstDayOfMonth (int nMonth, int nYear) 
	{
	    Calendar oCalendar = Calendar.getInstance();
	    oCalendar.set(nYear, nMonth - 1, 1);
	    oCalendar.set(Calendar.DATE, oCalendar.getActualMinimum(Calendar.DATE));
	    Date dDate = oCalendar.getTime();
	    DateFormat oDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    return oDateFormat.format(dDate);
	}
}
