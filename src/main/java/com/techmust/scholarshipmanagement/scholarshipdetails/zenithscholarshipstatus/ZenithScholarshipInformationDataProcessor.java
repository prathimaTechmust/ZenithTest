package com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.techmust.constants.Constants;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.utils.AWSUtils;
import com.techmust.utils.Utils;

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

	@RequestMapping(value="/studentStatusInfoUpdate", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse update(@RequestParam(name = "scancopy",required = false)MultipartFile oScanCopyMultipartFile,@RequestParam("studentId") int nStudentId) throws Exception
	{
		
		m_oLogger.info ("applicationStatusUpdate");
		m_oLogger.debug ("applicationStatusUpdate - ScanCopy [IN] : " + oScanCopyMultipartFile);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		ZenithScholarshipDetails oZenithScholarshipDetails = new ZenithScholarshipDetails();
		oZenithScholarshipDetails.setM_nStudentId(nStudentId);
		try
		{	
			String strUUID = Utils.getUUID();
			String strFileName = Constants.VERIFIEDAPPLICATION + strUUID + Constants.IMAGE_DEFAULT_EXTENSION;
			AWSUtils.UploadSealedCopyDocumentsFolder(strFileName,oScanCopyMultipartFile);
			oZenithScholarshipDetails.setM_strImage(strUUID);
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
	
	@RequestMapping(value="/studentRejectedStatusUpdate",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse rejectedStatusUpdate(@RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		m_oLogger.info("applicationRejectedStatus");
		m_oLogger.debug("applicationRejectedStatus - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oDataResponse = new ZenithScholarshipDetailsDataResponse();
		try 
		{
			oDataResponse.m_bSuccess = oZenithScholarshipDetails.updateStudentApplicationRejectedStatus(oZenithScholarshipDetails);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("applicationRejectedStatus- oException"+oException);
			throw oException;
		}
				
		return oDataResponse;		
	}
	
	@RequestMapping(value="/studentIssueCheque",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse disburseCheque(@RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		m_oLogger.info("disburseCheque");
		m_oLogger.debug("disburseCheque - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oDataResponse = new ZenithScholarshipDetailsDataResponse();
		try 
		{
			oDataResponse.m_bSuccess = oZenithScholarshipDetails.disburseCheque(oZenithScholarshipDetails);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("disburseCheque- oException"+oException);
			throw oException;
		}
				
		return oDataResponse;		
	}
	
	@RequestMapping(value="/reverifyapplication",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse reVerifyStudentApplication(@RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		m_oLogger.info("reVerfyStudentApplication");
		m_oLogger.debug("reVerfyStudentApplication - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oDataResponse = new ZenithScholarshipDetailsDataResponse();
		try 
		{
			oDataResponse.m_bSuccess = oZenithScholarshipDetails.reVerifyStudentApplication(oZenithScholarshipDetails);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("reVerfyStudentApplication- oException"+oException);
			throw oException;
		}				
		return oDataResponse;
		
	}

	@Override
	public String getXML(ZenithScholarshipDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse update(ZenithScholarshipDetails oGenericData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
