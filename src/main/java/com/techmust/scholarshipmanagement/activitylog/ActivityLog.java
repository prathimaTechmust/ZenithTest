package com.techmust.scholarshipmanagement.activitylog;

import java.util.Date;

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
import com.techmust.utils.Utils;

@Entity
@Table(name = "activitylog")
public class ActivityLog  extends MasterData
{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activityid")
	private int m_nActivityId;
	
	@Column(name = "username")
	private String m_strLoginUserName;
	
	@Column(name = "taskPerformed")
	private String m_strTaskPerformed;
	
	@Column(name = "Date")
	private Date m_dDate;
	
	@Column(name = "xmlparameter",columnDefinition = "TEXT")
	private String m_strXMLString;	

	public ActivityLog() 
	{
		m_nActivityId = -1;
		m_strLoginUserName = "";
		m_strTaskPerformed = "";
		m_dDate = null;
		m_strXMLString = "";		
	}

	public int getM_nActivityId()
	{
		return m_nActivityId;
	}

	public void setM_nActivityId(int nActivityId)
	{
		this.m_nActivityId = nActivityId;
	}

	public String getM_strLoginUserName()
	{
		return m_strLoginUserName;
	}

	public void setM_strLoginUserName(String strLoginUserName)
	{
		this.m_strLoginUserName = strLoginUserName;
	}

	public String getM_strTaskPerformed()
	{
		return m_strTaskPerformed;
	}

	public void setM_strTaskPerformed(String strTaskPerformed)
	{
		this.m_strTaskPerformed = strTaskPerformed;
	}

	public Date getM_dDate()
	{
		return m_dDate;
	}

	public void setM_dDate(Date dDate)
	{
		this.m_dDate = dDate;
	}

	public String getM_strXMLString()
	{
		return m_strXMLString;
	}

	public void setM_strXMLString(String strXMLString)
	{
		this.m_strXMLString = strXMLString;
	}
	
	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteriaQuery,CriteriaBuilder oCriteriaBuilder)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(getM_nActivityId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nActivityId"), m_nActivityId));
		if(!m_strLoginUserName.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strLoginUserName"), m_strLoginUserName));
		return oConjunct;
	}
	
	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject) 
	{	
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(getM_nActivityId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nActivityId"), m_nActivityId));
		if(!m_strLoginUserName.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strLoginUserName"), m_strLoginUserName));
		return oConjunct;
	}
	
	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strActivityInfoXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "ActivityLog");
			addChild (oXmlDocument, oRootElement, "m_nActivityId", m_nActivityId);
			addChild (oXmlDocument, oRootElement, "m_strLoginUserName", m_strLoginUserName);
			addChild (oXmlDocument, oRootElement, "m_strTaskPerformed", m_strTaskPerformed);
			addChild (oXmlDocument, oRootElement, "m_dDate", m_dDate != null ? m_dDate.toString(): "");
			addChild (oXmlDocument, oRootElement, "m_strXMLString", m_strXMLString);
			strActivityInfoXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
		return strActivityInfoXML;		
	}

}
