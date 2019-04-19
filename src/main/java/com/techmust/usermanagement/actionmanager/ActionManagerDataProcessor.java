package com.techmust.usermanagement.actionmanager;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.constants.Constants;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.email.EMailStatus;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.usermanagement.MessageConstants;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.usermanagement.userinfo.UserInformationDataProcessor;
import com.techmust.usermanagement.userinfo.UserStatus;

public class ActionManagerDataProcessor extends GenericIDataProcessor<ActionManagerData> 
{
	@Override
	public GenericResponse create (ActionManagerData oData)
	{
		return null;
	}
	
	public ActionManagerResponse changePassword (ActionManagerData oData)
	{
		m_oLogger.info ("changePassword");
		m_oLogger.debug( "changePassword - oData [IN] : " + oData);
		ActionManagerResponse oActionManagerResponse = new ActionManagerResponse ();
		try
		{
			String  strCurrentPassword = oData.getM_strPassword ();
			UserInformationData oUserInformationData = new UserInformationData ();
			oUserInformationData.setM_strLoginId (oData.getM_strLoginId ());
			oUserInformationData.setM_strPassword (strCurrentPassword);
			oUserInformationData = (UserInformationData) populateObject (oUserInformationData);
			String strPassword = oUserInformationData.getM_strPassword ();
			if(strCurrentPassword.equals (strPassword))
			{
				oUserInformationData.setM_strPassword (oData.getM_strNewPassword ());
				oActionManagerResponse.m_bSuccess = oUserInformationData.updateObject ();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("changePassword - oException : " +oException);
		}
		return oActionManagerResponse;
	}

	@Override
	public ActionManagerResponse get (ActionManagerData oData) 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData [IN] : " +oData);
		ActionManagerResponse oActionManagerResponse = new ActionManagerResponse ();
		UserInformationDataProcessor<UserInformationData> oUserInformationDataProcessor = new UserInformationDataProcessor<UserInformationData> ();
		try
		{
			UserInformationData oUserInfoData = getUserInfoData (oData);
			if(oUserInfoData != null && !IsAdminRole (oUserInfoData))
			{
				oActionManagerResponse.m_strError_Desc = MessageConstants.kMESSAGEINVALIDADMINUSER;
			}
			else if (oUserInfoData != null && oUserInfoData.getM_nStatus () == UserStatus.kActive)
			{
				oActionManagerResponse.set (oUserInfoData);
				createLog(oUserInfoData, "Login");
			}
			else if (oUserInfoData != null && oUserInfoData.getM_nStatus() == UserStatus.kInactive)
			{
				oActionManagerResponse.m_strError_Desc = MessageConstants.kMESSAGEUSERINACTIVE;
				oUserInformationDataProcessor.processConfirmation(oUserInfoData);
			}
			else
				oActionManagerResponse.m_strError_Desc = MessageConstants.kMESSAGEINVALIDUSER;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : " +oException);
		}
		m_oLogger.debug ("get - oRoleResponse.m_arrActionManagerData.size [OUT] : " +oActionManagerResponse.m_arrActionManagerData.size ());
		return oActionManagerResponse;
	}
	
	private boolean IsAdminRole(UserInformationData oUserInfoData) 
	{
		boolean bIsAdminUser = false;
		String strRoleName = oUserInfoData.getm_oRole().getM_strRoleName();
		bIsAdminUser = strRoleName.equals(Constants.kAdmin);
		return bIsAdminUser;
	}

	public void logOut (UserInformationData oUserInfoData)
	{
		createLog(oUserInfoData, "Logout");
	}
	
	@Override
	public GenericResponse list (ActionManagerData oData, HashMap<String, String> arrOrderBy) 
	{
		return null;
	}
	
	@Override
	public GenericResponse update (ActionManagerData oData) 
	{
		return null;
	}
	
	@Override
	public GenericResponse deleteData (ActionManagerData oData) 
	{
		return null;
	}
	
	public ActionManagerResponse processForgotPassword (String strLoginId, String strEmail)
	{
		m_oLogger.info ("proceessForgotPassword");
		m_oLogger.debug ("processForgotPassword - strLoginId [IN] : " +strLoginId);
		m_oLogger.debug ("processForgotPassword - strEmail [IN] : " +strEmail);
		ActionManagerResponse oActionManagerResponse = new ActionManagerResponse ();
		try
		{
			UserInformationData oUserInformationData = new UserInformationData ();
			if (strEmail != null && strEmail.length () > 0)
				oUserInformationData = getUserInfoByMail (strEmail);
			else
				oUserInformationData = getUserInfoByLoginId (strLoginId);
			oActionManagerResponse = deliverNewPassword (oUserInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("processForgotPassword - oException : " +oException);
		}
		return oActionManagerResponse;
	}
	
	@Override
	public String getXML (ActionManagerData oData)
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oData [IN] : " +oData);
		String strXml = "";
		try 
		{
			oData = (ActionManagerData)populateObject (oData);
			strXml = oData.generateXML ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getXML - oException : " +oException);
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}
	
	public boolean isUserTableEmpty () 
	{
		m_oLogger.info ("isEmptyUserTable");
		UserInformationData oUserInformationData = new UserInformationData ();
		boolean bEmpty = false;
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ArrayList<GenericData> arrUserList = oUserInformationData.list (oOrderBy);
			if (arrUserList.size() == 0)
				bEmpty = true;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("isEmptyUserTable - oException " +oException);
		}
		m_oLogger.debug ("isEmptyUserTable - bEmpty [OUT] : " +bEmpty);
		return bEmpty;
	}

