package com.techmust.generic.data;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.generic.util.HibernateUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class MasterData extends GenericData 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria,
			CriteriaBuilder oBuilder)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateXML()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericData getInstanceData(String strXML, UserInformationData oCredentials) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public EntityManager _getEntityManager()
	{
		// TODO Auto-generated method stub
		return HibernateUtil.getMasterEntityManager();
	}

}
