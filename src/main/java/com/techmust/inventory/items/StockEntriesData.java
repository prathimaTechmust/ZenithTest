package com.techmust.inventory.items;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@SuppressWarnings("serial")
public class StockEntriesData extends TenantData 
{
	public ItemData [] m_arrStockEntries;
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
