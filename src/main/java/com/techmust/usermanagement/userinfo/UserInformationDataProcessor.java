package com.techmust.usermanagement.userinfo;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.email.EMailStatus;
import com.techmust.generic.util.GenericUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class UserInformationDataProcessor <T extends IUserInformationData> extends GenericIDataProcessor<IUserInformationData> 
{
	private static final int kErrorLoginNameAlreadyExisted = 1;
	
	Logger m_oLogger = Logger.getLogger (UserInformationDataProcessor.class);
	@Override
	public UserInformationResponse create (IUserInformationData oData) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " +oData);
		UserInformationResponse oUserInformationResponse = new UserInformationResponse ();
		String strPassword = "";
		try 
		{
			if (isUserExists (oData))
			{
				oUserInformationResponse.m_bSuccess = true;
				oUserInformationResponse.m_strError_Desc = "kLoginIdExist";
				UserInformationData oUserData = new UserInformationData ();
				oUserData.setM_strLoginId(oData.getM_strLoginId());
				oUserData = (UserInformationData)populateObject(oUserData);
				oUserInformationResponse.m_arrUserInformationData.add((UserInformationData)oData);
				processConfirmation ((UserInformationData)oUserData);
			}
			else
			{
				strPassword = oData.getM_strPassword ();
				oData.setM_strPassword (strPassword);
				oData.setM_dDOB (getDBCompatibleDateFormat (oData.getM_strDOB ()));
//				oData.setM_oUserPhoto (getBlob (oData.getM_buffImgUserPhoto ()));
				oUserInformationResponse.m_bSuccess = oData.saveObject ();
				oUserInformationResponse.m_arrUserInformationData.add((UserInformationData)oData);
				processConfirmation ((UserInformationData)oData);
			}
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("create - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("create - oUserInformationResponse [OUT] : " +oUserInformationResponse);
		return oUserInformationResponse;
	}

	public UserInformationResponse confirmUser (int nUserId, long nUID)
	{
		m_oLogger.debug ("confirmUser - nUserId[IN] : " +nUserId);
		UserInformationResponse oUserInformationResponse = new UserInformationResponse ();
		try
		{
			UserInformationData oUserInformationData = new UserInformationData ();
			oUserInformationData.setM_nUserId(nUserId);
			oUserInformationData = (UserInformationData)populateObject(oUserInformationData);
			if (oUserInformationData.getM_nUID() == nUID)
			{
				oUserInformationData.setM_nStatus(UserStatus.kActive);
				oUserInformationResponse.m_bSuccess = oUserInformationData.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("confirmUser - oException : " +oException);
		}
		return oUserInformationResponse;
	}

	@Override
	public UserInformationResponse get (IUserInformationData oData) throws Exception
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData [IN] :" +oData);
		UserInformationResponse oUserInformationResponse = new UserInformationResponse ();
		UserInformationData oUserInformationData = new UserInformationData ();
		try 
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUserId(oData.getM_nUserId ());
			oUserData.setM_nUID(oData.getM_nUID());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
			oUserInformationData.setM_nUserId (oData.getM_nUserId ());
			oUserInformationData = (UserInformationData) populateObject (oUserInformationData);
			oUserInformationData.setM_buffImgUserPhoto (getBufferedImage (oUserInformationData.getM_oUserPhoto ()));
			oUserInformationData.setM_strDOB (getClientCompatibleFormat (oUserInformationData.getM_dDOB ()));
			oUserInformationResponse.m_arrUserInformationData.add (oUserInformationData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oUserInformationResponse;
	}

	@Override
	public UserInformationResponse list(IUserInformationData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public UserInformationResponse list (IUserInformationData oData,
			HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData[IN] : " +oData);
		UserInformationResponse oUserInformationResponse = new UserInformationResponse ();
		try 
		{
			GenericData oGenericData = (GenericData) oData;
			oUserInformationResponse.m_nRowCount = getRowCount(oGenericData);
			oUserInformationResponse.m_arrUserInformationData = new ArrayList (oGenericData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oUserInformationResponse;
	}
	
	@SuppressWarnings("unchecked")
	public UserInformationResponse getUserSuggesstions (IUserInformationData oUserData, String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getUserSuggesstions");
		m_oLogger.debug ("getUserSuggesstions - oUserData [IN] : " + oUserData);
		m_oLogger.debug ("getUserSuggesstions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getUserSuggesstions - strOrderBy [IN] : " + strOrderBy);
		UserInformationResponse oUserInformationResponse = new UserInformationResponse ();
		try 
		{
			oUserInformationResponse.m_arrUserInformationData = new ArrayList (((UserInformationData) oUserData).listCustomData(this));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getUserSuggesstions - oException : " +oException);
			throw oException;
		}
		return oUserInformationResponse;
	}
	
	@Override
	public Criteria prepareCustomCriteria (Criteria oCriteria, IUserInformationData oUserData) throws RuntimeException 
	{
		oCriteria.add (Restrictions.ilike ("m_strUserName", oUserData.getM_strUserName().trim(), MatchMode.START));
		oCriteria.setMaxResults(50);
		return oCriteria;
	}
	
	@Override
	public UserInformationResponse update (IUserInformationData oData) throws Exception
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData[IN] : " +oData);
		UserInformationResponse oUserInformationResponse = new UserInformationResponse ();
		
		try 
		{
			if (!oData.getM_strNewPassword ().isEmpty ())
				oData.setM_strPassword (oData.getM_strNewPassword ());
			oData.setM_dDOB (getDBCompatibleDateFormat (oData.getM_strDOB ()));
			setUserPhoto (oData);
			oUserInformationResponse.m_bSuccess = oData.updateObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException : " +oException);
			oUserInformationResponse.m_nErrorID = kErrorLoginNameAlreadyExisted;
			oUserInformationResponse.m_bSuccess = false;
		}
		return oUserInformationResponse;
	}
	
	@Override
	public UserInformationResponse deleteData (IUserInformationData oData) 
	{
		m_oLogger.debug ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] :" +oData);
		UserInformationResponse oUserInformationResponse = new UserInformationResponse ();
		try 
		{
			oUserInformationResponse.m_bSuccess = oData.deleteObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("deleteData - oException : " +oException);
		}
		return oUserInformationResponse;
	}
	
	public String getXML (IUserInformationData oData) throws Exception
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oData [IN] : " +oData);
		UserInformationData oUserData = new UserInformationData ();
		String strXml = "";
		try 
		{	
			oUserData.setM_nUserId(oData.getM_nUserId());
			oUserData = (UserInformationData) populateObject (oUserData);
			strXml = oUserData.generateXML ();
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}
	
	public UserInformationData getImagePreview (UserInformationData oData) throws Exception
	{
		// Must call from the client side because of dwr conversion required
		if (!isValidUser(oData))
			throw new Exception (kUserCredentialsFailed);
		return oData;
	}
	
	public UserInformationResponse activate (UserSelectedData oData )
	{
		m_oLogger.info("activate");
		m_oLogger.debug ("activate - oData [IN] : " +oData );
		UserInformationResponse oUserInformationResponse = new UserInformationResponse ();
		UserInformationData oUserInformationData = new UserInformationData ();
		try
		{
			for (int nIndex = 0; nIndex < (oData.m_arrSelectedUsersData).length; nIndex++)
			{
				oUserInformationData = oData.m_arrSelectedUsersData [nIndex];
				UserInformationData oUpdateUserInformationData = get (oUserInformationData).m_arrUserInformationData.get(0);
				oUpdateUserInformationData .setM_nStatus (oUserInformationData.getM_nStatus ());
				oUserInformationResponse = update (oUpdateUserInformationData );
			}

		}
		catch (Exception oException) 
		{
			m_oLogger.error ("activate - oException : " +oException);
			oUserInformationResponse .m_bSuccess = false;
		}
		return oUserInformationResponse ;
	}
	
	public void processConfirmation(UserInformationData oUserInformationData)
    {
		m_oLogger.debug("processConfirmation - oUserInformationData [IN] : " + oUserInformationData);
		try 
		{
			String strSubject =  "Registration Confirmation";
			String strDestEmailId = oUserInformationData.getM_strEmailAddress();
			String strContent = GenericUtil.buildHtml(oUserInformationData.generateXML(), GenericUtil.getProperty("kCONFIRMATION_MAIL_XSLT_PATH"));
			String strMessageFrom = GenericUtil.getProperty ("kAPPLICATIONNAME") + " <" + GenericUtil.getProperty ("kMAILPROPERTYUSER") + ">";
//			sendEmail (strMessageFrom, strDestEmailId, strSubject, strContent, "", EMailStatus.kToSend, "");
		}
		catch (Exception oException) 
		{
			m_oLogger.error("processConfirmation - oException : " +oException);
		}
    }
	
	private void setUserPhoto (IUserInformationData oData)
	{
		m_oLogger.info ("setUserPhoto");
		m_oLogger.debug ("setUserPhoto - oData [IN] : " + oData);
		UserInformationResponse oUserInformationResponse = new UserInformationResponse ();
		try 
		{
			if (oData.getM_buffImgUserPhoto () != null)
				oData.setM_oUserPhoto (getBlob (oData.getM_buffImgUserPhoto ()));
			else
			{
				oUserInformationResponse = get (oData);
				Blob oBlob = oUserInformationResponse.m_arrUserInformationData.get(0).getM_oUserPhoto ();
				oData.setM_oUserPhoto (oBlob);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("setUserPhoto - oException" + oException);
		}
	}
	
	protected boolean isUserExists(IUserInformationData oData)
    {
		boolean bIsUserExists = false;
		m_oLogger.debug ("isUserExists - oData.getM_strLoginId() [IN] : " + oData.getM_strLoginId());
	    try
	    {
	    	UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_strLoginId(oData.getM_strLoginId());
			oUserData = (UserInformationData)populateObject(oUserData);
			if (oUserData != null)
			{
				oData.setM_nUserId(oUserData.getM_nUserId());
				oData.setM_strPassword(oUserData.getM_strPassword());
				bIsUserExists = true;
			}
	    }
	    catch (Exception oException)
	    {
	    	m_oLogger.error ("isUserExists - oException : " +oException);
	    }
	    m_oLogger.debug ("isUserExists - bIsUserExists [OUT] : " +bIsUserExists);
	    return bIsUserExists;
    }
}