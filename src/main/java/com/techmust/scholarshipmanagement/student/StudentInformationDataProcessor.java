package com.techmust.scholarshipmanagement.student;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.techmust.constants.Constants;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.HibernateUtil;
import com.techmust.helper.ZenithHelper;
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetailsProcessor;
import com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus.ZenithScholarshipDetails;
import com.techmust.scholarshipmanagement.sholarshipaccounts.StudentScholarshipAccount;
import com.techmust.scholarshipmanagement.studentdocuments.StudentDocuments;
import com.techmust.utils.AWSUtils;
import com.techmust.utils.Reports;
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
			oStudentDataResponse.m_strError_Desc = Constants.DELETEERRORDESC;			
		}
		return oStudentDataResponse;
	}
	
	@RequestMapping(value="/uploadStudentImageData", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse uploadStudentImagetoS3bucket(@RequestParam(name = "studentimage",required = false) MultipartFile oStudentMultipartFile,
														@RequestParam(name = "studentaadhar",required = false) MultipartFile oStudentAadharMultipartFile,
														@RequestParam(name = "studentElectricityBill",required = false) MultipartFile oStudentElectricityBillMultipartFile,
														@RequestParam(name = "fatheraadhar",required = false) MultipartFile oFatherMultipartFile,
														@RequestParam(name = "motheraadhar",required = false) MultipartFile oMotherMultipartFile,
														@RequestParam(name = "studentMarksCard1",required = false) MultipartFile oStudentMarksCard1MultipartFile,
														@RequestParam(name = "studentMarksCard2",required = false) MultipartFile oStudentMarksCard2MultipartFile,
														@RequestParam(name = "additionalDocuments",required = false) MultipartFile oOtherDocumentsMultipartFile,
														@RequestParam("studentId") int nStudentId,
														@RequestParam(name = "academicId",required = false) int nAcademicId) throws Exception
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
			    AcademicDetailsProcessor.uploadStudentDocumentstoS3bucket(oStudentAadharMultipartFile,
			    														  oStudentElectricityBillMultipartFile,			    														  
			    														  oFatherMultipartFile,
			    														  oMotherMultipartFile,
			    														  oStudentMarksCard1MultipartFile,
			    														  oStudentMarksCard2MultipartFile,
			    														  oOtherDocumentsMultipartFile,nAcademicId);
			}
			oStudentDataResponse.m_strStudentXMLData = generatePrintXMLData(oStudentDataResponse.m_bSuccess,oStudentInformationData);				
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("UploadstudentImage - oException : " + oException);
		}				
		return oStudentDataResponse; 
    }

	private String generatePrintXMLData(boolean bSuccess, StudentInformationData oStudentInformationData) throws Exception
	{
		String strXMLData = null;
		if(bSuccess)
		{
			oStudentInformationData = (StudentInformationData) populateObject(oStudentInformationData);
			strXMLData = oStudentInformationData.generateXML();
		}
		return strXMLData;		
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
			oStudentDataResponse.m_oStudentDocuments = getStudentUploadDocuments(oAcademicDetails);			
			oStudentDataResponse.m_arrStudentInformationData.add (oStudentInformationData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oStudentDataResponse;
	}	

	private StudentDocuments getStudentUploadDocuments(AcademicDetails oAcademicDetails)
	{
		StudentDocuments oStudentDocuments = null;
		if(oAcademicDetails != null && oAcademicDetails.getM_arrStudentDocuments().size() >0)
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
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
				Utils.createActivityLog("StudentInformationDataProcessor::update", oStudentInformationData);
				oStudentDataResponse.m_nStudentId = oStudentInformationData.getM_nStudentId();
				oStudentDataResponse.m_strStudentImageId = oStudentInformationData.getM_strStudentImageId();
				List<AcademicDetails> m_arrAcademicList = new ArrayList(oStudentInformationData.getM_oAcademicDetails());
				oStudentDataResponse.m_nAcademicId = m_arrAcademicList.get(0).getM_nAcademicId();				
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
		if(oAcademic.getM_nAcademicId() > 0)
		{
			StudentDocuments oStudentDocuments = oStudentInformationData.getStudentUploadDocuments(oAcademic);
			List<StudentDocuments> listDocuments = new ArrayList<StudentDocuments>();
			if(oStudentDocuments.getM_nDocumentId() > 0)
			{
				listDocuments.add(oStudentDocuments);
				oAcademic.setM_arrStudentDocuments(listDocuments);
				oAcademicDetails.clear();
				oAcademicDetails.add(oAcademic);
			}
			oStudentInformationData.setM_oAcademicDetails(arrAcademics);
		}		
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/isAadharnumberExist", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getAadharNumber(@RequestBody StudentInformationData oStudentInformationData) throws Exception
	{
		m_oLogger.info("getAadharNumber");
		m_oLogger.debug("getAadharNumber - oStudentInformationData [IN] :" + oStudentInformationData.getM_nFatherAadharNumber());
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			oStudentDataResponse.m_arrStudentInformationData = new ArrayList(getFatherAadharandMotherAadhar(oStudentInformationData));	
			if(oStudentDataResponse.m_arrStudentInformationData.size() > 0)
				oStudentDataResponse.m_bSuccess = true;
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("getStudentUID - oException : "  + oException);
			throw oException;
		}
		return oStudentDataResponse;		
	}
	
	private ArrayList<StudentInformationData> getFatherAadharandMotherAadhar(StudentInformationData oStudentInformationData)
	{
		ArrayList<StudentInformationData> arrStudentList = null;
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			oCriteriaQuery.select(oStudentRoot);
			List<Predicate> arrPredicateList = new ArrayList<Predicate>();
			if(oStudentInformationData.getM_nFatherAadharNumber() > 0)
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_nFatherAadharNumber"),oStudentInformationData.getM_nFatherAadharNumber()));
			else
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_nMotherAadharNumber"),oStudentInformationData.getM_nMotherAadharNumber()));
			oCriteriaQuery.where(arrPredicateList.toArray(new Predicate[] {}));
			arrStudentList = (ArrayList<StudentInformationData>) oEntityManager.createQuery(oCriteriaQuery).getResultList();
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("isAadharExist - oException : "  + oException);
			throw oException;
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return arrStudentList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/studentStatusInfoList",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getStatusStudentsList(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info ("studentStatuslist");
		m_oLogger.debug ("studentStatuslist - oStudentInformationData [IN] : " + oStudentInformationData);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			oStudentDataResponse.m_nRowCount = getStudentRowCount(oStudentInformationData);
			oStudentDataResponse.m_arrStudentInformationData = new ArrayList(oStudentInformationData.getStatusStudentsList(oStudentInformationData));			
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
				oStudentDataResponse.m_oStudentDocuments = getStudentUploadDocuments(m_arrAcademicList.size() > 0 ? m_arrAcademicList.get(0):null);
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getStudentFilterData",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse  getStudentFilterData (@RequestBody StudentInformationData oStudentInformationData)
	{
		
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try 
		{			
			oStudentDataResponse.m_arrStudentInformationData = (ArrayList<StudentInformationData>) populateStudentFilterObjectData(oStudentInformationData);
			if(oStudentDataResponse.m_arrStudentInformationData.size() > 0)
			{
				oStudentDataResponse.m_bSuccess = true;
				oStudentDataResponse.m_nRowCount = oStudentDataResponse.m_arrStudentInformationData.size();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("getStudentDetails - oException : " +oException);
			throw oException;
		}	
		return oStudentDataResponse;
	}
	
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value = "/getStudentList",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public static GenericResponse  getStudentList (@RequestBody StudentInformationData oStudentInformationData)
	{
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		oStudentDataResponse.m_nRowCount = getStudentRowCount(oStudentInformationData);
		try 
		{	//CriteriaBuilder,CriteriaQuery,Root Objects	
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			//Join the Child Entities
			Join<Object,Object> oZenithJoin = (Join<Object, Object>) oStudentRoot.fetch("m_oZenithScholarshipDetails");
			oCriteriaQuery.select(oStudentRoot);
			oCriteriaQuery.orderBy(oCriteriaBuilder.asc(oStudentRoot.get("m_nApplicationPriority")),oCriteriaBuilder.asc(oStudentRoot.get("m_nUID")));
			//Predicate List
			List<Predicate> m_arrPredicateList = new ArrayList<Predicate>();
			m_arrPredicateList.add(oCriteriaBuilder.equal(oZenithJoin.get("m_oAcademicYear"), oStudentInformationData.getM_nAcademicYearId()));
			oCriteriaQuery.where(m_arrPredicateList.toArray(new Predicate[] {}));
			//Get the Result List
			TypedQuery<StudentInformationData> oTypedQuery = oEntityManager.createQuery(oCriteriaQuery);
			oTypedQuery.setFirstResult((oStudentInformationData.getM_oZenithHelper().getM_nPageNo()-1)*oStudentInformationData.getM_oZenithHelper().getM_nPageSize());
			oTypedQuery.setMaxResults(oStudentInformationData.getM_oZenithHelper().getM_nPageSize());
			List<StudentInformationData> oList = oTypedQuery.getResultList();
			oStudentDataResponse.m_arrStudentInformationData = new ArrayList<>(oList);	
		}
		catch (Exception oException)
		{
			m_oLogger.error("getStudentList - oException : " +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}		
		return oStudentDataResponse;
	}	

	@RequestMapping(value = "/getFacilitatorWiseData", method = RequestMethod.POST, headers = {"content-type=application/json"})
	@ResponseBody
	public  GenericResponse getFacilitatorWiseStudent(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info("getFacilitatorWiseStudent");
		m_oLogger.debug("getFacilitatorWiseStudent - oStudentInformationData[IN] : " +oStudentInformationData);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		try 
		{
			 CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			 CriteriaQuery<StudentInformationData>  oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			 Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			 oCriteriaQuery.select(oStudentRoot);
			 oCriteriaQuery.where(oCriteriaBuilder.equal(oStudentRoot.get("m_oFacilitatorInformationData"),oStudentInformationData.getM_nFacilitatorId()));
		     List<StudentInformationData> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
		    oStudentDataResponse.m_arrStudentInformationData.addAll(list);
		}
		catch (Exception oException)
		{
			oException.printStackTrace();
		}
		return oStudentDataResponse;	
	}
	
	
	@RequestMapping(value = "/updateApplicationPriority",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse updateStudentApplicationPriority(@RequestBody StudentInformationData oStudentData)
	{
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		StudentInformationData oStudentInformationData = new StudentInformationData();
		try 
		{
			oStudentInformationData = oStudentInformationData.getStudentDetails (oStudentData);
			oStudentInformationData = getStudentDocuments(oStudentInformationData);
			oStudentInformationData.setM_nApplicationPriority(oStudentData.getM_nApplicationPriority());
			oStudentDataResponse.m_bSuccess = oStudentInformationData.updateObject();
			if(oStudentDataResponse.m_bSuccess)
				Utils.createActivityLog("StudentInformationDataProcessor::updateStudentApplicationPriority", oStudentInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateStudentApplicationPriority - oException"+oException);
		}
		return oStudentDataResponse;		
	}
	
	@SuppressWarnings("unchecked")
	private static long getStudentRowCount(StudentInformationData oStudentInformationData) 
	{
		long nStudentRowCount = 0;
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<Long> oCriteriaQuery = oCriteriaBuilder.createQuery(Long.class);
			Root<ZenithScholarshipDetails> oZenithRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);			
			oCriteriaQuery.select(oCriteriaBuilder.count(oZenithRoot));
			List<Predicate> m_arrPredicateList = new ArrayList<Predicate>();
			m_arrPredicateList.add(oCriteriaBuilder.equal(oZenithRoot.get("m_oAcademicYear"), oStudentInformationData.getM_nAcademicYearId()));
			if(oStudentInformationData.getM_strStatus() != "")
				m_arrPredicateList.add(oCriteriaBuilder.equal(oZenithRoot.get("m_strStatus"), oStudentInformationData.getM_strStatus()));
			oCriteriaQuery.where(m_arrPredicateList.toArray(new Predicate[] {}));
			TypedQuery<Long> oTypedQuery = oEntityManager.createQuery(oCriteriaQuery);
			nStudentRowCount = oTypedQuery.getSingleResult();			
		}
		catch (Exception oException)
		{
			m_oLogger.error("getStudentRowCount - oException"+oException);
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return nStudentRowCount;
	}	
	
	// Populate DropDown values in Reports Form
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getStudentCityNames",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getStudentCityNames(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info("getStudentCityNames");
		m_oLogger.debug("getStudentCityNames - StudentInformationData"+oStudentInformationData);
		StudentDataResponse oDataResponse = new StudentDataResponse();
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			oCriteriaQuery.select(oStudentRoot.get("m_strCity")).distinct(true);
			Query oQuery = oEntityManager.createQuery(oCriteriaQuery);
			oDataResponse.m_arrStudentInformationData = (ArrayList<StudentInformationData>)oQuery.getResultList();		
		}
		catch (Exception oException)
		{
			m_oLogger.debug("getStudentCityNames - oException"+oException);
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oDataResponse;
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getStudentCategory",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getStudentCategory(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info("getStudentCategory");
		m_oLogger.debug("getStudentCategory - StudentInformationData"+oStudentInformationData);
		StudentDataResponse oDataResponse = new StudentDataResponse();
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			oCriteriaQuery.select(oStudentRoot.get("m_strCategory")).distinct(true);
			Query oQuery = oEntityManager.createQuery(oCriteriaQuery);
			oDataResponse.m_arrStudentInformationData = (ArrayList<StudentInformationData>)oQuery.getResultList();		
		}
		catch (Exception oException)
		{
			m_oLogger.debug("getStudentCategory - oException"+oException);
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oDataResponse;
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getStudentParentalOccupations",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getParentalOccupations(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info("getParentalOccupations");
		m_oLogger.debug("getParentalOccupations - StudentInformationData"+oStudentInformationData);
		StudentDataResponse oDataResponse = new StudentDataResponse();
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			oCriteriaQuery.select(oStudentRoot.get("m_strFatherOccupation")).distinct(true);
			Query oQuery = oEntityManager.createQuery(oCriteriaQuery);
			oDataResponse.m_arrStudentInformationData = (ArrayList<StudentInformationData>)oQuery.getResultList();			
		}
		catch (Exception oException)
		{
			m_oLogger.debug("getParentalOccupations - oException"+oException);
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oDataResponse;		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getStudentMotherOccupations",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getMotherOccupations(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info("getMotherOccupations");
		m_oLogger.debug("getMotherOccupations - StudentInformationData"+oStudentInformationData);
		StudentDataResponse oDataResponse = new StudentDataResponse();
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			oCriteriaQuery.select(oStudentRoot.get("m_strMotherOccupation")).distinct(true);
			Query oQuery = oEntityManager.createQuery(oCriteriaQuery);
			oDataResponse.m_arrStudentInformationData = (ArrayList<StudentInformationData>)oQuery.getResultList();			
		}
		catch (Exception oException)
		{
			m_oLogger.debug("getMotherOccupations - oException"+oException);
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oDataResponse;		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getStudentParentalStatus",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getParentalStatus(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info("getStudentParentalStatus");
		m_oLogger.debug("getStudentParentalStatus - StudentInformationData"+oStudentInformationData);
		StudentDataResponse oDataResponse = new StudentDataResponse();
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			oCriteriaQuery.select(oStudentRoot.get("m_strParentalStatus")).distinct(true);
			Query oQuery = oEntityManager.createQuery(oCriteriaQuery);
			oDataResponse.m_arrStudentInformationData = (ArrayList<StudentInformationData>)oQuery.getResultList();			
		}
		catch (Exception oException)
		{
			m_oLogger.debug("getStudentParentalStatus - oException"+oException);
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oDataResponse;		
	}	
	
	//Download Reports Based on Particular Parameters	
	@RequestMapping(value = "/downloadStudentReports",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse downloadReports (@RequestBody StudentInformationData oInformationData) 
	{
		m_oLogger.info("downloadReports");
		StudentDataResponse oDataResponse = new StudentDataResponse();
		try
		{
			ArrayList<StudentInformationData> StudentReportList = getDownloadReportData(oInformationData);
			String strReportURL = studentExcelData(StudentReportList);
			if(strReportURL != "")
			{
				oDataResponse.m_strStudentDownloadReportURL = strReportURL;			
				oDataResponse.m_bSuccess = true;
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("downloadReports - oException"+oException);
		}
		return oDataResponse;		
	}
	
	//Check Max Student UID Number
	@RequestMapping(value = "/getMaxUIDNumber",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getStudentMaxUID(@RequestBody StudentInformationData oStudentInformationData)
	{
		m_oLogger.info("getStudentMaxUID");
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<Long> oCriteriaQuery = oCriteriaBuilder.createQuery(Long.class);
			Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			oCriteriaQuery.select(oCriteriaBuilder.max(oStudentRoot.get("m_nUID")));
			TypedQuery<Long> oTypedQuery = oEntityManager.createQuery(oCriteriaQuery);
			Long nStudentUID = oTypedQuery.getSingleResult();
			oStudentDataResponse.m_nUID = nStudentUID;
			oStudentDataResponse.m_bSuccess = true;
		}
		catch (Exception oException)
		{
			m_oLogger.error("getStudentMaxUID - oException: "+oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oStudentDataResponse;
	}	
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public static ArrayList<StudentInformationData> getDownloadReportData (StudentInformationData oStudentData)
	{
		EntityManager oEntityManager = oStudentData._getEntityManager();
		ArrayList<StudentInformationData> m_arrStudentList = null;
		try
		{
			//CriteriaBuilder,CriteriaQuery and Root Object
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			//Predicate List
			List<Predicate> m_PredicateList = new ArrayList<Predicate>();
			//Join Objects
			Join<Object, Object> oJoinStudentAcademicRoot = (Join<Object, Object>) oStudentRoot.fetch("m_oAcademicDetails");
			Join<Object, Object> oZenithStudentRoot = (Join<Object, Object>) oStudentRoot.fetch("m_oZenithScholarshipDetails");			
			m_PredicateList.add(oCriteriaBuilder.equal(oJoinStudentAcademicRoot.get("m_oAcademicYear"),oStudentData.getM_nAcademicYearId()));
			m_PredicateList.add(oCriteriaBuilder.equal(oZenithStudentRoot.get("m_oAcademicYear"),oStudentData.getM_nAcademicYearId()));
			
			if(oStudentData.getM_dFromDate() != null && oStudentData.getM_dToDate() != null ) 
				m_PredicateList.add(oCriteriaBuilder.between(oZenithStudentRoot.<Date>get("m_dApprovedDate"), oStudentData.getM_dFromDate(), oStudentData.getM_dToDate()));

			//Checking Input Request and Joined Parameters
			if(oStudentData.getM_nCourseId() > 0)
				m_PredicateList.add(oCriteriaBuilder.equal(oJoinStudentAcademicRoot.get("m_oCourseInformationData"),oStudentData.getM_nCourseId()));
			
			if(oStudentData.isM_bStudentGraduate())
				m_PredicateList.add(oCriteriaBuilder.equal(oJoinStudentAcademicRoot.get("m_oCourseInformationData").get("m_bFinalYear"),oStudentData.isM_bStudentGraduate()));
			
			if(oStudentData.getM_nInstitutionId() > 0)
				m_PredicateList.add(oCriteriaBuilder.equal(oJoinStudentAcademicRoot.get("m_oInstitutionInformationData"), oStudentData.getM_nInstitutionId()));	
			if(oStudentData.getM_strScore() != null)
				m_PredicateList.add(oCriteriaBuilder.equal(oJoinStudentAcademicRoot.get("m_strStudentScore"), oStudentData.getM_strScore()));			
			m_PredicateList = addPredicateList(m_PredicateList,oCriteriaBuilder,oStudentRoot,oStudentData);
			if(oStudentData.getM_nFamilyCount() > 0)
			{
				Join oJoinRoot = (Join) oStudentRoot.fetch("m_oSibilingDetails");
				Expression sibilingCount = oCriteriaBuilder.count(oJoinRoot);
				oCriteriaQuery.groupBy(oStudentRoot.get("m_nStudentId"));				
				oCriteriaQuery.having(oCriteriaBuilder.and(oCriteriaBuilder.equal(sibilingCount,oStudentData.getM_nFamilyCount())));
				m_PredicateList.add(oCriteriaBuilder.greaterThan(oJoinRoot.get("m_nZenithUID"),0));
			}
			oCriteriaQuery.select(oStudentRoot);			
			if(oStudentData.getM_strSortBy().equals("m_nUID"))
			   oCriteriaQuery.orderBy(oCriteriaBuilder.asc(oStudentRoot.get("m_nUID")));
			else
				oCriteriaQuery.orderBy(oCriteriaBuilder.asc(oStudentRoot.get("m_strStudentName")));			
			oCriteriaQuery.where(m_PredicateList.toArray(new Predicate[] {}));
			TypedQuery<StudentInformationData>oTypedQuery = oEntityManager.createQuery(oCriteriaQuery);
			List<StudentInformationData> studentList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			m_arrStudentList = new ArrayList<>(studentList);			
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getDownloadReportData - oException"+oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return m_arrStudentList;		
	}
	
	private static List<Predicate> addPredicateList(List<Predicate> arrPredicateList, CriteriaBuilder oCriteriaBuilder,Root<StudentInformationData> oStudentRoot, StudentInformationData oStudentData)
	{
		try 
		{
			if(oStudentData.getM_strCategory() != "")
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_strCategory"),oStudentData.getM_strCategory()));					
			
			//StudentData Parameters
			if(oStudentData.isM_bStudentMedicalCondition())
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_bStudentMedicalCondition"),oStudentData.isM_bStudentMedicalCondition()));
			
			if(oStudentData.isM_bParentMedicalCondition())
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_bParentMedicalCondition"),oStudentData.isM_bParentMedicalCondition()));
			
			if(oStudentData.getM_strGender() != "")
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_strGender"), oStudentData.getM_strGender()));
			
			if(oStudentData.getM_nFacilitatorId() > 0)
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_oFacilitatorInformationData"), oStudentData.getM_nFacilitatorId()));
			
			if(oStudentData.getM_strCity() != "")
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_strCity"), oStudentData.getM_strCity()));
			
			if(oStudentData.getM_strReligion() != "")
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_strReligion"), oStudentData.getM_strReligion()));
			
			if(oStudentData.getM_strMotherOccupation() != "")
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_strMotherOccupation"), oStudentData.getM_strMotherOccupation()));
			
			if(oStudentData.getM_strParentalStatus() != "")
				arrPredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_strParentalStatus"), oStudentData.getM_strParentalStatus()));				
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("addPredicateList - oException"+oException);
			throw oException;
		}
		return arrPredicateList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String studentExcelData (ArrayList<StudentInformationData> arrStudentInformationData)
	{		
		
		String strDownloadPath = "";
		try
		{			
			Map<Integer,Object[]> oMapData = new HashMap<Integer,Object[]> ();
			oMapData.put(0, Reports.getColumnHeader());						
			for(int nIndex = 0; nIndex < arrStudentInformationData.size(); nIndex++)
			{
				List listArray = new ArrayList();
				Object[]  oObject = new Object[] {} ;
				StudentInformationData oExcelData = arrStudentInformationData.get(nIndex);
								
				listArray.add(oExcelData.getM_nUID());
				listArray.add(oExcelData.getM_strStudentName());
				listArray.add(String.valueOf(oExcelData.getM_nStudentAadharNumber()));
				listArray.add(oExcelData.getM_strCategory());				
				listArray.add(oExcelData.getM_oFacilitatorInformationData().getM_strFacilitatorName());
				listArray.add(oExcelData.getM_strGender());
				listArray.add(oExcelData.getM_strReligion());
				//Getting SubEntities Data
				AcademicDetails oDetails = getAcademicDetails(oExcelData.getM_oAcademicDetails());
				StudentScholarshipAccount oAccount = getScholarshipAccount(oDetails.getM_oStudentScholarshipAccount());
				ZenithScholarshipDetails oZenith = getZenithScholarshipData(oExcelData.getM_oZenithScholarshipDetails());
				listArray.add(oDetails.getM_oCourseInformationData().getM_strShortCourseName());				
				listArray.add(oDetails.getM_oInstitutionInformationData().getM_strInstitutionName());
				listArray.add(oDetails.getM_strStudentScore());
				listArray.add(oExcelData.getM_strParentalStatus());
				listArray.add(oExcelData.getM_strFatherName());
				listArray.add(String.valueOf(oExcelData.getM_nFatherAadharNumber()));
				listArray.add(oExcelData.getM_strFatherOccupation());
				listArray.add(oExcelData.getM_strPhoneNumber());
				listArray.add(oExcelData.getM_strMotherName());
				listArray.add(String.valueOf(oExcelData.getM_nMotherAadharNumber()));
				listArray.add(oExcelData.getM_strMotherOccupation());
				listArray.add(oExcelData.getM_strCity());
				listArray.add(oExcelData.getM_strState());
				listArray.add(oExcelData.getM_nPincode());
				listArray.add(oZenith.getM_fApprovedAmount());
				listArray.add(oAccount.getM_nChequeNumber());
				listArray.add(oZenith.getM_strStudentRemarks());
				oObject = listArray.toArray();
				oMapData = addObject(oMapData,nIndex, oObject);							
			}
			strDownloadPath = Reports.createWorkBook(oMapData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("studentExcelData - oException"+oException);
			throw oException;
		}
		return strDownloadPath;
	}
	
	private static Map<Integer, Object[]> addObject(Map<Integer, Object[]> oMapObjectData, int keyCount, Object[] oObject)
	{		
		oMapObjectData.put(++keyCount, oObject);		
		return oMapObjectData;		
	}
	
	private static ZenithScholarshipDetails getZenithScholarshipData(Set<ZenithScholarshipDetails> oZenithScholarshipDetails) 
	{
		ZenithScholarshipDetails oScholarshipDetails = new ZenithScholarshipDetails();
		try
		{
			List<ZenithScholarshipDetails> listZenith = new ArrayList<ZenithScholarshipDetails>(oZenithScholarshipDetails);			
			if(listZenith.size() > 0)
				oScholarshipDetails = listZenith.get(0);
		}
		catch 
		(Exception oException) 
		{
			m_oLogger.error("getZenithScholarshipData- oException"+oException);
		}		
		return oScholarshipDetails;
	}
	
	private static StudentScholarshipAccount getScholarshipAccount(Set<StudentScholarshipAccount> set)
	{
		StudentScholarshipAccount oScholarshipAccount = new StudentScholarshipAccount();
		try 
		{
			List<StudentScholarshipAccount> liStudentScholarshipAccounts = new ArrayList<StudentScholarshipAccount>(set);			
			for(int nIndex = 0;nIndex < liStudentScholarshipAccounts.size(); nIndex++)
			{
				StudentScholarshipAccount oActiveCheque = liStudentScholarshipAccounts.get(nIndex);
				if(oActiveCheque.getM_strChequeStatus().equals(Constants.CHEQUESTATUS))
				{
					oScholarshipAccount = liStudentScholarshipAccounts.get(nIndex);
				}
			}		
		} 
		catch (Exception oException)
		{
			m_oLogger.debug("getScholarshipAccount - oException"+oException);
		}		
		return oScholarshipAccount;
	}
	
	private static AcademicDetails getAcademicDetails(Set<AcademicDetails> set)
	{
		AcademicDetails oAcademicDetails = new AcademicDetails();
		try 
		{
			List<AcademicDetails> listAcademic = new ArrayList<AcademicDetails>(set);			
			if(listAcademic.size() > 0)
				 oAcademicDetails = listAcademic.get(0);
		} 
		catch (Exception oException) 
		{
			m_oLogger.debug("getAcademicDetails - oException"+oException);
		}		
		return oAcademicDetails;		
	}
	
	@Override	
	public GenericResponse list(StudentInformationData oGenericData, HashMap<String, String> arrOrderBy)throws Exception
	{		
		return null;
	}

	public static boolean uploadStudentImage(MultipartFile oStudentMultipartFile, int nStudentId) 
	{
		StudentInformationData oStudentInformationData = new StudentInformationData();
		oStudentInformationData.setM_nStudentId(nStudentId);
		boolean isUpdate = false;
		try 
		{
			if(oStudentMultipartFile.getSize() > 0)
			{
				oStudentInformationData = (StudentInformationData) populateObject(oStudentInformationData);	
		        String strUUID = Utils.getUUID();
		        String strFileExtension = Constants.IMAGE_DEFAULT_EXTENSION;
				String strStudentImagePath = Constants.STUDENTIMAGEFOLDER + strUUID + strFileExtension;
			    AWSUtils.UploadToStudentImagesFolder(strStudentImagePath, oStudentMultipartFile);
			    oStudentInformationData.setM_strStudentImageId(strUUID);
			    isUpdate = oStudentInformationData.updateObject();
			}
		} 
		catch (Exception oException)
		{
			m_oLogger.error("uploadStudentImage - oException"+oException);
		}
		return isUpdate;
	}
}
