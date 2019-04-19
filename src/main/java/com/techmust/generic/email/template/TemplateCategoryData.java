package com.techmust.generic.email.template;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "tac59_template_category")
public class TemplateCategoryData extends TenantData
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac59_category_id")
	private int m_nCategoryId;
	@Column(name = "ac59_category_name")
	private String m_strCategoryName;

	public TemplateCategoryData ()
	{
		m_nCategoryId = -1;
		m_strCategoryName = "";
	}
	
	public int getM_nCategoryId () 
	{
		return m_nCategoryId;
	}

	public void setM_nCategoryId (int nCategoryId)
	{
		m_nCategoryId = nCategoryId;
	}

	public String getM_strCategoryName () 
	{
		return m_strCategoryName;
	}

	public void setM_strCategoryName (String strCategoryName) 
	{
		m_strCategoryName = strCategoryName;
	}

	public String generateXML()
	{
		String strTemplateCategoryDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "TemplateCategoryData");
			addChild (oXmlDocument, oRootElement, "m_nCategoryId", m_nCategoryId);
			addChild (oXmlDocument, oRootElement, "m_strCategoryName", m_strCategoryName);
			strTemplateCategoryDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strTemplateCategoryDataXml;
	}

	@Override
	public GenericData getInstanceData(String strXML,UserInformationData oCredentials) throws Exception
	{
		return null;
		
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if (m_nCategoryId > 0)
		oCriteria.add (Restrictions.eq ("m_nCategoryId", m_nCategoryId));
		if (!m_strCategoryName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strCategoryName", m_strCategoryName.trim (), MatchMode.ANYWHERE));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nCategoryId");
		return oCriteria;
	}


	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nCategoryId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCategoryId"), m_nCategoryId)); 
		if (!m_strCategoryName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCategoryName")), m_strCategoryName.toLowerCase())); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nCategoryId");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nCategoryId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCategoryId"), m_nCategoryId));
		return oConjunct;
	}
}
