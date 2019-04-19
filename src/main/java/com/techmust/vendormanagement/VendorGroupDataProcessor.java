package com.techmust.vendormanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class VendorGroupDataProcessor extends GenericIDataProcessor<VendorGroupData> 
{
	@Override
	@RequestMapping(value="/vendorGroupCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody VendorGroupData oGroupData) throws Exception 
	{
		m_oLogger.debug("create");
		m_oLogger.debug("create - oGroupData [IN] :" + oGroupData);
		VendorGroupResponse oResponse = new VendorGroupResponse ();
		try 
		{
			createLog(oGroupData, "VendorGroupDataProcessor.create : ");
			isvalidUser (oGroupData);
			HashSet<VendorData> oVendors = new HashSet<VendorData> ();
			oVendors.addAll (buildVendorList (oGroupData.m_arrVendorData));
			oVendors.remove (null);
			oGroupData.setM_oVendors (oVendors);
			oResponse.m_bSuccess = oGroupData.saveObject();
		} 
		catch (Exception oException)
		{
			m_oLogger.error("create -oException " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oResponse.m_bSuccess [OUT] : " + oResponse.m_bSuccess);
		return oResponse;
	}

	@Override
	@RequestMapping(value="/vendorGroupDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody VendorGroupData oGroupData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug("deleteData - oGroupData.getM_nGroupId() [IN] :" +oGroupData.getM_nGroupId());
		VendorGroupResponse oResponse = new VendorGroupResponse ();
		try 
		{
			createLog(oGroupData, "VendorGroupDataProcessor.deleteData : ");
			isvalidUser (oGroupData);
			oResponse.m_bSuccess = oGroupData.deleteObject();
		} 
		catch (Exception oException)
		{
			m_oLogger.error("deleteData -oException " + oException);
		}
		m_oLogger.error("deleteData - oResponse.m_bSuccess [OUT] :  " + oResponse.m_bSuccess);
		return oResponse;
	}

	@Override
	@RequestMapping(value="/vendorGroupGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody VendorGroupData oGroupData) throws Exception 
	{
		m_oLogger.debug("get - oLocationData.getM_nGroupId() [IN] :" +oGroupData.getM_nGroupId());
		VendorGroupResponse oResponse = new VendorGroupResponse ();
		try 
		{
			isvalidUser (oGroupData);
			oGroupData = (VendorGroupData) populateObject(oGroupData);
			oGroupData.setM_strDate(getClientCompatibleFormat(oGroupData.getM_dCreatedOn()));
			oResponse.m_arrGroupData.add(oGroupData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("get -oException " + oException);
		}
		return oResponse;
	}

	@Override
	@RequestMapping(value="/vendorGroupGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody VendorGroupData oGroupData) throws Exception 
	{
		isvalidUser (oGroupData);
		oGroupData = (VendorGroupData) populateObject(oGroupData);
	    return oGroupData != null ? oGroupData.generateXML () : "";
	}
	
	@RequestMapping(value="/vendorGroupList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData)throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list(oData.getM_oVendorGroupData(),oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}

	@Override
	public GenericResponse list(VendorGroupData oGroupData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oGroupData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
    public GenericResponse list(VendorGroupData oGroupData,  HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
            throws Exception
    {
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oGroupData [IN] : " +oGroupData);
		VendorGroupResponse oResponse = new VendorGroupResponse ();
		try 
		{
			isvalidUser (oGroupData);
			oResponse.m_nRowCount = getRowCount(oGroupData);
			oResponse.m_arrGroupData = new ArrayList (oGroupData.list (arrOrderBy, nPageNumber, nPageSize));
			oResponse.m_arrGroupData = buildVendorGroupData (oResponse.m_arrGroupData);

		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oResponse;
    }

	@Override
	@RequestMapping(value="/vendorGroupUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody VendorGroupData oGroupData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oGroupData.getM_nGroupId() [IN] : " + oGroupData.getM_nGroupId());
		VendorGroupResponse oResponse = new VendorGroupResponse ();
		try
		{
			createLog(oGroupData, "VendorGroupDataProcessor.update : ");
			isvalidUser (oGroupData);
			HashSet<VendorData> oVendors = new HashSet<VendorData> ();
			oVendors.addAll (buildVendorList (oGroupData.m_arrVendorData));
			oVendors.remove (null);
			oGroupData.setM_oVendors (oVendors);
			oResponse.m_bSuccess = oGroupData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oResponse.m_bSuccess [OUT] : " + oResponse.m_bSuccess);
		return oResponse;
	}
	
	private Collection<VendorData> buildVendorList (VendorData [] m_arrVendorData)
    {
		m_oLogger.info ("buildVendorList");
		m_oLogger.debug ("buildVendorList - m_arrVendorData.length [IN] : " + m_arrVendorData != null ? m_arrVendorData.length : 0);
		ArrayList<VendorData> oArrayList = new ArrayList<VendorData> ();
		try
		{
			for (int nIndex = 0; m_arrVendorData != null && nIndex < m_arrVendorData.length; nIndex++)
				oArrayList.add(m_arrVendorData [nIndex]);
		}
		catch(Exception oException)
		{
			m_oLogger.error ("buildVendorList - oException : " +oException);
		}
		return oArrayList;
    }
	
	private ArrayList<VendorGroupData> buildVendorGroupData(ArrayList<VendorGroupData> arrVendorGroupData) 
	{
		m_oLogger.info("buildSalesData");
		for (int nIndex=0; nIndex < arrVendorGroupData.size(); nIndex++)
			arrVendorGroupData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrVendorGroupData.get(nIndex).getM_dCreatedOn())); 
		return arrVendorGroupData;
	}
	
	private void isvalidUser (VendorGroupData oGroupData) throws Exception
	{
		m_oLogger.info("isvalidUser");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oGroupData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oGroupData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}
	
	private void createLog (VendorGroupData oGroupData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oGroupData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oGroupData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName + oGroupData.getM_strGroupName());
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
}

