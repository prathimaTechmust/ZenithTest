package com.techmust.usermanagement.facilitator;

import java.util.ArrayList;
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

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.HibernateUtil;
import com.techmust.helper.ZenithHelper;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.utils.Utils;

@Controller
public class FacilitatorInformationDataProcessor extends GenericIDataProcessor <FacilitatorInformationData>
{

	@Override
	@RequestMapping(value = "/facilitatorInfoCreate",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oOrganization [IN] : " + oFacilitatorInformationData);
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try
		{
			oFacilitatorDataResponse.m_bSuccess = oFacilitatorInformationData.saveObject();
			if(oFacilitatorDataResponse.m_bSuccess)
				Utils.createActivityLog("FacilitatorInformationDataProcessor::create", oFacilitatorInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		return oFacilitatorDataResponse;
	}

	@Override
	@RequestMapping(value = "/facilitatorInfoDelete",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oFacilitatorInformationData.getM_nItemId() [IN] : " + oFacilitatorInformationData.getM_nFacilitatorId());
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try
		{
			oFacilitatorInformationData = (FacilitatorInformationData) populateObject (oFacilitatorInformationData);
			oFacilitatorDataResponse.m_bSuccess = oFacilitatorInformationData.deleteObject();
			if(oFacilitatorDataResponse.m_bSuccess)
				Utils.createActivityLog("FacilitatorInformationDataProcessor::deleteData", oFacilitatorInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		throw oException;
		}
		return oFacilitatorDataResponse;
	}

	@Override
	@RequestMapping(value="/facilitatorInfoGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oFacilitatorInformationData.getM_nFacilitatorId() [IN] :" + oFacilitatorInformationData.getM_nFacilitatorId());
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try 

		{
			oFacilitatorInformationData = (FacilitatorInformationData) populateObject (oFacilitatorInformationData);
			oFacilitatorDataResponse.m_arrFacilitatorInformationData.add (oFacilitatorInformationData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oFacilitatorDataResponse;
	}

	@RequestMapping(value="/facilitatorInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strSortColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oFacilitatorInformationData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
	private GenericResponse list(FacilitatorInformationData oFacilitatorInformationData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oFacilitatorInformationData [IN] : " + oFacilitatorInformationData);
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try 
		{
			oFacilitatorDataResponse.m_nRowCount = getRowCount(oFacilitatorInformationData);
			oFacilitatorDataResponse.m_arrFacilitatorInformationData = new ArrayList (oFacilitatorInformationData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oFacilitatorDataResponse;
	}


	@Override
	@RequestMapping(value="/facilitatorInfoUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception
	{
		
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oFacilitatorInformationData.getM_nFacilitatorId() [IN] : " + oFacilitatorInformationData.getM_nFacilitatorId());
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try
		{			
			oFacilitatorDataResponse.m_bSuccess = oFacilitatorInformationData.updateObject();
			if(oFacilitatorDataResponse.m_bSuccess)
				Utils.createActivityLog("FacilitatorInformationDataProcessor::update", oFacilitatorInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		return oFacilitatorDataResponse;
	}

	@Override
	@RequestMapping(value="/facilitatorInfoGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception 
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oFacilitatorInformationData [IN] : " +oFacilitatorInformationData);
		String strXml = "";
		try 
		{	
			oFacilitatorInformationData = (FacilitatorInformationData) populateObject (oFacilitatorInformationData);
			strXml = oFacilitatorInformationData != null ? oFacilitatorInformationData.generateXML ():"";
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}	
	
	@RequestMapping(value="/facilitatorInfoGetFacilitatorSuggestions", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public FacilitatorDataResponse getInstitutionSuggestions (@RequestBody ZenithHelper oData) throws Exception
	{
		return getFacilitatorSuggestions(oData.getM_oFacilitatorInformationData(),oData.getM_strSortColumn(),oData.getM_strOrderBy());
	}
	

	public FacilitatorDataResponse getFacilitatorSuggestions(FacilitatorInformationData oFacilitatorInformationData,String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getFacilitatorSuggestions");
		m_oLogger.debug ("getFacilitatorSuggestions - FacilitatorInformationData [IN] : " + oFacilitatorInformationData);
		m_oLogger.debug ("getFacilitatorSuggestions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getFacilitatorSuggestions - strOrderBy [IN] : " + strOrderBy);
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try 
		{
			oFacilitatorDataResponse.m_arrFacilitatorInformationData = new ArrayList(oFacilitatorInformationData.listCustomData(this));
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getFacilitatorSuggestions - oException : " +oException);
			throw oException;
		}
		return oFacilitatorDataResponse;
	}	
	
	@RequestMapping(value="/getFilterFacilitatorData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getFilterFacilitatorData (@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception
	{
		m_oLogger.info("getFilterFacilitatorData");
		m_oLogger.debug("getFilterFacilitatorData FacilitatorInformationData-"+oFacilitatorInformationData);
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		EntityManager oEntityManager = oFacilitatorInformationData._getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<FacilitatorInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(FacilitatorInformationData.class);
			Root<FacilitatorInformationData> oFacilitatorRoot = oCriteriaQuery.from(FacilitatorInformationData.class);   
			List<Predicate> m_arrPredicateList = new ArrayList<Predicate>();
			if(oFacilitatorInformationData.getM_strFacilitatorName() != "")
					m_arrPredicateList.add(oCriteriaBuilder.equal(oFacilitatorRoot.get("m_strFacilitatorName"), oFacilitatorInformationData.getM_strFacilitatorName()));
			if(oFacilitatorInformationData.getM_strPhoneNumber() != "")
				m_arrPredicateList.add(oCriteriaBuilder.equal(oFacilitatorRoot.get("m_strPhoneNumber"), oFacilitatorInformationData.getM_strPhoneNumber()));
			if(oFacilitatorInformationData.getM_strCity() != "")
				m_arrPredicateList.add(oCriteriaBuilder.equal(oFacilitatorRoot.get("m_strCity"), oFacilitatorInformationData.getM_strCity()));
			 oCriteriaQuery.select(oFacilitatorRoot).where(m_arrPredicateList.toArray(new Predicate[]{}));
			List<FacilitatorInformationData> facilitatorList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(facilitatorList.size() > 0)
			{	
				oFacilitatorDataResponse.m_arrFacilitatorInformationData = new ArrayList<FacilitatorInformationData>(facilitatorList);
				oFacilitatorDataResponse.m_bSuccess = true;				
			}			
			
		}
		catch (Exception oException)
		{
			m_oLogger.debug("getFilterFacilitatorData - oException"+oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oFacilitatorDataResponse;		
	}

	@Override
	public GenericResponse list(FacilitatorInformationData oGenericData, HashMap<String, String> arrOrderBy)throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
