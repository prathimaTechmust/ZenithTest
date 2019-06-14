package com.techmust.applicationstatus.verified;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.ZenithHelper;
import com.techmust.scholarshipmanagement.student.StudentInformationData;

public class VerifiedStudentInformationDataProcessor extends GenericIDataProcessor<VerifiedStudents>
{

	
	@RequestMapping(value = "/studentInfoVerified",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse verifiedStudents(@RequestBody VerifiedStudents oVerifiedStudents) throws Exception
	{
		m_oLogger.info ("verifying");
		m_oLogger.debug ("verifying - oVerifiedStudentsData [IN] : " + oVerifiedStudents);
		VerifiedStudentDataResponse oVerifiedStudentDataResponse = new VerifiedStudentDataResponse();
		try 
		{
			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("verifying - oException : " + oException);
			throw oException;
		}
		return oVerifiedStudentDataResponse;		
	}
	
	@Override
	public GenericResponse create(VerifiedStudents oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse deleteData(VerifiedStudents oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(VerifiedStudents oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*@RequestMapping(value="/studentVerifiedInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody	
	public GenericResponse getToBeStudentsVerified(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info ("ToBeverifiedstudentlist");
		m_oLogger.debug ("ToBeverifiedstudentlist - oStudentInformationData [IN] : " + oStudentInformationData);
		VerifiedStudentDataResponse oVerifiedStudentDataResponse = new VerifiedStudentDataResponse();
		try 
		{
			oVerifiedStudentDataResponse.m_arrToBeVerifiedStudentInformationData = new ArrayList (oStudentInformationData.getToBeVerifiedStudents(oStudentInformationData));
			oVerifiedStudentDataResponse.m_nRowCount = oVerifiedStudentDataResponse.m_arrToBeVerifiedStudentInformationData.size();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("ToBeverifiedstudentlist - oException : " +oException);
		}
		return oVerifiedStudentDataResponse;		
	}	*/

	@Override
	public GenericResponse update(VerifiedStudents oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXML(VerifiedStudents oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse list(VerifiedStudents oGenericData, HashMap<String, String> arrOrderBy) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

}
