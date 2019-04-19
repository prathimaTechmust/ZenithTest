package com.techmust.master.businesstype;

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
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name="TCL04_Business_Type")
public class BusinessTypeData extends MasterData
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="CL04_businessstype_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int m_nBusinessTypeId;
	@Column(name="CL04_businesstype_name")
	private String m_strBusinessName;

	public BusinessTypeData ()
	{
		m_nBusinessTypeId = -1;
		m_strBusinessName = "";
	}

	public void setM_nBusinessTypeId (int nBusinessTypeId) 
	{
		m_nBusinessTypeId = nBusinessTypeId;
	}

	public int getM_nBusinessTypeId ()
	{
		return m_nBusinessTypeId;
	}

	public void setM_strBusinessName (String strBusinessName) 
	{
		m_strBusinessName = strBusinessName;
	}

	public String getM_strBusinessName () 
	{
		return m_strBusinessName;
	}
	
	@Override
    public String generateXML ()
    {
		m_oLogger.info ("generateXML");
		String strBusinessTypeXml = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "BusinessTypeData");
			addChild (oXmlDocument, oRootElement, "m_nBusinessTypeId", m_nBusinessTypeId);
			addChild (oXmlDocument, oRootElement, "m_strBusinessName", m_strBusinessName);
			strBusinessTypeXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}
		return strBusinessTypeXml;
    }
	
	
	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (!m_strBusinessName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strBusinessName", m_strBusinessName, MatchMode.ANYWHERE));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nBusinessTypeId");
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String strXML,UserInformationData oCredentials)
	{
		BusinessTypeData oBusinessTypeData = new BusinessTypeData ();
		try
		{
			Document oXMLDocument = getXmlDocument(strXML);
			Node oBusinessTypeNode = oXMLDocument.getFirstChild();
	    	oBusinessTypeData.setM_nBusinessTypeId(Integer.parseInt(UserManagementInitializer.getValue(oBusinessTypeNode, "m_nBusinessTypeId")));
		}
		catch (Exception oException)
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return oBusinessTypeData;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!m_strBusinessName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strBusinessName")), m_strBusinessName.toLowerCase())); 
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nBusinessTypeId"), m_nBusinessTypeId));
		return oConjunct;
	}
}