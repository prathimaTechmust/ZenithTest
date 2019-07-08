package com.techmust.scholarshipmanagement.sholarshipaccounts;

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
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus.ZenithScholarshipDetails;
import com.techmust.scholarshipmanagement.student.StudentInformationData;

@Controller
public class StudentScholarshipAccountsProcessor extends GenericIDataProcessor<StudentScholarshipAccount>
{

	@RequestMapping(value = "/studentAccountDataCreate",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	@Override
	public GenericResponse create(@RequestBody StudentScholarshipAccount oStudentScholarshipAccount) throws Exception
	{
		m_oLogger.info("create");
		m_oLogger.debug("create");
		StudentScholarshipAccountsDataResponse oAccountsDataResponse = new StudentScholarshipAccountsDataResponse();
		StudentInformationData oStudentInformationData = new StudentInformationData();
		ZenithScholarshipDetails oZenithScholarshipDetails = new ZenithScholarshipDetails();
		try 
		{			
			oStudentInformationData.setM_nStudentId(oStudentScholarshipAccount.getM_nStudentId());
			oStudentInformationData.setM_strAcademicYear(oStudentScholarshipAccount.getM_strAcademicYear());
			oStudentInformationData = oStudentInformationData.getStudentDetails(oStudentInformationData);
			List<AcademicDetails> list = new ArrayList<AcademicDetails>(oStudentInformationData.getM_oAcademicDetails());		
			oStudentScholarshipAccount.setM_oAcademicDetails(list.get(0));
			oAccountsDataResponse.m_bSuccess = oStudentScholarshipAccount.saveObject();
			if(oAccountsDataResponse.m_bSuccess  == true)
			{
				oZenithScholarshipDetails.setM_nStudentId(oStudentScholarshipAccount.getM_nStudentId());
				boolean m_bSuccess = oZenithScholarshipDetails.applicationStatusUpdate(oZenithScholarshipDetails);
			}			
		}
		catch (Exception oException)
		{
			m_oLogger.error("create - oException" +oException);
		}
		return oAccountsDataResponse;
	}

	@Override
	public GenericResponse deleteData(StudentScholarshipAccount oGenericData) throws Exception
	{
		return null;
	}

	@Override
	public GenericResponse get(StudentScholarshipAccount oGenericData) throws Exception 
	{
		return null;
	}
	
	@RequestMapping(value="/studentAccountlist", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oStudentScholarshipAccount(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
	private GenericResponse list(StudentScholarshipAccount oStudentScholarshipAccount, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - StudentScholarshipAccount [IN] : " + oStudentScholarshipAccount);
		StudentScholarshipAccountsDataResponse oStudentDataResponse = new StudentScholarshipAccountsDataResponse();
		try 
		{
			oStudentDataResponse.m_nRowCount = getRowCount(oStudentScholarshipAccount);
			oStudentDataResponse.m_arrStudentAccountInformationData = new ArrayList (oStudentScholarshipAccount.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oStudentDataResponse;
	}

	@Override
	public GenericResponse list(StudentScholarshipAccount oGenericData, HashMap<String, String> arrOrderBy)throws Exception
	{
		return null;
	}

	@Override
	public GenericResponse update(StudentScholarshipAccount oGenericData) throws Exception
	{
		return null;
	}

	@Override
	@RequestMapping(value = "/studentAccountXML",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody StudentScholarshipAccount oStudentScholarshipAccount) throws Exception
	{
		m_oLogger.info("generateXMLData");
		m_oLogger.debug("generateXMLData");
		String strXMLData = "";
		try 
		{
			oStudentScholarshipAccount = (StudentScholarshipAccount) populateObject(oStudentScholarshipAccount);
			strXMLData = oStudentScholarshipAccount != null ? oStudentScholarshipAccount.generateXML() : "";
		} 
		catch (Exception oException)
		{
			m_oLogger.error("generateXMLData - oException"+oException);
			throw oException;
		}
		return strXMLData;
	}
}