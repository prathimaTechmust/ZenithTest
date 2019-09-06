package com.techmust.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.techmust.constants.Constants;

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
	
	public static String UploadToStudentAadharDocumentsFolder(String strFileName,MultipartFile oStudentAadharMultipartFile) throws AmazonServiceException,
																																   AmazonClientException,																																   
																																   IOException,
																																   InterruptedException
	{
		return UploadFile(strFileName, oStudentAadharMultipartFile);
		
	}

	public static String UploadToStudentElectricityDocumentsFolder(String strFileName,MultipartFile oStudentElectricityBillMultipartFile) throws AmazonServiceException,
																																				 AmazonClientException,
																																				 IOException,
																																				 InterruptedException
	{
		return UploadFile(strFileName, oStudentElectricityBillMultipartFile);
		
	}
	
	public static String UploadToUserImagesFolder(String strUserImageName,MultipartFile oUserImage)throws AmazonServiceException,
																										  AmazonClientException,
																										  IOException,
																										  InterruptedException
	{
		return UploadFile(strUserImageName,oUserImage);
	}	
	
	public static String UploadToStudentFatherAadharDocumentsFolder(String strFileName,MultipartFile oFatherMultipartFile) throws AmazonServiceException,
																																  AmazonClientException,
																																  IOException,
																																  InterruptedException
	{
		return UploadFile(strFileName, oFatherMultipartFile);		
	}	

	public static String UploadToStudentMarksCard2DocumentsFolder(String strFileName,	MultipartFile oStudentMarksCard2MultipartFile) throws AmazonServiceException,
																																			  AmazonClientException, 
																																			  IOException, 
																																			  InterruptedException
	{
		return UploadFile(strFileName, oStudentMarksCard2MultipartFile);	
	}
	
	public static String UploadToStudentOtherDocumentsFolder(String strFileName,MultipartFile oOtherDocumentsMultipartFile) throws AmazonServiceException,
																																   AmazonClientException,
																																   IOException,
																																   InterruptedException
	{
		return UploadFile(strFileName, oOtherDocumentsMultipartFile);	
	}

	public static String UploadToStudentMotherAadharDocumentsFolder(String strFileName,MultipartFile oMotherMultipartFile) throws AmazonServiceException,
																																  AmazonClientException,
																																  IOException,
																																  InterruptedException
	{
		return UploadFile(strFileName, oMotherMultipartFile);	
	}
	
	public static String UploadToStudentMarksCard1DocumentsFolder(String strFileName,MultipartFile oStudentMarksCard1MultipartFile) throws AmazonServiceException,
																																		   AmazonClientException, 
																																		   IOException, 
																																		   InterruptedException
	{
		return UploadFile(strFileName, oStudentMarksCard1MultipartFile);	
	}
	
	public static String UploadSealedCopyDocumentsFolder(String strFileName, MultipartFile oScanCopyMultipartFile) throws AmazonServiceException,
																														  AmazonClientException,
																														  IOException, 
																														  InterruptedException
	{
		return UploadFile(strFileName, oScanCopyMultipartFile);		
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
	
	public static String UploadExcelReport (String strFileName, File oFile) throws AmazonServiceException,AmazonClientException,IOException,InterruptedException
	{
		String strUploadFolder = "";
		InputStream oInputStream = null;
		if(oFile != null)
			oInputStream = new FileInputStream(oFile);
		try 
		{
			//Getting S3 Details
			AmazonS3 oAmazonS3Client = AmazonS3ClientBuilder.standard().withRegion(m_oAwsS3ConfigProperties.getClientRegion()).build();
			TransferManager oTransferManager = TransferManagerBuilder.standard().withS3Client(oAmazonS3Client).build();
			//Calculate File Length
			byte[] arrFileBytes = IOUtils.toByteArray(oInputStream);
			ObjectMetadata oObjectMetadata = new ObjectMetadata();
			oObjectMetadata.setContentLength(arrFileBytes.length);
			ByteArrayInputStream oByteArrayInputStream = new ByteArrayInputStream(arrFileBytes);
			
			//Set S3Bucket Object Default
			PutObjectRequest oPutObjectRequest = new PutObjectRequest(m_oAwsS3ConfigProperties.getBucketName(), strFileName, oByteArrayInputStream, oObjectMetadata);
			oPutObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			
			//Upload file to S3
			Upload oUpload = oTransferManager.upload(oPutObjectRequest);
			System.out.println("Object Upload Started");
			oUpload.waitForCompletion();
			System.out.println("Object Uploaded Completed");
			strUploadFolder = oAmazonS3Client.getUrl(m_oAwsS3ConfigProperties.getBucketName(), strFileName).toString();
		}
		catch (Exception oException)
		{
			oException.printStackTrace();
		}
		return strUploadFolder;
		
	}
		
}