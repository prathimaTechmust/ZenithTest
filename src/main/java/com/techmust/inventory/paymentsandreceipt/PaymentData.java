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
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@Entity
@Table(name = "tac37_payment")
public class PaymentData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac37_id")
	private int m_nPaymentId;
	@ManyToOne
	@JoinColumn(name = "ac37_Vendor_id")
	private VendorData m_oVendorData;
	
	@JsonManagedReference
	@OneToMany
	@JoinColumn(name = "ac38_payment_id")
	private Set<PurchasePaymentData> m_oPurchases;
	@Transient
	public PurchasePaymentData [] m_arrPurchases;
	@Column(name = "ac37_amount")
	private float m_nAmount;
	@ManyToOne
	@JoinColumn(name = "ac37_mode_id")
	private TransactionMode m_oMode;
	@Column(name = "ac37_details")
	private String m_strDetails;
	@Column(name = "ac37_paymentNumber")
	private String m_strPaymentNumber;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="ac37_created_on")
    private Date m_dCreatedOn;
	@Column(name = "ac37_Created_by")
    private int m_nCreatedBy;
	@Transient
    private String m_strDate;
	@Transient
    private UserInformationData m_oUserCredentialsData;
	@Transient
    private String m_strFromDate = "";
	@Transient
    private String m_strToDate = "";	
	@Transient
    private String m_strTallyTransformDate;   
    
    public PaymentData() 
	{
    	m_nPaymentId = -1;
    	m_oPurchases = new HashSet<PurchasePaymentData> ();
		m_nAmount = 0;
		m_strDetails = "";
		setM_dCreatedOn(Calendar.getInstance().getTime());
		m_strTallyTransformDate = "";
	}
    
	public int getM_nPaymentId() 
	{
		return m_nPaymentId;
	}

	public void setM_nPaymentId(int nPaymentId) 
	{
		m_nPaymentId = nPaymentId;
	}

	public VendorData getM_oVendorData() 
	{
		return m_oVendorData;
	}

	public void setM_oVendorData(VendorData oVendorData) 
	{
		m_oVendorData = oVendorData;
	}
	
	public float getM_nAmount() 
	{
		return m_nAmount;
	}

	public void setM_nAmount(float nAmount) 
	{
		m_nAmount = nAmount;
	}

	public TransactionMode getM_oMode() 
	{
		return m_oMode;
	}

	public void setM_oMode(TransactionMode oMode) 
	{
		m_oMode = oMode;
	}

	public String getM_strDetails() 
	{
		return m_strDetails;
	}

	public void setM_strDetails(String strDetails) 
	{
		m_strDetails = strDetails;
	}

	public void setM_strPaymentNumber(String strPaymentNumber) 
	{
		m_strPaymentNumber = strPaymentNumber;
	}

	public String getM_strPaymentNumber() 
	{
		return m_strPaymentNumber;
	}

	public Date getM_dCreatedOn() 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public void setM_strDate(String strDate) 
	{
		this.m_strDate = strDate;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public int getM_nCreatedBy() 
	{
		return m_nCreatedBy;
	}

	public void setM_nCreatedBy(int nCreatedBy)
	{
		m_nCreatedBy = nCreatedBy;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		this.m_oUserCredentialsData = oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}
	
	public void setM_strFromDate(String strFromDate) 
	{
		m_strFromDate = strFromDate;
	}

	public String getM_strFromDate()
	{
		return m_strFromDate;
	}

	public void setM_strToDate(String strToDate) 
	{
		m_strToDate = strToDate;
	}

	public String getM_strToDate() 
	{
		return m_strToDate;
	}

	public Set<PurchasePaymentData> getM_oPurchases() 
	{
		return m_oPurchases;
	}

	public void setM_oPurchases(Set<PurchasePaymentData> purchases)
	{
		m_oPurchases = purchases;
	}

	public void setM_strTallyTransformDate(String strTallyTransformDate) 
	{
		this.m_strTallyTransformDate = strTallyTransformDate;
	}

	public String getM_strTallyTransformDate() 
	{
		return m_strTallyTransformDate;
	}

	@Override
	public String generateXML() 
	{
		String strXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "PaymentData");
 			addChild (oXmlDocument, oRootElement, "m_nPaymentId", m_nPaymentId);
			addChild (oXmlDocument, oRootElement, "m_dCreatedOn", m_dCreatedOn != null ? m_dCreatedOn.toString() : "");
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(getM_dCreatedOn()));
			addChild (oXmlDocument, oRootElement, "m_nAmount", TraderpUtil.formatNumber(m_nAmount));
			addChild (oXmlDocument, oRootElement, "m_strPaymentAmountInWords", NumberToWords.convertNumberToWords(new BigDecimal (m_nAmount), true, true));
			addChild (oXmlDocument, oRootElement, "m_strTallyTransformDate", TraderpUtil.getTallyCompatibleFormat(m_dCreatedOn));
			addChild (oXmlDocument, oRootElement, "m_strDetails", m_strDetails);
			addChild (oXmlDocument, oRootElement, "m_strPaymentNumber", m_strPaymentNumber);
			Document oClientDoc = getXmlDocument (m_oVendorData.generateXML());
			Node oClientNode = oXmlDocument.importNode (oClientDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
			Document oModeDoc = getXmlDocument (m_oMode.generateXML());
			Node oModeNode = oXmlDocument.importNode (oModeDoc.getFirstChild (), true);
			oRootElement.appendChild (oModeNode);
			Document oPurchasePayementSetDoc = getXmlDocument ("<m_oPurchasePayementSet>" + buildPurchasePaymentSetXml () + "</m_oPurchasePayementSet>");
			Node oPurchasePayementSetNode = oXmlDocument.importNode (oPurchasePayementSetDoc.getFirstChild (), true);
			oRootElement.appendChild (oPurchasePayementSetNode);
			strXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strXml;
	}
	
	private String buildPurchasePaymentSetXml() 
	{
		m_oLogger.debug("buildPurchasePaymentSetXml");
		String strXml = "";
		Iterator<PurchasePaymentData> oIterator = m_oPurchases.iterator();
		while (oIterator.hasNext())
			strXml += oIterator.next().generateXML();
	    return strXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oVendorCriteria = oCriteria.createCriteria("m_oVendorData");
		if (m_nPaymentId > 0)
			oCriteria.add (Restrictions.eq ("m_nPaymentId", m_nPaymentId));
		if(m_strDate != null && !m_strDate.trim().isEmpty())
			oCriteria.add(Restrictions.between("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate.trim(), false),GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate.trim(), true)));
		if (m_strPaymentNumber != null && !m_strPaymentNumber.isEmpty())
			oCriteria.add (Restrictions.ilike ("m_strInvoiceNumber", m_strPaymentNumber.trim(), MatchMode.ANYWHERE));
		if (m_nCreatedBy  > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if (m_oVendorData != null && !m_oVendorData.getM_strCompanyName().trim().isEmpty())
			oVendorCriteria.add(Restrictions.ilike("m_strCompanyName", m_oVendorData.getM_strCompanyName().trim(),MatchMode.ANYWHERE));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oVendorCriteria.add(Restrictions.eq ("m_nClientId", m_oVendorData.getM_nClientId()));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (strColumn.contains("m_strCompanyName"))
			oVendorCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strColumn.contains("m_strModeName"))
			oCriteria.createCriteria ("m_oMode").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nPaymentId");
		return oCriteria;
	}

	public void prepareForSaveOrUpdate() throws Exception 
	{
		for (int nIndex = 0; nIndex < m_arrPurchases.length; nIndex++) 
		{
			PurchasePaymentData oPurchasePaymentData = m_arrPurchases[nIndex];
			oPurchasePaymentData.setM_oPaymentData(this);
			oPurchasePaymentData.setM_nPurchasePaymentId(oPurchasePaymentData.getPurchasePaymentId(oPurchasePaymentData.getM_oPurchaseData().getM_nId(), m_nPaymentId));
			m_oPurchases.add(oPurchasePaymentData);
		}
	}

	public void updatePaymentInvoices(PaymentData oPaymentData) 
	{
		Iterator<PurchasePaymentData> oIterator = m_oPurchases.iterator();
		while (oIterator.hasNext())
		{
			PurchasePaymentData oPurchasePaymentData = oIterator.next();
			try
			{
				PurchaseData oPurchaseData = (PurchaseData) GenericIDataProcessor.populateObject(oPurchasePaymentData.getM_oPurchaseData());
				oPurchaseData.setM_nPaymentAmount(Math.round(getPaymentAmount (oPurchaseData)));
				oPurchaseData.setM_nBalanceAmount(Math.round(oPurchaseData.getPurchaseAmount() - oPurchaseData.getM_nPaymentAmount()));
				oPurchaseData.updateObject();
			}
			catch (Exception oException)
			{
				
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private float getPaymentAmount(PurchaseData oPurchaseData) 
	{
		float nAmount = 0;
		PurchasePaymentData oPurchasePaymentData = new PurchasePaymentData ();
		PurchasePaymentDataResponse oPurchasePaymentDataResponse = new PurchasePaymentDataResponse ();
		oPurchasePaymentData.setM_oPurchaseData(oPurchaseData);
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oPurchasePaymentDataResponse.m_arrPurchasePaymentData = new ArrayList(oPurchasePaymentData.list(oOrderBy));
			for (int nIndex = 0; nIndex < oPurchasePaymentDataResponse.m_arrPurchasePaymentData.size(); nIndex++) 
				nAmount += oPurchasePaymentDataResponse.m_arrPurchasePaymentData.get(nIndex).getM_nAmount();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getPaymentAmount - oException" + oException);
		}
		return nAmount;
	}
	
	@SuppressWarnings("unchecked")
	public float getVendorPaymentsAmount(int nVendorId) 
	{
		float nAmount = 0;
		PaymentData oPaymentData = new PaymentData ();
		VendorData oVendorData = new VendorData ();
		oVendorData.setM_nClientId(nVendorId);
		oPaymentData.setM_oVendorData(oVendorData);
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			ArrayList<PaymentData> arrPayments =  new ArrayList (oPaymentData.list (oOrderBy));
			for (int nIndex = 0; nIndex < arrPayments.size(); nIndex++) 
				nAmount += arrPayments.get(nIndex).getM_nAmount();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getVendorPaymentsAmount - oException" + oException);
		}
		return nAmount;
	}

	public String generateTallyDataXML(ArrayList<PaymentData> arrPaymentData) 
	{
		String strTallyDataXml = "";
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Document oInvoiceXmlDoc = getXmlDocument ("<ReportPaymentData>" + buildPaymentXml (arrPaymentData) + "</ReportPaymentData>");
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

	private String buildPaymentXml(ArrayList<PaymentData> arrPaymentData) 
	{
		m_oLogger.debug("buildPaymentXml");
		String strXml = "";
		for(int nIndex = 0; nIndex < arrPaymentData.size(); nIndex++)
		{
			strXml += arrPaymentData.get(nIndex).generateXML();
		}
	    return strXml;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*
		Criteria oVendorCriteria = oCriteria.createCriteria("m_oVendorData");
		if (m_nPaymentId > 0)
			oCriteria.add (Restrictions.eq ("m_nPaymentId", m_nPaymentId));
		if(m_strDate != null && !m_strDate.trim().isEmpty())
			oCriteria.add(Restrictions.between("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate.trim(), false),GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate.trim(), true)));
		if (m_strPaymentNumber != null && !m_strPaymentNumber.isEmpty())
			oCriteria.add (Restrictions.ilike ("m_strInvoiceNumber", m_strPaymentNumber.trim(), MatchMode.ANYWHERE));
		if (m_oCreatedBy != null && m_oCreatedBy.getM_nUserId() > 0)
			oCriteria.add (Restrictions.eq ("m_oCreatedBy", m_oCreatedBy));
		if (m_oVendorData != null && !m_oVendorData.getM_strCompanyName().trim().isEmpty())
			oVendorCriteria.add(Restrictions.ilike("m_strCompanyName", m_oVendorData.getM_strCompanyName().trim(),MatchMode.ANYWHERE));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oVendorCriteria.add(Restrictions.eq ("m_nClientId", m_oVendorData.getM_nClientId()));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (strColumn.contains("m_strCompanyName"))
			oVendorCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else if (strColumn.contains("m_strModeName"))
			oCriteria.createCriteria ("m_oMode").addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nPaymentId");
*/			
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nPaymentId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nPaymentId"), m_nPaymentId));
		return oConjunct;
	}
}
