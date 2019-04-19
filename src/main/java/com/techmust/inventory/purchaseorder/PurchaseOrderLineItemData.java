package com.techmust.inventory.purchaseorder;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.challan.ChallanData;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.VendorItemData;
import com.techmust.inventory.purchase.NonStockPurchaseLineItem;
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.inventory.sales.ClientArticleData;
import com.techmust.inventory.sales.SalesLineItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;
@Entity
@Table(name = "tac15_Purchaseorder_lineitem")
public class PurchaseOrderLineItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac15_id")
	private int m_nId; //
	@Column(name = "ac15_description")
	private String m_strDesc; //
	@Column(name = "ac15_quantity")
	private float m_nQty; //
	@Column(name = "ac15_unit")
	private String m_strUnit;
	@Column(name = "ac15_price")
	private float m_nPrice; //
	@Column(name = "ac15_discount")
	@ColumnDefault("0")
	private float m_nDiscount; //
	@Column(name = "ac15_tax")
	private float m_nTax; //
	@Column(name = "ac15_shipped_qty")
	@ColumnDefault("0")
	private float m_nShippedQty; //
	@Column(name = "ac15_challan_qty")
	@ColumnDefault("0")
	private float m_nChallanQty;
	@Column(name = "ac15_invoice_qty")
	@ColumnDefault("0")
	private float m_nInvoiceQty; 
	@Column(name = "ac15_returned_qty")
	@ColumnDefault("0")
	private float m_nReturnedQty;
	@Column(name = "ac15_tax_name")
	private String m_strTaxName;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ac15_puchaseOrder_id",nullable = false)
	private PurchaseOrderData m_oPurchaseOrderData;
	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="ac17_stocklineitem_id")
	private Set<PurchaseOrderStockLineItemData> m_oPurchaseOrderStockLineItems; //
	@Transient
	public PurchaseOrderStockLineItemData [] m_arrPurchaseOrderStockLineItems;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "tac17_po_lineitem_challans",joinColumns = {@JoinColumn(name = "ac17_purchaseorderlineitem_id")},inverseJoinColumns = {@JoinColumn(name = "ac17_challan_id")})
	private Set<ChallanData> m_oChallans;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "tac18_po_lineitem_invoices",joinColumns = {@JoinColumn(name = "ac18_purchaseorderlineitem_id")},inverseJoinColumns = {@JoinColumn(name = "ac18_invoice_id")})
	private Set<InvoiceData> m_oInvoices;
	@Column(name = "ac15_Created_by")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac15_created_on")
	private Date m_dCreatedOn; 
	@Column(name = "ac15_purchased_qty")
	@ColumnDefault("0")
	private float m_nPurchasedQty;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "tac22_po_lineitem_purchases",joinColumns = {@JoinColumn(name = "ac22_purchaseorderlineitem_id")},inverseJoinColumns = {@JoinColumn(name = "ac22_purchase_id")})
	private Set<PurchaseData> m_oPurchases;
	
	@Column(name = "ac15_serial_number")
	@ColumnDefault("0")
	private int m_nSerialNumber;
	@Transient
	private float m_nShipQty; //
	@Transient
	private String m_strArticleNumber; //

	
	public PurchaseOrderLineItemData ()
	{
		m_nId = -1;
		m_strDesc = "";
		m_nQty = 0;
		m_nPrice = 0;
		m_nDiscount = 0;
		m_nTax = 0;
		m_nShippedQty = 0;
		m_nChallanQty = 0;
		m_nInvoiceQty = 0;
		setM_strArticleNumber("");
		m_strTaxName = "TAX";
		m_oPurchaseOrderData = new PurchaseOrderData();
		m_oPurchaseOrderStockLineItems = new HashSet<PurchaseOrderStockLineItemData> ();
		m_oChallans = new HashSet<ChallanData> ();
		m_oInvoices = new HashSet<InvoiceData> ();
		m_oPurchases = new HashSet<PurchaseData> ();
		setM_nCreatedBy(-1);
		setM_dCreatedOn(Calendar.getInstance().getTime()); 
		m_nPurchasedQty = 0;
		m_nReturnedQty = 0;
		m_nSerialNumber = 0;
	}

	public int getM_nId () 
	{
		return m_nId;
	}

	public void setM_nId (int nId) 
	{
		m_nId = nId;
	}

	public String getM_strDesc () 
	{
		return m_strDesc;
	}

	public void setM_strDesc (String strDesc) 
	{
		m_strDesc = strDesc;
	}

	public float getM_nQty () 
	{
		return m_nQty;
	}

	public void setM_nQty (float nQty) 
	{
		m_nQty = nQty;
	}

	public void setM_strUnit(String strUnit) 
	{
		this.m_strUnit = strUnit;
	}

	public String getM_strUnit() 
	{
		return m_strUnit;
	}

	public float getM_nPrice () 
	{
		return m_nPrice;
	}

	public void setM_nPrice (float nPrice) 
	{
		m_nPrice = nPrice;
	}

	public float getM_nTax () 
	{
		return m_nTax;
	}

	public void setM_nTax (float nTax) 
	{
		m_nTax = nTax;
	}

	public PurchaseOrderData getM_oPurchaseOrderData () 
	{
		return m_oPurchaseOrderData;
	}

	public void setM_oPurchaseOrderData (PurchaseOrderData oPurchaseOrderData) 
	{
		m_oPurchaseOrderData = oPurchaseOrderData;
	}

	public Set<PurchaseOrderStockLineItemData> getM_oPurchaseOrderStockLineItems () 
	{
		return m_oPurchaseOrderStockLineItems;
	}

	public void setM_oPurchaseOrderStockLineItems (Set<PurchaseOrderStockLineItemData> oPurchaseOrderStockLineItems) 
	{
		m_oPurchaseOrderStockLineItems = oPurchaseOrderStockLineItems;
	}
	
	public void setM_nShippedQty(float nShippedQty) 
	{
		this.m_nShippedQty = nShippedQty;
	}

	public float getM_nShippedQty() 
	{
		return m_nShippedQty;
	}
	
	public void setM_nShipQty(float nShipQty)
	{
		this.m_nShipQty = nShipQty;
	}

	public float getM_nShipQty() 
	{
		return m_nShipQty;
	}

	public void shipped (float nQty)
	{
		m_nShippedQty += nQty;
	}
	
	public float getM_nPurchasedQty()
	{
		return m_nPurchasedQty;
	}

	public void setM_nPurchasedQty(float nPurchasedQty) 
	{
		m_nPurchasedQty = nPurchasedQty;
	}

	public Set<PurchaseData> getM_oPurchases() 
	{
		return m_oPurchases;
	}

	public void setM_oPurchases(Set<PurchaseData> oPurchases) 
	{
		m_oPurchases = oPurchases;
	}

	public void addLineItem (PurchaseOrderStockLineItemData oPurchaseOrderStockLineItemData) 
	{
		oPurchaseOrderStockLineItemData.setM_oPurchaseOrderLineItemData(this);
		m_oPurchaseOrderStockLineItems.add(oPurchaseOrderStockLineItemData);
	}

	public int getM_nSerialNumber () 
	{
		return m_nSerialNumber;
	}

	public void setM_nSerialNumber (int nSerialNumber) 
	{
		m_nSerialNumber = nSerialNumber;
	}

	@Override
	public String generateXML ()
	{
		return null;
	}

	@Override
	protected Criteria listCriteria (Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if (getM_nId()> 0)
			oCriteria.add (Restrictions.eq ("m_nId", getM_nId()));
		if (m_oPurchaseOrderData != null && m_oPurchaseOrderData.getM_nPurchaseOrderId() > 0)
			oCriteria.add (Restrictions.eq ("m_oPurchaseOrderData", m_oPurchaseOrderData));
		if (m_oPurchaseOrderData != null && m_oPurchaseOrderData.isM_bIsForVendorOrder())
		{
			Criteria oPODataCriteria = oCriteria.createCriteria("m_oPurchaseOrderStockLineItems");
			DetachedCriteria oDetachedCriteria = DetachedCriteria.forClass(VendorItemData.class).setProjection(Property.forName("m_oItemData"));
			oDetachedCriteria.add(Restrictions.eq("m_oVendorData.m_nClientId", m_oPurchaseOrderData.getM_nVendorId()));
			oPODataCriteria.add(Subqueries.propertyIn("m_oItemData", oDetachedCriteria));
		}
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nId");
		return oCriteria;
	}
	
	public int getM_nCreatedBy()
    {
		return m_nCreatedBy;
    }

	public void setM_nCreatedBy(int nCreatedBy)
    {
	    m_nCreatedBy = nCreatedBy;
    }

	public void setM_nChallanQty(float m_nChallanQty)
    {
	    this.m_nChallanQty = m_nChallanQty;
    }

	public float getM_nChallanQty()
    {
	    return m_nChallanQty;
    }

	public void setM_nInvoiceQty(float m_nInvoiceQty)
    {
	    this.m_nInvoiceQty = m_nInvoiceQty;
    }

	public float getM_nInvoiceQty()
    {
	    return m_nInvoiceQty;
    }

	public void setM_dCreatedOn(Date oCreatedOn)
    {
	    this.m_dCreatedOn = oCreatedOn;
    }

	public Date getM_dCreatedOn()
    {
	    return m_dCreatedOn;
    }

	public void setM_oChallans(Set<ChallanData> oChallans)
    {
	    this.m_oChallans = oChallans;
    }

	public Set<ChallanData> getM_oChallans()
    {
	    return m_oChallans;
    }

	public void setM_strArticleNumber(String strArticleNumber)
    {
	    this.m_strArticleNumber = strArticleNumber;
    }

	public String getM_strArticleNumber()
    {
	    return m_strArticleNumber;
    }
	
	public void setM_strTaxName(String strTaxName) 
	{
		this.m_strTaxName = strTaxName;
	}

	public String getM_strTaxName() 
	{
		return m_strTaxName;
	}
	
	public void setM_nDiscount(float nDiscount)
    {
	    this.m_nDiscount = nDiscount;
    }

	public float getM_nDiscount()
    {
	    return m_nDiscount;
    }

	public void setM_oInvoices(Set<InvoiceData> oInvoices)
    {
	    this.m_oInvoices = oInvoices;
    }

	public Set<InvoiceData> getM_oInvoices()
    {
	    return m_oInvoices;
    }

	public float getM_nReturnedQty()
    {
    	return m_nReturnedQty;
    }

	public void setM_nReturnedQty(float nReturnedQty)
    {
    	m_nReturnedQty = nReturnedQty;
    }

	public void challaned(float nShipQty)
    {
	    m_nChallanQty += nShipQty;
    }

	public void returned (float nQuantity) throws Exception
	{
		m_nReturnedQty += nQuantity;
		updateObject();
	}
	
	public void invoiced(float nShipQty)
    {
	    m_nInvoiceQty += nShipQty;
    }

	public void addChallan(ChallanData oChallanData) throws Exception
    {
		if (m_nShipQty > 0)
		{
			challaned (m_nShipQty);
			shipped (m_nShipQty);
			issued (m_nShipQty);
			m_oChallans.add(oChallanData);
			updateObject();
		}
    }
	
	public void addInvoice (InvoiceData oInvoiceData)
    {
		m_oInvoices.add(oInvoiceData);
    }

	public HashMap<Integer, ChallanData> getUnbilledChallans(
            HashMap<Integer, ChallanData> oUnbilledChallans)
    {
		Iterator<ChallanData> oChallans = m_oChallans.iterator();
		while (oChallans.hasNext())
		{
			ChallanData oChallan = oChallans.next();
			Integer oChallanId = new Integer (oChallan.getM_nChallanId());
			if ((oChallan.getM_oInvoiceData() == null || (oChallan.getM_oInvoiceData() != null && oChallan.getM_oInvoiceData().getM_nInvoiceId() <= 0)) && !oUnbilledChallans.containsKey(oChallanId))
				oUnbilledChallans.put(oChallanId, oChallan);
		}
		return oUnbilledChallans;
    }
	
	public HashMap<Integer, ChallanData> getChallans(HashMap<Integer, ChallanData> arrChallans) 
	{
		Iterator<ChallanData> oChallans = m_oChallans.iterator();
		while (oChallans.hasNext())
		{
			ChallanData oChallan = oChallans.next();
			Integer oChallanId = new Integer (oChallan.getM_nChallanId());
			if (!arrChallans.containsKey(oChallanId))
			{
				oChallan.setM_strDate(GenericIDataProcessor.getClientCompatibleFormat(oChallan.getM_dCreatedOn()));
				arrChallans.put(oChallanId, oChallan);
			}
		}
		return arrChallans;
	}

	public HashMap<Integer, InvoiceData> getInvoices(HashMap<Integer, InvoiceData> arrInvoices) 
	{
		Iterator<InvoiceData> oInvoices = m_oInvoices.iterator();
		while (oInvoices.hasNext())
		{
			InvoiceData oInvoice = oInvoices.next();
			Integer oInvoiceId = new Integer (oInvoice.getM_nInvoiceId());
			if (!arrInvoices.containsKey(oInvoiceId))
			{
				oInvoice.setM_strDate(GenericIDataProcessor.getClientCompatibleFormat(oInvoice.getM_dCreatedOn()));
				arrInvoices.put(oInvoiceId, oInvoice);
			}
		}
		return arrInvoices;
	}
	
	public HashMap<Integer, PurchaseData> getPurchases(HashMap<Integer, PurchaseData> arrPurchases) 
	{
		Iterator<PurchaseData> oPurchases = m_oPurchases.iterator();
		while (oPurchases.hasNext())
		{
			PurchaseData oPurchaseData = oPurchases.next();
			Integer oPurchaseId = new Integer (oPurchaseData.getM_nId());
			if (!arrPurchases.containsKey(oPurchaseId))
			{
				oPurchaseData.setM_strDate(GenericIDataProcessor.getClientCompatibleFormat(oPurchaseData.getM_dCreatedOn()));
				arrPurchases.put(oPurchaseId, oPurchaseData);
			}
		}
		return arrPurchases;
	}
	
	public void prepareForUpdate(PurchaseOrderData oPOData) throws Exception
    {
		try
		{
			if (this.getM_nId() > 0)
			{
				PurchaseOrderLineItemData oPOLineItem = (PurchaseOrderLineItemData) GenericIDataProcessor.populateObject(this);
				m_nChallanQty = oPOLineItem.m_nChallanQty;
				m_nInvoiceQty = oPOLineItem.m_nInvoiceQty;
				m_nPurchasedQty = oPOLineItem.m_nPurchasedQty;
				m_nReturnedQty = oPOLineItem.m_nReturnedQty;
				m_oChallans = oPOLineItem.m_oChallans;
				m_oInvoices = oPOLineItem.m_oInvoices;
				m_oPurchases = oPOLineItem.m_oPurchases;
			}
		}
		catch (Exception oException)
		{
			// new POLineItem added which does not have an id.
		}
		m_nCreatedBy = oPOData.getM_nCreatedBy();
		m_oPurchaseOrderData = oPOData;
		m_oPurchaseOrderStockLineItems = new HashSet<PurchaseOrderStockLineItemData> (); 
		for (int nIndex = 0 ; m_arrPurchaseOrderStockLineItems != null && nIndex < m_arrPurchaseOrderStockLineItems.length; nIndex++ )
		{
			PurchaseOrderStockLineItemData oPOStockLineItem = m_arrPurchaseOrderStockLineItems[nIndex];
			ClientArticleData oItemData = ClientArticleData.getInstance(m_oPurchaseOrderData.getM_oClientData(), oPOStockLineItem.getM_oItemData().getM_strArticleNumber(), m_oPurchaseOrderData.getM_oUserCredentialsData());
			ItemData oInventoryItem = new ItemData ((ItemData)oItemData);
			if (oInventoryItem != null)
			{
				ItemData oLineItemData = new ItemData (); 
				oLineItemData.setM_nItemId(oInventoryItem.getM_nItemId());
				oPOStockLineItem.setM_oItemData(oLineItemData);
				oPOStockLineItem.setM_oCreatedBy(oPOData.getM_nCreatedBy());
				addLineItem(oPOStockLineItem);
			}
		}
    }

	public void invoiced(InvoiceData oInvoiceData) throws Exception
    {
		Iterator<ChallanData> oChallans = m_oChallans.iterator();
		while (oChallans.hasNext())
		{
			ChallanData oChallan = oChallans.next();
			if (oInvoiceData.contains(oChallan.getM_oSalesData()))
			{
				invoiced (oChallan.getQuantity (this));
				addInvoice (oInvoiceData);
			}
		}
    }

	public ItemData getStockLineItem() throws Exception
    {
		ItemData oItemData = null;
		Iterator<PurchaseOrderStockLineItemData> oStockLineItems = m_oPurchaseOrderStockLineItems.iterator();
		if (oStockLineItems.hasNext())
			oItemData = (ItemData) GenericIDataProcessor.populateObject(oStockLineItems.next().getM_oItemData());
	    return oItemData;
    }

	public void updateLineItem(InvoiceData oInvoice) throws Exception
    {
		if (m_nShipQty > 0)
		{
			shipped (m_nShipQty);
			issued (m_nShipQty);
			invoiced (m_nShipQty);
			addInvoice (oInvoice);
		}
    }

	public boolean isDelivered()
    {
	    return m_nQty == m_nInvoiceQty;
    }
	
	private void issued(float nShipQty) throws Exception
    {
		ItemData oItemData = getStockLineItem ();
		if (oItemData != null)
		{
			if (oItemData.getM_oChildItems().size() > 0)
				oItemData.updateIssuedForChildItems(nShipQty);
			else
			{
				oItemData.issued(nShipQty);
				oItemData.updateObject();
			}
		}
    }

	public void purchased(float nQty) 
	{
		m_nPurchasedQty += nQty;
	}

	public void addPurchase (NonStockPurchaseLineItem oNSPurchaseLineItem) throws Exception
	{
		purchased (oNSPurchaseLineItem.getM_nQuantity());
		m_oPurchases.add(oNSPurchaseLineItem.getM_oPurchaseData());	
		updateObject();
	}

	private void removeOldPurchase(NonStockPurchaseLineItem oPurchaseLineItem) throws Exception 
	{
		NonStockPurchaseLineItem oLineItem = (NonStockPurchaseLineItem) GenericIDataProcessor.populateObject(oPurchaseLineItem);
		if(oLineItem != null)
		{
			purchased(-oLineItem.getM_nQuantity());
			updateObject();
		}
	}

	public void removePurchaseQty(NonStockPurchaseLineItem oNSPurchaseLineItem) throws Exception 
	{
		removeOldPurchase(oNSPurchaseLineItem);
		updateObject();
	}

	public void updatePurchaseQty (NonStockPurchaseLineItem oNSPurchaseLineItem) throws Exception 
	{
		purchased (oNSPurchaseLineItem.getM_nQuantity());
		updateObject();
	}

	public void removePurchaseQty(float nQty) throws Exception 
	{
		purchased (-nQty);
		updateObject();
	}

	public void returned(SalesLineItemData oSLIData, float nQuantity)
    {
		try
		{
			ItemData oItemData = getStockLineItem ();
			if (oSLIData.getM_oItemData().getM_nItemId() == oItemData.getM_nItemId())
				returned (nQuantity);
		}
		catch (Exception oException)
		{
			
		}
    }

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void customizeStockLineItems() 
	{
		Iterator<PurchaseOrderStockLineItemData> oIterator = m_oPurchaseOrderStockLineItems.iterator();
		while (oIterator.hasNext())
		{
			PurchaseOrderStockLineItemData POStockLineItemData = oIterator.next();
			POStockLineItemData.customizeItemData ();
		}
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (getM_nId()> 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId)); 
		if (m_oPurchaseOrderData != null && m_oPurchaseOrderData.getM_nPurchaseOrderId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oPurchaseOrderData"), m_oPurchaseOrderData)); 
/*
		if (m_oPurchaseOrderData != null && m_oPurchaseOrderData.isM_bIsForVendorOrder())
		{
			Criteria oPODataCriteria = oCriteria.createCriteria("m_oPurchaseOrderStockLineItems");
			DetachedCriteria oDetachedCriteria = DetachedCriteria.forClass(VendorItemData.class).setProjection(Property.forName("m_oItemData"));
			oDetachedCriteria.add(Restrictions.eq("m_oVendorData.m_nClientId", m_oPurchaseOrderData.getM_nVendorId()));
			oPODataCriteria.add(Subqueries.propertyIn("m_oItemData", oDetachedCriteria));
		}
		addSortByCondition (oCriteria, strColumn, strOrderBy, "m_nId");
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
