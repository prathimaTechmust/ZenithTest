package com.techmust.inventory.configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Entity
@Table(name = "tac61_configuraction")
public class ConfigurationData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac61_id")
	private int m_nId;
	@Column(name = "ac61_key")
	private String m_strKey;
	@Column(name ="ac61_int_value")
	private int m_nIntValue;
	@Column(name = "ac61_double_value")
	private double m_nDoubleValue;
	@Column(name = "ac61_string_value")
	private String m_strStringValue; 

	public ConfigurationData ()
	{
		m_nId = -1;
		m_strKey = "";
		m_nIntValue = -1;
		m_nDoubleValue = -1;
		m_strStringValue = "";
	}
	
	public int getM_nId () 
	{
		return m_nId;
	}

	public void setM_nId (int nId) 
	{
		m_nId = nId;
	}

	public String getM_strKey () 
	{
		return m_strKey;
	}

	public void setM_strKey (String strKey) 
	{
		m_strKey = strKey;
	}

	public int getM_nIntValue ()
	{
		return m_nIntValue;
	}

	public void setM_nIntValue (int nIntValue) 
	{
		m_nIntValue = nIntValue;
	}

	public double getM_nDoubleValue () 
	{
		return m_nDoubleValue;
	}

	public void setM_nDoubleValue (double nDoubleValue)
	{
		m_nDoubleValue = nDoubleValue;
	}

	public String getM_strStringValue ()
	{
		return m_strStringValue;
	}

	public void setM_strStringValue (String strStringValue)
	{
		m_strStringValue = strStringValue;
	}

	@Override
	public String generateXML () 
	{
		return null;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1) throws Exception  
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (!m_strKey.trim().isEmpty ())
			oCriteria.add (Restrictions.eq("m_strKey", m_strKey.trim()));
		return oCriteria;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId)); 
		if (!m_strKey.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strKey"), m_strKey)); 
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId));
		if (!m_strKey.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strKey"), m_strKey));
		return oConjunct;
	}
}
