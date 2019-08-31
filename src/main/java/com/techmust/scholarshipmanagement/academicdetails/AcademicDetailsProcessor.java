package com.techmust.scholarshipmanagement.academicdetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.techmust.scholarshipmanagement.student.StudentDataResponse;
import com.techmust.scholarshipmanagement.studentdocuments.StudentDocuments;
import com.techmust.utils.AWSUtils;
import com.techmust.utils.Utils;

@Controller
public class AcademicDetailsProcessor extends GenericIDataProcessor<AcademicDetails>
{	
	

	public static GenericResponse uploadStudentDocumentstoS3bucket(MultipartFile oStudentAadharMultipartFile,
																   MultipartFile oStudentElectricityBillMultipartFile,
																   MultipartFile oFatherMultipartFile,
																   MultipartFile oMotherMultipartFile,
																   MultipartFile oStudentMarksCard1MultipartFile,
																   MultipartFile oStudentMarksCard2MultipartFile,
																   MultipartFile oOtherDocumentsMultipartFile, int nAcademicId) throws Exception
    {
		AcademicDetails oAcademicDetails = new AcademicDetails();
		oAcademicDetails.setM_nAcademicId(nAcademicId);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();			
		try 
		{			
			if(oStudentAadharMultipartFile.getSize() > 0 && oStudentAadharMultipartFile != null)
			{				
				oStudentDataResponse.m_bSuccess = uploadStudentAadhar(oAcademicDetails,oStudentAadharMultipartFile);
				
			}
			if(oStudentElectricityBillMultipartFile.getSize() > 0 && oStudentElectricityBillMultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadStudentElectricityBill(oAcademicDetails,oStudentElectricityBillMultipartFile);				
			}
			if(oFatherMultipartFile.getSize() > 0 && oFatherMultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadStudentFatherAadhar(oAcademicDetails,oFatherMultipartFile);				
			}
			if(oMotherMultipartFile.getSize() > 0 && oMotherMultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadStudentMotherAadhar(oAcademicDetails,oMotherMultipartFile);				
			}
			if(oStudentMarksCard1MultipartFile.getSize() > 0 && oStudentMarksCard1MultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadStudentMarksCard1(oAcademicDetails,oStudentMarksCard1MultipartFile);				
			}			
			if(oStudentMarksCard2MultipartFile.getSize() > 0 && oStudentMarksCard2MultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadStudentMarksCard2(oAcademicDetails,oStudentMarksCard2MultipartFile);				
			}
			if(oOtherDocumentsMultipartFile.getSize() > 0 && oOtherDocumentsMultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadOtherDocuments(oAcademicDetails,oOtherDocumentsMultipartFile);				
			}
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("Upload Documents - oException : "  +oException);
		}        		
		return oStudentDataResponse; 
    }	
	
	private static boolean uploadOtherDocuments(AcademicDetails oAcademicDetails,MultipartFile oOtherDocumentsMultipartFile)
	{
		boolean bIsOtherDocumentsUpload = false;
		StudentDocuments oStudentDocuments = new StudentDocuments();
		List<StudentDocuments> m_arrDocuments = new ArrayList<StudentDocuments> ();
		try 
		{
			oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
			String strUUID = Utils.getUUID();
			String strFileName = Constants.STUDENTOTHERDOCUMENTS + strUUID + Constants.PDF_DEFAULT_EXTENSION;
			AWSUtils.UploadToStudentOtherDocumentsFolder(strFileName, oOtherDocumentsMultipartFile);
			if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
				oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
			oStudentDocuments.setM_strOtherDocuments(strUUID);
			m_arrDocuments.add(oStudentDocuments);
			oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
			bIsOtherDocumentsUpload = oAcademicDetails.updateObject();
		} 
		catch (Exception oException) 
		{
			m_oLogger.debug("uploadOtherDocuments - oException"+oException);
		}
		return bIsOtherDocumentsUpload;
	}

	private static boolean uploadStudentMarksCard2(AcademicDetails oAcademicDetails,MultipartFile oStudentMarksCard2MultipartFile)
	{
		boolean bIsMarksCard2Upload = false;
		StudentDocuments oStudentDocuments = new StudentDocuments();
		List<StudentDocuments> m_arrDocuments = new ArrayList<StudentDocuments> ();
		try 
		{
			oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
			String strUUID = Utils.getUUID();
			String strFileName = Constants.STUDENTMARKSCARD2 + strUUID + Constants.IMAGE_DEFAULT_EXTENSION;
			AWSUtils.UploadToStudentMarksCard2DocumentsFolder(strFileName, oStudentMarksCard2MultipartFile);
			if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
				oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
			oStudentDocuments.setM_strStudentMarksCard2(strUUID);
			m_arrDocuments.add(oStudentDocuments);
			oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
			bIsMarksCard2Upload = oAcademicDetails.updateObject();
			
		} catch (Exception oException)
		{
			m_oLogger.debug("uploadStudentMarksCard2 - oException"+oException);
		}
		return bIsMarksCard2Upload;
	}

