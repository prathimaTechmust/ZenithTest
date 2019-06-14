package com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

@Controller
public class ZenithScholarshipInformationDataProcessor extends GenericIDataProcessor<ZenithScholarshipDetails>
{

	@Override
	public GenericResponse create(ZenithScholarshipDetails oGenericData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse deleteData(ZenithScholarshipDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(ZenithScholarshipDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse list(ZenithScholarshipDetails oGenericData, HashMap<String, String> arrOrderBy)throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value="/studentStatusInfoUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update( @RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		
		m_oLogger.info ("applicationStatusUpdate");
		m_oLogger.debug ("applicationStatusUpdate - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		try
		{			
			oZenithScholarshipDetailsDataResponse.m_bSuccess =  oZenithScholarshipDetails.updateStudentApplicationVerifiedStatus(oZenithScholarshipDetails);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("applicationStatusUpdate - oException : " + oException);
			throw oException;
		}
		return oZenithScholarshipDetailsDataResponse;
	}
	
	@RequestMapping(value="/studentApprovedStatusInfoUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse approvedStatusupdate( @RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		
		m_oLogger.info ("applicationApprovedStatusUpdate");
		m_oLogger.debug ("applicationApprovedStatusUpdate - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		try
		{			
			oZenithScholarshipDetailsDataResponse.m_bSuccess =  oZenithScholarshipDetails.updateStudentApplicationApprovedStatus(oZenithScholarshipDetails);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("applicationApprovedStatusUpdate - oException : " + oException);
			throw oException;
		}
		return oZenithScholarshipDetailsDataResponse;
	}

	@Override
	public String getXML(ZenithScholarshipDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
}
