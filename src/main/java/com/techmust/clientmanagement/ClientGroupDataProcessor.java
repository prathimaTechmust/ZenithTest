package com.techmust.clientmanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class ClientGroupDataProcessor extends GenericIDataProcessor<ClientGroupData> 
{

	@Override
	public GenericResponse create(ClientGroupData oData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oData);
		ClientGroupDataResponse oClientGroupDataResponse = new ClientGroupDataResponse ();
		try
		{
			isvalidUser (oData);
			HashSet<ClientData> oClients = new HashSet<ClientData> ();
			oClients.addAll (buildClientList (oData.m_arrClientData));
			oClients.remove (null);
			oData.setM_oClientSet (oClients);
			oClientGroupDataResponse.m_bSuccess = oData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oClientGroupDataResponse.m_bSuccess [OUT] : " + oClientGroupDataResponse.m_bSuccess);
		return oClientGroupDataResponse;
	}

	@Override
	public GenericResponse deleteData(ClientGroupData oData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData.getM_nGroupId() [IN] : " + oData.getM_nGroupId());
		ClientGroupDataResponse oClientGroupDataResponse = new ClientGroupDataResponse ();
		try
		{
			isvalidUser (oData);
			oClientGroupDataResponse.m_bSuccess = oData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oClientGroupDataResponse.m_bSuccess [OUT] : " + oClientGroupDataResponse.m_bSuccess);
		return oClientGroupDataResponse;
	}

	@Override
	public GenericResponse get(ClientGroupData oData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData.getM_nGroupId() [IN] :" +oData.getM_nGroupId());
		ClientGroupDataResponse oClientGroupDataResponse = new ClientGroupDataResponse ();
		try 
		{
			isvalidUser (oData);
			oData = (ClientGroupData) populateObject (oData);
			oData.setM_strDate(getClientCompatibleFormat(oData.getM_dCreatedOn()));
			oClientGroupDataResponse.m_arrGroupData.add (oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oClientGroupDataResponse;
	}

	@Override
	public String getXML(ClientGroupData oData) throws Exception 
	{
		isvalidUser (oData);
		oData = (ClientGroupData) populateObject(oData);
	    return oData != null ? oData.generateXML () : "";
	}

	@Override
	public GenericResponse list(ClientGroupData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}

	@SuppressWarnings("unchecked")
	public GenericResponse list(ClientGroupData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
    throws Exception
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " +oData);
		ClientGroupDataResponse oClientGroupDataResponse = new ClientGroupDataResponse ();
		try 
		{
			isvalidUser (oData);
			oClientGroupDataResponse.m_nRowCount = getRowCount(oData);
			oClientGroupDataResponse.m_arrGroupData = new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize));
			oClientGroupDataResponse.m_arrGroupData = buildVendorGroupData (oClientGroupDataResponse.m_arrGroupData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oClientGroupDataResponse;
	}
	
	@SuppressWarnings("unchecked")
	public ClientGroupDataResponse getClientGroupSuggesstions (ClientGroupData oClientGroupData, String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getClientGroupSuggesstions");
		m_oLogger.debug ("getClientGroupSuggesstions - oClientGroupData [IN] : " + oClientGroupData);
		m_oLogger.debug ("getClientGroupSuggesstions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getClientGroupSuggesstions - strOrderBy [IN] : " + strOrderBy);
		ClientGroupDataResponse oClientGroupDataResponse = new ClientGroupDataResponse ();
		try 
		{
			oClientGroupDataResponse.m_arrGroupData = new ArrayList (oClientGroupData.listCustomData(this));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getClientGroupSuggesstions - oException : " +oException);
			throw oException;
		}
		return oClientGroupDataResponse;
	}
	
	private ArrayList<ClientGroupData> buildVendorGroupData(ArrayList<ClientGroupData> arrClientGroupData) 
	{
		m_oLogger.info("buildSalesData");
		for (int nIndex=0; nIndex < arrClientGroupData.size(); nIndex++)
			arrClientGroupData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrClientGroupData.get(nIndex).getM_dCreatedOn())); 
		return arrClientGroupData;
	}

	@Override
	public GenericResponse update(ClientGroupData oData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oTax.getM_nGroupId() [IN] : " + oData.getM_nGroupId());
		ClientGroupDataResponse oClientGroupDataResponse = new ClientGroupDataResponse ();
		try
		{
			isvalidUser (oData);
			HashSet<ClientData> oClients = new HashSet<ClientData> ();
			oClients.addAll (buildClientList (oData.m_arrClientData));
			oClients.remove (null);
			oData.setM_oClientSet (oClients);
			oClientGroupDataResponse.m_bSuccess = oData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oClientGroupDataResponse.m_bSuccess [OUT] : " + oClientGroupDataResponse.m_bSuccess);
		return oClientGroupDataResponse;
	}
	
	public Criteria prepareCustomCriteria(Criteria oCriteria, ClientGroupData oClientGroupData) throws RuntimeException 
	{
		oCriteria.add (Restrictions.ilike ("m_strGroupName", oClientGroupData.getM_strGroupName().trim(), MatchMode.START));
		oCriteria.setMaxResults(50);
		return oCriteria;
	}
	
	private Collection<ClientData> buildClientList (ClientData [] arrClientData)
    {
		m_oLogger.info ("buildClientList");
		m_oLogger.debug ("buildClientList - arrClientData.length [IN] : " + arrClientData != null ? arrClientData.length : 0);
		ArrayList<ClientData> oArrayList = new ArrayList<ClientData> ();
		try
		{
			for (int nIndex = 0; arrClientData != null && nIndex < arrClientData.length; nIndex++)
				oArrayList.add(arrClientData [nIndex]);
		}
		catch(Exception oException)
		{
			m_oLogger.error ("buildClientList - oException : " +oException);
		}
		return oArrayList;
    }
	
	private void isvalidUser (ClientGroupData oData) throws Exception
	{
		m_oLogger.info("isvalidUser");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}
}
