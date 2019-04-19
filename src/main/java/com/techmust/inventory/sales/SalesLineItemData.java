package com.techmust.inventory.sales;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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
import org.hibernate.annotations.Formula;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.ItemDataProcessor;
import com.techmust.inventory.purchaseorder.PurchaseOrderLineItemData;
import com.techmust.inventory.purchaseorder.PurchaseOrderStockLineItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name="tac07_sales_lineItems")
public class SalesLineItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ac07_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int m_nLineItemId;
	@Column(name = "ac07_article_description")
	protected String m_strArticleDescription;
	@ManyToOne
	@JoinColumn(name="ac07_item_id")
	protected ItemData m_oItemData;
	@Column(name="ac07_created_on")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	protected Date m_dCreatedOn;
	@Column(name = "ac07_quantity")
	protected float m_nQuantity;
	@Column(name = "ac07_returned_qty")
	@ColumnDefault("0")
	protected float m_nReturnedQuantity;
	@Column(name="ac07_price")
	protected float m_nPrice;
	@Column(name = "ac07_discount")
	@ColumnDefault("0")
	protected float m_nDiscount;
	@Column(name = "ac07_tax")
	@ColumnDefault("0")
	protected float m_nTax;
	@Column(name="ac07_tax_name")
	protected String m_strTaxName;
	@Column(name="ac07_Created_by")
	protected int m_nCreatedBy;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="ac07_salesId")
	protected SalesData m_oSalesData;
	@Column(name="ac07_serial_number")
	@ColumnDefault("0")
	protected int m_nSerialNumber;
	
	
	// Used for client side
	@Transient
	protected String m_strArticleNumber;
	@Transient
	protected String m_strFromDate;
	@Transient
	protected String m_strToDate;
	@Transient
	protected int m_nMaxResult;
	@Transient
	protected String m_strDate;
	@Transient
	private boolean m_bForStockMovementReport;
	@Formula("m_nPrice * m_nQuantity")
	private double m_nAmount;

	public SalesLineItemData ()
	{
		m_nLineItemId = -1;
		setM_strArticleNumber("");
		m_oItemData = new ItemData ();
		setM_dCreatedOn(Calendar.getInstance().getTime());
		m_nQuantity = 0;
		m_nReturnedQuantity = 0;
		m_nPrice = 0;
		m_nDiscount = 0;
		m_nMaxResult = 0;
		m_nTax = 0;
		m_strTaxName = "";
		m_strArticleDescription = "";
		m_nSerialNumber = 0;
		m_oSalesData = new SalesData ();
		setM_nAmount(0);
		setM_bForStockMovementReport(false);
	}
	
	public int getM_nLineItemId() 
	{
		return m_nLineItemId;
	}

	public void setM_nLineItemId(int nLineItemId) 
	{
		m_nLineItemId = nLineItemId;
	}

	public String getM_strArticleDescription() {
		return m_strArticleDescription;
	}

	public void setM_strArticleDescription(String articleDescription) {
		m_strArticleDescription = articleDescription;
	}
	
	public void setM_strArticleDescriptions(ItemData oItemData) 
	{
		if(m_strArticleDescription == null || m_strArticleDescription.isEmpty())
			m_strArticleDescription = oItemData.getM_strArticleNumber() + "|" + oItemData.getM_strDetail() + "|" + oItemData.getM_strArticleNumber();
	}

	public void setM_strArticleNumber(String strArticleNumber) 
	{
		m_strArticleNumber = strArticleNumber;
	}

	public String getM_strArticleNumber() 
	{
		return m_strArticleNumber;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_oItemData(ItemData oItemData) 
	{
		m_oItemData = oItemData;
	}
	
	public void setM_nQuantity (float nQuantity) 
	{
		this.m_nQuantity = nQuantity;
	}

	public float getM_nQuantity()
	{
		return m_nQuantity;
	}
	
	public void setM_nPrice (float nPrice) 
	{
		this.m_nPrice = nPrice;
	}

	public float getM_nPrice ()
	{
		return m_nPrice;
	}


	public float getM_nDiscount()
	{
		return m_nDiscount;
	}

	public void setM_nDiscount(float nDiscount) 
	{
		m_nDiscount = nDiscount;
	}

	public void setM_strFromDate(String m_strFromDate) 
	{
		this.m_strFromDate = m_strFromDate;
	}

	public String getM_strFromDate() 
	{
		return m_strFromDate;
	}

	public void setM_strToDate(String m_strToDate) 
	{
		this.m_strToDate = m_strToDate;
	}

	public String getM_strToDate()
	{
		return m_strToDate;
	}
	
	public void setM_nMaxResult(int nMaxResult)
	{
		this.m_nMaxResult = nMaxResult;
	}

	public int getM_nMaxResult() 
	{
		return m_nMaxResult;
	}

	public void setM_strDate(String strDate) 
	{
		this.m_strDate = strDate;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}
	
	public int getM_nSerialNumber() 
	{
		return m_nSerialNumber;
	}

	public void setM_nSerialNumber(int nSerialNumber) 
	{
		m_nSerialNumber = nSerialNumber;
	}

	@Override
	public String generateXML() 
	{
		m_oLogger.info ("generateXML");
		String strSalesLineItemXml = "";
		try 
		{
			
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "SalesLineItemData");
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

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (m_bForStockMovementReport)
			buildStockMovementCriteria (oCriteria, strColumn, strOrderBy);
		else
			buildListCriteria (oCriteria, strColumn, strOrderBy);
		return oCriteria;
	}

	private void buildStockMovementCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if(m_oItemData != null && (!m_oItemData.getM_strItemName().trim().isEmpty() || !m_oItemData.getM_strBrand().trim().isEmpty() || !m_oItemData.getM_strArticleNumber().trim().isEmpty()))
			buildItemCriteria (oCriteria, strColumn, strOrderBy);
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("m_oItemData"));
		oProjectionList.add(Projections.sum("m_nQuantity"));
		oCriteria.setProjection(oProjectionList);
	}

	private void buildItemCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oItemCriteria = oCriteria.createCriteria("m_oItemData");
		if (m_oItemData != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strItemName", m_oItemData.getM_strItemName().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oItemCriteria.add(Restrictions.ilike("m_strBrand", m_oItemData.getM_strBrand().trim(), MatchMode.ANYWHERE));
		if (m_oItemData != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oItemCriteria.add(Restrictions.eq("m_strArticleNumber", m_oItemData.getM_strArticleNumber().trim()));
	}

	private void buildListCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
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
		if (m_nMaxResult > 0)
			oCriteria.setMaxResults(getM_nMaxResult ());
	}
	
	public void setM_dCreatedOn (Date dCreatedOn) 
	{
		this.m_dCreatedOn = dCreatedOn;
	}

	public Date getM_dCreatedOn () 
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

	public void setM_oItemDatas (int nItemId)
	{
		m_oItemData.setM_nItemId(nItemId);
	}

	public void setM_nReturnedQuantity(float nReturnedQuantity)
    {
	    this.m_nReturnedQuantity = nReturnedQuantity;
    }

	public float getM_nReturnedQuantity()
    {
	    return m_nReturnedQuantity;
    }
	
	public void setM_nAmount(double m_nAmount) 
	{
		this.m_nAmount = m_nAmount;
	}

	public double getM_nAmount() 
	{
		return m_nAmount;
	}

	public void setM_bForStockMovementReport(boolean bForStockMovementReport) 
	{
		this.m_bForStockMovementReport = bForStockMovementReport;
	}

	public boolean isM_bForStockMovementReport() 
	{
		return m_bForStockMovementReport;
	}

	public void returned (float nQuantity)
	{
		m_nReturnedQuantity += nQuantity;
		try
		{
			updateObject();
			m_oItemData.received(nQuantity);
			m_oItemData.updateObject();
			m_oSalesData.returned (this, nQuantity);
		}
		catch (Exception oException)
		{
			
		}
	}

	public void create(PurchaseOrderLineItemData oPOLineItem, boolean bIsForOutOfStateClient) throws NonStockItemException, Exception
    {
		Iterator<PurchaseOrderStockLineItemData> oStockLineItems = oPOLineItem.getM_oPurchaseOrderStockLineItems().iterator();
		if (oStockLineItems.hasNext())
		{
			PurchaseOrderStockLineItemData oStockLineItem = oStockLineItems.next();
			ItemData oItemData = ItemDataProcessor.getItem(oStockLineItem.getM_oItemData().getM_nItemId(), null);
			setM_strArticleDescription(oPOLineItem.getM_strDesc());
			setM_strArticleNumber(oItemData.getM_strArticleNumber());
			setM_oItemDatas(oItemData.getM_nItemId());
			setM_nCreatedBy(oPOLineItem.getM_nCreatedBy());
			setM_strTaxName(oItemData.getTaxName(bIsForOutOfStateClient));
			setM_nQuantity(oPOLineItem.getM_nShipQty());
			setM_nPrice(oPOLineItem.getM_nPrice());
			setM_nDiscount(oPOLineItem.getM_nDiscount());
			setM_nTax(oPOLineItem.getM_nTax());
		}
		else
			throw new NonStockItemException ();
    }

	@SuppressWarnings("unchecked")
	public static ArrayList<GenericData> getPreviousSales(
			String strArticleNumber, int nClientId,
			GenericIDataProcessor oCustomDataProcessor) throws Exception
	{
		SalesLineItemData oSalesLineItem = new SalesLineItemData ();
		oSalesLineItem.m_oItemData = ItemData.getInstance(strArticleNumber, null);
		oSalesLineItem.m_oSalesData = SalesData.getInstance(nClientId);
		return oSalesLineItem.listCustomData(oCustomDataProcessor);
	}

	public String generateXMLWithSerialNumber(int nIndex)
	{
		String strXML = "<LineItemData>";
		strXML += "<SerialNumber>"+nIndex+ "</SerialNumber>";
		strXML += generateXML();
		strXML += "</LineItemData>";
		return strXML;
	}
	
	public void customizeItemData() 
	{
		try 
		{
			CustomizedItemData oCustomItemData = null;
			if(m_oItemData != null && m_oSalesData != null &&  m_oSalesData.getM_oClientData().getM_nClientId() > 0 && m_oItemData.getM_nItemId() > 0)
				oCustomItemData = CustomizedItemData.getItemInstance("", m_oSalesData.getM_oClientData().getM_nClientId(), m_oItemData.getM_nItemId());
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
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList getStockMovementForPeriod (String strFromDate, String strToDate, ItemData oItemData) throws Exception
	{
		SalesLineItemData oSalesLineItemData = new SalesLineItemData ();
		oSalesLineItemData.setM_strFromDate(strFromDate);
		oSalesLineItemData.setM_strToDate(strToDate);
		oSalesLineItemData.setM_bForStockMovementReport(true);
		oSalesLineItemData.setM_oItemData(oItemData);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		ArrayList arrSalesStockMovement = oSalesLineItemData.list(oOrderBy);
		return arrSalesStockMovement;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*
 		if (m_bForStockMovementReport)
			buildStockMovementCriteria (oCriteria, strColumn, strOrderBy);
		else
			buildListCriteria (oCriteria, strColumn, strOrderBy);
*/
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
