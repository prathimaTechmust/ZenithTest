package com.techmust.usermanagement.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.log4j.Logger;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.usermanagement.action.ActionData;
import com.techmust.usermanagement.actionarea.ActionAreaData;

public class RoleDataProcessor extends GenericIDataProcessor<RoleData>
{
	Logger m_oLogger = Logger.getLogger(RoleDataProcessor.class);
	
	@Override
	public RoleResponse create (RoleData oData)
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " +oData);
		RoleResponse oRoleResponse = new RoleResponse ();
		try
		{
			HashSet<ActionData> oActions = new HashSet<ActionData> ();
			oActions.addAll (buildActionList (oData.m_arrActionData));
			oActions.remove (null);
			oData.setm_oActions (oActions);
			oRoleResponse.m_bSuccess = oData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " +oException);
		}
		m_oLogger.debug ("create - oRoleResponse.m_bSuccess [OUT] : "
							+oRoleResponse.m_bSuccess);
		return oRoleResponse;
	}

	@Override
	public RoleResponse get (RoleData oData) 
	{
		m_oLogger.info("get");
		m_oLogger.debug ("get - oData[IN] : " +oData);
		RoleResponse oRoleResponse = new RoleResponse ();
		try 
		{
			oData = (RoleData) populateObject (oData);
			oRoleResponse.m_arrRoleData.add (oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("get - oException :" +oException);
		}
		return oRoleResponse;
	}

	@SuppressWarnings("unchecked")
	public RoleResponse listActionData () 
	{
		m_oLogger.info ("listActionData");
		RoleResponse oRoleResponse = new RoleResponse ();
		ActionData oActionData = new ActionData ();
		ActionAreaData oActionAreaData = new ActionAreaData ();
		try
		{
			oActionData.setM_oActionArea(oActionAreaData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oRoleResponse.m_arrActionData = new ArrayList (oActionData.list (oOrderBy));
			oRoleResponse.m_bSuccess = true;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("listActionData - oException : " +oException);
		}
		m_oLogger.debug ("listActionData - oRoleResponse.m_arrActionData.size [OUT] : "
							+oRoleResponse.m_arrActionData.size ());
		return oRoleResponse;
	}

	@Override
	public RoleResponse list(RoleData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public RoleResponse list (RoleData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) 
	{
		m_oLogger.info("list");
		m_oLogger.debug ("list - oData[IN] : " +oData);
		RoleResponse oRoleResponse = new RoleResponse();
		try
		{
			oRoleResponse.m_nRowCount = getRowCount(oData);
			oRoleResponse.m_arrRoleData = new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("list - oException : " +oException);
		}
		return oRoleResponse;
	}

	@Override
	public RoleResponse update (RoleData oData) 
	{
		m_oLogger.debug ("update");
		m_oLogger.error ("update - oData.getM_nRoleId () [IN] : " +oData.getM_nRoleId ());
		RoleResponse oRoleResponse = new RoleResponse ();
		try
		{
			HashSet<ActionData> oActions = new HashSet<ActionData> ();
			oActions.addAll(buildActionList (oData.m_arrActionData));
			oActions.remove (null);
			oData.setm_oActions (oActions);
			oRoleResponse.m_bSuccess = oData.updateObject ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " +oException);
		}
		m_oLogger.error ("update - oRoleResponse.m_bSuccess [OUT] : " 
							+oRoleResponse.m_bSuccess);
		return oRoleResponse;
	}
	
	@Override
	public RoleResponse deleteData (RoleData oData) 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " +oData);
		RoleResponse oRoleResponse = new RoleResponse ();
		try
		{
			oRoleResponse.m_bSuccess = oData.deleteObject ();
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("deleteData - oException : " +oException);
		}
		m_oLogger.debug ("deleteData - oRoleResponse.m_bSuccess [OUT] : " 
							+oRoleResponse.m_bSuccess);
		return oRoleResponse;
	}
	
	@Override
	public String getXML (RoleData oData)
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oData [IN] : " +oData);
		String strXml = "";
		try 
		{
			oData = (RoleData)populateObject(oData);
			strXml = oData.generateXML();
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}
	
	private Collection<ActionData> buildActionList (ActionData [] arrActionData)
    {
		m_oLogger.info ("buildActionList");
		m_oLogger.debug ("buildActionList - arrActionData.length [IN] : " + arrActionData != null ? arrActionData.length : 0);
		ArrayList<ActionData> oArrayList = new ArrayList<ActionData> ();
		try
		{
			for (int nIndex = 0; arrActionData != null && nIndex < arrActionData.length; nIndex++)
				oArrayList.add(arrActionData [nIndex]);
		}
		catch(Exception oException)
		{
			m_oLogger.error ("buildActionList - oException : " +oException);
		}
		return oArrayList;
    }
}