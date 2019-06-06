package com.techmust.scholarshipmanagement.academicdetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.ZenithHelper;

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
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		return oAcademicYearResponse;
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

	@RequestMapping(value="/academicyearInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oZenithHelper) throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String,String>();
		oOrderBy.put(oZenithHelper.getM_strColumn(), oZenithHelper.getM_strOrderBy());
		return list(oZenithHelper.getM_oAcademicYear(),oOrderBy,oZenithHelper.getM_nPageNo(),oZenithHelper.getM_nPageSize());
	}

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


	@RequestMapping(value = "/academicyearupdate",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody	
	public GenericResponse update(@RequestBody ZenithHelper oZenithHelper) throws Exception
	{		
		return null;
	}

	@Override
	public String getXML(AcademicYear oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse list(AcademicYear oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse update(AcademicYear oGenericData) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
