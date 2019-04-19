package com.techmust.clientmanagement;

public enum SiteStatus 
{
	kActive, kInactive;
	private static final String [] m_arrSiteStatus = {"Active", "Inactive"};
	
	public String toString ()
	{
		return m_arrSiteStatus [this.ordinal ()];
	}
}
