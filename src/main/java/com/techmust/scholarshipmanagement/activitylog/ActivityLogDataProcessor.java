package com.techmust.scholarshipmanagement.activitylog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.constants.Constants;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.HibernateUtil;
import com.techmust.helper.ZenithHelper;

@Controller
public class ActivityLogDataProcessor  extends GenericIDataProcessor<ActivityLog>
{
	
	@Override
	public GenericResponse create(ActivityLog oActivityLog) throws Exception
	{
		m_oLogger.info("create - ActivityLog"+oActivityLog);
		m_oLogger.debug("create - ActivityLog"+oActivityLog);
		ActivityLogResponse oActivityLogResponse = new ActivityLogResponse();
		try 
		{
			oActivityLogResponse.m_bSuccess  = oActivityLog.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error("create - oException"+ oException);
			throw oException;
		}
		return oActivityLogResponse;
	}
	
	@RequestMapping(value="/activityLogListInfo", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strSortColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oActivityLog(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
	private GenericResponse list(ActivityLog oActivityLog, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oActivityLogData [IN] : " + oActivityLog);
		ActivityLogResponse oActivityLogResponse = new ActivityLogResponse();
		try 
		{
			oActivityLogResponse.m_nRowCount = getRowCount(oActivityLog);
			oActivityLogResponse.m_arrActivityLog = new ArrayList (oActivityLog.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oActivityLogResponse;
	}
	
	@RequestMapping(value = "/sortActivityLogListInfo",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse sortingList(@RequestBody ZenithHelper oZenithHelper)
	{
		m_oLogger.info("sortingList");
		m_oLogger.debug("sortingList Zenith Helper"+ oZenithHelper);
		ActivityLogResponse oActivityLogResponse = new ActivityLogResponse();		
		try
		{
			HashMap<String, String> sortBy = new HashMap<String,String>();
			sortBy.put(oZenithHelper.getM_strSortColumn(), oZenithHelper.getM_strOrderBy());
			oActivityLogResponse.m_arrActivityLog = new ArrayList(oZenithHelper.list(sortBy,oZenithHelper.getM_nPageNo(),oZenithHelper.getM_nPageSize()));
		}
		catch (Exception oException)
		{
			m_oLogger.error("sortingList - oException "+oException);
		}
		return oActivityLogResponse;
		
	}
	
	@RequestMapping(value = "/getLoginUsersList",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getLoginUsersList(@RequestBody ActivityLog oActivityLog)
	{
		m_oLogger.info("getLoginUsersList");
		m_oLogger.debug("getLoginUsersList ActivityLog"+ oActivityLog);
		ActivityLogResponse oActivityLogResponse = new ActivityLogResponse();
		EntityManager oEntityManager = oActivityLog._getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ActivityLog> oCriteriaQuery = oCriteriaBuilder.createQuery(ActivityLog.class);
			Root<ActivityLog> oRootActivityLog = oCriteriaQuery.from(ActivityLog.class);
			oCriteriaQuery.select(oRootActivityLog.get("m_strLoginUserName")).distinct(true);
			oActivityLogResponse.m_arrActivityLog = new ArrayList(oEntityManager.createQuery(oCriteriaQuery).getResultList());			
		}
		catch (Exception oException)
		{
			m_oLogger.error("getLoginUsersList - oException "+oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oActivityLogResponse;		
	}
	
	@RequestMapping(value = "/activityLogInfoGetXML",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody	
	public String getXML(@RequestBody ActivityLog oActivityLog) throws Exception
	{
		m_oLogger.info("getXML");
		m_oLogger.debug("getXML - ActivityLog"+oActivityLog);
		String strActivityLogXMLData = "";
		try 
		{
			oActivityLog = (ActivityLog) populateObject(oActivityLog);
			strActivityLogXMLData = oActivityLog != null ? oActivityLog.generateXML() : "";
		}
		catch (Exception oException) 
		{
			m_oLogger.error("getXML - oException"+oException);
		}
		return strActivityLogXMLData;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getFilteredActivityLog",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getFilteredActivityLog(@RequestBody ActivityLog oActivityLog)
	{
		m_oLogger.info("getFilteredActivityLog");
		m_oLogger.debug("getFilteredActivityLog - ActivityLog"+oActivityLog);
		ActivityLogResponse oActivityLogResponse = new ActivityLogResponse();
		try 
		{
			oActivityLogResponse.m_arrActivityLog = (ArrayList<ActivityLog>) populateFilterObjectData(oActivityLog);
			if(oActivityLogResponse.m_arrActivityLog.size() > 0)
				oActivityLogResponse.m_bSuccess = true;
		}
		catch (Exception oException)
		{
			m_oLogger.error("getFilteredActivityLog - oException"+oException);
		}
		return oActivityLogResponse;		
	}	
	
	@Override
	public GenericResponse deleteData(ActivityLog oActivityLog) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(ActivityLog oActivityLog) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public GenericResponse update(ActivityLog oActivityLog) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public GenericResponse list(ActivityLog oActivityLog, HashMap<String, String> arrOrderBy) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
}
