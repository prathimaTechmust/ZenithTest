package com.techmust.inventory.crm;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.email.EmailDataResponse;
import com.techmust.generic.email.EmailMessageData;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.ItemGroupData;
import com.techmust.traderp.util.TraderpUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class CRMDataProcessor 
{
	public Logger m_oLogger = Logger.getLogger(CRMDataProcessor.class);
	
	private TraderpUtil m_oTraderpUtil;
	
	public CRMDataProcessor ()
	{
		m_oTraderpUtil = new TraderpUtil ();
	}
	
	public GenericResponse publicise (PubliciseData oPubliciseData)
	{
		m_oLogger.info ("publicise");
		m_oLogger.debug ("publicise - oPubliciseData - [IN] : " + oPubliciseData);
		EmailMessageData oEmailData = new EmailMessageData ();
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		MessageDataProcessor oMessageDataProcessor = new MessageDataProcessor ();
		try
		{
			isvalidUser (oPubliciseData);
			prepareEmailData(oEmailData, oPubliciseData);
			if(oPubliciseData.getM_nItemGroupId() > 0)
			oEmailData.m_arrContactData = ItemGroupData.getClientContacts (oPubliciseData);
		else if(oPubliciseData.getM_nClientGroupId() > 0)
			{
				PubliciseClientGroupData oPubliciseClientGroupData = new PubliciseClientGroupData ();
				oEmailData.m_arrContactData = oPubliciseClientGroupData.getClientContacts (oPubliciseData);
			}
			else if (oPubliciseData.getM_nItemId() > 0)
				oEmailData.m_arrContactData = ItemData.getClientContacts (oPubliciseData);
			oEmailData.setM_strHTML(getXML(oPubliciseData));
			oEmailDataResponse = (EmailDataResponse) oMessageDataProcessor.publicise(oEmailData);
			oEmailDataResponse.m_nRowCount = oEmailData.m_arrContactData.length;
		}
		catch (Exception oException)
		{
			oEmailDataResponse.m_strError_Desc = oException.getMessage();
			m_oLogger.error ("publicise - oException : " + oException);
		}
		return oEmailDataResponse;
	}

	public GenericResponse getPublicisePreview (PubliciseData oPubliciseData) throws Exception 
	{
		m_oLogger.info ("getPublicisePreview");
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		try
		{
			isvalidUser (oPubliciseData);
			String strXsltpath = m_oTraderpUtil.getCRMXSLT (oPubliciseData.getM_oTemplateData());
			String strXML = getXML(oPubliciseData);
			String strHTMLContent = GenericUtil.buildHtml(strXML, strXsltpath);
			EmailMessageData oMessageData = new EmailMessageData ();
			oMessageData.setM_strHTML(strHTMLContent);
			oEmailDataResponse.m_arrEmailMessage.add(oMessageData);
			m_oTraderpUtil.deleteTempXslt (strXsltpath);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getPreview - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("getPreview - oEmailDataResponse.m_bSuccess [OUT] : " + oEmailDataResponse.m_bSuccess);
		return oEmailDataResponse;
	}
	
	private String getXML(PubliciseData oPubliciseData) throws Exception 
	{
		String strXML = "<root>";
		if(oPubliciseData.getM_nItemId() > 0)
		{
			ItemData oItemData = new ItemData ();
			oItemData.setM_nItemId(oPubliciseData.getM_nItemId());
			oItemData = (ItemData) GenericIDataProcessor.populateObject(oItemData);
			strXML += oItemData.generateXML();
		}
		return strXML + "</root>";
	}

	private void prepareEmailData(EmailMessageData oEmailData, PubliciseData oPubliciseData) 
	{
		oEmailData.setM_oTemplateData(oPubliciseData.getM_oTemplateData());
		oEmailData.setM_strSubject(oPubliciseData.getM_strSubject());
		oEmailData.setM_oUserCredentialsData(oPubliciseData.getM_oUserCredentialsData());
		oEmailData.setM_nCreatedBy(oPubliciseData.getM_oCreatedBy().getM_nUserId());
		oEmailData.m_arrAttachmentData = oPubliciseData.m_arrAttachmentData;
		buildDate(oPubliciseData);
	}
	
	private void buildDate(PubliciseData oPubliciseData) 
	{
		Calendar oPreviousDate = Calendar.getInstance();
		oPreviousDate.add(Calendar.DATE, -oPubliciseData.getM_nNoOfDays());
		String strFromDate =  GenericIDataProcessor.getClientCompatibleFormat((oPreviousDate.getTime()));
		oPubliciseData.setM_strFromDate(strFromDate);
		oPubliciseData.setM_strToDate(GenericIDataProcessor.getClientCompatibleFormat(Calendar.getInstance().getTime()));
	}

	private void isvalidUser(PubliciseData oPubliciseData) throws Exception 
	{
		m_oLogger.info("isvalidUser");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oPubliciseData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oPubliciseData.getM_oUserCredentialsData().getM_nUserId());
			if (!GenericIDataProcessor.isValidUser(oUserData))
				throw new Exception (GenericIDataProcessor.kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}
}
