package com.techmust.inventory.purchasereturned;

import java.util.Calendar;
import java.util.Date;
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

import org.codehaus.jackson.annotate.JsonIgnore;
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
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.purchase.PurchaseLineItem;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@Entity
@Table(name = "tac57_purchase_returned")
public class PurchaseReturnedData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac57_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name = "ac57_Vendor_id")
	@ColumnDefault("-1")
	private VendorData m_oVendorData;
	@JsonManagedReference
	@OneToMany
	@JoinColumn(name = "ac58_returned_id")
	private Set<PurchaseReturnedLineItemData> m_oPurchaseReturnedLineItemData;
	@Transient
	private String m_strDate;
	@Transient
	private String m_strFromDate;
	@Transient
    private String m_strToDate;
    @Column(name = "ac57_debitNote_number")
    private String m_strDebitNoteNumber;
    @Transient
	public PurchaseReturnedLineItemData [] m_arrPurchaseReturnedLineItemData;
	@Column(name = "ac57_Created_by")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac57_created_on")
	private Date m_dCreatedOn;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	
	public PurchaseReturnedData () 
	{
		m_nId = -1;
		m_oVendorData = new VendorData ();
		m_oPurchaseReturnedLineItemData = new HashSet<PurchaseReturnedLineItemData> ();
		m_strDate = "";
		m_strFromDate = "";
		m_strToDate = "";
		m_strDebitNoteNumber = "";
		m_nCreatedBy = -1;
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_oUserCredentialsData = new UserInformationData ();
	}

	public int getM_nId () 
	{
		return m_nId;
	}

	public void setM_nId (int nId) 
	{
		m_nId = nId;
	}

	public VendorData getM_oVendorData ()
	{
		return m_oVendorData;
	}

	public void setM_oVendorData (VendorData oVendorData) 
	{
		m_oVendorData = oVendorData;
	}

	public Set<PurchaseReturnedLineItemData> getM_oPurchaseReturnedLineItemData () 
	{
		return m_oPurchaseReturnedLineItemData;
	}

	public void setM_oPurchaseReturnedLineItemData (Set<PurchaseReturnedLineItemData> oPurchaseReturnedLineItemData) 
	{
		m_oPurchaseReturnedLineItemData = oPurchaseReturnedLineItemData;
	}

	public String getM_strDate () 
	{
		return m_strDate;
	}

	public void setM_strDate (String strDate) 
	{
		m_strDate = strDate;
	}

	public String getM_strFromDate () 
	{
		return m_strFromDate;
	}

	public void setM_strFromDate (String strFromDate) 
	{
		m_strFromDate = strFromDate;
	}

	public String getM_strToDate () 
	{
		return m_strToDate;
	}

	public void setM_strToDate (String strToDate) 
	{
		m_strToDate = strToDate;
	}

	public String getM_strDebitNoteNumber ()
	{
		return m_strDebitNoteNumber;
	}

	public void setM_strDebitNoteNumber (String strDebitNoteNumber) 
	{
		m_strDebitNoteNumber = strDebitNoteNumber;
	}

	public int getM_nCreatedBy () 
	{
		return m_nCreatedBy;
	}

	public void setM_oCreatedBy (int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public Date getM_dCreatedOn () 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn (Date oCreatedOn) 
	{
		m_dCreatedOn = oCreatedOn;
	}

	public UserInformationData getM_oUserCredentialsData () 
	{
		return m_oUserCredentialsData;
	}

	public void setM_oUserCredentialsData (UserInformationData oUserCredentialsData) 
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}

	@Override
	public String generateXML () 
	{
		String strReturnedDataXml = "";
		m_oLogger.info("generateXML ");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "PurchaseReturnedData");
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			addChild (oXmlDocument, oRootElement, "m_strDebitNoteNumber", m_strDebitNoteNumber);
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dCreatedOn));
			Document oClientDoc = getXmlDocument (m_oVendorData.generateXML());
			Node oClientNode = oXmlDocument.importNode (oClientDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
			Document oReturnedLineItemDoc = getXmlDocument ("<m_oPurchaseReturnedLineItemData>" + buildReturnedLineItem () + "</m_oPurchaseReturnedLineItemData>");
			Node oLineItemNode = oXmlDocument.importNode (oReturnedLineItemDoc.getFirstChild (), true);
			oRootElement.appendChild (oLineItemNode);
			strReturnedDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strReturnedDataXml;
	}

	private String buildReturnedLineItem() 
	{
		String strXML = "";
		Object [] arrReturnedLineitems = m_oPurchaseReturnedLineItemData.toArray();
		float nTotal = 0;
		for (int nIndex = 0; nIndex < arrReturnedLineitems.length; nIndex ++)
		{
			if (isReturnedLineItemClass(arrReturnedLineitems[nIndex]))
			{
				PurchaseReturnedLineItemData oReturnedLineItemData = (PurchaseReturnedLineItemData) arrReturnedLineitems [nIndex];
				strXML += oReturnedLineItemData.generateXML();
				nTotal += oReturnedLineItemData.getM_nQuantity() * (oReturnedLineItemData.getM_oPurchaseLineItem().getM_nPrice() - (oReturnedLineItemData.getM_oPurchaseLineItem().getM_nPrice() * oReturnedLineItemData.getM_oPurchaseLineItem().getM_nDiscount()/100) + (oReturnedLineItemData.getM_oPurchaseLineItem().getM_nPrice() * oReturnedLineItemData.getM_oPurchaseLineItem().getM_nTax()/100));
			}
		}
		strXML += "<grandTotal>"+ nTotal +"</grandTotal>";
		return strXML;
	}

	private boolean isReturnedLineItemClass(Object oLineItem) 
	{
		return oLineItem.getClass().toString().equalsIgnoreCase("class com.techmust.inventory.purchasereturned.PurchaseReturnedLineItemData");
	}

	@Override
	public GenericData getInstanceData (String strXML, UserInformationData credentials) throws Exception
	{
		return null;
	}

	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy)
	{
		Criteria oVendorDataCriteria = oCriteria.createCriteria ("m_oVendorData");
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_oVendorData != null && m_oVendorData.getM_strCompanyName().trim() != "")
			oVendorDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oVendorData.getM_strCompanyName().trim(),MatchMode.ANYWHERE));
		if (m_strDebitNoteNumber != null && !m_strDebitNoteNumber.trim().isEmpty())
			oCriteria.add(Restrictions.ilike ("m_strDebitNoteNumber", m_strDebitNoteNumber.trim(),MatchMode.ANYWHERE));
		if (m_nCreatedBy > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if ((m_strDate != null && !m_strDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate, true)));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oVendorData", m_oVendorData));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (strColumn.contains("m_strCompanyName"))
			oVendorDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_dCreatedOn");
		return oCriteria;
	}
	
	public void prepareReturnData() 
	{
		for (int nIndex = 0; nIndex < m_arrPurchaseReturnedLineItemData.length; nIndex++)
		{
			m_arrPurchaseReturnedLineItemData[nIndex].setM_oPurchaseReturnedData(this);
			m_oPurchaseReturnedLineItemData.add(m_arrPurchaseReturnedLineItemData[nIndex]);
		}
	}

	public void returned(boolean bAdd) throws Exception 
	{
		Iterator<PurchaseReturnedLineItemData> oIterator = m_oPurchaseReturnedLineItemData.iterator();
		while (oIterator.hasNext())
		{
			PurchaseReturnedLineItemData oReturnedLineItemData = oIterator.next();
			PurchaseLineItem oPurchaseLineItem = new PurchaseLineItem ();
			oPurchaseLineItem.setM_nLineItemId(oReturnedLineItemData.getM_oPurchaseLineItem().getM_nLineItemId());
			oPurchaseLineItem = (PurchaseLineItem) GenericIDataProcessor.populateObject(oPurchaseLineItem);
			oPurchaseLineItem.returned(bAdd ? oReturnedLineItemData.getM_nQuantity() : -oReturnedLineItemData.getM_nQuantity());
		}
	}

	@JsonIgnore
	public PurchaseReturnedData getRemovedReturns() throws Exception 
	{
		PurchaseReturnedData oRemovedReturns = new PurchaseReturnedData ();
		PurchaseReturnedData oReturnedData = (PurchaseReturnedData) GenericIDataProcessor.populateObject(this);
		addRemovedReturnedLineItems (oReturnedData, oRemovedReturns);
		return oRemovedReturns;
	}

	private void addRemovedReturnedLineItems(PurchaseReturnedData oReturnedData, PurchaseReturnedData oRemovedReturns) 
	{
		Iterator<PurchaseReturnedLineItemData> oIterator =  oReturnedData.m_oPurchaseReturnedLineItemData.iterator();
		while (oIterator.hasNext())
		{
			PurchaseReturnedLineItemData oReturnedLineItemData = oIterator.next();
			if (!isItemExists (oReturnedLineItemData))
				oRemovedReturns.m_oPurchaseReturnedLineItemData.add(oReturnedLineItemData);
		}
	}
	
	private boolean isItemExists(PurchaseReturnedLineItemData oReturnedLineItemData) 
	{
		boolean bIsExist = false;
		Iterator<PurchaseReturnedLineItemData> oIterator =  m_oPurchaseReturnedLineItemData.iterator();
		while (!bIsExist && oIterator.hasNext())
			bIsExist = (oReturnedLineItemData.getM_nId() == oIterator.next().getM_nId());
		return bIsExist;
	}

	public void deleteReturns(PurchaseReturnedData oRemovedReturns) throws Exception 
	{
		Iterator<PurchaseReturnedLineItemData> oIterator =  oRemovedReturns.m_oPurchaseReturnedLineItemData.iterator();
		while (oIterator.hasNext())
			oIterator.next().deleteObject();
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId));
//		Criteria oVendorDataCriteria = oCriteria.createCriteria ("m_oVendorData");
//		if (m_oVendorData != null && m_oVendorData.getM_strCompanyName().trim() != "")
//			oVendorDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oVendorData.getM_strCompanyName().trim(),MatchMode.ANYWHERE));
		if (m_strDebitNoteNumber != null && !m_strDebitNoteNumber.trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strDebitNoteNumber")), m_strDebitNoteNumber.toLowerCase())); 
		if (m_nCreatedBy > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCreatedBy"), m_nCreatedBy));
		if ((m_strDate != null && !m_strDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate,false).toString()), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate, true).toString())));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oVendorData"), m_oVendorData));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false).toString()), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.ge(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false).toString())));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.le(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate,false).toString())));
/*
		if (strColumn.contains("m_strCompanyName"))
			oVendorDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_dCreatedOn");
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
