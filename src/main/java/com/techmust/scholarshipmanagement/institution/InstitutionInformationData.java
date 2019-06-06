package com.techmust.scholarshipmanagement.institution;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;

@Entity
@Table(name = "institutions")
@JsonIgnoreProperties(value = {"m_oStudentData"})
public class InstitutionInformationData extends MasterData
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "institutionid")
	private int m_nInstitutionId;
	
	@Column(name = "institutionname")
	private String m_strInstitutionName;
	
	@Column(name = "institutionemail")
	private String m_strInstitutionEmailAddress;
	
	@Column(name = "institutionaddress")
	private String m_strInstitutionAddress;
	
	@Column(name = "contactpersonname")
	private String m_strContactPersonName;
	
	@Column(name = "contactpersonemail")
	private String m_strContactPersonEmail;
	
	@Column(name = "phonenumber")
	private String m_strPhoneNumber;
	
	@Column(name = "city")
	private String m_strCity;
	
	@Column(name = "state")
	private String m_strState;
	
	@Column(name = "pincode")
	private int m_nPincode;		
	
	@Column(name = "issue")
	private boolean m_bCheckedIssue;
	
	public InstitutionInformationData()
	{
		m_nInstitutionId = -1;
		m_strInstitutionName = "";
		m_strInstitutionEmailAddress = "";
		m_strInstitutionAddress = "";
		m_strContactPersonName = "";
		m_strContactPersonEmail = "";
		m_strPhoneNumber = "";
		m_strCity = "";
		m_strState = "";
		m_nPincode = -1;	
		m_bCheckedIssue = false;
	}
	
	public boolean isM_bCheckedIssue()
	{
		return m_bCheckedIssue;
	}

	public void setM_bCheckedIssue(boolean bCheckedIssue)
	{
		this.m_bCheckedIssue = bCheckedIssue;
	}
	
	public int getM_nInstitutionId()
	{
		return m_nInstitutionId;
	}

	public void setM_nInstitutionId(int m_nInstitutionId)
	{
		this.m_nInstitutionId = m_nInstitutionId;
	}

	public String getM_strInstitutionName()
	{
		return m_strInstitutionName;
	}

	public void setM_strInstitutionName(String m_strInstitutionName)
	{
		this.m_strInstitutionName = m_strInstitutionName;
	}

	public String getM_strInstitutionEmailAddress() 
	{
		return m_strInstitutionEmailAddress;
	}

	public void setM_strInstitutionEmailAddress(String m_strInstitutionEmailAddress)
	{
		this.m_strInstitutionEmailAddress = m_strInstitutionEmailAddress;
	}

	public String getM_strInstitutionAddress() 
	{
		return m_strInstitutionAddress;
	}

	public void setM_strInstitutionAddress(String m_strInstitutionAddress)
	{
		this.m_strInstitutionAddress = m_strInstitutionAddress;
	}

	public String getM_strContactPersonName()
	{
		return m_strContactPersonName;
	}

	public void setM_strContactPersonName(String m_strContactPersonName) 
	{
		this.m_strContactPersonName = m_strContactPersonName;
	}

	public String getM_strContactPersonEmail()
	{
		return m_strContactPersonEmail;
	}

	public void setM_strContactPersonEmail(String m_strContactPersonEmail)
	{
		this.m_strContactPersonEmail = m_strContactPersonEmail;
	}

	public String getM_strPhoneNumber() 
	{
		return m_strPhoneNumber;
	}

	public void setM_strPhoneNumber(String m_strPhoneNumber)
	{
		this.m_strPhoneNumber = m_strPhoneNumber;
	}

	public String getM_strCity()
	{
		return m_strCity;
	}

	public void setM_strCity(String m_strCity)
	{
		this.m_strCity = m_strCity;
	}

	public String getM_strState()
	{
		return m_strState;
	}

	public void setM_strState(String m_strState) 
	{
		this.m_strState = m_strState;
	}

	public int getM_nPincode()
	{
		return m_nPincode;
	}

	public void setM_nPincode(int m_nPincode)
	{
		this.m_nPincode = m_nPincode;
	}
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nInstitutionId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nInstitutionId"), m_nInstitutionId));
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nInstitutionId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nInstitutionId"), m_nInstitutionId));
		if (!m_strInstitutionName.isEmpty())
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strInstitutionName"), m_strInstitutionName));
		}
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strInstitutionInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "InstitutionInformationData");
			addChild (oXmlDocument, oRootElement, "m_nInstitutionId", m_nInstitutionId);
			addChild (oXmlDocument, oRootElement, "m_strInstitutionName", m_strInstitutionName);
			addChild (oXmlDocument, oRootElement, "m_strInstitutionEmailAddress", m_strInstitutionEmailAddress);			
			addChild (oXmlDocument, oRootElement, "m_strInstitutionAddress", m_strInstitutionAddress);
			addChild (oXmlDocument, oRootElement, "m_strContactPersonName", m_strContactPersonName);
			addChild (oXmlDocument, oRootElement, "m_strContactPersonEmail", m_strContactPersonEmail);
			addChild (oXmlDocument, oRootElement, "m_strPhoneNumber", m_strPhoneNumber);			
			addChild (oXmlDocument, oRootElement, "m_strCity", m_strCity);
			addChild (oXmlDocument, oRootElement, "m_strState", m_strState);
			addChild (oXmlDocument, oRootElement, "m_nPincode", m_nPincode);
			strInstitutionInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strInstitutionInfoXML;		
	}
}
