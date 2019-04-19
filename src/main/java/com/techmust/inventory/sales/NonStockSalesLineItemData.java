package com.techmust.inventory.sales;

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
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.inventory.purchaseorder.PurchaseOrderLineItemData;
import com.techmust.inventory.supply.SupplyData;
import com.techmust.usermanagement.userinfo.UserInformationData;
@Entity
@Table(name = "tac19_nonstock_sales_lineItems")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NonStockSalesLineItemData extends TenantData
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ac19_id")
	private int m_nLineItemId;
    @Column(name = "ac19_article_description")
	private String m_strArticleDescription;
	@Column(name = "ac19_quantity")
	private float m_nQuantity;
	@Column(name = "ac19_returned_qty")
	@ColumnDefault("0")
	private float m_nReturnedQuantity;
	@Column(name = "ac19_price")
	private float m_nPrice;
	@Column(name = "ac19_discount")
	@ColumnDefault("0")
	private float m_nDiscount;
	@Column(name = "ac19_tax")
	@ColumnDefault("0")
	private float m_nTax;
	@Column(name = "ac19_tax_name")
	private String m_strTaxName;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac19_created_on")
	private Date m_dCreatedOn;
	@Column(name = "ac19_Created_by")
	private int m_nCreatedBy;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ac19_salesId",nullable = false)
	private SalesData m_oSalesData;
	@ManyToOne
	@JoinColumn(name ="ac19_supply_id")
	private SupplyData m_oSupplyData;
	@ManyToOne
	@JoinColumn(name = "ac19_poLineitem_Id",nullable = false)
	private PurchaseOrderLineItemData m_oPOLineItem;
	@Column(name = "ac19_serial_number")
	@ColumnDefault("0")
	private int m_nSerialNumber;
	@Transient
	private boolean m_bIsForLocalPurchaseList;
	
	public NonStockSalesLineItemData ()
	{
		m_nQuantity = 0;
		m_nReturnedQuantity = 0;
		m_bIsForLocalPurchaseList = false;
		m_oSalesData = new SalesData ();
		m_nCreatedBy = -1;
		m_oSupplyData = new SupplyData ();
		m_oPOLineItem = new PurchaseOrderLineItemData ();
	}
	
	public void setM_nLineItemId(int m_nLineItemId)
    {
	    this.m_nLineItemId = m_nLineItemId;
	    m_dCreatedOn = Calendar.getInstance().getTime();
    }

	public int getM_nLineItemId()
    {
	    return m_nLineItemId;
    }

	public void setM_strArticleDescription(String strArticleDescription)
    {
	    this.m_strArticleDescription = strArticleDescription;
    }

	public String getM_strArticleDescription()
    {
	    return m_strArticleDescription;
    }

	public void setM_nQuantity(float nQuantity)
    {
	    this.m_nQuantity = nQuantity;
    }

	public float getM_nQuantity()
    {
	    return m_nQuantity;
    }

	public void setM_nPrice(float nPrice)
    {
	    this.m_nPrice = nPrice;
    }

	public float getM_nPrice()
    {
	    return m_nPrice;
    }

	public void setM_nDiscount(float nDiscount)
    {
	    this.m_nDiscount = nDiscount;
    }

	public float getM_nDiscount()
    {
	    return m_nDiscount;
    }

	public void setM_nTax(float nTax)
    {
	    this.m_nTax = nTax;
    }

	public float getM_nTax()
    {
	    return m_nTax;
    }

	public void setM_strTaxName(String strTaxName)
    {
	    this.m_strTaxName = strTaxName;
    }

	public String getM_strTaxName()
    {
	    return m_strTaxName;
    }

	public void setM_dCreatedOn(Date oCreatedOn)
    {
	    this.m_dCreatedOn = oCreatedOn;
    }

	public Date getM_dCreatedOn()
    {
	    return m_dCreatedOn;
    }

	public void setM_nCreatedBy(int nCreatedBy)
    {
	    this.m_nCreatedBy = nCreatedBy;
    }

	public int getM_nCreatedBy()
    {
	    return m_nCreatedBy;
    }

	public void setM_oSalesData(SalesData oSalesData)
    {
	    this.m_oSalesData = oSalesData;
    }

	public SalesData getM_oSalesData()
    {
	    return m_oSalesData;
    }

	@Override
	public String generateXML()
	{
		m_oLogger.info ("generateXML");
		String strLineItemXml = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "SalesLineItemData");
			addChild (oXmlDocument, oRootElement, "m_nLineItemId", m_nLineItemId);
			addChild (oXmlDocument, oRootElement, "m_strArticleDescription", m_strArticleDescription != null && !m_strArticleDescription.isEmpty() ? getArticleDetail(m_strArticleDescription, 0) : "");
			addChild (oXmlDocument, oRootElement, "m_strDetail", m_strArticleDescription != null && !m_strArticleDescription.isEmpty() ? getArticleDetail(m_strArticleDescription, 1) : "");
			addChild (oXmlDocument, oRootElement, "m_nQuantity", m_nQuantity);
			addChild (oXmlDocument, oRootElement, "m_nPrice", m_nPrice);
			addChild (oXmlDocument, oRootElement, "m_strUnit", m_oPOLineItem.getM_strUnit());
			addChild (oXmlDocument, oRootElement, "m_nTax", m_nTax);
			addChild (oXmlDocument, oRootElement, "m_nDiscount", m_nDiscount);
			addChild (oXmlDocument, oRootElement, "m_nAmount", (m_nPrice * ((100-m_nDiscount)/100)) * m_nQuantity); 
			strLineItemXml = getXmlString(oXmlDocument);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strLineItemXml;
	}

	private String getArticleDetail (String strArticleDescription, int nIndex) 
	{
		String [] arrDesc = strArticleDescription.split("\\|");
		return arrDesc.length > nIndex ? arrDesc[nIndex] : "";
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn,
	        String strOrderBy)
	{
		Criteria oPOLineItemCriteria = oCriteria.createCriteria ("m_oPOLineItem");
		if (!m_strArticleDescription.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strArticleDescription", m_strArticleDescription.trim(), MatchMode.ANYWHERE));
		if (m_nLineItemId > 0)
			oCriteria.add (Restrictions.eq("m_nLineItemId",m_nLineItemId));
		if (m_bIsForLocalPurchaseList)
			oPOLineItemCriteria.add(Restrictions.sqlRestriction("ac15_quantity - ac15_shipped_qty > 0"));
		if (strColumn.contains("m_nQty"))
			oPOLineItemCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strColumn.contains("m_nShippedQty"))
			oPOLineItemCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_dCreatedOn");
		return oCriteria;
	}

	public void create(PurchaseOrderLineItemData oPOLineItem)
    {
		m_strArticleDescription = oPOLineItem.getM_strDesc();
		m_nQuantity = oPOLineItem.getM_nShipQty();
		m_nPrice = oPOLineItem.getM_nPrice();
		m_nDiscount = oPOLineItem.getM_nDiscount();
		m_nTax = oPOLineItem.getM_nTax();
		m_strTaxName = oPOLineItem.getM_strTaxName();
		m_nCreatedBy = oPOLineItem.getM_nCreatedBy();
		m_oPOLineItem = oPOLineItem;
    }

	public void setM_oPOLineItem(PurchaseOrderLineItemData oPOLineItem)
    {
	    this.m_oPOLineItem = oPOLineItem;
    }

	public PurchaseOrderLineItemData getM_oPOLineItem()
    {
	    return m_oPOLineItem;
    }
	
	public int getM_nSerialNumber() 
	{
		return m_nSerialNumber;
	}

	public void setM_nSerialNumber(int nSerialNumber) 
	{
		m_nSerialNumber = nSerialNumber;
	}


	public void setM_oSupplyData(SupplyData oSupplyData) 
	{
		this.m_oSupplyData = oSupplyData;
	}

	public SupplyData getM_oSupplyData() 
	{
		return m_oSupplyData;
	}

	public void setM_nReturnedQuantity(float nReturnedQuantity)
    {
	    this.m_nReturnedQuantity = nReturnedQuantity;
    }

	public float getM_nReturnedQuantity()
    {
	    return m_nReturnedQuantity;
    }
	
	public void returned (float nQuantity) throws Exception
	{
		m_nReturnedQuantity += nQuantity;
		m_oPOLineItem.returned(nQuantity);
		updateObject();
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
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
//		Criteria oPOLineItemCriteria = oCriteria.createCriteria ("m_oPOLineItem");
		if (!m_strArticleDescription.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strArticleDescription")), m_strArticleDescription.trim().toLowerCase())); 
		if (m_nLineItemId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nLineItemId"), m_nLineItemId)); 
/*		
		if (m_bIsForLocalPurchaseList)
			oPOLineItemCriteria.add(Restrictions.sqlRestriction("ac15_quantity - ac15_shipped_qty > 0"));
		if (strColumn.contains("m_nQty"))
			oPOLineItemCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strColumn.contains("m_nShippedQty"))
			oPOLineItemCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_dCreatedOn");
*/
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nLineItemId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nLineItemId"), m_nLineItemId)); 
		return oConjunct;
	}
}
