package com.techmust.vendormanagement;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class VendorCounterDataProcessor extends GenericIDataProcessor<VendorCounterData> 
{
	public static String kInvoice = "kInvoice";
	@Override
	public VendorCounterDataResponse create(VendorCounterData oVendorCounterData)throws Exception 
	{
		m_oLogger.info("create");
		m_oLogger.debug("create - oVendorCounterData [IN] : " + oVendorCounterData);
		VendorCounterDataResponse oVendorCounterDataResponse = new VendorCounterDataResponse ();
		try 
		{
			if (!(oVendorCounterData.isVendorExists (oVendorCounterData)))
			{
				oVendorCounterData.setM_nCreatedBy(getAdminUserInfo ().getM_nUserId());
				oVendorCounterData.setM_strKey(kInvoice);
				oVendorCounterDataResponse.m_bSuccess = oVendorCounterData.saveObject();
			}
			else
			{
				oVendorCounterDataResponse.m_strError_Desc = "Serial Number already exists for this vendor";
			}
		}
		catch (Exception oException) 
		{
			m_oLogger.error("create - oException : " + oException);
		}
		return oVendorCounterDataResponse;
	}

	@Override
	public VendorCounterDataResponse deleteData(VendorCounterData oVendorCounterData)throws Exception 
	{
		return null;
	}

	@Override
	public VendorCounterDataResponse get(VendorCounterData oVendorCounterData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oVendorCounterData.getM_nId() [IN] :" +oVendorCounterData.getM_nId());
		VendorCounterDataResponse oVendorCounterDataResponse = new VendorCounterDataResponse ();
		try 
		{
			oVendorCounterData = (VendorCounterData) populateObject (oVendorCounterData);
			oVendorCounterDataResponse.m_arrVendorCounterData.add (oVendorCounterData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oVendorCounterDataResponse;
	}

	@Override
	public String getXML(VendorCounterData oVendorCounterData) throws Exception
	{
		return null;
	}

	@Override
	public VendorCounterDataResponse list(VendorCounterData oVendorCounterData,HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oVendorCounterData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public VendorCounterDataResponse list (VendorCounterData oVendorCounterData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oVendorCounterData [IN] : " + oVendorCounterData);
		VendorCounterDataResponse oVendorCounterDataResponse = new VendorCounterDataResponse ();
		try
		{
			oVendorCounterDataResponse.m_nRowCount = getRowCount(oVendorCounterData);
			oVendorCounterDataResponse.m_arrVendorCounterData = new ArrayList (oVendorCounterData.list (arrOrderBy, nPageNumber, nPageSize));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("list - oException : " + oException);
		}
		return oVendorCounterDataResponse;
	}

	@Override
	public VendorCounterDataResponse update(VendorCounterData oVendorCounterData)throws Exception
	{
		m_oLogger.info("update");
		m_oLogger.debug("update - oVendorCounterData [IN] : " + oVendorCounterData);
		VendorCounterDataResponse oVendorCounterDataResponse = new VendorCounterDataResponse ();
		try 
		{
			oVendorCounterData.setM_nCreatedBy(getAdminUserInfo ().getM_nUserId());
			oVendorCounterData.setM_strKey(kInvoice);
			oVendorCounterDataResponse.m_bSuccess = oVendorCounterData.updateObject();
		}
		catch (Exception oException) 
		{
			m_oLogger.error("update - oException : " + oException);
		}
		return oVendorCounterDataResponse;
	}
	
	private UserInformationData getAdminUserInfo() 
	{
		m_oLogger.info ("getAdminUserInfo");
		UserInformationData oUserData = new UserInformationData ();
		try
		{	
			oUserData.setM_strUserName("admin");
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oUserData = (UserInformationData) oUserData.list(oOrderBy).get(0);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getAdminUserInfo - oException : " + oException);
		}
		return oUserData;
	}
	
}
