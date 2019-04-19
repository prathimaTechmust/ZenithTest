package com.techmust.generic.data;

import java.util.Properties;

public class AppProperties
{
	static Properties m_oProperties = new Properties ();
	public static void setProperty (String strKey, String strValue)
	{
		m_oProperties.setProperty(strKey, strValue);
	}
	
	public static String getProperty (String strKey)
	{
		return m_oProperties.getProperty(strKey);
	}
}
