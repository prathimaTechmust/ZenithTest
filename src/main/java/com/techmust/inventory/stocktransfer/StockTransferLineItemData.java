package com.techmust.inventory.stocktransfer;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.inventory.items.ItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class StockTransferLineItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	private String m_strArticleNumber;
	private ItemData m_oItemData;
	private Date m_dCreatedOn;
	private float m_nQuantity;
	private UserInformationData m_oTransferredBy;
	
	public StockTransferLineItemData ()
	{
		m_strArticleNumber= "";
		m_oItemData = new ItemData ();
		setM_dCreatedOn(Calendar.getInstance().getTime());
		m_nQuantity = 0;
	}
	
	public String getM_strArticleNumber() 
	{
		return m_strArticleNumber;
	}

	public void setM_strArticleNumber(String strArticleNumber) 
	{
		m_strArticleNumber = strArticleNumber;
	}

	public ItemData getM_oItemData()
	{
		return m_oItemData;
	}

	public void setM_oItemData(ItemData oItemData) 
	{
		m_oItemData = oItemData;
	}

	public Date getM_dCreatedOn() 
	{
		return m_dCreatedOn;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public float getM_nQuantity() 
	{
		return m_nQuantity;
	}

	public void setM_nQuantity(float nQuantity)
	{
		m_nQuantity = nQuantity;
	}


	public void setM_oTransferredBy(UserInformationData oTransferredBy) 
	{
		m_oTransferredBy = oTransferredBy;
	}

	public UserInformationData getM_oTransferredBy() 
	{
		return m_oTransferredBy;
	}

	@Override
	public String generateXML() 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria arg0, String arg1, String arg2) 
	{
		return null;
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
		return oConjunct;
		
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		return oConjunct;
	}
}
