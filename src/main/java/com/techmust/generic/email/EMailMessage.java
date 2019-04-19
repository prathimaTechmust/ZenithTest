package com.techmust.generic.email;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import org.apache.log4j.Logger;
import com.sun.mail.imap.IMAPFolder;
import com.techmust.generic.data.AppProperties;
import com.techmust.generic.util.GenericUtil;
import com.techmust.usermanagement.userinfo.IUserInformationData;


public class EMailMessage 
{
	private static final Logger m_oLogger = Logger.getLogger(EMailMessage.class);
	public static final String kDefaultProtocol = "imap";
	public static final String kMailUserKey = "user";
	public static final String kMailUserPasswordKey = "password";
	public static final String kMailHostKey = "host";
	public static final String kMailPortNumberKey = "port";
	public static final String kMailProtocolKey = "protocol";

	private Message m_oMessage;
	private static Session m_koMailSession = null;
	
	public EMailMessage ()
	{
		createMessageObject ();
	}
	
	public static void forgetMailSession ()
	{
		m_koMailSession = null;
	}
	
	public void setFrom (String strAddress)
	{
		m_oLogger.info("setFrom");
		m_oLogger.debug("setFrom - strAddress [IN] : " +strAddress);
		try
		{
			m_oMessage.setFrom (new InternetAddress (strAddress));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("setFrom : oException : " +oException);
		}
	}
	
	public void setSubject (String strSubject)
	{
		m_oLogger.info("setSubject");
		m_oLogger.debug("setSubkect strSubject [IN] : " +strSubject);
		try
		{
			m_oMessage.setSubject (strSubject);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("setSubject : oException : " +oException);
		}
	}
	
	public void setContent (String strContent, String strPlainText)
	{
		m_oLogger.info("setContent");
		try
		{
 			String strHtmlText  = strContent;
			MimeMultipart oMultipartContent = new MimeMultipart();
			BodyPart oTextPart = new MimeBodyPart();
			BodyPart oHtmlPart = new MimeBodyPart();
			
			oTextPart.setText (strPlainText);
			oHtmlPart.setContent(strHtmlText, "text/html");
			oMultipartContent.addBodyPart (oHtmlPart);
			oMultipartContent.addBodyPart(oTextPart);
			m_oMessage.setContent (oMultipartContent);
		}
		catch (Exception oException)
		{
			m_oLogger.error (" setContent - oException : " +oException);
		}
	}
	
	public void setReplyTo (String strReplyTo)
	{
		m_oLogger.info("setReplyTo");
		m_oLogger.debug("setReplyTo - setReplyTo [IN] : " +strReplyTo);
		try
		{
			InternetAddress[] oAddress = 
			{
				new InternetAddress(strReplyTo)
			};
			m_oMessage.setReplyTo (oAddress);
		}
		catch (Exception oException)
		{
			m_oLogger.error (" setReplyTo - oException : " +oException);
		}
	}

	public void setRecipients (String strEmailTo)
	{
		m_oLogger.info("setRecipients");
		m_oLogger.debug("setRecipients - strEmailTo [IN] : " +strEmailTo);
		String[] strMailIDArray = strEmailTo.split (",");
		InternetAddress[] oAddress = new InternetAddress [strMailIDArray.length];
		try
		{
			if (strMailIDArray.length == 1)
				oAddress[0] = new InternetAddress (strEmailTo);
			else
			{
				for (int nIndex = 0; nIndex < strMailIDArray.length; nIndex++)
					oAddress [nIndex] = new InternetAddress (strMailIDArray[nIndex]);
			}
			m_oMessage.setRecipients (Message.RecipientType.TO, oAddress);
		}
		catch (Exception oException)
		{
			m_oLogger.error (" setRecipients -  oException : " +oException);
		}
	}

	public void sendMail () throws Exception
	{
		m_oLogger.info ("sendMail");
		try
		{
			m_oLogger.debug (" sendMail : Mail sending started [MESSAGE] ");
			Transport.send (m_oMessage);
			m_oLogger.debug (" sendMail : Mail sending finished [MESSAGE] ");
		}
		catch (Exception oException)
		{
			m_oLogger.error (" sendMail - oException : " +oException);
			throw oException;
		}
	}
	
