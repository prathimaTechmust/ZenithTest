package com.techmust.inventory.challan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
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
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.purchaseorder.PurchaseOrderLineItemData;
import com.techmust.inventory.sales.SalesData;
import com.techmust.inventory.serial.SerialNumberData;
import com.techmust.inventory.serial.SerialType;
import com.techmust.usermanagement.userinfo.UserInformationData;
@Entity
@Table(name = "tac12_challan")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChallanData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac12_id")
	private int m_nChallanId;
	@ManyToOne
	@JoinColumn(name = "ac12_sales_id")
	private SalesData m_oSalesData;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac12_created_on")
    private Date m_dCreatedOn;
	@Column(name = "ac12_Created_by")
    private int m_nCreatedBy;
	@Column(name = "ac12_challan_number")
    private String m_strChallanNumber;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ac12_invoice")
    private InvoiceData m_oInvoiceData;
    @Transient
    private String m_strDate;
    @Transient
    private String m_strTime;
    @Transient
    private float m_nChallanAmount;
    @Transient
    private String m_strFromDate = "";
    @Transient
	private String m_strToDate = "";
    
    public ChallanData ()
    {
    	m_nChallanId = -1;
    	m_dCreatedOn = Calendar.getInstance().getTime();
    	m_oSalesData = new SalesData ();
    	setM_strChallanNumber("");
      	m_strDate = "";
    	m_strTime = "";
    }

	public int getM_nChallanId () 
	{
		return m_nChallanId;
	}

	public void setM_nChallanId (int nChallanId) 
	{
		m_nChallanId = nChallanId;
	}

	public SalesData getM_oSalesData () 
	{
		return m_oSalesData;
	}

	public void setM_oSalesData (SalesData oSalesData) 
	{
		m_oSalesData = oSalesData;
	}

	public void setM_oInvoiceData(InvoiceData oInvoiceData) 
	{
		m_oInvoiceData = oInvoiceData;
	}

	public InvoiceData getM_oInvoiceData()
	{
		return m_oInvoiceData;
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

	public void setM_strChallanNumber(String strChallanNumber)
    {
	    this.m_strChallanNumber = strChallanNumber;
    }

	public String getM_strChallanNumber()
    {
	    return m_strChallanNumber;
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
	
	public void setM_nChallanAmount(float nChallanAmount) 
	{
		this.m_nChallanAmount = nChallanAmount;
	}

	public float getM_nChallanAmount() 
	{
		return m_nChallanAmount;
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

	@Override
	public String generateXML () 
	{
		String strChallanDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "ChallanData");
			addChild (oXmlDocument, oRootElement, "m_nChallanId", m_nChallanId);
			addChild (oXmlDocument, oRootElement, "m_dCreatedOn", m_dCreatedOn != null ? m_dCreatedOn.toString() : "");
//			addChild (oXmlDocument, oRootElement, "m_strUserName", getM_oCreatedBy() != null ? getM_oCreatedBy().getM_strUserName() : "");
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dCreatedOn));
			addChild (oXmlDocument, oRootElement, "m_strTime", TraderpUtil.getTime(m_dCreatedOn));
			addChild (oXmlDocument, oRootElement, "m_strChallanNumber", m_strChallanNumber);
			float nChallanAmount= m_oSalesData.getSalesTotal();
			addChild (oXmlDocument, oRootElement, "m_nChallanAmount", nChallanAmount);
			addChild (oXmlDocument, oRootElement, "m_nChallanAmountInWords",  NumberToWords.convertNumberToWords(new BigDecimal (Math.round(nChallanAmount)), true, true));
			Document oSalesXmlDoc = getXmlDocument ("<m_oSalesData>" + m_oSalesData.generateXML () + "</m_oSalesData>");
			Node oSalesNode = oXmlDocument.importNode (oSalesXmlDoc.getFirstChild (), true);
			oRootElement.appendChild (oSalesNode);
			strChallanDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strChallanDataXml;
	}

	public void setSalesId(int nSalesId)
    {
		m_oSalesData.setM_nId(nSalesId);
    }

	public static ChallanData getInstance(SalesData oSalesData) throws Exception
    {
		ChallanData oChallan = new ChallanData ();
		oChallan.m_oSalesData.setM_nId(oSalesData.getM_nId());
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		ArrayList<GenericData> oChallans = oChallan.list(oOrderBy);
		if (oChallans.size() > 1)
			throw new Exception ("ERROR: Multiple Challans Found for id !!"+oSalesData.getM_nId());
		else
			oChallan = (ChallanData) oChallans.get(0);
		return oChallan;
    }
	
	public static ChallanData createChallan (SalesData oSalesData) throws Exception
	{
		// make new challan
		String strSerialNumber = SerialNumberData.generateSerialNumber(SerialType.kChallan);
		ChallanData oChallan = new ChallanData ();
		oChallan.setSalesId (oSalesData.getM_nId());
		oChallan.setM_strChallanNumber(strSerialNumber);
		oChallan.setM_nCreatedBy(-1);
		oChallan.setM_oInvoiceData(null);
		oChallan.saveObject();
		return oChallan;
	}
	
	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oSalesCriteria = oCriteria.createCriteria ("m_oSalesData");
		if(m_nChallanId > 0)
			oCriteria.add(Restrictions.eq("m_nChallanId", m_nChallanId));
		if (m_oSalesData != null && m_oSalesData.getM_nId() > 0)
			oCriteria.add (Restrictions.eq ("m_oSalesData", m_oSalesData));
		if (m_nCreatedBy > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if ((m_oSalesData.getM_strFromDate() != null && !m_oSalesData.getM_strFromDate().isEmpty()) && (m_oSalesData.getM_strToDate() != null && !m_oSalesData.getM_strToDate().isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oSalesData.getM_strFromDate(),false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oSalesData.getM_strToDate(), true)));
		if(m_oSalesData.getM_strTo() != null && !m_oSalesData.getM_strTo().isEmpty())
			oSalesCriteria.add(Restrictions.ilike ("m_strTo", m_oSalesData.getM_strTo().trim(), MatchMode.ANYWHERE));
		if(m_oSalesData.getM_strChallanNumber() != null && !m_oSalesData.getM_strChallanNumber().isEmpty())
			oSalesCriteria.add(Restrictions.ilike ("m_strChallanNumber", m_oSalesData.getM_strChallanNumber().trim(), MatchMode.ANYWHERE));
		if(m_oSalesData.getM_nCreatedBy() > 0)
			oSalesCriteria.add(Restrictions.eq ("m_nCreatedBy", m_oSalesData.getM_nCreatedBy()));
		if(m_oSalesData.getM_oClientData() != null && m_oSalesData.getM_oClientData().getM_nClientId() > 0)
			oSalesCriteria.createCriteria("m_oClientData").add (Restrictions.eq ("m_nClientId", m_oSalesData.getM_oClientData().getM_nClientId()));
		if(m_oSalesData.getM_oContactData() != null && m_oSalesData.getM_oContactData().getM_nContactId() > 0)
			oSalesCriteria.createCriteria("m_oContactData").add (Restrictions.eq ("m_nContactId", m_oSalesData.getM_oContactData().getM_nContactId()));
		if(m_oSalesData.getM_oSiteData() != null && m_oSalesData.getM_oSiteData().getM_nSiteId() > 0)
			oSalesCriteria.createCriteria("m_oSiteData").add (Restrictions.eq ("m_nSiteId", m_oSalesData.getM_oSiteData().getM_nSiteId()));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (strColumn.contains("m_strTo"))
			oSalesCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nChallanId");
		return oCriteria;
	}

	public float getQuantity (PurchaseOrderLineItemData oPOLineItem) throws Exception
    {
		return m_oSalesData.getQuantity (oPOLineItem);
    }

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) 
	{
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if(m_nChallanId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nChallanId"), m_nChallanId));
		if (m_oSalesData != null && m_oSalesData.getM_nId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oSalesData"), m_oSalesData));
		if (m_nCreatedBy > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCreatedBy"), m_nCreatedBy));
		if ((m_oSalesData.getM_strFromDate() != null && !m_oSalesData.getM_strFromDate().isEmpty()) && (m_oSalesData.getM_strToDate() != null && !m_oSalesData.getM_strToDate().isEmpty()))
			 oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oSalesData.getM_strFromDate(), false).toString()), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
		if(m_oSalesData.getM_strTo() != null && !m_oSalesData.getM_strTo().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strTo")),m_oSalesData.getM_strTo().trim())); 
		if(m_oSalesData.getM_strChallanNumber() != null && !m_oSalesData.getM_strChallanNumber().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strChallanNumber")),m_oSalesData.getM_strChallanNumber().trim()));
		if(m_oSalesData.getM_nCreatedBy() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCreatedBy"), m_oSalesData.getM_nCreatedBy()));
		if(m_oSalesData.getM_oClientData() != null && m_oSalesData.getM_oClientData().getM_nClientId() > 0)
			//oSalesCriteria.createCriteria("m_oClientData").add (Restrictions.eq ("m_nClientId", m_oSalesData.getM_oClientData().getM_nClientId()));
		if(m_oSalesData.getM_oContactData() != null && m_oSalesData.getM_oContactData().getM_nContactId() > 0)
			//oSalesCriteria.createCriteria("m_oContactData").add (Restrictions.eq ("m_nContactId", m_oSalesData.getM_oContactData().getM_nContactId()));
		if(m_oSalesData.getM_oSiteData() != null && m_oSalesData.getM_oSiteData().getM_nSiteId() > 0)
		//	oSalesCriteria.createCriteria("m_oSiteData").add (Restrictions.eq ("m_nSiteId", m_oSalesData.getM_oSiteData().getM_nSiteId()));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"),
				oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false).toString()),
				oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.ge(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false).toString())));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.le(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
		//if (strColumn.contains("m_strTo"))
		//	oSalesCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nChallanId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nChallanId"), m_nChallanId));
		return oConjunct;
	}
}
