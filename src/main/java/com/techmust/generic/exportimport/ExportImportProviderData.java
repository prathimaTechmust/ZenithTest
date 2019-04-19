package com.techmust.generic.exportimport;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.directwebremoting.io.FileTransfer;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.response.GenericResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;
@Entity
@Table(name = "tac55_export_import_providers")
@SuppressWarnings("serial")
public class ExportImportProviderData<T> extends TenantData 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac55_id")
	private int m_nId;
	@Column(name = "ac55_description")
	private String m_strDescription;
	@Column(name = "ac55_providerName")
	private String m_strProviderName;
	@OneToMany
	@JoinColumn(name = "ac55_id")
	private Set<DataExchangeClasses> m_oDataExchangeClasses;
	@Transient
	public DataExchangeClasses [] m_arrDataExchangeClasses;
	
	public ExportImportProviderData ()
	{
		m_nId = -1;
		m_strDescription = "";
		m_strProviderName = "";
		m_oDataExchangeClasses = new HashSet<DataExchangeClasses> ();
	}
	
	public int getM_nId () 
	{
		return m_nId;
	}

	public void setM_nId (int nId) 
	{
		m_nId = nId;
	}

	public String getM_strDescription () 
	{
		return m_strDescription;
	}

	public void setM_strDescription (String strDescription)
	{
		m_strDescription = strDescription;
	}

	public String getM_strProviderName ()
	{
		return m_strProviderName;
	}

	public void setM_strProviderName (String strProviderName) 
	{
		m_strProviderName = strProviderName;
	}

	public Set<DataExchangeClasses> getM_oDataExchangeClasses () 
	{
		return m_oDataExchangeClasses;
	}

	public void setM_oDataExchangeClasses (Set<DataExchangeClasses> oDataExchangeClasses)
	{
		m_oDataExchangeClasses = oDataExchangeClasses;
	}

	public void addClasses ()
	{
		for (int nIndex = 0; nIndex < m_arrDataExchangeClasses.length; nIndex++)
		{
			DataExchangeClasses oDataExchangeClasses = m_arrDataExchangeClasses[nIndex];
			oDataExchangeClasses.setM_oExportImportProviders(this);
			m_oDataExchangeClasses.add(oDataExchangeClasses);
		}
	}

	@Override
	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strItemInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "ExportImportProvider");
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			addChild (oXmlDocument, oRootElement, "m_strDescription", m_strDescription);
			addChild (oXmlDocument, oRootElement, "m_strProviderName", m_strProviderName);
			Node oActionsXmlNode = oXmlDocument.importNode (getXmlDocument ("<m_oDataExchangeClasses>"+ getDataExchangeClassesXML ()+"</m_oDataExchangeClasses>").getFirstChild (), true);
			oRootElement.appendChild (oActionsXmlNode);
			strItemInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strItemInfoXML;
	}

	private String getDataExchangeClassesXML()
	{
		m_oLogger.info ("getDataExchangeClassesXML");
		String strXml = "";
	    Iterator<DataExchangeClasses> oIterator = m_oDataExchangeClasses.iterator ();
	    while (oIterator.hasNext ())
	    {
	    	strXml += ((DataExchangeClasses)oIterator.next ()).generateXML ();
	    }
	    m_oLogger.debug ( "getDataExchangeClassesXML - strXml [OUT] : " + strXml);
		return strXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (!m_strDescription.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strDescription", m_strDescription.trim(), MatchMode.ANYWHERE));
		if (!m_strProviderName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strProviderName", m_strProviderName.trim(), MatchMode.ANYWHERE));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strDescription");
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) 
	{
		return null;
	}

	public FileTransfer export(ArrayList<T> arrData) throws Exception
	{
		ExportImportProviderDataResponse oDataResponse = new ExportImportProviderDataResponse();
		ExportImportProviderDataProcessor oDataProcessor = new ExportImportProviderDataProcessor ();
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oDataResponse = (ExportImportProviderDataResponse) oDataProcessor.list(this, oOrderBy);
		Class oClass = Class.forName(oDataResponse.m_arrExportImportProvider.get(0).getM_strProviderName());
		Object oExportProvider = oClass.newInstance();
		Method oGetInstanceMethod = oClass.getMethod("exportData", ArrayList.class);
		return (FileTransfer) oGetInstanceMethod.invoke(oExportProvider, arrData);
	}

	public GenericResponse importData(FileTransfer oFileTransfer, String strClassName, UserInformationData oUserInformationData) throws Exception 
	{
		ExportImportProviderDataResponse oDataResponse = new ExportImportProviderDataResponse ();
		ExportImportProviderDataProcessor oProviderDataProcessor = new ExportImportProviderDataProcessor ();
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oDataResponse =(ExportImportProviderDataResponse) oProviderDataProcessor.list(this, oOrderBy);
		Class oClass = Class.forName(oDataResponse.m_arrExportImportProvider.get(0).getM_strProviderName());
        Object oImportProvider = oClass.newInstance();
        Method oGetInstanceMethod = oClass.getMethod("importData", FileTransfer.class, String.class, UserInformationData.class);
        return (GenericResponse) oGetInstanceMethod.invoke(oImportProvider, oFileTransfer, strClassName, oUserInformationData);
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId)); 
		if (!m_strDescription.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strDescription")), m_strDescription.toLowerCase())); 
		if (!m_strProviderName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strProviderName")), m_strProviderName.toLowerCase()));
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strDescription");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId));
		return oConjunct;
	}
}
