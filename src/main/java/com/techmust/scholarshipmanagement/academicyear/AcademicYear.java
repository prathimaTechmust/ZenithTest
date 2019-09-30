package com.techmust.scholarshipmanagement.academicyear;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "academicyear")
public class AcademicYear extends MasterData
{	
	private static final long serialVersionUID = 1L;	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "academicyearid")
	private int m_nAcademicYearId;
	
	@Column(name = "year")
	private String m_strAcademicYear;
	
	@Column(name = "defaultYear")
	private boolean m_bDefaultYear;
	
	//Mandatory Columns
	@Column(name = "created_on")
	private Date m_dCreatedOn;
	
	@Column(name = "updated_on")
	private Date m_dUpdatedOn;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by")
	private UserInformationData m_oUserCreatedBy;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "updated_by")
	private UserInformationData m_oUserUpdatedBy;
	
	public AcademicYear()
	{
		m_nAcademicYearId = -1;
		m_strAcademicYear = "";
		m_bDefaultYear = false;
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_dUpdatedOn = Calendar.getInstance().getTime();
		m_oUserCreatedBy = new UserInformationData();
		m_oUserUpdatedBy = new UserInformationData();
	}	
	
	public Date getM_dCreatedOn() 
	{
		return m_dCreatedOn;
	}
	
	public void setM_dCreatedOn(Date m_dCreatedOn)
	{
		this.m_dCreatedOn = m_dCreatedOn;
	}

	public Date getM_dUpdatedOn() 
	{
		return m_dUpdatedOn;
	}

	public void setM_dUpdatedOn(Date m_dUpdatedOn)
	{
		this.m_dUpdatedOn = m_dUpdatedOn;
	}

	public UserInformationData getM_oUserCreatedBy()
	{
		return m_oUserCreatedBy;
	}

	public void setM_oUserCreatedBy(UserInformationData m_oUserCreatedBy) 
	{
		this.m_oUserCreatedBy = m_oUserCreatedBy;
	}

	public UserInformationData getM_oUserUpdatedBy()
	{
		return m_oUserUpdatedBy;
	}

	public void setM_oUserUpdatedBy(UserInformationData m_oUserUpdatedBy)
	{
		this.m_oUserUpdatedBy = m_oUserUpdatedBy;
	}

	public boolean isM_bDefaultYear()
	{
		return m_bDefaultYear;
	}

	public void setM_bDefaultYear(boolean m_bDefaultYear)
	{
		this.m_bDefaultYear = m_bDefaultYear;
	}
	
	public int getM_nAcademicYearId()
	{
		return m_nAcademicYearId;
	}

	public void setM_nAcademicYearId(int nAcademicYearId)
	{
		this.m_nAcademicYearId = nAcademicYearId;
	}

	public String getM_strAcademicYear() 
	{
		return m_strAcademicYear;
	}

	public void setM_strAcademicYear(String strAcademicYear) 
	{
		this.m_strAcademicYear = strAcademicYear;
	}	
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nAcademicYearId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nAcademicYearId"), m_nAcademicYearId));
		if(!m_strAcademicYear.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strAcademicYear"), m_strAcademicYear));
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nAcademicYearId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nAcademicYearId"), m_nAcademicYearId));
		if(!m_strAcademicYear.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strAcademicYear"), m_strAcademicYear));
		if(m_bDefaultYear)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_bDefaultYear"), m_bDefaultYear));
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strAcademicInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "AcademicYear");			
			addChild (oXmlDocument, oRootElement, "m_nAcademicYearId", m_nAcademicYearId);
			addChild (oXmlDocument, oRootElement, "m_strAcademicYear", m_strAcademicYear);			
			strAcademicInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strAcademicInfoXML;
	}
}
