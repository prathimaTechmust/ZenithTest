package com.techmust.property.propertytype;

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
@Table(name = "tac45_property_type")
public class PropertyTypeData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac45_property_type_id")
	private int m_nPropertyTypeId;
	@Column(name ="ac45_property_type_name")
	private String m_strPropertyType;

	public void setM_nPropertyTypeId(int nPropertyTypeId) 
	{
		m_nPropertyTypeId = nPropertyTypeId;
	}

	public int getM_nPropertyTypeId() 
	{
		return m_nPropertyTypeId;
	}

	public void setM_strPropertyType(String strPropertyType) 
	{
		m_strPropertyType = strPropertyType;
	}

	public String getM_strPropertyType() 
	{
		return m_strPropertyType;
	}

	@Override
	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strXml = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "PropertyTypeData");
			addChild (oXmlDocument, oRootElement, "m_nPropertyTypeId", m_nPropertyTypeId);
			addChild (oXmlDocument, oRootElement, "m_strPropertyType", m_strPropertyType);
			strXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}
		return strXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (!m_strPropertyType.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strPropertyType", m_strPropertyType, MatchMode.ANYWHERE));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nPropertyTypeId");
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!m_strPropertyType.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strPropertyType")),m_strPropertyType)); 
		//addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nPropertyTypeId");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nPropertyTypeId"), m_nPropertyTypeId)); 
		return oConjunct;
	}
}
