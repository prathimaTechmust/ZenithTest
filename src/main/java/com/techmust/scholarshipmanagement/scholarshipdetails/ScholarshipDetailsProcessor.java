package com.techmust.scholarshipmanagement.scholarshipdetails;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

@Controller
public class ScholarshipDetailsProcessor extends GenericIDataProcessor <ScholarshipDetails>
{	

	@Override
	@RequestMapping(value = "/orgInfoDelete",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody ScholarshipDetails oScholarshipDetails) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oScholarshipDetails [IN] : " + oScholarshipDetails.getM_strOrganizationName());
		ScholarshipDetailsResponse oScholarshipDetailsResponse = new ScholarshipDetailsResponse();
		try
		{
			oScholarshipDetails = (ScholarshipDetails) populateObject (oScholarshipDetails);
			oScholarshipDetailsResponse.m_bSuccess = oScholarshipDetails.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		throw oException;
		}
		return oScholarshipDetailsResponse;
	}

	@Override
	public GenericResponse create(ScholarshipDetails oGenericData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(ScholarshipDetails oGenericData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse list(ScholarshipDetails oGenericData, HashMap<String, String> arrOrderBy) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse update(ScholarshipDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXML(ScholarshipDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

}
