package com.techmust.clientmanagement;

import java.util.ArrayList;

import com.techmust.generic.response.GenericResponse;

public class SiteDataResponse extends GenericResponse
{
	public ArrayList<SiteData> m_arrSiteData;
	private static final long serialVersionUID = 1L;
	public SiteDataResponse ()
	{
		m_arrSiteData = new ArrayList<SiteData> ();
	}
}
