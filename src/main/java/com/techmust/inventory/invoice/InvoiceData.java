package com.techmust.inventory.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmust.traderp.util.NumberToWords;
import com.techmust.traderp.util.TraderpUtil;
import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.SiteData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataexchange.ChildKey;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.challan.ChallanData;
import com.techmust.inventory.paymentsandreceipt.InvoiceReceiptData;
import com.techmust.inventory.purchaseorder.PurchaseOrderData;
import com.techmust.inventory.sales.NonStockSalesLineItemData;
import com.techmust.inventory.sales.SalesData;
import com.techmust.inventory.sales.SalesLineItemData;
import com.techmust.inventory.serial.SerialNumberData;
import com.techmust.inventory.serial.SerialType;
import com.techmust.inventory.utility.TallyTransformData;
import com.techmust.master.tax.Tax;
import com.techmust.usermanagement.initializer.UserManagementInitializer;
import com.techmust.usermanagement.userinfo.UserInformationData;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "tac11_invoice")
public class InvoiceData extends TenantData
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ac11_id")
    private int m_nInvoiceId;
    @ManyToOne
    @JoinColumn(name = "CL01_client_id")
    @Fetch(FetchMode.SELECT)
    private ClientData m_oClientData;
    @Column(name="ac11_invoice_number")
    private String m_strInvoiceNumber;
    
    @OneToMany(orphanRemoval=true)
    @JoinColumn( name="ac06_id")
    private Set<SalesData> m_oSalesSet;
    @Column(name = "ac11_created_on")
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date m_dCreatedOn;
    @Column(name="ac11_Created_by")
    private int m_nCreatedBy;
    @Column(name="ac11_Remarks")
    private String m_strRemarks;
    @Column(name = "ac11_LRNumber")
    private String m_strLRNumber;
    @Column(name = "ac11_ESugamNumber")
    private String m_strESugamNumber;
    @Column(name = "ac11_receipt_amount")
    @ColumnDefault("0")
    private float m_nReceiptAmount;
    @Column(name="ac11_balance_amount")
    @ColumnDefault("0")
    private float m_nBalanceAmount;
    @Column(name = "ac11_invoice_amount")
    private float m_nInvoiceAmount;
    @Transient
    private String m_strDate;
    @Transient
    private String m_strTime;
    @Transient
    private String m_strFromDate = "";
    @Transient
    private String m_strToDate = "";
    @Transient
    private UserInformationData m_oUserCredentialsData;
    @Transient
    private boolean m_bIsForReceipt;
    @Transient
    private boolean m_bIsForClientOutstanding;
    @Transient
    private String m_strTallyTransformDate;
    @Transient
    private int m_nClientId;
    @Transient
    private int m_nContactId;
    @Transient
    private int m_nSiteId;
    @Transient
    public boolean m_bIsForAgeWise;
    
    public InvoiceData ()
    {
    	m_nInvoiceId = -1;
    	m_strInvoiceNumber = "";
    	m_dCreatedOn = Calendar.getInstance().getTime();
    	m_oSalesSet = new HashSet<SalesData> ();
    	m_strDate = "";
    	m_strTime = "";
    	m_bIsForReceipt = false;
    	setM_bIsForClientOutstanding(false);
    	m_strTallyTransformDate = "";
    	m_nClientId=-1;
    	m_nReceiptAmount = 0;
    	m_nBalanceAmount = 0;
    	m_nInvoiceAmount = 0;
    	m_strRemarks = "";
    	m_strLRNumber = "";
    	m_strESugamNumber = "";
    	m_oClientData = new ClientData ();
    	m_nContactId = -1;
    	m_nSiteId = -1;
    	m_bIsForAgeWise = false;
    }
    
	public void setM_nInvoiceId(int nInvoiceId)
    {
	    this.m_nInvoiceId = nInvoiceId;
    }

	public int getM_nInvoiceId()
    {
	    return m_nInvoiceId;
    }

	public void setM_dCreatedOn(Date dCreatedOn)
    {
	    this.m_dCreatedOn = dCreatedOn;
    }

	public Date getM_dCreatedOn()
    {
	    return m_dCreatedOn;
    }

	public String getM_strInvoiceNumber()
    {
    	return m_strInvoiceNumber;
    }

	public void setM_strInvoiceNumber(String strInvoiceNumber)
    {
    	m_strInvoiceNumber = strInvoiceNumber;
    }

	public void setM_oClientData(ClientData oClientData) 
	{
		m_oClientData = oClientData;
	}

	public ClientData getM_oClientData() 
	{
		return m_oClientData;
	}

	public void setM_nCreatedBy(int nCreatedBy)
    {
	    this.m_nCreatedBy = nCreatedBy;
    }

	public int getM_nCreatedBy()
    {
	    return m_nCreatedBy;
    }

	public Set<SalesData> getM_oSalesSet()
    {
    	return m_oSalesSet;
    }

	public void setM_oSalesSet(Set<SalesData> oSalesSet)
    {
    	m_oSalesSet = oSalesSet;
    }
	
	public void setM_strRemarks(String strRemarks)
	{
		m_strRemarks = strRemarks;
	}

	public String getM_strRemarks() 
	{
		return m_strRemarks;
	}

	public void setM_nInvoiceAmount(float m_nInvoiceAmount) 
	{
		this.m_nInvoiceAmount = m_nInvoiceAmount;
	}

	public float getM_nInvoiceAmount() 
	{
		return m_nInvoiceAmount;
	}

	public void setM_strLRNumber(String strLRNumber) 
	{
		m_strLRNumber = strLRNumber;
	}

	public String getM_strLRNumber() 
	{
		return m_strLRNumber;
	}

	public void setM_strESugamNumber(String strESugamNumber) 
	{
		m_strESugamNumber = strESugamNumber;
	}

	public String getM_strESugamNumber() 
	{
		return m_strESugamNumber;
	}

	public SalesData getSalesData ()
	{
		SalesData oSalesData = null;
		Iterator<SalesData> oIterator = m_oSalesSet.iterator();
		if (oIterator.hasNext())
			oSalesData = oIterator.next();
		return oSalesData;
	}
	
	@Override
	public String generateXML()
	{
		String strInvoiceDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "InvoiceData");
			Document oSalesXmlDoc = getXmlDocument ("<m_oSalesSet>" + buildSalesSetXml () + "</m_oSalesSet>");
			Node oSalesNode = oXmlDocument.importNode (oSalesXmlDoc.getFirstChild (), true);
			oRootElement.appendChild (oSalesNode);
			m_oClientData = (ClientData) GenericIDataProcessor.populateObject(m_oClientData);
			Document oClientDoc = getXmlDocument ("<m_oClientData>" + m_oClientData.generateXML() + "</m_oClientData>");
			Node oClientNode = oXmlDocument.importNode (oClientDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
//			addChild (oXmlDocument, oRootElement, "m_strUserName", getM_oCreatedBy() != null ? getM_oCreatedBy().getM_strUserName() : "");
			addChild (oXmlDocument, oRootElement, "m_nInvoiceId", m_nInvoiceId);
			addChild (oXmlDocument, oRootElement, "m_dCreatedOn", m_dCreatedOn != null ? m_dCreatedOn.toString() : "");
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dCreatedOn));
			addChild (oXmlDocument, oRootElement, "m_strTallyTransformDate", TraderpUtil.getTallyCompatibleFormat(m_dCreatedOn));
			addChild (oXmlDocument, oRootElement, "m_strTime", TraderpUtil.getTime (m_dCreatedOn));
			addChild (oXmlDocument, oRootElement, "m_strInvoiceNumber", m_strInvoiceNumber);
			addChild (oXmlDocument, oRootElement, "m_strRemarks", m_strRemarks);
			addChild (oXmlDocument, oRootElement, "m_strLRNumber", m_strLRNumber);
			addChild (oXmlDocument, oRootElement, "m_strESugamNumber", m_strESugamNumber);
			addChild (oXmlDocument, oRootElement, "m_nReceiptAmount", m_nReceiptAmount);
			addChild (oXmlDocument, oRootElement, "m_nBalanceAmount", m_nBalanceAmount);
			addChild (oXmlDocument, oRootElement, "m_nTotalAmount",  m_nInvoiceAmount);
			addChild (oXmlDocument, oRootElement, "m_nInvoiceAmountInWords",  NumberToWords.convertNumberToWords(new BigDecimal (m_nInvoiceAmount), true, true));
			ArrayList<Tax> arrTaxes = getTaxes();
			Document oTaxItemDoc = getXmlDocument("<m_oTaxes>"+ buildTaxItems (arrTaxes) + "</m_oTaxes>");
			addChild (oXmlDocument, oRootElement, "m_strTaxedAmount", getTotalTaxableAmount (arrTaxes));
			addChild (oXmlDocument, oRootElement, "m_strZeroTaxLedgerName", TallyTransformData.getTallyZeroPercentageKeyValue ("LEDGERNAME-SALESAMOUNT", "VAT"));
			Node oTaxNode = oXmlDocument.importNode (oTaxItemDoc.getFirstChild (), true);
			oRootElement.appendChild (oTaxNode);
			strInvoiceDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strInvoiceDataXml;
	}

	private String getTotalTaxableAmount(ArrayList<Tax> arrTaxes) throws Exception 
	{
		float nTotal = 0;
		for(int nIndex = 0; nIndex < arrTaxes.size(); nIndex++)
			nTotal += (arrTaxes.get(nIndex).getM_nAmount() / arrTaxes.get(nIndex).getM_nPercentage())* 100;
		return String.valueOf(nTotal);
	}
	
	public void setM_strTime(Date oDate) 
	{
		this.m_strTime = TraderpUtil.getTime (oDate);
	}

	public String getM_strTime()
	{
		return m_strTime;
	}
	
	public String getM_strDate()
	{
		return m_strDate;
	}

	public void setM_strDate(String strDate) 
	{
		m_strDate = strDate;
	}
	
	public void buildSalesSet ()
	{
		Iterator<SalesData> oIterator = m_oSalesSet.iterator();
		while (oIterator.hasNext())
		{
			SalesData oSalesData = oIterator.next();
			try
			{
				ClientData oClientData = (ClientData) GenericIDataProcessor.populateObject(oSalesData.getM_oClientData());
				oSalesData.setM_oClientData(oClientData);
				SiteData oSiteData = (SiteData) GenericIDataProcessor.populateObject(oSalesData.getM_oSiteData());
				oSalesData.setM_oSiteData(oSiteData);
			}
			catch (Exception oException)
			{
				
			}
		}
	}

	public InvoiceData create (PurchaseOrderData oPOData) throws Exception
    {
		InvoiceData oInvoiceData = create (oPOData.m_arrChallans, oPOData.getM_nCreatedBy(), oPOData.getM_oClientData());
		oPOData.createSales (oInvoiceData);
		oPOData.invoiced (oInvoiceData);
		oInvoiceData.setM_nInvoiceAmount(Math.round(SalesData.getInvoiceAmount (oInvoiceData)));
		oInvoiceData.setM_nBalanceAmount(oInvoiceData.getM_nInvoiceAmount() - oInvoiceData.getM_nReceiptAmount());
		oInvoiceData.setM_oClientData(oPOData.getM_oClientData());
		oInvoiceData.updateObject();
		return oInvoiceData;
    }

	public InvoiceData create (ChallanData[] arrChallans, int nUserID, ClientData oClientData) throws Exception
    {
		InvoiceData oInvoiceData = new InvoiceData ();
		oInvoiceData.m_nCreatedBy = nUserID;
		oInvoiceData.m_oClientData = oClientData;
		oInvoiceData.m_strInvoiceNumber = generateInvoiceNumber ();
		for (int nIndex = 0; nIndex < arrChallans.length; nIndex++)
		{
			arrChallans [nIndex] = (ChallanData) GenericIDataProcessor.populateObject (arrChallans [nIndex]);
			oInvoiceData.addSales (arrChallans [nIndex].getM_oSalesData());
		}
		oInvoiceData.saveObject();
		updateChallans (arrChallans, oInvoiceData);
	    return oInvoiceData;
    }
	
	protected String generateInvoiceNumber () throws Exception
	{
		return SerialNumberData.generateSerialNumber(SerialType.kInvoice);
	}

	public void updateChallans(ChallanData[] arrChallans,InvoiceData oInvoiceData) throws Exception
    {
		for (int nIndex = 0; nIndex < arrChallans.length; nIndex++)
		{
			arrChallans[nIndex].setM_oInvoiceData(oInvoiceData);
			arrChallans[nIndex].updateObject();
			SalesData oSalesData = (SalesData) GenericIDataProcessor.populateObject(arrChallans[nIndex].getM_oSalesData());
			oSalesData.setM_oInvoiceData(oInvoiceData);
			oSalesData.updateObject();
		}
    }

	public InvoiceData create(SalesData oSalesData, int nCreatedBy) throws Exception
    {
		InvoiceData oInvoiceData = new InvoiceData ();
		oInvoiceData.m_nCreatedBy = nCreatedBy;
		oInvoiceData.m_strInvoiceNumber = SerialNumberData.generateSerialNumber(SerialType.kInvoice);
		oInvoiceData.addSales (oSalesData);
		oInvoiceData.setM_oClientData(oSalesData.getM_oClientData());
		oInvoiceData.saveObject();
		oInvoiceData.setM_nInvoiceAmount(Math.round(SalesData.getInvoiceAmount(oInvoiceData)));
		oInvoiceData.setM_nBalanceAmount(oInvoiceData.getM_nInvoiceAmount() - oInvoiceData.getM_nReceiptAmount());
		oInvoiceData.updateObject();
		oSalesData.setM_strInvoiceNo(oInvoiceData.m_strInvoiceNumber);
		oSalesData.updateObject();
	    return oInvoiceData;    
    }
	
	public void addSales(SalesData oSalesData) throws Exception
    {
		oSalesData.setM_strInvoiceNo(m_strInvoiceNumber);
		oSalesData.setM_oInvoiceData(this);
		m_oSalesSet.add(oSalesData);
    }

	public static InvoiceData getInstance(int nSalesId) throws Exception
    {
	    SalesData oSalesData = new SalesData ();
	    oSalesData.setM_nId(nSalesId);
	    oSalesData = (SalesData) GenericIDataProcessor.populateObject(oSalesData);
	    return getInstance (oSalesData.getM_strInvoiceNo());
    }

	public static InvoiceData getInstance(String strInvoiceNumber) throws Exception
    {
	    InvoiceData oInvoiceData = new InvoiceData ();
	    if (strInvoiceNumber == null || strInvoiceNumber.isEmpty())
	    	throw new Exception ("InvoiceData.getInstance - Unique Invoice not found : " + strInvoiceNumber);
	    	
	    oInvoiceData.setM_strInvoiceNumber(strInvoiceNumber);
	    HashMap<String, String> oOrderBy = new HashMap<String, String> ();
	    ArrayList<GenericData> arrInvoices = oInvoiceData.list(oOrderBy);
	    if (arrInvoices.size() != 1)
	    	throw new Exception ("InvoiceData.getInstance - Unique Invoice not found : " + strInvoiceNumber);
	    else
	    	oInvoiceData = (InvoiceData) arrInvoices.get(0);
	    return oInvoiceData;
    }

	public boolean contains (SalesData oSalesData)
    {
		boolean bContains = false;
		Iterator<SalesData> oSalesList = m_oSalesSet.iterator();
		while (oSalesList.hasNext() && !bContains)
			bContains = oSalesList.next().getM_nId() == oSalesData.getM_nId();
		return bContains;
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

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData)
    {
	    this.m_oUserCredentialsData = oUserCredentialsData;
    }

	public UserInformationData getM_oUserCredentialsData()
    {
	    return m_oUserCredentialsData;
    }
	
	public void setM_nReceiptAmount(float nReceiptAmount) 
	{
		this.m_nReceiptAmount = nReceiptAmount;
	}

	public float getM_nReceiptAmount() 
	{
		return m_nReceiptAmount;
	}

	public void setM_nBalanceAmount(float nBalanceAmount) 
	{
		this.m_nBalanceAmount = nBalanceAmount;
	}

	public float getM_nBalanceAmount() 
	{
		return m_nBalanceAmount;
	}

	public void setM_strTallyTransformDate(String strTallyTransformDate) 
	{
		this.m_strTallyTransformDate = strTallyTransformDate;
	}

	public String getM_strTallyTransformDate() 
	{
		return m_strTallyTransformDate;
	}

	public int getM_nClientId () 
	{
		return m_nClientId;
	}

	public void setM_nClientId (int nClientId) 
	{
		m_nClientId = nClientId;
	}

	public int getM_nContactId () 
	{
		return m_nContactId;
	}

	public void setM_nContactId (int nContactId) 
	{
		m_nContactId = nContactId;
	}

	public int getM_nSiteId () 
	{
		return m_nSiteId;
	}

	public void setM_nSiteId (int nSiteId) 
	{
		m_nSiteId = nSiteId;
	}

	public String generateTallyDataXML(ArrayList<InvoiceData> arrInvoice) 
	{
		String strTallyDataXml = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Document oInvoiceXmlDoc = getXmlDocument ("<ReportInvoiceData>" + buildInvoiceXml (arrInvoice) + "</ReportInvoiceData>");
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
	
	public void addSales(ArrayList<InvoiceData> arrInvoice) throws Exception 
    {
		for(int nIndex = 0; nIndex < arrInvoice.size(); nIndex++)
			addSalesSet(arrInvoice.get(nIndex).m_oSalesSet);
    }
	
	public float getInvoiceAmount() 
	{
		float nTotal = 0;
		Iterator<SalesData> oIterator = m_oSalesSet.iterator();
		while (oIterator.hasNext())
			nTotal += oIterator.next().getSalesTotal ();
		return Math.round(nTotal);
	}
	
	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if (m_bIsForAgeWise)
			buildAgeWiseInvoiceList (oCriteria, strColumn, strOrderBy);
		else if (m_bIsForClientOutstanding)
			buildClientOutstandingListCriteria (oCriteria, strColumn, strOrderBy);
		else if (m_nContactId > 0 || m_nSiteId > 0)
			buildContactSiteInvoiceListCriteria (oCriteria, strColumn, strOrderBy);
		else
			buildListCriteria (oCriteria, strColumn, strOrderBy);
		return oCriteria;
	}
	
	private void buildAgeWiseInvoiceList(Criteria oCriteria, String strColumn,String strOrderBy) 
	{
		if (m_nContactId > 0 || m_nSiteId > 0)
			buildContactSiteInvoiceListCriteria (oCriteria, strColumn, strOrderBy);
		buildDateAndClientCriteria (oCriteria, strColumn, strOrderBy);
		oCriteria.add (Restrictions.gt ("m_nBalanceAmount", (float)0.0));;
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.rowCount());
		oProjectionList.add(Projections.sum("m_nBalanceAmount"));
		oCriteria.setProjection(oProjectionList);
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_dCreatedOn");
	}
	
	private void buildClientOutstandingListCriteria(Criteria oCriteria,	String strColumn, String strOrderBy) 
	{
		buildDateAndClientCriteria (oCriteria, strColumn, strOrderBy);
		ProjectionList oProjectionList = Projections.projectionList();
		oProjectionList.add(Projections.groupProperty("m_oClientData"));
		oProjectionList.add(Projections.sum("m_nInvoiceAmount"));
		oProjectionList.add(Projections.sum("m_nReceiptAmount"));
		oProjectionList.add(Projections.sum("m_nBalanceAmount"), "nBalanceAmount");
		oCriteria.setProjection(oProjectionList);
		oCriteria.addOrder(Order.desc("nBalanceAmount"));
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_oClientData");
	}
	
	private void buildDateAndClientCriteria (Criteria oCriteria, String strColumn, String strOrderBy)
	{
		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oCriteria.add (Restrictions.eq ("m_oClientData", m_oClientData));
		if(m_oClientData.getM_strCompanyName() != null && !m_oClientData.getM_strCompanyName().trim().isEmpty())
			oClientDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
	}

	private void buildListCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
		m_oLogger.info("listCriteria:");
		if (m_nInvoiceId > 0)
			oCriteria.add (Restrictions.eq ("m_nInvoiceId", m_nInvoiceId));
		if (m_strInvoiceNumber != null && !m_strInvoiceNumber.isEmpty())
			oCriteria.add (Restrictions.eq ("m_strInvoiceNumber", m_strInvoiceNumber));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oCriteria.add (Restrictions.eq ("m_oClientData", m_oClientData));
		if(m_oClientData.getM_strCompanyName() != null && !m_oClientData.getM_strCompanyName().trim().isEmpty())
			oClientDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if (!m_strRemarks.trim().isEmpty ())
		{
			oCriteria.add(Restrictions.disjunction().add(Restrictions.ilike("m_strRemarks", m_strRemarks.trim(), MatchMode.ANYWHERE))
				.add(Restrictions.ilike("m_strLRNumber", m_strRemarks.trim(), MatchMode.ANYWHERE))
				.add(Restrictions.ilike("m_strESugamNumber", m_strRemarks.trim(), MatchMode.ANYWHERE)));
		}
		if (m_nCreatedBy  > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if(m_bIsForReceipt)
			oCriteria.add(Restrictions.disjunction().add(Restrictions.eq("m_nReceiptAmount", (float)0.0)).add(Restrictions.gt("m_nBalanceAmount", (float)0.0)));
		if (strColumn.contains("m_strCompanyName"))
			oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nInvoiceId");
	}
	
	private void buildContactSiteInvoiceListCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oSalesSetCriteria = oCriteria.createCriteria ("m_oSalesSet");
		if(m_nContactId > 0)
		{
			Criteria oContactCriteria = oSalesSetCriteria.createCriteria("m_oContactData");
			oContactCriteria.add (Restrictions.eq ("m_nContactId", m_nContactId));
		}
		if(m_nSiteId > 0)
		{
			Criteria oSiteCriteria = oSalesSetCriteria.createCriteria("m_oSiteData");
			oSiteCriteria.add (Restrictions.eq ("m_nSiteId", m_nSiteId));
		}
	}
	
	private String buildSalesSetXml()
    {
		m_oLogger.debug("buildSalesSetXml");
		String strXml = "<nLineItemCount>" +getLineItemCount (m_oSalesSet)+ "</nLineItemCount>";
		Iterator<SalesData> oIterator = m_oSalesSet.iterator();
		while (oIterator.hasNext())
			strXml += oIterator.next().generateXML();
		strXml += "<grandTotal>" + getGrandTotal ("<Sales>" + strXml + "</Sales>") +  "</grandTotal>";
	    return strXml;
    }
	
	private double getGrandTotal(String strSalesXml) 
	{
		double nGrandTotal = 0;
		Document oLineItemDoc = getXmlDocument(strSalesXml);
		NodeList oLineItems = oLineItemDoc.getElementsByTagName("m_oSalesLineItems");
		for(int nIndex = 0; nIndex < oLineItems.getLength(); nIndex ++)
		{
			Node oSubTotalItem = getChildNodeByName(oLineItems.item(nIndex), "subTotal");
			Node oDiscountsItem = getChildNodeByName(oLineItems.item(nIndex), "discounts");
			Node oTaxesItem = getChildNodeByName(oLineItems.item(nIndex), "taxes");
			float nSubTotal = TraderpUtil.getFloatValue(oSubTotalItem.getFirstChild().getNodeValue());
			float nTotalDiscount = TraderpUtil.getFloatValue(getTagValue (oDiscountsItem, "totalDiscount"));
			float nTotalTaxAmount = TraderpUtil.getFloatValue(getTagValue (oTaxesItem, "totalTaxAmount"));
			nGrandTotal += ((double)nSubTotal - (double)nTotalDiscount) + (double)nTotalTaxAmount;
		}
		return nGrandTotal;
	}

	private int getLineItemCount(Set<SalesData> oSales)
    {
		int nCount = 0;
		Iterator<SalesData> oIterator = m_oSalesSet.iterator();
		while (oIterator.hasNext())
		{
			SalesData oSalesData = oIterator.next();
			nCount += oSalesData.getM_oSalesLineItems().size() + oSalesData.getM_oNonStockSalesLineItems().size();
		}
	    return nCount;
    }

	private String buildTaxItems(ArrayList<Tax> arrTaxes) throws Exception 
	{
		String strXML = "";
		float nTotalTaxAmount = 0;
		for(int nIndex = 0; nIndex < arrTaxes.size(); nIndex++)
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement (oXmlDocument, "Tax"); 
			addChild (oXmlDocument, oRootElement, "m_strTaxName", isOutOfStateClient()? "CST" : arrTaxes.get(nIndex).getM_strTaxName());
			addChild (oXmlDocument, oRootElement, "m_nTaxPercent", arrTaxes.get(nIndex).getM_nPercentage());
			addChild (oXmlDocument, oRootElement, "m_nTaxAmount", arrTaxes.get(nIndex).getM_nAmount());
			nTotalTaxAmount += arrTaxes.get(nIndex).getM_nAmount();
			addChild (oXmlDocument, oRootElement, "m_strOutputTaxClassificationName", TallyTransformData.getTallyTransformKeyValue ("TAXCLASSIFICATIONNAME-OUTPUT", arrTaxes.get(nIndex).getM_strTaxName(), arrTaxes.get(nIndex).getM_nPercentage()));
			addChild (oXmlDocument, oRootElement, "m_strSalesAmountLedgerName", TallyTransformData.getTallyTransformKeyValue ("LEDGERNAME-SALESAMOUNT", arrTaxes.get(nIndex).getM_strTaxName(), arrTaxes.get(nIndex).getM_nPercentage()));
			addChild (oXmlDocument, oRootElement, "m_strOutputTaxAmountLedgerName", TallyTransformData.getTallyTransformKeyValue ("LEDGERNAME-OUPUTTAXAMOUNT", arrTaxes.get(nIndex).getM_strTaxName(), arrTaxes.get(nIndex).getM_nPercentage()));
			strXML += getXmlString(oXmlDocument);
		}
		strXML += "<totalTaxAmountInWords>" + NumberToWords.convertNumberToWords(new BigDecimal (nTotalTaxAmount), true, true) + "</totalTaxAmountInWords>";
		return strXML;
	}
	
	private boolean isOutOfStateClient() 
	{
		boolean bIsOutOfStateClient= false;
		Iterator<SalesData> oIterator = m_oSalesSet.iterator();
		if(oIterator.hasNext())
		{
			try 
			{
				SalesData oSalesData = (SalesData) GenericIDataProcessor.populateObject(oIterator.next());
				bIsOutOfStateClient = oSalesData.getM_oClientData().isM_bOutstationClient();
			} 
			catch (Exception oException) 
			{
			}
		}
		return bIsOutOfStateClient;
	}

	private void addSalesSet(Set<SalesData> oSalesSet) throws Exception 
	{
		Iterator<SalesData> oIterator = oSalesSet.iterator();
		while(oIterator.hasNext())
		{
			SalesData oData = (SalesData) GenericIDataProcessor.populateObject(oIterator.next());
			oData.setM_oInvoiceData(this);
			oData.updateObject();
			m_oSalesSet.add(oData);
		}
	}

	private ArrayList<Tax> getTaxes() 
	{
		ArrayList<Tax> arrTax = new ArrayList<Tax>();
		Iterator<SalesData> oIterator = m_oSalesSet.iterator();
		while(oIterator.hasNext())
		{
			SalesData oSalesData = oIterator.next();
			buildSalesLineItemTaxes(arrTax, oSalesData.getM_oSalesLineItems());
			buildNSSalesLineItemTaxes(arrTax, oSalesData.getM_oNonStockSalesLineItems());
		}
		return arrTax;
	}
	
	private void buildSalesLineItemTaxes(ArrayList<Tax> arrTax, Set<SalesLineItemData> arrSalesLineItems) 
	{
		Iterator<SalesLineItemData> oIterator = arrSalesLineItems.iterator();
		while(oIterator.hasNext())
		{
			SalesLineItemData oSalesLineItemData = oIterator.next();
			addTax(arrTax, oSalesLineItemData);
		}
	}
	
	private void buildNSSalesLineItemTaxes(ArrayList<Tax> arrTax, Set<NonStockSalesLineItemData> arrNSSalesLineItems) 
	{
		Iterator<NonStockSalesLineItemData> oIterator = arrNSSalesLineItems.iterator();
		while(oIterator.hasNext())
		{
			NonStockSalesLineItemData oNSSalesLineItemData = oIterator.next();
			addTax(arrTax, oNSSalesLineItemData);
		}
	}

	private void addTax(ArrayList<Tax> arrTax,  NonStockSalesLineItemData oNSSalesLineItemData)
    {
		m_oLogger.info("addTax");
		Tax oLineItemTax = new Tax ();
		oLineItemTax.setM_strTaxName(oNSSalesLineItemData.getM_strTaxName());
		oLineItemTax.setM_nAmount(getTaxAmount(oNSSalesLineItemData));
		oLineItemTax.setM_nPercentage(oNSSalesLineItemData.getM_nTax());
		if(oLineItemTax.getM_nAmount() > 0 && isNewTaxData(oLineItemTax.getM_strTaxName(), oNSSalesLineItemData.getM_nTax(), arrTax))
			arrTax.add(oLineItemTax);
		else
			updateToTaxArray(arrTax, oLineItemTax);
    }
	
	private void addTax(ArrayList<Tax> arrTax, SalesLineItemData oSalesLineItemData)
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

	private float getTaxAmount(SalesLineItemData oSalesLineItemData) 
	{
		return (oSalesLineItemData.getM_nQuantity()*(oSalesLineItemData.getM_nPrice() - (oSalesLineItemData.getM_nPrice() * (oSalesLineItemData.getM_nDiscount()/100)))) * oSalesLineItemData.getM_nTax()/100 ;
	}

	private float getTaxAmount(NonStockSalesLineItemData oNSSalesLineItemData)
    {
		return (oNSSalesLineItemData.getM_nQuantity()*(oNSSalesLineItemData.getM_nPrice() - (oNSSalesLineItemData.getM_nPrice() * (oNSSalesLineItemData.getM_nDiscount()/100)))) * oNSSalesLineItemData.getM_nTax()/100 ;
    }

	private String buildInvoiceXml(ArrayList<InvoiceData> arrInvoice) 
	{
		m_oLogger.debug("buildInvoiceXml");
		String strXml = "";
		for(int nIndex = 0; nIndex < arrInvoice.size(); nIndex++)
		{
			strXml += arrInvoice.get(nIndex).generateXML();
		}
	    return strXml;
	}

	public void setM_bIsForClientOutstanding(boolean bIsForClientOutstanding) 
	{
		this.m_bIsForClientOutstanding = bIsForClientOutstanding;
	}

	public boolean isM_bIsForClientOutstanding() 
	{
		return m_bIsForClientOutstanding;
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) throws Exception 
	{
		InvoiceData oInvoiceData = new InvoiceData ();
		strXML = strXML.replaceAll(">\\s+<", "><").trim();
		Document oXMLDocument = getXmlDocument(strXML);
		NodeList oChildNodes = oXMLDocument.getChildNodes();
		for (int nIndex = 0; nIndex < oChildNodes.getLength(); nIndex++)
	    {
			Node oChildNode = oChildNodes.item(nIndex);
			oInvoiceData.setM_nCreatedBy(oCredentials.getM_nUserId());
			oInvoiceData = buildInvoiceData(oChildNode, oInvoiceData);
			buildClientData (oChildNode, oInvoiceData);
			buildSales (oChildNode, oInvoiceData);
	    }
		InvoiceData oData = new InvoiceData ();
		oData.setM_strInvoiceNumber(oInvoiceData.getM_strInvoiceNumber());
		if(isInvoiceExists(oData))
			throw new Exception ("Duplicate");
		return oInvoiceData;
	}

	private void buildClientData(Node oChildNode, InvoiceData oInvoiceData) throws Exception 
	{
		NodeList oNodeList = oChildNode.getChildNodes();
		for(int nIndex=0; nIndex < oNodeList.getLength(); nIndex++)
		{
			String strNodeName = oNodeList.item(nIndex).getNodeName();
			if(strNodeName.equalsIgnoreCase("m_oClientData"))
			{
				if(oNodeList.item(nIndex).getChildNodes().getLength()>0)
					setClientData (oNodeList.item(nIndex), oInvoiceData);
				else
					throw new Exception  ("ClientData does not exist for Invoice Number:" + oInvoiceData.getM_strInvoiceNumber());
			}
		}
	}

	private void setClientData(Node oNode,InvoiceData oInvoiceData) throws Exception
	{
		NodeList oChildNodes = oNode.getChildNodes();
		for(int nIndex=0; nIndex < oChildNodes.getLength(); nIndex++)
		{
			String strNodeName = oChildNodes.item(nIndex).getNodeName();
			if(strNodeName.equalsIgnoreCase("ClientData"))
				setClientId(oChildNodes.item(nIndex), oInvoiceData);
		}
	}

	private void setClientId(Node oNode, InvoiceData oInvoiceData) throws Exception 
	{
		NodeList oChildNodes = oNode.getChildNodes();
		for(int nIndex=0; nIndex < oChildNodes.getLength(); nIndex++)
		{
			String strNodeName = oChildNodes.item(nIndex).getNodeName();
			if(strNodeName.equalsIgnoreCase("m_nClientId"))
			{
				int nClientId = Integer.parseInt(UserManagementInitializer.getValue(oNode, "m_nClientId"));
				if(isClientExist (nClientId))
					oInvoiceData.getM_oClientData().setM_nClientId(nClientId);
				else 
					throw new Exception ("Client Id " + nClientId + " does not exist for Invoice Number:" + oInvoiceData.getM_strInvoiceNumber());
			}
		}
	}

	private boolean isClientExist(int nClientId) throws Exception 
	{
		boolean bIsClientExist = false;
		ClientData oClientData = new ClientData ();
		oClientData.setM_nClientId(nClientId);
		oClientData = (ClientData) GenericIDataProcessor.populateObject(oClientData);
		bIsClientExist = (oClientData != null);
		return bIsClientExist;
	}

	private boolean isInvoiceExists(InvoiceData oInvoiceData) 
	{
		 boolean bIsInvoiceExists = false;
		 m_oLogger.info("isInvoiceExists");
		 try
		 {
			 InvoiceDataProcessor oDataProcessor = new InvoiceDataProcessor ();
			 HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			 InvoiceDataResponse oResponse =(InvoiceDataResponse) oDataProcessor.list(oInvoiceData, oOrderBy,0,0);  //new
			 bIsInvoiceExists = (oResponse.m_arrInvoice.size() > 0);
		 }
		 catch (Exception oException)
		 {
			 m_oLogger.error("isInvoiceExists - oException : " + oException);
		 }
		 m_oLogger.debug("isInvoiceExists - bIsInvoiceExists [OUT] : " + bIsInvoiceExists);
		 return bIsInvoiceExists;
	}

	private InvoiceData buildInvoiceData(Node oChildNode, InvoiceData oInvoiceData) throws Exception 
	{
		oInvoiceData.setM_strInvoiceNumber(UserManagementInitializer.getValue(oChildNode, "m_strInvoiceNumber"));
		oInvoiceData.setM_strRemarks(UserManagementInitializer.getValue(oChildNode, "m_strRemarks"));
		oInvoiceData.setM_strLRNumber(UserManagementInitializer.getValue(oChildNode, "m_strLRNumber"));
		oInvoiceData.setM_strESugamNumber(UserManagementInitializer.getValue(oChildNode, "m_strESugamNumber"));
		oInvoiceData.setM_nReceiptAmount(Float.parseFloat(UserManagementInitializer.getValue(oChildNode, "m_nReceiptAmount")));
		oInvoiceData.setM_nBalanceAmount(Float.parseFloat(UserManagementInitializer.getValue(oChildNode, "m_nBalanceAmount")));
		oInvoiceData.setM_nInvoiceAmount(Float.parseFloat(UserManagementInitializer.getValue(oChildNode, "m_nTotalAmount")));
		return oInvoiceData;
	}

	private void buildSales(Node oInvoiceNode, InvoiceData oInvoiceData) throws Exception 
	{
		NodeList oChildNodeList = oInvoiceNode.getChildNodes();
		for(int nIndex = 0; nIndex < ((NodeList) oChildNodeList).getLength(); nIndex++)
		{
			if(oChildNodeList.item(nIndex).getNodeName().equalsIgnoreCase("m_oSalesSet"))
			{
				NodeList oNodeList = oChildNodeList.item(nIndex).getChildNodes();
				if(oNodeList.getLength()>0)
				{
					buildSalesSet(oNodeList, oInvoiceData);
					break;
				}
				else
					throw new Exception ("SalesData  does not Exist for Invoice Number :" + oInvoiceData.getM_strInvoiceNumber());
			}
		}
	}

	private void buildSalesSet(NodeList oNodeList, InvoiceData oInvoiceData) throws Exception
	{
		for(int nIndex = 0; nIndex < ((NodeList) oNodeList).getLength(); nIndex++)
			if(oNodeList.item(nIndex).getNodeName().equalsIgnoreCase("SalesData"))
				setSalesData (oNodeList.item(nIndex), oInvoiceData);
	}

	private void setSalesData(Node oNodeItem, InvoiceData oInvoiceData) throws Exception
	{
		NodeList oChildNodes = oNodeItem.getChildNodes();
		for(int nIndex=0; nIndex < oChildNodes.getLength(); nIndex++)
		{
			String strNodeName = oChildNodes.item(nIndex).getNodeName();
			if(strNodeName.equalsIgnoreCase("m_nId"))
			{
				int nSalesId = Integer.parseInt(UserManagementInitializer.getValue(oNodeItem, "m_nId"));
				if(!isSalesExist (nSalesId, oInvoiceData)  )
					throw new Exception ("Sales Id " + nSalesId +" does not Exist for Invoice Number :" + oInvoiceData.getM_strInvoiceNumber());
			}
				
		}
	}

	private boolean isSalesExist(int nSalesId, InvoiceData oInvoiceData) throws Exception
	{
		boolean bIsSalesExist = false;
		SalesData oSalesData = new SalesData ();
		oSalesData.setM_nId(nSalesId);
		oSalesData = (SalesData) GenericIDataProcessor.populateObject(oSalesData);
		if( oSalesData != null)
		{
			bIsSalesExist = true;
			oInvoiceData.addSales(oSalesData);
		}
		return bIsSalesExist;
	}
	
	
	public void removeReceiptAmount(InvoiceReceiptData oInvoiceReceiptData) throws Exception 
	{
		InvoiceData oInvoiceData = (InvoiceData) GenericIDataProcessor.populateObject(this);
		setM_nReceiptAmount(oInvoiceData.getM_nReceiptAmount() - oInvoiceReceiptData.getM_nAmount());
		setM_nBalanceAmount(oInvoiceData.getM_nInvoiceAmount() - m_nReceiptAmount);
		this.updateObject();
	}
	
	@SuppressWarnings("unchecked")
	public HashMap getHeaderKeys() 
	{
		HashMap oHeaderKey = new LinkedHashMap ();
		oHeaderKey.put("Invoice Id", "m_nInvoiceId");
		oHeaderKey.put("Invoice Number", "m_strInvoiceNumber");
		oHeaderKey.put("Remarks", "m_strRemarks");
		oHeaderKey.put("LRNumber", "m_strLRNumber");
		oHeaderKey.put("ESugamNumber", "m_strESugamNumber");
		oHeaderKey.put("Receipt Amount", "m_nReceiptAmount");
		oHeaderKey.put("Balance Amount", "m_nBalanceAmount");
		oHeaderKey.put("Total Amount", "m_nTotalAmount");
		return oHeaderKey;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ChildKey> getChildKeys ()
	{
		ArrayList<ChildKey> arrChildKeys = new ArrayList<ChildKey>();
		ChildKey oSalesKey = new ChildKey ();
		oSalesKey.m_oChildKey.put("Sales" ,"m_oSalesSet");
		oSalesKey.m_oChildKey.put("Sales Data" ,"SalesData");
		oSalesKey.m_oChildKey.put("Sales Id" ," m_nId");
		ChildKey oClientKey = new ChildKey ();
		oClientKey.m_oChildKey.put("ClientData", "m_oClientData");
		oClientKey.m_oChildKey.put("Clients", "ClientData");
		oClientKey.m_oChildKey.put("Client Id", "m_nClientId");
		arrChildKeys.add(oSalesKey);
		arrChildKeys.add(oClientKey);
		return arrChildKeys;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		/*
			 if (m_bIsForAgeWise)
				buildAgeWiseInvoiceList (oCriteria, strColumn, strOrderBy);
			else if (m_bIsForClientOutstanding)
				buildClientOutstandingListCriteria (oCriteria, strColumn, strOrderBy);
			else if (m_nContactId > 0 || m_nSiteId > 0)
				buildContactSiteInvoiceListCriteria (oCriteria, strColumn, strOrderBy);
			else
				buildListCriteria (oCriteria, strColumn, strOrderBy);
		 */
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nInvoiceId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nInvoiceId"), m_nInvoiceId));
		return oConjunct;
	}
}
