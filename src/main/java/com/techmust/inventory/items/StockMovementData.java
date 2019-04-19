package com.techmust.inventory.items;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class StockMovementData extends TenantData
{
    private static final long serialVersionUID = 1L;
	private ItemData m_oItemData;
	private float m_nReceivedQuantity;
	private float m_nIssuedQuantity;
	
	public StockMovementData ()
	{
		m_oItemData = new ItemData ();
		setM_nReceivedQuantity(0);
		setM_nIssuedQuantity(0);
	}
	
	public void setM_oItemData(ItemData m_oItemData)
    {
	    this.m_oItemData = m_oItemData;
    }

	public ItemData getM_oItemData()
    {
	    return m_oItemData;
    }

	public void setM_nReceivedQuantity(float nReceivedQuantity) 
	{
		this.m_nReceivedQuantity = nReceivedQuantity;
	}

	public float getM_nReceivedQuantity() 
	{
		return m_nReceivedQuantity;
	}

	public void setM_nIssuedQuantity(float m_nIssuedQuantity) {
		this.m_nIssuedQuantity = m_nIssuedQuantity;
	}

	public float getM_nIssuedQuantity() {
		return m_nIssuedQuantity;
	}
	@Override
	public String generateXML()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria arg0, String arg1, String arg2)
	{
		// TODO Auto-generated method stub
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
