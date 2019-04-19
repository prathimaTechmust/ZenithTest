package com.techmust.inventory.sales;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
import org.w3c.dom.Node;
import com.techmust.clientmanagement.ClientGroupData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.items.ItemGroupData;
import com.techmust.usermanagement.userinfo.UserInformationData;
@Entity
@Table(name = "tac31_discount_structure")
public class DiscountStructureData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private DiscountStructureDataEmmbedable compositeId;
	
	@ManyToOne
	@JoinColumn(name = "ac31_client_group_id")
	private ClientGroupData m_oClientGroupData;	
	@ManyToOne
	@JoinColumn(name = "ac31_item_group_id")
	private ItemGroupData m_oItemGroupData;
	@Column(name = "ac31_discount")
	private float m_nDiscount;
	@Column(name = "ac31_Created_by")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac31_created_on")
	private Date m_dCreatedOn;	
	
	public DiscountStructureDataEmmbedable getCompositeId()
	{
		return compositeId;
	}

	public void setCompositeId(DiscountStructureDataEmmbedable compositeId)
	{
		this.compositeId = compositeId;
	}
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Transient
	private String m_strDate;
	
	public DiscountStructureData() 
	{
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_nDiscount = 0;
	}

	public ClientGroupData getM_oClientGroupData() 
	{
		return m_oClientGroupData;
	}

	public void setM_oClientGroupData(ClientGroupData oClientGroupData) 
	{
		m_oClientGroupData = oClientGroupData;
	}

	public void setM_oItemGroupData(ItemGroupData oItemGroupData) 
	{
		this.m_oItemGroupData = oItemGroupData;
	}

	public ItemGroupData getM_oItemGroupData() 
	{
		return m_oItemGroupData;
	}

	public void setM_nDiscount(float nDiscount) 
	{
		this.m_nDiscount = nDiscount;
	}

	public float getM_nDiscount()
	{
		return m_nDiscount;
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

	public UserInformationData getM_oUserCredentialsData()
	{
		return m_oUserCredentialsData;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public void setM_strDate(String strDate) 
	{
		m_strDate = strDate;
	}

	@Override
	public String generateXML() 
	{
		String strXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "SalesData");
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dCreatedOn));
			Document oClientDoc = getXmlDocument (m_oClientGroupData.generateXML());
			Node oClientNode = oXmlDocument.importNode (oClientDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
			strXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_nCreatedBy  > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if ((m_strDate != null && !m_strDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate, true)));
		if (m_oClientGroupData != null && m_oClientGroupData.getM_nGroupId() > 0)
			oCriteria.add(Restrictions.eq("m_oClientGroupData", m_oClientGroupData));
		if (m_oItemGroupData != null && m_oItemGroupData.getM_nItemGroupId() > 0)
			oCriteria.add(Restrictions.eq("m_oItemGroupData", m_oItemGroupData));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_oCreatedBy");
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
		if (m_nCreatedBy > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCreatedBy"), m_nCreatedBy)); 
		if ((m_strDate != null && !m_strDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate,false).toString()), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate, true).toString())));
		if (m_oClientGroupData != null && m_oClientGroupData.getM_nGroupId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oClientGroupData"), m_oClientGroupData)); 
		if (m_oItemGroupData != null && m_oItemGroupData.getM_nItemGroupId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oItemGroupData"), m_oItemGroupData)); 
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_oCreatedBy");
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}
}
