package com.techmust.scholarshipmanagement.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
	
	/*@RequestMapping(value = "/studentInfoCreateAndPrint",method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse createAndPrint(@RequestParam(name = "studentimage",required = false)MultipartFile oStudentMultipartFile,@RequestParam("studentObject") String oStudentData) throws Exception
	{
		m_oLogger.info ("createAndPrint");
		m_oLogger.debug ("createAndPrint - oStudentInformationData [IN] : " + oStudentData);
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try
		{
			StudentInformationData oStudentInformationData = new Gson().fromJson(oStudentData, StudentInformationData.class);
			oStudentDataResponse.m_bSuccess = oStudentInformationData.saveObject();
			oStudentDataResponse.m_arrStudentInformationData.add(oStudentInformationData);			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("createAndPrint - oException : " + oException);
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

	@SuppressWarnings("unused")
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
		if(oAcademic.getM_nAcademicId() > 0)
		{
			StudentDocuments oStudentDocuments = oStudentInformationData.getStudentUploadDocuments(oAcademic);
			List<StudentDocuments> listDocuments = new ArrayList<StudentDocuments>();
			listDocuments.add(oStudentDocuments);
			oAcademic.setM_arrStudentDocuments(listDocuments);
			arrAcademics.add(oAcademic);
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
				oStudentDataResponse.m_bSuccess = true;
		}
		catch (Exception oException)
		{
			m_oLogger.error("getStudentDetails - oException : " +oException);
			throw oException;
		}	
		return oStudentDataResponse;
	}
	
	@SuppressWarnings({ "unused", "rawtypes", "unchecked"})
	@RequestMapping(value = "/getStudentList",method = RequestMethod.POST,headers = {"Content-type=application/json"})
	@ResponseBody
	public static GenericResponse  getStudentList (@RequestBody StudentInformationData oStudentInformationData)
	{
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		StudentDataResponse oStudentDataResponse = new StudentDataResponse();
		try 
		{		
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentRoot = oCriteriaQuery.from(StudentInformationData.class);
			Join<Object, Object> oAcademicJoin = (Join<Object, Object>) oStudentRoot.fetch("m_oAcademicDetails");
			Join<Object,Object> oZenithJoin = (Join<Object, Object>) oStudentRoot.fetch("m_oZenithScholarshipDetails");
			oCriteriaQuery.select(oStudentRoot);
			oCriteriaQuery.orderBy(oCriteriaBuilder.asc(oStudentRoot.get("m_nApplicationPriority")),oCriteriaBuilder.asc(oStudentRoot.get("m_nUID")));
			List<Predicate> m_arrPredicateList = new ArrayList<Predicate>();
			m_arrPredicateList.add(oCriteriaBuilder.equal(oAcademicJoin.get("m_oAcademicYear"), oStudentInformationData.getM_nAcademicYearId()));
			m_arrPredicateList.add(oCriteriaBuilder.equal(oZenithJoin.get("m_oAcademicYear"), oStudentInformationData.getM_nAcademicYearId()));
			oCriteriaQuery.where(m_arrPredicateList.toArray(new Predicate[] {}));
			List<StudentInformationData> oList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
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
			oStudentInformationData = (StudentInformationData) populateObject(oStudentData);
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
	
	@SuppressWarnings("unchecked")
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
			//Checking Input Request
			if(oStudentData.getM_nCourseId() > 0)
				m_PredicateList.add(oCriteriaBuilder.equal(oJoinStudentAcademicRoot.get("m_oCourseInformationData"),oStudentData.getM_nCourseId()));
			if(oStudentData.getM_nInstitutionId() > 0)
				m_PredicateList.add(oCriteriaBuilder.equal(oJoinStudentAcademicRoot.get("m_oInstitutionInformationData"), oStudentData.getM_nInstitutionId()));
			if(oStudentData.getM_strGender() != "")
				m_PredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_strGender"), oStudentData.getM_strGender()));
			if(oStudentData.getM_nFacilitatorId() > 0)
				m_PredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_oFacilitatorInformationData"), oStudentData.getM_nFacilitatorId()));
			if(oStudentData.getM_strCity() != "")
				m_PredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_strCity"), oStudentData.getM_strCity()));
			if(oStudentData.getM_strReligion() != "")
				m_PredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_strReligion"), oStudentData.getM_strReligion()));
			oCriteriaQuery.select(oStudentRoot);
			if(oStudentData.getM_strSortBy().equals("m_nUID"))
			   oCriteriaQuery.orderBy(oCriteriaBuilder.asc(oStudentRoot.get("m_nUID")));
			else
				 oCriteriaQuery.orderBy(oCriteriaBuilder.asc(oStudentRoot.get("m_strStudentName")));
			oCriteriaQuery.where(m_PredicateList.toArray(new Predicate[] {}));
			List<StudentInformationData> studentList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			m_arrStudentList = new ArrayList<>(studentList);			
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getDownloadReportData - oException"+oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return m_arrStudentList;		
	}
	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public static String studentExcelData (ArrayList<StudentInformationData> arrStudentInformationData)
	{		
		boolean isExcelDownloaded = true;
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
				listArray.add(oExcelData.getM_strCategory());				
				listArray.add(oExcelData.getM_oFacilitatorInformationData().getM_strFacilitatorName());
				listArray.add(oExcelData.getM_strGender());
				listArray.add(oExcelData.getM_strReligion());
				AcademicDetails oDetails = getAcademicDetails(oExcelData.getM_oAcademicDetails());
				StudentScholarshipAccount oAccount = getScholarshipAccount(oDetails.getM_oStudentScholarshipAccount());
				ZenithScholarshipDetails oZenith = getZenithScholarshipData(oExcelData.getM_oZenithScholarshipDetails());
				listArray.add(oDetails.getM_oCourseInformationData().getM_strShortCourseName());				
				listArray.add(oDetails.getM_oInstitutionInformationData().getM_strInstitutionName());
				listArray.add(oDetails.getM_strStudentScore());
				listArray.add(oExcelData.getM_strParentalStatus());
				listArray.add(oExcelData.getM_strFatherName());
				listArray.add(oExcelData.getM_strFatherOccupation());
				listArray.add(oExcelData.getM_strPhoneNumber());
				listArray.add(oExcelData.getM_strMotherName());
				listArray.add(oExcelData.getM_strMotherOccupation());
				listArray.add(oExcelData.getM_strCity());
				listArray.add(oExcelData.getM_strState());
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
			isExcelDownloaded = false;
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
		List<ZenithScholarshipDetails> listZenith = new ArrayList<ZenithScholarshipDetails>(oZenithScholarshipDetails);
		ZenithScholarshipDetails oScholarshipDetails = new ZenithScholarshipDetails();
		if(listZenith.size() > 0)
			oScholarshipDetails = listZenith.get(0);
		return oScholarshipDetails;
	}
	
	private static StudentScholarshipAccount getScholarshipAccount(Set<StudentScholarshipAccount> set)
	{
		List<StudentScholarshipAccount> liStudentScholarshipAccounts = new ArrayList<StudentScholarshipAccount>(set);
		StudentScholarshipAccount oScholarshipAccount = new StudentScholarshipAccount();
		if(liStudentScholarshipAccounts.size() > 0)
			oScholarshipAccount = liStudentScholarshipAccounts.get(0);
		return oScholarshipAccount;
	}
	
	private static AcademicDetails getAcademicDetails(Set<AcademicDetails> set)
	{
		List<AcademicDetails> listAcademic = new ArrayList<AcademicDetails>(set);
		AcademicDetails oAcademicDetails = new AcademicDetails();
		if(listAcademic.size() > 0)
			 oAcademicDetails = listAcademic.get(0);
		return oAcademicDetails;		
	}
	
	@Override	
	public GenericResponse list(StudentInformationData oGenericData, HashMap<String, String> arrOrderBy)throws Exception
	{		
		return null;
	}
}
