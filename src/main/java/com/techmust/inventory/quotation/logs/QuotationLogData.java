package com.techmust.inventory.quotation.logs;

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
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.traderp.util.TraderpUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.inventory.quotation.QuotationData;
@Entity
@Table(name = "tac41_quotation_log")
public class QuotationLogData extends TenantData 
{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac41_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name = "ac41_quotation_id")
	private QuotationData m_oQuotationData;
	@Transient
	private String m_strDate;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac41_created_on")
	private Date m_dDate;
	@Column(name ="ac41_comment")
	private String m_strComment;
	@Transient
	private String m_strTime;
	
	public QuotationLogData ()
	{
		m_nId = -1;
		setM_oQuotationData(new QuotationData ());
		m_strDate = "";
		m_dDate = Calendar.getInstance ().getTime ();
		m_strComment = "";
		m_strTime = "";
	}

	public void setM_nId(int m_nId) 
	{
		this.m_nId = m_nId;
	}

	public int getM_nId() 
	{
		return m_nId;
	}

	public void setM_oQuotationData(QuotationData oQuotationData) 
	{
		this.m_oQuotationData = oQuotationData;
	}

	public QuotationData getM_oQuotationData() 
	{
		return m_oQuotationData;
	}
	
	public void setM_strDate(String m_strDate) 
	{
		this.m_strDate = m_strDate;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public void setM_dDate(Date m_dDate)
	{
		this.m_dDate = m_dDate;
	}

	public Date getM_dDate() 
	{
		return m_dDate;
	}

	public void setM_strComment(String m_strComment) 
	{
		this.m_strComment = m_strComment;
	}

	public String getM_strComment() 
	{
		return m_strComment;
	}
	
	public void setM_strTime(Date oDate) 
	{
		this.m_strTime = TraderpUtil.getTime (oDate);
	}

	public String getM_strTime()
	{
		return m_strTime;
	}

	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strQuotationLogsXML ="";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "QuotationLogsData");
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			addChild (oXmlDocument, oRootElement, "m_strDate", m_strDate);
			addChild (oXmlDocument, oRootElement, "m_strComment", m_strComment);
			strQuotationLogsXML = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException : " + oException);
		}
	    m_oLogger.debug( "generateXML - strQuotationLogsXML [OUT] : " + strQuotationLogsXML);
	    return strQuotationLogsXML;
	}

	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_strComment != null && !m_strComment.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strComment", m_strComment.trim()));
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_oQuotationData != null && m_oQuotationData.getM_nQuotationId() > 0)
			oCriteria.add (Restrictions.eq ("m_oQuotationData", m_oQuotationData));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nId");
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_strComment != null && !m_strComment.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strComment")), m_strComment.toLowerCase())); 
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId)); 
		if (m_oQuotationData != null && m_oQuotationData.getM_nQuotationId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oQuotationData"), m_oQuotationData)); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nId");
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