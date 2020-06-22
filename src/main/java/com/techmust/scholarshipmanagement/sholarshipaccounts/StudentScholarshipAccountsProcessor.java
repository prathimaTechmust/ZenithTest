package com.techmust.scholarshipmanagement.sholarshipaccounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.constants.Constants;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.ZenithHelper;
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus.ZenithScholarshipDetails;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.usermanagement.facilitator.FacilitatorInformationData;
import com.techmust.utils.AmazonSMS;
import com.techmust.utils.MailService;
import com.techmust.utils.Utils;

@Controller
public class StudentScholarshipAccountsProcessor extends GenericIDataProcessor<StudentScholarshipAccount>
{

	@SuppressWarnings("unused")
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
			oStudentInformationData.setM_nAcademicYearId(oStudentScholarshipAccount.getM_nAcademicYearId());
			oStudentInformationData = oStudentInformationData.getStudentDetails(oStudentInformationData);
			List<AcademicDetails> list = new ArrayList<AcademicDetails>(oStudentInformationData.getM_oAcademicDetails());
			if(list.size() > 0)
			{				
				oStudentScholarshipAccount.setM_oAcademicDetails(list.get(0));
				oAccountsDataResponse.m_bSuccess = oStudentScholarshipAccount.saveObject();
				oAccountsDataResponse.m_nStudentId = oStudentScholarshipAccount.getM_nStudentId();
			}
			
			if(oAccountsDataResponse.m_bSuccess  == true)
			{
				oZenithScholarshipDetails.setM_nStudentId(oStudentScholarshipAccount.getM_nStudentId());
				oZenithScholarshipDetails.setM_nAcademicYearId(oStudentScholarshipAccount.getM_nAcademicYearId());
				oZenithScholarshipDetails.setM_oUserUpdatedBy(oStudentScholarshipAccount.getM_oUserUpdatedBy());
				boolean m_bSuccess = oZenithScholarshipDetails.applicationStatusUpdate(oZenithScholarshipDetails);
				oZenithScholarshipDetails.setM_strStudentName(oStudentInformationData.getM_strStudentName());
				oZenithScholarshipDetails.setM_nStudentUID(oStudentInformationData.getM_nUID());
				Utils.createActivityLog("StudentScholarshipAccountsProcessor::applicationStatusUpdate", oZenithScholarshipDetails);
			}			
		}
		catch (Exception oException)
		{
			m_oLogger.error("create - oException" +oException);
		}
		return oAccountsDataResponse;
	}	
	
	@RequestMapping(value = "/sendSMSAndMailNotification",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse sendSMSAndMail(@RequestBody StudentScholarshipAccount oStudentScholarshipAccount)
	{
		m_oLogger.info("sendSMSAndMail");
		m_oLogger.debug("sendSMSAndMail");
		StudentScholarshipAccountsDataResponse oAccountsDataResponse = new StudentScholarshipAccountsDataResponse();
		try
		{
			StudentInformationData oStudentInformationData = new StudentInformationData();
			oStudentInformationData.setM_nStudentId(oStudentScholarshipAccount.getM_nStudentId());
			oStudentInformationData = oStudentScholarshipAccount.getStudentDetails(oStudentInformationData);
			oAccountsDataResponse.m_bSuccess = sendSMSAndEmail(oStudentInformationData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("sendSMSAndMail - oException" +oException);
		}
		return oAccountsDataResponse;		
	}
	
	private boolean sendSMSAndEmail(StudentInformationData oStudentInformationData)
	{
		m_oLogger.info("sendSMSAndEmail");
		m_oLogger.debug("sendSMSAndEmail - oStudentInformationData [IN] :"+oStudentInformationData.toString());
		boolean m_bIsSentSMSAndMail = false;
		MailService m_oMailService = new MailService();
		FacilitatorInformationData oFacilitatorInformationData = oStudentInformationData.getM_oFacilitatorInformationData();		
		try 
		{
			if(oStudentInformationData.getM_strPhoneNumber() != null)
			{				
				AmazonSMS.sendSMSToStudent(Constants.NUMBERPREFIX+oStudentInformationData.getM_strPhoneNumber(),oStudentInformationData.getM_strStudentName());			
				m_bIsSentSMSAndMail = true;
			}			
			if(oFacilitatorInformationData.getM_strPhoneNumber() != null)
			{
				AmazonSMS.sendSMSToFacilitator(Constants.NUMBERPREFIX+oFacilitatorInformationData.getM_strPhoneNumber(), oFacilitatorInformationData.getM_strFacilitatorName(),oStudentInformationData);
				m_bIsSentSMSAndMail = true;
			}
			if(oStudentInformationData.getM_strEmailAddress() != null)
			{				
				m_oMailService.sendMailToStudent(oStudentInformationData.getM_strEmailAddress(),oStudentInformationData.getM_strStudentName());
				m_bIsSentSMSAndMail = true;
			}			
			if(oFacilitatorInformationData.getM_strEmail() != null)
			{
				m_oMailService.sendMailToFacilitator(oFacilitatorInformationData.getM_strEmail(), oFacilitatorInformationData.getM_strFacilitatorName(),oStudentInformationData);
				m_bIsSentSMSAndMail = true;
			}			
			
		}
		catch (Exception oException) 
		{
			m_oLogger.error("sendSMSAndMail - oException" +oException);
		}			
		return m_bIsSentSMSAndMail;
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
	
	@RequestMapping(value="/studentAccountlist", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strSortColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oStudentScholarshipAccount(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	public GenericResponse deleteData(StudentScholarshipAccount oGenericData) throws Exception
	{
		return null;
	}

	@Override
	public GenericResponse get(StudentScholarshipAccount oGenericData) throws Exception 
	{
		return null;
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
}
