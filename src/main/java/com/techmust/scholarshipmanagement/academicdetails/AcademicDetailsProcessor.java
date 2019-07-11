package com.techmust.scholarshipmanagement.academicdetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
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
	
	@RequestMapping(value="/uploadStudentDocuments", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse uploadStudentDocumentstoS3bucket(@RequestParam(name = "studentaadhar",required = false) MultipartFile oStudentAadharMultipartFile,
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
		List<StudentDocuments> m_arrDocuments = new ArrayList<StudentDocuments> ();
		StudentDocuments oStudentDocuments = new StudentDocuments();
		try 
		{
			
			String strUUID = "";
			String strExtension = Constants.IMAGE_DEFAULT_EXTENSION;			
			if(oStudentAadharMultipartFile != null)
			{
				oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
				strUUID = Utils.getUUID();
				String strFileName = Constants.STUDENTAADHARDOCUMENTFOLDER + strUUID + strExtension;
				AWSUtils.UploadToStudentAadharDocumentsFolder(strFileName, oStudentAadharMultipartFile);
				if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
					oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
				oStudentDocuments.setM_strStudentAadhar(strUUID);
				m_arrDocuments.add(oStudentDocuments);
				oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
				oStudentDataResponse.m_bSuccess = oAcademicDetails.updateObject();
			}
			if(oStudentElectricityBillMultipartFile != null)
			{
				oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
				strUUID = Utils.getUUID();
				String strFileName = Constants.STUDENTELECTRICITYBILLDOCUMENTFOLDER + strUUID + strExtension;
				AWSUtils.UploadToStudentElectricityDocumentsFolder(strFileName, oStudentElectricityBillMultipartFile);
				if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
					oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);				
				oStudentDocuments.setM_strStudentElectricityBill(strUUID);
				m_arrDocuments.add(oStudentDocuments);
				oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
				oStudentDataResponse.m_bSuccess = oAcademicDetails.updateObject();
			}
			if(oFatherMultipartFile != null)
			{
				oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
				strUUID = Utils.getUUID();
				String strFileName = Constants.STUDENTFATHERAADHAR + strUUID + strExtension;
				AWSUtils.UploadToStudentFatherAadharDocumentsFolder(strFileName, oFatherMultipartFile);
				if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
					oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
				oStudentDocuments.setM_strFatherAadharImageId(strUUID);
				m_arrDocuments.add(oStudentDocuments);
				oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
				oStudentDataResponse.m_bSuccess = oAcademicDetails.updateObject();
			}
			if(oMotherMultipartFile != null)
			{
				oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
				strUUID = Utils.getUUID();
				String strFileName = Constants.STUDENTMOTHERAADHAR + strUUID + strExtension;
				AWSUtils.UploadToStudentMotherAadharDocumentsFolder(strFileName, oMotherMultipartFile);
				if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
					oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
				oStudentDocuments.setM_strMotherAadharImageId(strUUID);
				m_arrDocuments.add(oStudentDocuments);
				oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
				oStudentDataResponse.m_bSuccess = oAcademicDetails.updateObject();
			}
			if(oStudentMarksCard1MultipartFile != null)
			{
				oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
				strUUID = Utils.getUUID();
				String strFileName = Constants.STUDENTMARKSCARD1 + strUUID + strExtension;
				AWSUtils.UploadToStudentMarksCard1DocumentsFolder(strFileName, oStudentMarksCard1MultipartFile);
				if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
					oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
				oStudentDocuments.setM_strStudentMarksCard1(strUUID);
				m_arrDocuments.add(oStudentDocuments);
				oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
				oStudentDataResponse.m_bSuccess = oAcademicDetails.updateObject();
			}			
			if(oStudentMarksCard2MultipartFile != null)
			{
				oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
				strUUID = Utils.getUUID();
				String strFileName = Constants.STUDENTMARKSCARD2 + strUUID + strExtension;
				AWSUtils.UploadToStudentMarksCard2DocumentsFolder(strFileName, oStudentMarksCard2MultipartFile);
				if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
					oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
				oStudentDocuments.setM_strStudentMarksCard2(strUUID);
				m_arrDocuments.add(oStudentDocuments);
				oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
				oStudentDataResponse.m_bSuccess = oAcademicDetails.updateObject();
			}
			if(oOtherDocumentsMultipartFile != null)
			{
				oAcademicDetails = (AcademicDetails) populateObject(oAcademicDetails);
				strUUID = Utils.getUUID();
				String strFileName = Constants.STUDENTOTHERDOCUMENTS + strUUID + strExtension;
				AWSUtils.UploadToStudentOtherDocumentsFolder(strFileName, oOtherDocumentsMultipartFile);
				if(oAcademicDetails.getM_arrStudentDocuments().size() > 0)
					oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
				oStudentDocuments.setM_strOtherDocuments(strUUID);
				m_arrDocuments.add(oStudentDocuments);
				oAcademicDetails.setM_arrStudentDocuments(m_arrDocuments);
				oStudentDataResponse.m_bSuccess = oAcademicDetails.updateObject();
			}
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("Upload Documents - oException : "  +oException);
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
