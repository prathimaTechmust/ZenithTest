package com.techmust.inventory.purchase;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.inventory.location.LocationData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class PurchaseToLocationData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	
	private int m_nPurchaseToLocationId;
	private PurchaseData m_oPurchaseData;
	private LocationData m_oLocationData;
	
	public void setM_nPurchaseToLocationId(int nPurchaseToLocationId) 
	{
		this.m_nPurchaseToLocationId = nPurchaseToLocationId;
	}

	public int getM_nPurchaseToLocationId() 
	{
		return m_nPurchaseToLocationId;
	}
	
	public void setM_oPurchaseData(PurchaseData oPurchaseData) 
	{
		this.m_oPurchaseData = oPurchaseData;
	}

	public PurchaseData getM_oPurchaseData() 
	{
		return m_oPurchaseData;
	}

	public void setM_oLocationData(LocationData oLocationData) 
	{
		this.m_oLocationData = oLocationData;
	}

	public LocationData getM_oLocationData() 
	{
		return m_oLocationData;
	}
	
	@Override
	public String generateXML() 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
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
