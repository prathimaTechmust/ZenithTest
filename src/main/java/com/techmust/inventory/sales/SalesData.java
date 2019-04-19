package com.techmust.inventory.sales;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.FetchMode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ClientGroupData;
import com.techmust.clientmanagement.ContactData;
import com.techmust.clientmanagement.SiteData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.challan.ChallanData;
import com.techmust.inventory.challan.ChallanDataProcessor;
import com.techmust.inventory.challan.ChallanDataResponse;
import com.techmust.inventory.crm.PubliciseData;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.invoice.TaxDiscountInfo;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.purchaseorder.PurchaseOrderData;
import com.techmust.inventory.purchaseorder.PurchaseOrderLineItemData;
import com.techmust.master.tax.Tax;
import com.techmust.traderp.util.NumberToWords;
import com.techmust.traderp.util.TraderpUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="tac06_sales")
public class SalesData extends TenantData 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ac06_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int m_nId;
	@Column(name="ac06_to")
	private String m_strTo;
	@Column(name="ac06_invoiceNo")
	private String m_strInvoiceNo;
	@Column(name="ac06_sale_date")
	@Basic
	@Temporal(TemporalType.DATE)
	private Date m_dDate; //Sales Date
	@Column(name="ac06_created_on")
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date m_dCreatedOn;
	@Column(name="ac06_Created_by")
	private int m_nCreatedBy;
	@ManyToOne
	@JoinColumn(name="ac06_Client_id")
	private ClientData m_oClientData;
	@ManyToOne
	@JoinColumn(name="ac06_Site_id")
	private SiteData m_oSiteData;
	@ManyToOne
	@JoinColumn(name="ac14_Contact_id")
	private ContactData m_oContactData;
	@Column(name="ac06_challanNumber")
	private String m_strChallanNumber;
	@Column(name="ac06_isCForm_provided")
	@ColumnDefault("0")
	private boolean m_bIsCFormProvided;
	
	// non-persistent data used by client
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Transient
	private String m_strDate;

	// non-persistent data used by xsl
	@Transient
	private String m_strTime;
	@Transient
	private String m_strChallanDate;

	// non-persistent data used by reports from client
	@Transient
	private String m_strFromDate;
	@Transient
	private String m_strToDate;
	@Transient
	private boolean m_bIsForClientwise;
	@Transient
	private boolean m_bIsForItemGroupClientProfile;
	@Transient
	private int m_nItemId;
	@Transient
	private int m_nItemGroupId;
	
	@JsonManagedReference
	@OneToMany
	@JoinColumn( name="ac07_id")
	private Set<SalesLineItemData> m_oSalesLineItems;
	@Transient
	public SalesLineItemData [] m_arrSalesLineItem;
	
	@JsonManagedReference
	@OneToMany
	@JoinColumn( name="ac19_id")
	private Set<NonStockSalesLineItemData> m_oNonStockSalesLineItems;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ac14_purchaseorder_id")
	private PurchaseOrderData m_oPOData;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ac11_invoiceId")
	private InvoiceData m_oInvoiceData;
	
	public SalesData ()
	{
		m_nId = -1;
		m_oSalesLineItems = new HashSet<SalesLineItemData> ();
		m_oNonStockSalesLineItems = new HashSet<NonStockSalesLineItemData> ();
	}

	public int getM_nId() 
	{
		return m_nId;
	}

	public void setM_nId(int id)
	{
		m_nId = id;
	}

	public String getM_strTo() {
		return m_strTo;
	}

	public void setM_strTo(String to) {
		m_strTo = to;
	}

	public String getM_strInvoiceNo() {
		return m_strInvoiceNo;
	}

	public void setM_strInvoiceNo(String invoiceNo) {
		m_strInvoiceNo = invoiceNo;
	}

	public Date getM_dDate() {
		return m_dDate;
	}

	public void setM_dDate(Date date) {
		m_dDate = date;
	}

	public Date getM_dCreatedOn() {
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date createdOn) {
		m_dCreatedOn = createdOn;
	}

	public int getM_nCreatedBy() {
		return m_nCreatedBy;
	}

	public void setM_oCreatedBy(int nCreatedBy) {
		m_nCreatedBy = nCreatedBy;
	}

	public ClientData getM_oClientData() {
		return m_oClientData;
	}

	public void setM_oClientData(ClientData clientData) {
		m_oClientData = clientData;
	}

	public SiteData getM_oSiteData() {
		return m_oSiteData;
	}

	public void setM_oSiteData(SiteData siteData) {
		m_oSiteData = siteData;
	}

	public ContactData getM_oContactData() {
		return m_oContactData;
	}

	public void setM_oContactData(ContactData contactData) {
		m_oContactData = contactData;
	}

	public String getM_strChallanNumber() {
		return m_strChallanNumber;
	}

	public void setM_strChallanNumber(String challanNumber) {
		m_strChallanNumber = challanNumber;
	}

	public boolean isM_bIsCFormProvided() {
		return m_bIsCFormProvided;
	}

	public void setM_bIsCFormProvided(boolean isCFormProvided) {
		m_bIsCFormProvided = isCFormProvided;
	}

	public UserInformationData getM_oUserCredentialsData() {
		return m_oUserCredentialsData;
	}

	public void setM_oUserCredentialsData(UserInformationData userCredentialsData) {
		m_oUserCredentialsData = userCredentialsData;
	}

	public String getM_strDate() {
		return m_strDate;
	}

	public void setM_strDate(String date) {
		m_strDate = date;
	}

	public String getM_strTime() {
		return m_strTime;
	}

	public void setM_strTime(String time) {
		m_strTime = time;
	}

	public String getM_strChallanDate() {
		return m_strChallanDate;
	}

	public void setM_strChallanDate(String challanDate) {
		m_strChallanDate = challanDate;
	}

	public String getM_strFromDate() {
		return m_strFromDate;
	}

	public void setM_strFromDate(String fromDate) {
		m_strFromDate = fromDate;
	}

	public String getM_strToDate() {
		return m_strToDate;
	}

	public void setM_strToDate(String toDate) {
		m_strToDate = toDate;
	}

	public boolean isM_bIsForClientwise() {
		return m_bIsForClientwise;
	}

	public void setM_bIsForClientwise(boolean isForClientwise) {
		m_bIsForClientwise = isForClientwise;
	}

	public boolean isM_bIsForItemGroupClientProfile() {
		return m_bIsForItemGroupClientProfile;
	}

	public void setM_bIsForItemGroupClientProfile(
			boolean isForItemGroupClientProfile) {
		m_bIsForItemGroupClientProfile = isForItemGroupClientProfile;
	}

	public int getM_nItemId() {
		return m_nItemId;
	}

	public void setM_nItemId(int itemId) {
		m_nItemId = itemId;
	}

	public int getM_nItemGroupId() {
		return m_nItemGroupId;
	}

	public void setM_nItemGroupId(int itemGroupId) {
		m_nItemGroupId = itemGroupId;
	}

	public void setM_oNonStockSalesLineItems(
			Set<NonStockSalesLineItemData> m_oNonStockSalesLineItems) {
		this.m_oNonStockSalesLineItems = m_oNonStockSalesLineItems;
	}

	public Set<NonStockSalesLineItemData> getM_oNonStockSalesLineItems() {
		return m_oNonStockSalesLineItems;
	}

	public void setM_oPOData(PurchaseOrderData m_oPOData) {
		this.m_oPOData = m_oPOData;
	}

	public PurchaseOrderData getM_oPOData() {
		return m_oPOData;
	}

	public void setM_oInvoiceData(InvoiceData m_oInvoiceData) {
		this.m_oInvoiceData = m_oInvoiceData;
	}

	public InvoiceData getM_oInvoiceData() {
		return m_oInvoiceData;
	}

	public Set<SalesLineItemData> getM_oSalesLineItems() {
		return m_oSalesLineItems;
	}

	public void setM_oSalesLineItems(Set<SalesLineItemData> salesLineItems) {
		m_oSalesLineItems = salesLineItems;
	}

	@Override
	public String generateXML() 
	{
		String strSalesDataXml = "";
		try
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "SalesData");
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			addChild (oXmlDocument, oRootElement, "m_dDate", m_dDate != null ? m_dDate.toString() : "");
			addChild (oXmlDocument, oRootElement, "m_strInvoiceNo", m_strInvoiceNo);
			addChild (oXmlDocument, oRootElement, "m_strTo", m_strTo);
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dDate));
			addChild (oXmlDocument, oRootElement, "m_strChallanNumber", m_strChallanNumber);
			addChild (oXmlDocument, oRootElement, "m_strTime", TraderpUtil.getTime(m_dDate));
			addChild (oXmlDocument, oRootElement, "m_strChallanDate", getChallanDate(m_nId));
			addChild (oXmlDocument, oRootElement, "m_strPurchaseOrderNumber", m_oPOData != null ? m_oPOData.getM_strPurchaseOrderNumber() : "");
			addChild (oXmlDocument, oRootElement, "m_strPurchaseOrderDate", m_oPOData != null ? GenericIDataProcessor.getClientCompatibleFormat(m_oPOData.getM_dPurchaseOrderDate()) : "");
			
			Document oTaxItemDoc = getXmlDocument ("<m_oTaxes>" + buildTaxItems (getTaxes()) + "</m_oTaxes>");
			Node oTaxNode = oXmlDocument.importNode (oTaxItemDoc.getFirstChild (), true);
			oRootElement.appendChild (oTaxNode);
			
			Document oClientDoc = getXmlDocument (m_oClientData.generateXML());
			Node oClientNode = oXmlDocument.importNode (oClientDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
			
			Document oSiteDoc = getXmlDocument (m_oSiteData.generateXML());
			Node oSiteNode = oXmlDocument.importNode (oSiteDoc.getFirstChild (), true);
			oRootElement.appendChild (oSiteNode);
			
			Document oSalesLineItemDoc = getXmlDocument ("<m_oSalesLineItems>" + buildSalesLineItem () + "</m_oSalesLineItems>");
			Node oLineItemNode = oXmlDocument.importNode (oSalesLineItemDoc.getFirstChild (), true);
			oRootElement.appendChild (oLineItemNode);
			
			
			strSalesDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException)
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strSalesDataXml;
	}

	@SuppressWarnings("unchecked")
	private String buildSalesLineItem() {
		String strXML = "";
    	Object [] arrSalesLineitems = m_oSalesLineItems.toArray();
		Object [] arrNSSalesLineitems = m_oNonStockSalesLineItems.toArray();
		List arrLneItem = new ArrayList(Arrays.asList(arrSalesLineitems));
		arrLneItem.addAll(Arrays.asList(arrNSSalesLineitems));
		Object[] arrSalesLine = arrLneItem.toArray();
		getOrderedLineItems (arrSalesLine);
		float nSubTotal = 0;
		float nTotalQuantity = 0;
		for (int nIndex = 0; nIndex < arrSalesLine.length; nIndex ++)
		{
			if (isSalesLineItemClass(arrSalesLine[nIndex]))
			{
				SalesLineItemData oSalesLineItemData = (SalesLineItemData) arrSalesLine [nIndex];
				strXML += oSalesLineItemData.generateXMLWithSerialNumber(nIndex+1);
				nSubTotal += oSalesLineItemData.getM_nPrice() * oSalesLineItemData.getM_nQuantity();
				nTotalQuantity +=  oSalesLineItemData.getM_nQuantity();
			}
			else
			{
				NonStockSalesLineItemData oNSSalesLineItemData = (NonStockSalesLineItemData) arrSalesLine [nIndex];
				strXML += oNSSalesLineItemData.generateXMLWithSerialNumber(nIndex+1);
				nSubTotal += oNSSalesLineItemData.getM_nPrice() * oNSSalesLineItemData.getM_nQuantity();
				nTotalQuantity +=  oNSSalesLineItemData.getM_nQuantity();
			}
		}
		strXML += "<subTotal>" + nSubTotal + "</subTotal>";
		strXML += "<totalQuantity>" + nTotalQuantity + "</totalQuantity>";
		String strLineItemXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <LineItems>" + strXML + "</LineItems>";
		strXML += "<discounts>" + addDiscountInfo (strLineItemXml) + "</discounts>";
		strXML += "<taxes>" + addTaxInfo (strLineItemXml) + "</taxes>";
		return strXML;
	}

	private String getChallanDate(int nSalesId) 
	{
		String strChallanDate = "";
		ChallanDataProcessor oChallanDataProcessor = new ChallanDataProcessor ();
		ChallanData oChallanData = new ChallanData ();
		SalesData oSalesData = new SalesData ();
		ChallanDataResponse oChallanDataResponse = new ChallanDataResponse ();
		try 
		{
			oSalesData.setM_nId(nSalesId);
			oChallanData.setM_oSalesData(oSalesData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oChallanDataResponse = (ChallanDataResponse) oChallanDataProcessor.list(oChallanData,oOrderBy);
			strChallanDate = ChallanDataProcessor.getClientCompatibleFormat( oChallanDataResponse.m_arrChallan.get(0).getM_dCreatedOn());
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getChallanTime - oException" + oException);
		}
		
		return strChallanDate;
	}

	@Override
	public GenericData getInstanceData(String strXML,
			UserInformationData credentials) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn,
			String strOrderBy) {
		if (m_bIsForClientwise)
			buildClientwiseListCriteria (oCriteria, strColumn, strOrderBy);
		else if (isM_bIsForItemGroupClientProfile())
			buildClientwiseListCriteriaForItemGroup (oCriteria, strColumn, strOrderBy);
		else
			buildListCriteria (oCriteria, strColumn, strOrderBy);
		return oCriteria;
	}

	private void buildListCriteria(Criteria oCriteria, String strColumn,
			String strOrderBy) {
		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
		if (m_nItemId > 0)
		{
			Criteria oSalesLineItemCriteria = oCriteria.createCriteria ("m_oSalesLineItems");
			Criteria oClientCriteria = oSalesLineItemCriteria.createCriteria("m_oItemData");
			oClientCriteria.add (Restrictions.eq ("m_nItemId", m_nItemId));
		}
		if (!m_strTo.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strTo", m_strTo.trim(), MatchMode.ANYWHERE));
		if (m_strInvoiceNo != null && !m_strInvoiceNo.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strInvoiceNo", m_strInvoiceNo.trim()));
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_nCreatedBy > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dDate", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if ((m_strFromDate != null &&m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if(m_oClientData.getM_strCompanyName() != null && !m_oClientData.getM_strCompanyName().trim().isEmpty())
			oClientDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oClientData", m_oClientData));
		if (m_oContactData != null && m_oContactData.getM_nContactId() > 0)
			oCriteria.add(Restrictions.eq("m_oContactData", m_oContactData));
		if (m_oSiteData != null && m_oSiteData.getM_nSiteId() > 0)
			oCriteria.add(Restrictions.eq("m_oSiteData", m_oSiteData));
		if (strColumn.contains("m_strCompanyName"))
			oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nId");
		
	}

	private void buildClientwiseListCriteriaForItemGroup(Criteria oCriteria,String strColumn, String strOrderBy) 
	{
		Criteria oSalesLineItemDataCriteria = oCriteria.createCriteria("m_oSalesLineItems", "saleslineitems");
		Criteria oItemCriteria = oSalesLineItemDataCriteria.createCriteria("m_oItemData", "itemdata");
		Criteria oItemGroupCriteria = oItemCriteria.createCriteria("m_oItemGroups", "itemgroupdata");
		oItemGroupCriteria.setProjection( Property.forName("m_oGroupItems"));
		oItemGroupCriteria.add(Restrictions.eq("m_nItemGroupId", m_nItemGroupId));
		oCriteria.setFetchMode("m_oSalesLineItems", FetchMode.JOIN);
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("m_oClientData"));
		oProjectionList.add(Projections.sum("saleslineitems.m_nAmount"), "amount" );
		oCriteria.setProjection(oProjectionList);
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
				oCriteria.add (Restrictions.between ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (strColumn.contains("m_nAmount"))
			oSalesLineItemDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc("amount") : Order.asc("amount"));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_oClientData");
		
	}

	private void buildClientwiseListCriteria(Criteria oCriteria,String strColumn, String strOrderBy) 
	{
		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
		Criteria oSalesLineItemDataCriteria = oCriteria.createCriteria("m_oSalesLineItems", "salesline");
		Criteria oItemDataCriteria = oSalesLineItemDataCriteria.createCriteria("m_oItemData", "item");
		oItemDataCriteria.add(Restrictions.eq("m_nItemId", m_nItemId));
		oCriteria.setFetchMode("m_oSalesLineItems", FetchMode.JOIN);
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("m_oClientData"));
		oProjectionList.add(Projections.sum("salesline.m_nQuantity"), "salesline.m_nQuantity" );
		oProjectionList.add(Projections.property("salesline.m_nPrice"));
		oCriteria.setProjection( oProjectionList);
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (strColumn.contains("m_strCompanyName"))
			oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_oClientData");
		
	}

	private ArrayList<Tax> getTaxes() 
	{
		ArrayList<Tax> arrTax = new ArrayList<Tax>();
		Iterator<SalesLineItemData> oIterator = m_oSalesLineItems.iterator();
		while(oIterator.hasNext())
		{
			SalesLineItemData oSalesLineItemData = oIterator.next();
			addTaxs(arrTax, oSalesLineItemData);
		}
		Iterator<NonStockSalesLineItemData> oNSIterator = m_oNonStockSalesLineItems.iterator();
		while(oNSIterator.hasNext())
		{
			NonStockSalesLineItemData oNSSalesLineItemData = oNSIterator.next();
			addTax(arrTax, oNSSalesLineItemData);
		}
		return arrTax;
	}
	
	private void addTaxs(ArrayList<Tax> arrTax, SalesLineItemData oSalesLineItemData)
	{
		m_oLogger.info("addTax");
		Tax oLineItemTax = new Tax ();
		oLineItemTax.setM_strTaxName(oSalesLineItemData.getM_strTaxName());
		oLineItemTax.setM_nAmount(getTaxAmount(oSalesLineItemData));
		oLineItemTax.setM_nPercentage(oSalesLineItemData.getM_nTax());
		if(oLineItemTax.getM_nAmount() > 0 && isNewTaxData(oLineItemTax.getM_strTaxName(), oSalesLineItemData.getM_nTax(), arrTax))
			arrTax.add(oLineItemTax);
		else
			updateToTaxArray(arrTax, oLineItemTax);
	}
	
	private void addTax(ArrayList<Tax> arrTax,  NonStockSalesLineItemData oNSSalesLineItemData)
    {
		m_oLogger.info("addTax");
		Tax oLineItemTax = new Tax ();
		oLineItemTax.setM_strTaxName(oNSSalesLineItemData.getM_strTaxName());
		oLineItemTax.setM_nAmount(getTaxAmounts(oNSSalesLineItemData));
		oLineItemTax.setM_nPercentage(oNSSalesLineItemData.getM_nTax());
		if(oLineItemTax.getM_nAmount() > 0 && isNewTaxData(oLineItemTax.getM_strTaxName(), oNSSalesLineItemData.getM_nTax(), arrTax))
			arrTax.add(oLineItemTax);
		else
			updateToTaxArray(arrTax, oLineItemTax);
    }
	
	private float getTaxAmount(SalesLineItemData oSalesLineItemData) 
	{
		return (oSalesLineItemData.getM_nQuantity()*(oSalesLineItemData.getM_nPrice() - (oSalesLineItemData.getM_nPrice() * (oSalesLineItemData.getM_nDiscount()/100)))) * oSalesLineItemData.getM_nTax()/100 ;
	}
	
	private float getTaxAmounts (NonStockSalesLineItemData oNSSalesLineItemData)
    {
		return (oNSSalesLineItemData.getM_nQuantity()*(oNSSalesLineItemData.getM_nPrice() - (oNSSalesLineItemData.getM_nPrice() * (oNSSalesLineItemData.getM_nDiscount()/100)))) * oNSSalesLineItemData.getM_nTax()/100 ;
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
	
	private String buildTaxItems(ArrayList<Tax> arrTaxes) throws Exception 
	{
		String strXML = "";
		float nTotalTaxAmount = 0;
		for(int nIndex = 0; nIndex < arrTaxes.size(); nIndex++)
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "Tax"); 
			addChild (oXmlDocument, oRootElement, "m_strTaxName", isOutOfStateClient () ? "CST" : arrTaxes.get(nIndex).getM_strTaxName());
			addChild (oXmlDocument, oRootElement, "m_nTaxPercent", arrTaxes.get(nIndex).getM_nPercentage());
			addChild (oXmlDocument, oRootElement, "m_nTaxAmount", arrTaxes.get(nIndex).getM_nAmount());
			nTotalTaxAmount += arrTaxes.get(nIndex).getM_nAmount();
			strXML += getXmlString(oXmlDocument);
		}
		strXML += "<totalTaxAmountInWords>" + NumberToWords.convertNumberToWords(new BigDecimal (nTotalTaxAmount), true, true) + "</totalTaxAmountInWords>";
		return strXML;
	}
	
	private boolean isOutOfStateClient() 
	{
		boolean bIsOutOfStateClient = false;
		try 
		{
			SalesData oSalesData = (SalesData)GenericIDataProcessor.populateObject(this);
			bIsOutOfStateClient = oSalesData.m_oClientData.isM_bOutstationClient();
		}
		catch (Exception oException)
		{
		}
		return bIsOutOfStateClient;
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
	
	private boolean isSalesLineItemClass(Object oLineItem) 
	{
		return oLineItem.getClass().toString().equalsIgnoreCase("class com.techmust.inventory.sales.SalesLineItemData");
	}
	
	private String addDiscountInfo(String strLineItemXml) 
	{
		String strXml = "";
		Document oLineItemDoc = getXmlDocument(strLineItemXml);
		NodeList oLineItems = oLineItemDoc.getElementsByTagName("LineItemData");
		ArrayList<TaxDiscountInfo> arrTaxDiscountInfo = new ArrayList<TaxDiscountInfo> ();
		for(int nIndex = 0; nIndex < oLineItems.getLength(); nIndex ++)
		{
			Node oSalesLineItem = getChildNodeByName(oLineItems.item(nIndex), "SalesLineItemData");
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
	
	private String addTaxInfo(String strLineItemXml)
	{
		String strXml = "";
		Document oLineItemDoc = getXmlDocument(strLineItemXml);
		NodeList oLineItems = oLineItemDoc.getElementsByTagName("LineItemData");
		ArrayList<TaxDiscountInfo> arrTaxDiscountInfo = new ArrayList<TaxDiscountInfo> ();
		for(int nIndex = 0; nIndex < oLineItems.getLength(); nIndex ++)
		{
			Node oSalesLineItem = getChildNodeByName(oLineItems.item(nIndex), "SalesLineItemData");
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
		strXml = buildTaxXml (arrTaxDiscountInfo);
		strXml += "<totalTaxAmount>" + getTotalDiscount (arrTaxDiscountInfo) + "</totalTaxAmount>";
		return strXml;
	}
	
	private int getSerialNo(Object oObject) 
	{
		int nSerialNumber = 0;
		if (isSalesLineItemClass (oObject))
		{
			SalesLineItemData oSalesLineItemData = (SalesLineItemData)oObject;
			nSerialNumber = oSalesLineItemData.getM_nSerialNumber();
		}
		else
		{
			NonStockSalesLineItemData oNSSalesLineItemData = (NonStockSalesLineItemData)oObject;
			nSerialNumber = oNSSalesLineItemData.getM_nSerialNumber();
		}
		return nSerialNumber;
	}
	
	private float getAmount(Node oItem) 
	{
		float nQuantity = TraderpUtil.getFloatValue(getTagValue (oItem, "m_nQuantity"));
		float nPrice = TraderpUtil.getFloatValue(getTagValue (oItem, "m_nPrice"));
		return nQuantity * nPrice;
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
	
	private float getTotalDiscount(ArrayList<TaxDiscountInfo> arrTaxDiscountInfo) 
	{
		float nTotalDiscount = 0;
		for(int nIndex = 0; nIndex < arrTaxDiscountInfo.size(); nIndex++)
			nTotalDiscount += arrTaxDiscountInfo.get(nIndex).m_nAmount * (arrTaxDiscountInfo.get(nIndex).m_nPercentage / 100);
		return nTotalDiscount;
	}
	
	private float getDiscountedAmount(Node oItem) 
	{
		float nQuantity = TraderpUtil.getFloatValue(getTagValue (oItem, "m_nQuantity"));
		float nPrice = TraderpUtil.getFloatValue(getTagValue (oItem, "m_nPrice"));
		float nDiscount = TraderpUtil.getFloatValue(getTagValue (oItem, "m_nDiscount"));
		return (nPrice * ((100-nDiscount)/100) * nQuantity);
	}
	
	private String buildTaxXml(ArrayList<TaxDiscountInfo> arrTaxDiscountInfo) 
	{
		String strXML = "";
		for (int nIndex = 0; nIndex < arrTaxDiscountInfo.size(); nIndex++)
		{
			strXML += "<LineItemData>";
			strXML += "<SerialNumber></SerialNumber>";
			strXML += generateTaxXML(arrTaxDiscountInfo.get(nIndex));
			strXML += "</LineItemData>";
		}
		return strXML;
	}

	private String generateTaxXML(TaxDiscountInfo oTaxDiscountInfo) 
	{
		m_oLogger.info ("generateTaxXML");
		String strTaxXml = "";
		try 
		{
			
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "SalesLineItemData");
		
			addChild (oXmlDocument, oRootElement, "m_nLineItemId", -1);
			addChild (oXmlDocument, oRootElement, "m_strArticleNumber", "");
			addChild (oXmlDocument, oRootElement, "m_strArticleDescription", getTaxDescription(oTaxDiscountInfo));
			addChild (oXmlDocument, oRootElement, "m_strDetail", "");
			addChild (oXmlDocument, oRootElement, "m_strUnit", "");
			addChild (oXmlDocument, oRootElement, "m_nQuantity", "");
			addChild (oXmlDocument, oRootElement, "m_nPrice", "");
			addChild (oXmlDocument, oRootElement, "m_nTax", "");
			addChild (oXmlDocument, oRootElement, "m_nDiscount", "");
			addChild (oXmlDocument, oRootElement, "m_nAmount", getDiscountPrice(oTaxDiscountInfo)); 
			strTaxXml = getXmlString(oXmlDocument);
			
		} 
		catch (Exception oException)
		{
			m_oLogger.error("generateTaxXML - oException" + oException);
		}
		return strTaxXml;
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
	
	private String generateDiscountXML(TaxDiscountInfo oTaxDiscountInfo) 
	{
		m_oLogger.info ("generateDiscountXML");
		String strDiscountsXml = "";
		try 
		{
			
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "SalesLineItemData");
		
			addChild (oXmlDocument, oRootElement, "m_nLineItemId", -1);
			addChild (oXmlDocument, oRootElement, "m_strArticleNumber", "");
			addChild (oXmlDocument, oRootElement, "m_strArticleDescription", getDescription(oTaxDiscountInfo));
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
	
	private String getDescription(TaxDiscountInfo oTaxDiscountInfo) 
	{
		String strDesc = "";
		strDesc += "Less @ " + TraderpUtil.formatNumber(oTaxDiscountInfo.m_nPercentage) + "% on Rs." + TraderpUtil.formatNumber(oTaxDiscountInfo.m_nAmount) + " for Sl. " + getSerialsAsString(oTaxDiscountInfo.m_arrSerials);        
		return strDesc;
	}
	
	private String getTaxDescription(TaxDiscountInfo oTaxDiscountInfo) 
	{
		String strDesc = "";
		strDesc += TraderpUtil.formatNumber(oTaxDiscountInfo.m_nPercentage) + "% OUTPUT TAX % on Rs." + TraderpUtil.formatNumber(oTaxDiscountInfo.m_nAmount) + " for Sl. " + getSerialsAsString(oTaxDiscountInfo.m_arrSerials);        
		return strDesc;
	}
	
	private String getSerialsAsString(ArrayList<Integer> arrSerials) 
	{
		String strSerial = "";
		for (int nIndex = 0; nIndex < arrSerials.size(); nIndex++)
			strSerial += nIndex == 0 ? arrSerials.get(nIndex) : "," + arrSerials.get(nIndex);
		return strSerial;
	}
	
	private float getDiscountPrice(TaxDiscountInfo oTaxDiscountInfo) 
	{
		float nDiscountPrice = 0;
		nDiscountPrice = oTaxDiscountInfo.m_nAmount * (oTaxDiscountInfo.m_nPercentage / 100);
		return nDiscountPrice;
	}
	
	public void create(PurchaseOrderData oPOData) throws NonStockItemException, Exception
    {
	 // ClientData oClientData = ClientData.getInstance (oPOData.getM_oClientData());
		ClientData oClientData = new ClientData ();
		oClientData.setM_nClientId(oPOData.getM_oClientData().getM_nClientId());
		oClientData = (ClientData) GenericIDataProcessor.populateObject(oClientData);
		
		setM_strTo (oClientData.getM_strCompanyName());
		setM_oClientData(oPOData.getM_oClientData());
		setM_oSiteData(oPOData.getM_oSiteData());
		setM_oContactData(oPOData.getM_oContactData());
		setM_oCreatedBy(oPOData.getM_nCreatedBy());
		setM_dDate(Calendar.getInstance().getTime ());
		Set<PurchaseOrderLineItemData> oLineItems = oPOData.getM_oPurchaseOrderLineItems();
		Iterator<PurchaseOrderLineItemData> oPOLineItems = oLineItems.iterator();
		while (oPOLineItems.hasNext())
		{
			PurchaseOrderLineItemData oPOLineItem = oPOLineItems.next();
			if (oPOLineItem.getM_nShipQty() > 0)
				this.addLineItems(oPOLineItem);
		}
		int nSalesLineItemSize = getM_oSalesLineItems().size();
		if (nSalesLineItemSize > 0)
			m_arrSalesLineItem = getM_oSalesLineItems().toArray(new SalesLineItemData[nSalesLineItemSize]);
		setM_oUserCredentialsData(oPOData.getM_oUserCredentialsData());
    }
	
	public void addLineItems (PurchaseOrderLineItemData oPOLineItem) throws NonStockItemException, Exception
    {
		SalesLineItemData oSalesLineItemData = new SalesLineItemData ();
		try
		{
			oSalesLineItemData.create (oPOLineItem, m_oClientData.isM_bOutstationClient());
			oSalesLineItemData.setM_oSalesData(this);
			oSalesLineItemData.setM_nCreatedBy(m_nCreatedBy);
			oSalesLineItemData.setM_nSerialNumber(oPOLineItem.getM_nSerialNumber());
			m_oSalesLineItems.add(oSalesLineItemData);
		}
		catch (NonStockItemException oException)
		{
			NonStockSalesLineItemData oNSSLineItem = new NonStockSalesLineItemData ();
			oNSSLineItem.create (oPOLineItem);
			oNSSLineItem.setM_oSalesData(this);
			oNSSLineItem.setM_nSerialNumber(oPOLineItem.getM_nSerialNumber());
			m_oNonStockSalesLineItems.add(oNSSLineItem);
		}
    }
	
	public ChallanData createChallan() throws Exception
    {
		ChallanData oChallan = new ChallanData ();
		try
		{
			// see if challan exists for this sale
			oChallan = ChallanData.getInstance (this);
		}
		catch (Exception oNoChallan)
		{
			oChallan = ChallanData.createChallan (this);
			m_strChallanNumber = oChallan.getM_strChallanNumber();
			updateObject();
			oChallan.setM_oSalesData(this);
		}
	    return oChallan;
    }
	
	public InvoiceData createInvoice () throws Exception
	{
		InvoiceData oInvoiceData = new InvoiceData ();
		try
		{
			oInvoiceData = InvoiceData.getInstance(m_nId);
		}
		catch (Exception oNoInvoice)
		{
			oInvoiceData = oInvoiceData.create(this, m_nCreatedBy);
			m_strInvoiceNo = oInvoiceData.getM_strInvoiceNumber();
			updateObject();
		}
		return oInvoiceData;
	}
	
	public static SalesData getInstance (PurchaseOrderData oPOData) throws NonStockItemException, Exception
	{
		SalesData oSalesData = new SalesData ();
		oSalesData.create(oPOData);
		if (oSalesData.m_oSalesLineItems.size() == 0 && oSalesData.m_oNonStockSalesLineItems.size() == 0)
			oSalesData = null;
		return oSalesData;
	}
	
	public float getQuantity (ItemData oItem)
    {
		Iterator<SalesLineItemData> oLineItems = m_oSalesLineItems.iterator();
		float nQuantity = 0;
		while (oItem != null && oLineItems.hasNext())
		{
			SalesLineItemData oLineItem = oLineItems.next ();
			if (oLineItem.getM_oItemData().getM_nItemId() != oItem.getM_nItemId())
				continue;
			nQuantity = oLineItem.getM_nQuantity();
			break;
		}
		return nQuantity;
    }
	
	public float getQuantity (PurchaseOrderLineItemData oPOLineItem) throws Exception
	{
		float nQuantity = 0;
		ItemData oItem = oPOLineItem.getStockLineItem();
		if (oItem != null)
			nQuantity = getQuantity(oItem);
		else
		{
			Iterator<NonStockSalesLineItemData> oLineItems = m_oNonStockSalesLineItems.iterator();
			while (oLineItems.hasNext())
			{
				NonStockSalesLineItemData oLineItem = oLineItems.next ();
				if (oLineItem.getM_oPOLineItem().getM_nId() != oPOLineItem.getM_nId())
					continue;
				nQuantity = oLineItem.getM_nQuantity();
				break;
			}
		}
		return nQuantity;
	}
	
	public void mergeClient(ClientData oClientData) throws Exception
	{
		ClientData oClient = new ClientData ();
		oClient.setM_nClientId(oClientData.getM_nClientId());
		setM_oClientData(oClient);
		setM_strTo(oClientData.getM_strCompanyName());
		setM_oSiteData(getSiteData (oClientData));
		setM_oContactData(getContact (oClientData));
		updateObject();
	}
	
	private SiteData getSiteData(ClientData oClientData) 
	{
		SiteData oSiteData = null;
		Iterator<SiteData> oSites = oClientData.getM_oSites().iterator();
		while (oSites.hasNext())
			oSiteData = oSites.next();
		return oSiteData;
	}
	
	private ContactData getContact(ClientData oClientData) 
	{
		ContactData oContactData = null;
		Iterator<ContactData> oContacts = oClientData.getM_oContacts().iterator();
		while (oContacts.hasNext())
			oContactData = oContacts.next();
		return oContactData;
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	public float getSalesTotal() 
	{
		Object [] arrSalesLineitems = m_oSalesLineItems.toArray();
		Object [] arrNSSalesLineitems = m_oNonStockSalesLineItems.toArray();
		List arrLneItem = new ArrayList(Arrays.asList(arrSalesLineitems));
		arrLneItem.addAll(Arrays.asList(arrNSSalesLineitems));
		Object[] arrSalesLine = arrLneItem.toArray();
		return calculateSalesTotal(arrSalesLine);
	}
	
	public float calculateSalesTotal (Object [] arrSalesLine)
	{
		float nTotal = 0;
		for (int nIndex = 0; nIndex < arrSalesLine.length; nIndex ++)
			nTotal += getLineItemTotal(arrSalesLine[nIndex]);
		return nTotal;
	}
	
	private float getLineItemTotal(Object oLineItem) 
	{
		float nTotal = 0;
		if (isSalesLineItemClass(oLineItem))
		{
			SalesLineItemData oSalesLineItemData = (SalesLineItemData) oLineItem;
			nTotal = calculateTotalAmount (oSalesLineItemData.getM_nPrice(), oSalesLineItemData.getM_nQuantity(), oSalesLineItemData.getM_nDiscount(), oSalesLineItemData.getM_nTax());
		}
		else
		{
			NonStockSalesLineItemData oNSSalesLineItemData = (NonStockSalesLineItemData) oLineItem;
			nTotal =  calculateTotalAmount (oNSSalesLineItemData.getM_nPrice(), oNSSalesLineItemData.getM_nQuantity(), oNSSalesLineItemData.getM_nDiscount(), oNSSalesLineItemData.getM_nTax());
		}
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
	
	public static float getInvoiceAmount(InvoiceData oInvoiceData) throws Exception 
	{
		float nInvoiceAmount = 0;
		InvoiceData oData = (InvoiceData) GenericIDataProcessor.populateObject(oInvoiceData);
		Iterator<SalesData> oIterator = oData.getM_oSalesSet().iterator();
		while(oIterator.hasNext())
		{
			SalesData oSalesData = (SalesData) GenericIDataProcessor.populateObject(oIterator.next());
			nInvoiceAmount += oSalesData.getSalesTotal();
		}
		return nInvoiceAmount;
	}

	public void updateInvoiceAmount() throws Exception
	{
		InvoiceData oData = (InvoiceData) GenericIDataProcessor.populateObject(m_oInvoiceData);
		oData.setM_nInvoiceAmount(getInvoiceAmount(m_oInvoiceData));
		oData.setM_nBalanceAmount(oData.getM_nInvoiceAmount() - oData.getM_nReceiptAmount());
		oData.updateObject();
	}
	
	@SuppressWarnings("unchecked")
	public static ContactData [] getContactsToPublicise(PubliciseData oPubliciseData, SalesData oSalesData) throws Exception 
	{
		ArrayList<ClientData> arrClientList = buildClientList (oPubliciseData.m_arrClientGroup);
		oSalesData.setM_strFromDate(oPubliciseData.getM_strFromDate());
		oSalesData.setM_strToDate(oPubliciseData.getM_strToDate());
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		return getSales(new ArrayList (oSalesData.list(oOrderBy)), arrClientList);
	}
	
	
	public static ArrayList<ClientData> buildClientList(ClientGroupData[] arrClientGroup) throws Exception 
	{
		ArrayList<ClientData> arrClientList = new ArrayList<ClientData> ();
		for (int nIndex = 0; nIndex < arrClientGroup.length; nIndex ++)
		{
			ClientGroupData oClientGroupData = new ClientGroupData ();
			oClientGroupData.setM_nGroupId(arrClientGroup[nIndex].getM_nGroupId());
			oClientGroupData = (ClientGroupData) GenericIDataProcessor.populateObject(oClientGroupData);
		    arrClientList.addAll(oClientGroupData.getM_oClientSet());
		}
		return arrClientList;
	}
	
	@SuppressWarnings("unchecked")
	private static ContactData [] getSales(ArrayList arrSales, ArrayList<ClientData> arrClientList) 
	{
		ArrayList<ContactData> arrContactList = new ArrayList<ContactData> ();
		for(int nIndex = 0; nIndex < arrSales.size(); nIndex++)
		{
			Object[] oSalesObject = (Object[])arrSales.get(nIndex);
			ClientData oClientdata = (ClientData) oSalesObject[0];
			if(isClientExist (oClientdata, arrClientList))
				buildContacts (arrContactList, oClientdata);
		}
		return arrContactList.toArray(new ContactData[arrContactList.size()]);
	}
	
	public static boolean isClientExist(ClientData oClientdata,ArrayList<ClientData> arrClientList) 
	{
		boolean bIsClientExist = false;
		for(int nIndex = 0; nIndex < arrClientList.size(); nIndex++)
		{
			if(arrClientList.get(nIndex).getM_nClientId() == oClientdata.getM_nClientId())
			{
				bIsClientExist = true;
				break;
			}
		}
		return bIsClientExist;
	}
	
	private static void buildContacts(ArrayList<ContactData> arrContactList, ClientData oClientdata) 
	{
		Iterator<ContactData> oContactsSet = oClientdata.getM_oContacts().iterator();
		while (oContactsSet.hasNext())
		{
			arrContactList.add(oContactsSet.next());
		}
	}
	
	public boolean hasLineItems()
    {
		return m_oSalesLineItems.size () > 0 || m_oNonStockSalesLineItems.size() > 0;
    }

	public void returned (SalesLineItemData oSLIData, float nQuantity) throws Exception // not added
    {
	    m_oPOData.returned (oSLIData, nQuantity);
    }

	public static SalesData getInstance(int nClientId)
	{
		SalesData oSalesData = new SalesData ();
		oSalesData.m_oClientData.setM_nClientId(nClientId);
		return oSalesData;
	}
	
	public void addLineItem (SalesLineItemData oSalesLineItemData) 
	{
		oSalesLineItemData.setM_oSalesData(this);
	    m_oSalesLineItems.add(oSalesLineItemData);
	}
	
	public void customizeSalesLineItems()
	{
		Iterator<SalesLineItemData> oIterator = m_oSalesLineItems.iterator();
		while (oIterator.hasNext()) 
		{
			SalesLineItemData oSalesLineItemData = (SalesLineItemData) oIterator.next();
			oSalesLineItemData.customizeItemData();
		}
 	}

	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root, String strColumn, String strOrderBy)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*
 		if (m_bIsForClientwise)
			buildClientwiseListCriteria (oCriteria, strColumn, strOrderBy);
		else if (isM_bIsForItemGroupClientProfile())
			buildClientwiseListCriteriaForItemGroup (oCriteria, strColumn, strOrderBy);
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

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root) {
		// TODO Auto-generated method stub
		return null;
	}
}