	private static boolean uploadStudentMarksCard1(AcademicDetails oAcademicDetails,MultipartFile oStudentMarksCard1MultipartFile)
	{
		boolean bIsMarksCard1Upload = false;
		StudentDocuments oStudentDocuments = new StudentDocuments();
		List<StudentDocuments> m_arrDocuments = new ArrayList<StudentDocuments> ();
		try
		{
			oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
			String strUUID = Utils.getUUID();
			String strFileName = Constants.STUDENTMARKSCARD1 + strUUID + Constants.IMAGE_DEFAULT_EXTENSION;
			AWSUtils.UploadToStudentMarksCard1DocumentsFolder(strFileName, oStudentMarksCard1MultipartFile);
			if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
				oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
			oStudentDocuments.setM_strStudentMarksCard1(strUUID);
			m_arrDocuments.add(oStudentDocuments);
			oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
			bIsMarksCard1Upload = oAcademicDetails.updateObject();
		} catch (Exception oException)
		{
			m_oLogger.debug("uploadStudentMarksCard1 - oException"+oException);
		}
		return bIsMarksCard1Upload;
	}

	private static boolean uploadStudentMotherAadhar(AcademicDetails oAcademicDetails,MultipartFile oMotherMultipartFile) 
	{
		boolean bIsMotherAadharUpload = false;
		StudentDocuments oStudentDocuments = new StudentDocuments();
		List<StudentDocuments> m_arrDocuments = new ArrayList<StudentDocuments> ();
		try
		{
			oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
			String strUUID = Utils.getUUID();
			String strFileName = Constants.STUDENTMOTHERAADHAR + strUUID + Constants.IMAGE_DEFAULT_EXTENSION;
			AWSUtils.UploadToStudentMotherAadharDocumentsFolder(strFileName, oMotherMultipartFile);
			if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
				oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
			oStudentDocuments.setM_strMotherAadharImageId(strUUID);
			m_arrDocuments.add(oStudentDocuments);
			oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
			bIsMotherAadharUpload = oAcademicDetails.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.debug("uploadStudentMotherAadhar - oException"+oException);
		}
		return bIsMotherAadharUpload;
	}

	private static boolean uploadStudentFatherAadhar(AcademicDetails oAcademicDetails,MultipartFile oFatherMultipartFile)
	{
		boolean bIsFatherAadharUpload = false;
		StudentDocuments oStudentDocuments = new StudentDocuments();
		List<StudentDocuments> m_arrDocuments = new ArrayList<StudentDocuments> ();
		try
		{
			oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
			String strUUID = Utils.getUUID();
			String strFileName = Constants.STUDENTFATHERAADHAR + strUUID + Constants.IMAGE_DEFAULT_EXTENSION;
			AWSUtils.UploadToStudentFatherAadharDocumentsFolder(strFileName, oFatherMultipartFile);
			if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
				oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
			oStudentDocuments.setM_strFatherAadharImageId(strUUID);
			m_arrDocuments.add(oStudentDocuments);
			oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
			bIsFatherAadharUpload = oAcademicDetails.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error("uploadStudentFatherAadhar -oException"+oException);
		}
		return bIsFatherAadharUpload;
	}

