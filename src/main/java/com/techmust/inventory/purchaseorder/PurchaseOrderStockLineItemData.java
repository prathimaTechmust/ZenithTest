package com.techmust.inventory.purchaseorder;

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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.Criteria;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.VendorItemData;
import com.techmust.inventory.sales.CustomizedItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac16_purchaseorder_stockLineItem")
public class PurchaseOrderStockLineItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac16_purchaseorder_stocklineitem_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name = "ac16_item_id")
	@ColumnDefault("-1")
	private ItemData m_oItemData;
	@Column(name = "ac16_quantity")
	private float m_nQuantity;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "ac16_purchaseorderlineitem_id")
	@ColumnDefault("-1")
	private PurchaseOrderLineItemData m_oPurchaseOrderLineItemData;
	
	@Column(name = "ac16_Created_by")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ac16_created_on")
	private Date m_dCreatedOn;	
	
	public PurchaseOrderStockLineItemData ()
	{
		m_nId = -1;
		m_oItemData = new ItemData ();
		m_nQuantity = 0;
		m_oPurchaseOrderLineItemData = new PurchaseOrderLineItemData();
		setM_dCreatedOn(Calendar.getInstance().getTime()); 
	}

	public int getM_nId() 
	{
		return m_nId;
	}

	public void setM_nId(int nId) 
	{
		m_nId = nId;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_oItemData(ItemData oItemData) 
	{
		m_oItemData = oItemData;
	}

	public float getM_nQuantity() 
	{
		return m_nQuantity;
	}

	public void setM_nQuantity(float nQuantity) 
	{
		m_nQuantity = nQuantity;
	}

	public PurchaseOrderLineItemData getM_oPurchaseOrderLineItemData() 
	{
		return m_oPurchaseOrderLineItemData;
	}

	public void setM_oPurchaseOrderLineItemData(PurchaseOrderLineItemData oPurchaseOrderLineItemData) 
	{
		m_oPurchaseOrderLineItemData = oPurchaseOrderLineItemData;
	}

	public void setM_oCreatedBy(int m_nCreatedBy)
    {
	    this.m_nCreatedBy = m_nCreatedBy;
    }

	public int getM_nCreatedBy()
    {
	    return m_nCreatedBy;
    }

	public void setM_dCreatedOn(Date m_dCreatedOn)
    {
	    this.m_dCreatedOn = m_dCreatedOn;
    }

	public Date getM_dCreatedOn()
    {
	    return m_dCreatedOn;
    }

	@Override
	public String generateXML() 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		return oCriteria;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) 
	{
		return null;
	}

	public void customizeItemData() 
	{
		try 
		{
			CustomizedItemData oCustomItemData = null;
			int nItemId = m_oItemData != null ? m_oItemData.getM_nItemId() : -1;
			int nClientId = m_oPurchaseOrderLineItemData != null ? m_oPurchaseOrderLineItemData.getM_oPurchaseOrderData().getM_oClientData().getM_nClientId() : -1;
			if(nItemId > 0 && nClientId > 0)
				oCustomItemData = CustomizedItemData.getItemInstance("", nClientId, nItemId);
			if(oCustomItemData != null)
			{
				m_oItemData.setM_strArticleNumber(oCustomItemData.getM_strClientArticleNumber());
				m_oItemData.setM_strItemName(CustomizedItemData.getArticleDescription(oCustomItemData.getM_strClientArticleDescription(), 0, m_oItemData.getM_strItemName()));
				m_oItemData.setM_strDetail(CustomizedItemData.getArticleDescription(oCustomItemData.getM_strClientArticleDescription(), 1, m_oItemData.getM_strDetail ()));
			}
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("customizeItemData - oException" + oException);
		}
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}
}
