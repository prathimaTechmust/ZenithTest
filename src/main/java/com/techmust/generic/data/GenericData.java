package com.techmust.generic.data;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.listener.ITradeMustEventListener;
import com.techmust.generic.util.GenericUtil;
import com.techmust.generic.util.HibernateUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;

public abstract class GenericData implements IGenericData, Serializable
{
	private static final long serialVersionUID = 1L;
	public static Logger m_oLogger = Logger.getLogger(GenericData.class);
	@Transient
	private String m_strXMLData;
	@Transient
	private GenericUtil m_oGenericUtils;
	
	@SuppressWarnings("unchecked")
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
			typedQuery.setFirstResult(nPageNumber * nPageSize);
			typedQuery.setMaxResults(nPageSize);
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
}