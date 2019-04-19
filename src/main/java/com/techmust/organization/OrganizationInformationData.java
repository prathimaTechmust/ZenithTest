package com.techmust.organization;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;

@Entity
@Table(name = "organization")
public class OrganizationInformationData extends MasterData 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orgId")
	private int m_nOrganizationId;
	@Column(name = "orgname")
	private String m_strOrganizationName;
	@Column(name = "address")
	private String m_strAddress;
	@Column(name = "phonenumber")
	private String  m_strPhoneNumber;
	@Column(name = "emailaddress")
	private String m_strEmailAddress;
	@Column(name = "status")
	private String m_strStatus;
	@Transient
	private String m_strPassword;
	@Transient
	private String m_strLoginID;	
	
	public OrganizationInformationData() 
	{
		m_nOrganizationId = -1;
		m_strOrganizationName = "";
		m_strAddress = "";
		m_strPhoneNumber = "";
		m_strEmailAddress = "";
		m_strStatus = "";
		m_strPassword = "";
		m_strLoginID = "";
	}
	public int getM_nOrganizationId() 
	{
		return m_nOrganizationId;
	}
	public void setM_nOrganizationId(int m_nOrganizationId) 
	{
		this.m_nOrganizationId = m_nOrganizationId;
	}
	public String getM_strOrganizationName() 
	{
		return m_strOrganizationName;
	}
	public void setM_strOrganizationName(String m_strOrganizationName) 
	{
		this.m_strOrganizationName = m_strOrganizationName;
	}
	public String getM_strAddress() 
	{
		return m_strAddress;
	}
	public void setM_strAddress(String m_strAddress) 
	{
		this.m_strAddress = m_strAddress;
	}
	public String getM_strPhoneNumber() 
	{
		return m_strPhoneNumber;
	}
	public void setM_strPhoneNumber(String m_strPhoneNumber) 
	{
		this.m_strPhoneNumber = m_strPhoneNumber;
	}
	public String getM_strEmailAddress()
	{
		return m_strEmailAddress;
	}
	public void setM_strEmailAddress(String m_strEmailAddress) 
	{
		this.m_strEmailAddress = m_strEmailAddress;
	}
	public String getM_strStatus() 
	{
		return m_strStatus;
	}

	public void setM_strStatus(String m_strStatus)
	{
		this.m_strStatus = m_strStatus;
	}

	public String getM_strPassword()
	{
		return m_strPassword;
	}
	
	public void setM_strPassword(String m_strPassword) 
	{
		this.m_strPassword = m_strPassword;
	}

	public String getM_strLoginID() 
	{
		return m_strLoginID;
	}

	public void setM_strLoginID(String m_strLoginID) 
	{
		this.m_strLoginID = m_strLoginID;
	}
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!m_strOrganizationName.isEmpty())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strOrganizationName"), m_strOrganizationName));
		}
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strItemInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "OrganizationInformationData");
			addChild (oXmlDocument, oRootElement, "m_strOrganizationName", m_strOrganizationName);
			addChild (oXmlDocument, oRootElement, "m_strAddress", m_strAddress);
			addChild (oXmlDocument, oRootElement, "m_strPhoneNumber", m_strPhoneNumber);
			addChild (oXmlDocument, oRootElement, "m_strEmailAddress", m_strEmailAddress);
			strItemInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strItemInfoXML;		
	}
}
