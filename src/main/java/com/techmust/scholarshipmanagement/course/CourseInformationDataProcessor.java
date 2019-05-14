package com.techmust.scholarshipmanagement.course;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.ZenithHelper;

@Controller
public class CourseInformationDataProcessor extends GenericIDataProcessor <CourseInformationData>
{
	
	@Override
	@RequestMapping(value = "/courseInfoCreate",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody CourseInformationData oCourseInformationData) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oCourseInformationData [IN] : " + oCourseInformationData);
		CourseDataResponse oCourseDataResponse = new CourseDataResponse();
		try
		{
			oCourseDataResponse.m_bSuccess = oCourseInformationData.saveObject();			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		return oCourseDataResponse;
	}

	@Override
	@RequestMapping(value = "/courseInfoDelete",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody CourseInformationData oCourseInformationData) throws Exception
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oCourseInformationData.getM_nCourseId() [IN] : " + oCourseInformationData.getM_nCourseId());
		CourseDataResponse oCourseDataResponse = new CourseDataResponse();
		try
		{
			oCourseInformationData = (CourseInformationData) populateObject (oCourseInformationData);
			oCourseDataResponse.m_bSuccess = oCourseInformationData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		throw oException;
		}
		return oCourseDataResponse;
	}

	@Override
	@RequestMapping(value="/courseInfoGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody CourseInformationData oCourseInformationData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oCourseInformationData.getM_nCourseId() [IN] :" + oCourseInformationData.getM_nCourseId());
		CourseDataResponse oCourseDataResponse = new CourseDataResponse();
		try 

		{
			oCourseInformationData = (CourseInformationData) populateObject (oCourseInformationData);
			oCourseDataResponse.m_arrCourseInformationData.add (oCourseInformationData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oCourseDataResponse;
	}

	@RequestMapping(value="/courseInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oCourseInformationData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
	private GenericResponse list(CourseInformationData oCourseInformationData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oCourseInformationData [IN] : " + oCourseInformationData);
		CourseDataResponse oCourseDataResponse = new CourseDataResponse();
		try 
		{
			oCourseDataResponse.m_nRowCount = getRowCount(oCourseInformationData);
			oCourseDataResponse.m_arrCourseInformationData = new ArrayList (oCourseInformationData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oCourseDataResponse;
	}


	@Override
	@RequestMapping(value="/courseInfoUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody CourseInformationData oCourseInformationData) throws Exception
	{
		
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oCourseInformationData.getM_nCourseId() [IN] : " + oCourseInformationData.getM_nCourseId());
		CourseDataResponse oCourseDataResponse = new CourseDataResponse();
		try
		{			
			oCourseDataResponse.m_bSuccess = oCourseInformationData.updateObject();
			oCourseDataResponse.m_arrCourseInformationData.add(oCourseInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		return oCourseDataResponse;
	}

	@Override
	@RequestMapping(value="/courseInfoGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody CourseInformationData oCourseInformationData) throws Exception 
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oCourseInformationData [IN] : " +oCourseInformationData);
		String strXml = "";
		try 
		{	
			oCourseInformationData = (CourseInformationData) populateObject (oCourseInformationData);
			strXml = oCourseInformationData != null ? oCourseInformationData.generateXML ():"";
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}
	
	@RequestMapping(value="/courseInfoGetSuggestions", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public CourseDataResponse getCourseSuggestions(@RequestBody ZenithHelper oData) throws Exception
	{
		return  getCoursesSuggestions(oData.getM_oCourseInformationData(),oData.getM_strColumn(),oData.getM_strOrderBy());
	}	

	public CourseDataResponse getCoursesSuggestions(CourseInformationData oCourseInformationData, String strColumn,String strOrderBy) throws Exception
	{
		m_oLogger.info ("getCoursesSuggestions");
		m_oLogger.debug ("getCoursesSuggestions - oCourseInformationData [IN] : " + oCourseInformationData);
		m_oLogger.debug ("getCoursesSuggestions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getCoursesSuggestions - strOrderBy [IN] : " + strOrderBy);
		CourseDataResponse oCourseDataResponse = new CourseDataResponse();
		try 
		{
			oCourseDataResponse.m_arrCourseInformationData = new ArrayList(oCourseInformationData.listCustomData(this));
			
		}
		catch (Exception oException)
		{
			m_oLogger.error("getCoursesSuggestions - oException : " +oException);
			throw oException;
		}
		return oCourseDataResponse;
	}

	@Override
	public GenericResponse list(CourseInformationData oGenericData, HashMap<String, String> arrOrderBy)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