	private UserInformationData getUserInfoData (ActionManagerData oData) 
	{
		m_oLogger.info ("getUserInfoData");
		m_oLogger.debug ("getUserInfoData - oData [IN] : " +oData);
		UserInformationData oUserInformationData = new UserInformationData ();
		try
		{
			oData.getM_strPassword().charAt(0);// password cannot be empty
			oUserInformationData.setM_nUserId(oData.getM_nUserId());
			oUserInformationData.setM_strLoginId (oData.getM_strUserName ());
			oUserInformationData.setM_strPassword (oData.getM_strPassword());
			oUserInformationData = (UserInformationData) populateObject (oUserInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getUserInfoData - oException : " +oException);
		}
		return oUserInformationData;
	}
	
	private String generatePassword () 
	{
		m_oLogger.info ("generatePassword");
		StringBuffer oStringBuffer = new StringBuffer();  
		try 
		{
			for (int nIndex = 0; nIndex < 8; nIndex++)  
		    {  
				oStringBuffer.append ((char)((int)(Math.random()*26)+97));  
		    }  
		}
		catch (Exception oException)
		{
			m_oLogger.error ("generatePassword - Exception : " +oException);
		}
		m_oLogger.debug( "generatePassword - oStringBuffer.toString() [OUT] : " +oStringBuffer.toString ());
		return oStringBuffer.toString ();
	}

	private UserInformationData getUserInfoByMail (String strEmail)
	{
		m_oLogger.info ("getUserInfo");
		m_oLogger.debug ("getUserInfo - strEmail [IN] " +strEmail);
		UserInformationData oUserInformationData = new UserInformationData ();
		try
		{
			oUserInformationData.setM_strEmailAddress (strEmail);
			oUserInformationData = (UserInformationData) populateObject (oUserInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.debug ("getUserInfo - oException : " +oException);
		}
		return oUserInformationData;
	}
	
	private UserInformationData getUserInfoByLoginId (String strLoginId)
	{
		m_oLogger.info ("getUserInfoByLoginId");
		m_oLogger.debug ("getUserInfoByLoginId - strLoginId [IN] " +strLoginId);
		UserInformationData oUserInformationData = new UserInformationData ();
		try
		{
			oUserInformationData.setM_strLoginId (strLoginId);
			oUserInformationData = (UserInformationData)populateObject (oUserInformationData);
		}
		
		catch (Exception oException)
		{
			m_oLogger.error ("getUserInfoByLoginId - Exception " +oException);
		}
		return oUserInformationData;
	}
	
	private ActionManagerResponse deliverNewPassword (UserInformationData oData)
	{
		m_oLogger.info("buildForgotPassword");
		m_oLogger.debug("buildForgotPassword - oData [IN] : " +oData);
		UserInformationDataProcessor<UserInformationData> oUserInformationDataProcessor = new UserInformationDataProcessor<UserInformationData> ();
		ActionManagerResponse oActionManagerResponse = new ActionManagerResponse ();
		try 
		{
			// Generate a new password
			String strPassword = generatePassword ();
			oData.setM_strNewPassword (GenericIDataProcessor.encryptPassword(strPassword));
			oUserInformationDataProcessor.update (oData);
			
			// send new password by email
			String strMessageFrom = GenericUtil.getProperty("kAPPLICATIONNAME")+" <"+GenericUtil.getProperty("kMAILREPLAYTO")+">";
			String strSubject =  GenericUtil.getProperty("kForgotPasswordSubject");
			String strDestEmailId = oData.getM_strEmailAddress();
			String strXML = oData.generateForgotPasswordXML (strPassword);
			String strContent = GenericUtil.buildHtml(strXML, GenericUtil.getProperty("kForgotPasswordContent"));
//			sendEmail(strMessageFrom, strDestEmailId, strSubject, strContent, "", EMailStatus.kToSend,"");
			oActionManagerResponse.m_bSuccess = true;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("buildForgotPassword - oException " +oException);
		}
		return oActionManagerResponse;
	}
}