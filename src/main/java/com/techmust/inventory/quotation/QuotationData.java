package com.techmust.inventory.quotation;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ContactData;
import com.techmust.clientmanagement.SiteData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.invoice.TaxDiscountInfo;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.purchaseorder.PurchaseOrderData;
import com.techmust.inventory.sales.ClientArticleData;
import com.techmust.master.tax.Tax;
import com.techmust.traderp.util.NumberToWords;
import com.techmust.traderp.util.TraderpUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac39_quotation")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac39_quotation_id")
	private int m_nQuotationId;
	@ManyToOne
	@JoinColumn(name = "ac39_Client_id")
	private ClientData m_oClientData;
	@ManyToOne
	@JoinColumn(name = "ac39_Site_id")
	private SiteData m_oSiteData;
	@ManyToOne
	@JoinColumn(name = "ac39_Contact_id")
	private ContactData m_oContactData;
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "ac39_quotation_date")
	private Date m_dDate;
	@Column(name = "ac39_quotation_number")
	private String m_strQuotationNumber;
	@Transient
	private String m_strDate;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac39_created_on")
	private Date m_dCreatedOn;
	@Column(name ="ac39_Created_by")
	private int m_nCreatedBy;
	
	@JsonManagedReference
	@OneToMany
	@JoinColumn(name = "ac40_quotationId")
	private Set<QuotationLineItemData> m_oQuotationLineItems;
	@Transient
	public QuotationLineItemData [] m_arrQuotationLineItems;
	
	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "ac14_quotationId")
	private Set<PurchaseOrderData> m_oPurchaseOrders;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Column(name = "ac39_terms")
	private String m_strTermsAndCondition;
	@Column(name = "ac01_archived")
	@ColumnDefault("0")
	private boolean m_bIsArchived;
	@Transient
	private boolean m_bIsForAllList;
	
	@JsonManagedReference
	@OneToMany
	@JoinColumn(name = "ac47_quotationId")
	private Set<QuotationAttachmentData> m_oQuotationAttachmentData;
	@Transient
	public QuotationAttachmentData [] m_arrQuotationAttachments;
	@Transient
	private String m_strFromDate = "";
	@Transient
    private String m_strToDate = "";
    @Column(name = "ac39_isCForm_provided")
    private boolean m_bIsCFormProvided;
	
	public QuotationData ()
	{
		m_nQuotationId = -1;
		m_oClientData = new ClientData ();
		m_oSiteData = new SiteData ();
		m_oContactData = new ContactData ();
		m_dDate = Calendar.getInstance().getTime();
		m_dCreatedOn = Calendar.getInstance().getTime();
		setM_nCreatedBy(-1);
		m_oQuotationLineItems = new HashSet<QuotationLineItemData> ();
//		setM_oPurchaseOrders(new HashSet<PurchaseOrderData> ());
		m_bIsArchived  = false;
		setM_oQuotationAttachmentData(new HashSet<QuotationAttachmentData> ());
		m_bIsCFormProvided = false;
		m_strQuotationNumber = "";
	}
	
	public int getM_nQuotationId () 
	{
		return m_nQuotationId;
	}
	
	public void setM_nQuotationId (int nQuotationId) 
	{
		m_nQuotationId = nQuotationId;
	}
	
	public ClientData getM_oClientData () 
	{
		return m_oClientData;
	}
	
	public void setM_oClientData (ClientData oClientData) 
	{
		m_oClientData = oClientData;
	}
	
	public SiteData getM_oSiteData () 
	{
		return m_oSiteData;
	}
	
	public void setM_oSiteData (SiteData oSiteData) 
	{
		m_oSiteData = oSiteData;
	}
	
	public ContactData getM_oContactData () 
	{
		return m_oContactData;
	}
	
	public void setM_oContactData (ContactData oContactData) 
	{
		m_oContactData = oContactData;
	}
	
	public Date getM_dDate () 
	{
		return m_dDate;
	}
	
	public void setM_dDate (Date dDate) 
	{
		m_dDate = dDate;
	}
	
	public String getM_strQuotationNumber() 
	{
		return m_strQuotationNumber;
	}

	public void setM_strQuotationNumber(String strQuotationNumber)
	{
		m_strQuotationNumber = strQuotationNumber;
	}

	public String getM_strDate () 
	{
		return m_strDate;
	}
	
	public void setM_strDate (String strDate) 
	{
		m_strDate = strDate;
	}
	
	public Date getM_dCreatedOn () 
	{
		return m_dCreatedOn;
	}
	
	public void setM_dCreatedOn (Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}
	
	public int getM_nCreatedBy () 
	{
		return m_nCreatedBy;
	}
	
	public void setM_nCreatedBy (int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}
	
	public void setM_oQuotationLineItems(Set<QuotationLineItemData> oQuotationLineItems) 
	{
		m_oQuotationLineItems = oQuotationLineItems;
	}

	public void setM_oPurchaseOrders(Set<PurchaseOrderData> oPurchaseOrders) 
	{
		m_oPurchaseOrders = oPurchaseOrders;
	}

	public Set<PurchaseOrderData> getM_oPurchaseOrders() 
	{
		return m_oPurchaseOrders;
	}

	public Set<QuotationLineItemData> getM_oQuotationLineItems() 
	{
		return m_oQuotationLineItems;
	}

	public void setM_bIsArchived(boolean bIsArchived)
	{
		m_bIsArchived = bIsArchived;
	}

	public boolean isM_bIsArchived() 
	{
		return m_bIsArchived;
	}

	public void setM_strTermsAndCondition(String strTermsAndCondition)
	{
		m_strTermsAndCondition = strTermsAndCondition;
	}

	public String getM_strTermsAndCondition() 
	{
		return m_strTermsAndCondition;
	}

	public UserInformationData getM_oUserCredentialsData () 
	{
		return m_oUserCredentialsData;
	}
	
	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData)
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}
	
	public Set<QuotationAttachmentData> getM_oQuotationAttachmentData ()
	{
		return m_oQuotationAttachmentData;
	}
	
	public void setM_oQuotationAttachmentData (Set<QuotationAttachmentData> oQuotationAttachmentData) 
	{
		m_oQuotationAttachmentData = oQuotationAttachmentData;
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

	public String getM_strToDate ()
	{
		return m_strToDate;
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
	public String generateXML () 
	{
		String strQuotationDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "QuotationData");
			addChild (oXmlDocument, oRootElement, "m_nQuotationId", m_nQuotationId);
			addChild (oXmlDocument, oRootElement, "m_strQuotationNumber", m_strQuotationNumber);
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dDate));
			Document oClientDoc = getXmlDocument (m_oClientData.generateXML());
			Node oClientNode = oXmlDocument.importNode (oClientDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
			Document oSiteDoc = getXmlDocument (m_oSiteData.generateXML());
			Node oSiteNode = oXmlDocument.importNode (oSiteDoc.getFirstChild (), true);
			oRootElement.appendChild (oSiteNode);
			if(m_oContactData != null)
			{
				Document oContactDoc = getXmlDocument (m_oContactData.generateXML());
				Node oContactNode = oXmlDocument.importNode (oContactDoc.getFirstChild (), true);
				oRootElement.appendChild (oContactNode);
			}
			Document oQuotationLineItemDoc = getXmlDocument ("<m_oQuotationLineItems>" + buildQuotationLineItem () + "</m_oQuotationLineItems>");
			Node oLineItemNode = oXmlDocument.importNode (oQuotationLineItemDoc.getFirstChild (), true);
			oRootElement.appendChild (oLineItemNode);
			float nGrandTotal= getGrandTotal(oQuotationLineItemDoc);
			addChild (oXmlDocument, oRootElement, "grandTotal", nGrandTotal);
			addChild (oXmlDocument, oRootElement, "m_nQuotationAmountInWords",  NumberToWords.convertNumberToWords(new BigDecimal (Math.round(nGrandTotal)), true, true));
			Document oTaxItemDoc = getXmlDocument ("<m_oTaxes>" + buildTaxItems (getTaxes()) + "</m_oTaxes>");
			Node oTaxNode = oXmlDocument.importNode (oTaxItemDoc.getFirstChild (), true);
			oRootElement.appendChild (oTaxNode);
			strQuotationDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strQuotationDataXml;
	}
	
	private String buildTaxItems(ArrayList<Tax> arrTaxes) throws Exception 
	{
		String strXML = "";
		float nTotalTaxAmount = 0;
		for(int nIndex = 0; nIndex < arrTaxes.size(); nIndex++)
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "Tax"); 
			addChild (oXmlDocument, oRootElement, "m_strTaxName", arrTaxes.get(nIndex).getM_strTaxName());
			addChild (oXmlDocument, oRootElement, "m_nTaxPercent", arrTaxes.get(nIndex).getM_nPercentage());
			addChild (oXmlDocument, oRootElement, "m_nTaxAmount", arrTaxes.get(nIndex).getM_nAmount());
			nTotalTaxAmount += arrTaxes.get(nIndex).getM_nAmount();
			strXML += getXmlString(oXmlDocument);
		}
		strXML += "<totalTaxAmountInWords>" + NumberToWords.convertNumberToWords(new BigDecimal (nTotalTaxAmount), true, true) + "</totalTaxAmountInWords>";
		return strXML;
	}
	
	private ArrayList<Tax> getTaxes() 
	{
		ArrayList<Tax> arrTax = new ArrayList<Tax>();
		Iterator<QuotationLineItemData> oIterator = m_oQuotationLineItems.iterator();
		while(oIterator.hasNext())
		{
			QuotationLineItemData oQuotationLineItemData = oIterator.next();
			addTax(arrTax, oQuotationLineItemData);
		}
		return arrTax;
	}
	
	private void addTax(ArrayList<Tax> arrTax, QuotationLineItemData oQuotationLineItemData)
	{
		m_oLogger.info("addTax");
		Tax oLineItemTax = new Tax ();
		oLineItemTax.setM_strTaxName(oQuotationLineItemData.getM_strTaxName());
		oLineItemTax.setM_nAmount(getTaxAmount(oQuotationLineItemData));
		oLineItemTax.setM_nPercentage(oQuotationLineItemData.getM_nTax());
		if(oLineItemTax.getM_nAmount() > 0 && isNewTaxData(oLineItemTax.getM_strTaxName(), oQuotationLineItemData.getM_nTax(), arrTax))
			arrTax.add(oLineItemTax);
		else
			updateToTaxArray(arrTax, oLineItemTax);
	}
	
	private void updateToTaxArray(ArrayList<Tax> arrTax, Tax oLineItemTax) 
	{
		for(int nIndex = 0; nIndex < arrTax.size(); nIndex++)
		{
			if(arrTax.get(nIndex).getM_strTaxName().equals(oLineItemTax.getM_strTaxName()) && arrTax.get(nIndex).getM_nPercentage()== oLineItemTax.getM_nPercentage())
			{
				arrTax.get(nIndex).setM_nAmount(arrTax.get(nIndex).getM_nAmount() + oLineItemTax.getM_nAmount());
				break;
			}
		}
	}

	private boolean isNewTaxData(String strTaxName, float nTax, ArrayList<Tax> arrTax) 
	{
		boolean bIsNewTaxData = true;
		Tax oTax = new Tax(); 
		Iterator<Tax> oIterator = arrTax.iterator();
		while(oIterator.hasNext())
		{
			oTax = oIterator.next();
			if(oTax.getM_strTaxName().equals(strTaxName) && oTax.getM_nPercentage() == nTax)
				bIsNewTaxData = false;
		}
		return bIsNewTaxData;
	}
	
	private float getTaxAmount(QuotationLineItemData oQuotationLineItemData) 
	{
		return (oQuotationLineItemData.getM_nQuantity()*(oQuotationLineItemData.getM_nPrice() - (oQuotationLineItemData.getM_nPrice() * (oQuotationLineItemData.getM_nDiscount()/100)))) * oQuotationLineItemData.getM_nTax()/100 ;
	}
	
	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
		Criteria oSiteDataCriteria = oCriteria.createCriteria ("m_oSiteData");
		if (m_nQuotationId > 0)
			oCriteria.add (Restrictions.eq ("m_nQuotationId", m_nQuotationId));
		if (m_nCreatedBy > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if (m_strQuotationNumber != null && !m_strQuotationNumber.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strPurchaseOrderNumber", m_strQuotationNumber.trim(),MatchMode.ANYWHERE));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oClientData", m_oClientData));
		if (m_oContactData != null && m_oContactData.getM_nContactId() > 0)
		{
			Criteria oContactDataCriteria = oCriteria.createCriteria("m_oContactData");
			oContactDataCriteria.add(Restrictions.eq("m_nContactId", m_oContactData.getM_nContactId()));
		}
		if (m_oSiteData != null && m_oSiteData.getM_nSiteId() > 0)
			oSiteDataCriteria.add(Restrictions.eq("m_nSiteId", m_oSiteData.getM_nSiteId()));
		if(m_oClientData.getM_strCompanyName() != null && !m_oClientData.getM_strCompanyName().trim().isEmpty())
			oClientDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if(m_oSiteData.getM_strSiteName() != null && !m_oSiteData.getM_strSiteName().trim().isEmpty())
			oSiteDataCriteria.add(Restrictions.ilike ("m_strSiteName", m_oSiteData.getM_strSiteName().trim(), MatchMode.ANYWHERE));
		if(m_strDate != null && !m_strDate.trim().isEmpty())
			oCriteria.add(Restrictions.eq("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate.trim(), true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dDate", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dDate", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (!m_bIsForAllList)
			oCriteria.add(Restrictions.eq("m_bIsArchived", m_bIsArchived));
		if (strColumn.contains("m_strCompanyName"))
			oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strColumn.contains("m_strSiteName"))
			oSiteDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nQuotationId");
		return oCriteria;
	}
	
	private String buildQuotationLineItem () 
	{
		String strXML = "";
		Object [] arrQuotationLineitems = m_oQuotationLineItems.toArray();
		getOrderedLineItems (arrQuotationLineitems);
		float nSubTotal = 0;
		for (int nIndex = 0; nIndex < arrQuotationLineitems.length; nIndex ++)
		{
			QuotationLineItemData oQuotationLineItemData = (QuotationLineItemData) arrQuotationLineitems [nIndex];
			strXML += oQuotationLineItemData.generateXMLWithSerialNumber(nIndex+1);
			nSubTotal += oQuotationLineItemData.getM_nPrice() * oQuotationLineItemData.getM_nQuantity();
		}
		strXML += "<subTotal>" + nSubTotal + "</subTotal>";
		String strLineItemXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <LineItems>" + strXML + "</LineItems>";
		strXML += "<discounts>" + addDiscountInfo (strLineItemXml) + "</discounts>";
		strXML += "<taxes>" + addTaxInfo (strLineItemXml) + "</taxes>";
		return strXML;
	}
	
	private float getGrandTotal(Document oLineItemDoc) 
	{
		float nGrandTotal = 0;
		NodeList oLineItems = oLineItemDoc.getElementsByTagName("m_oQuotationLineItems");
		for(int nIndex = 0; nIndex < oLineItems.getLength(); nIndex ++)
		{
			Node oSubTotalItem = getChildNodeByName(oLineItems.item(nIndex), "subTotal");
			Node oDiscountsItem = getChildNodeByName(oLineItems.item(nIndex), "discounts");
			Node oTaxesItem = getChildNodeByName(oLineItems.item(nIndex), "taxes");
			float nSubTotal = TraderpUtil.getFloatValue(oSubTotalItem.getFirstChild().getNodeValue());
			float nTotalDiscount = TraderpUtil.getFloatValue(getTagValue (oDiscountsItem, "totalDiscount"));
			float nTotalTaxAmount = TraderpUtil.getFloatValue(getTagValue (oTaxesItem, "totalTaxAmount"));
			nGrandTotal += (nSubTotal - nTotalDiscount + nTotalTaxAmount);
		}
		return nGrandTotal;
	}
	
	private String addDiscountInfo(String strLineItemXml) 
	{
		String strXml = "";
		Document oLineItemDoc = getXmlDocument(strLineItemXml);
		NodeList oLineItems = oLineItemDoc.getElementsByTagName("LineItemData");
		ArrayList<TaxDiscountInfo> arrTaxDiscountInfo = new ArrayList<TaxDiscountInfo> ();
		for(int nIndex = 0; nIndex < oLineItems.getLength(); nIndex ++)
		{
			Node oSalesLineItem = getChildNodeByName(oLineItems.item(nIndex), "QuotationLineItemData");
			float nDiscount = TraderpUtil.getFloatValue(getTagValue (oSalesLineItem, "m_nDiscount"));
			if(nDiscount > 0)
			{
				TaxDiscountInfo oTaxDiscountInfo = new TaxDiscountInfo ();
				oTaxDiscountInfo.m_nPercentage = nDiscount;
				oTaxDiscountInfo.m_nAmount = getAmount (oSalesLineItem);
				oTaxDiscountInfo.m_nSerialNumber = TraderpUtil.getIntValue(getTagValue (oLineItems.item(nIndex), "SerialNumber"));
				buildTaxDiscountArray (oTaxDiscountInfo, arrTaxDiscountInfo);
			}
		}
		strXml = buildDiscountXml (arrTaxDiscountInfo);
		strXml += "<totalDiscount>" + getTotalDiscount (arrTaxDiscountInfo) + "</totalDiscount>";
		return strXml;
	}
	
	private void buildTaxDiscountArray(TaxDiscountInfo oTaxDiscountInfo, ArrayList<TaxDiscountInfo> arrTaxDiscountInfo) 
	{
		if(isNewTaxDiscountInfo (oTaxDiscountInfo, arrTaxDiscountInfo))
		{
			oTaxDiscountInfo.m_arrSerials.add(oTaxDiscountInfo.m_nSerialNumber);
			arrTaxDiscountInfo.add(oTaxDiscountInfo);
		}
		else
			updateTaxDiscountArray (oTaxDiscountInfo, arrTaxDiscountInfo);
	}

	private boolean isNewTaxDiscountInfo(TaxDiscountInfo oTaxDiscountInfo, ArrayList<TaxDiscountInfo> arrTaxDiscountInfo) 
	{
		boolean isNewTaxDiscountInfo = true;
		for (int nIndex = 0; nIndex < arrTaxDiscountInfo.size(); nIndex ++)
		{
			if(arrTaxDiscountInfo.get(nIndex).m_nPercentage == oTaxDiscountInfo.m_nPercentage)
			{
				isNewTaxDiscountInfo = false;
				break;
			}
		}
		return isNewTaxDiscountInfo;
	}

	private void updateTaxDiscountArray(TaxDiscountInfo oTaxDiscountInfo, ArrayList<TaxDiscountInfo> arrTaxDiscountInfo) 
	{
		for (int nIndex = 0; nIndex < arrTaxDiscountInfo.size(); nIndex ++)
		{
			if(arrTaxDiscountInfo.get(nIndex).m_nPercentage == oTaxDiscountInfo.m_nPercentage)
			{
				arrTaxDiscountInfo.get(nIndex).m_nAmount += oTaxDiscountInfo.m_nAmount;
				arrTaxDiscountInfo.get(nIndex).m_arrSerials.add(oTaxDiscountInfo.m_nSerialNumber);
				break;
			}
		}
	}

	private float getAmount(Node oItem) 
	{
		float nQuantity = TraderpUtil.getFloatValue(getTagValue (oItem, "m_nQuantity"));
		float nPrice = TraderpUtil.getFloatValue(getTagValue (oItem, "m_nPrice"));
		return nQuantity * nPrice;
	}
	
	private String buildDiscountXml(ArrayList<TaxDiscountInfo> arrTaxDiscountInfo) 
	{
		String strXML = "";
		for (int nIndex = 0; nIndex < arrTaxDiscountInfo.size(); nIndex++)
		{
			strXML += "<LineItemData>";
			strXML += "<SerialNumber></SerialNumber>";
			strXML += generateDiscountXML(arrTaxDiscountInfo.get(nIndex));
			strXML += "</LineItemData>";
		}
		return strXML;
	}

	public String generateDiscountXML(TaxDiscountInfo oTaxDiscountInfo) 
	{
		m_oLogger.info ("generateDiscountXML");
		String strDiscountsXml = "";
		try 
		{
			
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "QuotationLineItemData");
		
			addChild (oXmlDocument, oRootElement, "m_nLineItemId", -1);
			addChild (oXmlDocument, oRootElement, "m_strArticleNumber", "");
			addChild (oXmlDocument, oRootElement, "m_strDetail", "");
			addChild (oXmlDocument, oRootElement, "m_strUnit", "");
			addChild (oXmlDocument, oRootElement, "m_nQuantity", "");
			addChild (oXmlDocument, oRootElement, "m_nPrice", getDiscountPrice(oTaxDiscountInfo));
			addChild (oXmlDocument, oRootElement, "m_nTax", "");
			addChild (oXmlDocument, oRootElement, "m_nDiscount", "");
			addChild (oXmlDocument, oRootElement, "m_nAmount", ""); 
			strDiscountsXml = getXmlString(oXmlDocument);
			
		} 
		catch (Exception oException)
		{
			m_oLogger.error("generateDiscountXML - oException" + oException);
		}
		return strDiscountsXml;
	}
	
	
	private float getDiscountPrice(TaxDiscountInfo oTaxDiscountInfo) 
	{
		float nDiscountPrice = 0;
		nDiscountPrice = oTaxDiscountInfo.m_nAmount * (oTaxDiscountInfo.m_nPercentage / 100);
		return nDiscountPrice;
	}
	
	private float getTotalDiscount(ArrayList<TaxDiscountInfo> arrTaxDiscountInfo) 
	{
		float nTotalDiscount = 0;
		for(int nIndex = 0; nIndex < arrTaxDiscountInfo.size(); nIndex++)
			nTotalDiscount += arrTaxDiscountInfo.get(nIndex).m_nAmount * (arrTaxDiscountInfo.get(nIndex).m_nPercentage / 100);
		return nTotalDiscount;
	}
	
	private String addTaxInfo(String strLineItemXml)
	{
		String strXml = "";
		Document oLineItemDoc = getXmlDocument(strLineItemXml);
		NodeList oLineItems = oLineItemDoc.getElementsByTagName("LineItemData");
		ArrayList<TaxDiscountInfo> arrTaxDiscountInfo = new ArrayList<TaxDiscountInfo> ();
		for(int nIndex = 0; nIndex < oLineItems.getLength(); nIndex ++)
		{
			Node oSalesLineItem = getChildNodeByName(oLineItems.item(nIndex), "QuotationLineItemData");
			float nTax = TraderpUtil.getFloatValue(getTagValue (oSalesLineItem, "m_nTax"));
			if(nTax > 0)
			{
				TaxDiscountInfo oTaxDiscountInfo = new TaxDiscountInfo ();
				oTaxDiscountInfo.m_nPercentage = nTax;
				oTaxDiscountInfo.m_nAmount = getDiscountedAmount (oSalesLineItem);
				oTaxDiscountInfo.m_nSerialNumber = TraderpUtil.getIntValue(getTagValue (oLineItems.item(nIndex), "SerialNumber"));
				buildTaxDiscountArray (oTaxDiscountInfo, arrTaxDiscountInfo);
			}
		}
		strXml += "<totalTaxAmount>" + getTotalDiscount (arrTaxDiscountInfo) + "</totalTaxAmount>";
		return strXml;
	}
	

	private float getDiscountedAmount(Node oItem) 
	{
		float nQuantity = TraderpUtil.getFloatValue(getTagValue (oItem, "m_nQuantity"));
		float nPrice = TraderpUtil.getFloatValue(getTagValue (oItem, "m_nPrice"));
		float nDiscount = TraderpUtil.getFloatValue(getTagValue (oItem, "m_nDiscount"));
		return (nPrice * ((100-nDiscount)/100) * nQuantity);
	}
	
	private void getOrderedLineItems(Object[] arrSalesLine)
	{
		for(int nMainIndex = 0; nMainIndex < arrSalesLine.length; nMainIndex++)
		{
			for(int nChildIndex = nMainIndex+1; nChildIndex < arrSalesLine.length; nChildIndex++)
			{
				if (getSerialNo (arrSalesLine[nMainIndex]) > getSerialNo (arrSalesLine[nChildIndex]))
				{
					Object oTemp = arrSalesLine[nMainIndex];
					arrSalesLine[nMainIndex] = arrSalesLine[nChildIndex];
					arrSalesLine[nChildIndex] = oTemp;
				}
			}
		}
	}

	private int getSerialNo(Object oObject) 
	{
		int nSerialNumber = 0;
		QuotationLineItemData oQuotationLineItemData = (QuotationLineItemData)oObject;
		nSerialNumber = oQuotationLineItemData.getM_nSerialNumber();
		return nSerialNumber;
	}

	public void prepareAttachmentData ()
	{
		try
		{
			for (int nIndex = 0; nIndex < m_arrQuotationAttachments.length; nIndex++)
			{
				if (m_arrQuotationAttachments[nIndex].getM_oFile()!= null)// && m_arrQuotationAttachments[nIndex].getM_oFile().getSize() > 0)
				{
					InputStream oInputStream = m_arrQuotationAttachments[nIndex].getM_oFile().getInputStream();
					m_arrQuotationAttachments[nIndex].setM_oAttachment(GenericIDataProcessor.getBlob (oInputStream));
				}
				else
				{
					QuotationAttachmentData oQuotationAttachmentData = new QuotationAttachmentData ();
					oQuotationAttachmentData.setM_nAttachmentId(m_arrQuotationAttachments[nIndex].getM_nAttachmentId());
					oQuotationAttachmentData = (QuotationAttachmentData) GenericIDataProcessor.populateObject (oQuotationAttachmentData);
					m_arrQuotationAttachments[nIndex].setM_oAttachment(oQuotationAttachmentData.getM_oAttachment());
				}
				m_arrQuotationAttachments[nIndex].setM_oQuotationData(this);
				m_oQuotationAttachmentData.add(m_arrQuotationAttachments[nIndex]);
			}
		}
		catch(Exception oException)
		{
			m_oLogger.error("prepareData - oException : " +oException);
		}
	}

	public void deleteAttachmentData() throws Exception 
	{
		QuotationData oQuotationData = (QuotationData) GenericIDataProcessor.populateObject (this);
		Iterator<QuotationAttachmentData> oIterator =  oQuotationData.getM_oQuotationAttachmentData().iterator();
		while(oIterator.hasNext())
		{
			QuotationAttachmentData oQuotationAttachmentData = oIterator.next();
			if(!IsAttachmentExist (oQuotationAttachmentData.getM_strFileName()))
				oQuotationAttachmentData.deleteObject();
		}
	}

	private boolean IsAttachmentExist(String strFileName) 
	{	
		boolean IsAttachmentExist = false;
		for (int nIndex = 0; nIndex < m_arrQuotationAttachments.length; nIndex++)
		{
			if(strFileName.equals(m_arrQuotationAttachments[nIndex].getM_strFileName()))
			{
				IsAttachmentExist = true;
				break;
			}
		}
		return IsAttachmentExist;
	}

	public void prepareQuotationData() 
	{
		prepareAttachmentData ();
		for (int nIndex = 0 ; nIndex < m_arrQuotationLineItems.length; nIndex++ )
		{
			QuotationLineItemData oQuotationLineItemData = m_arrQuotationLineItems[nIndex];
			try
			{
				if (!oQuotationLineItemData.getM_strArticleNumber().trim().equalsIgnoreCase(""))
				{
					ClientArticleData oItemData = ClientArticleData.getInstance(m_oClientData, oQuotationLineItemData.getM_strArticleNumber(), getM_oUserCredentialsData());
					ItemData oInventoryItem = new ItemData ((ItemData)oItemData);
					ItemData oSalesItem = new ItemData ();
					oSalesItem.setM_nItemId(oInventoryItem.getM_nItemId());
					oQuotationLineItemData.setM_oItemData(oSalesItem);
				}
				else
					oQuotationLineItemData.setM_oItemData(null);
				oQuotationLineItemData.setM_nCreatedBy(m_nCreatedBy);
				addLineItem(oQuotationLineItemData);
			}
			catch (Exception oException)
			{
				m_oLogger.error("prepareQuotationData - oException : ", oException);
			}
		}
	}
	
	public void addLineItem (QuotationLineItemData oQuotationLineItemData) 
	{
		oQuotationLineItemData.setM_oQuotationData(this);
	    m_oQuotationLineItems.add(oQuotationLineItemData);
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void customizeQuotationLineItems() 
	{
		Iterator<QuotationLineItemData> oIterator = m_oQuotationLineItems.iterator();
		while (oIterator.hasNext()) 
		{
			QuotationLineItemData oQuotationLineItemData = (QuotationLineItemData) oIterator.next();
			oQuotationLineItemData.customizeItemData();
		}
	}

	public double calculateQuotationLineItemTotal(QuotationLineItemData[] arrQuotationLineItems) 
	{
		float nTotal = 0;
		for (int nIndex = 0; nIndex < arrQuotationLineItems.length; nIndex ++)
			nTotal += getLineItemTotal(arrQuotationLineItems[nIndex]);
		return nTotal;
	}

	private float getLineItemTotal(Object oLineItem)
	{
		float nTotal = 0;
		QuotationLineItemData oQuotationLineItemData = (QuotationLineItemData) oLineItem;
		nTotal = calculateTotalAmount (oQuotationLineItemData.getM_nPrice(), oQuotationLineItemData.getM_nQuantity(), oQuotationLineItemData.getM_nDiscount(), oQuotationLineItemData.getM_nTax());
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

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		/*
			Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
			Criteria oSiteDataCriteria = oCriteria.createCriteria ("m_oSiteData");
			if (m_nQuotationId > 0)
				oCriteria.add (Restrictions.eq ("m_nQuotationId", m_nQuotationId));
			if (m_oCreatedBy != null && m_oCreatedBy.getM_nUserId() > 0)
				oCriteria.add (Restrictions.eq ("m_oCreatedBy", m_oCreatedBy));
			if (m_strQuotationNumber != null && !m_strQuotationNumber.trim().isEmpty ())
				oCriteria.add (Restrictions.ilike ("m_strPurchaseOrderNumber", m_strQuotationNumber.trim(),MatchMode.ANYWHERE));
			if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
				oCriteria.add(Restrictions.eq("m_oClientData", m_oClientData));
			if (m_oContactData != null && m_oContactData.getM_nContactId() > 0)
			{
				Criteria oContactDataCriteria = oCriteria.createCriteria("m_oContactData");
				oContactDataCriteria.add(Restrictions.eq("m_nContactId", m_oContactData.getM_nContactId()));
			}
			if (m_oSiteData != null && m_oSiteData.getM_nSiteId() > 0)
				oSiteDataCriteria.add(Restrictions.eq("m_nSiteId", m_oSiteData.getM_nSiteId()));
			if(m_oClientData.getM_strCompanyName() != null && !m_oClientData.getM_strCompanyName().trim().isEmpty())
				oClientDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
			if(m_oSiteData.getM_strSiteName() != null && !m_oSiteData.getM_strSiteName().trim().isEmpty())
				oSiteDataCriteria.add(Restrictions.ilike ("m_strSiteName", m_oSiteData.getM_strSiteName().trim(), MatchMode.ANYWHERE));
			if(m_strDate != null && !m_strDate.trim().isEmpty())
				oCriteria.add(Restrictions.eq("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate.trim(), true)));
			if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
				oCriteria.add (Restrictions.between ("m_dDate", 
						GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
						GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
			if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
				oCriteria.add (Restrictions.ge("m_dDate", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
			if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
				oCriteria.add (Restrictions.le ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
			if (!m_bIsForAllList)
				oCriteria.add(Restrictions.eq("m_bIsArchived", m_bIsArchived));
			if (strColumn.contains("m_strCompanyName"))
				oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
			else if (strColumn.contains("m_strSiteName"))
				oSiteDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
			else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nQuotationId");
		 */
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nQuotationId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nQuotationId"), m_nQuotationId));
		return oConjunct;
	}
}
