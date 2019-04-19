package com.techmust.usermanagement.userinfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.MasterData;

@SuppressWarnings("serial")
public class UserSelectedData extends MasterData 
{
	public UserInformationData [] m_arrSelectedUsersData;

	@Override
	public String generateXML () 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria (Criteria criteria, String strColumn, String strOrderBy) 
	{
		return null;
	}

	@Override
	public GenericData getInstanceData(String strXML,
			UserInformationData oCredentials) {
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