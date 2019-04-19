package com.techmust.inventory.paymentsandreceipt;

import java.math.BigDecimal;
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

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.traderp.util.NumberToWords;
import com.techmust.traderp.util.TraderpUtil;
import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@Entity
@Table(name ="tac29_receipt")
public class ReceiptData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac29_id")
	private int m_nReceiptId;
	@ManyToOne
	@JoinColumn(name = "ac29_Client_id")
	private ClientData m_oClientData;
	@JsonManagedReference
	@OneToMany
	@JoinColumn(name ="ac32_receipt_id")
	private Set<InvoiceReceiptData> m_oInvoiceReceipts;
	@Column(name ="ac29_amount")
	private float m_nAmount;
	@ManyToOne
	@JoinColumn(name = "ac29_mode_id")
	private TransactionMode m_oMode;
	@Column(name ="ac29_receiptNumber")
	private String m_strReceiptNumber;
	@Column(name ="ac29_details")
	private String m_strDetails;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac29_created_on")
    private Date m_dCreatedOn;
	@Column(name = "ac29_Created_by")
    private int m_nCreatedBy;
	@Transient
    public InvoiceReceiptData [] m_arrInvoiceReceipt;
	@Transient
    private String m_strDate; 
	@Transient
    private UserInformationData m_oUserCredentialsData;
	@Transient
    private String m_strFromDate;
	@Transient
    private String m_strToDate;
	@Transient
    private String m_strTallyTransformDate;
    
    //client side data
	@Transient
    public int m_nContactId;
	@Transient
    public int m_nSiteId;
    
	public ReceiptData() 
	{
		m_nReceiptId = -1;
		m_oInvoiceReceipts = new HashSet<InvoiceReceiptData> ();
		m_nAmount = 0;
		m_strDetails = "";
		m_strDate = "";
		m_strFromDate = "";
		m_strToDate = "";
		m_strTallyTransformDate = "";
		setM_dCreatedOn(Calendar.getInstance().getTime());
		m_nContactId = -1;
		m_nSiteId = -1;
	}
	
	public void setM_nReceiptId(int nReceiptId) 
	{
		this.m_nReceiptId = nReceiptId;
	}

	public int getM_nReceiptId() 
	{
		return m_nReceiptId;
	}

	public void setM_oClientData(ClientData oClientData) 
	{
		this.m_oClientData = oClientData;
	}

	public ClientData getM_oClientData() 
	{
		return m_oClientData;
	}

	public void setM_oInvoiceReceipts(Set<InvoiceReceiptData> oInvoiceReceipts)
	{
		this.m_oInvoiceReceipts = oInvoiceReceipts;
	}

	public Set<InvoiceReceiptData> getM_oInvoiceReceipts() 
	{
		return m_oInvoiceReceipts;
	}

	public void setM_nAmount(float nAmount) 
	{
		this.m_nAmount = nAmount;
	}

	public float getM_nAmount()
	{
		return m_nAmount;
	}

	public void setM_oMode(TransactionMode oMode) 
	{
		this.m_oMode = oMode;
	}

	public TransactionMode getM_oMode() 
	{
		return m_oMode;
	}

	public void setM_strReceiptNumber(String strReceiptNumber) 
	{
		m_strReceiptNumber = strReceiptNumber;
	}

	public String getM_strReceiptNumber() 
	{
		return m_strReceiptNumber;
	}

	public void setM_strDetails(String strDetails) 
	{
		this.m_strDetails = strDetails;
	}

	public String getM_strDetails() 
	{
		return m_strDetails;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		this.m_dCreatedOn = dCreatedOn;
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

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public void setM_strDate(String strDate) 
	{
		m_strDate = strDate;
	}

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		m_oUserCredentialsData = oUserCredentialsData;
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

	public void setM_strTallyTransformDate(String strTallyTransformDate) 
	{
		this.m_strTallyTransformDate = strTallyTransformDate;
	}

	public String getM_strTallyTransformDate()
	{
		return m_strTallyTransformDate;
	}

	public int getM_nContactId() 
	{
		return m_nContactId;
	}

	public void setM_nContactId (int nContactId) 
	{
		m_nContactId = nContactId;
	}

	public int getM_nSiteId() 
	{
		return m_nSiteId;
	}

	public void setM_nSiteId(int nSiteId) 
	{
		m_nSiteId = nSiteId;
	}

	@Override
	public String generateXML() 
	{
		String strReceiptDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "ReceiptData");
 			addChild (oXmlDocument, oRootElement, "m_nReceiptId", m_nReceiptId);
			addChild (oXmlDocument, oRootElement, "m_dCreatedOn", m_dCreatedOn != null ? m_dCreatedOn.toString() : "");
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(getM_dCreatedOn()));
			addChild (oXmlDocument, oRootElement, "m_nAmount", TraderpUtil.formatNumber(m_nAmount));
			addChild (oXmlDocument, oRootElement, "m_strReceiptAmountInWords", NumberToWords.convertNumberToWords(new BigDecimal (m_nAmount), true, true));
			addChild (oXmlDocument, oRootElement, "m_strTallyTransformDate", TraderpUtil.getTallyCompatibleFormat(m_dCreatedOn));
			addChild (oXmlDocument, oRootElement, "m_strDetails", m_strDetails);
			addChild (oXmlDocument, oRootElement, "m_strReceiptNumber", m_strReceiptNumber);
			Document oClientDoc = getXmlDocument (m_oClientData.generateXML());
			Node oClientNode = oXmlDocument.importNode (oClientDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
			Document oModeDoc = getXmlDocument (m_oMode.generateXML());
			Node oModeNode = oXmlDocument.importNode (oModeDoc.getFirstChild (), true);
			oRootElement.appendChild (oModeNode);
			Document oInvoiceReceiptsSetDoc = getXmlDocument ("<m_oInvoiceReceiptsSet>" + buildInvoiceReceiptsSetXml () + "</m_oInvoiceReceiptsSet>");
			Node oInvoiceReceiptsSetNode = oXmlDocument.importNode (oInvoiceReceiptsSetDoc.getFirstChild (), true);
			oRootElement.appendChild (oInvoiceReceiptsSetNode);
			strReceiptDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strReceiptDataXml;
	}

	public void prepareForSaveOrUpdate() throws Exception 
	{
		for (int nIndex = 0; nIndex < m_arrInvoiceReceipt.length; nIndex++)
		{
			InvoiceReceiptData oInvoiceReceiptData = m_arrInvoiceReceipt[nIndex];
			oInvoiceReceiptData.setM_oReceiptData(this);
			oInvoiceReceiptData.setM_nInvoiceReceiptId(oInvoiceReceiptData.getInvoiceReceiptId(oInvoiceReceiptData.getM_oInvoiceData().getM_nInvoiceId(), m_nReceiptId));
			m_oInvoiceReceipts.add(oInvoiceReceiptData);
		}
	}

	public void updateReceiptInvoices(ReceiptData oReceiptData)
	{
		Iterator<InvoiceReceiptData> oIterator = m_oInvoiceReceipts.iterator();
		while (oIterator.hasNext())
		{
			InvoiceReceiptData oInvoiceReceiptData = oIterator.next();
			try
			{
				InvoiceData oInvoiceData = (InvoiceData) GenericIDataProcessor.populateObject(oInvoiceReceiptData.getM_oInvoiceData());
				float nReceiptAmount = getReceiptAmount(oInvoiceData);
				float nInvoiceAmount = oInvoiceData.getInvoiceAmount();;
				oInvoiceData.setM_nReceiptAmount(nReceiptAmount);
				oInvoiceData.setM_nBalanceAmount((nInvoiceAmount - nReceiptAmount));
				oInvoiceData.updateObject();
			}
			catch (Exception oException)
			{
				
			}
		}
	}
	
	public String generateTallyDataXML(ArrayList<ReceiptData> arrReceiptData) 
	{
		String strTallyDataXml = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Document oInvoiceXmlDoc = getXmlDocument ("<ReportReceiptData>" + buildReceiptXml (arrReceiptData) + "</ReportReceiptData>");
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

	@SuppressWarnings("unchecked")
	public float getClientReceiptsAmount(int nClientId) 
	{
		float nReceivedAmt = 0;
		ReceiptData oReceiptData = new ReceiptData ();
		ClientData oClientData = new ClientData ();
		oClientData.setM_nClientId(nClientId);
		oReceiptData.setM_oClientData(oClientData);
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ArrayList<ReceiptData> arrReceipts =  new ArrayList (oReceiptData.list (oOrderBy));
			for (int nIndex = 0; nIndex < arrReceipts.size(); nIndex++) 
				nReceivedAmt += arrReceipts.get(nIndex).getM_nAmount();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getClientReceiptsAmount - oException" + oException);
		}
		return nReceivedAmt;
	}
	
	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if(m_nContactId > 0 || m_nSiteId > 0)
			buildContactSiteReceiptListCriteria (oCriteria, strColumn, strOrderBy);
		else
			buildListCriteria  (oCriteria, strColumn, strOrderBy);
		return oCriteria;
	}

	private void buildContactSiteReceiptListCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oInvoiceReceiptSetCriteria = oCriteria.createCriteria("m_oInvoiceReceipts");
		Criteria oInvoicecriteria = oInvoiceReceiptSetCriteria.createCriteria("m_oInvoiceData");
		Criteria oSalesSetCriteria = oInvoicecriteria.createCriteria("m_oSalesSet");
		if(m_nSiteId > 0)
		{
			Criteria oSiteCriteria = oSalesSetCriteria.createCriteria("m_oSiteData");
			oSiteCriteria.add (Restrictions.eq ("m_nSiteId", m_nSiteId));
		}
		if(m_nContactId > 0)
		{
			Criteria oContactCriteria = oSalesSetCriteria.createCriteria("m_oContactData");
			oContactCriteria.add (Restrictions.eq ("m_nContactId", m_nContactId));
		}
	}

	private void buildListCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oClientCriteria = oCriteria.createCriteria("m_oClientData");
		if (m_nReceiptId > 0)
			oCriteria.add (Restrictions.eq ("m_nReceiptId", m_nReceiptId));
		if(m_strDate != null && !m_strDate.trim().isEmpty())
			oCriteria.add(Restrictions.between("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate.trim(), false),GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate.trim(), true)));
		if (m_strReceiptNumber != null && !m_strReceiptNumber.isEmpty())
			oCriteria.add (Restrictions.ilike ("m_strInvoiceNumber", m_strReceiptNumber.trim(), MatchMode.ANYWHERE));
		if (m_nCreatedBy > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if (m_oClientData != null && !m_oClientData.getM_strCompanyName().trim().isEmpty())
			oClientCriteria.add(Restrictions.ilike("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(),MatchMode.ANYWHERE));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oClientCriteria.add(Restrictions.eq("m_nClientId", m_oClientData.getM_nClientId()));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (strColumn.contains("m_strCompanyName"))
			oClientCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strColumn.contains("m_strModeName"))
			oCriteria.createCriteria ("m_oMode").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nReceiptId");
	}

	private String buildReceiptXml(ArrayList<ReceiptData> arrReceiptData) 
	{
		m_oLogger.debug("buildReceiptXml");
		String strXml = "";
		for(int nIndex = 0; nIndex < arrReceiptData.size(); nIndex++)
		{
			strXml += arrReceiptData.get(nIndex).generateXML();
		}
	    return strXml;
	}

	@SuppressWarnings("unchecked")
	private float getReceiptAmount(InvoiceData oInvoiceData) 
	{
		float nAmount = 0;
		InvoiceReceiptData oInvoiceReceiptData = new InvoiceReceiptData ();
		InvoiceReceiptDataResponse oInvoiceReceiptDataResponse = new InvoiceReceiptDataResponse ();
		oInvoiceReceiptData.setM_oInvoiceData(oInvoiceData);
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData = new ArrayList(oInvoiceReceiptData.list(oOrderBy));
			for (int nIndex = 0; nIndex < oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData.size(); nIndex++) 
				nAmount += oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData.get(nIndex).getM_nAmount();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getReceiptAmount - oException" + oException);
		}
		return nAmount;
	}

	private String buildInvoiceReceiptsSetXml () 
	{
		m_oLogger.debug("buildInvoiceReceiptsSetXml");
		String strXml = "";
		Iterator<InvoiceReceiptData> oIterator = m_oInvoiceReceipts.iterator();
		while (oIterator.hasNext())
			strXml += oIterator.next().generateXML();
	    return strXml;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object addVendorToXML(Document oXmlDocument, VendorData oVendorData) 
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
		if(m_nContactId > 0 || m_nSiteId > 0)
			buildContactSiteReceiptListCriteria (oCriteria, strColumn, strOrderBy);
		else
			buildListCriteria  (oCriteria, strColumn, strOrderBy);
*/
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nReceiptId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nReceiptId"), m_nReceiptId));
		return oConjunct;
	}
}
