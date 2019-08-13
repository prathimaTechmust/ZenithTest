package com.techmust.zenith.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.techmust.generic.util.GenericUtil;

public class ZenithUtil extends GenericUtil
{
	public static String getTime(Date oDate) 
	{
		String strTime = "";
		DateFormat oDateFormat = new SimpleDateFormat("hh:mm a");
		strTime = oDateFormat.format(oDate.getTime());
		return strTime;
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
