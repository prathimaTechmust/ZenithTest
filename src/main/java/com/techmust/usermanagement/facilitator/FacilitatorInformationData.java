package com.techmust.usermanagement.facilitator;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
@Entity
@Table(name = "facilitators")
public class FacilitatorInformationData extends MasterData
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "facilitatorid")
	private int m_nFacilitatorId;
	
	@Column(name = "facilitatorname")
	private String m_strFacilitatorName;
	
	@Column(name = "phonenumber")
	private String m_strPhoneNumber;
	
	@Column(name = "email")
	private String m_strEmail;
	
	@Column(name = "city")
	private String m_strCity;	
	
	@Column(name = "state")
	private String m_strState;
	
	public FacilitatorInformationData()
	{
		m_nFacilitatorId = -1;
		m_strFacilitatorName = "";
		m_strPhoneNumber = "";
		m_strEmail = "";
		m_strCity = "";
		m_strState = "";
	}	

	public int getM_nFacilitatorId() 
	{
		return m_nFacilitatorId;
	}

	public void setM_nFacilitatorId(int m_nFacilitatorId)
	{
		this.m_nFacilitatorId = m_nFacilitatorId;
	}

	public String getM_strFacilitatorName() 
	{
		return m_strFacilitatorName;
	}

	public void setM_strFacilitatorName(String m_strFacilitatorName) 
	{
		this.m_strFacilitatorName = m_strFacilitatorName;
	}

	public String getM_strPhoneNumber()
	{
		return m_strPhoneNumber;
	}

	public void setM_strPhoneNumber(String m_strPhoneNumber)
	{
		this.m_strPhoneNumber = m_strPhoneNumber;
	}

	public String getM_strEmail()
	{
		return m_strEmail;
	}

	public String getM_strState()
	{
		return m_strState;
	}

	public void setM_strState(String m_strState)
	{
		this.m_strState = m_strState;
	}

	public void setM_strEmail(String m_strEmail)
	{
		this.m_strEmail = m_strEmail;
	}

	public String getM_strCity()
	{
		return m_strCity;
	}

	public void setM_strCity(String m_strCity) 
	{
		this.m_strCity = m_strCity;
	}
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nFacilitatorId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nFacilitatorId"), m_nFacilitatorId));
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nFacilitatorId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nFacilitatorId"), m_nFacilitatorId));
		if (!m_strFacilitatorName.isEmpty())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strFacilitatorName"), m_strFacilitatorName));
		}
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strFacilitatorInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "FacilitatorInformationData");
			addChild (oXmlDocument, oRootElement, "m_nFacilitatorId", m_nFacilitatorId);
			addChild (oXmlDocument, oRootElement, "m_strFacilitatorName", m_strFacilitatorName);
			addChild (oXmlDocument, oRootElement, "m_strPhoneNumber", m_strPhoneNumber);
			addChild (oXmlDocument, oRootElement, "m_strEmail", m_strEmail);
			addChild (oXmlDocument, oRootElement, "m_strCity", m_strCity);
			addChild(oXmlDocument, oRootElement, "m_strState", m_strState);
			strFacilitatorInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strFacilitatorInfoXML;		
	}
}
