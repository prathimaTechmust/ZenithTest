package com.techmust.scholarshipmanagement.scholarshipdetails.scholarshiporganization;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

@Controller
public class ScholarshipOrganizationDetailsProcessor extends GenericIDataProcessor <ScholarshipOrganizationDetails>
{	

	@Override
	@RequestMapping(value = "/orgInfoDelete",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody ScholarshipOrganizationDetails oScholarshipDetails) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oScholarshipDetails [IN] : " + oScholarshipDetails.getM_strOrganizationName());
		ScholarshipOrganizationDetailsResponse oScholarshipDetailsResponse = new ScholarshipOrganizationDetailsResponse();
		try
		{
			oScholarshipDetails = (ScholarshipOrganizationDetails) populateObject (oScholarshipDetails);
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
	public GenericResponse create(ScholarshipOrganizationDetails oGenericData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(ScholarshipOrganizationDetails oGenericData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse list(ScholarshipOrganizationDetails oGenericData, HashMap<String, String> arrOrderBy) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse update(ScholarshipOrganizationDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXML(ScholarshipOrganizationDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

}
