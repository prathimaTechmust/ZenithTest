package com.techmust.inventory.crm;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.traderp.util.TraderpUtil;
import com.techmust.clientmanagement.ContactData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.email.EmailDataResponse;
import com.techmust.generic.email.EmailMessageData;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class MessageDataProcessor extends GenericIDataProcessor<EmailMessageData> 
{
	private TraderpUtil m_oTraderpUtil;
	
	public MessageDataProcessor ()
	{
		m_oTraderpUtil = new TraderpUtil ();
	}
	@RequestMapping(value="/messageDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody EmailMessageData oEmailMessageData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oEmailMessageData [IN] : " + oEmailMessageData);
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		try
		{
			createLog(oEmailMessageData, "MessageDataProcessor.create : ");
			isvalidUser (oEmailMessageData);
			oEmailMessageData.prepareData ();
			oEmailDataResponse.m_bSuccess = oEmailMessageData.saveObject();
			oEmailDataResponse.m_arrEmailMessage.add(oEmailMessageData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oEmailDataResponse.m_bSuccess [OUT] : " + oEmailDataResponse.m_bSuccess);
		return oEmailDataResponse;	}

	@Override
	public GenericResponse deleteData(EmailMessageData oEmailMessageData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oEmailMessageData.getM_nId[IN] : " + oEmailMessageData.getM_nId());
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		try
		{
			createLog(oEmailMessageData, "MessageDataProcessor.deleteData : ");
			isvalidUser (oEmailMessageData);
			oEmailMessageData.prepareData ();
			oEmailDataResponse.m_bSuccess = oEmailMessageData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oEmailDataResponse.m_bSuccess [OUT] : " + oEmailDataResponse.m_bSuccess);
		return oEmailDataResponse;
	}

	@Override
	public GenericResponse get(EmailMessageData oEmailMessageData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oEmailMessageData.getM_nId() [IN] :" +oEmailMessageData.getM_nId());
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		try 
		{
			oEmailMessageData = (EmailMessageData) populateObject (oEmailMessageData);
			oEmailDataResponse.m_arrEmailMessage.add (oEmailMessageData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oEmailDataResponse;
	}

	@Override
	public String getXML(EmailMessageData oEmailMessageData) throws Exception 
	{
		m_oLogger.info ("getXML : ");
		m_oLogger.debug ("getXML - oEmailMessageData [IN] : " +oEmailMessageData);
		String strXml = "";
		try 
		{
			oEmailMessageData = (EmailMessageData) populateObject(oEmailMessageData);
			strXml = oEmailMessageData != null ? oEmailMessageData.generateXML () : "";
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}

//	@RequestMapping(value="/messageDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
//	@ResponseBody
//	public GenericResponse list (@RequestBody TradeMustHelper oData) throws Exception 
//	{
//		return list (oData.getM_oEmailMessageData(),oData.getM_strColumn(),oData.getM_strOrderBy(),oData.getM_nPageNo(),oData.getM_nPageSize());
//	}

	@SuppressWarnings("unchecked")
	public GenericResponse list(EmailMessageData oEmailMessageData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oEmailMessageData [IN] : " + oEmailMessageData);
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		try 
		{
			oEmailDataResponse.m_nRowCount = getRowCount(oEmailMessageData);
			oEmailDataResponse.m_arrEmailMessage = new ArrayList (oEmailMessageData.list (arrOrderBy, nPageNumber, nPageSize));
			oEmailDataResponse.m_arrEmailMessage = buildEmailData (oEmailDataResponse.m_arrEmailMessage);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
			throw oException;
		}
		return oEmailDataResponse;
	}

	@Override
	public GenericResponse update(EmailMessageData oEmailMessageData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oEmailMessageData.getM_nId() [IN] : " + oEmailMessageData.getM_nId());
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		try
		{
			createLog(oEmailMessageData, "MessageDataProcessor.update : ");
			isvalidUser (oEmailMessageData);
			oEmailMessageData.prepareData ();
			oEmailDataResponse.m_bSuccess = oEmailMessageData.updateObject();
			oEmailDataResponse.m_arrEmailMessage.add(oEmailMessageData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oEmailDataResponse.m_bSuccess [OUT] : " + oEmailDataResponse.m_bSuccess);
		return oEmailDataResponse;
	}
	
	public GenericResponse sendMail (EmailMessageData oEmailMessageData) throws Exception 
	{
		m_oLogger.info ("sendMail");
		m_oLogger.debug ("sendMail - oEmailMessageData.getM_nId() [IN] : " + oEmailMessageData.getM_nId());
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		try
		{
			createLog(oEmailMessageData, "MessageDataProcessor.sendMail : ");
			isvalidUser (oEmailMessageData);
			oEmailDataResponse = (EmailDataResponse) create(oEmailMessageData);
			oEmailMessageData = (EmailMessageData) populateObject(oEmailDataResponse.m_arrEmailMessage.get(0));
			m_oTraderpUtil.sendCRMail(oEmailMessageData);
			oEmailDataResponse.m_bSuccess = true;
		}
		catch (Exception oException)
		{
			m_oLogger.error ("sendMail - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("sendMail - oEmailDataResponse.m_bSuccess [OUT] : " + oEmailDataResponse.m_bSuccess);
		return oEmailDataResponse;
	}
	
	public GenericResponse resendMail (EmailMessageData oEmailMessageData) throws Exception 
	{
		m_oLogger.info ("resendMail");
		m_oLogger.debug ("resendMail - oEmailMessageData.getM_nId() [IN] : " + oEmailMessageData.getM_nId());
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		try
		{
			createLog(oEmailMessageData, "MessageDataProcessor.resendMail : ");
			isvalidUser (oEmailMessageData);
			oEmailDataResponse = (EmailDataResponse) update(oEmailMessageData);
			oEmailMessageData = (EmailMessageData) populateObject(oEmailDataResponse.m_arrEmailMessage.get(0));
			m_oTraderpUtil.sendCRMail(oEmailMessageData);
			oEmailDataResponse.m_bSuccess = true;
		}
		catch (Exception oException)
		{
			m_oLogger.error ("resendMail - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("resendMail - oEmailDataResponse.m_bSuccess [OUT] : " + oEmailDataResponse.m_bSuccess);
		return oEmailDataResponse;
	}
	
	public GenericResponse getPreview (EmailMessageData oEmailMessageData) throws Exception 
	{
		m_oLogger.info ("getPreview");
		m_oLogger.debug ("getPreview - oEmailMessageData.getM_nId() [IN] : " + oEmailMessageData.getM_nId());
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		try
		{
			createLog(oEmailMessageData, "MessageDataProcessor.sendMail : ");
			isvalidUser (oEmailMessageData);
			String strXsltpath = m_oTraderpUtil.getCRMXSLT (oEmailMessageData.getM_oTemplateData());
			ContactData oContactData = new ContactData ();
			String strMailXML = oContactData.buildMailXML (oEmailMessageData.getM_strSubject(),oEmailMessageData.getM_strContent());
			String strHTMLContent = GenericUtil.buildHtml(strMailXML, strXsltpath);
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
	
	public GenericResponse publicise (EmailMessageData oEmailMessageData) throws Exception 
	{
		m_oLogger.info ("publicise");
		m_oLogger.debug ("publicise - oEmailMessageData.getM_nId() [IN] : " + oEmailMessageData.getM_nId());
		EmailDataResponse oEmailDataResponse = new EmailDataResponse ();
		try
		{
			if(oEmailMessageData.m_arrContactData.length > 0)
			{
				createLog(oEmailMessageData, "MessageDataProcessor.publicise : ");
				oEmailDataResponse = (EmailDataResponse) create(oEmailMessageData);
				EmailMessageData oData = (EmailMessageData) populateObject(oEmailDataResponse.m_arrEmailMessage.get(0));
				oData.setM_strHTML(oEmailMessageData.getM_strHTML());
				m_oTraderpUtil.publicise(oData);
				oEmailDataResponse.m_bSuccess = true;
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("publicise - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("publicise - oEmailDataResponse.m_bSuccess [OUT] : " + oEmailDataResponse.m_bSuccess);
		return oEmailDataResponse;
	}
	
	private ArrayList<EmailMessageData> buildEmailData(ArrayList<EmailMessageData> arrEmailMessageData) 
	{
		m_oLogger.info("buildSalesData");
		for (int nIndex=0; nIndex < arrEmailMessageData.size(); nIndex++)
			arrEmailMessageData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrEmailMessageData.get(nIndex).getM_dCreatedOn())); 
		return arrEmailMessageData;
	}
	
	private void isvalidUser (EmailMessageData oEmailMessageData) throws Exception
	{
		m_oLogger.info("isvalidUser");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oEmailMessageData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oEmailMessageData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}
	
	private void createLog (EmailMessageData oEmailMessageData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oEmailMessageData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oEmailMessageData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName + oEmailMessageData.getM_strSubject());
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
	@Override
	public GenericResponse list(EmailMessageData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}