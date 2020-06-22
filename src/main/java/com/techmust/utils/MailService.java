package com.techmust.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import com.techmust.generic.data.GenericData;
import com.techmust.scholarshipmanagement.student.StudentInformationData;


public class MailService
{	
	public static Logger m_oLogger = Logger.getLogger(GenericData.class);	
	
	public static String strSubject = "Cheque is Prepared";
	public static String strFacilitatorBodyHeading = "Dear ";
	public static String strFacilitatorBodyStartMessage = "your following student ";
	public static String strStudentUID = "UID : ";
	public static String strStudentName = " Name : ";
	public static String strFacilitatorBodyEndMessage = " application of scholarship with Zenith Foundation has been processed and cheque is prepared.Please come and collect cheque from our office in next 4 working days.";							
	public static String strStudentBodyHeading = "Congratulations ";							
	public static String strStudentBodyMessage = "your application of scholarship with Zenith Foundation has been processed and cheque prepared. Kindly collect your cheque from the office after 4 working days.\r\n" + 
													"\r\n" + 
													"Thanks & Regards\r\n" + 
													"\r\n" + 
													"Zenith Foundation";													
	
	private Properties setEmailProperties()
	{
		Properties oEmailProperties = new Properties();		
		oEmailProperties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		oEmailProperties.put("mail.smtp.auth", true);		
		oEmailProperties.put("mail.debug", "true");
		oEmailProperties.put("mail.transport.protocol", "smtp");
		oEmailProperties.put("mail.smtp.starttls.enable","true");
		return oEmailProperties;		
	}
	
	public void sendMailToFacilitator(String strToFacilitatorMail,String strFacilitatorName, StudentInformationData oStudentInformationData) throws MessagingException
	{
		m_oLogger.info("sendMailToFacilitator");
		m_oLogger.debug("sendMailToFacilitator - strToFacilitatorMail [IN] :"+strToFacilitatorMail);
		m_oLogger.debug("sendMailToFacilitator - strFacilitatorName [IN] :"+strFacilitatorName);
		m_oLogger.debug("sendMailToFacilitator - oStudentInformationData [IN] :"+oStudentInformationData.toString());
		try
		{
			Session oMailSession = Session.getInstance(setEmailProperties(),new Authenticator()
																				{
																					@Override
																					protected PasswordAuthentication getPasswordAuthentication()
																					{
																						return new PasswordAuthentication(System.getProperty("EMAIL_ID").toString(),System.getProperty("EMAIL_PASSWORD").toString());
																					}
																				});
			JavaMailSenderImpl oSender = new JavaMailSenderImpl();			
			oSender.setSession(oMailSession);
			oSender.setHost("smtp.gmail.com");
			oSender.setPort(587);						
			SimpleMailMessage oMessage = new SimpleMailMessage();
			oMessage.setTo(strToFacilitatorMail);
			oMessage.setSubject(strSubject);
			oMessage.setText(strFacilitatorBodyHeading+strFacilitatorName+","+strFacilitatorBodyStartMessage+strStudentUID+oStudentInformationData.getM_nUID()+strStudentName+oStudentInformationData.getM_strStudentName()+strFacilitatorBodyEndMessage);
			oSender.send(oMessage);
			System.out.println("Email Sent Sucessfully");
		}
		catch (Exception oException)
		{
			m_oLogger.error("Send Email To Facilitator - oException"+oException);
			oException.printStackTrace();
		}		
	}	

	public void sendMailToStudent(String strToStudentEmail, String strStudentName)
	{	
		m_oLogger.info("sendMailToStudent");
		m_oLogger.debug("sendMailToStudent - strToStudentEmail [IN] : "+strToStudentEmail);
		m_oLogger.debug("sendMailToStudent - strStudentName [IN] : "+strStudentName);
		try
		{
			Session oMailSession = Session.getInstance(setEmailProperties(),new Authenticator()
																				{
																					@Override
																					protected PasswordAuthentication getPasswordAuthentication()
																					{
																						return new PasswordAuthentication(System.getProperty("EMAIL_ID").toString(),System.getProperty("EMAIL_PASSWORD").toString());
																					}
																				});
			JavaMailSenderImpl oSender = new JavaMailSenderImpl();			
			oSender.setSession(oMailSession);
			oSender.setHost("smtp.gmail.com");
			oSender.setPort(587);						
			SimpleMailMessage oMessage = new SimpleMailMessage();
			oMessage.setTo(strToStudentEmail);
			oMessage.setSubject(strSubject);
			oMessage.setText(strStudentBodyHeading+strStudentName+","+strStudentBodyMessage);
			oSender.send(oMessage);
			System.out.println("Email Sent Sucessfully");
		}
		catch (Exception oException)
		{
			m_oLogger.error("Send Email To Student - oException"+oException);
			oException.printStackTrace();
		}		
	}
}
