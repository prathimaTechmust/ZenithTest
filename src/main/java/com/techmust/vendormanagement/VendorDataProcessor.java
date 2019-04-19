package com.techmust.vendormanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.directwebremoting.io.FileTransfer;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ClientDataProcessor;
import com.techmust.clientmanagement.ClientDataResponse;
import com.techmust.clientmanagement.ContactData;
import com.techmust.clientmanagement.SiteData;
import com.techmust.generic.dataexchange.DataExchangeResponse;
import com.techmust.generic.exportimport.ExportImportProviderData;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.GenericUtil;
import com.techmust.usermanagement.role.RoleData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class VendorDataProcessor extends ClientDataProcessor
{
	public static Logger m_oLogger = Logger.getLogger(VendorDataProcessor.class);
	public static String kVendor = "Vendor";
	
	public ClientDataResponse createVendor(VendorData oVendorData)
    {
		m_oLogger.info("create");
		m_oLogger.debug("create - oVendorData [IN] : " + oVendorData);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try 
		{
			oVendorData.setM_oLogo(getBlob (oVendorData.getM_buffImgLogo()));
			oClientDataResponse =  super.create((ClientData)oVendorData);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("create - oException : " +oException);
		}
		m_oLogger.debug("create - oClientDataResponse [OUT] : " + oClientDataResponse);
		return oClientDataResponse;
    }
	
	public ClientDataResponse createOnlineVendor(VendorData oVendorData, VendorCounterData oVendorCounterData)
    {
		m_oLogger.info("create");
		m_oLogger.debug("create - oVendorData [IN] : " + oVendorData);
		ClientDataResponse oClientDataResponse = new ClientDataResponse ();
		try 
		{
			oClientDataResponse  =  super.create((ClientData)oVendorData);
			VendorData oData = (VendorData) oClientDataResponse.m_arrClientData.get(0);
			oVendorCounterData.getM_oVendorData().setM_nClientId(oData.getM_nClientId());
			VendorCounterDataProcessor oVendorCounterDataProcessor = new VendorCounterDataProcessor ();
			oVendorCounterDataProcessor.create(oVendorCounterData);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("create - oException : " +oException);
		}
		m_oLogger.debug("create - oClientDataResponse [OUT] : " + oClientDataResponse);
		return oClientDataResponse;
    }	
	
	protected Collection<SiteData> buildSiteList (ClientData oClientData)
    {
		m_oLogger.info ("buildSiteList");
		return new ArrayList<SiteData> ();
    }
	
	public GenericResponse deleteVendorData (VendorData oData) 
	{
		m_oLogger.info("deleteData");
		m_oLogger.debug("deleteData - oVendorData [IN] : " +oData);
		VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try 
		{
			HashSet<ContactData> oContacts = new HashSet<ContactData> ();
			oContacts.addAll(buildContactList(oData));
			oData.setM_oContacts(oContacts);
			oVendorDataResponse.m_bSuccess = oData.deleteObject();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("deleteVendorData - oException : " +oException);
		}
		m_oLogger.debug("deleteData - oVendorDataResponse [OUT] :" + oVendorDataResponse);
		return oVendorDataResponse;
	}

	public VendorDataResponse getVendor(VendorData oData)
    {
		m_oLogger.info("get");
		m_oLogger.debug("get - oData [IN] :  " +oData);
		VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try 
		{
			VendorData oVendorData = (VendorData)super.get((ClientData)oData).m_arrClientData.get(0);
			oVendorData.setM_buffImgLogo(getBufferedImage(oVendorData.getM_oLogo()));
			oVendorDataResponse.m_arrVendorData.add(oVendorData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("get - oException : " +oException);
		}
		m_oLogger.debug("get - oVendorDataResponse [OUT] :" +oVendorDataResponse);
		return oVendorDataResponse;
    }

    public VendorDataResponse listVendor(VendorData oData, String strColumn, String strOrderBy) throws Exception 
	{
		return listVendor (oData, strColumn, strOrderBy, 0, 0);
	}
    
    public VendorDataResponse loginVendor (VendorData oData) 
	{
		m_oLogger.info ("login");
		m_oLogger.debug ("login - oData [IN] : " + oData);
		VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try
		{
			VendorData oVendorData = (VendorData)super.login((ClientData)oData).m_arrClientData.get(0);
			oVendorData.setM_buffImgLogo(getBufferedImage(oVendorData.getM_oLogo()));
			oVendorDataResponse.m_arrVendorData.add(oVendorData);	
			oVendorDataResponse.m_bSuccess = true;
			RoleData oRoleData = new RoleData ();
			oRoleData.setM_strRoleName(oData.kVendor);
			oVendorDataResponse.m_strMenuHTML = GenericUtil.buildHtml (getActionsAsXML ((ClientData)oVendorData, oRoleData), GenericUtil.getProjectProperty ("MENU_XSLT_FILE"));
			oVendorDataResponse.m_strOnlineMenuHTML = GenericUtil.buildHtml (getActionsAsXML ((ClientData)oVendorData, oRoleData), GenericUtil.getProjectProperty ("ONLINE_MENU_XSLT_FILE"));
			oVendorDataResponse.m_strOffcanvasMenu = GenericUtil.buildHtml (getActionsAsXML ((ClientData)oVendorData, oRoleData), GenericUtil.getProjectProperty ("OFFCANVAS_XSLT_FILE"));
			
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("login - oException : " + oException);
		}
		return oVendorDataResponse;
	}	
	
	@SuppressWarnings("unchecked")
    public VendorDataResponse listVendor(VendorData oData, String strColumn, String strOrderBy, int nPageNumber, int nPageSize)
    {
		m_oLogger.info("list");
		m_oLogger.debug("list - oData [IN] : " +oData);
		m_oLogger.debug("list - strColumn [IN] : " +strColumn);
		m_oLogger.debug("list - strOrderBy [IN] : " +strOrderBy);
		VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try 
		{
			oVendorDataResponse.m_nRowCount = getRowCount(oData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOrderBy.put(strColumn,strOrderBy);
			oVendorDataResponse.m_arrVendorData = new ArrayList (oData.list(oOrderBy, nPageNumber, nPageSize));
			oVendorDataResponse.m_arrVendorData = buildVendorData (oVendorDataResponse.m_arrVendorData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		m_oLogger.debug("list - oVendorDataResponse [OUT] :" +oVendorDataResponse);
		return oVendorDataResponse;
    }
	
	private ArrayList<VendorData> buildVendorData(ArrayList<VendorData> arrVendorData) 
	{
		m_oLogger.info("buildItemData");
		for (int nIndex=0; nIndex < arrVendorData.size(); nIndex++)
			arrVendorData.get(nIndex).setM_buffImgLogo(getBufferedImage (arrVendorData.get(nIndex).getM_oLogo()));
		return arrVendorData;
	}
	
	@SuppressWarnings("unchecked")
	public VendorDataResponse getVendorSuggesstions (VendorData oVendorData, String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getVendorSuggesstions");
		m_oLogger.debug ("getVendorSuggesstions - oItemData [IN] : " + oVendorData);
		m_oLogger.debug ("getVendorSuggesstions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getVendorSuggesstions - strOrderBy [IN] : " + strOrderBy);
		VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try 
		{
//			oVendorDataResponse.m_arrVendorData = new ArrayList (oVendorData.listCustomData(this));
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOrderBy.put(strColumn, strOrderBy);
			oVendorDataResponse.m_arrVendorData = new ArrayList (oVendorData.list(oOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getVendorSuggesstions - oException : " +oException);
			throw oException;
		}
		return oVendorDataResponse;
	}
	
	@Override
	public Criteria prepareCustomCriteria(Criteria oCriteria, ClientData oClientData) throws RuntimeException 
	{
		VendorData oVendorData = (VendorData) oClientData;
		oCriteria.add((Restrictions.eq("class", kVendor)));
		oCriteria.add (Restrictions.ilike ("m_strCompanyName", oVendorData.getM_strCompanyName().trim(), MatchMode.START));
		oCriteria.setMaxResults(50);
		return oCriteria;
	}

	public VendorDataResponse updateVendor(VendorData oData)
    {
		m_oLogger.info("update");
		m_oLogger.debug("update - oData [IN] : " +oData);
		VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try 
		{
			if (oData.getM_buffImgLogo()!= null)
				oData.setM_oLogo(getBlob (oData.getM_buffImgLogo()));
			oVendorDataResponse.m_bSuccess = super.update((ClientData)oData).m_bSuccess;
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("update - oException : " +oException);
		}
		m_oLogger.debug("update - oVendorDataResponse [OUT] : " +oVendorDataResponse);
		return oVendorDataResponse;
    }
	
	public String getXML (VendorData oVendorData)
	{
		String strXml = "";
		try 
		{
			oVendorData = (VendorData)populateObject(oVendorData);
			strXml = oVendorData.generateXML();
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
		}
		return strXml;
	}
	
	public VendorDataResponse updateVendorBalanceData (VendorData oVendorData)
	{
		m_oLogger.info ("updateVendorBalanceData");
		m_oLogger.debug ("updateVendorBalanceData - oVendorData [IN] : " + oVendorData);
		VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try
		{
			for (int nIndex=0 ; nIndex < oVendorData.m_arrClientBalanceData.length ; nIndex++)
			{
				VendorData oNewVendorData = new VendorData ();
				oNewVendorData.setM_nClientId(oVendorData.m_arrClientBalanceData[nIndex].getM_nClientId());
				oNewVendorData = (VendorData) populateObject(oNewVendorData);
				oNewVendorData.setM_nOpeningBalance(oVendorData.m_arrClientBalanceData[nIndex].getM_nOpeningBalance());
				oVendorDataResponse.m_bSuccess = oNewVendorData.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateVendorBalanceData - oException : " + oException);
		}
		return oVendorDataResponse;
	}
	
	protected Collection<ContactData> buildContactList (VendorData oVendorData)
    {
		m_oLogger.info("buildContactList");
		m_oLogger.debug("buildContactList - oVendorData [IN] : " +oVendorData);
		ArrayList<ContactData> oArrayList = new ArrayList<ContactData> ();
		try
		{
			for (int nIndex = 0; nIndex < oVendorData.m_arrContactData.length; nIndex++)
			{
				oVendorData.m_arrContactData[nIndex].setM_oClientData (oVendorData);
				oArrayList.add(oVendorData.m_arrContactData[nIndex]);
			}
		}
		catch(Exception oException)
		{
			m_oLogger.error("buildContactList - oException : " +oException);
		}
		m_oLogger.debug("buildContactList - oArrayList [OUT] : " +oArrayList);
		return oArrayList;
    }
	
	public FileTransfer exportVendorData (VendorData oData,  ExportImportProviderData oProviderData) throws Exception
    {
		m_oLogger.info ("exportItemData");
		m_oLogger.debug ("exportItemData - oData [IN] : " + oData);
    	FileTransfer oFile = null;
    	VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try
		{
			oVendorDataResponse = (VendorDataResponse) listVendor(oData, "", "");
	    	oFile = oProviderData.export (oVendorDataResponse.m_arrVendorData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("exportItemData - oException : " + oException);
		}
    	return oFile;
    }
	
	public GenericResponse importVendorData (VendorData oVendorData, ExportImportProviderData oProviderData, FileTransfer oFileTransfer)
	{
		m_oLogger.info ("importItemData");
		m_oLogger.debug ("importItemData - oFileTransfer [IN] : " + oFileTransfer);
		DataExchangeResponse oDataExchangeResponse = new DataExchangeResponse ();
		try 
		{
			UserInformationData oUserInformationData = new UserInformationData ();
			oDataExchangeResponse = (DataExchangeResponse) oProviderData.importData (oFileTransfer, oVendorData.getClass().getName(), oUserInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("importItemData - oException : " + oException);
		}
		return oDataExchangeResponse;
	}
	
	public VendorDataResponse verify(VendorData oData,boolean bVerify)
    {
		m_oLogger.info("verify");
		m_oLogger.debug("verify - oData [IN] :  " +oData);
		VendorData oVendorData = new VendorData ();
		VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try 
		{
			oVendorData.setM_nClientId(oData.getM_nClientId());
			oVendorData = (VendorData) populateObject(oVendorData);
			oVendorData.setM_bVerified(bVerify);
			oVendorDataResponse.m_bSuccess = oVendorData.updateObject();
			oVendorDataResponse.m_arrVendorData.add(oVendorData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("verify - oException : " +oException);
		}
		m_oLogger.debug("verify - oVendorDataResponse [OUT] :" +oVendorDataResponse);
		return oVendorDataResponse;
    }
	
	public VendorDataResponse AllowAutomaticPublishing (VendorData oData,boolean bVerify)
    {
		m_oLogger.info("AllowAutomaticPublishing");
		m_oLogger.debug("AllowAutomaticPublishing - oData [IN] :  " +oData);
		VendorData oVendorData = new VendorData ();
		VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try 
		{
			oVendorData.setM_nClientId(oData.getM_nClientId());
			oVendorData = (VendorData) populateObject(oVendorData);
			oVendorData.setM_bAllowAutomaticPublishing(bVerify);
			oVendorDataResponse.m_bSuccess = oVendorData.updateObject();
			oVendorDataResponse.m_arrVendorData.add(oVendorData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("AllowAutomaticPublishing - oException : " +oException);
		}
		m_oLogger.debug("AllowAutomaticPublishing - oVendorDataResponse [OUT] :" +oVendorDataResponse);
		return oVendorDataResponse;
    }
	
	public VendorDataResponse changePassword (VendorData oVendorData)
	{
		m_oLogger.info("changePassword");
		m_oLogger.debug("changePassword - oVendorData [IN] : " +oVendorData);
		VendorDataResponse oVendorDataResponse = new VendorDataResponse ();
		try 
		{
			oVendorDataResponse.m_bSuccess = super.changePassword((ClientData)oVendorData).m_bSuccess;
		}
		catch (Exception oException) 
		{
			m_oLogger.error("changePassword - oException : " +oException);
		}
		m_oLogger.debug("changePassword - oVendorDataResponse [OUT] : " +oVendorDataResponse);
		return oVendorDataResponse;
	}
	
	public VendorData getImagePreview (VendorData oData)
	{
		return oData;
	}
}
