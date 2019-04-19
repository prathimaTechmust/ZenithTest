package com.techmust.generic.exportimport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac56_data_exchange_classes")
@SuppressWarnings("serial")
public class DataExchangeClasses extends TenantData 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac56_id")
	private int m_nId;
	@Column(name = "ac56_registered_class_name")
	private String m_strRegisteredClassName;
	@ManyToOne
	@JoinColumn(name = "ac55_id")
	private ExportImportProviderData m_oExportImportProviders;
	
	public DataExchangeClasses ()
	{
		m_nId = -1;
		m_strRegisteredClassName = "";
		m_oExportImportProviders = new ExportImportProviderData ();
	}
	
	public int getM_nId() 
	{
		return m_nId;
	}

	public void setM_nId(int nId) 
	{
		m_nId = nId;
	}

	public String getM_strRegisteredClassName() 
	{
		return m_strRegisteredClassName;
	}

	public void setM_strRegisteredClassName(String strRegisteredClassName) 
	{
		m_strRegisteredClassName = strRegisteredClassName;
	}

	public ExportImportProviderData getM_oExportImportProviders() 
	{
		return m_oExportImportProviders;
	}

	public void setM_oExportImportProviders(ExportImportProviderData oExportImportProviders) 
	{
		m_oExportImportProviders = oExportImportProviders;
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
			addChild (oXmlDocument, oRootElement, "m_strRegisteredClassName", m_strRegisteredClassName);
			strItemInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strItemInfoXML;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (!m_strRegisteredClassName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strRegisteredClassName", m_strRegisteredClassName.trim(), MatchMode.ANYWHERE));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strRegisteredClassName");
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId)); 
		if (!m_strRegisteredClassName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strRegisteredClassName")), m_strRegisteredClassName .toLowerCase())); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strRegisteredClassName");
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
