package com.techmust.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3")
public class AwsS3ConfigProperties
{
	private String m_strClientRegion;	
	private String m_strBucketName;	
	private String m_strCauseImagesFolder;
	private String m_strCauseThumbnailsFolder;
	private String m_strOrganizationLogosFolder;
	
	public String getClientRegion()
	{
		return m_strClientRegion;
	}
	
	public void setClientRegion(String strClientRegion)
	{
		this.m_strClientRegion = strClientRegion;
	}
	public String getBucketName()
	{
		return m_strBucketName;
	}
	
	public void setBucketName(String strBucketName)
	{
		this.m_strBucketName = strBucketName;
	}	
	
	public String getCauseImagesFolder() 
	{
		return m_strCauseImagesFolder;
	}

	public void setCauseImagesFolder(String strCauseImagesFolder)
	{
		this.m_strCauseImagesFolder = strCauseImagesFolder;
	}

	public String getCauseThumbnailsFolder() 
	{
		return m_strCauseThumbnailsFolder;
	}

	public void setCauseThumbnailsFolder(String strCauseThumbnailsFolder)
	{
		this.m_strCauseThumbnailsFolder = strCauseThumbnailsFolder;
	}

	public String getOrganizationLogosFolder()
	{
		return m_strOrganizationLogosFolder;
	}

	public void setOrganizationLogosFolder(String m_strOrganizationLogosFolder)
	{
		this.m_strOrganizationLogosFolder = m_strOrganizationLogosFolder;
	}
}
