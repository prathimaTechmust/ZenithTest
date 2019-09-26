package com.techmust.utils;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.time.ZoneId;
import java.util.Calendar;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.techmust.constants.Constants;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.scholarshipmanagement.activitylog.ActivityLog;
import com.techmust.scholarshipmanagement.activitylog.ActivityLogDataProcessor;
import com.techmust.scholarshipmanagement.studentdocuments.StudentDocuments;
import com.techmust.usermanagement.userinfo.UserInformationData;


public class Utils 
{	
	public static Logger m_oLogger = Logger.getLogger(GenericIDataProcessor.class);
	public static void saveCookie(String strCookieName, String strValue, HttpServletResponse oResponse)
	{
		try
		{
			Cookie cookie = new Cookie(strCookieName, URLEncoder.encode(strValue, "UTF-8"));
			oResponse.addCookie(cookie);
		}
		catch (Exception oException)
		{			
			m_oLogger.error("Save Cookie - oExeception"+oException);
		}	   
	}

	public static String getCookie(HttpServletRequest oRequest, String strCookieName)
	{
		String strCookie = null;
		Cookie[] cookies = oRequest.getCookies();
		if(cookies != null)
		{
			for (Cookie nthCookie : cookies) 
			{
				if (strCookieName.equals(nthCookie.getName()))
				{
					strCookie = nthCookie.getValue();
					break;
				}
			}
		}
		return strCookie;
	}

	public static long getTimeStamp()
	{
		long oTimestamp = System.currentTimeMillis();
		return oTimestamp;
	}

	public static String getLocaleString (String strKey)
	{
		ResourceBundle oRsrcBundle = ResourceBundle.getBundle(Constants.MESSAGES_BUNDLE_NAME, Locale.getDefault());
		return oRsrcBundle.getString(strKey);
	}

	public static String getRandomPassword()
	{
		return RandomStringUtils.randomAlphanumeric(8);
	}
	
	public static String getUUID()
	{
		return UUID.randomUUID().toString();
	}
	
	@SuppressWarnings("Depricated")
	public static String convertTimeStampToDate(long dLongTimeStamp)
	{	        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Kolkata")));
        Date date = new Date(dLongTimeStamp);
        return formatter.format(date);
	}
	
	public static StudentDocuments getStudentDocuments(StudentDocuments oStudentDocuments)
	{
		 if(oStudentDocuments.getM_strStudentAadhar() != null)
		 {
			 String strStudentAadharURL = Constants.S3BUCKETURL + Constants.STUDENTAADHARDOCUMENTFOLDER + oStudentDocuments.getM_strStudentAadhar() + Constants.IMAGE_DEFAULT_EXTENSION;
 			 oStudentDocuments.setM_strStudentAadhar(strStudentAadharURL);
		 }
		 if(oStudentDocuments.getM_strFatherAadharImageId() != null)
		 {
			 String strStudentFatherAadharURL = Constants.S3BUCKETURL + Constants.STUDENTFATHERAADHAR + oStudentDocuments.getM_strFatherAadharImageId() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strFatherAadharImageId(strStudentFatherAadharURL);
		 }
		 if(oStudentDocuments.getM_strMotherAadharImageId() != null)
		 {
			 String strStudentMotherAadharURL = Constants.S3BUCKETURL + Constants.STUDENTMOTHERAADHAR + oStudentDocuments.getM_strMotherAadharImageId() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strMotherAadharImageId(strStudentMotherAadharURL);
		 }	
		 if(oStudentDocuments.getM_strStudentElectricityBill() != null)
		 {
			 String strStudentElectricityBillURL = Constants.S3BUCKETURL + Constants.STUDENTELECTRICITYBILLDOCUMENTFOLDER + oStudentDocuments.getM_strStudentElectricityBill() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strStudentElectricityBill(strStudentElectricityBillURL);
		 }			
		 if(oStudentDocuments.getM_strStudentMarksCard1() != null)
		 {
			 String strStudentMarkscard1URL = Constants.S3BUCKETURL + Constants.STUDENTMARKSCARD1 + oStudentDocuments.getM_strStudentMarksCard1() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strStudentMarksCard1(strStudentMarkscard1URL);
		 }
		 if(oStudentDocuments.getM_strStudentMarksCard2() != null)
		 {
			 String strStudentMarkscard2URL = Constants.S3BUCKETURL + Constants.STUDENTMARKSCARD2 + oStudentDocuments.getM_strStudentMarksCard2() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strStudentMarksCard2(strStudentMarkscard2URL);
		 }
		 if(oStudentDocuments.getM_strOtherDocuments() != null)
		 {
			 String strStudentOtherDocumentsURL = Constants.S3BUCKETURL + Constants.STUDENTOTHERDOCUMENTS + oStudentDocuments.getM_strOtherDocuments() + Constants.PDF_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strOtherDocuments(strStudentOtherDocumentsURL);
		 }
		 if(oStudentDocuments.getM_strVerifyScanDocument() != null)
		 {
			 String strStudentVerifiedDocumentURL = Constants.S3BUCKETURL + Constants.VERIFIEDAPPLICATION + oStudentDocuments.getM_strVerifyScanDocument() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strVerifyScanDocument(strStudentVerifiedDocumentURL);
		 }
		return oStudentDocuments;
	}