	private static boolean uploadStudentElectricityBill(AcademicDetails oAcademicDetails,MultipartFile oStudentElectricityBillMultipartFile)
	{
		boolean bIsElectricityBillUpload = false;
		StudentDocuments oStudentDocuments = new StudentDocuments();
		List<StudentDocuments> m_arrDocuments = new ArrayList<StudentDocuments> ();
		try
		{
			oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
			String strUUID = Utils.getUUID();
			String strFileName = Constants.STUDENTELECTRICITYBILLDOCUMENTFOLDER + strUUID + Constants.IMAGE_DEFAULT_EXTENSION;
			AWSUtils.UploadToStudentElectricityDocumentsFolder(strFileName, oStudentElectricityBillMultipartFile);
			if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
				oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);				
			oStudentDocuments.setM_strStudentElectricityBill(strUUID);
			m_arrDocuments.add(oStudentDocuments);
			oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
			bIsElectricityBillUpload = oAcademicDetails.updateObject();
		} 
		catch (Exception oException)
		{
			m_oLogger.debug("uploadStudentElectricityBill - oException"+oException);
		}
		return bIsElectricityBillUpload;
	}

	private static boolean uploadStudentAadhar(AcademicDetails oAcademicDetails, MultipartFile oStudentAadharMultipartFile)
	{
		boolean bIsStudentAadharUpload = false;
		StudentDocuments oStudentDocuments = new StudentDocuments();
		List<StudentDocuments> m_arrDocuments = new ArrayList<StudentDocuments> ();	
		try 
		{
			oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
			String strUUID = Utils.getUUID();
			String strFileName = Constants.STUDENTAADHARDOCUMENTFOLDER + strUUID + Constants.IMAGE_DEFAULT_EXTENSION;
			AWSUtils.UploadToStudentAadharDocumentsFolder(strFileName, oStudentAadharMultipartFile);
			if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
				oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
			oStudentDocuments.setM_strStudentAadhar(strUUID);
			m_arrDocuments.add(oStudentDocuments);
			oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
			bIsStudentAadharUpload = oAcademicDetails.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error("uploadStudentAadhar - oException"+oException);
		}
		return bIsStudentAadharUpload;
	}

	@RequestMapping(value = "/getStudentUploadedDocuments",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getStudentUploadedDocuments(@RequestBody AcademicDetails oAcademicDetails)
	{
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		StudentDocuments oStudentDocuments = oAcademicDetails.getStudentUploadDocuments(oAcademicDetails);
		if(oStudentDocuments != null)
		{
			oStudentDataResponse.m_oStudentDocuments = Utils.getStudentDocuments(oStudentDocuments);
			oStudentDataResponse.m_bSuccess = true;
		}
		return oStudentDataResponse;		
	}
	
	@RequestMapping(value="/uploadStudentDocuments", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse uploadStudentNewDocumentstoS3bucket(@RequestParam(name = "studentaadhar",required = false) MultipartFile oStudentAadharMultipartFile,
															@RequestParam(name = "studentelectricitybill",required = false) MultipartFile oStudentElectricityBillMultipartFile,
															@RequestParam(name = "fatheraadhar",required = false) MultipartFile oFatherMultipartFile,
															@RequestParam(name = "motheraadhar",required = false) MultipartFile oMotherMultipartFile,
															@RequestParam(name = "studentmarkscard1",required = false) MultipartFile oStudentMarksCard1MultipartFile,
															@RequestParam(name = "studentmarkscard2",required = false) MultipartFile oStudentMarksCard2MultipartFile,
															@RequestParam(name = "otherdocuments",required = false) MultipartFile oOtherDocumentsMultipartFile,
															@RequestParam("academicId") int nAcademicId) throws Exception
    {
		AcademicDetails oAcademicDetails = new AcademicDetails();
		oAcademicDetails.setM_nAcademicId(nAcademicId);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();			
		try 
		{			
			if(oStudentAadharMultipartFile != null)
			{				
				oStudentDataResponse.m_bSuccess = uploadStudentAadhar(oAcademicDetails,oStudentAadharMultipartFile);				
			}
			if(oStudentElectricityBillMultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadStudentElectricityBill(oAcademicDetails,oStudentElectricityBillMultipartFile);				
			}
			if(oFatherMultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadStudentFatherAadhar(oAcademicDetails,oFatherMultipartFile);				
			}
			if(oMotherMultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadStudentMotherAadhar(oAcademicDetails,oMotherMultipartFile);				
			}
			if(oStudentMarksCard1MultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadStudentMarksCard1(oAcademicDetails,oStudentMarksCard1MultipartFile);				
			}			
			if(oStudentMarksCard2MultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadStudentMarksCard2(oAcademicDetails,oStudentMarksCard2MultipartFile);				
			}
			if(oOtherDocumentsMultipartFile != null)
			{
				oStudentDataResponse.m_bSuccess = uploadOtherDocuments(oAcademicDetails,oOtherDocumentsMultipartFile);				
			}
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("uploadStudentNewDocumentstoS3bucket - oException : "  +oException);
		}        		
		return oStudentDataResponse;		
    }
	
	@Override
	public GenericResponse create(AcademicDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse deleteData(AcademicDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(AcademicDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse list(AcademicDetails oGenericData, HashMap<String, String> arrOrderBy) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse update(AcademicDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXML(AcademicDetails oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}	

}
