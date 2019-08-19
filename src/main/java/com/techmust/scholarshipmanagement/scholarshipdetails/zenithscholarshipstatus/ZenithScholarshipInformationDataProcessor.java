package com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import com.techmust.scholarshipmanagement.chequeFavourOf.ChequeInFavourOf;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.utils.AWSUtils;
import com.techmust.utils.AmazonSMS;
import com.techmust.utils.Utils;

@Controller
public class ZenithScholarshipInformationDataProcessor extends GenericIDataProcessor<ZenithScholarshipDetails>
{	

	@RequestMapping(value="/studentStatusInfoUpdate", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse toBeVerifiedStatusUpdate(@RequestParam(name = "scancopy",required = false)MultipartFile oScanCopyMultipartFile,@RequestParam("studentId") int nStudentId,@RequestParam("chequefavourId") int nFavourid) throws Exception
	{
		
		m_oLogger.info ("toBeVerifiedStatusUpdate");
		m_oLogger.debug ("toBeVerifiedStatusUpdate - ScanCopy [IN] : " + oScanCopyMultipartFile);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		ZenithScholarshipDetails oZenithScholarshipDetails = new ZenithScholarshipDetails();
		ChequeInFavourOf oChequeInFavourOf = new ChequeInFavourOf();
		oChequeInFavourOf.setM_nChequeFavourId(nFavourid);
		oZenithScholarshipDetails.setM_nStudentId(nStudentId);
		oZenithScholarshipDetails.setM_oChequeInFavourOf(oChequeInFavourOf);
		try
		{	
			String strUUID = Utils.getUUID();
			String strFileName = Constants.VERIFIEDAPPLICATION + strUUID + Constants.IMAGE_DEFAULT_EXTENSION;
			AWSUtils.UploadSealedCopyDocumentsFolder(strFileName,oScanCopyMultipartFile);
			oZenithScholarshipDetails.setM_strImage(strUUID);
			oZenithScholarshipDetailsDataResponse.m_bSuccess =  oZenithScholarshipDetails.updateStudentApplicationVerifiedStatus(oZenithScholarshipDetails);
			if(oZenithScholarshipDetailsDataResponse.m_bSuccess)			
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::toBeVerifiedStatusUpdate", oZenithScholarshipDetails);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("toBeVerifiedStatusUpdate - oException : " + oException);
			throw oException;
		}
		return oZenithScholarshipDetailsDataResponse;
	}
	
	@RequestMapping(value="/studentApprovedStatusInfoUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse approvedStatusUpdate( @RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		
		m_oLogger.info ("applicationApprovedStatusUpdate");
		m_oLogger.debug ("applicationApprovedStatusUpdate - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		try
		{			
			oZenithScholarshipDetailsDataResponse.m_bSuccess =  oZenithScholarshipDetails.updateStudentApplicationApprovedStatus(oZenithScholarshipDetails);
			if(oZenithScholarshipDetailsDataResponse.m_bSuccess)		
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::approvedStatusUpdate", oZenithScholarshipDetails);	
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
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		try 
		{
			oZenithScholarshipDetailsDataResponse.m_bSuccess = oZenithScholarshipDetails.updateStudentApplicationRejectedStatus(oZenithScholarshipDetails);
			if(oZenithScholarshipDetailsDataResponse.m_bSuccess)		
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::rejectedStatusUpdate", oZenithScholarshipDetails);			
		} 
		catch (Exception oException)
		{
			m_oLogger.error("applicationRejectedStatus- oException"+oException);
			throw oException;
		}				
		return oZenithScholarshipDetailsDataResponse;		
	}
	
	@RequestMapping(value="/studentIssueCheque",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse disburseCheque(@RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		m_oLogger.info("disburseCheque");
		m_oLogger.debug("disburseCheque - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		try 
		{
			oZenithScholarshipDetailsDataResponse.m_bSuccess = oZenithScholarshipDetails.disburseCheque(oZenithScholarshipDetails);
			if(oZenithScholarshipDetailsDataResponse.m_bSuccess)		
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::disburseCheque", oZenithScholarshipDetails);			
		} 
		catch (Exception oException)
		{
			m_oLogger.error("disburseCheque- oException"+oException);
			throw oException;
		}
				
		return oZenithScholarshipDetailsDataResponse;		
	}
	
	@RequestMapping(value="/reverifyapplication",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse reVerifyStudentApplication(@RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		m_oLogger.info("reVerfyStudentApplication");
		m_oLogger.debug("reVerfyStudentApplication - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		try 
		{
	    oZenithScholarshipDetailsDataResponse.m_bSuccess = oZenithScholarshipDetails.reVerifyStudentApplication(oZenithScholarshipDetails);
	    StudentInformationData	oStudentInformationData = new StudentInformationData();
	    oStudentInformationData.setM_nStudentId(oZenithScholarshipDetails.getM_nStudentId());
	    oStudentInformationData= (StudentInformationData) populateObject(oStudentInformationData);
	    List<ZenithScholarshipDetails> oDetails = new ArrayList<ZenithScholarshipDetails>(oStudentInformationData.getM_oZenithScholarshipDetails());		;
	    AmazonSMS.sendSmsToCounselingCandidate(Constants.NUMBERPREFIX+oStudentInformationData.getM_strPhoneNumber(),oStudentInformationData.getM_strStudentName(),oDetails.get(0).getM_strStudentRemarks());	
			if(oZenithScholarshipDetailsDataResponse.m_bSuccess)
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::reVerifyStudentApplication", oZenithScholarshipDetails);			
		} 
		catch (Exception oException)
		{
			m_oLogger.error("reVerfyStudentApplication- oException"+oException);
			throw oException;
		}				
		return oZenithScholarshipDetailsDataResponse;
		
	}	
	
	@RequestMapping(value="/claimChequeUpdate",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse claimCheque(@RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		m_oLogger.info("claimCheque");
		m_oLogger.debug("claimCheque - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		try 
		{
			oZenithScholarshipDetailsDataResponse.m_bSuccess = oZenithScholarshipDetails.claimCheque(oZenithScholarshipDetails);
			if(oZenithScholarshipDetailsDataResponse.m_bSuccess)
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::claimCheque", oZenithScholarshipDetails);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("claimCheque - oException"+oException);
			throw oException;
		}				
		return oZenithScholarshipDetailsDataResponse;
		
	}
	
	@RequestMapping(value = "/reIssueCheque", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    @ResponseBody
	public GenericResponse reIssueCheque(@RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		m_oLogger.info("reIssueCheckDetails");
		m_oLogger.debug("reIssueCheckDetails -  ZenithScholarshipDetails [IN] : "  + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();		
		try 
		{
			oZenithScholarshipDetailsDataResponse.m_bSuccess = oZenithScholarshipDetails.reIssueCheckDetails(oZenithScholarshipDetails);
			if(oZenithScholarshipDetailsDataResponse.m_bSuccess)
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::reIssueCheque", oZenithScholarshipDetails);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("reIssueCheque - oException"+oException);
			throw oException;
		}				
		return oZenithScholarshipDetailsDataResponse; 
	}
	
	
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
