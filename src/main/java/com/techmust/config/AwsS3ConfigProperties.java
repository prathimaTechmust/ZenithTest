package com.techmust.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.techmust.constants.Constants;

@Configuration
@ConfigurationProperties(prefix = "aws.s3")
public class AwsS3ConfigProperties
{
	private String m_strClientRegion = Constants.CLIENTREGION;	
	private String m_strBucketName = Constants.BUCKETNAME;	
	private String m_strStudentImagesFolder = Constants.STUDENTIMAGEFOLDER;
	
	
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

	public String getM_strClientRegion()
	{
		return m_strClientRegion;
	}

	public void setM_strClientRegion(String strClientRegion)
	{
		this.m_strClientRegion = strClientRegion;
	}

	public String getM_strStudentImagesFolder()
	{
		return m_strStudentImagesFolder;
	}

	public void setM_strStudentImagesFolder(String m_strStudentImagesFolder)
	{
		this.m_strStudentImagesFolder = m_strStudentImagesFolder;
	}	
}
