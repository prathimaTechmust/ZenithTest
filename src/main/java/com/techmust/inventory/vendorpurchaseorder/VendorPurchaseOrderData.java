package com.techmust.inventory.vendorpurchaseorder;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.inventory.purchase.PurchaseLineItem;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@Entity
@Table(name = "tac47_vendor_purchaseorder")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorPurchaseOrderData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac47_purchaseorder_id")
	private int m_nPurchaseOrderId;
	@ManyToOne
	@JoinColumn(name = "ac47_Vendor_id")
	@ColumnDefault("-1")
	private VendorData m_oVendorData;
	@Column(name = "ac47_purchaseorder_number")
	private String m_strPurchaseOrderNumber;
	@Basic
	@Temporal(TemporalType.DATE)
	@Column(name = "ac47_purchaseorder_date")
	private Date m_dPurchaseOrderDate;
	@Transient
	private String m_strPurchaseOrderDate;
	@Column(name = "ac47_Created_by")	
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac47_created_on")
	private Date m_dCreatedOn;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ac47_Vendor_PO_status")
	private VendorPurchaseOrderStatus m_nVendorPurchaseOrderStatus;
	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "ac48_vendor_PO_id")
	private Set<VendorPOLineItemData> m_oVendorPOLineItemData;
	@Transient
	public VendorPOLineItemData [] m_arrPurchaseOrderLineItems;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Transient
	private boolean m_bIsForAllList;
	@Transient
	protected String m_strFromDate;
	@Transient
	protected String m_strToDate;
	
	public VendorPurchaseOrderData ()
	{
		m_nPurchaseOrderId = -1;
		m_strPurchaseOrderNumber = "";
		m_oVendorData = new VendorData ();
		m_dPurchaseOrderDate = Calendar.getInstance().getTime();
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_nVendorPurchaseOrderStatus = VendorPurchaseOrderStatus.kPending;
		m_strPurchaseOrderDate = "";
		m_oUserCredentialsData = new UserInformationData ();
		setM_oVendorPOLineItemData(new HashSet<VendorPOLineItemData> ());
	}

	public int getM_nPurchaseOrderId() 
	{
		return m_nPurchaseOrderId;
	}

	public void setM_nPurchaseOrderId(int nPurchaseOrderId) 
	{
		m_nPurchaseOrderId = nPurchaseOrderId;
	}

	public VendorData getM_oVendorData() 
	{
		return m_oVendorData;
	}

	public void setM_oVendorData(VendorData oVendorData)
	{
		m_oVendorData = oVendorData;
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

	public String getM_strPurchaseOrderDate() 
	{
		return m_strPurchaseOrderDate;
	}

	public void setM_strPurchaseOrderDate(String strPurchaseOrderDate) 
	{
		m_strPurchaseOrderDate = strPurchaseOrderDate;
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

	public VendorPurchaseOrderStatus getM_nVendorPurchaseOrderStatus() 
	{
		return m_nVendorPurchaseOrderStatus;
	}

	public void setM_nVendorPurchaseOrderStatus(VendorPurchaseOrderStatus nVendorPurchaseOrderStatus) 
	{
		m_nVendorPurchaseOrderStatus = nVendorPurchaseOrderStatus;
	}

	public void setM_oVendorPOLineItemData(Set<VendorPOLineItemData> oVendorPOLineItemData) 
	{
		m_oVendorPOLineItemData = oVendorPOLineItemData;
	}

	public Set<VendorPOLineItemData> getM_oVendorPOLineItemData() 
	{
		return m_oVendorPOLineItemData;
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

	public void setM_strFromDate(String fromDate) 
	{
		m_strFromDate = fromDate;
	}

	public String getM_strToDate() 
	{
		return m_strToDate;
	}

	public void setM_strToDate(String toDate) 
	{
		m_strToDate = toDate;
	}

	@Override
	public String generateXML() 
	{
		String strPurchaseOrderDataXml = "";
		m_oLogger.info("generateXML ");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "VendorPurchaseOrderData");
			addChild (oXmlDocument, oRootElement, "m_nPurchaseOrderId", m_nPurchaseOrderId);
			addChild (oXmlDocument, oRootElement, "m_dPurchaseOrderDate", m_dPurchaseOrderDate != null ? m_dPurchaseOrderDate.toString() : "");
			addChild (oXmlDocument, oRootElement, "m_strPurchaseOrderNumber", m_strPurchaseOrderNumber);
			Document oVendorDoc = getXmlDocument (m_oVendorData.generateXML());
			Node oClientNode = oXmlDocument.importNode (oVendorDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
			Document oVendorPOLineItemDataDoc = getXmlDocument ("<m_oVendorPOLineItemData>" + buildPurchaseOrderLineItem () + "</m_oVendorPOLineItemData>");
			Node oLineItemNode = oXmlDocument.importNode (oVendorPOLineItemDataDoc.getFirstChild (), true);
			oRootElement.appendChild (oLineItemNode);
			strPurchaseOrderDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strPurchaseOrderDataXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oVendorDataCriteria = oCriteria.createCriteria ("m_oVendorData");
		if (m_nPurchaseOrderId > 0)
			oCriteria.add (Restrictions.eq ("m_nPurchaseOrderId", m_nPurchaseOrderId));
		if (m_strPurchaseOrderNumber != null && !m_strPurchaseOrderNumber.trim().isEmpty ())
			oCriteria.add (Restrictions.ilike ("m_strPurchaseOrderNumber", m_strPurchaseOrderNumber.trim(),MatchMode.ANYWHERE));
		if(m_oVendorData.getM_strCompanyName() != null && !m_oVendorData.getM_strCompanyName().trim().isEmpty())
			oVendorDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oVendorData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if(m_strPurchaseOrderDate != null && !m_strPurchaseOrderDate.trim().isEmpty())
			oCriteria.add(Restrictions.eq("m_dPurchaseOrderDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strPurchaseOrderDate.trim(), true)));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oClientData", m_oVendorData));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dPurchaseOrderDate", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (!m_bIsForAllList)
			oCriteria.add(Restrictions.eq("m_nVendorPurchaseOrderStatus", m_nVendorPurchaseOrderStatus));
		if (strColumn.contains("m_strCompanyName"))
			oVendorDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nPurchaseOrderId");
		return oCriteria;
	}

	public void prepareForUpdate() throws Exception 
	{
		m_oLogger.info("preparePurchaseOrderData");
		try
		{
			if(this.getM_nPurchaseOrderId() > 0)
			{	
				VendorPurchaseOrderData oPOData = (VendorPurchaseOrderData) GenericIDataProcessor.populateObject(this);
				m_nVendorPurchaseOrderStatus = oPOData.m_nVendorPurchaseOrderStatus;
			}
		}
		catch (Exception oException)
		{
			// 
		}
		for (int nIndex = 0 ; m_arrPurchaseOrderLineItems != null && nIndex < m_arrPurchaseOrderLineItems.length; nIndex++ )
		{
			VendorPOLineItemData oPOLineItem = m_arrPurchaseOrderLineItems[nIndex];
			oPOLineItem.prepareForUpdate (this);
			m_oVendorPOLineItemData.add(oPOLineItem);
		}
		setM_dPurchaseOrderDate(GenericIDataProcessor.getDBCompatibleDateFormat(getM_strPurchaseOrderDate()));
	}

	public void setReceived(PurchaseData oPurchaseData) throws Exception 
	{
		Iterator<PurchaseLineItem> oIterator = oPurchaseData.getM_oPurchaseLineItems().iterator();
		while(oIterator.hasNext())
		{
			setReceive (oIterator.next());
		}
		setStatus ();
	}

	private void setStatus() throws Exception 
	{
		int nMatchCount = 0;
		Iterator<VendorPOLineItemData> oIterator =  m_oVendorPOLineItemData.iterator();
		int nLineitemCount = m_oVendorPOLineItemData.size();
		while(oIterator.hasNext())
		{
			VendorPOLineItemData oVendorPOLineItemData = oIterator.next();
			if(oVendorPOLineItemData.getM_nQuantity() == oVendorPOLineItemData.getM_nReceivedQty())
				nMatchCount++;
		}
		m_nVendorPurchaseOrderStatus = nMatchCount == nLineitemCount ? VendorPurchaseOrderStatus.kDelivered : m_nVendorPurchaseOrderStatus;
		this.updateObject();
	}

	private void setReceive(PurchaseLineItem oPurchaseLineItem) throws Exception 
	{
		Iterator<VendorPOLineItemData> oIterator =  m_oVendorPOLineItemData.iterator();
		while(oIterator.hasNext())
		{
			VendorPOLineItemData oVendorPOLineItemData = oIterator.next();
			if(oVendorPOLineItemData.getM_oItemData().getM_nItemId() == oPurchaseLineItem.getM_oItemData().getM_nItemId())
			{
				oVendorPOLineItemData.setReceived (oPurchaseLineItem.getM_nQuantity());
				oVendorPOLineItemData.updateObject();
			}
		}
	}
	
	private String buildPurchaseOrderLineItem ()
	{
		String strXml = "";
		Iterator<VendorPOLineItemData> oIterator = m_oVendorPOLineItemData.iterator();
		while (oIterator.hasNext())
		{
			VendorPOLineItemData oVendorPOLineItemData = oIterator.next();
			strXml += oVendorPOLineItemData.generateXML();
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
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nPurchaseOrderId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nPurchaseOrderId"), m_nPurchaseOrderId));
		if (m_strPurchaseOrderNumber != null && !m_strPurchaseOrderNumber.trim().isEmpty ())
		    oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strPurchaseOrderNumber")), m_strPurchaseOrderNumber.trim())); 
		if(m_oVendorData.getM_strCompanyName() != null && !m_oVendorData.getM_strCompanyName().trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCompanyName")), m_oVendorData.getM_strCompanyName().trim())); 
		if(m_strPurchaseOrderDate != null && !m_strPurchaseOrderDate.trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_dPurchaseOrderDate"), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strPurchaseOrderDate.trim(), true)));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oClientData"), m_oVendorData));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dPurchaseOrderDate"), 
										  oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false).toString()),
										  oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
		if (!m_bIsForAllList)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nVendorPurchaseOrderStatus"), m_nVendorPurchaseOrderStatus));		
		/*if (strColumn.contains("m_strCompanyName"))
			oVendorDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
			addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nPurchaseOrderId");*/
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
