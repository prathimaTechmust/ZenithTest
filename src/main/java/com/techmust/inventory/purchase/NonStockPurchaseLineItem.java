package com.techmust.inventory.purchase;

import java.util.Calendar;
import java.util.Date;

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

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.Restrictions;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.inventory.purchaseorder.PurchaseOrderLineItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac21_nonstock_purchase_lineitems")
public class NonStockPurchaseLineItem extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac21_id")
	private int m_nLineItemId;
	@Column(name = "ac21_desc")
	private String m_strArticleDescription;
	@Column(name ="ac21_quantity")
	private float m_nQuantity;
	@Column(name = "ac21_price")
	private float m_nPrice;
	@Column(name ="ac21_discount")
	@ColumnDefault("0")
	private float m_nDiscount;
	@Column(name ="ac21_excise")
	@ColumnDefault("0")
	private float m_nExcise;
	@Column(name = "ac21_tax")
	@ColumnDefault("0")
	private float m_nTax;
	@Column(name = "ac21_tax_name")
	private String m_strTaxName;
	@Column(name = "ac21_other_charges")
	@ColumnDefault("0")
	private float m_nOtherCharges;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac21_created_on")
	private Date m_dCreatedOn;
	@Column(name = "ac21_Created_by")
	private int m_nCreatedBy;
	@ManyToOne
	@JoinColumn(name = "ac21_purchaseId",nullable = false)
	private PurchaseData m_oPurchaseData;
	@ManyToOne
	@JoinColumn(name ="ac21_polineitem_Id")
	private PurchaseOrderLineItemData m_oPOLineItem;
	@Transient
	private String m_strFromDate;
	@Transient
	private String m_strToDate;
	@Transient
	private UserInformationData m_oUserCredentialsData;
	
	public NonStockPurchaseLineItem ()
	{
		m_nLineItemId = -1;
		m_nQuantity = 0;
		m_nPrice = 0;
		m_nDiscount = 0;
		m_nExcise = 0;
		m_nTax = 0;
		m_nOtherCharges = 0;
		setM_strTaxName("");
		setM_dCreatedOn(Calendar.getInstance().getTime());
	}
	
	public int getM_nLineItemId() 
	{
		return m_nLineItemId;
	}

	public void setM_nLineItemId(int nLineItemId) 
	{
		m_nLineItemId = nLineItemId;
	}

	public String getM_strArticleDescription() 
	{
		return m_strArticleDescription;
	}

	public void setM_strArticleDescription(String strArticleDescription) 
	{
		m_strArticleDescription = strArticleDescription;
	}

	public float getM_nQuantity() 
	{
		return m_nQuantity;
	}

	public void setM_nQuantity(float nQuantity) 
	{
		m_nQuantity = nQuantity;
	}

	public float getM_nPrice() 
	{
		return m_nPrice;
	}

	public void setM_nPrice(float nPrice) 
	{
		m_nPrice = nPrice;
	}

	public float getM_nDiscount() 
	{
		return m_nDiscount;
	}

	public void setM_nDiscount(float nDiscount) 
	{
		m_nDiscount = nDiscount;
	}

	public float getM_nExcise() 
	{
		return m_nExcise;
	}

	public void setM_nExcise(float nExcise) 
	{
		m_nExcise = nExcise;
	}

	public float getM_nTax() 
	{
		return m_nTax;
	}

	public void setM_nTax(float nTax) 
	{
		m_nTax = nTax;
	}

	public float getM_nOtherCharges() 
	{
		return m_nOtherCharges;
	}

	public void setM_nOtherCharges(float nOtherCharges) 
	{
		m_nOtherCharges = nOtherCharges;
	}

	public Date getM_dCreatedOn() 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public int getM_nCreatedBy() 
	{
		return m_nCreatedBy;
	}

	public void setM_nCreatedBy(int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public PurchaseData getM_oPurchaseData() 
	{
		return m_oPurchaseData;
	}

	public void setM_oPurchaseData(PurchaseData oPurchaseData) 
	{
		m_oPurchaseData = oPurchaseData;
	}


	public void setM_oPOLineItem(PurchaseOrderLineItemData oPOLineItem) 
	{
		this.m_oPOLineItem = oPOLineItem;
	}


	public PurchaseOrderLineItemData getM_oPOLineItem() 
	{
		return m_oPOLineItem;
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

	public UserInformationData getM_oUserCredentialsData() 
	{
		return m_oUserCredentialsData;
	}

	public void setM_oUserCredentialsData(UserInformationData oUserCredentialsData) 
	{
		m_oUserCredentialsData = oUserCredentialsData;
	}

	public void setM_strTaxName(String strTaxName) 
	{
		this.m_strTaxName = strTaxName;
	}

	public String getM_strTaxName()
	{
		return m_strTaxName;
	}

	@Override
	public String generateXML() 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		if (getM_nLineItemId ()> 0)
			oCriteria.add (Restrictions.eq ("m_nLineItemId", getM_nLineItemId ()));
		return oCriteria;
	}

	public void addPurchase() throws Exception
    {
		PurchaseOrderLineItemData oPOLineItem = getM_oPOLineItem();
		if (oPOLineItem != null)
		{
			oPOLineItem = (PurchaseOrderLineItemData) GenericIDataProcessor.populateObject (oPOLineItem);
			oPOLineItem.addPurchase(this);
		}	    
    }

	public void removePurchaseQty() throws Exception 
	{
		PurchaseOrderLineItemData oPOLineItem = getM_oPOLineItem();
		if (oPOLineItem != null)
		{
			oPOLineItem = (PurchaseOrderLineItemData) GenericIDataProcessor.populateObject (oPOLineItem);
			oPOLineItem.removePurchaseQty(this);
		}	    
	}

	public void updatePurchaseQty() throws Exception 
	{
		PurchaseOrderLineItemData oPOLineItem = getM_oPOLineItem();
		if (oPOLineItem != null)
		{
			oPOLineItem = (PurchaseOrderLineItemData) GenericIDataProcessor.populateObject (oPOLineItem);
			oPOLineItem.updatePurchaseQty(this);
		}	    
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
		if (getM_nLineItemId ()> 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nLineItemId"), m_nLineItemId)); 
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nLineItemId"), m_nLineItemId));
		return oConjunct;
	}
}
