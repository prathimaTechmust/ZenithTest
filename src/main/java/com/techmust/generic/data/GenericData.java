package com.techmust.generic.data;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.constants.Constants;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.listener.ITradeMustEventListener;
import com.techmust.generic.util.GenericUtil;
import com.techmust.generic.util.HibernateUtil;
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.academicyear.AcademicYear;
import com.techmust.scholarshipmanagement.scholarshipdetails.zenithscholarshipstatus.ZenithScholarshipDetails;
import com.techmust.scholarshipmanagement.sholarshipaccounts.StudentScholarshipAccount;
import com.techmust.scholarshipmanagement.student.StudentInformationData;
import com.techmust.scholarshipmanagement.studentdocuments.StudentDocuments;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.utils.Utils;

public abstract class GenericData implements IGenericData, Serializable
{
	private static final long serialVersionUID = 1L;
	public static Logger m_oLogger = Logger.getLogger(GenericData.class);
	@Transient
	private String m_strXMLData;
	@Transient
	private GenericUtil m_oGenericUtils;

	@SuppressWarnings({ "unchecked", "rawtypes" })
    List<ITradeMustEventListener> m_arrListeners = new ArrayList<ITradeMustEventListener>();

	public GenericData ()
	{
		m_oGenericUtils = new GenericUtil ();
//		addListeners();
	}
	
	
	public abstract Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oBuilder);
	protected abstract Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) ;
	protected abstract Predicate listCriteria (CriteriaBuilder oCriteriaBuilder, Root<GenericData> root);
	public abstract EntityManager _getEntityManager ();

	public abstract String generateXML ();
	
	public abstract GenericData getInstanceData (String strXML, UserInformationData oCredentials) throws Exception;
	
	public void addListeners ()
	{
		HibernateUtil.addListeners (this);
	}
	
	public <T> void addListener (ITradeMustEventListener<T> tEventListener)
	{
		m_arrListeners.add(tEventListener);
	}
	
	public void notifyEventListeners (String strEventName)
	{
		for (int nIndex = 0; nIndex < m_arrListeners.size(); nIndex++)
		{
			m_arrListeners.get(nIndex).handle(strEventName, this);
		}
	}
	
	public boolean deleteObject () throws Exception
	{ 
		m_oLogger.info("deleteObject");
		boolean bIsDeleted  = false;
		EntityManager oEntityManager =  _getEntityManager();
		try 
		{
			oEntityManager.getTransaction().begin();
			GenericData oData = (GenericData) GenericIDataProcessor.populateObject (this);
			if(oData != null)
			{
			oEntityManager.remove( oEntityManager.contains(oData) ? oData : oEntityManager.merge(oData) );
			oEntityManager.getTransaction( ).commit( );
			bIsDeleted = true;
			}
/**
 			oSession.delete(this);
 			oTransaction.commit();
			bIsDeleted  = oTransaction.getStatus() == TransactionStatus.COMMITTED; //oTransaction.wasCommitted();
	//		oSession.flush();  // commented this line on 1/10/2019 ny Madhusudhan as this was causing javax.persistence.TransactionRequiredException: no transaction is in progress
 */
		} 
		catch (ConstraintViolationException oException)
		{
			m_oLogger.error("deleteObject - oException : " +oException);
			String strConstrainName = oException.getConstraintName();
			strConstrainName = strConstrainName;
		}
		catch (Exception oException) 
		{
			m_oLogger.error("deleteObject - oException : " +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("deleteObject - bIsDeleted  [OUT] :" +bIsDeleted );
		return bIsDeleted ;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<GenericData> list (HashMap<String, String> arrOrderBy) throws Exception
	{
		m_oLogger.info("list");
		Set<String>arrKeys = arrOrderBy.keySet();
		Iterator<String> oIterator = arrKeys.iterator();
		for(Map.Entry<String, String> oEntry:arrOrderBy.entrySet())
		{
			String strColumn = oEntry.getKey();
			String strOrderBy = oEntry.getValue();
			m_oLogger.debug("list - strColumn [IN] : " +strColumn);
			m_oLogger.debug("list - strOrderBy [IN] : " +strOrderBy);
		}
		EntityManager oEntityManager =  _getEntityManager();
		ArrayList<GenericData> arrGenericData = new ArrayList<GenericData> ();
		try
		{
 			CriteriaBuilder oBuilder = oEntityManager.getCriteriaBuilder();
 			Class<GenericData> oClass = (Class<GenericData>) this.getClass();
			CriteriaQuery<GenericData> oCriteria = oBuilder.createQuery(oClass);
			Root<GenericData> oEntity = oCriteria.from(oClass );
			oCriteria.select( oEntity );
			oCriteria.where(listCriteria(oBuilder, oEntity));
			if(arrOrderBy.size() > 0)
				oCriteria.orderBy(addOrder(oBuilder, oEntity, arrOrderBy));
			arrGenericData = (ArrayList<GenericData>) oEntityManager.createQuery( oCriteria ).getResultList();
		}
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException :" +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("list - arrGenericData [OUT] :" +arrGenericData);
		return  arrGenericData;
	}
	
	public long getRowCount (GenericData  oGenericData)
	{
		long nCount = 0;
		EntityManager oEntityManager = _getEntityManager();
		try
		{
 			CriteriaBuilder builder = oEntityManager.getCriteriaBuilder();
 			Class<GenericData> oClass = (Class<GenericData>) this.getClass();
			CriteriaQuery<GenericData> oCriteria = builder.createQuery(oClass);
			Root<GenericData> root = oCriteria.from(oClass );
			oCriteria.select( root );
			nCount = oEntityManager.createQuery( oCriteria ).getResultList().size();
		}
		catch (Exception oException) 
		{
			m_oLogger.error("getRowCount - oException ::" +oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return nCount;
	}
	
	public ArrayList<GenericData> listCustomData (GenericIDataProcessor oProcessor) throws Exception
	{
		m_oLogger.info("listCustomData");
		EntityManager oEntityManager = _getEntityManager();
		ArrayList<GenericData> arrGenericData = new ArrayList<GenericData> ();
		try
		{
 			CriteriaBuilder builder = oEntityManager.getCriteriaBuilder();
 			Class<GenericData> oClass = (Class<GenericData>) this.getClass();
			CriteriaQuery<GenericData> oCriteria = builder.createQuery(oClass);
			Root<GenericData> root = oCriteria.from(oClass );
			oCriteria.select( root );
//			oCriteria = oProcessor.prepareCustomCriteria (oCriteria, this);
			arrGenericData = (ArrayList<GenericData>) oEntityManager.createQuery( oCriteria ).getResultList();

		}
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException :" +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("list - arrGenericData [OUT] :" +arrGenericData);
		return  arrGenericData;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<GenericData> list (HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception
	{
		m_oLogger.info("list");
		Set<String>arrKeys = arrOrderBy.keySet();
		Iterator<String> oIterator = arrKeys.iterator();
		for(Map.Entry<String, String> oEntry:arrOrderBy.entrySet())
		{
			String strColumn = oEntry.getKey();
			String strOrderBy = oEntry.getValue();
			m_oLogger.debug("list - strColumn [IN] : " +strColumn);
			m_oLogger.debug("list - strOrderBy [IN] : " +strOrderBy);
		}
		EntityManager oEntityManager = _getEntityManager();
		ArrayList<GenericData> arrGenericData = new ArrayList<GenericData> ();
		try
		{
 			CriteriaBuilder builder = oEntityManager.getCriteriaBuilder();
 			Class<GenericData> oClass = (Class<GenericData>) this.getClass();
			CriteriaQuery<GenericData> oCriteria = builder.createQuery(oClass);
			Root<GenericData> root = oCriteria.from(oClass );
			CriteriaQuery<GenericData> select = oCriteria.select(root);
			TypedQuery<GenericData> typedQuery = oEntityManager.createQuery(select);
			//typedQuery.setFirstResult(nPageNumber * nPageSize);
			//typedQuery.setMaxResults(nPageSize);
			typedQuery.setMaxResults(nPageNumber * nPageSize);
			oCriteria.where(listCriteria(builder, root));
			arrGenericData = (ArrayList<GenericData>) oEntityManager.createQuery( oCriteria ).getResultList();
		}
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException :" +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("list - arrGenericData [OUT] :" +arrGenericData);
		return  arrGenericData;
	}
	
	public boolean saveObject () throws Exception
	{ 
		m_oLogger.info("saveObject");
		m_oLogger.info("saveObject");
		boolean bIsSaved = false;
		EntityManager oEntityManager = _getEntityManager();
		try 
		{
			oEntityManager.getTransaction().begin();
			oEntityManager.persist( this );
			oEntityManager.getTransaction( ).commit( );
			bIsSaved = true;
			notifyEventListeners(ITradeMustEventListener.kNew);		
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("saveObject - oException :" +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("saveObject - bIsSaved [OUT] :" +bIsSaved);
		return bIsSaved;
	}
	
	public boolean saveOrUpdateObject () throws Exception
	{ 
		m_oLogger.info("saveOrUpdateObject");
		boolean bIsSaved = false;
//		Session oSession =  HibernateUtil.getSession();
		EntityManager oEntityManager = _getEntityManager();
		try 
		{
	/*
	 		Transaction oTransaction = oSession.beginTransaction();
			oSession.saveOrUpdate(this);
			oTransaction.commit();
			bIsSaved = oTransaction.getStatus() == TransactionStatus.COMMITTED; //oTransaction.wasCommitted();
//			oSession.flush();  // commented this line on 1/10/2019 ny Madhusudhan as this was causing javax.persistence.TransactionRequiredException: no transaction is in progress
  */
			oEntityManager.getTransaction( ).begin( );
		    GenericData oData = oEntityManager.find( GenericData.class, this.getClass());
		    if(oData == null)
		    	saveObject ();
		    else
		    	oEntityManager.getTransaction( ).commit( );
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("saveOrUpdateObject - oException :" +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("saveOrUpdateObject - bIsSaved [OUT] :" +bIsSaved);
		return bIsSaved;
	}
	
	public boolean updateObject () throws Exception
	{
		m_oLogger.info("updateObject");
		boolean bIsUpdated = false;
		
//		Session oSession = HibernateUtil.getSession();
		EntityManager oEntityManager = _getEntityManager();
		try 
		{
			Session oSession = oEntityManager.unwrap(org.hibernate.Session.class);
 			Transaction oTransaction = oSession.beginTransaction ();
			oSession.update(this);
			oTransaction.commit();
			bIsUpdated = true;//oTransaction.getStatus() == TransactionStatus.COMMITTED; //oTransaction.wasCommitted();
//			oSession.flush();  // commented this line on 1/10/2019 ny Madhusudhan as this was causing javax.persistence.TransactionRequiredException: no transaction is in progress

/*			oEntityManager.getTransaction( ).begin( );
//		    GenericData oData = oEntityManager.find( this.getClass(), this);
//		    if(oData != null)
//			oEntityManager.merge(this);
		    oEntityManager.getTransaction( ).commit( );
 */  
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("updateObject - oException : " +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("updateObject - bIsUpdated [OUT] : " +bIsUpdated);
		return bIsUpdated;
	}
	
	public void setXMLData (String strXMLData)
    {
	    m_strXMLData = strXMLData;
    }

	public String getXMLData ()
    {
	    return m_strXMLData;
    }
	
	public BufferedImage getBufferedImage (Blob oBlob)
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
	
	protected Element addChild (Document oXmlDocument, Element oRootElement, String strTagName, int nTagValue) 
	{
		Element oElement = oXmlDocument.createElement(strTagName);
		oElement.appendChild(oXmlDocument.createTextNode(String.valueOf(nTagValue)));
		oRootElement.appendChild(oElement);
		return oElement;
	}
	
	protected Element addChild (Document oXmlDocument, Element oRootElement, String strTagName, float nTagValue) 
	{
		Element oElement = oXmlDocument.createElement(strTagName);
		oElement.appendChild(oXmlDocument.createTextNode(String.valueOf(nTagValue)));
		oRootElement.appendChild(oElement);
		return oElement;
	}
	
	protected Element addChild (Document oXmlDocument, Element oRootElement, String strTagName, long nTagValue) 
	{
		Element oElement = oXmlDocument.createElement(strTagName);
		oElement.appendChild(oXmlDocument.createTextNode(String.valueOf(nTagValue)));
		oRootElement.appendChild(oElement);
		return oElement;
	}
	
	protected Element addChild (Document oXmlDocument, Element oRootElement, String strTagName, String strTagValue) 
	{
		Element oElement = oXmlDocument.createElement(strTagName);
		oElement.appendChild(oXmlDocument.createTextNode(strTagValue != null ? strTagValue : ""));
		oRootElement.appendChild(oElement);
		return oElement;
	}
	
	protected Element createRootElement (Document oXmlDocument, String strRoot) 
	{
		Element oRootElement = m_oGenericUtils.createRootElement(oXmlDocument, strRoot);
		return oRootElement;
	}
	
	protected String getXmlString (Document oXmlDocument)
	{
		m_oLogger.info ("getXmlString");
		m_oLogger.debug ("getXmlString - oXmlDocument [IN] : " + oXmlDocument);
		String strXml = "";
		try 
		{
			strXml = m_oGenericUtils.getXmlString(oXmlDocument) ;
		}
		catch (Exception oException) 
		{
			m_oLogger.error("getXmlString - oException : " + oException);
		}
		m_oLogger.debug("getXmlString - strXml [OUT] : " + strXml);
		return strXml;
	}
	
	protected void addSortByCondition (Criteria oCriteria, String strColumn, String strOrder, String strDefaultColumn)
	{
/*
 			if (strOrder == null || strColumn == null)
				oCriteria.addOrder(Order.desc(strDefaultColumn));
			else if (strOrder != null && strOrder.contains("asc"))
				oCriteria.addOrder(Order.asc(strColumn));
			else if (strOrder != null && strOrder.contains("desc"))
			oCriteria.addOrder(Order.desc(strColumn));
*/			
	}
	
	protected Document createNewXMLDocument ()
	{
		Document oXmlDoc = null;
		try
		{
			oXmlDoc = m_oGenericUtils.createNewXMLDocument();
		}
		catch (Exception oException)
		{
			m_oLogger.error("createNewXMLDocument - oException : " +oException);
		}
		m_oLogger.debug("createNewXMLDocument - bIsUpdated [OUT] : " + oXmlDoc);
		return oXmlDoc;
	}
	
	protected Document getXmlDocument (String strXmlContent)
	{
		m_oLogger.info ("getXmlDocument");
		m_oLogger.debug ("getXmlDocument - strXmlpath [IN] : " + strXmlContent);
		Document oXmlDocument = null;
		try
		{
			oXmlDocument = m_oGenericUtils.getXmlDocument(strXmlContent);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getXmlDocument - oException" +oException);
		}
		m_oLogger.debug ("getXmlDocument - oXmlDocument [OUT] : " + oXmlDocument);
		return oXmlDocument;
	}

	@SuppressWarnings("unused")
    private void generateXML(ArrayList<GenericData> arrGenericData)
    {
	    for (int nIndex = 0; nIndex < arrGenericData.size(); nIndex++)
	    	arrGenericData.get(nIndex).setXMLData(arrGenericData.get(nIndex).generateXML());
    }
	
	public Node getChildNodeByName(Node oItem, String strNodeName)
	{
		Node oNode = null;
		NodeList oNodeList = oItem.getChildNodes();
		for(int nIndex = 0; nIndex < oNodeList.getLength(); nIndex ++)
		{
			if(oNodeList.item(nIndex).getNodeName().equals(strNodeName))
			{
				oNode = oNodeList.item(nIndex);
				break;
			}
		}
		return oNode;
	}

	public String getTagValue(Node oItem, String strTagName)
	{
		String strValue = "";
		NodeList oNodeList = oItem.getChildNodes();
		for(int nIndex = 0; nIndex < oNodeList.getLength(); nIndex ++)
		{
			String strNodeName = oNodeList.item(nIndex).getNodeName();
			if(strNodeName.equals(strTagName))
			{
				strValue = oNodeList.item(nIndex).getFirstChild().getNodeValue();
				break;
			}
		}
		return strValue;
	}

	protected List<Order> addOrder(CriteriaBuilder oBuilder, Root<GenericData> oEntity,	HashMap<String, String> arrOrderBy)
	{
		List<Order> arrOrder =  new  ArrayList<Order>();
		Set<String>arrKeys = arrOrderBy.keySet();
		Iterator<String> oIterator = arrKeys.iterator();
		for(Map.Entry<String, String> oEntry:arrOrderBy.entrySet())
		{
			String strColumn = oEntry.getKey();
			String strOrderBy = oEntry.getValue();
			Order oOrder = null;
			if (strOrderBy != null && strOrderBy.contains("asc"))
				oOrder = oBuilder.asc(oEntity.get(strColumn));
			else if (strOrderBy != null && strOrderBy.contains("desc"))
				oOrder = oBuilder.desc(oEntity.get(strColumn));
			if(oOrder != null)
				arrOrder.add(oOrder);
		}
		return arrOrder;
	}
	public  StudentInformationData  getStudentDetails (StudentInformationData oStudentData)
	{
		EntityManager oEntityManager = _getEntityManager();
		StudentInformationData oStudentInformationData = null;
		try 
		{			
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
	        CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
	        Root<StudentInformationData> oStudentInformationRoot = oCriteriaQuery.from(StudentInformationData.class);    
	        oCriteriaQuery.select(oStudentInformationRoot);
	        oCriteriaQuery.where(oCriteriaBuilder.equal(oStudentInformationRoot.get("m_nStudentId"), oStudentData.getM_nStudentId()));
	        oStudentInformationData = oEntityManager.createQuery(oCriteriaQuery).getSingleResult();
	        if(oStudentData.getM_nAcademicYearId() > 0)
	        	oStudentInformationData.setM_oAcademicDetails(getAcademicDetails(oStudentData));						
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
		return oStudentInformationData;
	}


	public Set<AcademicDetails> getAcademicDetails(StudentInformationData oStudentData) 
	{
		EntityManager oEntityManager = _getEntityManager();
		List<AcademicDetails> oAcademicDetails = null;
		Set<AcademicDetails> arrAcademicDetails = null;
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
	        CriteriaQuery<AcademicDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(AcademicDetails.class);
	        Root<AcademicDetails> oAcademicDetailsRoot = oCriteriaQuery.from(AcademicDetails.class);   
	        oCriteriaQuery.select(oAcademicDetailsRoot);
	        oCriteriaQuery.where(oCriteriaBuilder.equal(oAcademicDetailsRoot.get("m_oAcademicYear"), oStudentData.getM_nAcademicYearId()),
	        		oCriteriaBuilder.equal(oAcademicDetailsRoot.get("m_oStudentInformationData"), oStudentData.getM_nStudentId()));	        				
	        oAcademicDetails =  (ArrayList<AcademicDetails>) oEntityManager.createQuery(oCriteriaQuery).getResultList();
	        if(oAcademicDetails.size() > 0)
	        	oAcademicDetails = getCurrentYearActiveCheque(oAcademicDetails);
	        arrAcademicDetails = new HashSet<AcademicDetails>(oAcademicDetails);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getAcademicDetails - oException : " +oException);
			throw oException;
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return arrAcademicDetails;
	}
	
	private List<AcademicDetails> getCurrentYearActiveCheque(List<AcademicDetails> oAcademicDetails)
	{
		m_oLogger.info("get Current active Cheque");
		m_oLogger.debug("get Current active Cheque"+oAcademicDetails);
		EntityManager oEntityManager = _getEntityManager();
		AcademicDetails oDetails = oAcademicDetails.get(0);
		Set<StudentScholarshipAccount> oSetAccount = oDetails.getM_oStudentScholarshipAccount();
		try
		{
			if(!oSetAccount.isEmpty())
			{
				CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
				CriteriaQuery<StudentScholarshipAccount> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentScholarshipAccount.class);
				Root<StudentScholarshipAccount> oAccountRoot = oCriteriaQuery.from(StudentScholarshipAccount.class);
				oCriteriaQuery.select(oAccountRoot);
				oCriteriaQuery.where(oCriteriaBuilder.equal(oAccountRoot.get("m_oAcademicDetails"),oDetails.getM_nAcademicId()),
									 oCriteriaBuilder.equal(oAccountRoot.get("m_strChequeStatus"),Constants.CHEQUESTATUS));
				List<StudentScholarshipAccount> liScholarshipAccounts =  oEntityManager.createQuery(oCriteriaQuery).getResultList();
				oSetAccount = new HashSet<StudentScholarshipAccount>(liScholarshipAccounts);
				oDetails.setM_oStudentScholarshipAccount(oSetAccount);
				oAcademicDetails.add(oDetails);
			}
			
		}
		catch (Exception oException)
		{
			m_oLogger.error("get Current active Cheque"+oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oAcademicDetails;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<StudentInformationData> getStatusStudentsList(StudentInformationData oStudentInformationData)
	{
		ArrayList<StudentInformationData> arrStudentInformationData = null;
		AcademicYear oAcademicYear = new AcademicYear();
		oAcademicYear.setM_nAcademicYearId(oStudentInformationData.getM_nAcademicYearId());
		EntityManager oEntityManager = _getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
			Root<StudentInformationData> oStudentInformationDataRoot = oCriteriaQuery.from(StudentInformationData.class);
			//Join the SubEntities to Root Object
			Join<Object, Object> oZenithJoin = (Join<Object, Object>) oStudentInformationDataRoot.fetch("m_oZenithScholarshipDetails");
			Join<Object, Object> oAcademicJoin = (Join<Object, Object>) oStudentInformationDataRoot.fetch("m_oAcademicDetails");
			//Predicate List(Where Conditions)
			List<Predicate> m_arrPredicateList = new ArrayList<Predicate>();
			m_arrPredicateList.add(oCriteriaBuilder.equal(oAcademicJoin.get("m_oAcademicYear"),oStudentInformationData.getM_nAcademicYearId()));
			m_arrPredicateList.add(oCriteriaBuilder.equal(oZenithJoin.get("m_strStatus"), oStudentInformationData.getM_strStatus()));
			m_arrPredicateList.add(oCriteriaBuilder.equal(oZenithJoin.get("m_oAcademicYear"),oStudentInformationData.getM_nAcademicYearId()));
			boolean bIsSuccess = getApplicationStatus(oStudentInformationData.getM_strStatus());
			if(bIsSuccess)
			{
				//Join If Account Details Exists of Application And Get Active Cheque Details
				Join oJoin = (Join) oAcademicJoin.fetch("m_oStudentScholarshipAccount");
				m_arrPredicateList.add(oCriteriaBuilder.equal(oJoin.get("m_strChequeStatus"),Constants.CHEQUESTATUS));
			}					
			oCriteriaQuery.select(oStudentInformationDataRoot);
			oCriteriaQuery.orderBy(oCriteriaBuilder.asc(oStudentInformationDataRoot.get("m_nApplicationPriority")),oCriteriaBuilder.asc(oStudentInformationDataRoot.get("m_nUID")));
			oCriteriaQuery.where(m_arrPredicateList.toArray(new Predicate[] {}));
			TypedQuery<StudentInformationData> typedquery = oEntityManager.createQuery(oCriteriaQuery);
			typedquery.setFirstResult((oStudentInformationData.getM_oZenithHelper().getM_nPageNo()-1)*oStudentInformationData.getM_oZenithHelper().getM_nPageSize());
			typedquery.setMaxResults(oStudentInformationData.getM_oZenithHelper().getM_nPageSize());
			arrStudentInformationData = (ArrayList<StudentInformationData>) typedquery.getResultList();			
		}
		catch (Exception oException)
		{
			m_oLogger.error("getStatusStudentsList - oException : " +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}		
		return arrStudentInformationData;		
	}
	
	private boolean getApplicationStatus(String strStatus)
	{
		boolean bIsSuccess = false;
		try
		{
			if(Constants.CHEQUEPREPARED.equals(strStatus))
			{
				bIsSuccess = true;
			}
			else if(Constants.CHEQUEDISBURSED.equals(strStatus))
			{
				bIsSuccess = true;
			}
			else if(Constants.CHEQUECLAIMED.equals(strStatus))
			{
				bIsSuccess = true;
			}
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getApplicationStatus - oException"+oException);
		}
		return bIsSuccess;
	}
// Student Application Status Update Functions
	
	public boolean updateStudentApplicationVerifiedStatus(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		boolean IsUpdate = false;
		EntityManager oEntityManager = _getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			Root<ZenithScholarshipDetails> oZenithStatusRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			oCriteriaQuery.select(oZenithStatusRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithStatusRoot.get("m_oStudentInformationData"),oZenithScholarshipDetails.getM_nStudentId()));
			ArrayList<ZenithScholarshipDetails> arrZenithScholarshipDetails =  (ArrayList<ZenithScholarshipDetails>) oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(arrZenithScholarshipDetails.size() > 0)
			{
				ZenithScholarshipDetails oZenith = arrZenithScholarshipDetails.get(0);
				oZenith.setM_strStatus(Constants.STUDENTVERIFIED);
				oZenith.setM_strImage(oZenithScholarshipDetails.getM_strImage());
				oZenith.setM_oChequeInFavourOf(oZenithScholarshipDetails.getM_oChequeInFavourOf());
				oZenith.setM_strPaymentType(oZenithScholarshipDetails.getM_strPaymentType());
				oZenith.setM_strVerifyRemarks(oZenithScholarshipDetails.getM_strVerifyRemarks());
				oZenith.setM_oUserUpdatedBy(oZenithScholarshipDetails.getM_oUserUpdatedBy());
				oZenith.setM_dUpdatedOn(Calendar.getInstance().getTime());
				IsUpdate = oZenith.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateStudentApplicationVerifiedStatus - oException : " +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}		
		return IsUpdate;		
	}
	
	public boolean updateStudentApplicationApprovedStatus(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		boolean IsUpdate = false;
		EntityManager oEntityManager = _getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			Root<ZenithScholarshipDetails> oZenithStatusRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			oCriteriaQuery.select(oZenithStatusRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithStatusRoot.get("m_oStudentInformationData"),oZenithScholarshipDetails.getM_nStudentId()));
			ArrayList<ZenithScholarshipDetails> arrZenithScholarshipDetails =  (ArrayList<ZenithScholarshipDetails>) oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(arrZenithScholarshipDetails.size() > 0)
			{
				ZenithScholarshipDetails oZenith = arrZenithScholarshipDetails.get(0);
				oZenith.setM_strStatus(Constants.STUDENTAPPROVED);
				oZenith.setM_fApprovedAmount(oZenithScholarshipDetails.getM_fApprovedAmount());
				oZenith.setM_oApprovedBy(oZenithScholarshipDetails.getM_oApprovedBy());
				oZenith.setM_oUserUpdatedBy(oZenithScholarshipDetails.getM_oApprovedBy());
				oZenith.setM_dUpdatedOn(Calendar.getInstance().getTime());
				oZenith.setM_dApprovedDate(Calendar.getInstance().getTime());
				IsUpdate = oZenith.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateStudentApplicationVerifiedStatus - oException : " +oException);
			throw oException;
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}		
		return IsUpdate;
	}
	
	public boolean doesCourseHaveAcademic(int nCourseId)
	{
		boolean IsHaveAcademic = false;
		EntityManager oEntityManager = _getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<AcademicDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(AcademicDetails.class);
			Root<AcademicDetails> oAcademicDetails = oCriteriaQuery.from(AcademicDetails.class);
			oCriteriaQuery.select(oAcademicDetails);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oAcademicDetails.get("m_oCourseInformationData"),nCourseId));
			List<AcademicDetails> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(list.size() > 0)
				IsHaveAcademic = true;
		}
		catch (Exception oException) 
		{
			m_oLogger.error("doesCourseHaveAcademic - oException : " +oException);
			throw oException;
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}		
		return IsHaveAcademic;		
	}
	public boolean doesInstitutionHaveAcademic(int nInstitutionId)
	{
		boolean IsHaveAcademic = false;
		EntityManager oEntityManager = _getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<AcademicDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(AcademicDetails.class);
			Root<AcademicDetails> oAcademicDetails = oCriteriaQuery.from(AcademicDetails.class);
			oCriteriaQuery.select(oAcademicDetails);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oAcademicDetails.get("m_oInstitutionInformationData"),nInstitutionId));
			List<AcademicDetails> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(list.size() > 0)
				IsHaveAcademic = true;
		} 
		catch (Exception oException)
		{
			m_oLogger.error("doesCourseHaveAcademic - oException : " +oException);
			throw oException;
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}		
		return IsHaveAcademic;	
	}
	
	public boolean updateStudentApplicationRejectedStatus(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		boolean bIsUpdate = false;
		EntityManager oEntityManager = _getEntityManager();
		try 
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			Root<ZenithScholarshipDetails> oZenithRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			oCriteriaQuery.select(oZenithRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithRoot.get("m_oStudentInformationData"),oZenithScholarshipDetails.getM_nStudentId()));
			List<ZenithScholarshipDetails> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(list.size() > 0)
			{
				ZenithScholarshipDetails oDetails = list.get(0);
				oDetails.setM_strStatus(Constants.STUDENTREJECTED);
				oDetails.setM_strStudentRemarks(oZenithScholarshipDetails.getM_strStudentRemarks());
				bIsUpdate = oDetails.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateRejectStatus - oException" + oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return bIsUpdate;		
	}
	
	public boolean disburseCheque (ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		boolean bIsChequeIssue = false;
		EntityManager oEntityManager = _getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			Root<ZenithScholarshipDetails> oZenithRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			oCriteriaQuery.select(oZenithRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithRoot.get("m_oStudentInformationData"),oZenithScholarshipDetails.getM_nStudentId()));
			List<ZenithScholarshipDetails> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(list.size() > 0)
			{
				ZenithScholarshipDetails oDetails = list.get(0);
				//String strLogedUser = Utils.getLoginUser();
				//oDetails.setM_strChequeDisburseBy(strLogedUser);
				oDetails.setM_strReceiverName(oZenithScholarshipDetails.getM_strReceiverName());
				oDetails.setM_strReceiverContactNumber(oZenithScholarshipDetails.getM_strReceiverContactNumber());				
				oDetails.setM_dChequeIssueDate(oZenithScholarshipDetails.getM_dChequeIssueDate());
				oDetails.setM_oChequeDisburseBy(oZenithScholarshipDetails.getM_oChequeDisburseBy());
				oDetails.setM_oUserUpdatedBy(oZenithScholarshipDetails.getM_oChequeDisburseBy());
				oDetails.setM_dUpdatedOn(Calendar.getInstance().getTime());
				oDetails.setM_strStatus(Constants.CHEQUEDISBURSED);
				bIsChequeIssue = oDetails.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("IssueCheque - oException"+oException);
			throw oException;
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return bIsChequeIssue;		
	}
	
	public boolean applicationStatusUpdate(ZenithScholarshipDetails oZenithScholarshipDetails) throws Exception
	{
		boolean bIsStatusUpdate = false;
		EntityManager oEntityManager = _getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			Root<ZenithScholarshipDetails> oZenithRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			oCriteriaQuery.select(oZenithRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithRoot.get("m_oStudentInformationData"),oZenithScholarshipDetails.getM_nStudentId()));
			List<ZenithScholarshipDetails> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(list.size() > 0)
			{
				ZenithScholarshipDetails oDetails = list.get(0);
				oDetails.setM_strStatus(Constants.CHEQUEPREPARED);
				bIsStatusUpdate = oDetails.updateObject();				
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("chequeprepared - oException"+oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return bIsStatusUpdate;
		
	}	

	public boolean counselingListApplication(ZenithScholarshipDetails oZenithData) throws Exception
	{
		boolean bIsStatusReVerify = false;
		EntityManager oEntityManager = _getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			Root<ZenithScholarshipDetails> oZenithRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			oCriteriaQuery.select(oZenithRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithRoot.get("m_oStudentInformationData"),oZenithData.getM_nStudentId()));
			List<ZenithScholarshipDetails> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(list.size() > 0)
			{

				ZenithScholarshipDetails oDetails = list.get(0);
				oDetails.setM_strStatus(Constants.APPLICATIONCOUNSELING);
				oDetails.setM_strStudentRemarks(oZenithData.getM_strStudentRemarks());
				oDetails.setM_dCounselingDate(oZenithData.getM_dCounselingDate());
				bIsStatusReVerify = oDetails.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("applicationReverify - oException"+oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return bIsStatusReVerify;		
	}
	
	public boolean reVerifyStudentApplication(ZenithScholarshipDetails oZenithData) throws Exception
	{
		boolean bIsStatusReVerify = false;
		EntityManager oEntityManager = _getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			Root<ZenithScholarshipDetails> oZenithRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			oCriteriaQuery.select(oZenithRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithRoot.get("m_oStudentInformationData"),oZenithData.getM_nStudentId()));
			List<ZenithScholarshipDetails> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(list.size() > 0)
			{

				ZenithScholarshipDetails oDetails = list.get(0);
				oDetails.setM_strStatus(Constants.APPLICATIONREVERIFICATION);
				//oDetails.setM_strStudentRemarks(oZenithData.getM_strStudentRemarks());
				bIsStatusReVerify = oDetails.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("applicationReverify - oException"+oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return bIsStatusReVerify;		
	}

	public boolean approveCounselingStudentApplication(ZenithScholarshipDetails oZenithData) throws Exception
	{
		boolean bIsStatusReVerify = false;
		EntityManager oEntityManager = _getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			Root<ZenithScholarshipDetails> oZenithRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			oCriteriaQuery.select(oZenithRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithRoot.get("m_oStudentInformationData"),oZenithData.getM_nStudentId()));
			List<ZenithScholarshipDetails> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(list.size() > 0)
			{

				ZenithScholarshipDetails oDetails = list.get(0);
				oDetails.setM_strStatus(Constants.COUNSELINGSTUDENTVERIFIED);
				bIsStatusReVerify = oDetails.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("applicationReverify - oException"+oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return bIsStatusReVerify;	
	}
	 public boolean reIssueCheckDetails(ZenithScholarshipDetails oZenithData) throws Exception
	  {
		 boolean bIsStatusReVerify = false;
		 EntityManager oEntityManager = _getEntityManager();
		 try
		 {
			 CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			 CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			 Root<ZenithScholarshipDetails> oZenithRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			 oCriteriaQuery.select(oZenithRoot);
			 oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithRoot.get("m_oStudentInformationData"),oZenithData.getM_nStudentId()));
			 List<ZenithScholarshipDetails> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			 if(list.size() > 0)
			 {
				 ZenithScholarshipDetails oDetails = list.get(0);
				 oDetails.setM_strStatus(Constants.REISSUECHEQUE);
				 oDetails.setM_strChequeRemark(oZenithData.getM_strChequeRemark());
				 bIsStatusReVerify = oDetails.updateObject();
			 }
			 if(bIsStatusReVerify)
				 changeChequeStatus(oZenithData.getM_nAcademicId(),oZenithData.getM_strChequeRemark());
		 }
		  catch (Exception oException)
		 {
			 m_oLogger.error("CHEQUEDREISSUED - oException"+oException); 
			 throw oException;
		}
			finally 
			{
				oEntityManager.close();
				HibernateUtil.removeConnection();
			 
		    }
		return bIsStatusReVerify;
	  }

	private void changeChequeStatus(int nAcademicId,String strChequeRemark)
	{
		m_oLogger.info("Change Cheque Status");
		m_oLogger.debug("Change Cheque Status - AcademicId"+ nAcademicId);
		EntityManager oEntityManager = _getEntityManager();
		StudentScholarshipAccount oAccount = new StudentScholarshipAccount();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<StudentScholarshipAccount> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentScholarshipAccount.class);
			Root<StudentScholarshipAccount> oAccountRoot = oCriteriaQuery.from(StudentScholarshipAccount.class);
			oCriteriaQuery.select(oAccountRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oAccountRoot.get("m_oAcademicDetails"),nAcademicId),
								 oCriteriaBuilder.equal(oAccountRoot.get("m_strChequeStatus"),Constants.CHEQUESTATUS));
			List<StudentScholarshipAccount> listAccount = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(listAccount.size() > 0)
			{
				oAccount = listAccount.get(0);
				oAccount.setM_strChequeStatus("InActive");
				oAccount.setM_strChequeRemarks(strChequeRemark);
				oAccount.setM_bChequeValid(false);
				oAccount.updateObject();
			}			
		}
		catch (Exception oException)
		{
			m_oLogger.error("Change Cheque Status"+ oException);
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
	}


	public boolean checkChequePrepared(Set<AcademicDetails> oAcademicDetails)
	{
		boolean bIsCheckPrepared = false;
		EntityManager oEntityManager = _getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder =  oEntityManager.getCriteriaBuilder();
			CriteriaQuery<AcademicDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(AcademicDetails.class);
			Root<AcademicDetails> oRootAcademicDetails = oCriteriaQuery.from(AcademicDetails.class);
			Join<AcademicDetails,StudentScholarshipAccount> oJoinTable = oRootAcademicDetails.join("m_oStudentScholarshipAccount",JoinType.INNER);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oJoinTable.get("m_oStudentScholarshipAccount"),((AcademicDetails) oAcademicDetails).getM_nAcademicId()));
			TypedQuery<AcademicDetails> typedquery = oEntityManager.createQuery(oCriteriaQuery);
			ArrayList<AcademicDetails> arrAcademicInformationData = (ArrayList<AcademicDetails>) typedquery.getResultList();
			if(arrAcademicInformationData.size() > 0)
				bIsCheckPrepared = true;
		} 
		catch (Exception oException)
		{			
			m_oLogger.error("chequePrepared - oException"+oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return bIsCheckPrepared;
	}
	
	public StudentInformationData getSearchUIDStudentData(StudentInformationData oStudentData)
	{
		EntityManager oEntityManager = _getEntityManager();
		StudentInformationData oStudentInformationData = null;
		try 
		{			
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
	        CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
	        Root<StudentInformationData> oStudentInformationRoot = oCriteriaQuery.from(StudentInformationData.class);    
	        oCriteriaQuery.select(oStudentInformationRoot);
	        oCriteriaQuery.where(oCriteriaBuilder.equal(oStudentInformationRoot.get("m_nUID"), oStudentData.getM_nUID()));
	        List<StudentInformationData> arrStudentList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
	        if(arrStudentList.size() > 0)
	        {
		        oStudentInformationData = arrStudentList.get(0);
		        oStudentInformationData.setM_nAcademicYearId(oStudentData.getM_nAcademicYearId());
		        oStudentInformationData.setM_strStatus(oStudentData.getM_strStatus());
		        oStudentInformationData.setM_oAcademicDetails(getAcademicDetails(oStudentInformationData));
		        oStudentInformationData.setM_oZenithScholarshipDetails(getSearchStudentUID(oStudentInformationData));
	        }						
		}
		catch (Exception oException)
		{
			m_oLogger.error("getSearchUIDStudentData - oException : " +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}		
		return oStudentInformationData;		
	}

	private Set<ZenithScholarshipDetails> getSearchStudentUID(StudentInformationData oStudentData) 
	{
		EntityManager oEntityManager = _getEntityManager();
		ArrayList<ZenithScholarshipDetails> arrZenithScholarshipDetails = null;
		Set<ZenithScholarshipDetails> oZenithScholarshipDetails = null;
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
	        CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
	        Root<ZenithScholarshipDetails> oZenithScholarshipDetailsRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);   
	        oCriteriaQuery.select(oZenithScholarshipDetailsRoot);
	        oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithScholarshipDetailsRoot.get("m_strStatus"), oStudentData.getM_strStatus()),
	        		oCriteriaBuilder.equal(oZenithScholarshipDetailsRoot.get("m_oStudentInformationData"), oStudentData.getM_nStudentId()));	        				
	        arrZenithScholarshipDetails =  (ArrayList<ZenithScholarshipDetails>) oEntityManager.createQuery(oCriteriaQuery).getResultList();
	        oZenithScholarshipDetails = new HashSet<ZenithScholarshipDetails>(arrZenithScholarshipDetails);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getSearchStudentUID - oException : " +oException);
			throw oException;
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oZenithScholarshipDetails;
	}
	
	public boolean claimCheque(ZenithScholarshipDetails oZenithData) throws Exception
	{
		boolean bIsChequeClaimed = false;
		EntityManager oEntityManager = _getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			Root<ZenithScholarshipDetails> oZenithRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			oCriteriaQuery.select(oZenithRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithRoot.get("m_oStudentInformationData"),oZenithData.getM_nStudentId()));
			List<ZenithScholarshipDetails> list = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			if(list.size() > 0)
			{
				ZenithScholarshipDetails oDetails = list.get(0);
				oDetails.setM_strStatus(Constants.CHEQUECLAIMED);
				oDetails.setM_dClaimedDate(oZenithData.getM_dClaimedDate());
				bIsChequeClaimed = oDetails.updateObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("Claimed Cheque - oException"+oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return bIsChequeClaimed;		
	}
	
	public StudentDocuments getStudentUploadDocuments(AcademicDetails oAcademicDetails)
	{
		List<StudentDocuments> m_arrStudentDocuments = null;
		StudentDocuments oStudentDocuments = new StudentDocuments();
		EntityManager oEntityManager = _getEntityManager();
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<AcademicDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(AcademicDetails.class);
			Root<AcademicDetails> oRoot = oCriteriaQuery.from(AcademicDetails.class);
			oCriteriaQuery.select(oRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oRoot.get("m_nAcademicId"),oAcademicDetails.getM_nAcademicId()));
			List<AcademicDetails> documentList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			AcademicDetails oAcademicData = documentList.get(0);
			if(oAcademicData.getM_arrStudentDocuments().size() > 0)
			{
				m_arrStudentDocuments = documentList.get(0).getM_arrStudentDocuments();
				oStudentDocuments = m_arrStudentDocuments.get(0);
			}
			oStudentDocuments.setM_strVerifyScanDocument(getStudentVerifiedDocument(oAcademicData.getM_oStudentInformationData()));
		} 
		catch (Exception oException)
		{
			m_oLogger.error("get Documents - oException" + oException);
			throw oException;
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return oStudentDocuments;		
	}
	
	private String getStudentVerifiedDocument(StudentInformationData oData)
	{
		EntityManager oEntityManager = _getEntityManager();
		String strVerifiedDocument = "";
		try
		{
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
			CriteriaQuery<ZenithScholarshipDetails> oCriteriaQuery = oCriteriaBuilder.createQuery(ZenithScholarshipDetails.class);
			Root<ZenithScholarshipDetails> oZenithRoot = oCriteriaQuery.from(ZenithScholarshipDetails.class);
			oCriteriaQuery.select(oZenithRoot);
			oCriteriaQuery.where(oCriteriaBuilder.equal(oZenithRoot.get("m_oStudentInformationData"),oData.getM_nStudentId()));
			List<ZenithScholarshipDetails> documentList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
			strVerifiedDocument = documentList.get(0).getM_strImage();
		}
		catch (Exception oException)
		{
			m_oLogger.error("getStudentVerifiedDocument - oException" + oException);
		}
		finally 
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		return strVerifiedDocument;		
	}


	public StudentInformationData getUIDAndAadharFormData(StudentInformationData oData)
	{
		EntityManager oEntityManager = _getEntityManager();
		StudentInformationData oStudentInformationData = null;
		try 
		{			
			CriteriaBuilder oCriteriaBuilder = oEntityManager.getCriteriaBuilder();
	        CriteriaQuery<StudentInformationData> oCriteriaQuery = oCriteriaBuilder.createQuery(StudentInformationData.class);
	        Root<StudentInformationData> oStudentInformationRoot = oCriteriaQuery.from(StudentInformationData.class);
	        List<Predicate> m_arrPredicateList = new ArrayList<Predicate>();
			if(oData.getM_nUID() > 0)
				m_arrPredicateList.add(oCriteriaBuilder.equal(oStudentInformationRoot.get("m_nUID"), oData.getM_nUID()));
			else
				m_arrPredicateList.add(oCriteriaBuilder.equal(oStudentInformationRoot.get("m_nStudentAadharNumber"), oData.getM_nStudentAadharNumber()));
	        oCriteriaQuery.select(oStudentInformationRoot).where(m_arrPredicateList.toArray(new Predicate[]{}));
	        List<StudentInformationData> arrStudentList = oEntityManager.createQuery(oCriteriaQuery).getResultList();
	        if(arrStudentList.size() > 0)
	        {
		        oStudentInformationData = arrStudentList.get(0);
		        oStudentInformationData.setM_nAcademicYearId(oData.getM_nAcademicYearId());
		        oStudentInformationData.setM_oAcademicDetails(getAcademicDetails(oStudentInformationData));
	        }						
		}
		catch (Exception oException)
		{
			m_oLogger.error("getUIDAndAadharFormData - oException : " +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}		
		return oStudentInformationData;		
	}
}


