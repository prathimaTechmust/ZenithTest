package com.techmust.clientmanagement;

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
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name="TCL02_Demography")
public class DemographyData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "CL02_demography_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int m_nDemographyId;
	@Column(name="CL02_businesstype_id")
	private int m_nBusinessType;
	
	public DemographyData ()
	{
		m_nDemographyId = -1;
		m_nBusinessType = -1;
	}

	public void setM_nDemographyId (int demographyId) 
	{
		m_nDemographyId = demographyId;
	}
	
	public int getM_nDemographyId () 
	{
		return m_nDemographyId;
	}
	
	public void setM_nBusinessType (int nBusinessType) 
	{
		m_nBusinessType = nBusinessType;
	}

	public int getM_nBusinessType () 
	{
		return m_nBusinessType;
	}	

	@Override
    public String generateXML ()
    {
		m_oLogger.info ("generateXML");
		String strXML = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement  = createRootElement (oXmlDocument, "DemographyData");
			addChild (oXmlDocument, oRootElement, "m_nDemographyId", m_nDemographyId);
			addChild (oXmlDocument, oRootElement, "m_nBusinessType", m_nBusinessType);
			strXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}
		m_oLogger.debug ("generateXML - strXML [OUT] : " + strXML);
	    return strXML;
    }
	
	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{		
		oCriteria.setMaxResults (100);
		if (getM_nBusinessType () > 0)
			oCriteria.add (Restrictions.eq ("m_nBusinessType", m_nBusinessType));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nDemographyId");
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData OCredentials) throws Exception
	{
		DemographyData oDemographyData = new DemographyData ();
		try
		{
		}
		catch (Exception oException)
		{
			m_oLogger.error("generateXML - oException : " + oException);
			throw oException;
		}
		return oDemographyData;
	}


	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
//		oCriteria.setMaxResults (100);
		if (m_nBusinessType > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nBusinessType"), m_nBusinessType)); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nDemographyId");
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nDemographyId"), m_nDemographyId));
		return oConjunct;
	}
}