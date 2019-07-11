package com.techmust.scholarshipmanagement.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.techmust.constants.Constants;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.ZenithHelper;
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.studentdocuments.StudentDocuments;
import com.techmust.utils.AWSUtils;
import com.techmust.utils.Utils;

@Controller
public class StudentInformationDataProcessor extends GenericIDataProcessor <StudentInformationData> 
{

	@Override
	@RequestMapping(value = "/studentInfoCreate",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody StudentInformationData oStudentInformationData) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oStudentInformationData [IN] : " + oStudentInformationData);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			oStudentDataResponse.m_bSuccess = oStudentInformationData.saveObject();
			oStudentDataResponse.m_arrStudentInformationData.add(oStudentInformationData);
			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		return oStudentDataResponse;
	}
	/*@RequestMapping(value = "/studentInfoCreate",method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse createStudentData(@RequestParam(name = "studentimage",required = false)MultipartFile oStudentMultipartFile,@RequestParam("studentObject") String oStudentData) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oStudentInformationData [IN] : " + oStudentData);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			StudentInformationData oStudentInformationData = new Gson().fromJson(oStudentData, StudentInformationData.class);
			oStudentDataResponse.m_bSuccess = oStudentInformationData.saveObject();
			oStudentDataResponse.m_arrStudentInformationData.add(oStudentInformationData);			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		return oStudentDataResponse;
		
	}*/

	@Override
	@RequestMapping(value = "/studentInfoDelete",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody StudentInformationData oStudentInformationData) throws Exception
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oStudentInformationData.getM_nStudentId() [IN] : " + oStudentInformationData.getM_nStudentId());
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			oStudentInformationData = (StudentInformationData) populateObject (oStudentInformationData);
			oStudentDataResponse.m_bSuccess = oStudentInformationData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		throw oException;
		}
		return oStudentDataResponse;
	}
	
	@RequestMapping(value="/createStudentImageData", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse createStudentImagetoS3bucket(@RequestParam(name = "studentimage",required = false) MultipartFile oStudentMultipartFile, @RequestParam("studentId") int nStudentId ) throws Exception
    {
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		StudentInformationData oStudentInformationData = new StudentInformationData();
		oStudentInformationData.setM_nStudentId(nStudentId);
		try 
		{	
			if(oStudentMultipartFile != null)
			{
				oStudentInformationData = (StudentInformationData) populateObject(oStudentInformationData);	
		        String strUUID = Utils.getUUID();
		        String strFileExtension = Constants.IMAGE_DEFAULT_EXTENSION;
				String strStudentImagePath = Constants.STUDENTIMAGEFOLDER + strUUID + strFileExtension;
			    AWSUtils.UploadToStudentImagesFolder(strStudentImagePath, oStudentMultipartFile);
			    oStudentInformationData.setM_strStudentImageId(strUUID);
			    oStudentDataResponse.m_bSuccess = oStudentInformationData.updateObject();
			}			
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("UploadstudentImage - oException : " + oException);
		}				
		return oStudentDataResponse; 
    }

	@Override
	@RequestMapping(value="/studentInfoGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody StudentInformationData oStudentInformationData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oStudentInformationData.getM_nStudentId() [IN] :" + oStudentInformationData.getM_nStudentId());
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try 

		{
			//oStudentInformationData = (StudentInformationData) populateObject (oStudentInformationData);
			oStudentInformationData = oStudentInformationData.getStudentDetails (oStudentInformationData);
			Set<AcademicDetails> m_setAcademicDetails = oStudentInformationData.getM_oAcademicDetails();
			List<AcademicDetails>m_arrAcademicDetails = new ArrayList<AcademicDetails>(m_setAcademicDetails);
			AcademicDetails oAcademicDetails = m_arrAcademicDetails.get(0);
			if(oAcademicDetails.getM_arrStudentDocuments().size() >0)
			{
				StudentDocuments oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
				oStudentDataResponse.m_oStudentDocuments = getStudentDocuments(oStudentDocuments);
			}
			oStudentDataResponse.m_arrStudentInformationData.add (oStudentInformationData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oStudentDataResponse;
	}
	
	private StudentDocuments getStudentDocuments(StudentDocuments oStudentDocuments)
	{
		 if(oStudentDocuments.getM_strStudentAadhar() != null)
		 {
			 String strStudentAadharURL = Constants.S3BUCKETURL + Constants.STUDENTAADHARDOCUMENTFOLDER + oStudentDocuments.getM_strStudentAadhar() + Constants.IMAGE_DEFAULT_EXTENSION;
 			 oStudentDocuments.setM_strStudentAadhar(strStudentAadharURL);
		 }
		 if(oStudentDocuments.getM_strFatherAadharImageId() != null)
		 {
			 String strStudentFatherAadharURL = Constants.S3BUCKETURL + Constants.STUDENTFATHERAADHAR + oStudentDocuments.getM_strFatherAadharImageId() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strFatherAadharImageId(strStudentFatherAadharURL);
		 }
		 if(oStudentDocuments.getM_strMotherAadharImageId() != null)
		 {
			 String strStudentMotherAadharURL = Constants.S3BUCKETURL + Constants.STUDENTMOTHERAADHAR + oStudentDocuments.getM_strMotherAadharImageId() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strMotherAadharImageId(strStudentMotherAadharURL);
		 }	
		 if(oStudentDocuments.getM_strStudentElectricityBill() != null)
		 {
			 String strStudentElectricityBillURL = Constants.S3BUCKETURL + Constants.STUDENTELECTRICITYBILLDOCUMENTFOLDER + oStudentDocuments.getM_strStudentElectricityBill() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strStudentElectricityBill(strStudentElectricityBillURL);
		 }			
		 if(oStudentDocuments.getM_strStudentMarksCard1() != null)
		 {
			 String strStudentMarkscard1URL = Constants.S3BUCKETURL + Constants.STUDENTMARKSCARD1 + oStudentDocuments.getM_strStudentMarksCard1() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strStudentMarksCard1(strStudentMarkscard1URL);
		 }
		 if(oStudentDocuments.getM_strStudentMarksCard2() != null)
		 {
			 String strStudentMarkscard2URL = Constants.S3BUCKETURL + Constants.STUDENTMARKSCARD2 + oStudentDocuments.getM_strStudentMarksCard2() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strStudentMarksCard2(strStudentMarkscard2URL);
		 }
		 if(oStudentDocuments.getM_strOtherDocuments() != null)
		 {
			 String strStudentOtherDocumentsURL = Constants.S3BUCKETURL + Constants.STUDENTOTHERDOCUMENTS + oStudentDocuments.getM_strOtherDocuments() + Constants.IMAGE_DEFAULT_EXTENSION;
			 oStudentDocuments.setM_strOtherDocuments(strStudentOtherDocumentsURL);
		 }			 
		return oStudentDocuments;
	}

	@RequestMapping(value="/studentInfoGetUIDData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getStudentUIDData(@RequestBody StudentInformationData oStudentInformationData) throws Exception
	{
		m_oLogger.info("getStudentUID");
		m_oLogger.debug("getStudentUID - oStudentInformationData [IN] :" + oStudentInformationData.getM_nUID());
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			oStudentInformationData = (StudentInformationData) populateObject (oStudentInformationData);
			oStudentDataResponse.m_arrStudentInformationData.add (oStudentInformationData);	
			if(oStudentInformationData != null)
				oStudentDataResponse.m_bSuccess = true;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getStudentUID - oException : "  + oException);
			throw oException;
		}
		return oStudentDataResponse;
	}

	@RequestMapping(value="/studentInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oStudentInformationData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
	private GenericResponse list(StudentInformationData oStudentInformationData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oStudentInformationData [IN] : " + oStudentInformationData);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try 
		{
			oStudentDataResponse.m_nRowCount = getRowCount(oStudentInformationData);
			oStudentDataResponse.m_arrStudentInformationData = new ArrayList (oStudentInformationData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oStudentDataResponse;
	}

	@Override
	@RequestMapping(value="/studentInfoUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody StudentInformationData oStudentInformationData) throws Exception
	{
		
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oStudentInformationData.getM_nStudentId() [IN] : " + oStudentInformationData.getM_nStudentId());
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{			
			oStudentDataResponse.m_bSuccess = oStudentInformationData.updateObject();
			oStudentDataResponse.m_arrStudentInformationData.add(oStudentInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		return oStudentDataResponse;
	}

	@Override
	@RequestMapping(value="/studentInfoGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody StudentInformationData oStudentInformationData) throws Exception 
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oStudentInformationData [IN] : " +oStudentInformationData);
		String strXml = "";
		try 
		{
			oStudentInformationData = oStudentInformationData.getStudentDetails (oStudentInformationData);
			//oStudentInformationData = (StudentInformationData) populateObject (oStudentInformationData);
			strXml = oStudentInformationData != null ? oStudentInformationData.generateXML ():"";
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}

	@RequestMapping(value="/isAadharnumberExist", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getAadharNumber(@RequestBody StudentInformationData oStudentInformationData) throws Exception
	{
		m_oLogger.info("getAadharNumber");
		m_oLogger.debug("getAadharNumber - oStudentInformationData [IN] :" + oStudentInformationData.getM_nFatherAadharNumber());
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			oStudentInformationData = (StudentInformationData) populateObject (oStudentInformationData);
			oStudentDataResponse.m_arrStudentInformationData.add (oStudentInformationData);	
			if(oStudentInformationData != null)
				oStudentDataResponse.m_bSuccess = true;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getStudentUID - oException : "  + oException);
			throw oException;
		}
		return oStudentDataResponse;
		
	}
	
	@RequestMapping(value = "/studentStatusInfoList",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getStatusStudentsList(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info ("studentStatuslist");
		m_oLogger.debug ("studentStatuslist - oStudentInformationData [IN] : " + oStudentInformationData);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			oStudentDataResponse.m_arrStudentInformationData = new ArrayList(oStudentInformationData.getStatusStudentsList(oStudentInformationData));
			oStudentDataResponse.m_nRowCount = oStudentDataResponse.m_arrStudentInformationData.size();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("studentStatuslist - oException : "  + oException);
			throw oException;
		}
		return oStudentDataResponse;
		
	}
	
	@Override	
	public GenericResponse list(StudentInformationData oGenericData, HashMap<String, String> arrOrderBy)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
}
