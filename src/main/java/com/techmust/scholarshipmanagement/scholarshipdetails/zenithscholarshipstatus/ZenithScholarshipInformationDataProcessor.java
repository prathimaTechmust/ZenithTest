package com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
import com.techmust.generic.util.HibernateUtil;
import com.techmust.scholarshipmanagement.chequeFavourOf.ChequeInFavourOf;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.utils.AWSUtils;
import com.techmust.utils.AmazonSMS;
import com.techmust.utils.Utils;

@Controller
public class ZenithScholarshipInformationDataProcessor extends GenericIDataProcessor<ZenithScholarshipDetails>
{	

	@RequestMapping(value="/studentStatusInfoUpdate", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse toBeVerifiedStatusUpdate(@RequestParam(name = "scancopy",required = false)MultipartFile oScanCopyMultipartFile,
													@RequestParam("studentId") int nStudentId,
													@RequestParam(value="chequefavourId",required = false) Integer nFavourid,
													@RequestParam(value="strPaymentType",required = false) String strPaymentType,
													@RequestParam(value="strVerifyRemarks",required = false) String strVerifyRemarks,
													@RequestParam(name = "nLoginUserId",required = false) Integer nLoginUserId,
													@RequestParam(name = "nAcademicyearId",required = false)Integer nAcademicyearId) throws Exception
	{
		
		m_oLogger.info ("toBeVerifiedStatusUpdate");
		m_oLogger.debug ("toBeVerifiedStatusUpdate - ScanCopy [IN] : " + oScanCopyMultipartFile);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		ZenithScholarshipDetails oZenithScholarshipDetails = new ZenithScholarshipDetails();
		UserInformationData oUserInformationData = new UserInformationData();
		oUserInformationData.setM_nUserId(nLoginUserId);
		ChequeInFavourOf oChequeInFavourOf = new ChequeInFavourOf();
		if(nFavourid != null)
		{
			oChequeInFavourOf.setM_nChequeFavourId(nFavourid);
			oZenithScholarshipDetails.setM_oChequeInFavourOf(oChequeInFavourOf);
		}
		oZenithScholarshipDetails.setM_nAcademicYearId(nAcademicyearId);
		oZenithScholarshipDetails.setM_strPaymentType(strPaymentType);
		oZenithScholarshipDetails.setM_nStudentId(nStudentId);
		oZenithScholarshipDetails.setM_oUserUpdatedBy(oUserInformationData);
		oZenithScholarshipDetails.setM_strVerifyRemarks(strVerifyRemarks);
		try                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
		{	
			String strUUID = Utils.getUUID();
			String strFileName = Constants.VERIFIEDAPPLICATION + strUUID + Constants.IMAGE_DEFAULT_EXTENSION;
			AWSUtils.UploadSealedCopyDocumentsFolder(strFileName,oScanCopyMultipartFile);
			oZenithScholarshipDetails.setM_strImage(strUUID);
			oZenithScholarshipDetailsDataResponse.m_bSuccess =  oZenithScholarshipDetails.updateStudentApplicationVerifiedStatus(oZenithScholarshipDetails);
			if(oZenithScholarshipDetailsDataResponse.m_bSuccess)
			{
				ZenithScholarshipDetails oZenithScholarshipData = getStudentData(nStudentId,nAcademicyearId,oZenithScholarshipDetails);
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::toBeVerifiedStatusUpdate", oZenithScholarshipData);
			}
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
			{
				ZenithScholarshipDetails oScholarshipDetails = getStudentData(oZenithScholarshipDetails.getM_nStudentId(), oZenithScholarshipDetails.getM_nAcademicYearId(), oZenithScholarshipDetails);
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::approvedStatusUpdate", oScholarshipDetails);
			}
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
			{
				ZenithScholarshipDetails oScholarshipDetails = getStudentData(oZenithScholarshipDetails.getM_nStudentId(), oZenithScholarshipDetails.getM_nAcademicYearId(), oZenithScholarshipDetails);
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::rejectedStatusUpdate", oScholarshipDetails);
			}
							
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
			{
				ZenithScholarshipDetails oScholarshipDetails = getStudentData(oZenithScholarshipDetails.getM_nStudentId(), oZenithScholarshipDetails.getM_nAcademicYearId(), oZenithScholarshipDetails);
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::disburseCheque", oScholarshipDetails);
			}
							
		} 
		catch (Exception oException)
		{
			m_oLogger.error("disburseCheque- oException"+oException);
			throw oException;
		}				
		return oZenithScholarshipDetailsDataResponse;	
	}
	
	@RequestMapping(value="/counselingStatusUpdate",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse counselingStatusUpdate(@RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		m_oLogger.info("counselingStatusApplication");
		m_oLogger.debug("counselingStatusApplication - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		try 
		{
			oZenithScholarshipDetailsDataResponse.m_bSuccess = oZenithScholarshipDetails.counselingStatusUpdate(oZenithScholarshipDetails);
			if(oZenithScholarshipDetailsDataResponse.m_bSuccess) 
			{
				
				StudentInformationData	oStudentInformationData = new StudentInformationData();
			    oStudentInformationData.setM_nStudentId(oZenithScholarshipDetails.getM_nStudentId());
			    oStudentInformationData.setM_nAcademicYearId(oZenithScholarshipDetails.getM_nAcademicYearId());
			    StudentInformationData  oStudentData= oStudentInformationData.getStudentDetails(oStudentInformationData);
			    List<ZenithScholarshipDetails> oDetails = new ArrayList<ZenithScholarshipDetails>(oStudentData.getM_oZenithScholarshipDetails());
			    AmazonSMS.sendSmsToCounselingCandidate(oStudentData.getM_strStudentName(),Constants.NUMBERPREFIX+oStudentData.getM_strPhoneNumber(),oDetails.get(0).getM_dCounselingDate());	
			    ZenithScholarshipDetails oScholarshipDetails = getStudentData(oZenithScholarshipDetails.getM_nStudentId(), oZenithScholarshipDetails.getM_nAcademicYearId(), oZenithScholarshipDetails);
			    Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::counselingStatusApplication", oScholarshipDetails);			
			} 
		}
		catch (Exception oException)
		{
			m_oLogger.error("counselingStatusApplication- oException"+oException);
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
		    if(oZenithScholarshipDetailsDataResponse.m_bSuccess)
		    {
		    	ZenithScholarshipDetails oScholarshipDetails = getStudentData(oZenithScholarshipDetails.getM_nStudentId(), oZenithScholarshipDetails.getM_nAcademicYearId(), oZenithScholarshipDetails);
		    	Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::reVerifyStudentApplication", oScholarshipDetails);
		    }
							
		} 
		catch (Exception oException)
		{
			m_oLogger.error("reVerfyStudentApplication- oException"+oException);
			throw oException;
		}				
		return oZenithScholarshipDetailsDataResponse;	
	}	
	
	@RequestMapping(value="/approveCounselingStudent",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse approveCounselingStudentApplication(@RequestBody ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		m_oLogger.info("approveCounselingStudentApplication");
		m_oLogger.debug("approveCounselingStudentApplication - ZenithScholarshipDetails [IN] : " + oZenithScholarshipDetails);
		ZenithScholarshipDetailsDataResponse oZenithScholarshipDetailsDataResponse = new ZenithScholarshipDetailsDataResponse();
		try 
		{
		    oZenithScholarshipDetailsDataResponse.m_bSuccess = oZenithScholarshipDetails.approveCounselingStudentApplication(oZenithScholarshipDetails);
		    if(oZenithScholarshipDetailsDataResponse.m_bSuccess)
		    {
		    	ZenithScholarshipDetails oScholarshipDetails = getStudentData(oZenithScholarshipDetails.getM_nStudentId(), oZenithScholarshipDetails.getM_nAcademicYearId(), oZenithScholarshipDetails);
		    	Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::approveCounselingStudentApplication", oScholarshipDetails);
		    }
							
		} 
		catch (Exception oException)
		{
			m_oLogger.error("approveCounselingStudentApplication- oException"+oException);
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
			{
				ZenithScholarshipDetails oScholarshipDetails = getStudentData(oZenithScholarshipDetails.getM_nStudentId(), oZenithScholarshipDetails.getM_nAcademicYearId(), oZenithScholarshipDetails);
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::claimCheque", oScholarshipDetails);
			}
				
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
			{
				ZenithScholarshipDetails oScholarshipDetails = getStudentData(oZenithScholarshipDetails.getM_nStudentId(), oZenithScholarshipDetails.getM_nAcademicYearId(), oZenithScholarshipDetails);
				Utils.createActivityLog("ZenithScholarshipInformationDataProcessor::reIssueCheque", oScholarshipDetails);
			}
				
		} 
		catch (Exception oException)
		{
			m_oLogger.error("reIssueCheque - oException"+oException);
			throw oException;
		}				
		return oZenithScholarshipDetailsDataResponse; 
	}	
	
	private ZenithScholarshipDetails getStudentData(int nStudentId, Integer nAcademicyearId, ZenithScholarshipDetails oZenithScholarshipDetails)
	{
		EntityManager oEntityManager = oZenithScholarshipDetails._getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oRoot = oCriteriaQuery.from(StudentInformationData.class);
			oCriteriaQuery.select(oRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oRoot.get("m_nStudentId"), nStudentId));
			ArrayList<StudentInformationData> arrStudentListData = (ArrayList<StudentInformationData>) oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(arrStudentListData.size() > 0)
			{
				StudentInformationData oStudentData = arrStudentListData.get(0);
				oZenithScholarshipDetails.setM_nStudentUID(oStudentData.getM_nUID());
				oZenithScholarshipDetails.setM_strStudentName(oStudentData.getM_strStudentName());
				oZenithScholarshipDetails.setM_nAcademicYearId(nAcademicyearId);
			}
				
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("getStudentData - oException : " + oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oZenithScholarshipDetails;
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
