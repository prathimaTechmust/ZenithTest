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


public class MailService
{	
	public static Logger m_oLogger = Logger.getLogger(GenericData.class);	
	
	public static String strSubject = "Cheque is Prepared";
	public static String strFacilitatorBodyHeading = "Dear ";
	public static String strFacilitatorBodyMessage = "your following student application of scholarship with Zenith Foundation has been processed and cheque prepared.Please come and collect cheque from our office";							
	public static String strStudentBodyHeading = "Congratulations ";							
	public static String strStudentBodyMessage = "your application of scholarship with Zenith Foundation has been processed and cheque prepared.Please come and collect cheque from our office";													
	
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
	
	public void sendMailToFacilitator(String strToFacilitatorMail,String strFacilitatorName) throws MessagingException
	{
		
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
			oMessage.setText(strFacilitatorBodyHeading+strFacilitatorName+","+strFacilitatorBodyMessage);
			oSender.send(oMessage);
			System.out.println("Email Sent Sucessfully");
		}
		catch (Exception oException)
		{
			m_oLogger.debug("Send Email To User - oException"+oException);
			oException.printStackTrace();
		}		
	}	

	public void sendMailToStudent(String strToStudentEmail, String strStudentName)
	{		
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
			m_oLogger.debug("Send Email To User - oException"+oException);
			oException.printStackTrace();
		}		
	}
}