	public static void createActivityLog(String strFunctionName, GenericData oGenericData)
	{
		m_oLogger.debug("createLog - oLoginUserData.getM_strUserName() :" );
		ActivityLog oActivityLog = new ActivityLog();
		ActivityLogDataProcessor oActivityLogDataProcessor = new ActivityLogDataProcessor ();
		UserInformationData oUserInformationData = new UserInformationData();
		try
		{
			Integer nLogedUserId = getLoginUser();
			String strLoginUserName = getLoginUserName();
			if(nLogedUserId != null)
			{
				oUserInformationData.setM_nUserId(nLogedUserId);			
				oActivityLog.setM_strTaskPerformed(strFunctionName);
				oActivityLog.setM_strLoginUserName(strLoginUserName);
				oActivityLog.setM_oUserCreatedBy(oUserInformationData);
				oActivityLog.setM_oUserUpdatedBy(oUserInformationData);
				String strXMLData = oGenericData.generateXML();
				oActivityLog.setM_strXMLString(strXMLData);
				oActivityLogDataProcessor.create(oActivityLog);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}		
	}

	private static String getLoginUserName()
	{
		String strUserName = null;
		try 
		{
			ServletRequestAttributes oServletRequestAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest oHttpServletRequest = oServletRequestAttr.getRequest();		
			HttpSession oHttpSession = oHttpServletRequest.getSession();
			strUserName = (String) oHttpSession.getAttribute(Constants.LOGINUSERNAME);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("getLoginUserName - oException"+oException);
		}
		return strUserName;
	}

	public static Integer getLoginUser()
	{
		Integer nLoginUserId = null;
		try
		{
			ServletRequestAttributes oServletRequestAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest oHttpServletRequest = oServletRequestAttr.getRequest();		
			HttpSession oHttpSession = oHttpServletRequest.getSession();
			nLoginUserId = (Integer) oHttpSession.getAttribute(Constants.LOGINUSERID);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getLoginUser - oException"+oException);
		}
		return nLoginUserId;
	}

	public static long convertDateTimetoTimeStamp(Date time)
	{
		
		return 0;
	}

	public static long getSystemDateTime()
	{
		long timeStamp = 0;		
		try
		{
			Date getDate = Calendar.getInstance().getTime();
			Date oDate = new Date();
			timeStamp = oDate.getTime();			
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getSystemDateTime - oException"+oException);
		}
		return timeStamp;
	}
	
	public static String formatDate(String stringdate) 
	{
		String date = "";
		try 
		{
			String formatDate = stringdate.substring(0,10);
			SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MM-yyyy");
			date = convertFormat.format(dbFormat.parse(formatDate));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("formatDate - oException"+oException);
		}
		return date;
	}

}