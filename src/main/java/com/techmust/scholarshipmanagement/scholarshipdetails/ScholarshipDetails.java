package com.techmust.scholarshipmanagement.scholarshipdetails;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.scholarshipmanagement.academicdetails.AcademicDetails;
import com.techmust.scholarshipmanagement.academicdetails.AcademicYear;
import com.techmust.usermanagement.facilitator.FacilitatorInformationData;

@Entity
@Table(name = "scholarshipdetails")
public class ScholarshipDetails extends MasterData
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scholarshipid")
	private int m_nScholarshipId;
	
	@Column(name = "organizationname")
	private String m_strOrganizationName;
	
	@Column(name = "amount")
	private float m_fAmount;		
	
	@Column(name = "date")
	private Calendar m_dDate;	
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "academicid")
	private AcademicDetails m_oAcademicDetails;		
	
	public ScholarshipDetails()
	{
		m_nScholarshipId = -1;
		m_strOrganizationName = "";
		m_fAmount = 0;	
		m_dDate = Calendar.getInstance();		
		m_oAcademicDetails = new AcademicDetails();
		
	}	

	public AcademicDetails getM_oAcademicDetails()
	{
		return m_oAcademicDetails;
	}

	public void setM_oAcademicDetails(AcademicDetails m_oAcademicDetails)
	{
		this.m_oAcademicDetails = m_oAcademicDetails;
	}

	public int getM_nScholarshipId()
	{
		return m_nScholarshipId;
	}

	public void setM_nScholarshipId(int m_nScholarshipId)
	{
		this.m_nScholarshipId = m_nScholarshipId;
	}

	public String getM_strOrganizationName()
	{
		return m_strOrganizationName;
	}

	public void setM_strOrganizationName(String m_strOrganizationName) 
	{
		this.m_strOrganizationName = m_strOrganizationName;
	}

	public float getM_fAmount() 
	{
		return m_fAmount;
	}

	public void setM_fAmount(float m_fAmount)
	{
		this.m_fAmount = m_fAmount;
	}

	public Calendar getM_dDate()
	{
		return m_dDate;
	}

	public void setM_dDate(Calendar m_dDate) 
	{
		this.m_dDate = m_dDate;
	}
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!getM_strOrganizationName().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strOrganizationName"), m_strOrganizationName));
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!getM_strOrganizationName().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strOrganizationName"), m_strOrganizationName));
		return oConjunct;
	}
	
	@Override
	public String generateXML() 
	{
		String strScholarshipDetails = "";
		m_oLogger.info ("generateXML");
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "ScholarshipDetails");			
			addChild (oXmlDocument, oRootElement, "m_nScholarshipId", m_nScholarshipId);
			addChild (oXmlDocument, oRootElement, "m_strOrganizationName", m_strOrganizationName);
			addChild (oXmlDocument, oRootElement, "m_fAmount", m_fAmount);			
			strScholarshipDetails = getXmlString (oXmlDocument);
		} 
		catch (Exception oException)
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}		
		return strScholarshipDetails;		
	}	
}
