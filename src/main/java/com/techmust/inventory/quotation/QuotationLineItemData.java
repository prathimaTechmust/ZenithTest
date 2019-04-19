package com.techmust.inventory.quotation;

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

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.sales.CustomizedItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac40_quotation_lineItems")
public class QuotationLineItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac40_id")
	private int m_nLineItemId;
	@Column(name = "ac40_article_description")
	private String m_strArticleDescription;
	@ManyToOne
	@JoinColumn(name = "ac40_item_id")
	private ItemData m_oItemData;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="ac40_created_on")
	private Date m_dCreatedOn;
	@Column(name = "ac40_quantity")
	private float m_nQuantity;
	@Column(name = "ac40_price")
	private float m_nPrice;
	@Column(name ="ac40_discount")
	@ColumnDefault("0")
	private float m_nDiscount;
	@Column(name = "ac40_tax")
	private float m_nTax;
	@Column(name = "ac40_tax_name")
	private String m_strTaxName;
	@Column(name = "ac40_Created_by")
	private int m_nCreatedBy;
	@Column(name = "ac40_serial_number")
	private int m_nSerialNumber;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ac40_quotationId")
	private QuotationData m_oQuotationData;
	@Column(name = "ac40_Detail")
	private String m_strDetail;
	
	// Used for client side	
	@Transient
	private String m_strArticleNumber;
	@Transient
	private String m_strFromDate;
	@Transient
	private String m_strToDate;
	@Transient
	private String m_strDate;
	
	public QuotationLineItemData() 
	{
		m_nLineItemId = -1;
		m_strArticleDescription = "";
		m_oItemData = new ItemData ();
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_nQuantity = 0;
		m_nPrice = 0;
		m_nDiscount = 0;
		m_nTax = 0;
		m_strTaxName = "";
		m_nCreatedBy = -1;
		m_nSerialNumber = 0;
		m_strDetail = "";
		m_oQuotationData = new QuotationData ();
		m_strArticleNumber = "";
		m_strFromDate = "";
		m_strToDate = "";
		m_strDate = "";
	}
	
	public int getM_nLineItemId() 
	{
		return m_nLineItemId;
	}

	public void setM_nLineItemId(int nLineItemId) 
	{
		m_nLineItemId = nLineItemId;
	}

	public String getM_strArticleDescription() 
	{
		return m_strArticleDescription;
	}

	public void setM_strArticleDescription(String strArticleDescription) 
	{
		m_strArticleDescription = strArticleDescription;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_oItemData(ItemData oItemData)
	{
		m_oItemData = oItemData;
	}

	public Date getM_dCreatedOn()
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public float getM_nQuantity()
	{
		return m_nQuantity;
	}

	public void setM_nQuantity(float nQuantity) 
	{
		m_nQuantity = nQuantity;
	}

	public float getM_nPrice() 
	{
		return m_nPrice;
	}

	public void setM_nPrice(float nPrice) 
	{
		m_nPrice = nPrice;
	}

	public float getM_nDiscount()
	{
		return m_nDiscount;
	}

	public void setM_nDiscount(float nDiscount) 
	{
		m_nDiscount = nDiscount;
	}

	public float getM_nTax() 
	{
		return m_nTax;
	}

	public void setM_nTax(float nTax) 
	{
		m_nTax = nTax;
	}

	public String getM_strTaxName() 
	{
		return m_strTaxName;
	}

	public void setM_strTaxName(String strTaxName)
	{
		m_strTaxName = strTaxName;
	}

	public int getM_nCreatedBy()
	{
		return m_nCreatedBy;
	}

	public void setM_nCreatedBy(int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public int getM_nSerialNumber() 
	{
		return m_nSerialNumber;
	}

	public void setM_nSerialNumber(int nSerialNumber) 
	{
		m_nSerialNumber = nSerialNumber;
	}

	public String getM_strArticleNumber() 
	{
		return m_strArticleNumber;
	}

	public void setM_strArticleNumber(String strArticleNumber) 
	{
		m_strArticleNumber = strArticleNumber;
	}

	public String getM_strFromDate()
	{
		return m_strFromDate;
	}

	public void setM_strFromDate(String strFromDate)
	{
		m_strFromDate = strFromDate;
	}

	public String getM_strToDate() 
	{
		return m_strToDate;
	}

	public void setM_strToDate(String strToDate) 
	{
		m_strToDate = strToDate;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public void setM_strDate(String strDate) 
	{
		m_strDate = strDate;
	}

	public void setM_oQuotationData(QuotationData oQuotationData) 
	{
		m_oQuotationData = oQuotationData;
	}
	
	public QuotationData getM_oQuotationData() 
	{
		return m_oQuotationData;
	}
	
	public void setM_strDetail(String m_strDetail) 
	{
		this.m_strDetail = m_strDetail;
	}

	public String getM_strDetail()
	{
		return m_strDetail;
	}

	@Override
	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strSalesLineItemXml = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "QuotationLineItemData");
			ItemData oItemData = new ItemData ();
			oItemData.setM_nItemId(m_oItemData.getM_nItemId());
			oItemData = (ItemData) GenericIDataProcessor.populateObject(oItemData);
			Document oItemDataXmlDoc = getXmlDocument ("<m_oItemData>" + oItemData.generateXML () + "</m_oItemData>");
			Node oItemDataXmlDocNode = oXmlDocument.importNode (oItemDataXmlDoc.getFirstChild (), true);
			oRootElement.appendChild (oItemDataXmlDocNode);
			addChild (oXmlDocument, oRootElement, "m_nLineItemId", m_nLineItemId);
			addChild (oXmlDocument, oRootElement, "m_strArticleNumber", m_strArticleNumber);
			addChild (oXmlDocument, oRootElement, "m_strArticleDescription", m_strArticleDescription != null && !m_strArticleDescription.isEmpty() ? getArticleDescription(m_strArticleDescription, 0) : oItemData.getM_strItemName());
			addChild (oXmlDocument, oRootElement, "m_strDetail", m_strArticleDescription != null && !m_strArticleDescription.isEmpty() ? getArticleDescription(m_strArticleDescription, 1) : oItemData.getM_strDetail());
			addChild (oXmlDocument, oRootElement, "m_strUnit", oItemData.getM_strUnit());
			addChild (oXmlDocument, oRootElement, "m_nQuantity", m_nQuantity);
			addChild (oXmlDocument, oRootElement, "m_nPrice", m_nPrice);
			addChild (oXmlDocument, oRootElement, "m_nTax", m_nTax);
			addChild (oXmlDocument, oRootElement, "m_nDiscount", m_nDiscount);
			addChild (oXmlDocument, oRootElement, "m_nAmount", (m_nPrice * ((100-m_nDiscount)/100)) * m_nQuantity); 
			strSalesLineItemXml = getXmlString(oXmlDocument);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strSalesLineItemXml;
	}
	
	protected String getArticleDescription(String strArticleDesc, int nIndex) 
	{
		String [] arrDesc = strArticleDesc.split("\\|");
		return arrDesc.length > nIndex ? arrDesc[nIndex] : "";
	}
	
	public void customizeItemData() 
	{
		try 
		{
			CustomizedItemData oCustomItemData = null;
			if(m_oItemData != null && m_oQuotationData != null &&  m_oQuotationData.getM_oClientData().getM_nClientId() > 0 && m_oItemData.getM_nItemId() > 0)
				oCustomItemData = CustomizedItemData.getItemInstance("", m_oQuotationData.getM_oClientData().getM_nClientId(), m_oItemData.getM_nItemId());
			if(oCustomItemData != null)
			{
				m_oItemData.setM_strArticleNumber(oCustomItemData.getM_strClientArticleNumber());
				m_oItemData.setM_strItemName(CustomizedItemData.getArticleDescription(oCustomItemData.getM_strClientArticleDescription(), 0, m_oItemData.getM_strItemName()));
				m_oItemData.setM_strDetail(CustomizedItemData.getArticleDescription(oCustomItemData.getM_strClientArticleDescription(), 1, m_oItemData.getM_strDetail ()));
			}
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("customizeItemData - oException" + oException);
		}
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strOrderBy, String strColumn) 
	{
		Criteria oItemCriteria = oCriteria.createCriteria("m_oItemData");
		if (m_nLineItemId > 0)
			oCriteria.add (Restrictions.eq("m_nLineItemId",m_nLineItemId));
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oCriteria.add (Restrictions.eq ("m_oItemData", m_oItemData));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (m_oItemData != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strItemName", m_oItemData.getM_strItemName().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strBrand", m_oItemData.getM_strBrand().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oItemCriteria.add(Restrictions.eq("m_strArticleNumber", m_oItemData.getM_strArticleNumber().trim()));
		strOrderBy = strOrderBy!= null && strOrderBy.isEmpty()? null : strOrderBy;
		strColumn = strColumn != null && strColumn.isEmpty() ? null : strColumn;
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_dCreatedOn");
		return oCriteria;
	}
	
	public String generateXMLWithSerialNumber(int nIndex)
	{
		String strXML = "<LineItemData>";
		strXML += "<SerialNumber>"+nIndex+ "</SerialNumber>";
		strXML += generateXML();
		strXML += "</LineItemData>";
		return strXML;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
//		Criteria oItemCriteria = oCriteria.createCriteria("m_oItemData");
		if (m_nLineItemId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nLineItemId"), m_nLineItemId)); 
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oItemData"), m_oItemData)); 
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false).toString()), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
		if (m_oItemData != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strItemName")), m_oItemData.getM_strItemName().trim().toLowerCase())); 
		if (m_oItemData != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strBrand")), m_oItemData.getM_strBrand().trim().toLowerCase())); 
		if (m_oItemData != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strArticleNumber"), m_oItemData.getM_strArticleNumber().trim())); 
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nLineItemId"), m_nLineItemId));
		return oConjunct;
	}
}
