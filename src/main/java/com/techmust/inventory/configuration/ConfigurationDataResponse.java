package com.techmust.inventory.configuration;

import java.util.ArrayList;
import com.techmust.generic.response.GenericResponse;

public class ConfigurationDataResponse extends GenericResponse 
{
	private static final long serialVersionUID = 1L;
    public ArrayList<ConfigurationData> m_arrConfigurationData ;
    public long m_nRowCount;
    
    public ConfigurationDataResponse ()
	{
    	m_arrConfigurationData = new ArrayList<ConfigurationData> ();
    	m_nRowCount=0;
	}
}
