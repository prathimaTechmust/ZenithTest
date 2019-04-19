package com.techmust.inventory.returned;

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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.sales.NonStockSalesLineItemData;
import com.techmust.inventory.sales.SalesLineItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac23_returned")
public class ReturnedData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac23_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name = "ac23_Client_id")
	@ColumnDefault("-1")
	private ClientData m_oClientData;
	
	@OneToMany
	@JoinColumn(name = "ac24_returned_id")
	private Set<ReturnedLineItemData> m_oReturnedLineItems;
	@Transient
	public ReturnedLineItemData [] m_arrReturnedLineItemData;
	@OneToMany
	@JoinColumn(name = "ac25_returned_id")
	private Set<NonStockReturnedLineItemData> m_oNonStockReturnedLineItems;
	@Transient
	public NonStockReturnedLineItemData [] m_arrNonStockReturnedLineItemData;
	
	@Column(name = "ac23_Created_by")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac23_created_on")
	private Date m_dCreatedOn;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	@Transient
	private String m_strDate;
	@Transient
	private String m_strFromDate;
	@Transient
    private String m_strToDate;
    @Column(name = "ac23_creditNote_number")
    private String m_strCreditNoteNumber;

	public ReturnedData ()
	{
		m_nId = -1;
		m_dCreatedOn = Calendar.getInstance().getTime();
		m_oReturnedLineItems = new HashSet<ReturnedLineItemData> ();
		m_oNonStockReturnedLineItems = new HashSet<NonStockReturnedLineItemData> ();
		m_strFromDate = "";
	    m_strToDate = "";
	    m_strCreditNoteNumber = "";
	}
	
	public int getM_nId() 
	{
		return m_nId;
	}

	public void setM_nId(int nId) 
	{
		m_nId = nId;
	}

	public ClientData getM_oClientData() 
	{
		return m_oClientData;
	}

	public void setM_oClientData(ClientData oClientData) 
	{
		m_oClientData = oClientData;
	}

	public Set<ReturnedLineItemData> getM_oReturnedLineItems() 
	{
		return m_oReturnedLineItems;
	}

	public void setM_oReturnedLineItems(Set<ReturnedLineItemData> returnedLineItems) 
	{
		m_oReturnedLineItems = returnedLineItems;
	}

	public Set<NonStockReturnedLineItemData> getM_oNonStockReturnedLineItems()
	{
		return m_oNonStockReturnedLineItems;
	}

	public void setM_oNonStockReturnedLineItems(Set<NonStockReturnedLineItemData> nonStockReturnedLineItems) 
	{
		m_oNonStockReturnedLineItems = nonStockReturnedLineItems;
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
	
	public void setM_strDate(String strDate) 
	{
		m_strDate = strDate;
	}

	public String getM_strDate() 
	{
		return m_strDate;
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

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}
	
	public void setM_strCreditNoteNumber (String strCreditNoteNumber) 
	{
		m_strCreditNoteNumber = strCreditNoteNumber;
	}

	public String getM_strCreditNoteNumber () 
	{
		return m_strCreditNoteNumber;
	}

	public void returned (boolean bAdd) throws Exception
	{
		Iterator<ReturnedLineItemData> oIterator = m_oReturnedLineItems.iterator();
		while (oIterator.hasNext())
		{
			ReturnedLineItemData oReturnedLineItemData = oIterator.next();
			SalesLineItemData oSaleslLineItemData = (SalesLineItemData) GenericIDataProcessor.populateObject(oReturnedLineItemData.getM_oSalesLineItemData());
			oSaleslLineItemData.returned(bAdd ? oReturnedLineItemData.getM_nQuantity() : -oReturnedLineItemData.getM_nQuantity());
		}
		Iterator<NonStockReturnedLineItemData> oNSReturnedLineItems = m_oNonStockReturnedLineItems.iterator();
		while (oNSReturnedLineItems.hasNext())
		{
			NonStockReturnedLineItemData oNonStockReturnedLineItemData = oNSReturnedLineItems.next();
			NonStockSalesLineItemData oNStockSalesLineItemData = (NonStockSalesLineItemData) GenericIDataProcessor.populateObject(oNonStockReturnedLineItemData.getM_oNonStockSalesLineItemData());
			oNStockSalesLineItemData.returned(bAdd ? oNonStockReturnedLineItemData.getM_nQuantity() : -oNonStockReturnedLineItemData.getM_nQuantity());
		}
	}

	@Override
	public String generateXML() 
	{
		String strReturnedDataXml = "";
		m_oLogger.info("generateXML ");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "ReturnedData");
			addChild (oXmlDocument, oRootElement, "m_nId", m_nId);
			addChild (oXmlDocument, oRootElement, "m_strCreditNoteNumber", m_strCreditNoteNumber);
			addChild (oXmlDocument, oRootElement, "m_strDate", GenericIDataProcessor.getClientCompatibleFormat(m_dCreatedOn));
			Document oClientDoc = getXmlDocument (m_oClientData.generateXML());
			Node oClientNode = oXmlDocument.importNode (oClientDoc.getFirstChild (), true);
			oRootElement.appendChild (oClientNode);
			Document oReturnedLineItemDoc = getXmlDocument ("<m_oReturnedLineItems>" + buildReturnedLineItem () + "</m_oReturnedLineItems>");
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

	@SuppressWarnings("unchecked")
	private String buildReturnedLineItem () 
	{
		String strXML = "";
		Object [] arrReturnedLineitems = m_oReturnedLineItems.toArray();
		Object [] arrNSReturnedLineitems = m_oNonStockReturnedLineItems.toArray();
		List arrLineItem = new ArrayList(Arrays.asList(arrReturnedLineitems));
		arrLineItem.addAll(Arrays.asList(arrNSReturnedLineitems));
		Object[] arrReturnedLine = arrLineItem.toArray();
		float nTotal = 0;
		for (int nIndex = 0; nIndex < arrReturnedLine.length; nIndex ++)
		{
			if (isReturnedLineItemClass(arrReturnedLine[nIndex]))
			{
				ReturnedLineItemData oReturnedLineItemData = (ReturnedLineItemData) arrReturnedLine [nIndex];
				strXML += oReturnedLineItemData.generateXML();
				nTotal += oReturnedLineItemData.getM_nQuantity() * (oReturnedLineItemData.getM_oSalesLineItemData().getM_nPrice() - (oReturnedLineItemData.getM_oSalesLineItemData().getM_nPrice() * oReturnedLineItemData.getM_oSalesLineItemData().getM_nDiscount()/100) + (oReturnedLineItemData.getM_oSalesLineItemData().getM_nPrice() * oReturnedLineItemData.getM_oSalesLineItemData().getM_nTax()/100));
			}
			else
			{
				NonStockReturnedLineItemData oNSReturnedLineItemData = (NonStockReturnedLineItemData) arrReturnedLine [nIndex];
				strXML += oNSReturnedLineItemData.generateXML();
				nTotal += oNSReturnedLineItemData.getM_nQuantity() * (oNSReturnedLineItemData.getM_oNonStockSalesLineItemData().getM_nPrice() - (oNSReturnedLineItemData.getM_oNonStockSalesLineItemData().getM_nPrice() * oNSReturnedLineItemData.getM_oNonStockSalesLineItemData().getM_nDiscount()/100) + (oNSReturnedLineItemData.getM_oNonStockSalesLineItemData().getM_nPrice() * oNSReturnedLineItemData.getM_oNonStockSalesLineItemData().getM_nTax()/100));
			}
		}
		strXML += "<grandTotal>"+ nTotal +"</grandTotal>";
		return strXML;
	}

	private boolean isReturnedLineItemClass (Object oLineItem) 
	{
		return oLineItem.getClass().toString().equalsIgnoreCase("class com.techmust.inventory.returned.ReturnedLineItemData");
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_oClientData != null && m_oClientData.getM_strCompanyName().trim() != "")
			oClientDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(),MatchMode.ANYWHERE));
		if (m_strCreditNoteNumber != null && !m_strCreditNoteNumber.trim().isEmpty())
			oCriteria.add(Restrictions.ilike ("m_strCreditNoteNumber", m_strCreditNoteNumber.trim(),MatchMode.ANYWHERE));
		if (m_nCreatedBy > 0)
			oCriteria.add (Restrictions.eq ("m_nCreatedBy", m_nCreatedBy));
		if ((m_strDate != null && !m_strDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate,false), GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate, true)));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oCriteria.add(Restrictions.eq("m_oClientData", m_oClientData));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.between ("m_dCreatedOn", 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate, false), 
					GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_strFromDate)));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true)));
		if (strColumn.contains("m_strCompanyName"))
			oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
		else
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_dCreatedOn");
		return oCriteria;
	}

	public void prepareReturnData()
	{
		for (int nIndex = 0; nIndex < m_arrReturnedLineItemData.length; nIndex++)
			addReturnedLineItem (m_arrReturnedLineItemData[nIndex]);
		for (int nIndex = 0; nIndex < m_arrNonStockReturnedLineItemData.length; nIndex++)
			addNonStockReturnedLineItem (m_arrNonStockReturnedLineItemData[nIndex]);
	}

	public void addNonStockReturnedLineItem(NonStockReturnedLineItemData oNonStockReturnedLineItemData) 
	{
		oNonStockReturnedLineItemData.setM_oReturnedData(this);
		m_oNonStockReturnedLineItems.add(oNonStockReturnedLineItemData);
	}

	public void addReturnedLineItem(ReturnedLineItemData oReturnedLineItemData) 
	{
		oReturnedLineItemData.setM_oReturnedData(this);
		m_oReturnedLineItems.add(oReturnedLineItemData);
	}

	@JsonIgnore
	public ReturnedData getRemovedReturns () throws Exception 
	{
		ReturnedData oRemovedReturns = new ReturnedData ();
		ReturnedData oReturnedData = (ReturnedData) GenericIDataProcessor.populateObject(this);
		addRemovedReturnedLineItems (oReturnedData, oRemovedReturns);
		addRemovedNonStockReturnedLineItems (oReturnedData, oRemovedReturns);
		return oRemovedReturns;
	}

	private void addRemovedNonStockReturnedLineItems(ReturnedData oReturnedData, ReturnedData oRemovedReturns)
	{
		Iterator<NonStockReturnedLineItemData> oIterator =  oReturnedData.m_oNonStockReturnedLineItems.iterator();
		while (oIterator.hasNext())
		{
			NonStockReturnedLineItemData oNSReturnedLineItemData = oIterator.next();
			if (!isNSItemExists (oNSReturnedLineItemData))
				oRemovedReturns.m_oNonStockReturnedLineItems.add(oNSReturnedLineItemData);
		}
	}

	private boolean isNSItemExists(NonStockReturnedLineItemData oNSReturnedLineItemData) 
	{
		boolean bIsExist = false;
		Iterator<NonStockReturnedLineItemData> oIterator =  m_oNonStockReturnedLineItems.iterator();
		while (!bIsExist && oIterator.hasNext())
			bIsExist = (oNSReturnedLineItemData.getM_nId() == oIterator.next().getM_nId());
		return bIsExist;
	}

	private void addRemovedReturnedLineItems(ReturnedData oReturnedData, ReturnedData oRemovedReturns) 
	{
		Iterator<ReturnedLineItemData> oIterator =  oReturnedData.m_oReturnedLineItems.iterator();
		while (oIterator.hasNext())
		{
			ReturnedLineItemData oReturnedLineItemData = oIterator.next();
			if (!isItemExists (oReturnedLineItemData))
				oRemovedReturns.m_oReturnedLineItems.add(oReturnedLineItemData);
		}
	}

	private boolean isItemExists(ReturnedLineItemData oReturnedLineItemData) 
	{
		boolean bIsExist = false;
		Iterator<ReturnedLineItemData> oIterator =  m_oReturnedLineItems.iterator();
		while (!bIsExist && oIterator.hasNext())
			bIsExist = (oReturnedLineItemData.getM_nId() == oIterator.next().getM_nId());
		return bIsExist;
	}

	public void deleteReturns(ReturnedData oRemovedReturns) throws Exception 
	{
		deleteReturnedLineItems (oRemovedReturns);
		deleteNSReturnedLineItems (oRemovedReturns);
	}
	
	public void mergeClient(ClientData oClientData) throws Exception
	{
		ClientData oClient = new ClientData ();
		oClient.setM_nClientId(oClientData.getM_nClientId());
		setM_oClientData(oClient);
		updateObject();
	}

	private void deleteNSReturnedLineItems(ReturnedData oRemovedReturns) throws Exception 
	{
		Iterator<NonStockReturnedLineItemData> oNSReturnedLineItems = oRemovedReturns.m_oNonStockReturnedLineItems.iterator();
		while (oNSReturnedLineItems.hasNext())
			oNSReturnedLineItems.next().deleteObject();
	}

	private void deleteReturnedLineItems(ReturnedData oRemovedReturns) throws Exception 
	{
		Iterator<ReturnedLineItemData> oIterator =  oRemovedReturns.m_oReturnedLineItems.iterator();
		while (oIterator.hasNext())
			oIterator.next().deleteObject();
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
//		Criteria oClientDataCriteria = oCriteria.createCriteria ("m_oClientData");
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId)); 
//		if (m_oClientData != null && m_oClientData.getM_strCompanyName().trim() != "")
//			oClientDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oClientData.getM_strCompanyName().trim(),MatchMode.ANYWHERE));
		if (m_strCreditNoteNumber != null && !m_strCreditNoteNumber.trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCreditNoteNumber")), m_strCreditNoteNumber.trim().toLowerCase())); 
		if (m_nCreatedBy > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nCreatedBy"), m_nCreatedBy)); 
		if ((m_strDate != null && !m_strDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate,false).toString()), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strDate, true).toString())));
		if (m_oClientData != null && m_oClientData.getM_nClientId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oClientData"), m_oClientData)); 
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.between(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false).toString()), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
		if ((m_strFromDate != null && !m_strFromDate.isEmpty()) && m_strToDate.isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.ge(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strFromDate,false).toString())));
		if (m_strFromDate.isEmpty() && (m_strToDate != null && !m_strToDate.isEmpty()))
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.le(oRootObject.get("m_dCreatedOn"), oRootObject.get(GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_strToDate, true).toString())));
//		if (strColumn.contains("m_strCompanyName"))
//			oClientDataCriteria.addOrder(strOrderBy.equalsIgnoreCase("desc") ? Order.desc(strColumn) : Order.asc(strColumn));
//		else
//		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_dCreatedOn");
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