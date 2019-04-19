package com.techmust.generic.log;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tg02_logs")
public class LogData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "g02_log_id")
	private int m_nId;
	@Column(name = "g02_function_name")
	private String m_strFunctionName;

	@Column(name = "g02_user")
	private int m_nUserID;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "g02_created_on")
	private Date m_dCreatedOn;
	@Transient
	private String m_strDate;
	@Transient
	private String m_strTime;
	@Transient
	private String m_strFromDate = "";
	@Transient
    private String m_strToDate = "";
	
	public LogData ()
	{
		m_dCreatedOn = Calendar.getInstance ().getTime ();
		setM_strTime("");
	}
	
	public int getM_nId()
	{
		return m_nId;
	}

	public void setM_nId(int nId)
	{
		m_nId = nId;
	}

	public String getM_strFunctionName() 
	{
		return m_strFunctionName;
	}

	public void setM_strFunctionName(String strFunctionName) 
	{
		m_strFunctionName = strFunctionName;
	}

	public int getM_nUserID() 
	{
		return m_nUserID;
	}

	public void setM_nUserID(int nUserID)
	{
		m_nUserID = nUserID;
	}

	public Date getM_dCreatedOn() 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public void setM_strDate(String strDate)
	{
		m_strDate = strDate;
	}

	public void setM_strTime(String strTime) 
	{
		m_strTime = strTime;
	}

	public String getM_strTime() 
	{
		return m_strTime;
	}

	public void setM_strFromDate (String strFromDate) 
	{
		m_strFromDate = strFromDate;
	}

	public String getM_strFromDate ()
	{
		return m_strFromDate;
	}

	public void setM_strToDate (String strToDate) 
	{
		m_strToDate = strToDate;
	}

	public String getM_strToDate() {
		return m_strToDate;
	}

	@Override
	public String generateXML() 
	{
		String strLogDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "LogData");
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
//			addChild (oXmlDocument, oRootElement, "m_strUserName", getM_oUserData() != null ? getM_oUserData().getM_strUserName() : "");
			addChild (oXmlDocument, oRootElement, "m_strFunctionName", m_strFunctionName);
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dCreatedOn));
			addChild (oXmlDocument, oRootElement, "m_strTime", GenericIDataProcessor.getTime(m_dCreatedOn));
			strLogDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strLogDataXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_nUserID > 0)
			oCriteria.add (Restrictions.eq ("m_nUserID", m_nUserID));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (strColumn.contains("m_strUserName"))
			oCriteria.createCriteria ("m_oUserData").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nId");
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nUserID > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oUserData"), m_nUserID)); 
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn")
					, oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false).toString())
					, oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.ge(oRootObject.get("m_dCreatedOn")
					, oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false).toString())));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.le(oRootObject.get("m_dCreatedOn")
					, oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, false).toString())));
/*
 		if (strColumn.contains("m_strUserName"))
			oCriteria.createCriteria ("m_oUserData").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nId");
 */
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId));
		return oConjunct;
	}
}
