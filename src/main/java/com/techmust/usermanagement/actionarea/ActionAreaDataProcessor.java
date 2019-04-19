package com.techmust.usermanagement.actionarea;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.util.HibernateUtil;

public class ActionAreaDataProcessor extends GenericIDataProcessor<ActionAreaData>
{
	private static final int kErrorActionAreaAlreadyExisted = 1;
	
	public static Logger m_oLogger = Logger.getLogger(ActionAreaDataProcessor.class);
	
	@Override
	public ActionAreaResponse create (ActionAreaData oData) 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " +oData);
		ActionAreaResponse oActionAreaResponse = new ActionAreaResponse ();
		try
		{
			boolean bIsNameExist = isActionAreaExist (oData);
			if (bIsNameExist)
				oActionAreaResponse.m_nErrorID = kErrorActionAreaAlreadyExisted;
			else
			{
				oActionAreaResponse.m_bSuccess = oData.saveObject ();
				oActionAreaResponse.m_arrActionArea.add (oData);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " +oException);
		}
		return oActionAreaResponse;
	}
	
	@Override
	public ActionAreaResponse get (ActionAreaData oData) 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oData [IN] : " +oData);
		ActionAreaResponse oActionAreaResponse = new ActionAreaResponse ();
		ActionAreaData oActionAreaData = new ActionAreaData ();
		
		try
		{
			oActionAreaData.setM_nActionAreaId (oData.getM_nActionAreaId ());
			oActionAreaData = (ActionAreaData) my_populateObject (oActionAreaData);
			oActionAreaResponse.m_arrActionArea.add (oActionAreaData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("get - oException : " +oException);
		}
		return oActionAreaResponse;
	}
	
	@Override
	public ActionAreaResponse list(ActionAreaData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public ActionAreaResponse list (ActionAreaData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " +oData);
		ActionAreaResponse oActionAreaResponse = new ActionAreaResponse ();
		try
		{
			oActionAreaResponse.m_nRowCount = getRowCount(oData);
			oActionAreaResponse.m_arrActionArea = new ArrayList (oData.list(arrOrderBy, nPageNumber, nPageSize));
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("list - oException : " +oException);
		}
		return oActionAreaResponse;
	}
	
	@Override
	public ActionAreaResponse update (ActionAreaData oData)
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData [IN] : " +oData);
		ActionAreaResponse oActionAreaResponse = new ActionAreaResponse ();
		try
		{
			oActionAreaResponse.m_bSuccess = oData.updateObject ();
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("update - oException : " +oException);
		}
		return oActionAreaResponse;
	}
	
	@Override
	public ActionAreaResponse deleteData (ActionAreaData oData)
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " +oData);
		ActionAreaResponse oActionAreaResponse = new ActionAreaResponse ();
		try
		{	
			oActionAreaResponse.m_bSuccess = oData.deleteObject ();
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("delete - oException : " +oException);
		}
		return oActionAreaResponse;
	}
	
	@Override
	public String getXML (ActionAreaData oData)
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oData [IN] : " +oData);
		String strXml = "";
		try 
		{
			oData = (ActionAreaData) my_populateObject (oData);
			strXml = oData.generateXML ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getXML - oException : " +oException);
		}
		m_oLogger.debug ("getXML - strXml [OUT] :" +strXml);
		return strXml;
	}

	private boolean isActionAreaExist (ActionAreaData oData)
	{
		m_oLogger.info ("isActionAreaExist");
		m_oLogger.debug ("isActionAreaExist - oData [IN] : " +oData);
		boolean bIsActionAreaExist = false;
		try
		{
			oData = (ActionAreaData) my_populateObject(oData);
			if (oData != null)
				bIsActionAreaExist = true;
		}
		catch (Exception oException)
		{
			m_oLogger.error ("isActionAreaExist - oException : " +oException);
		}
		m_oLogger.debug ("isActionAreaExist - bIsActionAreaExist [OUT] : " +bIsActionAreaExist);
		return bIsActionAreaExist;
	}
	
	public Object my_populateObject (ActionAreaData  oActionAreaData) throws Exception
	{
		m_oLogger.debug("my_populateObject");
		m_oLogger.debug("my_populateObject - oGenericData [IN] :" +oActionAreaData);
		EntityManager oEntityManager = oActionAreaData._getEntityManager();
		Object oResult = null;
		ArrayList<ActionAreaData> arrGenericData = new ArrayList<ActionAreaData> ();
		try
		{
 			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ActionAreaData> oCriteria = (CriteriaQuery<ActionAreaData>) oCriteriaBuilder.createQuery(oActionAreaData.getClass());
			Root<? extends ActionAreaData> oRootObject = oCriteria.from(oActionAreaData.getClass());
			oCriteria.select( oRootObject );
			Predicate oConjunct = oCriteriaBuilder.conjunction();
			if (!oActionAreaData.getM_strActionAreaName().isEmpty ()) 
			{
				oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(
						oCriteriaBuilder.lower(oRootObject.get("m_strActionAreaName")), 
						oActionAreaData.getM_strActionAreaName().toLowerCase())); 
			}
			else
			{
				oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nActionAreaId"), oActionAreaData.getM_nActionAreaId())); 
			}
			oCriteria.where(oConjunct);
			arrGenericData = (ArrayList<ActionAreaData>) oEntityManager.createQuery( oCriteria ).getResultList();
			if(arrGenericData.size() > 0)
				oResult = arrGenericData.get(0);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("populateObject - oException :" +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("populateObject - oResult [OUT] :" +oResult);
		return oResult;
	}
}