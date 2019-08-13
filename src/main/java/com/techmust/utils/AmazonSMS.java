package com.techmust.utils;


import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.techmust.generic.data.GenericData;

public class AmazonSMS 
{
	public static Logger m_oLogger = Logger.getLogger(GenericData.class);
	public static String strFacilitatorBodyHeading = "Dear ";
	public static String strFacilitatorBodyMessage = "Your following student application of scholarship with Zenith Foundation has been processed and cheque prepared.Please come and collect cheque from our office";							
	public static String strStudentBodyHeading = "Congratulations ";							
	public static String strStudentBodyMessage = "Your application of scholarship with Zenith Foundation has been processed and cheque prepared.Please come and collect cheque from our office";
	public static String strCounselingBodyHeading = "Dear ";
		
	
	
	public static AmazonSNS getClient ()
    {
		m_oLogger.info("getClientDetails");
		 BasicAWSCredentials oBasicAWSCredentials = new BasicAWSCredentials (System.getenv("AWS_ACCESS_KEY"), System.getenv("AWS_SECRET_KEY"));
		 AmazonSNS oAmazonSNSClient = AmazonSNSClient
		         .builder()
		         .withRegion("ap-southeast-1")
		         .withCredentials(new AWSStaticCredentialsProvider(oBasicAWSCredentials))
		         .build();			 
		 return oAmazonSNSClient;
    }
	 
	public static void sendSMSToStudent (String strPhoneNumber,String strStudentName)
	{
		try
		{
			AmazonSNSClient oAmazonSNSClient = (AmazonSNSClient) getClient();
			PublishResult oPublishResult = oAmazonSNSClient.publish(new PublishRequest()
	                 .withMessage(strStudentBodyHeading+strStudentName+","+strStudentBodyMessage)
	                 .withPhoneNumber(strPhoneNumber));
		}
		catch (Exception oException)
		{
			m_oLogger.error("sendSMS - oException"+oException);
		}
		
	}
	
	public static void sendSMSToFacilitator (String strPhoneNumber,String strFacilitatorName)
	{
		try
		{
			AmazonSNSClient oAmazonSNSClient = (AmazonSNSClient) getClient();
			PublishResult oPublishResult = oAmazonSNSClient.publish(new PublishRequest()
	                 .withMessage(strFacilitatorBodyHeading+strFacilitatorName+","+strFacilitatorBodyMessage)
	                 .withPhoneNumber(strPhoneNumber));
		}
		catch (Exception oException)
		{
			m_oLogger.error("sendSMS - oException"+oException);
		}
		
	} 
	 public  static void sendSmsToCounselingCandidate(String strPhoneNumber,String strStudentName, String strStudentRemarks) 
	 {
		 try
		 {
			 AmazonSNSClient oAmazonSNSClient = (AmazonSNSClient) getClient();
			 PublishResult oPublishResult = oAmazonSNSClient.publish(new PublishRequest()
					 .withMessage(strCounselingBodyHeading+strStudentName+"," + strStudentRemarks)
					 .withPhoneNumber(strPhoneNumber));
					 
		 }
		 catch (Exception oException) 
		 {
			 m_oLogger.error("sendSMS - oException"+oException);
		}
		
	}
}
