package com.techmust.generic;

import java.sql.Blob;

public class AttachmentData 
{
	private String m_strFileName;
	private Blob m_oAttachment;
	
	public AttachmentData ()
	{
		setM_strFileName("");
	}

	public void setM_strFileName (String strFileName) 
	{
		this.m_strFileName = strFileName;
	}

	public String getM_strFileName () 
	{
		return m_strFileName;
	}

	public void setM_oAttachment(Blob oAttachment)
	{
		this.m_oAttachment = oAttachment;
	}

	public Blob getM_oAttachment()
	{
		return m_oAttachment;
	}

}
