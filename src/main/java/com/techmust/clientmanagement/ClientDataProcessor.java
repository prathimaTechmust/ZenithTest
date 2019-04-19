package com.techmust.clientmanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.directwebremoting.io.FileTransfer;
import org.hibernate.Criteria;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.techmust.generic.dataexchange.DataExchangeResponse;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.email.EMailStatus;
import com.techmust.generic.exportimport.ExportImportProviderData;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.usermanagement.MessageConstants;
import com.techmust.usermanagement.role.RoleData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class ClientDataProcessor extends GenericIDataProcessor<ClientData> 
{
	Logger m_oLogger  = Logger.getLogger (ClientDataProcessor.class);
	public static String kClient = "client";
	
	@Override
	public ClientDataResponse create (ClientData oData) 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oData);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try 
		{
			HashSet<ContactData> oContacts = new HashSet<ContactData> ();
			HashSet<SiteData> oSites = new HashSet<SiteData> ();
			oContacts.addAll (buildContactList (oData));
			oSites.addAll(buildSiteList (oData));
			oData.setM_oContacts (oContacts);
			oData.setM_oSites(oSites);
			oClientDataResponse.m_bSuccess = oData.saveObject ();
			oClientDataResponse.m_arrClientData.add(oData);
			if(oData.isM_bAllowOnlineAccess())
				saveLoginDetails (oData);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("create - oException : " +oException);
		}
		return oClientDataResponse;
	}

	private void saveLoginDetails(ClientData oData) throws Exception 
	{
		m_oLogger.info ("saveLoginDetails");
		m_oLogger.debug ("saveLoginDetails - oData [IN] : " + oData);
		String strPassword = generatePassword ();
		oData.setM_strPassword (GenericIDataProcessor.encryptPassword(strPassword));
		oData.updateObject();
		if(oData.getM_strEmail().trim() != "" && oData.getM_strEmail().trim() != null)
			sendLoginDetails (oData, strPassword);
	}
	
	private void sendLoginDetails(ClientData oData, String strPassword) 
	{
		m_oLogger.info ("sendLoginDetails");
		m_oLogger.debug ("sendLoginDetails - oData [IN] : " + oData);
		String strMessageFrom = GenericUtil.getProperty("kAPPLICATIONNAME");
		String strSubject =  GenericUtil.getProperty("KLOGINDETAILS");
		String strDestEmailId = oData.getM_strEmail();
		String strXML = oData.generateLoginDetailsXML (strPassword);
		String strContent = GenericUtil.buildHtml(strXML, GenericUtil.getProperty("kLoginDetailsContent"));
		sendEmail(strMessageFrom, strDestEmailId, strSubject, strContent, "", EMailStatus.kToSend,"");
	}

	private String generatePassword () 
	{
		m_oLogger.info ("generatePassword");
		StringBuffer oStringBuffer = new StringBuffer();  
		try 
		{
			for (int nIndex = 0; nIndex < 8; nIndex++)  
				oStringBuffer.append ((char)((int)(Math.random()*26)+97));  
		}
		catch (Exception oException)
		{
			m_oLogger.error ("generatePassword - Exception : " +oException);
		}
		m_oLogger.debug( "generatePassword - oStringBuffer.toString() [OUT] : " +oStringBuffer.toString ());
		return oStringBuffer.toString ();
	}

	@Override
	public ClientDataResponse get (ClientData oData) 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData [IN] : " + oData);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try 
		{
			oData = (ClientData) populateObject (oData);
			oClientDataResponse.m_arrClientData.add (oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : " + oException);
		}
		return oClientDataResponse;
	}
	
	@Override
	public ClientDataResponse list(ClientData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public ClientDataResponse list (ClientData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " + oData);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try 
		{
			oClientDataResponse.m_nRowCount = getRowCount(oData);
			oClientDataResponse.m_arrClientData = new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("list - oException : " + oException);
		}
		return oClientDataResponse;
	}

	@SuppressWarnings("unchecked")
	public ClientDataResponse getClientSuggesstions (ClientData oClientData, String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getClientSuggesstions");
		m_oLogger.debug ("getClientSuggesstions - oClientData [IN] : " + oClientData);
		m_oLogger.debug ("getClientSuggesstions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getClientSuggesstions - strOrderBy [IN] : " + strOrderBy);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOrderBy.put(strColumn, strOrderBy);
			oClientDataResponse.m_arrClientData = new ArrayList (oClientData.list(oOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getArtileSuggesstions - oException : " +oException);
			throw oException;
		}
		return oClientDataResponse;
	}
	
	@Override
	public ClientDataResponse update (ClientData oData) 
	{
		m_oLogger.info("update");
		m_oLogger.debug("update - oData [IN] : " + oData);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try 
		{
			HashSet<ContactData> oContacts = new HashSet<ContactData> ();
			HashSet<SiteData> oSites = new HashSet<SiteData> ();
			oContacts.addAll (buildContactList (oData));
			oSites.addAll(buildSiteList (oData));
			oData.setM_oContacts (oContacts);
			oData.setM_oSites(oSites);
			oClientDataResponse.m_bSuccess = oData.updateObject ();
			if(oData.isM_bAllowOnlineAccess() && oData.getM_strPassword().trim().length() == 0)
				saveLoginDetails (oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		return oClientDataResponse;
	}
	
	@Override
	public ClientDataResponse deleteData (ClientData oData) 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " + oData);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try 
		{
			ClientData oClientData = (ClientData) populateObject(oData);
			oClientDataResponse.m_bSuccess = oClientData.delete ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("deleteData - oException : " + oException);
		}
		return oClientDataResponse;
	}
	
	@Override
	public String getXML (ClientData oClientData) 
	{
		String strXml = "";
		try 
		{
			oClientData = (ClientData) populateObject (oClientData);
			strXml = oClientData.generateXML ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getXML - oException : " + oException);
		}
		m_oLogger.debug("getXML - strXml [OUT] : " + strXml);
		return strXml;
	}
	
	@Override
	public Criteria prepareCustomCriteria(Criteria oCriteria, ClientData oClientData) throws RuntimeException 
	{
		oCriteria.add ((Restrictions.eq ("class", kClient)));
		oCriteria.add (Restrictions.ilike ("m_strCompanyName", oClientData.getM_strCompanyName().trim(), MatchMode.START));
		oCriteria.setMaxResults(50);
		return oCriteria;
	}
	
	protected Collection<ContactData> buildContactList (ClientData oClientData)
    {
		m_oLogger.info ("buildContactList");
		ArrayList<ContactData> oArrayList = new ArrayList<ContactData> ();
		try
		{
			for (int nIndex = 0; nIndex < oClientData.m_arrContactData.length; nIndex++)
			{
				oClientData.m_arrContactData[nIndex].setM_oClientData (oClientData);
				oArrayList.add (oClientData.m_arrContactData[nIndex]);
			}
		}
		catch(Exception oException)
		{
			m_oLogger.error ("buildContactList - oException : " + oException);
		}
		m_oLogger.debug ("buildContactList - oArrayList [OUT] : " + oArrayList);
		return oArrayList;
    }
	
	protected Collection<SiteData> buildSiteList (ClientData oClientData)
    {
		m_oLogger.info ("buildSiteList");
		ArrayList<SiteData> oArrayList = new ArrayList<SiteData> ();
		try
		{
			if (oClientData.m_arrSiteData.length <= 0)
			{
				SiteData oSiteData = new SiteData ();
				oSiteData.setM_strSiteName(oClientData.getM_strCompanyName());
				oSiteData.setM_strSiteAddress(oClientData.getM_strAddress());
				oArrayList.add(oSiteData);
			}
			for (int nIndex = 0; nIndex < oClientData.m_arrSiteData.length; nIndex++)
			{
				oClientData.m_arrSiteData[nIndex].setM_oClientData (oClientData);
				oArrayList.add (oClientData.m_arrSiteData[nIndex]);
			}
		}
		catch(Exception oException)
		{
			m_oLogger.error ("buildSiteList - oException : " + oException);
		}
		m_oLogger.debug ("buildSiteList - oArrayList [OUT] : " + oArrayList);
		return oArrayList;
    }
	
	public ClientDataResponse login (ClientData oData) 
	{
		m_oLogger.info ("login");
		m_oLogger.debug ("login - oData [IN] : " + oData);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try
		{
			ClientData oClientData = getClientInfoData (oData);
			if (oClientData != null)
			{
				oClientDataResponse.m_arrClientData.add(oClientData);
				oClientDataResponse.m_bSuccess = true;
				RoleData oRoleData = new RoleData ();
				oRoleData.setM_strRoleName(oClientData.kClient);
				oClientDataResponse.m_strMenuHTML = GenericUtil.buildHtml (getActionsAsXML (oClientData, oRoleData), GenericUtil.getProjectProperty ("MENU_XSLT_FILE"));
				oClientDataResponse.m_strOnlineMenuHTML = GenericUtil.buildHtml (getActionsAsXML (oClientData, oRoleData), GenericUtil.getProjectProperty ("ONLINE_MENU_XSLT_FILE"));
			}
			else
				oClientDataResponse.m_strError_Desc = MessageConstants.kMESSAGEINVALIDUSER;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("login - oException : " + oException);
		}
		return oClientDataResponse;
	}
	
	public String getActionsAsXML (ClientData oClientData, RoleData oRoleData) throws Exception
	{
		m_oLogger.info ("getActionsAsXML");
		String strXML = "<root>";
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oRoleData = (RoleData) oRoleData.list(oOrderBy).get(0);
		PersistentSet oActionSet = (PersistentSet) (oRoleData.getm_oActions ());
		strXML += UserInformationData.getActionsAsXML (oActionSet);
		strXML += "</root>";
		m_oLogger.debug ( "getActionsAsXML - strXML [OUT] : " + strXML);
		return strXML;
	}
	
	public ClientDataResponse changePassword (ClientData oData)
	{
		m_oLogger.info ("changePassword");
		m_oLogger.debug( "changePassword - oData [IN] : " + oData);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try
		{
			String  strCurrentPassword = oData.getM_strPassword ();
			ClientData oClientData = new ClientData ();
			oClientData = (ClientData) populateObject (oData);
			String strPassword = oClientData.getM_strPassword ();
			if(strCurrentPassword.equals (strPassword))
			{
				oClientData.setM_strPassword (oData.getM_strNewPassword ());
				oClientDataResponse.m_bSuccess = oClientData.updateObject ();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("changePassword - oException : " +oException);
		}
		return oClientDataResponse;
	}
	
	public ClientDataResponse processForgotPassword (String strEmail)
	{
		m_oLogger.info ("proceessForgotPassword");
		m_oLogger.debug ("processForgotPassword - strEmail [IN] : " +strEmail);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try
		{
			ClientData oClientData = new ClientData ();
			if (strEmail != null && strEmail.length () > 0)
				oClientData = getUserInfoByMail (strEmail);
			oClientDataResponse = deliverNewPassword (oClientData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("processForgotPassword - oException : " +oException);
		}
		return oClientDataResponse;
	}
	
	public ClientDataResponse updateClientBalanceData (ClientData oClientData)
	{
		m_oLogger.info ("updateClientBalanceData");
		m_oLogger.debug ("updateClientBalanceData - oClientData [IN] : " + oClientData);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try
		{
			for (int nIndex=0 ; nIndex < oClientData.m_arrClientBalanceData.length ; nIndex++)
			{
				ClientData oNewClientData = new ClientData();
				oNewClientData.setM_nClientId(oClientData.m_arrClientBalanceData[nIndex].getM_nClientId());
				oNewClientData = (ClientData) populateObject(oNewClientData);
				oNewClientData.setM_nOpeningBalance(oClientData.m_arrClientBalanceData[nIndex].getM_nOpeningBalance());
				oNewClientData.setM_nCreditLimit(oClientData.m_arrClientBalanceData[nIndex].getM_nCreditLimit());
				oClientDataResponse.m_bSuccess =  oNewClientData.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateClientBalanceData - oException : " + oException);
		}
		return oClientDataResponse;
		
	}
	
	private ClientData getUserInfoByMail(String strEmail) 
	{
		m_oLogger.info ("getUserInfo");
		m_oLogger.debug ("getUserInfo - strEmail [IN] " +strEmail);
		ClientData oClientData = new ClientData ();
		try
		{
			oClientData.setM_strEmail(strEmail);
			oClientData = (ClientData) populateObject (oClientData);
		}
		catch (Exception oException)
		{
			m_oLogger.debug ("getUserInfo - oException : " +oException);
		}
		return oClientData;
	}

	private ClientDataResponse deliverNewPassword (ClientData oData) 
	{
		m_oLogger.info("buildForgotPassword");
		m_oLogger.debug("buildForgotPassword - oData [IN] : " +oData);
		ClientDataProcessor oClientDataProcessor = new ClientDataProcessor ();
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try 
		{
			// Generate a new password
			String strPassword = generatePassword ();
			oData.setM_strPassword (GenericIDataProcessor.encryptPassword(strPassword));
			oClientDataProcessor.update (oData);
			
			// send new password by email
			String strMessageFrom = GenericUtil.getProperty("kAPPLICATIONNAME")+" <"+GenericUtil.getProperty("kMAILREPLAYTO")+">";
			String strSubject =  GenericUtil.getProperty("kForgotPasswordSubject");
			String strDestEmailId = oData.getM_strEmail();
			String strXML = oData.generateForgotPasswordXML (strPassword);
			String strContent = GenericUtil.buildHtml(strXML, GenericUtil.getProperty("kForgotPasswordContent"));
			sendEmail(strMessageFrom, strDestEmailId, strSubject, strContent, "", EMailStatus.kToSend,"");
			oClientDataResponse.m_bSuccess = true;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("buildForgotPassword - oException " +oException);
		}
		return oClientDataResponse;
	}

	
	private ClientData getClientInfoData (ClientData oData) 
	{
		m_oLogger.info ("getClientInfoData");
		m_oLogger.debug ("getClientInfoData - oData [IN] : " + oData);
		ClientData oClientData = oData;
		try
		{
			oData.getM_strPassword().charAt(0);// password cannot be empty
			oClientData.setM_strEmail (oData.getM_strEmail());
			oClientData.setM_strPassword (oData.getM_strPassword());
			oClientData = (ClientData) populateObject (oClientData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getUserInfoData - oException : " +oException);
		}
		return oClientData;
	}
	
	public FileTransfer exportClientData (ClientData oData,  ExportImportProviderData oProviderData) throws Exception
    {
		m_oLogger.info ("exportItemData");
		m_oLogger.debug ("exportItemData - oData [IN] : " + oData);
    	FileTransfer oFile = null;
    	ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oClientDataResponse = (ClientDataResponse) list(oData, oOrderBy);
	    	oFile = oProviderData.export (oClientDataResponse.m_arrClientData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("exportItemData - oException : " + oException);
		}
    	return oFile;
    }
	
	public GenericResponse importClientData (ClientData oClientData, ExportImportProviderData oProviderData, FileTransfer oFileTransfer)
	{
		m_oLogger.info ("importItemData");
		m_oLogger.debug ("importItemData - oFileTransfer [IN] : " + oFileTransfer);
		DataExchangeResponse oDataExchangeResponse = new DataExchangeResponse ();
		try 
		{
			UserInformationData oUserInformationData = new UserInformationData ();
			oDataExchangeResponse = (DataExchangeResponse) oProviderData.importData (oFileTransfer, oClientData.getClass().getName(), oUserInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("importItemData - oException : " + oException);
		}
		return oDataExchangeResponse;
	}
}