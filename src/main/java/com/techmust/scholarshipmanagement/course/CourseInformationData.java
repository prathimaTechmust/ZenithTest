package com.techmust.scholarshipmanagement.course;

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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "coursedetails")
public class CourseInformationData extends MasterData
{	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "courseid")
	private int m_nCourseId;
	
	@Column(name = "shortcoursename")
	private String m_strShortCourseName;
	
	@Column(name = "longcoursename")
	private String m_strLongCourseName;	

	@Column(name="finalYear")
	private String m_strFinalYear;

	//Mandatory Columns
	@Column(name = "created_on")
	private Date m_dCreatedOn;
	
	@Column(name = "updated_on")
	private Date m_dUpdatedOn;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by")	
	private UserInformationData m_oUserCreatedBy;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "update_by")
	private UserInformationData m_oUserUpdatedBy;

	
	public CourseInformationData()
	{
		m_nCourseId = -1;
		m_strShortCourseName = "";
		m_strLongCourseName  = "";
		m_strFinalYear = "";
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

	public int getM_nCourseId()
	{
		return m_nCourseId;
	}

	public void setM_nCourseId(int m_nCourseId)
	{
		this.m_nCourseId = m_nCourseId;
	}

	public String getM_strShortCourseName()
	{
		return m_strShortCourseName;
	}

	public void setM_strShortCourseName(String m_strShortCourseName)
	{
		this.m_strShortCourseName = m_strShortCourseName;
	}

	public String getM_strLongCourseName()
	{
		return m_strLongCourseName;
	}

	public void setM_strLongCourseName(String m_strLongCourseName)
	{
		this.m_strLongCourseName = m_strLongCourseName;
	}
	
	public String getM_strFinalYear()
	{
		return m_strFinalYear;
	}

	public void setM_strFinalYear(String m_strFinalYear) 
	{
		this.m_strFinalYear = m_strFinalYear;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nCourseId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCourseId"), m_nCourseId));
		return oConjunct;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nCourseId() > 0)
		{
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCourseId"), m_nCourseId));
		}
		if (!m_strShortCourseName.isEmpty())
		{
			Expression<String> oExpression = oRootObject.get("m_strShortCourseName"); 
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oExpression,"%"+m_strShortCourseName+"%"));
		}
		if (!m_strLongCourseName.isEmpty())
		{
			Expression<String> oExpression = oRootObject.get("m_strLongCourseName"); 
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oExpression,"%"+m_strLongCourseName+"%"));
		}
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strCourseInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "CourseInformationData");
			addChild (oXmlDocument, oRootElement, "m_nCourseId", m_nCourseId);
			addChild (oXmlDocument, oRootElement, "m_strShortCourseName", m_strShortCourseName);
			addChild (oXmlDocument, oRootElement, "m_strLongCourseName", m_strLongCourseName);	
			addChild (oXmlDocument, oRootElement, "m_strFinalYear", m_strFinalYear);
			
			strCourseInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strCourseInfoXML;		
	}

}
