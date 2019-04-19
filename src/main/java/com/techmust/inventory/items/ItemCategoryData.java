package com.techmust.inventory.items;

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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac62_item_category")
public class ItemCategoryData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac62_id")
	private int m_nCategoryId;
	@Column(name = "ac62_category_name")
	private String m_strCategoryName;
	@Column(name = "ac62_Created_by")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac62_created_on")
	private Date m_dCreatedOn;
	@Transient
	private String m_strDate;
	
	public ItemCategoryData() 
	{
		m_nCategoryId = -1;
		m_strCategoryName = "";
		m_nCreatedBy = -1;
		m_dCreatedOn = Calendar.getInstance ().getTime ();
		setM_strDate("");
	}

	public int getM_nCategoryId() 
	{
		return m_nCategoryId;
	}

	public void setM_nCategoryId(int nCategoryId) 
	{
		m_nCategoryId = nCategoryId;
	}

	public String getM_strCategoryName()
	{
		return m_strCategoryName;
	}

	public void setM_strCategoryName(String strCategoryName) 
	{
		m_strCategoryName = strCategoryName;
	}

	public int getM_nCreatedBy() 
	{
		return m_nCreatedBy;
	}

	public void setM_nCreatedBy(int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public Date getM_dCreatedOn() 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public void setM_strDate(String strDate) 
	{
		m_strDate = strDate;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	@Override
	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strXml = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "ItemCategoryData");
			addChild (oXmlDocument, oRootElement, "m_nCategoryId", m_nCategoryId);
			addChild (oXmlDocument, oRootElement, "m_strCategoryName", m_strCategoryName);
//			addChild (oXmlDocument, oRootElement, "m_strCreatedBy", m_oCreatedBy.getM_strUserName());
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(getM_dCreatedOn()));
			strXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error ("generateXML - oException : " + oException);
		}
		return strXml;
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData credentials) throws Exception 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if (!m_strCategoryName.isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strCategoryName", m_strCategoryName, MatchMode.ANYWHERE));
		if (strColumn.contains("m_strUserName"))
			oCriteria.createCriteria ("m_oCreatedBy").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nCategoryId");
		return oCriteria;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (!m_strCategoryName.isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCategoryName")), m_strCategoryName.toLowerCase())); 
/*
 		if (strColumn.contains("m_strUserName"))
			oCriteria.createCriteria ("m_oCreatedBy").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nCategoryId");
 */			
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nCategoryId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCategoryId"), m_nCategoryId));
		return oConjunct;
	}
}