	public void prepareMail (IUserInformationData oUserInformationData, String strPassword)
	{
		m_oLogger.info("prepareMail");
		m_oLogger.debug("prepareMail - oUserInformationData [IN] : " +oUserInformationData);
		m_oLogger.debug("prepareMail - strPassword [IN] : " +strPassword);
		try 
		{
			String strSubject =  "login details";
			String strDestEmailId = oUserInformationData.getM_strEmailAddress();
			String strUserName = oUserInformationData.getM_strUserName();
			String strLoginId = oUserInformationData.getM_strLoginId();
			String strContent = generateContent(strUserName, strPassword, strLoginId);
			GenericUtil.sendEmail (strDestEmailId, strSubject, strContent, "");
		}
		catch (Exception oException) 
		{
			m_oLogger.error("prepareMail - oException : " +oException);
		}
	}
	
	public Message[] receiveMail ()
	{
		m_oLogger.info("receiveMail");
		Message[] oMessages = null;
		try 
		{
			EMailSettingsData oEmailSettingsData = new EMailSettingsData ();
			oEmailSettingsData.setM_strEmailUserAddress(GenericUtil.getProperty ("kMAILPROPERTYUSER"));
			oEmailSettingsData.setM_strPassword(GenericUtil.getProperty ("kMAILPROPERTYPASSWORD"));
			oEmailSettingsData.setM_strReceiveEmailHost(GenericUtil.getProperty ("kMAILPROPERTYHOST"));
			oMessages = receiveMail (oEmailSettingsData);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("receiveMail - oException" +oException);
		}
		m_oLogger.debug("receiveMail - oMessages [OUT] : " +oMessages);
		return oMessages;
	}
	
