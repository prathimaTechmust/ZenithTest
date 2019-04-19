package com.techmust.inventory.serial;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name = "tac13_serial_number")
public class SerialNumberData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac13_id")
	private int m_nSerialId;
	@Column(name = "ac13_serial_number")
	private int m_nSerialNumber;
	@Column(name = "ac13_prefix")
	private String m_strPrefix;
	@Column(name = "ac13_serial_type")
	@Enumerated(EnumType.ORDINAL)
	private SerialType m_nSerialType;
	@Transient
	private String m_strSerialTypeName;
	@Transient
	public SerialNumberData [] m_arrSerialNumbers;
	
	public SerialNumberData ()
	{
		setM_nSerialNumber(0);
		setM_nSerialType(SerialType.kInvalid);
	}
	
	static public SerialNumberData getInstance (SerialType nType) throws Exception
	{
		SerialNumberData oSerialnNumberData = new SerialNumberData ();
		oSerialnNumberData.setM_nSerialType(nType);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oSerialnNumberData = (SerialNumberData) oSerialnNumberData.list(oOrderBy).get(0);
		return oSerialnNumberData;
	}

	@Override
	public String generateXML() 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy)
	{
		if(m_nSerialType != SerialType.kInvalid)
			oCriteria.add (Restrictions.eq ("m_nSerialType", m_nSerialType));
		return oCriteria;
	}

	public void setM_nSerialId(int nSerialId) 
	{
		this.m_nSerialId = nSerialId;
	}

	public int getM_nSerialId() 
	{
		return m_nSerialId;
	}

	public void setM_nSerialNumber(int nSerialNumber)
	{
		this.m_nSerialNumber = nSerialNumber;
	}

	public int getM_nSerialNumber() 
	{
		return m_nSerialNumber;
	}

	public void setM_strPrefix(String strPrefix) 
	{
		this.m_strPrefix = strPrefix;
	}

	public String getM_strPrefix() 
	{
		return m_strPrefix;
	}

	public void setM_nSerialType(SerialType nSerialType) 
	{
		this.m_nSerialType = nSerialType;
	}

	public SerialType getM_nSerialType() 
	{
		return m_nSerialType;
	}

	public void setM_strSerialTypeName(String strSerialTypeName) 
	{
		this.m_strSerialTypeName = strSerialTypeName;
	}

	public String getM_strSerialTypeName() 
	{
		return m_strSerialTypeName;
	}
	
	public int generateNumber () throws Exception
	{
		try
		{
			m_nSerialNumber++;
			this.updateObject();
		}
		catch (Exception oException)
		{
			throw oException;
		}
		return m_nSerialNumber;
	}
	
	static public String generateSerialNumber (SerialType nType) throws Exception
	{
		SerialNumberData oSerialNumber = getInstance (nType);
		return oSerialNumber.m_strPrefix + String.valueOf(oSerialNumber.generateNumber());
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
		if(m_nSerialType != SerialType.kInvalid)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nSerialType"), m_nSerialType)); 
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nSerialId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nSerialId"), m_nSerialId)); 
		return oConjunct;
	}
}
