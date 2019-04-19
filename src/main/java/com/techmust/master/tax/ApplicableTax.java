package com.techmust.master.tax;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name="tac03_applicable_tax")
public class ApplicableTax extends MasterData
{
    private static final long serialVersionUID = 1L;
    @Transient
    public Tax [] m_arrTax;
    @Id
    @Column(name = "ac03_applied_tax_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int m_nId;
    @Column(name = "ac03_name")
    private String m_strApplicableTaxName;
    @ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
            name="tac03_applied_taxes",
            joinColumns = @JoinColumn( name="ac04_applied_tax_id"),
            inverseJoinColumns = @JoinColumn( name="ac04_tax_id")
    )
    private Set<Tax> m_oTaxes;
    
    public ApplicableTax ()
	{
    	m_nId = -1;
    	m_strApplicableTaxName = "";
    	setM_oTaxes(new HashSet<Tax> ());
	}
    
	public void setM_nId(int nId)
    {
	    this.m_nId = nId;
    }

	public int getM_nId()
    {
	    return m_nId;
    }

	public void setM_strApplicableTaxName(String strApplicableTaxName)
    {
	    this.m_strApplicableTaxName = strApplicableTaxName;
    }

	public String getM_strApplicableTaxName()
    {
	    return m_strApplicableTaxName;
    }



	public void setM_oTaxes(Set<Tax> m_oTaxes)
    {
	    this.m_oTaxes = m_oTaxes;
    }

	public Set<Tax> getM_oTaxes()
    {
	    return m_oTaxes;
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
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			addChild (oXmlDocument, oRootElement, "m_strApplicableTaxName", m_strApplicableTaxName);
			Node oActionsXmlNode = oXmlDocument.importNode (getXmlDocument ("<m_oTaxes>"+ getTaxXML ()+"</m_oTaxes>").getFirstChild (), true);
			oRootElement.appendChild (oActionsXmlNode);
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
		if (!m_strApplicableTaxName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strApplicableTaxName", m_strApplicableTaxName.trim(), MatchMode.ANYWHERE));
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strApplicableTaxName");
		return oCriteria;
	}

	private String getTaxXML ()
    {
		m_oLogger.info ("getTaxXML");
		String strXml = "";
	    Iterator<Tax> oIterator = m_oTaxes.iterator ();
	    while (oIterator.hasNext ())
	    {
	    	strXml += ((Tax)oIterator.next ()).generateXML ();
	    }
	    m_oLogger.debug ( "getTaxXML - strXml [OUT] : " + strXml);
		return strXml;
    }

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) throws Exception 
	{
		ApplicableTax oApplicableTax = new ApplicableTax ();
		try
		{
			Document oXMLDocument = getXmlDocument(strXML);
			NodeList oChildNodes = oXMLDocument.getChildNodes();
			for (int nIndex = 0; nIndex < oChildNodes.getLength(); nIndex++)
		    {
		    	Node oChildNode = oChildNodes.item(nIndex);
	    		oApplicableTax.setM_nId(Integer.parseInt(UserManagementInitializer.getValue(oChildNode, "m_nId")));
	    		break;
		    }
		}
		catch (Exception oException)
		{
			m_oLogger.error("getInstanceData - oException : " + oException);
			throw oException;
		}
		return oApplicableTax;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!m_strApplicableTaxName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strApplicableTaxName")), m_strApplicableTaxName.trim().toLowerCase())); 
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId)); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strApplicableTaxName");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nId()> 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), getM_nId())); 
		return oConjunct;
	}
}
