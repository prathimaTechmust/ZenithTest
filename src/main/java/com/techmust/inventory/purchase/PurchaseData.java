package com.techmust.inventory.purchase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import javax.persistence.JoinTable;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.traderp.util.TraderpUtil;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.paymentsandreceipt.PurchasePaymentData;
import com.techmust.inventory.purchaseorder.PurchaseOrderLineItemData;
import com.techmust.inventory.utility.TallyTransformData;
import com.techmust.inventory.vendorpurchaseorder.VendorPurchaseOrderData;
import com.techmust.master.tax.Tax;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="tac04_purchase")
public class PurchaseData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ac04_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int m_nId;
	@Column(name="ac04_from")
	private String m_strFrom;
	@Transient
	private String m_strInvoiceNo;
	@Column(name = "ac04_purchase_date")
	@Basic
	@Temporal(TemporalType.DATE)
	private Date m_dDate;
	@Transient
	private String m_strDate;
	@Column(name="ac04_created_on")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date m_dCreatedOn;
	@Transient
	private String m_strFromDate;
	@Transient
	private String m_strToDate;
	
	@JsonManagedReference
	@OneToMany(orphanRemoval=true)
	@JoinColumn( name="ac05_id")
 	private Set<PurchaseLineItem> m_oPurchaseLineItems;
	@Transient
	public PurchaseLineItem [] m_arrPurchaseLineItem;
	
	@OneToMany(orphanRemoval=true)
    @JoinColumn( name="ac21_id")
 	private Set<NonStockPurchaseLineItem> m_oNonStockPurchaseLineItems;
	@Transient
	public NonStockPurchaseLineItem [] m_arrNonStockPurchaseLineItem;
	@Column(name="ac04_purchase_type")
	@Enumerated(EnumType.ORDINAL) 
	private PurchaseType m_nPurchaseType;
	@Column(name="ac04_Created_by")
	private int m_nCreatedBy;
	@ManyToOne
	@JoinColumn(name="ac05_Vendor_id")
	private VendorData m_oVendorData;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ac05_Vendor_PO_id")
	private VendorPurchaseOrderData m_oVendorPurchaseOrderData;
	@Column(name="ac04_payment_amount")
	@ColumnDefault("0")
    private float m_nPaymentAmount;
	@Column(name = "ac04_balance_amount")
	@ColumnDefault("0")
    private float m_nBalanceAmount;
	@Column(name = "ac04_Total_Amount")
	@ColumnDefault("0")
    private float m_nTotalAmount;
	@Transient
    private boolean m_bIsForPayment;
    @Transient
    private String m_strTallyTransformDate;
    @Transient
    private String m_strTime;
    @Transient
    public boolean m_bIsForVendorOutstanding;
    @Transient
    public boolean m_bIsForAgeWise;
    @Transient
    private boolean m_bIsForAll;
	
	public PurchaseData ()
	{
		m_nId = -1;
		m_strFrom = "";
		m_strInvoiceNo = "";
		m_dDate = Calendar.getInstance().getTime ();
		m_dCreatedOn = Calendar.getInstance().getTime ();
		m_oPurchaseLineItems = new HashSet<PurchaseLineItem> ();
		setM_oNonStockPurchaseLineItems(new HashSet<NonStockPurchaseLineItem> ());
		setM_nPurchaseType(PurchaseType.kPurchase);
		m_bIsForPayment = false;
		m_strTime = "";
		m_strDate = "";
		m_strFromDate = "";
		m_strToDate = "";
		m_oVendorData = new VendorData ();
		m_nCreatedBy = -1;
		m_oVendorPurchaseOrderData = new VendorPurchaseOrderData ();
		m_oUserCredentialsData = new UserInformationData (); 
		m_bIsForAgeWise = false;
		m_bIsForAll = false;
	}

	public void setM_nId (int nId) 
	{
		this.m_nId = nId;
	}

	public int getM_nId ()
	{
		return m_nId;
	}

	public void setM_strFrom (String strFrom)
	{
		this.m_strFrom = strFrom;
	}

	public String getM_strFrom () 
	{
		return m_strFrom;
	}

	public void setM_strInvoiceNo (String strInvoiceNo) 
	{
		this.m_strInvoiceNo = strInvoiceNo;
	}

	public String getM_strInvoiceNo () 
	{
		return m_strInvoiceNo;
	}

	public void setM_dDate (Date dDate)
	{
		this.m_dDate = dDate;
	}

	public Date getM_dDate () 
	{
		return m_dDate;
	}
	
	public void setM_strDate (String strDate)
	{
		this.m_strDate = strDate;
	}

	public String getM_strDate () 
	{
		return m_strDate;
	}
	
	public void setM_oCreatedBy (int nCreatedBy)
	{
		this.m_nCreatedBy = nCreatedBy;
	}

	public int getM_nCreatedBy ()
	{
		return m_nCreatedBy;
	}
	
	public void setM_oVendorPurchaseOrderData(VendorPurchaseOrderData oVendorPurchaseOrderData) 
	{
		m_oVendorPurchaseOrderData = oVendorPurchaseOrderData;
	}

	public VendorPurchaseOrderData getM_oVendorPurchaseOrderData() 
	{
		return m_oVendorPurchaseOrderData;
	}

	public void setM_strFromDate (String strFromDate)
	{
		this.m_strFromDate = strFromDate;
	}

	public String getM_strFromDate ()
	{
		return m_strFromDate;
	}
	
	public void setM_strToDate(String strToDate) 
	{
		this.m_strToDate = strToDate;
	}

	public String getM_strToDate ()
	{
		return m_strToDate;
	}
	
	public void setM_dCreatedOn (Date dCreatedOn) 
	{
		this.m_dCreatedOn = dCreatedOn;
	}

	public Date getM_dCreatedOn ()
	{
		return m_dCreatedOn;
	}

	public void setM_oPurchaseLineItems (Set<PurchaseLineItem> oPurchaseLineItems)
	{
		this.m_oPurchaseLineItems = oPurchaseLineItems;
	}

	public Set<PurchaseLineItem> getM_oPurchaseLineItems () 
	{
		return m_oPurchaseLineItems;
	}

	public void setM_oNonStockPurchaseLineItems(Set<NonStockPurchaseLineItem> oNonStockPurchaseLineItems) 
	{
		this.m_oNonStockPurchaseLineItems = oNonStockPurchaseLineItems;
	}

	public Set<NonStockPurchaseLineItem> getM_oNonStockPurchaseLineItems() 
	{
		return m_oNonStockPurchaseLineItems;
	}

	public void setM_nPurchaseType(PurchaseType nPurchaseType) 
	{
		this.m_nPurchaseType = nPurchaseType;
	}

	public PurchaseType getM_nPurchaseType() 
	{
		return m_nPurchaseType;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		this.m_oUserCredentialsData = oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData()
	{
		return m_oUserCredentialsData;
	}

	public void setM_oVendorData(VendorData oVendorData)
	{
		this.m_oVendorData = oVendorData;
	}

	public VendorData getM_oVendorData() 
	{
		return m_oVendorData;
	}

	public float getM_nPaymentAmount() 
	{
		return m_nPaymentAmount;
	}

	public void setM_nPaymentAmount(float nPaymentAmount) 
	{
		m_nPaymentAmount = nPaymentAmount;
	}

	public float getM_nBalanceAmount()
	{
		return m_nBalanceAmount;
	}

	public void setM_nBalanceAmount(float nBalanceAmount) 
	{
		m_nBalanceAmount = nBalanceAmount;
	}
	
	public void setM_nTotalAmount(float m_nTotalAmount) 
	{
		this.m_nTotalAmount = m_nTotalAmount;
	}

	public float getM_nTotalAmount() 
	{
		return m_nTotalAmount;
	}

	public void setM_strTallyTransformDate(String strTallyTransformDate) 
	{
		this.m_strTallyTransformDate = strTallyTransformDate;
	}

	public String getM_strTallyTransformDate()
	{
		return m_strTallyTransformDate;
	}

	public void setM_strTime(String strTime) 
	{
		this.m_strTime = strTime;
	}

	public String getM_strTime() 
	{
		return m_strTime;
	}

	public PurchaseLineItem[] getM_arrPurchaseLineItem() {
		return m_arrPurchaseLineItem;
	}

	public void setM_arrPurchaseLineItem(PurchaseLineItem[] purchaseLineItem) {
		m_arrPurchaseLineItem = purchaseLineItem;
	}

	public NonStockPurchaseLineItem[] getM_arrNonStockPurchaseLineItem() {
		return m_arrNonStockPurchaseLineItem;
	}
	
	public void setM_arrNonStockPurchaseLineItem(
			NonStockPurchaseLineItem[] nonStockPurchaseLineItem) {
		m_arrNonStockPurchaseLineItem = nonStockPurchaseLineItem;
	}

	public boolean isM_bIsForPayment() {
		return m_bIsForPayment;
	}

	public void setM_bIsForPayment(boolean isForPayment) {
		m_bIsForPayment = isForPayment;
	}

	public boolean isM_bIsForVendorOutstanding() {
		return m_bIsForVendorOutstanding;
	}

	public void setM_bIsForVendorOutstanding(boolean isForVendorOutstanding) {
		m_bIsForVendorOutstanding = isForVendorOutstanding;
	}

	public boolean isM_bIsForAgeWise() {
		return m_bIsForAgeWise;
	}

	public void setM_bIsForAgeWise(boolean isForAgeWise) {
		m_bIsForAgeWise = isForAgeWise;
	}

	public boolean isM_bIsForAll() {
		return m_bIsForAll;
	}

	public void setM_bIsForAll(boolean isForAll) {
		m_bIsForAll = isForAll;
	}

	@Override
	public String generateXML () 
	{
		String strPurchaseDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "PurchaseData");
			Document oPurchaseLineItemDoc = getXmlDocument ("<m_oPurchaseLineItems>" + buildPurchaseLineItem (m_oPurchaseLineItems) + "</m_oPurchaseLineItems>");
			Node oApplicableTaxNode = oXmlDocument.importNode (oPurchaseLineItemDoc.getFirstChild (), true);
			oRootElement.appendChild (oApplicableTaxNode);
 			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			addChild (oXmlDocument, oRootElement, "m_dDate", m_dDate != null ? m_dDate.toString() : "");
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(getM_dDate()));
			addChild (oXmlDocument, oRootElement, "m_strInvoiceNo", m_strInvoiceNo);
			addChild (oXmlDocument, oRootElement, "m_strFrom", m_strFrom);
			addChild (oXmlDocument, oRootElement, "m_strTallyTransformDate", TraderpUtil.getTallyCompatibleFormat(m_dDate));
			addChild (oXmlDocument, oRootElement, "m_strTime", TraderpUtil.getTime(m_dDate));
			addChild (oXmlDocument, oRootElement, "m_nTotalAmount", TraderpUtil.formatNumber(getPurchaseAmount ()));
			Document oVendorDoc = getXmlDocument (m_oVendorData.generateXML());
			Node oVendorNode = oXmlDocument.importNode (oVendorDoc.getFirstChild (), true);
			oRootElement.appendChild (oVendorNode);
			addChild (oXmlDocument, oRootElement, "m_nPaymentAmount", m_nPaymentAmount);
			addChild (oXmlDocument, oRootElement, "m_nBalanceAmount", m_nBalanceAmount);
			Document oTaxItemDoc = getXmlDocument("<m_oInputTaxes>"+ buildInputTaxItems (getTaxes()) + "</m_oInputTaxes>");
			Node oTaxNode = oXmlDocument.importNode (oTaxItemDoc.getFirstChild (), true);
			oRootElement.appendChild (oTaxNode);
			addChild (oXmlDocument, oRootElement, "m_nRoundedTotalAmount", m_nTotalAmount);
			strPurchaseDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strPurchaseDataXml;
	}
	
	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if (m_bIsForAgeWise)
			buildAgeWiseInvoiceList (oCriteria, strColumn, strOrderBy);
		else if (m_bIsForVendorOutstanding)
			buildVendorOutstandingListCriteria (oCriteria, strColumn, strOrderBy);
		else
			buildListCriteria (oCriteria, strColumn, strOrderBy);
		return oCriteria;
	}

	private void buildAgeWiseInvoiceList(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		buildDateAndVendorListCriteria (oCriteria, strColumn, strOrderBy);
		oCriteria.add(Restrictions.gt("m_nBalanceAmount", (float)0.0));
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.rowCount());
		oProjectionList.add(Projections.sum("m_nBalanceAmount"));
		oCriteria.setProjection(oProjectionList);
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_dCreatedOn");
	}
	
	private void buildDateAndVendorListCriteria(Criteria oCriteria,String strColumn, String strOrderBy)
	{
		Criteria oVendorDataCriteria = oCriteria.createCriteria ("m_oVendorData");
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oCriteria.add (Restrictions.eq ("m_oVendorData", m_oVendorData));
		if(m_oVendorData.getM_strCompanyName() != null && !m_oVendorData.getM_strCompanyName().trim().isEmpty())
			oVendorDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oVendorData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if(m_oVendorData.getM_strCity() != null && !m_oVendorData.getM_strCity().trim().isEmpty())
			oVendorDataCriteria.add(Restrictions.ilike ("m_strCity", m_oVendorData.getM_strCity().trim(), MatchMode.ANYWHERE));
	}

	private void buildVendorOutstandingListCriteria(Criteria oCriteria,String strColumn, String strOrderBy) 
	{
		buildDateAndVendorListCriteria (oCriteria, strColumn, strOrderBy);
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("m_oVendorData"));
		oProjectionList.add(Projections.sum("m_nTotalAmount"));
		oProjectionList.add(Projections.sum("m_nPaymentAmount"));
		oProjectionList.add(Projections.sum("m_nBalanceAmount"),"nBalanceAmount");
		oCriteria.setProjection(oProjectionList);
		oCriteria.addOrder(Order.desc("nBalanceAmount"));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_oVendorData");
	}

	private void buildListCriteria(Criteria oCriteria, String strColumn,String strOrderBy) 
	{
		if (!m_strFrom.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strFrom", m_strFrom.trim(), MatchMode.ANYWHERE));
		if (m_strInvoiceNo != null && !m_strInvoiceNo.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strInvoiceNo", m_strInvoiceNo.trim()));
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_nCreatedBy  > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if (m_oVendorPurchaseOrderData != null && m_oVendorPurchaseOrderData.getM_nPurchaseOrderId() > 0)
			oCriteria.add (Restrictions.eq ("m_oVendorPurchaseOrderData", m_oVendorPurchaseOrderData));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dDate", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate != null && m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if(m_nPurchaseType != PurchaseType.kInvalid && !m_bIsForPayment && !m_bIsForAll)
			oCriteria.add(Restrictions.eq("m_nPurchaseType", m_nPurchaseType));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oVendorData", m_oVendorData));
		if (strColumn.contains("m_strCompanyName"))
			oCriteria.createCriteria ("m_oVendorData").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		if(m_bIsForPayment)
			oCriteria.add(Restrictions.disjunction().add(Restrictions.eq("m_nPaymentAmount", (float)0.0)).add(Restrictions.gt("m_nBalanceAmount", (float)0.0)));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_strFrom");
	}

	public void addLineItem (PurchaseLineItem oPurchaseLineItem) 
	{
		oPurchaseLineItem.setM_oPurchaseData(this);
		m_oPurchaseLineItems.add(oPurchaseLineItem);
	}
	
	public void prepareSupplyData () throws Exception 
	{
		setM_nPurchaseType(PurchaseType.kSupply);
		setM_dDate(GenericIDataProcessor.getDBCompatibleDateFormat(getM_strDate()));
//		setM_oVendorPurchaseOrderData(null);
		setM_nTotalAmount(Math.round(calculatePurchaseTotal(m_arrNonStockPurchaseLineItem)));
		if(m_nId > 0)
		{
			PurchaseData oPurchaseData = (PurchaseData) GenericIDataProcessor.populateObject(this);
			setM_nPaymentAmount(oPurchaseData.m_nPaymentAmount);
			setM_nBalanceAmount(m_nTotalAmount - oPurchaseData.m_nPaymentAmount);
		}
		else
			setM_nBalanceAmount(calculatePurchaseTotal(m_arrNonStockPurchaseLineItem));
		for (int nIndex = 0; nIndex < m_arrNonStockPurchaseLineItem.length; nIndex++)
		{
			m_arrNonStockPurchaseLineItem[nIndex].setM_nCreatedBy(m_nCreatedBy);
			m_arrNonStockPurchaseLineItem[nIndex].setM_dCreatedOn(Calendar.getInstance().getTime());
			addNSLineItem(m_arrNonStockPurchaseLineItem[nIndex]);
		}
	}

	public void addPurchase () throws Exception
	{
		Iterator<NonStockPurchaseLineItem> oNSPurchaseLineItems = m_oNonStockPurchaseLineItems.iterator();
		while (oNSPurchaseLineItems.hasNext())
			oNSPurchaseLineItems.next().addPurchase ();
	}
	
	private void addNSLineItem (NonStockPurchaseLineItem oPurchaseLineItem) 
	{
		oPurchaseLineItem.setM_oPurchaseData(this);
		m_oNonStockPurchaseLineItems.add(oPurchaseLineItem);
	}
	
	private String buildPurchaseLineItem (Set<PurchaseLineItem> oPurchaseLineItems)
	{
		String strXML = "";
		Object [] arrPurchaseLineitems = oPurchaseLineItems.toArray();
		for (int nIndex = 0; nIndex < arrPurchaseLineitems.length; nIndex ++)
		{
			PurchaseLineItem oPurchaseLineItem = (PurchaseLineItem) arrPurchaseLineitems [nIndex];
			strXML += oPurchaseLineItem.generateXML();
		}
		return strXML;
	}

	public void removeOldPurchaseQty() throws Exception 
	{
		Iterator<NonStockPurchaseLineItem> oNSPurchaseLineItems = m_oNonStockPurchaseLineItems.iterator();
		while (oNSPurchaseLineItems.hasNext())
			oNSPurchaseLineItems.next().removePurchaseQty ();
	}

	public void updatePurchaseQty () throws Exception
	{
		Iterator<NonStockPurchaseLineItem> oNSPurchaseLineItems = m_oNonStockPurchaseLineItems.iterator();
		while (oNSPurchaseLineItems.hasNext())
			oNSPurchaseLineItems.next().updatePurchaseQty ();
	}

	@JsonIgnore
	public ArrayList<NonStockPurchaseLineItem> getDeletedNSLineItems() 
	{
		ArrayList<NonStockPurchaseLineItem> arrDeletedNSItems = new ArrayList<NonStockPurchaseLineItem>();
		try
		{
			PurchaseData oPurchaseData = (PurchaseData) GenericIDataProcessor.populateObject(this);
			Iterator<NonStockPurchaseLineItem> oNSPurchaseLineItems = oPurchaseData.m_oNonStockPurchaseLineItems.iterator();
			while(oNSPurchaseLineItems.hasNext())
			{
				NonStockPurchaseLineItem oLineItem = oNSPurchaseLineItems.next();
				if(!isLineItemExists(oLineItem))
					arrDeletedNSItems.add(oLineItem);
			}
			
		}
		catch(Exception oException)
		{
		}
		return arrDeletedNSItems;
	}

	private boolean isLineItemExists(NonStockPurchaseLineItem oLineItem) 
	{
		boolean bIsItemExists = false;
		for(int nIndex = 0; !bIsItemExists && nIndex < m_arrNonStockPurchaseLineItem.length; nIndex++)
		{
			bIsItemExists = oLineItem.getM_nLineItemId() == m_arrNonStockPurchaseLineItem[nIndex].getM_nLineItemId();
		}
		return bIsItemExists;
	}

	public void removeNSItems (ArrayList<NonStockPurchaseLineItem> arrDeletedLineItems) throws Exception 
	{
		for(int nIndex = 0; nIndex < arrDeletedLineItems.size(); nIndex++)
		{
			PurchaseOrderLineItemData oPOLineItem = (PurchaseOrderLineItemData) GenericIDataProcessor.populateObject(arrDeletedLineItems.get(nIndex).getM_oPOLineItem());
			oPOLineItem.removePurchaseQty(arrDeletedLineItems.get(nIndex).getM_nQuantity());
			arrDeletedLineItems.get(nIndex).deleteObject();
		}
	}

	@SuppressWarnings("unchecked")
	public float getPurchaseAmount ()
	{
		Object [] arrPurchaseLineItems = m_oPurchaseLineItems.toArray();
		Object [] arrNSPurchaseLineItems = m_oNonStockPurchaseLineItems.toArray();
		List arrLineItem = new ArrayList(Arrays.asList(arrPurchaseLineItems));
		arrLineItem.addAll(Arrays.asList(arrNSPurchaseLineItems));
		Object[] arrPurchaseLine = arrLineItem.toArray();
		return calculatePurchaseTotal (arrPurchaseLine);
	}
	
	public float calculatePurchaseTotal (Object [] arrPurchaseLine)
	{
		float nTotal = 0;
		for (int nIndex = 0; nIndex < arrPurchaseLine.length; nIndex ++)
			nTotal += getLineItemTotal(arrPurchaseLine[nIndex]);
		return nTotal;
	}
	
	public String generateTallyDataXML(ArrayList<PurchaseData> arrPurchaseData) 
	{
		String strTallyDataXml = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Document oInvoiceXmlDoc = getXmlDocument ("<ReportPurchaseData>" + buildPurchaseXml (arrPurchaseData) + "</ReportPurchaseData>");
			Node oInvoiceNode = oXmlDocument.importNode (oInvoiceXmlDoc.getFirstChild (), true);
			oXmlDocument.appendChild(oInvoiceNode);
			strTallyDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateTallyDataXML - oException" + oException);
		}
		m_oLogger.debug("generateTallyDataXML - strTallyDataXml [OUT] : " + strTallyDataXml);
		return strTallyDataXml;
	}
	
	private float getLineItemTotal(Object oLineItem) 
	{
		float nTotal = 0;
		if (isPurchaseLineItemClass(oLineItem))
		{
			PurchaseLineItem oPurchaseLineItem = (PurchaseLineItem) oLineItem;
			nTotal = calculateTotalPurchaseLineItemAmount (oPurchaseLineItem);
		}
		else
		{
			NonStockPurchaseLineItem oNonStockPurchaseLineItem = (NonStockPurchaseLineItem) oLineItem;
			nTotal =  calculateTotalAmount (oNonStockPurchaseLineItem);
		}
		return nTotal;
	}
	
	private float calculateTotalAmount(NonStockPurchaseLineItem oNonStockPurchaseLineItem) 
	{
		float nAmount = 0;
		nAmount =  oNonStockPurchaseLineItem.getM_nQuantity() * oNonStockPurchaseLineItem.getM_nPrice() ;
		nAmount -= nAmount *(oNonStockPurchaseLineItem.getM_nDiscount()/100);
		nAmount += nAmount *(oNonStockPurchaseLineItem.getM_nTax()/100);
		nAmount += nAmount *(oNonStockPurchaseLineItem.getM_nExcise()/100);
		nAmount += nAmount *(oNonStockPurchaseLineItem.getM_nOtherCharges()/100);
		return nAmount;
	}

	private boolean isPurchaseLineItemClass(Object oLineItem) 
	{
		return oLineItem.getClass().toString().equalsIgnoreCase("class com.techmust.inventory.purchase.PurchaseLineItem");
	}

	private float calculateTotalPurchaseLineItemAmount(PurchaseLineItem oPurchaseLineItem) 
	{
		float nAmount = 0;
		float nQuantity = m_bIsForVendorOutstanding ?  oPurchaseLineItem.getM_nReturnedQuantity() : oPurchaseLineItem.getM_nQuantity();
		nAmount =  nQuantity * oPurchaseLineItem.getM_nPrice() ;
		nAmount -= nAmount *(oPurchaseLineItem.getM_nDiscount()/100);
		nAmount += nAmount *(oPurchaseLineItem.getM_nTax()/100);
		nAmount += nAmount *(oPurchaseLineItem.getM_nExcise()/100);
		nAmount += nAmount *(oPurchaseLineItem.getM_nOtherCharges()/100);
		return nAmount;
	}

	private String buildPurchaseXml(ArrayList<PurchaseData> arrPurchaseData) 
	{
		m_oLogger.debug("buildPurchaseXml");
		String strXml = "";
		for(int nIndex = 0; nIndex < arrPurchaseData.size(); nIndex++)
		{
			strXml += arrPurchaseData.get(nIndex).generateXML();
		}
	    return strXml;
	}
	
	private String buildInputTaxItems(ArrayList<Tax> arrTaxes) throws Exception 
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
			addChild (oXmlDocument, oRootElement, "m_strInputTaxClassificationName", TallyTransformData.getTallyTransformKeyValue ("TAXCLASSIFICATIONNAME-INPUT", arrTaxes.get(nIndex).getM_strTaxName(), arrTaxes.get(nIndex).getM_nPercentage()));
			addChild (oXmlDocument, oRootElement, "m_strPurchasesAmountLedgerName", TallyTransformData.getTallyTransformKeyValue ("LEDGERNAME-PURCHASESAMOUNT", arrTaxes.get(nIndex).getM_strTaxName(), arrTaxes.get(nIndex).getM_nPercentage()));
			addChild (oXmlDocument, oRootElement, "m_strInputTaxAmountLedgerName", TallyTransformData.getTallyTransformKeyValue ("LEDGERNAME-INPUTTAXAMOUNT", arrTaxes.get(nIndex).getM_strTaxName(), arrTaxes.get(nIndex).getM_nPercentage()));
			strXML += getXmlString(oXmlDocument);
		}
		strXML += "<totalTaxAmount>" + nTotalTaxAmount + "</totalTaxAmount>";
		return strXML;
	}
	
	private ArrayList<Tax> getTaxes() 
	{
		ArrayList<Tax> arrTax = new ArrayList<Tax>();
		buildPurchaseLineItemTaxes(arrTax, m_oPurchaseLineItems);
		buildNSPurchaseLineItemTaxes(arrTax, m_oNonStockPurchaseLineItems);
		return arrTax;
	}
	
	private void buildPurchaseLineItemTaxes(ArrayList<Tax> arrTax, Set<PurchaseLineItem> arrPurchaseLineItems) 
	{
		Iterator<PurchaseLineItem> oIterator = arrPurchaseLineItems.iterator();
		while(oIterator.hasNext())
		{
			PurchaseLineItem oPurchaseLineItemData = oIterator.next();
			addTax(arrTax, oPurchaseLineItemData);
		}
	}
	
	private void buildNSPurchaseLineItemTaxes(ArrayList<Tax> arrTax, Set<NonStockPurchaseLineItem> arrNSPurchaseLineItems) 
	{
		Iterator<NonStockPurchaseLineItem> oIterator = arrNSPurchaseLineItems.iterator();
		while(oIterator.hasNext())
		{
			NonStockPurchaseLineItem oNSPurchaseLineItemData = oIterator.next();
			addTax(arrTax, oNSPurchaseLineItemData);
		}
	}

	private void addTax(ArrayList<Tax> arrTax,  NonStockPurchaseLineItem oNSPurchaseLineItemData)
    {
		m_oLogger.info("addTax");
		Tax oLineItemTax = new Tax ();
		oLineItemTax.setM_strTaxName(oNSPurchaseLineItemData.getM_strTaxName());
		oLineItemTax.setM_nAmount(getTaxAmount(oNSPurchaseLineItemData));
		oLineItemTax.setM_nPercentage(oNSPurchaseLineItemData.getM_nTax());
		if(oLineItemTax.getM_nAmount() > 0 && isNewTaxData(oLineItemTax.getM_strTaxName(), oNSPurchaseLineItemData.getM_nTax(), arrTax))
			arrTax.add(oLineItemTax);
		else
			updateToTaxArray(arrTax, oLineItemTax);
    }
	
	private void addTax(ArrayList<Tax> arrTax, PurchaseLineItem oPurchaseLineItemData)
	{
		m_oLogger.info("addTax");
		Tax oLineItemTax = new Tax ();
		oLineItemTax.setM_strTaxName("VAT"); // Tax name has to be provided
		oLineItemTax.setM_nAmount(getTaxAmount(oPurchaseLineItemData));
		oLineItemTax.setM_nPercentage(oPurchaseLineItemData.getM_nTax());
		if(oLineItemTax.getM_nAmount() > 0 && isNewTaxData(oLineItemTax.getM_strTaxName(), oPurchaseLineItemData.getM_nTax(), arrTax))
			arrTax.add(oLineItemTax);
		else
			updateToTaxArray(arrTax, oLineItemTax);
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

	private float getTaxAmount(NonStockPurchaseLineItem oNSPurchaseLineItemData) 
	{
		float nAmount = 0;
		nAmount =  oNSPurchaseLineItemData.getM_nQuantity() * oNSPurchaseLineItemData.getM_nPrice() ;
		nAmount -= nAmount *(oNSPurchaseLineItemData.getM_nDiscount()/100);
		nAmount += nAmount *(oNSPurchaseLineItemData.getM_nExcise()/100);
		nAmount += nAmount *(oNSPurchaseLineItemData.getM_nOtherCharges()/100);
		return nAmount * oNSPurchaseLineItemData.getM_nTax()/100 ;
	}

	private float getTaxAmount(PurchaseLineItem oPurchaseLineItemData)
    {
		float nAmount = 0;
		nAmount =  oPurchaseLineItemData.getM_nQuantity() * oPurchaseLineItemData.getM_nPrice() ;
		nAmount -= nAmount *(oPurchaseLineItemData.getM_nDiscount()/100);
		nAmount += nAmount *(oPurchaseLineItemData.getM_nExcise()/100);
		nAmount += nAmount *(oPurchaseLineItemData.getM_nOtherCharges()/100);
		return nAmount * oPurchaseLineItemData.getM_nTax()/100 ;
    }

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void removePaymentAmount(PurchasePaymentData oPurchasePaymentData) throws Exception 
	{
		PurchaseData oPurchaseData = (PurchaseData) GenericIDataProcessor.populateObject(this);
		setM_nPaymentAmount(oPurchaseData.getM_nPaymentAmount() - oPurchasePaymentData.getM_nAmount());
		setM_nBalanceAmount(oPurchaseData.getM_nTotalAmount() - m_nPaymentAmount);
		this.updateObject();
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*
		if (m_bIsForAgeWise)
			buildAgeWiseInvoiceList (oCriteria, strColumn, strOrderBy);
		else if (m_bIsForVendorOutstanding)
			buildVendorOutstandingListCriteria (oCriteria, strColumn, strOrderBy);
		else
			buildListCriteria (oCriteria, strColumn, strOrderBy);
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
