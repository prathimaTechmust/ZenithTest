package com.techmust.vendormanagement;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@Entity
@Table(name = "tcl10_vendor_counter")
public class VendorCounterData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cl10_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name = "cl10_Vendor_id")
	private VendorData m_oVendorData;
	@Column(name = "cl10_serial_number")
	private int m_nSerialNumber;
	@Column(name = "cl10_Prefix")
	private String m_strPrefix;
	@Column(name = "cl10_Suffix")
	private String m_strSuffix;
	@Column(name = "cl10_key")
	private String m_strKey; 
	@Column(name = "cl10_Created_by")
	private int m_nCreatedBy;
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cl10_created_on")
	private Date m_dCreatedOn;
	
	public VendorCounterData() 
	{
		m_nId = -1;
		m_oVendorData = new VendorData ();
		m_strPrefix = "";
		m_nSerialNumber = -1;
		m_strSuffix = "";
		m_strKey = "";
		m_dCreatedOn = Calendar.getInstance().getTime();
	}
	
	public void setM_nId(int nId) 
	{
		m_nId = nId;
	}

	public int getM_nId() 
	{
		return m_nId;
	}

	public VendorData getM_oVendorData() 
	{
		return m_oVendorData;
	}

	public void setM_oVendorData(VendorData oVendorData) 
	{
		m_oVendorData = oVendorData;
	}

	public int getM_nSerialNumber() 
	{
		return m_nSerialNumber;
	}

	public void setM_nSerialNumber(int nSerialNumber) 
	{
		m_nSerialNumber = nSerialNumber;
	}

	public String getM_strPrefix() 
	{
		return m_strPrefix;
	}

	public void setM_strPrefix(String strPrefix) 
	{
		m_strPrefix = strPrefix;
	}

	public String getM_strSuffix() 
	{
		return m_strSuffix;
	}

	public void setM_strSuffix(String strSuffix)
	{
		m_strSuffix = strSuffix;
	}

	public String getM_strKey() 
	{
		return m_strKey;
	}

	public void setM_strKey(String strKey) 
	{
		m_strKey = strKey;
	}

	public void setM_nCreatedBy(int nCreatedBy) 
	{
		m_nCreatedBy = nCreatedBy;
	}

	public int getM_nCreatedBy() 
	{
		return m_nCreatedBy;
	}

	public void setM_dCreatedOn(Date dCreatedOn) 
	{
		m_dCreatedOn = dCreatedOn;
	}

	public Date getM_dCreatedOn() 
	{
		return m_dCreatedOn;
	}

	public static String generateSerialNumber (int nVendorId) throws Exception
	{
		VendorCounterData oVendorCounterData = getInstance (nVendorId);
		return oVendorCounterData.m_strPrefix + String.valueOf(oVendorCounterData.generateNumber()) + oVendorCounterData.m_strSuffix;
	}
	
	public static VendorCounterData getInstance(int nVendorId)throws Exception 
	{
		VendorCounterData oVendorCounterData = new VendorCounterData ();
		oVendorCounterData.m_oVendorData.setM_nClientId(nVendorId);
		oVendorCounterData.setM_strKey("kInvoice");
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oVendorCounterData = (VendorCounterData) oVendorCounterData.list(oOrderBy).get(0);
		return oVendorCounterData;
	}
	
	public int generateNumber () throws Exception
	{
		int nPresentValue = m_nSerialNumber;
		try
		{
			m_nSerialNumber++;
			this.updateObject();
		}
		catch (Exception oException)
		{
			throw oException;
		}
		return nPresentValue;
	}

	@Override
	public String generateXML() 
	{
		return null;
	}

	@Override
	public GenericData getInstanceData(String arg0, UserInformationData arg1)throws Exception 
	{
		return null;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		Criteria oVendorDataCriteria = oCriteria.createCriteria ("m_oVendorData");
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oCriteria.add (Restrictions.eq ("m_oVendorData", m_oVendorData));
		if(m_oVendorData.getM_strCompanyName() != null && !m_oVendorData.getM_strCompanyName().trim().isEmpty())
			oVendorDataCriteria.add(Restrictions.ilike ("m_strCompanyName", m_oVendorData.getM_strCompanyName().trim(), MatchMode.ANYWHERE));
		if (m_strKey != null && !m_strKey.trim().isEmpty ())
			oCriteria.add (Restrictions.eq ("m_strKey", m_strKey.trim()));
		if (m_strPrefix != null && !m_strPrefix.trim().isEmpty ())
			oCriteria.add (Restrictions.eq ("m_strPrefix", m_strPrefix.trim()));
		return oCriteria;
	}

	public boolean isVendorExists(VendorCounterData oVendorCounterData)
	{
		boolean bIsVendorExists = false;
		   m_oLogger.info("isVendorExists");
		   try
		   {
			   VendorCounterData oData = new VendorCounterData ();
			   VendorCounterDataProcessor oDataProcessor = new VendorCounterDataProcessor ();
			   oData.getM_oVendorData().setM_nClientId(oVendorCounterData.getM_oVendorData().getM_nClientId());
			   HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			   VendorCounterDataResponse oCounterDataResponse =(VendorCounterDataResponse) oDataProcessor.list(oData, oOrderBy);
			   bIsVendorExists = (oCounterDataResponse.m_arrVendorCounterData.size() > 0);
		   }
		   catch (Exception oException)
		   {
			   m_oLogger.error("isVendorExists - oException : " + oException);
		   }
		   m_oLogger.debug("isVendorExists - bIsArticleExists [OUT] : " + bIsVendorExists);
		   return bIsVendorExists;
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> oRootObject)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oVendorData"), m_oVendorData));
		if(m_oVendorData.getM_strCompanyName() != null && !m_oVendorData.getM_strCompanyName().trim().isEmpty())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.like(oCriteriaBuilder.lower(oRootObject.get("m_strCompanyName")), m_oVendorData.getM_strCompanyName().trim())); 
		if (m_strKey != null && !m_strKey.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strKey"), m_strKey.trim()));
		if (m_strPrefix != null && !m_strPrefix.trim().isEmpty ())
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_strPrefix"), m_strPrefix.trim()));
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_nId > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_nId"), m_nId)); 
		return oConjunct;
	}
}
