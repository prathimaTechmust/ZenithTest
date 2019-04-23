package com.techmust.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import com.techmust.config.AwsS3ConfigProperties;

@Component
public class AWSUtils 
{
	
	private static AwsS3ConfigProperties m_oAwsS3ConfigProperties;
	
	@Autowired
	private AwsS3ConfigProperties oAwsS3ConfigProperties;
	
	@PostConstruct
	private void staticConfig()
	{
		this.m_oAwsS3ConfigProperties = oAwsS3ConfigProperties;
	}
	
	public static String UploadToStudentImagesFolder(String strFileName, MultipartFile oStudentImg) throws AmazonServiceException, 
	                                                                                    AmazonClientException, 
	                                                                                    IOException, 
	                                                                                    InterruptedException 
	{
		return UploadFile(strFileName, oStudentImg);
	}
	
	public static String UploadFile (String strFileName, MultipartFile oImage) throws AmazonServiceException, 
																			 AmazonClientException, 
																			 IOException, 
																			 InterruptedException
	{
		String strUploadedFile = "";
		InputStream oInputStream = null;
	    if(oImage != null)
	       oInputStream = oImage.getInputStream();
		try
		{
			AmazonS3 oS3Client = AmazonS3ClientBuilder.standard()
			.withRegion(m_oAwsS3ConfigProperties.getClientRegion())
			.build();
		
			TransferManager oTranMan = TransferManagerBuilder.standard()
			.withS3Client(oS3Client)
			.build();
			
			// Calculate the length
			byte[] arrImgBytes = IOUtils.toByteArray(oInputStream);
			ObjectMetadata oMetadata = new ObjectMetadata();
			oMetadata.setContentLength(arrImgBytes.length);
			ByteArrayInputStream oByteArrayInputStream = new ByteArrayInputStream(arrImgBytes);
			
			// Make s3 object public by default
			PutObjectRequest oRequest = new PutObjectRequest(m_oAwsS3ConfigProperties.getBucketName(), strFileName, oByteArrayInputStream, oMetadata);
			oRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			
			// TransferManager processes all transfers asynchronously,
			// so this call returns immediately.
			Upload oUpload = oTranMan.upload(oRequest);
			
			System.out.println("Object upload started");
			// Optionally, wait for the upload to finish before continuing.
			oUpload.waitForCompletion();
			System.out.println("Object upload complete");
			
			strUploadedFile = oS3Client.getUrl(m_oAwsS3ConfigProperties.getBucketName(), strFileName).toString();
		}
		catch (Exception oException)
		{
			oException.printStackTrace();
		}
		finally
		{
			if (oInputStream != null)
			oInputStream.close();
		}
	   return strUploadedFile;
	}	
}