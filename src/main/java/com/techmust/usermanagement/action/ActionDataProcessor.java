package com.techmust.usermanagement.action;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
/**
 * Holds the functionalities for creating action,
 * updating action, deleting action and listing actions 
 * @author Techmust software pvt ltd
 *
 */
public class ActionDataProcessor extends GenericIDataProcessor<ActionData>
{
	/**
	  * Logger variable
	 */
	Logger m_oLogger = Logger.getLogger(ActionDataProcessor.class);
	
	/**
	 * Creates a new action
	 * @param oData - ActionData
	 * @return Returns an ActionResponse object
	 */
	@Override
	public ActionResponse create (ActionData oData) 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " +oData);
		ActionResponse oActionResponse = new ActionResponse ();
		try 
		{
			oActionResponse.m_bSuccess = oData.saveObject ();
			oActionResponse.m_arrActionData.add (oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("create - oException : "  +oException);
		}
		return oActionResponse;
	}
	
	/**
	 * Returns the ActionData in ActionResponse object for given Action id
	 * @param oData - ActionData (set the action id using the function setM_nActionId ())
	 * @return Returns an ActionResponse object
	 */
	@Override
	public ActionResponse get (ActionData oData)
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData [IN] : " +oData);
		ActionResponse oActionResponse = new ActionResponse ();
		ActionData oActionData = new ActionData ();
		try 
		{
			oActionData.setM_nActionId (oData.getM_nActionId ());
			oActionData = (ActionData) populateObject (oActionData);
			oActionResponse.m_arrActionData.add (oActionData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : " +oException);
		}
		return oActionResponse;
	}
	
	/**
	 * Lists the actions based on the provided criteria
	 * @param oData ActionData
	 * @param strColumn - the hibernate column name
	 * @param strOrderBy - the order by key ("asc" for ascending "dsc" for descending)
	 * @return Returns an ActionResponse object
	 */
	

	@Override
	public ActionResponse list(ActionData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public ActionResponse list (ActionData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " +oData);
		ActionResponse oActionResponse = new ActionResponse ();
		try 
		{	
			oActionResponse.m_nRowCount = getRowCount(oData);
			oActionResponse.m_arrActionData = new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("list - oException : " +oException);
		}
		return oActionResponse;
	}
	
	/**
	 * Updates an action
	 * @param oData - ActionData
	 * @return returns an ActionResponse object (contains the updated ActionData object in ArrayList<ActionData>)
	 */
	@Override
	public ActionResponse update (ActionData oData)
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData [IN] : " +oData);
		ActionResponse oActionResponse = new ActionResponse ();
		try 
		{
			oActionResponse.m_bSuccess = oData.updateObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException : " +oException);
		}
		return oActionResponse;
	}
	
	/**
	 * Deletes an Action
	 * @param oData - ActionData
	 * @return Returns an ActionResponse object
	 */
	@Override
	public ActionResponse deleteData (ActionData oData)
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " +oData);
		ActionResponse oActionResponse = new ActionResponse ();
		try 
		{
			oActionResponse.m_bSuccess = oData.deleteObject ();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("deleteData - oException : " +oException);
		}
		return oActionResponse;
	}

	@Override
	public String getXML (ActionData oData)
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oData [IN] : " +oData);
		String strXml = "";
		try
		{
			oData = (ActionData) populateObject (oData);
			strXml = oData.generateXML ();
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getXML - oException : " + oException);
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}
}