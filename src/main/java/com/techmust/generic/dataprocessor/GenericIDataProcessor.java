package com.techmust.generic.dataprocessor;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.hibernate.Criteria;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.IGenericData;
import com.techmust.generic.response.GenericResponse;
import com.techmust.generic.util.HibernateUtil;
import com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus.ZenithScholarshipDetails;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public abstract class GenericIDataProcessor<T extends IGenericData> 
{
	public static Logger m_oLogger = Logger.getLogger(GenericIDataProcessor.class);
	public static final String kUserCredentialsFailed = "User credential failed";
	
	public abstract GenericResponse create (T oGenericData) throws Exception;
	public abstract GenericResponse deleteData (T oGenericData) throws Exception;
	public abstract GenericResponse get (T oGenericData) throws Exception;
	public abstract GenericResponse list (T oGenericData, HashMap<String, String> arrOrderBy) throws Exception;
	public abstract GenericResponse update (T oGenericData) throws Exception;
	public abstract String getXML (T oGenericData) throws Exception;
	
	

	public static String getClientCompatibleFormat (Date oDateTime)
    {
		m_oLogger.debug("getClientCompatibleFormat");
		m_oLogger.debug("getClientCompatibleFormat - oDateTime [IN] :" +oDateTime);
		String strSimpleDate = "";
		try
		{
		    if (oDateTime != null)
		    {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				strSimpleDate = formatter.format(oDateTime);
		    }
		}
		catch (Exception oException)
		{
			m_oLogger.error("getClientCompatibleFormat - oException  :" +oException);
		}
		m_oLogger.debug("getClientCompatibleFormat - strSimpleDate [OUT] :" +strSimpleDate);
		return strSimpleDate;
    }
	
	public static String getTime(Date oDate) 
	{
		String strTime = "";
		DateFormat oDateFormat = new SimpleDateFormat("hh:mm a");
		strTime = oDateFormat.format(oDate.getTime());
		return strTime;
	}
	
	public static Date getDBCompatibleDateFormat (String strDate)
    {
		m_oLogger.debug("getDBCompatibleDateFormat");
		m_oLogger.debug("getDBCompatibleDateFormat - strDate [IN] :" +strDate);
		Date oUtilDate = null;
		try
		{
		    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		    oUtilDate = (Date) formatter.parse(strDate);
		}
		catch (Exception oException)
		{
			m_oLogger.error("getDBCompatibleDateFormat - oException  :" +oException);
		}
		m_oLogger.debug("getDBCompatibleDateFormat - oUtilDate [OUT] :" +oUtilDate);
		return oUtilDate;
    }
	
	public static Date getDBCompatibleFilterDateFormat (String strDate, boolean bIsForTo)
    {
		m_oLogger.debug("getDBCompatibleFilterDateFormat");
		m_oLogger.debug("getDBCompatibleFilterDateFormat - strDate [IN] :" +strDate);
		Date oUtilDate = null;
		try
		{
		    DateFormat oFormatter = new SimpleDateFormat("dd-MM-yyyy");
		    oUtilDate = (Date) oFormatter.parse(strDate);
		    if (bIsForTo)
		    {
		    	Calendar oCalendar =Calendar.getInstance();
		    	oCalendar.setTime(oUtilDate);
		    	oCalendar.add(Calendar.HOUR_OF_DAY,23);
		    	oCalendar.add(Calendar.MINUTE,59);
		    	oCalendar.add(Calendar.SECOND,59);
		    	oUtilDate = oCalendar.getTime();
		    }
		}
		catch (Exception oException)
		{
			m_oLogger.error("getDBCompatibleFilterDateFormat - oException  :" +oException);
		}
		m_oLogger.debug("getDBCompatibleFilterDateFormat - oUtilDate [OUT] :" +oUtilDate);
		return oUtilDate;
    }

	public static Object populateObject (GenericData  oGenericData) throws Exception
	{
		m_oLogger.debug("populateObject");
		m_oLogger.debug("populateObject - oGenericData [IN] :" +oGenericData);
//		Session oSession =	HibernateUtil.getSession();
		EntityManager oEntityManager = oGenericData._getEntityManager();
		Object oResult = null;
		ArrayList<GenericData> arrGenericData = new ArrayList<GenericData> ();
		try
		{
/*
 			Criteria oCriteria = oSession.createCriteria(oGenericData.getClass().getName());
			oCriteria = oGenericData.prepareCriteria(oCriteria);
			oResult = oCriteria.uniqueResult();
*/
 			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
 			Class<GenericData> oClass = (Class<GenericData>) oGenericData.getClass();
			CriteriaQuery<GenericData> oCriteria = oCriteriaBuilder.createQuery(oClass);
			Root<GenericData> oRootObject = oCriteria.from(oClass );
			oCriteria.select( oRootObject );
			oCriteria.where(oGenericData.prepareCriteria(oRootObject, oCriteria, oCriteriaBuilder));
			arrGenericData = (ArrayList<GenericData>) oEntityManager.createQuery( oCriteria ).getResultList();
			if(arrGenericData.size() > 0)
				oResult = arrGenericData.get(0);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("populateObject - oException :" +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("populateObject - oResult [OUT] :" +oResult);
		return oResult;
	}
	
	public static Object populateFilterObjectData (GenericData oGenericData)
	{
		m_oLogger.info("populateFilterObjectData");
		m_oLogger.debug("populateFilterObjectData - GenericData" + oGenericData);
		EntityManager oEntityManager = oGenericData._getEntityManager();
		ArrayList<GenericData> m_arrGenericDataList = new ArrayList<GenericData>();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			Class<GenericData> oGetClass = (Class<GenericData>) oGenericData.getClass();
			CriteriaQuery<GenericData> oCriteriaQuery = oCriteriaBuilder.createQuery(oGetClass);
			Root<GenericData> oRootGetClassObject = oCriteriaQuery.from(oGetClass);
			oCriteriaQuery.select(oRootGetClassObject);			
			oCriteriaQuery.where(oGenericData.prepareCriteria(oRootGetClassObject, oCriteriaQuery, oCriteriaBuilder));
			m_arrGenericDataList = (ArrayList<GenericData>) oEntityManager.createQuery(oCriteriaQuery).getResultList();
			
		}
		catch (Exception oException)
		{
			m_oLogger.error("populateFilterObjectData - oException"+ oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return m_arrGenericDataList;
	}
	
	@SuppressWarnings("unchecked")
	public static Object populateStudentFilterObjectData (StudentInformationData oStudentData)
	{
		m_oLogger.info("populateStudentFilterObjectData");
		m_oLogger.debug("populateStudentFilterObjectData - StudentInformationData" + oStudentData);
		ArrayList<StudentInformationData> m_arrStudentDataList = new ArrayList<StudentInformationData>();
		try
		{
			
			if(oStudentData.getM_strStatus() != null)
			{
				m_arrStudentDataList = getStudentStatusFilteredData(oStudentData);							
			}
			else
			{
				m_arrStudentDataList = getStudentListFilterObjectData(oStudentData);					
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("populateStudentFilterObjectData - oException"+ oException);
		}		
		return m_arrStudentDataList;		
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<StudentInformationData> getStudentStatusFilteredData(StudentInformationData oStudentData)
	{
		m_oLogger.info("StudentFilterObjectData");
		m_oLogger.debug("StudentFilterObjectData - StudentInformationData" + oStudentData);
		ArrayList<StudentInformationData> m_arrFilterStudentList = new ArrayList<StudentInformationData>();
		EntityManager oEntityManager = oStudentData._getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentRoot = oQuery.from(StudentInformationData.class);			
			Join<Object, Object> oRootJoin = (Join<Object, Object>) oStudentRoot.fetch("m_oZenithScholarshipDetails");
			Join<Object, Object> oAcademicJoin = (Join<Object, Object>) oStudentRoot.fetch("m_oAcademicDetails");
			List<Predicate> m_PredicateList = new ArrayList<Predicate>();
			m_PredicateList.add(oCriteriaBuilder.equal(oRootJoin.get("m_oAcademicYear"), oStudentData.getM_nAcademicYearId()));
			m_PredicateList.add(oCriteriaBuilder.equal(oAcademicJoin.get("m_oAcademicYear"), oStudentData.getM_nAcademicYearId()));
			if(oStudentData.getM_nUID() >  0)
				m_PredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_nUID"),oStudentData.getM_nUID()));
			if(oStudentData.getM_strStatus() != null)
				m_PredicateList.add(oCriteriaBuilder.equal(oRootJoin.get("m_strStatus"),oStudentData.getM_strStatus()));
			if(oStudentData.getM_strPhoneNumber() != "")
				m_PredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_strPhoneNumber"),oStudentData.getM_strPhoneNumber()));
			if(oStudentData.getM_strStudentName() != "")
			{
				Expression<String> oExpression = oStudentRoot.get("m_strStudentName");
				m_PredicateList.add( oCriteriaBuilder.like(oExpression,"%"+oStudentData.getM_strStudentName()+"%"));
			}	
			if(oStudentData.getM_nStudentAadharNumber() > 0)
				m_PredicateList.add(oCriteriaBuilder.equal(oStudentRoot.get("m_nStudentAadharNumber"),oStudentData.getM_nStudentAadharNumber()));
			oQuery.select(oStudentRoot).where(m_PredicateList.toArray(new Predicate[] {}));
			TypedQuery<StudentInformationData> typedQuery = oEntityManager.createQuery(oQuery);
			m_arrFilterStudentList = (ArrayList<StudentInformationData>) typedQuery.getResultList();	
		}
		catch (Exception oException)
		{
			m_oLogger.debug("getStudentFiltered Data - oException"+oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return m_arrFilterStudentList;
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<StudentInformationData> getStudentListFilterObjectData(StudentInformationData oStudentInformationData)
	{
		m_oLogger.info("populateFilterObjectData");
		m_oLogger.debug("populateFilterObjectData - StudentInformationData" + oStudentInformationData);
		EntityManager oEntityManager = oStudentInformationData._getEntityManager();
		ArrayList<StudentInformationData> m_arrGenericDataList = new ArrayList<StudentInformationData>();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oRootObject = oCriteriaQuery.from(StudentInformationData.class);
			Join<Object, Object> oJoin = (Join<Object, Object>) oRootObject.fetch("m_oAcademicDetails");
			Join<Object, Object> oZenithJoin = (Join<Object, Object>) oRootObject.fetch("m_oZenithScholarshipDetails");
			oCriteriaQuery.select(oRootObject);
			List<Predicate>m_arrPredicateList = new ArrayList<Predicate>();
			m_arrPredicateList.add(oCriteriaBuilder.equal(oJoin.get("m_oAcademicYear"), oStudentInformationData.getM_nAcademicYearId()));
			m_arrPredicateList.add(oCriteriaBuilder.equal(oZenithJoin.get("m_oAcademicYear"), oStudentInformationData.getM_nAcademicYearId()));
			if(oStudentInformationData.getM_strStudentName() != "")
			{
				Expression<String> oExpression = oRootObject.get("m_strStudentName");
				m_arrPredicateList.add( oCriteriaBuilder.like(oExpression,"%"+oStudentInformationData.getM_strStudentName()+"%"));
			}
			if(oStudentInformationData.getM_nUID() > 0 )
				m_arrPredicateList.add(oCriteriaBuilder.equal(oRootObject.get("m_nUID"),oStudentInformationData.getM_nUID()));
			if(oStudentInformationData.getM_strPhoneNumber() != "")
				m_arrPredicateList.add(oCriteriaBuilder.equal(oRootObject.get("m_strPhoneNumber"),oStudentInformationData.getM_strPhoneNumber()));
			if(oStudentInformationData.getM_nStudentAadharNumber() > 0)
				m_arrPredicateList.add(oCriteriaBuilder.equal(oRootObject.get("m_nStudentAadharNumber"),oStudentInformationData.getM_nStudentAadharNumber()));
			oCriteriaQuery.where(m_arrPredicateList.toArray(new Predicate[] {}));
			m_arrGenericDataList = (ArrayList<StudentInformationData>) oEntityManager.createQuery(oCriteriaQuery).getResultList();			
		}
		catch (Exception oException)
		{
			m_oLogger.error("populateFilterObjectData - oException"+ oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return m_arrGenericDataList;
	}
	
	public static long getRowCount (GenericData  oGenericData)
	{
		return oGenericData.getRowCount(oGenericData);
	}
 
	public static Object generateXML(Object oResult)
    {
	    try
	    {
	    	((GenericData)oResult).setXMLData(((GenericData)oResult).generateXML ());
	    }
	    catch (Exception oException)
	    {
	    	
	    }
	    return oResult;
    }
	
	public static BufferedImage getBufferedImage (Blob oBlob)
	{
		m_oLogger.info ("getBufferedImage");
		m_oLogger.debug ("getBufferedImage - oBlob [IN] : " +oBlob);
		BufferedImage oBufferedImage = null;
		try 
		{
			int nBlobLength = (int) oBlob.length ();
			byte [] blobAsBytes = oBlob.getBytes (1, nBlobLength); 
			oBufferedImage = ImageIO.read ( new ByteArrayInputStream (blobAsBytes)); 
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getBufferedImage - oException" +oException);
		}
		return oBufferedImage;
	}
	
	public static boolean isValidUser(UserInformationData oUserData)
    {
		m_oLogger.debug("isValidUser - oUserData.getM_nUserId() :" + oUserData.getM_nUserId());
		m_oLogger.debug("isValidUser - oUserData.getM_nUID() :" + oUserData.getM_nUID());
		boolean bIsValidUser = false;
		try
		{
			long nUID = oUserData.getM_nUID();
			oUserData = (UserInformationData)populateObject(oUserData);
			bIsValidUser = oUserData != null && oUserData.getM_nUID() == nUID;
		}
		catch (Exception oException)
		{
			m_oLogger.error("isValidUser - oException : " + oException);
		}
	    return bIsValidUser;
    }
	

	public Criteria prepareCustomCriteria(Criteria oCriteria, T oGenericData) throws RuntimeException
	{
		throw new RuntimeException ("prepareCustomCriteria must be implemented in derived class!");
	}
	
	public static String encryptPassword (String strPassword) throws SecurityException
	{
		m_oLogger.info ("encryptPassword");
		m_oLogger.debug ("encryptPassword - strPassword [IN] : " + strPassword);
		String strEncryptedPassword = "";
		try
		{
			DigestSHA3 oMDEncrypt = new DigestSHA3(512);
			oMDEncrypt.update (strPassword.getBytes (), 0, strPassword.length ());
			strEncryptedPassword = new BigInteger (1, oMDEncrypt.digest())
			        .toString (16);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("encryptPassword - oException : " +oException);
		}
		m_oLogger.debug ("encryptPassword - strEncryptedPassword [OUT] : " +strEncryptedPassword);
		return strEncryptedPassword;
	}
}