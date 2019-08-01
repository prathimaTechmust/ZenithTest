package com.techmust.scholarshipmanagement.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
import com.techmust.generic.util.HibernateUtil;
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
			if(oStudentDataResponse.m_bSuccess)
			{
				oStudentDataResponse.m_arrStudentInformationData.add(oStudentInformationData);
				Utils.createActivityLog("StudentInformationDataProcessor::create", oStudentInformationData);
			}			
			
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
	
	@RequestMapping(value = "/studentInfoCreateAndPrint",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse createAndPrint(@RequestBody StudentInformationData oStudentInformationData) throws Exception
	{
		m_oLogger.info ("create and print");
		m_oLogger.debug ("create and print - oStudentInformationData [IN] : " + oStudentInformationData);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			oStudentDataResponse.m_bSuccess = oStudentInformationData.saveObject();
			if(oStudentDataResponse.m_bSuccess == true)
				oStudentDataResponse.m_strStudentXMLData = oStudentInformationData.generateXML();
			oStudentDataResponse.m_arrStudentInformationData.add(oStudentInformationData);
			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create and print - oException : " + oException);
			throw oException;
		}
		return oStudentDataResponse;		
	}
	
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
			if(oStudentDataResponse.m_bSuccess)
				Utils.createActivityLog("StudentInformationDataProcessor::deleteData", oStudentInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		throw oException;
		}
		return oStudentDataResponse;
	}
	
	@RequestMapping(value="/uploadStudentImageData", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse uploadStudentImagetoS3bucket(@RequestParam(name = "studentimage",required = false) MultipartFile oStudentMultipartFile, @RequestParam("studentId") int nStudentId ) throws Exception
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
			oStudentDataResponse.m_oStudentDocuments = getStudentDocuments(oAcademicDetails);			
			oStudentDataResponse.m_arrStudentInformationData.add (oStudentInformationData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oStudentDataResponse;
	}	

	private StudentDocuments getStudentDocuments(AcademicDetails oAcademicDetails)
	{
		StudentDocuments oStudentDocuments = null;
		if(oAcademicDetails.getM_arrStudentDocuments().size() >0)
		{
			oStudentDocuments = oAcademicDetails.getM_arrStudentDocuments().get(0);
			oStudentDocuments = Utils.getStudentDocuments(oStudentDocuments);
		}
		return oStudentDocuments;		
	}

	@RequestMapping(value="/studentInfoGetUIDData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getSearchStudentUIDData(@RequestBody StudentInformationData oStudentInformationData) throws Exception
	{
		m_oLogger.info("getSearchStudentUIDData");
		m_oLogger.debug("getSearchStudentUIDData - oStudentInformationData [IN] :" + oStudentInformationData.getM_nUID());
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			oStudentInformationData = oStudentInformationData.getSearchUIDStudentData(oStudentInformationData);
			if(oStudentInformationData != null)
			{
				oStudentDataResponse = getStudentUIDData(oStudentInformationData);
			}
			
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getSearchUIDStudentData - oException : "  + oException);
			throw oException;
		}
		return oStudentDataResponse;
	}
	
	private StudentDataResponse getStudentUIDData(StudentInformationData oStudentInformationData)
	{
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		if(oStudentInformationData.getM_oZenithScholarshipDetails().size() > 0)
		{
			oStudentDataResponse.m_arrStudentInformationData.add (oStudentInformationData);
			oStudentDataResponse.m_bSuccess = true;
		}
		return oStudentDataResponse;
	}
	
	@RequestMapping(value="/studentInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strSortColumn(), oData.getM_strOrderBy());
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
			StudentInformationData oStudentData = getStudentDocuments(oStudentInformationData);		
			oStudentDataResponse.m_bSuccess = oStudentInformationData.updateObject();
			if(oStudentDataResponse.m_bSuccess)
			{
				oStudentDataResponse.m_arrStudentInformationData.add(oStudentInformationData);
				Utils.createActivityLog("StudentInformationDataProcessor::update", oStudentInformationData);
			}			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		return oStudentDataResponse;
	}

	private StudentInformationData getStudentDocuments(StudentInformationData oStudentInformationData)
	{
		Set<AcademicDetails> arrAcademics = oStudentInformationData.getM_oAcademicDetails();
		List<AcademicDetails> oAcademicDetails = new ArrayList<>(arrAcademics);
		AcademicDetails oAcademic = oAcademicDetails.get(0);
		StudentDocuments oStudentDocuments = oStudentInformationData.getStudentUploadDocuments(oAcademic);
		List<StudentDocuments> listDocuments = new ArrayList<StudentDocuments>();
		listDocuments.add(oStudentDocuments);
		oAcademic.setM_arrStudentDocuments(listDocuments);
		arrAcademics.add(oAcademic);
		oStudentInformationData.setM_oAcademicDetails(arrAcademics);
		return oStudentInformationData;	
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
	
	@RequestMapping(value = "/getStudentUIDData",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getStudentDataUID(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info ("getStudentDataUID");
		m_oLogger.debug ("getStudentDataUID - oStudentInformationData [IN] : " + oStudentInformationData);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			oStudentInformationData = oStudentInformationData.getUIDAndAadharFormData(oStudentInformationData);
			if(oStudentInformationData != null)
			{
				oStudentDataResponse.m_arrStudentInformationData.add(oStudentInformationData);
				List<AcademicDetails> m_arrAcademicList = new ArrayList<AcademicDetails>(oStudentInformationData.getM_oAcademicDetails());				
				oStudentDataResponse.m_oStudentDocuments = getStudentDocuments(m_arrAcademicList.get(0));
				oStudentDataResponse.m_bSuccess = true;
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getStudentDataUID - oException : "  + oException);
			throw oException;
		}
		return oStudentDataResponse;		
	}
	
	@RequestMapping(value = "/getStudentFilterData",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse  getStudentFilterData (@RequestBody StudentInformationData oStudentInformationData)
	{
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try 
		{			
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentInformationRoot = oCriteriaQuery.from(StudentInformationData.class);   
			List<Predicate> m_arrPredicateList = new ArrayList<Predicate>();
			if(oStudentInformationData.getM_strStudentName() != "")
					m_arrPredicateList.add(oCriteriaBuilder.equal(oStudentInformationRoot.get("m_strStudentName"), oStudentInformationData.getM_strStudentName()));
			if(oStudentInformationData.getM_strPhoneNumber() != "")
				m_arrPredicateList.add(oCriteriaBuilder.equal(oStudentInformationRoot.get("m_strPhoneNumber"), oStudentInformationData.getM_strPhoneNumber()));
			if(oStudentInformationData.getM_nStudentAadharNumber() > 0)
				m_arrPredicateList.add(oCriteriaBuilder.equal(oStudentInformationRoot.get("m_nStudentAadharNumber"), oStudentInformationData.getM_nStudentAadharNumber()));
			 oCriteriaQuery.select(oStudentInformationRoot).where(m_arrPredicateList.toArray(new Predicate[]{}));
			List<StudentInformationData> m_arrStudentInformationDataList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(m_arrStudentInformationDataList.size() > 0)
			{
				for(int nIndex = 0; nIndex < m_arrStudentInformationDataList.size(); nIndex++)
				{
					oStudentInformationData = m_arrStudentInformationDataList.get(nIndex);
					oStudentInformationData.setM_strAcademicYear(oStudentInformationData.getM_strAcademicYear());
					oStudentInformationData.setM_oAcademicDetails(oStudentInformationData.getAcademicDetails(oStudentInformationData));
					oStudentDataResponse.m_arrStudentInformationData.add(oStudentInformationData);
				}
				oStudentDataResponse.m_bSuccess = true;
			}			
			
		}
		catch (Exception oException)
		{
			m_oLogger.error("getStudentDetails - oException : " +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}		
		return oStudentDataResponse;
	}
	
	@Override	
	public GenericResponse list(StudentInformationData oGenericData, HashMap<String, String> arrOrderBy)throws Exception
	{		
		return null;
	}	
}
