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
}