	public Message[] receiveMail (EMailSettingsData oEmailSettingsData) throws Exception
	{
		m_oLogger.info("receiveMail");
		m_oLogger.debug("receiveMail - strLogin [IN] : " +oEmailSettingsData.getM_strEmailUserAddress());
		m_oLogger.debug("receiveMail - strPassword [IN] : " +oEmailSettingsData.getM_strPassword());
		Message[] arrMessages = null;
		try
		{ 
			Properties oProperties = new Properties ();
			setReceiveMailProperties (oProperties, oEmailSettingsData);
			Session oMailReceiveSession = Session.getDefaultInstance(oProperties, null);
			Store  oStore =  oMailReceiveSession.getStore("imaps");
			arrMessages = getMessages (oStore, oProperties, oEmailSettingsData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("receiveMail - oException " +oException);
			throw oException;
		}
		m_oLogger.debug("receiveMail - oMessages [OUT] : " +arrMessages);
		return arrMessages;
	}

	public void sendMail (EMailSettingsData oEmailSettingsData, String strRecipientsEmailID, 
			String strMessageSubject, String strContent, String strPlainText, String strAttachFilePath) throws Exception
	{
		m_oLogger.info("sendMail");
		m_oLogger.debug("sendMail - strLogin [IN] : " +oEmailSettingsData.getM_strEmailUserAddress());
		m_oLogger.debug("sendMail - strPassword [IN] : " +oEmailSettingsData.getM_strPassword());
		Message[] arrMessages = null;
		try
		{ 
			Properties oProperties = new Properties ();
			setSendMailProperties (oProperties, oEmailSettingsData);
			Authenticator oAuthenticator = new PopupAuthenticator (oEmailSettingsData.getM_strEmailUserAddress(), oEmailSettingsData.getM_strPassword());
			Session oMailSession = Session.getInstance(oProperties, oAuthenticator);
			oMailSession.setDebug (false);
			m_oMessage = new MimeMessage (oMailSession);
			sendMail(oEmailSettingsData.getM_strEmailUserAddress(), oEmailSettingsData.getM_strEmailUserAddress(), strRecipientsEmailID, 
						strMessageSubject, strContent, strPlainText, strAttachFilePath);
		}
		catch (Exception oException)
		{
			m_oLogger.error("receiveMail - oException " +oException);
			throw oException;
		}
		m_oLogger.debug("receiveMail - oMessages [OUT] : " +arrMessages);
	}
	public MessageData getMessageData (Message oMessage) throws Exception
	{
		MessageData oMessageData = new MessageData ();
		if (!oMessage.getFolder().isOpen())
			oMessage.getFolder().open(Folder.READ_WRITE);
		MimeMultipart oMultipart = (MimeMultipart) oMessage.getContent();
		getAllRecipients (oMessageData, oMessage.getAllRecipients());
		getMultipleContents (oMultipart, oMessageData);
		oMessageData.setM_strSubject (oMessage.getSubject());
		oMessageData.setM_strMessageFrom(getMessageFromInfo (oMessage.getFrom()));
		if (oMessage.getFolder().isOpen())
			oMessage.getFolder().close(false);
		return oMessageData;
	}
	
	public EMailSettingsData buildEmailSetting(String strHandlerData) 
	{
		EMailSettingsData oEmailSettingsData = new EMailSettingsData ();
		oEmailSettingsData.setM_strEmailUserAddress(getMailSettingsValue (strHandlerData, kMailUserKey));
		oEmailSettingsData.setM_strPassword(getMailSettingsValue (strHandlerData, kMailUserPasswordKey));
		oEmailSettingsData.setM_strReceiveEmailHost(getMailSettingsValue (strHandlerData, kMailHostKey));
		oEmailSettingsData.setM_strReceiveEmailPort(getMailSettingsValue (strHandlerData, kMailPortNumberKey));
		oEmailSettingsData.setM_strReceiveEmailProtocol(getMailSettingsValue (strHandlerData, kMailProtocolKey));
		oEmailSettingsData.setM_strSendEmailProtocol ("smtp");
		oEmailSettingsData.setM_strSendEmailHost ("smtp.gmail.com");
		oEmailSettingsData.setM_strSendEmailPort ("587");

		return oEmailSettingsData;
	}
	
	public boolean sendMail (String strFrom, String strReplyTo,  String strRecipientsEmailID, String strMessageSubject, String strContent, 
							String strPlainText, String strAttachments) throws Exception
	{
		boolean bSuccess = false;
		try
		{
			if (strFrom != null && !strFrom.isEmpty() && strReplyTo != null && !strReplyTo.isEmpty())
			{
				setFrom(strFrom);
				setSubject(strMessageSubject);
				setReplyTo(strReplyTo);
				setRecipients(strRecipientsEmailID);
				setContentWithAttachment (strContent, strAttachments);
				sendMail();
				bSuccess = true;
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("sendEmail - Exception : " +oException);
			throw oException;
		}
		m_oLogger.debug("sendEmail - bSuccess [OUT] : " +bSuccess);
		return bSuccess;
	}
	
	private void setContentWithAttachment (String strContent , String strAttachments)
	{
		m_oLogger.info("setAttachment");
		m_oLogger.debug("setAttachment - strAttachments [IN] : " + strAttachments);
		try
		{
				Multipart oMultipart = new MimeMultipart();
				BodyPart oHtmlPart = new MimeBodyPart();
				oHtmlPart.setContent(strContent, "text/html");
				oMultipart.addBodyPart (oHtmlPart);
				if (strAttachments.trim().length() > 0)
				{
					String  [] arrAttachments = strAttachments.split(",");
					for(int nIndex = 0; nIndex < arrAttachments.length; nIndex++)
						addAttachments (arrAttachments[nIndex], oMultipart);
				}
				m_oMessage.setContent(oMultipart);
		}
		catch (Exception oException)
		{
			m_oLogger.error("setAttachment - oException " +oException);
		}
	}
	
	private void addAttachments(String strAttachFileName, Multipart oMultipart) throws MessagingException
	{
		MimeBodyPart oBodyPart = new MimeBodyPart ();
		String strAttachFilePath = GenericUtil.getProperty("kATTACMENT_FOLDER_PATH") + File.separator + strAttachFileName;
		FileDataSource oFileSource = new FileDataSource (new File (strAttachFilePath));
		oBodyPart.setDataHandler(new DataHandler(oFileSource));
		oBodyPart.setHeader("Content-ID", "<image>");
		oBodyPart.setFileName(new File(strAttachFilePath).getName());
		oBodyPart.setDisposition(MimeBodyPart.INLINE);
		oMultipart.addBodyPart(oBodyPart);
	}
	
	private String getMessageFromInfo(Address[] arrMessageFrom)
    {
		String strFromAddress = "";
		if (arrMessageFrom != null && arrMessageFrom.length > 0)
			strFromAddress = arrMessageFrom[0].toString();
		return strFromAddress;
    }

	private Message[] getMessages (Store oStore, Properties oProperties, EMailSettingsData oEmailSettingsData) throws Exception 
	{
		m_oLogger.info ("getMessages");
		m_oLogger.debug("getMessages - oStore [IN] : " +oStore);
		Message[] arrMessages = null;
		try 
		{
			if (!oStore.isConnected())
			oStore.connect(oEmailSettingsData.getM_strReceiveEmailHost(), 
					oEmailSettingsData.getM_strEmailUserAddress(), oEmailSettingsData.getM_strPassword());
			IMAPFolder oFolder = (IMAPFolder) (oStore.getFolder("INBOX"));
			oFolder.open(Folder.READ_WRITE);
			arrMessages = oFolder.search(new FlagTerm (new Flags (Flag.SEEN), false));
			oFolder.close(true);
		}
		catch (IllegalStateException oIllegalStateException)
		{
			m_oLogger.error("getMessages - oIllegalStateException " +oIllegalStateException);
			throw oIllegalStateException;
		}
		catch (MessagingException oMessagingException)
		{
			m_oLogger.error("getMessages - oMessagingException " +oMessagingException);
			throw oMessagingException;
		}
		catch (Exception oException) 
		{
			m_oLogger.error("getMessages - oException " +oException);
			throw oException;
		}
		finally
		{
			oStore.close();
		}
		return arrMessages;
	}
	
	private void getAllRecipients (MessageData oMessageData, Address [] oAddresses)
	{
		m_oLogger.info ("getAllRecipients");
		m_oLogger.debug ("getAllRecipients - oMessageData [IN] : " +oMessageData);
		m_oLogger.debug ("getAllRecipients - oAddresses [IN] : " +oAddresses);
		ArrayList<String> oRecipientsArrayList = new ArrayList<String> ();
		try
		{
			for (int nAddressIndex = 0 ; nAddressIndex < oAddresses.length ; nAddressIndex++)
				oRecipientsArrayList.add(oAddresses[nAddressIndex].toString());
			oMessageData.setM_arrRecipient(oRecipientsArrayList);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getAllRecipients - oException " +oException);
		}
	}

	private void getMultipleContents (MimeMultipart oMultipart, MessageData oMessageData) throws Exception 
	{
		m_oLogger.info ("getMultipleContents");
		m_oLogger.debug ("getMultipleContents - oMultipart [IN] " +oMultipart);
		m_oLogger.debug ("getMultipleContents - oMessageData [IN] " +oMessageData);
		try
		{
			for (int nPartIndex = 0; nPartIndex < oMultipart.getCount(); nPartIndex++)
			{
				BodyPart oPart = oMultipart.getBodyPart (nPartIndex);
				String strContentType = oPart.getContentType();
				if (strContentType.contains ("multipart"))
					getBodyTextOfMultiPart (oPart, oMessageData);
				if ((Part.ATTACHMENT).equals(oPart.getDisposition()))
					getAttachment (oPart, oMessageData);
				if (oPart.isMimeType("text/plain"))
					oMessageData.setM_strContent (oPart.getContent().toString());
			}
		}
		catch (MessagingException oMessagingException)
		{
			m_oLogger.error("getMultipleContents - oMessagingException" +oMessagingException);
			throw oMessagingException;
		}
		catch (Exception oException) 
		{
			m_oLogger.error("getMultipleContents - oException" +oException);
			throw oException;
		}
	}

	private void getAttachment (BodyPart oPart, MessageData oMessageData) throws Exception
	{
		m_oLogger.info ("getAttachment");
		m_oLogger.debug("getAttachment - oPart [IN] : " +oPart);
		m_oLogger.debug("getAttachment - oMessageData [IN] : " +oMessageData);
		String strAttachFileName = "";
		try 
		{
			strAttachFileName = oPart.getFileName();
			oMessageData.getM_arrAttachments().add(strAttachFileName);
			InputStream oInputStream = (InputStream) oPart.getInputStream();
			saveFile (strAttachFileName, oInputStream);
		}
		catch (MessagingException oMessagingException)
		{
			m_oLogger.error("getAttachment - oMessagingException " +oMessagingException);
			throw oMessagingException;
		}
		catch (Exception oException)
		{
			m_oLogger.error("getAttachment - oException" +oException);
			throw oException;
		}
	}

	private void getBodyTextOfMultiPart (BodyPart oPart, MessageData oMessageData) throws Exception 
	{
		m_oLogger.info ("getBodyTextOfMultiPart");
		m_oLogger.debug ("getBodyTextOfMultiPart - oPart [IN] : " +oPart);
		m_oLogger.debug ("getBodyTextOfMultiPart - oMessageData [IN] : " +oMessageData);
		try 
		{	
			MimeMultipart oMultiBodyPart = new MimeMultipart (oPart.getDataHandler().getDataSource());
			for (int nSubpart = 0; nSubpart < oMultiBodyPart.getCount(); nSubpart++)
			{
				BodyPart oInternalPart = oMultiBodyPart.getBodyPart(nSubpart);
				String strDisposition = oInternalPart.getDisposition();
				oMessageData.setM_strContent(strDisposition == null ? oInternalPart.getContent().toString():oMessageData.getM_strContent());
			}
		}
		
		catch (MessagingException oMessagingException)
		{
			m_oLogger.error("getBodyTextOfMultiPart - oMessagingException" + oMessagingException);
			throw oMessagingException;
		}
		catch (Exception oException) 
		{
			m_oLogger.error("getBodyTextOfMultiPart - oException" + oException);
			throw oException;
		}
	}

	private void setReceiveMailProperties (Properties oProperties, EMailSettingsData oEmailSettingsData)
	{
		m_oLogger.info("setMailProperties");
		m_oLogger.debug("setMailProperties - oProperties [IN] : " + oProperties);
		try
		{
			oProperties.setProperty("mail.store.protocol", !oEmailSettingsData.getM_strReceiveEmailProtocol().isEmpty() ? oEmailSettingsData.getM_strReceiveEmailProtocol() : kDefaultProtocol);
			oProperties.setProperty("mail.imap.host", oEmailSettingsData.getM_strReceiveEmailHost());
			if (!oEmailSettingsData.getM_strReceiveEmailPort().isEmpty())
				oProperties.setProperty("mail.imap.port", oEmailSettingsData.getM_strReceiveEmailPort());
			
			
		}
		catch (Exception oException)
		{
			m_oLogger.error("setMailProperties - oException" +oException);
		}
	}
	
	private void setSendMailProperties (Properties oProperties, EMailSettingsData oEmailSettingsData)
	{
		m_oLogger.info("setSendMailProperties");
		m_oLogger.debug("setSendMailProperties - oProperties [IN] : " + oProperties);
		try
		{
			Properties oDefaultMailProperties = getMailProperties ();
			oProperties.setProperty ("mail.smtp.host", oEmailSettingsData.getM_strSendEmailHost());
			oProperties.setProperty ("mail.smtp.port", oEmailSettingsData.getM_strSendEmailPort());
			oProperties.setProperty("mail.smtp.auth", oDefaultMailProperties.getProperty("mail.smtp.auth"));
			oProperties.setProperty("mail.smtp.starttls.enable", oDefaultMailProperties.getProperty("mail.smtp.starttls.enable"));
			oProperties.setProperty("mail.smtp.host", oDefaultMailProperties.getProperty("mail.smtp.host"));
			oProperties.setProperty ("mail.user", oEmailSettingsData.getM_strEmailUserAddress());
			oProperties.setProperty ("mail.password", oEmailSettingsData.getM_strPassword());
			oProperties.setProperty ("mail.smtp.connectiontimeout", GenericUtil.getProperty ("kCONNECTION_TIMEOUT"));
			oProperties.setProperty ("mail.smtp.timeout", GenericUtil.getProperty ("kIOSOCKET_TIMEOUT"));
		}
		catch (Exception oException)
		{
			m_oLogger.error("setSendMailProperties - oException" +oException);
		}
	}
	
	private void saveFile (String strFileName, InputStream oInputStream)
	{
		m_oLogger.info("saveFile");
		m_oLogger.debug("saveFile - strFileName [IN] :  " +strFileName);
		m_oLogger.debug("saveFile - oInputStream  [IN] : " +oInputStream);
		int nDataStream ;
		try 
		{
			File oFile = new File(GenericUtil.getProperty("kDOWNLOADFOLDER")+strFileName);
			FileOutputStream oFileOutputStream = new FileOutputStream (oFile);
			BufferedOutputStream oBufferedOutputStream = new BufferedOutputStream (oFileOutputStream);
			BufferedInputStream oBufferedInputStream = new BufferedInputStream (oInputStream);
			while ((nDataStream = oBufferedInputStream.read()) != -1)
				oBufferedOutputStream.write(nDataStream);
			oBufferedOutputStream.close();
			oBufferedInputStream.close();
		} 
		catch (Exception oException)
		{
			m_oLogger.error("saveFile - oException" + oException);
		}
	}
	
	private void createMessageObject ()
	{
		m_oLogger.info("createMessageObject");
	
		try 
		{
			createMailSession ();
			m_oMessage = new MimeMessage (m_koMailSession);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("createMessageObject - oException : " +oException);
		}
	}

	private void createMailSession()
    {
		try
		{
			do
			{
				if (m_koMailSession != null)
					break;
				Properties oProperties = getMailProperties ();
				String strLogin = oProperties.getProperty("mail.smtp.user");
				String strPassword = oProperties.getProperty("mail.smtp.password");
				java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
				m_oLogger.debug("createMessageObject - oProperties : " + oProperties.toString());
				if (!strLogin.isEmpty () && strLogin != null && strPassword != null && !strPassword.isEmpty ())
				{
					oProperties.setProperty ("mail.smtp.auth", "true");
					Authenticator oAuthenticator = new PopupAuthenticator (strLogin, strPassword);
					m_koMailSession = Session.getInstance (oProperties, oAuthenticator);
				}
				else
				{
					oProperties.setProperty ("mail.smtp.auth", "false");
					m_koMailSession = Session.getInstance (oProperties);
				}
				m_koMailSession.setDebug (false);
			} while (false);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createMailSession - oException : " , oException);
		}
    }

	private Properties getMailProperties() 
	{
		m_oLogger.info("getMailProperties");
		String strPropertyFile = AppProperties.getProperty("MAIL_PROPERTY_FILE");
		Properties oProperties = new Properties();
		try
		{
			FileInputStream oFileInputStream = new FileInputStream(strPropertyFile);
			oProperties.load(oFileInputStream);
			oFileInputStream.close();

		}
		catch (Exception oException)
		{
			m_oLogger.error("getMailProperties - oException : " , oException);
		}
		return oProperties;
	}

	private String generateContent (String strUserName, String strPassword, String strLoginId) 
	{
		m_oLogger.info("generateContent");
		m_oLogger.debug("generateContent - strUserName [IN] : " +strUserName);
		m_oLogger.debug("generateContent - strPassword [IN] : " +strPassword);
		m_oLogger.debug("generateContent - strLoginId [IN] : " +strLoginId);
		String strContent = "";
		try 
		{
			 strContent = "<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>"
									+"<html>"
									+"<head>"
									+"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>"
									+"<title>Insert title here</title>"
									+"</head>"
									+"<body>"
										+"<table>"
											+"<tr>"
												+"<td>"+"Dear : "+strUserName+","+"</td>"
											+"</tr>"
											+"<tr>"
												+"<td>"+"<br>"+"</td>"
											+"</tr>"
											+"<tr>"
												+"<td>"+"Please Use the Following Credentials to login to Application"+"</td>"
											+"</tr>"
											+"<tr>"
												+"<td>"+"<br>"+"</td>"
											+"</tr>"
											+"<tr>"
												+"<td>"+"<br>"+"</td>" 
											+"</tr>"
											+"<tr>"
												+"<td>"+"<br>"+"</td>" 
											+"</tr>"
											+"<tr>"
												+"<td>"+"LoginId : "+strLoginId+"</td>"
											+"</tr>"
											+"<tr>"
												+"<td>" +"Password : "+strPassword+"</td>"
											+"</tr>"
											+"<tr>"
												+"<td>"+"<br>"+"</td>"
											+"</tr>"
											+"<tr>"
												+"<td>"+"<br>"+"</td>"
											+"</tr>"
											+"<tr>"
												+"<td>"+"<br>"+"</td>" 
											+"</tr>"
											+"<tr>"
												+"<td>" +"Regards"+","+"</td>"
											+"</tr>"
											+"<tr>"
												+"<td>" +"Project Support Team."+"</td>"
											+"</tr>"
										+"</table>"
									+"</body>"
									+"</html>";
		}
		catch (Exception oException)
		{
			m_oLogger.error("generateContent - Exception : " +oException );
		}
		m_oLogger.debug("generateContent - strContent [OUT] : " +strContent);
		return strContent;
	}

	private String getMailSettingsValue (String strHandlerData,	String strKey) 
	{
		m_oLogger.info ("getMailSettingsValue");
		m_oLogger.debug ("getMailSettingsValue - strHandlerData [IN] : " +strHandlerData);
		m_oLogger.debug("getMailSettingsValue - strKey [IN] : " +strKey);
		String strValue = "";
		try 
		{
			String [] arrHandlerData = strHandlerData.split(";");
			for (int nIndex = 0; nIndex < arrHandlerData.length; nIndex++ )
			{
				String [] arrKeyValue = arrHandlerData[nIndex].split("=");
				if (arrKeyValue [0].equalsIgnoreCase(strKey))
				{
					strValue = arrKeyValue[1];
					break;
				}
			}
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getMailSettingsValue - oException " + oException);
		}
		m_oLogger.debug("getMailSettingsValue - strValue [OUT] :" + strValue);
		return strValue;
	}
}
