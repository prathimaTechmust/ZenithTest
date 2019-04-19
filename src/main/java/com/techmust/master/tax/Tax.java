package com.techmust.master.tax;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.userinfo.UserInformationData;
@Entity
@Table(name="tac02_tax")
public class Tax extends MasterData
{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="ac02_tax_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int m_nTaxId;
    @Column(name="ac02_tax_name")
    private String m_strTaxName;
    @Column(name = "ac02_tax_percentage")
    private float m_nPercentage;
    @Transient
    private float m_nAmount;
    
    public Tax ()
	{
    	m_nTaxId = -1;
    	m_strTaxName = "";
    	m_nPercentage = 0;
    	m_nAmount = 0;
 	}

	public void setM_nTaxId(int nTaxId)
    {
	    this.m_nTaxId = nTaxId;
    }

	public int getM_nTaxId()
    {
	    return m_nTaxId;
    }

	public void setM_strTaxName(String strTaxName)
    {
	    this.m_strTaxName = strTaxName;
    }

	public String getM_strTaxName()
    {
	    return m_strTaxName;
    }
	
	public void setM_nPercentage(float nPercentage) 
	{
		m_nPercentage = nPercentage;
	}

	public float getM_nPercentage() 
	{
		return m_nPercentage;
	}

	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strItemInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "Applicabletax");
			addChild (oXmlDocument, oRootElement, "m_nTaxId", m_nTaxId);
			addChild (oXmlDocument, oRootElement, "m_strTaxName", m_strTaxName);
			addChild (oXmlDocument, oRootElement, "m_nPercentage", m_nPercentage);
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
		if (!m_strTaxName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strTaxName", m_strTaxName.trim(), MatchMode.ANYWHERE));
		if (m_nTaxId > 0)
			oCriteria.add (Restrictions.eq ("m_nTaxId", m_nTaxId));
		if (m_nPercentage > 0)
			oCriteria.add (Restrictions.eq ("m_nPercentage", m_nPercentage));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strTaxName");
		return oCriteria;
	}

	public void setM_nAmount(float m_nAmount) {
		this.m_nAmount = m_nAmount;
	}

	public float getM_nAmount() {
		return m_nAmount;
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
		if (!m_strTaxName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strTaxName")), m_strTaxName.trim().toLowerCase())); 
		if (m_nTaxId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nTaxId"), m_nTaxId)); 
		if (m_nPercentage > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nPercentage"), m_nPercentage)); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strTaxName");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nTaxId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nTaxId"), m_nTaxId)); 
		return oConjunct;
	}
}
