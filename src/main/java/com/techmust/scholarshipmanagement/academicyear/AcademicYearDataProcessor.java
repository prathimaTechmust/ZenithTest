package com.techmust.scholarshipmanagement.academicyear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.HibernateUtil;
import com.techmust.helper.ZenithHelper;
import com.techmust.utils.Utils;

@Controller
public class AcademicYearDataProcessor extends GenericIDataProcessor<AcademicYear>
{

	@Override
	@RequestMapping(value="/academicyearcreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody	
	public GenericResponse create(@RequestBody AcademicYear oAcademicYear) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oAcademicYear [IN] : " + oAcademicYear);
		AcademicYearResponse oAcademicYearResponse = new AcademicYearResponse();
		try
		{
			oAcademicYearResponse.m_bSuccess = oAcademicYear.saveObject();
			Utils.createActivityLog("AcademicYearDataProcessor::create", oAcademicYear);
			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		return oAcademicYearResponse;
	}

	
	@RequestMapping(value="/academicyearInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oZenithHelper) throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String,String>();
		oOrderBy.put(oZenithHelper.getM_strSortColumn(), oZenithHelper.getM_strOrderBy());
		return list(oZenithHelper.getM_oAcademicYear(),oOrderBy,oZenithHelper.getM_nPageNo(),oZenithHelper.getM_nPageSize());
	}

	@SuppressWarnings("unchecked")
	private GenericResponse list(AcademicYear oAcademicYear, HashMap<String, String> arrOrderBy, int nPageNumber,	int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oAcademicYear [IN] : " + oAcademicYear);
		AcademicYearResponse oAcademicYearResponse = new AcademicYearResponse();
		try 
		{
			oAcademicYearResponse.m_nRowCount = getRowCount(oAcademicYear);
			oAcademicYearResponse.m_arrAcademicYear = new ArrayList (oAcademicYear.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oAcademicYearResponse;
	}

	@RequestMapping(value = "/academicyearcreateandupdate",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse createAndUpdate (@RequestBody AcademicYear oAcademicYear)
	{
		m_oLogger.info("createAndUpdate");
		m_oLogger.debug("createAndUpdate - AcademicYear"+oAcademicYear);
		AcademicYearResponse oAcademicYearResponse = new AcademicYearResponse();
		try 
		{
			if(checkAcademicExists(oAcademicYear))
			{
				oAcademicYearResponse.m_bSuccess = updateAcademicYearData(oAcademicYear);
			}				
			else
			{
				updateOldAcademicValue();
				oAcademicYearResponse.m_bSuccess = oAcademicYear.saveObject();
			}
				
		}
		catch (Exception oException)
		{
			m_oLogger.error("createAndUpdate - oException"+oException);
		}
		return oAcademicYearResponse;		
	}

	private boolean updateAcademicYearData(AcademicYear oData)
	{
		m_oLogger.info("updateAcademicYearData");
		m_oLogger.debug("updateAcademicYearData - AcademicYear"+oData);
		boolean bIsUpdate = false;
		AcademicYear oAcademicYear = new AcademicYear();
		try
		{
			AcademicYear oYear = (AcademicYear) populateObject(oData);			
			oYear.setM_bDefaultYear(oData.isM_bDefaultYear());
			updateOldAcademicValue();
			bIsUpdate = oYear.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateAcademicYearData - oException"+oException);
		}
		return bIsUpdate;		
	}

	private void updateOldAcademicValue()
	{
		AcademicYear oYear = new AcademicYear();
		EntityManager oEntityManager = oYear._getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<AcademicYear> oCriteriaQuery = oCriteriaBuilder.createQuery(AcademicYear.class);
			Root<AcademicYear> oRoot = oCriteriaQuery.from(AcademicYear.class);
			oCriteriaQuery.select(oRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oRoot.get("m_bDefaultYear"), true));
			List<AcademicYear> m_YearList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(m_YearList.size() > 0)
			{
				oYear = m_YearList.get(0);
				oYear.setM_bDefaultYear(false);
				boolean isUpdate = oYear.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateOldAcademicValue - oException"+oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
	}


	private boolean checkAcademicExists(AcademicYear oAcademicYear)
	{
		boolean bIsExists = false;
		m_oLogger.info("checkAcademicExists");
		m_oLogger.debug("checkAcademicExists - AcademicYear"+oAcademicYear);
		EntityManager oEntityManager = oAcademicYear._getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<AcademicYear> oCriteriaQuery = oCriteriaBuilder.createQuery(AcademicYear.class);
			Root<AcademicYear> oAcademicRoot = oCriteriaQuery.from(AcademicYear.class);
			oCriteriaQuery.select(oAcademicRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oAcademicRoot.get("m_strAcademicYear"), oAcademicYear.getM_strAcademicYear()));
			List<AcademicYear> m_AcademicYearList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(m_AcademicYearList.size() > 0)
				bIsExists = true;
		} 
		catch (Exception oException)
		{
			m_oLogger.error("checkAcademicExists - oException"+oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return bIsExists;		
	}


	@RequestMapping(value = "/academicyearupdate",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody	
	public GenericResponse update(@RequestBody ZenithHelper oZenithHelper) throws Exception
	{		
		return null;
	}
	
	@Override
	public GenericResponse deleteData(AcademicYear oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(AcademicYear oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXML(AcademicYear oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse list(AcademicYear oGenericData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse update(AcademicYear oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
}
