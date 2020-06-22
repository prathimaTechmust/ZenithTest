package com.techmust.utils;


import java.util.Date;
import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.techmust.generic.data.GenericData;
import com.techmust.scholarshipmanagement.student.StudentInformationData;

public class AmazonSMS 
{
	public static Logger m_oLogger = Logger.getLogger(GenericData.class);
	public static String strDearHeading = "Dear";
	public static String strFacilitatorBodyStartMessage = " Your following student ";							
	public static String strStudentUID = "UID : ";
	public static String strStudentName = " Name : ";
	public static String strFacilitatorBodyEndMessage = "application of scholarship with Zenith Foundation has been processed and cheque is prepared.Please come and collect cheque from our office in next 4 working days.";
	public static String strStudentBodyHeading = "Congratulations ";							
	public static String strStudentBodyMessage = " your application of scholarship with Zenith Foundation has been processed and cheque prepared. Kindly collect your cheque from the office after 4 working days.\r\n" + 
												"\r\n" + 
												"Thanks & Regards\r\n" + 
												"\r\n" + 
												"Zenith Foundation";
	public static String strCounselingBodyHeading = " you have been called for counsellig at Zenith Foundation on (Date and Time).\r\n" + 
													"\r\n" + 
													"Thanks & Regards\r\n" + 
													"\r\n" + 
													"Zenith Foundation";
		
	
	
	public static AmazonSNS getClient ()
    {
		m_oLogger.info("getClientDetails");
		 BasicAWSCredentials oBasicAWSCredentials = new BasicAWSCredentials (System.getProperty("AWS_ACCESS_KEY"), System.getProperty("AWS_SECRET_KEY"));
		 AmazonSNS oAmazonSNSClient = AmazonSNSClient
		         .builder()
		         .withRegion("ap-southeast-1")
		         .withCredentials(new AWSStaticCredentialsProvider(oBasicAWSCredentials))
		         .build();			 
		 return oAmazonSNSClient;
    }
	 
	@SuppressWarnings("unused")
	public static void sendSMSToStudent (String strPhoneNumber,String strStudentName)
	{
		m_oLogger.info("sendSMSToStudent");
		m_oLogger.debug("sendSMSToStudent - strPhoneNumber [IN] :"+strPhoneNumber);
		m_oLogger.debug("sendSMSToStudent - strStudentName [IN] :"+strStudentName);
		try
		{
			AmazonSNSClient oAmazonSNSClient = (AmazonSNSClient) getClient();
			PublishResult oPublishResult = oAmazonSNSClient.publish(new PublishRequest()
	                 .withMessage(strStudentBodyHeading+strStudentName+","+strStudentBodyMessage)
	                 .withPhoneNumber(strPhoneNumber));
		}
		catch (Exception oException)
		{
			m_oLogger.error("sendSMSToStudent - oException"+oException);
		}
	}
	
	@SuppressWarnings("unused")
	public static void sendSMSToFacilitator (String strPhoneNumber,String strFacilitatorName, StudentInformationData oStudentInformationData)
	{
		m_oLogger.info("sendSMSToFacilitator");
		m_oLogger.debug("sendSMSToFacilitator - strPhoneNumber [IN] :"+strPhoneNumber);
		m_oLogger.debug("sendSMSToFacilitator - strFacilitatorName [IN] :"+strFacilitatorName);
		m_oLogger.debug("sendSMSToFacilitator - oStudentInformationData [IN] :"+oStudentInformationData.toString());
		try
		{
			AmazonSNSClient oAmazonSNSClient = (AmazonSNSClient) getClient();
			PublishResult oPublishResult = oAmazonSNSClient.publish(new PublishRequest()
	                 .withMessage(strDearHeading+strFacilitatorName+","+strFacilitatorBodyStartMessage+strStudentUID+oStudentInformationData.getM_nUID()+strStudentName+oStudentInformationData.getM_strStudentName()+strFacilitatorBodyEndMessage)
	                 .withPhoneNumber(strPhoneNumber));
		}
		catch (Exception oException)
		{
			m_oLogger.error("sendSMSToFacilitator - oException"+oException);
		}		
	} 
	
    @SuppressWarnings("unused")
	public  static void sendSmsToCounselingCandidate(String strStudentName, String strPhoneNumber, Date dCounselingDate) 
	 {
    	 m_oLogger.info("sendSmsToCounselingCandidate");
		 m_oLogger.debug("sendSmsToCounselingCandidate - strStudentName [IN] :"+strStudentName);
		 m_oLogger.debug("sendSmsToCounselingCandidate - strPhoneNumber [IN] :"+strPhoneNumber);
		 m_oLogger.debug("sendSmsToCounselingCandidate - dCounselingDate [IN] :"+dCounselingDate);
		 try
		 {
			 AmazonSNSClient oAmazonSNSClient = (AmazonSNSClient) getClient();
			 PublishResult oPublishResult = oAmazonSNSClient.publish(new PublishRequest()
					 .withMessage(strDearHeading+strStudentName+","+strCounselingBodyHeading+"," + dCounselingDate)
					 .withPhoneNumber(strPhoneNumber));
					 
		 }
		 catch (Exception oException) 
		 {
			 m_oLogger.error("sendSmsToCounselingCandidate - oException"+oException);
		 }		
	}
}
