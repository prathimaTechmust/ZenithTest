package com.techmust.inventory.purchaseorder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ContactData;
import com.techmust.clientmanagement.SiteData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.challan.ChallanData;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.items.VendorItemData;
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.inventory.quotation.QuotationData;
import com.techmust.inventory.sales.NonStockItemException;
import com.techmust.inventory.sales.SalesData;
import com.techmust.inventory.sales.SalesLineItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@Entity
@Table(name = "tac14_purchaseorder")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrderData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac14_purchaseorder_id")
	private int m_nPurchaseOrderId;
	@ManyToOne
	@JoinColumn(name = "ac14_Client_id")
	@ColumnDefault("-1")
	private ClientData m_oClientData;
	@ManyToOne
	@JoinColumn(name = "ac14_Site_id")
	@ColumnDefault("-1")
	private SiteData m_oSiteData;
	@ManyToOne
	@JoinColumn(name = "ac14_Contact_id")
	private ContactData m_oContactData;
	@Column(name = "ac14_purchaseorder_number")
	private String m_strPurchaseOrderNumber;
	@Column(name = "ac14_purchaseorder_date")
	private Date m_dPurchaseOrderDate;
	@Transient
	private String m_strPurchaseOrderDate;
	@Column(name = "ac14_Created_by")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac14_created_on")
	private Date m_dCreatedOn;
	@Column(name = "ac14_purchaseorder_status")
	@Enumerated(EnumType.ORDINAL)
	private PurchaseOrderStatus m_nPurchaseOrderStatus;
	
	@JsonManagedReference
	@OneToMany
	@JoinColumn(name = "ac15_puchaseOrder_id")
	private Set<PurchaseOrderLineItemData> m_oPurchaseOrderLineItems;
	@Transient
	public PurchaseOrderLineItemData [] m_arrPurchaseOrderLineItems;
	
	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "ac14_purchaseorder_id")
	private Set<SalesData> m_oSalesSet;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Transient
	public ChallanData[] m_arrChallans;
	@ManyToOne
	@JoinColumn(name = "ac14_quotationId")
	private QuotationData m_oQuotationData;
	@Transient
	private boolean m_bIsForAllList;
	@Transient
	private String m_strFromDate = "";
	@Transient
    private String m_strToDate = "";
    @Column(name = "ac15_isCForm_provided")
    @ColumnDefault("0")
    private boolean m_bIsCFormProvided;
    @Transient
	private boolean m_bIsForVendorOrder;
    @Transient
    private int m_nVendorId;
	
	public PurchaseOrderData ()
	{
		m_nPurchaseOrderId = -1;
		m_strPurchaseOrderNumber = "";
		m_oClientData = new ClientData ();
		m_oSiteData = new SiteData ();
		m_dPurchaseOrderDate = Calendar.getInstance().getTime();
		m_dCreatedOn = Calendar.getInstance().getTime();
		setM_nPurchaseOrderStatus(PurchaseOrderStatus.kPending);
		m_strPurchaseOrderDate="";
		m_oUserCredentialsData = new UserInformationData ();
		m_oPurchaseOrderLineItems = new HashSet<PurchaseOrderLineItemData> ();
		m_oSalesSet = new HashSet<SalesData> ();
		m_bIsCFormProvided = false;
		m_bIsForVendorOrder = false;
		m_nVendorId = -1;
		m_arrChallans = new ChallanData [100];
	}

	public int getM_nPurchaseOrderId() 
	{
		return m_nPurchaseOrderId;
	}

	public void setM_nPurchaseOrderId(int nPurchaseOrderId) 
	{
		m_nPurchaseOrderId = nPurchaseOrderId;
	}

	public ClientData getM_oClientData() 
	{
		return m_oClientData;
	}

	public void setM_oClientData(ClientData oClientData) 
	{
		m_oClientData = oClientData;
	}

	public SiteData getM_oSiteData() 
	{
		return m_oSiteData;
	}

	public void setM_oContactData(ContactData oContactData) 
	{
		this.m_oContactData = oContactData;
	}

	public ContactData getM_oContactData() 
	{
		return m_oContactData;
	}

	public void setM_oSiteData(SiteData oSiteData) 
	{
		m_oSiteData = oSiteData;
	}

	public String getM_strPurchaseOrderNumber() 
	{
		return m_strPurchaseOrderNumber;
	}

	public void setM_strPurchaseOrderNumber(String strPurchaseOrderNumber) 
	{
		m_strPurchaseOrderNumber = strPurchaseOrderNumber;
	}

	public Date getM_dPurchaseOrderDate() 
	{
		return m_dPurchaseOrderDate;
	}

	public void setM_dPurchaseOrderDate(Date dPurchaseOrderDate) 
	{
		m_dPurchaseOrderDate = dPurchaseOrderDate;
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

	public PurchaseOrderStatus getM_nPurchaseOrderStatus() 
	{
		return m_nPurchaseOrderStatus;
	}

	public void setM_nPurchaseOrderStatus(PurchaseOrderStatus nPurchaseOrderStatus) 
	{
		m_nPurchaseOrderStatus = nPurchaseOrderStatus;
	}

	public void setM_oPurchaseOrderLineItems(Set<PurchaseOrderLineItemData> oPurchaseOrderLineItems)
	{
		this.m_oPurchaseOrderLineItems = oPurchaseOrderLineItems;
	}

	public Set<PurchaseOrderLineItemData> getM_oPurchaseOrderLineItems() 
	{
		return m_oPurchaseOrderLineItems;
	}

	public void setM_strPurchaseOrderDate(String strPurchaseOrderDate) 
	{
		this.m_strPurchaseOrderDate = strPurchaseOrderDate;
	}

	public String getM_strPurchaseOrderDate() 
	{
		return m_strPurchaseOrderDate;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		this.m_oUserCredentialsData = oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}
	
	public void setM_oQuotationData(QuotationData m_oQuotationData) 
	{
		this.m_oQuotationData = m_oQuotationData;
	}

	public QuotationData getM_oQuotationData()
	{
		return m_oQuotationData;
	}
	
	public boolean isM_bIsForVendorOrder() 
	{
		return m_bIsForVendorOrder;
	}

	public void setM_bIsForVendorOrder(boolean bIsForVendorOrder) 
	{
		m_bIsForVendorOrder = bIsForVendorOrder;
	}

	public void setM_oSalesSet(Set<SalesData> oSalesSet)
    {
	    this.m_oSalesSet = oSalesSet;
    }

	public Set<SalesData> getM_oSalesSet()
    {
	    return m_oSalesSet;
    }

	public void setM_strFromDate (String strFromDate) 
	{
		this.m_strFromDate = strFromDate;
	}

	public String getM_strFromDate () 
	{
		return m_strFromDate;
	}

	public void setM_strToDate (String strToDate) 
	{
		this.m_strToDate = strToDate;
	}

	public String getM_strToDate () 
	{
		return m_strToDate;
	}

	public int getM_nVendorId() 
	{
		return m_nVendorId;
	}

	public void setM_nVendorId(int nVendorId) 
	{
		m_nVendorId = nVendorId;
	}

	public void setM_bIsCFormProvided(boolean bIsCFormProvided) 
	{
		this.m_bIsCFormProvided = bIsCFormProvided;
	}

	public boolean isM_bIsCFormProvided() 
	{
		return m_bIsCFormProvided;
	}

	@Override
	public String generateXML() 
	{
		String strPurchaseOrderDataXml = "";
		m_oLogger.info("generateXML ");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "PurchaseOrderData");
			addChild (oXmlDocument, oRootElement, "m_nPurchaseOrderId", m_nPurchaseOrderId);
			addChild (oXmlDocument, oRootElement, "m_dPurchaseOrderDate", m_dPurchaseOrderDate != null ? m_dPurchaseOrderDate.toString() : "");
			addChild (oXmlDocument, oRootElement, "m_strPurchaseOrderNumber", m_strPurchaseOrderNumber);
			Document oClientDoc = getXmlDocument (m_oClientData.generateXML());
			Node oClientNode = oXmlDocument.importNode (oClientDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
			Document oSiteDoc = getXmlDocument (m_oSiteData.generateXML());
			Node oSiteNode = oXmlDocument.importNode (oSiteDoc.getFirstChild (), true);
			oRootElement.appendChild (oSiteNode);
			strPurchaseOrderDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strPurchaseOrderDataXml;
	}
	
	public void addChallan(ChallanData oChallanData) throws Exception
    {
	    Iterator<PurchaseOrderLineItemData> oPOLineItems = m_oPurchaseOrderLineItems.iterator();
	    while (oPOLineItems.hasNext())
	    	oPOLineItems.next ().addChallan (oChallanData);
	    updateObject();
    }

	@JsonIgnore
	public ArrayList<ChallanData> getUnbilledChallans()
    {
		HashMap<Integer, ChallanData> oChallans = new HashMap<Integer, ChallanData>();
		Iterator<PurchaseOrderLineItemData> oPOLineItems = m_oPurchaseOrderLineItems.iterator();
		while (oPOLineItems.hasNext())
			oChallans = oPOLineItems.next().getUnbilledChallans (oChallans);
		return new ArrayList<ChallanData> (oChallans.values());
    }
	
	@JsonIgnore
	public ArrayList<ChallanData> getChallans() 
	{
		HashMap<Integer, ChallanData> oChallans = new HashMap<Integer, ChallanData>();
		Iterator<PurchaseOrderLineItemData> oPOLineItems = m_oPurchaseOrderLineItems.iterator();
		while (oPOLineItems.hasNext())
			oChallans = oPOLineItems.next().getChallans (oChallans);
		return new ArrayList<ChallanData> (oChallans.values());
	}
	
	@JsonIgnore
	public ArrayList<InvoiceData> getInvoices() 
	{
		HashMap<Integer, InvoiceData> oInvoices = new HashMap<Integer, InvoiceData>();
		Iterator<PurchaseOrderLineItemData> oPOLineItems = m_oPurchaseOrderLineItems.iterator();
		while (oPOLineItems.hasNext())
			oInvoices = oPOLineItems.next().getInvoices (oInvoices);
		return new ArrayList<InvoiceData> (oInvoices.values());
	}

	@JsonIgnore
	public ArrayList<PurchaseData> getPurchases() 
	{
		HashMap<Integer, PurchaseData> oPurchases = new HashMap<Integer, PurchaseData>();
		Iterator<PurchaseOrderLineItemData> oPOLineItems = m_oPurchaseOrderLineItems.iterator();
		while (oPOLineItems.hasNext())
			oPurchases = oPOLineItems.next().getPurchases (oPurchases);
		return new ArrayList<PurchaseData> (oPurchases.values());
	}
	
	@JsonIgnore
	public SalesData createSalesData() throws Exception
    {
		SalesData oSalesData = new SalesData ();
		oSalesData.create (this);
		addSales(oSalesData);
		oSalesData.saveObject();
		oSalesData = (SalesData) GenericIDataProcessor.populateObject(oSalesData);
	    return oSalesData;
    }

	public void addSales(SalesData oSalesData)
    {
		oSalesData.setM_oPOData(this);
	    m_oSalesSet.add(oSalesData);
    }

	public void prepareForUpdate() throws Exception
    {
		try
		{
			if(this.getM_nPurchaseOrderId() > 0)
			{	
				PurchaseOrderData oPOData = (PurchaseOrderData) GenericIDataProcessor.populateObject(this);
				m_nPurchaseOrderStatus = oPOData.m_nPurchaseOrderStatus;
			}
		}
		catch (Exception oException)
		{
			// 
		}
		
		m_oLogger.info("preparePurchaseOrderData");
		for (int nIndex = 0 ; m_arrPurchaseOrderLineItems != null && nIndex < m_arrPurchaseOrderLineItems.length; nIndex++ )
		{
			PurchaseOrderLineItemData oPOLineItem = m_arrPurchaseOrderLineItems[nIndex];
			oPOLineItem.prepareForUpdate (this);
			addLineItem(oPOLineItem);
		}
		setM_dPurchaseOrderDate(GenericIDataProcessor.getDBCompatibleDateFormat(getM_strPurchaseOrderDate()));
    }

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if(m_bIsForVendorOrder && m_nVendorId > 0)
		{
			Criteria oPOLineItemDataCriteria = oCriteria.createCriteria ("m_oPurchaseOrderLineItems");
			Criteria oPODataCriteria = oPOLineItemDataCriteria.createCriteria("m_oPurchaseOrderStockLineItems");
			DetachedCriteria oDetachedCriteria = DetachedCriteria.forClass(VendorItemData.class).setProjection(Property.forName("m_oItemData"));
			oDetachedCriteria.add(Restrictions.eq("m_oVendorData.m_nClientId", m_nVendorId));
			oPODataCriteria.add(Subqueries.propertyIn("m_oItemData", oDetachedCriteria));
		}
		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
		Criteria oSiteDataCriteria = oCriteria.createCriteria ("m_oSiteData");
		if (m_nPurchaseOrderId > 0)
			oCriteria.add (Restrictions.eq ("m_nPurchaseOrderId", m_nPurchaseOrderId));
		if (m_strPurchaseOrderNumber != null && !m_strPurchaseOrderNumber.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strPurchaseOrderNumber", m_strPurchaseOrderNumber.trim(),MatchMode.ANYWHERE));
		if(m_oClientData.getM_strCompanyName() != null && !m_oClientData.getM_strCompanyName().trim().isEmpty())
			oClientDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if(m_oSiteData.getM_strSiteName() != null && !m_oSiteData.getM_strSiteName().trim().isEmpty())
			oSiteDataCriteria.add(Restrictions.ilike ("m_strSiteName", m_oSiteData.getM_strSiteName().trim(), MatchMode.ANYWHERE));
		if(m_strPurchaseOrderDate != null && !m_strPurchaseOrderDate.trim().isEmpty())
			oCriteria.add(Restrictions.eq("m_dPurchaseOrderDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strPurchaseOrderDate.trim(), true)));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oClientData", m_oClientData));
		if (m_oContactData != null && m_oContactData.getM_nContactId() > 0)
		{
			Criteria oContactDataCriteria = oCriteria.createCriteria("m_oContactData");
			oContactDataCriteria.add(Restrictions.eq("m_nContactId", m_oContactData.getM_nContactId()));
		}
		if (m_oSiteData != null && m_oSiteData.getM_nSiteId() > 0)
			oSiteDataCriteria.add(Restrictions.eq("m_nSiteId", m_oSiteData.getM_nSiteId()));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dPurchaseOrderDate", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dPurchaseOrderDate", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dPurchaseOrderDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (!m_bIsForAllList)
			oCriteria.add(Restrictions.eq("m_nPurchaseOrderStatus", m_nPurchaseOrderStatus));
		if (strColumn.contains("m_strCompanyName"))
			oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strColumn.contains("m_strSiteName"))
			oSiteDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nPurchaseOrderId");
		return oCriteria;
	}
	
	public void invoiced(InvoiceData oInvoiceData) throws Exception
    {
		Iterator<PurchaseOrderLineItemData> oPOLineItems = m_oPurchaseOrderLineItems.iterator();
		while (oPOLineItems.hasNext())
			oPOLineItems.next().invoiced (oInvoiceData);
		updateStatus ();
		updateObject();
    }

	public void updateStatus()
    {
		PurchaseOrderStatus oStatus = PurchaseOrderStatus.kDelivered;
		Iterator<PurchaseOrderLineItemData> oPOLineItems = m_oPurchaseOrderLineItems.iterator();
		while (oPOLineItems.hasNext() && oStatus == PurchaseOrderStatus.kDelivered)
		{
			if (!oPOLineItems.next().isDelivered ())
				oStatus = PurchaseOrderStatus.kPending;
		}
		m_nPurchaseOrderStatus = oStatus;
    }

	public void createSales(InvoiceData oInvoiceData) throws NonStockItemException, Exception
    {
		SalesData oSalesData = new SalesData ();
		oSalesData.create (this);
		if (oSalesData.hasLineItems ())
		{
			updateLineItems (oInvoiceData);
			addSales(oSalesData);
			oSalesData.saveObject();
			oInvoiceData.addSales((SalesData)GenericIDataProcessor.populateObject(oSalesData));
			oInvoiceData.updateObject();
		}
    }
	
	private void addLineItem (PurchaseOrderLineItemData oPurchaseOrderLineItemData) 
	{
		m_oPurchaseOrderLineItems.add(oPurchaseOrderLineItemData);
	}

	private void updateLineItems(InvoiceData oInvoiceData) throws Exception
    {
	    Iterator<PurchaseOrderLineItemData> oLineItems = m_oPurchaseOrderLineItems.iterator();
	    while (oLineItems.hasNext())
	    	oLineItems.next().updateLineItem (oInvoiceData);
    }

	public void returned(SalesLineItemData oSLIData, float nQuantity)
    {
	    Iterator<PurchaseOrderLineItemData> oLineItems = m_oPurchaseOrderLineItems.iterator();
	    while (oLineItems.hasNext())
	    	oLineItems.next().returned (oSLIData, nQuantity);
    }

	public boolean canInvoice()
    {
	    boolean bCanInvoice = false;
	    bCanInvoice = m_arrChallans.length > 0 || getShipQuantity () > 0;
	    return bCanInvoice;
    }

	private int getShipQuantity()
    {
	   int nShipQty = 0;
	   for (int nIndex = 0; nIndex < m_arrPurchaseOrderLineItems.length; nIndex++)
		   nShipQty += m_arrPurchaseOrderLineItems[nIndex].getM_nShipQty();
	    return nShipQty;
    }

	public void mergeClient(ClientData oClientData) throws Exception
	{
		ClientData oClient = new ClientData ();
		oClient.setM_nClientId(oClientData.getM_nClientId());
		setM_oClientData(oClient);
		setM_oSiteData(getSiteData (oClientData));
		setM_oContactData(getContact (oClientData));
		updateObject();
	}

	private ContactData getContact(ClientData oClientData) 
	{
		ContactData oContactData = null;
		Iterator<ContactData> oContacts = oClientData.getM_oContacts().iterator();
		while (oContacts.hasNext())
			oContactData = oContacts.next();
		return oContactData;
	}

	private SiteData getSiteData(ClientData oClientData) 
	{
		SiteData oSiteData = null;
		Iterator<SiteData> oSites = oClientData.getM_oSites().iterator();
		while (oSites.hasNext())
			oSiteData = oSites.next();
		return oSiteData;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PurchaseOrderData> getList() throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		return new ArrayList (this.list(oOrderBy));
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void customizePOLineItems() 
	{
		Iterator<PurchaseOrderLineItemData> oIterator = m_oPurchaseOrderLineItems.iterator();
		while (oIterator.hasNext())
		{
			PurchaseOrderLineItemData oPOLineItemData = oIterator.next();
			oPOLineItemData.customizeStockLineItems ();
		}
	}

	public double calculateOrderTotal(PurchaseOrderLineItemData[] arrOrderLineItems) 
	{
		float nTotal = 0;
		for (int nIndex = 0; nIndex < arrOrderLineItems.length; nIndex ++)
			nTotal += getLineItemTotal(arrOrderLineItems[nIndex]);
		return nTotal;
	}
	
	private float getLineItemTotal(Object oLineItem) 
	{
		float nTotal = 0;
		PurchaseOrderLineItemData oPurchaseOrderLineItemData = (PurchaseOrderLineItemData) oLineItem;
		nTotal = calculateTotalAmount (oPurchaseOrderLineItemData.getM_nPrice(), oPurchaseOrderLineItemData.getM_nQty(), oPurchaseOrderLineItemData.getM_nDiscount(), oPurchaseOrderLineItemData.getM_nTax());
		return nTotal;
	}

	private float calculateTotalAmount(float nPrice, float nQuantity, float nDiscount, float nTax) 
	{
		float nAmount = 0;
		nAmount =  nQuantity * nPrice ;
		nAmount -= nAmount *(nDiscount/100);
		nAmount += nAmount *(nTax/100);
		return nAmount;
	}

	public String addVendorToXML(Document oXmlDocument, VendorData oVendorData) 
	{
		String strXML ="";
		try
		{
			if(oVendorData != null)
			{
				Element oRootElement = oXmlDocument.getDocumentElement();
				Document oVendorDataXmlDoc = getXmlDocument ("<m_oVendorData>" + oVendorData.generateXML () + "</m_oVendorData>");
				Node oVendorDataNode = oXmlDocument.importNode (oVendorDataXmlDoc.getFirstChild (), true);
				oRootElement.appendChild (oVendorDataNode);
				strXML = getXmlString (oXmlDocument);
			}
		}
		catch (Exception oException) 
		{
			m_oLogger.error("addVendorToXML - oException : " + oException);
		}
	    m_oLogger.debug( "addVendorToXML - strXML [OUT] : " + strXML);
	    return strXML;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*
 		if(m_bIsForVendorOrder && m_nVendorId > 0)
 		{
			Criteria oPOLineItemDataCriteria = oCriteria.createCriteria ("m_oPurchaseOrderLineItems");
			Criteria oPODataCriteria = oPOLineItemDataCriteria.createCriteria("m_oPurchaseOrderStockLineItems");
			DetachedCriteria oDetachedCriteria = DetachedCriteria.forClass(VendorItemData.class).setProjection(Property.forName("m_oItemData"));
			oDetachedCriteria.add(Restrictions.eq("m_oVendorData.m_nClientId", m_nVendorId));
			oPODataCriteria.add(Subqueries.propertyIn("m_oItemData", oDetachedCriteria));
		}
		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
		Criteria oSiteDataCriteria = oCriteria.createCriteria ("m_oSiteData");
		if (m_nPurchaseOrderId > 0)
			oCriteria.add (Restrictions.eq ("m_nPurchaseOrderId", m_nPurchaseOrderId));
		if (m_strPurchaseOrderNumber != null && !m_strPurchaseOrderNumber.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strPurchaseOrderNumber", m_strPurchaseOrderNumber.trim(),MatchMode.ANYWHERE));
		if(m_oClientData.getM_strCompanyName() != null && !m_oClientData.getM_strCompanyName().trim().isEmpty())
			oClientDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if(m_oSiteData.getM_strSiteName() != null && !m_oSiteData.getM_strSiteName().trim().isEmpty())
			oSiteDataCriteria.add(Restrictions.ilike ("m_strSiteName", m_oSiteData.getM_strSiteName().trim(), MatchMode.ANYWHERE));
		if(m_strPurchaseOrderDate != null && !m_strPurchaseOrderDate.trim().isEmpty())
			oCriteria.add(Restrictions.eq("m_dPurchaseOrderDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strPurchaseOrderDate.trim(), true)));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oClientData", m_oClientData));
		if (m_oContactData != null && m_oContactData.getM_nContactId() > 0)
		{
			Criteria oContactDataCriteria = oCriteria.createCriteria("m_oContactData");
			oContactDataCriteria.add(Restrictions.eq("m_nContactId", m_oContactData.getM_nContactId()));
		}
		if (m_oSiteData != null && m_oSiteData.getM_nSiteId() > 0)
			oSiteDataCriteria.add(Restrictions.eq("m_nSiteId", m_oSiteData.getM_nSiteId()));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dPurchaseOrderDate", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dPurchaseOrderDate", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dPurchaseOrderDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (!m_bIsForAllList)
			oCriteria.add(Restrictions.eq("m_nPurchaseOrderStatus", m_nPurchaseOrderStatus));
		if (strColumn.contains("m_strCompanyName"))
			oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strColumn.contains("m_strSiteName"))
			oSiteDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nPurchaseOrderId");
*/
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nPurchaseOrderId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nPurchaseOrderId"), m_nPurchaseOrderId));
		return oConjunct;
	}
}
