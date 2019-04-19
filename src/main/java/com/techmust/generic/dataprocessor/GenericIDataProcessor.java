package com.techmust.generic.dataprocessor;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.hibernate.Criteria;
import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.IGenericData;
import com.techmust.generic.email.EMailDataProcessor;
import com.techmust.generic.email.EMailStatus;
import com.techmust.generic.email.EmailMessageData;
import com.techmust.generic.email.MessageData;
import com.techmust.generic.log.LogData;
import com.techmust.generic.log.LogDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.HibernateUtil;
import com.techmust.usermanagement.actionarea.ActionAreaData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public abstract class GenericIDataProcessor<T extends IGenericData> 
{
	public static Logger m_oLogger = Logger.getLogger(GenericIDataProcessor.class);
	public static final String kUserCredentialsFailed = "User credential failed";
	
	public abstract GenericResponse create (T oGenericData) throws Exception;
	public abstract GenericResponse deleteData (T oGenericData) throws Exception;
	public abstract GenericResponse get (T oGenericData) throws Exception;
	public abstract GenericResponse list (T oGenericData, HashMap<String, String> arrOrderBy) throws Exception;
	public abstract GenericResponse update (T oGenericData) throws Exception;
	public abstract String getXML (T oGenericData) throws Exception;
	
	public static Blob getBlob (InputStream oInputStream)
	{
		m_oLogger.debug("getBlob");
		m_oLogger.info("getBlob - oBufferedImage [IN] : " +oInputStream);
		Blob oBlob = null;
//		Session oSession = HibernateUtil.getSession();
		EntityManager oEntityManager = HibernateUtil.getTenantEntityManager();
		Session oSession = oEntityManager.unwrap(org.hibernate.Session.class);
		try
		{
			LobHelper oHelper = oSession.getLobHelper();
			oBlob = oHelper.createBlob(oInputStream, 0);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getBlob - oException : " +oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.info("getBlob - oPhotoBlob [OUT] :" +oBlob);
		return oBlob;
	}
	
	public static Blob getBlob (BufferedImage oBufferedImage)
	{
		m_oLogger.debug("getBlob");
		m_oLogger.info("getBlob - oBufferedImage [IN] : " +oBufferedImage);
		Blob oPhotoBlob = null;
//		Session oSession = HibernateUtil.getSession();
		EntityManager oEntityManager = HibernateUtil.getTenantEntityManager();
		Session oSession = oEntityManager.unwrap(org.hibernate.Session.class);
		try
		{
			ByteArrayOutputStream oOutputStream = new ByteArrayOutputStream ();
			ImageIO.write(oBufferedImage, "jpg", oOutputStream);
			ByteArrayInputStream oInputStream = new ByteArrayInputStream (oOutputStream.toByteArray());
			LobHelper oLobHelper = oSession.getLobHelper();
			oPhotoBlob = oLobHelper.createBlob(oInputStream, 0);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getBlob - oException : " +oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}

		m_oLogger.info("getBlob - oPhotoBlob [OUT] :" +oPhotoBlob);
		return oPhotoBlob;
	}
	
	public static String getClientCompatibleFormat (Date oDateTime)
    {
		m_oLogger.debug("getClientCompatibleFormat");
		m_oLogger.debug("getClientCompatibleFormat - oDateTime [IN] :" +oDateTime);
		String strSimpleDate = "";
		try
		{
		    if (oDateTime != null)
		    {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				strSimpleDate = formatter.format(oDateTime);
		    }
		}
		catch (Exception oException)
		{
			m_oLogger.error("getClientCompatibleFormat - oException  :" +oException);
		}
		m_oLogger.debug("getClientCompatibleFormat - strSimpleDate [OUT] :" +strSimpleDate);
		return strSimpleDate;
    }
	
	public static String getTime(Date oDate) 
	{
		String strTime = "";
		DateFormat oDateFormat = new SimpleDateFormat("hh:mm a");
		strTime = oDateFormat.format(oDate.getTime());
		return strTime;
	}
	
	public static Date getDBCompatibleDateFormat (String strDate)
    {
		m_oLogger.debug("getDBCompatibleDateFormat");
		m_oLogger.debug("getDBCompatibleDateFormat - strDate [IN] :" +strDate);
		Date oUtilDate = null;
		try
		{
		    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		    oUtilDate = (Date) formatter.parse(strDate);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getDBCompatibleDateFormat - oException  :" +oException);
		}
		m_oLogger.debug("getDBCompatibleDateFormat - oUtilDate [OUT] :" +oUtilDate);
		return oUtilDate;
    }
	
	public static Date getDBCompatibleFilterDateFormat (String strDate, boolean bIsForTo)
    {
		m_oLogger.debug("getDBCompatibleFilterDateFormat");
		m_oLogger.debug("getDBCompatibleFilterDateFormat - strDate [IN] :" +strDate);
		Date oUtilDate = null;
		try
		{
		    DateFormat oFormatter = new SimpleDateFormat("dd-MM-yyyy");
		    oUtilDate = (Date) oFormatter.parse(strDate);
		    if (bIsForTo)
		    {
		    	Calendar oCalendar =Calendar.getInstance();
		    	oCalendar.setTime(oUtilDate);
		    	oCalendar.add(Calendar.HOUR_OF_DAY,23);
		    	oCalendar.add(Calendar.MINUTE,59);
		    	oCalendar.add(Calendar.SECOND,59);
		    	oUtilDate = oCalendar.getTime();
		    }
		}
		catch (Exception oException)
		{
			m_oLogger.error("getDBCompatibleFilterDateFormat - oException  :" +oException);
		}
		m_oLogger.debug("getDBCompatibleFilterDateFormat - oUtilDate [OUT] :" +oUtilDate);
		return oUtilDate;
    }

	public static Object populateObject (GenericData  oGenericData) throws Exception
	{
		m_oLogger.debug("populateObject");
		m_oLogger.debug("populateObject - oGenericData [IN] :" +oGenericData);
//		Session oSession =	HibernateUtil.getSession();
		EntityManager oEntityManager = oGenericData._getEntityManager();
		Object oResult = null;
		ArrayList<GenericData> arrGenericData = new ArrayList<GenericData> ();
		try
		{
/*
 			Criteria oCriteria = oSession.createCriteria(oGenericData.getClass().getName());
			oCriteria = oGenericData.prepareCriteria(oCriteria);
			oResult = oCriteria.uniqueResult();
*/
 			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
 			Class<GenericData> oClass = (Class<GenericData>) oGenericData.getClass();
			CriteriaQuery<GenericData> oCriteria = oCriteriaBuilder.createQuery(oClass);
			Root<GenericData> oRootObject = oCriteria.from(oClass );
			oCriteria.select( oRootObject );
			oCriteria.where(oGenericData.prepareCriteria(oRootObject, oCriteria, oCriteriaBuilder));
			arrGenericData = (ArrayList<GenericData>) oEntityManager.createQuery( oCriteria ).getResultList();
			if(arrGenericData.size() > 0)
				oResult = arrGenericData.get(0);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("populateObject - oException :" +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("populateObject - oResult [OUT] :" +oResult);
		return oResult;
	}
	
	public static long getRowCount (GenericData  oGenericData)
	{
		return oGenericData.getRowCount(oGenericData);
	}
 
	public static Object generateXML(Object oResult)
    {
	    try
	    {
	    	((GenericData)oResult).setXMLData(((GenericData)oResult).generateXML ());
	    }
	    catch (Exception oException)
	    {
	    	
	    }
	    return oResult;
    }
	
	public static BufferedImage getBufferedImage (Blob oBlob)
	{
		m_oLogger.info ("getBufferedImage");
		m_oLogger.debug ("getBufferedImage - oBlob [IN] : " +oBlob);
		BufferedImage oBufferedImage = null;
		try 
		{
			int nBlobLength = (int) oBlob.length ();
			byte [] blobAsBytes = oBlob.getBytes (1, nBlobLength); 
			oBufferedImage = ImageIO.read ( new ByteArrayInputStream (blobAsBytes)); 
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getBufferedImage - oException" +oException);
		}
		return oBufferedImage;
	}
	
	public static boolean sendEmail (String strMessageFrom, String strDestEmailId, String strSubject, String strHTMLContent, String strPlainText, EMailStatus eStatus, String strAttachments)
	{
		MessageData oMailData = new MessageData ();
		oMailData.setM_strMessageFrom(strMessageFrom);
		oMailData.setM_strSendTo(strDestEmailId);
		oMailData.setM_strSubject (strSubject);
		oMailData.setM_nStatus(EMailStatus.kToSend);
		oMailData.setM_strContent (strHTMLContent);
		oMailData.setM_strAttachments(strAttachments);
		return insertToEMailTable (oMailData);
	}
	
	public static boolean sendEmail (String strMessageFrom, String strDestEmailId, String strSubject, String strHTMLContent, String strPlainText, EMailStatus eStatus, EmailMessageData oEmailMessageData)
	{
		MessageData oMailData = new MessageData ();
		oMailData.setM_strMessageFrom(strMessageFrom);
		oMailData.setM_strSendTo(strDestEmailId);
		oMailData.setM_strSubject (strSubject);
		oMailData.setM_nStatus(EMailStatus.kToSend);
		oMailData.setM_strContent (strHTMLContent);
		oMailData.setM_oEmailMessageData(oEmailMessageData);
		return insertToEMailTable (oMailData);
	}
	
	public static boolean isValidUser(UserInformationData oUserData)
    {
		m_oLogger.debug("isValidUser - oUserData.getM_nUserId() :" + oUserData.getM_nUserId());
		m_oLogger.debug("isValidUser - oUserData.getM_nUID() :" + oUserData.getM_nUID());
		boolean bIsValidUser = false;
		try
		{
			long nUID = oUserData.getM_nUID();
			oUserData = (UserInformationData)populateObject(oUserData);
			bIsValidUser = oUserData != null && oUserData.getM_nUID() == nUID;
		}
		catch (Exception oException)
		{
			m_oLogger.error("isValidUser - oException : " + oException);
		}
	    return bIsValidUser;
    }
	
	public void createLog(UserInformationData oUserData, String strFunctionName) 
	{
		m_oLogger.debug("createLog - oUserData.getM_nUserId() :" + oUserData.getM_nUserId());
		m_oLogger.debug("createLog - oUserData.getM_nUID() :" + oUserData.getM_nUID());
		LogData oLogData = new LogData ();
		LogDataProcessor oDataProcessor = new LogDataProcessor ();
		try
		{
			oLogData.setM_nUserID(oUserData.getM_nUserId());
			oLogData.setM_strFunctionName(strFunctionName);
			oDataProcessor.create(oLogData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}

	private static boolean insertToEMailTable (MessageData oMailData)
    {
	   m_oLogger.info ("insertToEMailTable");
	   m_oLogger.debug("insertToEMailTable - oMailData" + oMailData);
	   boolean bSuccess = false;
	   try
	   {
		   EMailDataProcessor oMailDataProcessor = new EMailDataProcessor ();
		   oMailDataProcessor.create (oMailData);
		   bSuccess = true;
	   }
	   catch (Exception oException)
	   {
		   m_oLogger.error ("insertToEMailTable - oException" + oException);
	   }
	   return bSuccess;
    }
	
	public Criteria prepareCustomCriteria(Criteria oCriteria, T oGenericData) throws RuntimeException
	{
		throw new RuntimeException ("prepareCustomCriteria must be implemented in derived class!");
	}
	
	public static String encryptPassword (String strPassword) throws SecurityException
	{
		m_oLogger.info ("encryptPassword");
		m_oLogger.debug ("encryptPassword - strPassword [IN] : " + strPassword);
		String strEncryptedPassword = "";
		try
		{
			DigestSHA3 oMDEncrypt = new DigestSHA3(512);
			oMDEncrypt.update (strPassword.getBytes (), 0, strPassword.length ());
			strEncryptedPassword = new BigInteger (1, oMDEncrypt.digest())
			        .toString (16);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("encryptPassword - oException : " +oException);
		}
		m_oLogger.debug ("encryptPassword - strEncryptedPassword [OUT] : " +strEncryptedPassword);
		return strEncryptedPassword;
	}
	
}