package com.techmust.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;

import com.techmust.constants.Constants;
import com.techmust.scholarshipmanagement.studentdocuments.StudentDocuments;


public class Utils 
{	
	public static void saveCookie(String strCookieName, String strValue, HttpServletResponse oResponse)
	{
	    Cookie cookie = new Cookie(strCookieName, strValue);	  
	    oResponse.addCookie(cookie);
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
			 String strStudentOtherDocumentsURL = Constants.S3BUCKETURL + Constants.STUDENTOTHERDOCUMENTS + oStudentDocuments.getM_strOtherDocuments() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strOtherDocuments(strStudentOtherDocumentsURL);
		 }			 
		return oStudentDocuments;
	}
}