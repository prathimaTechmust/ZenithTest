package com.techmust.inventory.items;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.util.HibernateUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@Entity
@Table(name = "tac60_vendor_items")
public class VendorItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ac60_vendorItem_id")
	private int m_nId;
	@ManyToOne
	@JoinColumn(name = "ac60_vendor_id")
	protected VendorData m_oVendorData;
	@ManyToOne
	@JoinColumn(name = "ac60_item_id")
	protected ItemData m_oItemData;
	@Transient
	private float m_nMinimumPrice;
	@Transient
	private float m_nMaximumPrice;
	@Transient
	public ItemGroupData [] m_arrItemGroups;
	@Transient
	private Set<ItemGroupData> m_oItemGroups;
	@Transient
	public ItemCategoryData [] m_arrCategories;
	@Transient
	private Set<ItemCategoryData> m_oItemCategories;
	@Transient
	public VendorData [] m_arrVendors;
	@Transient
	private Set<VendorData> m_oVendors;
	
	public VendorItemData() 
	{
		m_nId = -1;
		m_oItemData = new ItemData ();
		m_oVendorData = new VendorData ();
		m_nMinimumPrice = -1;
		m_nMaximumPrice = -1;
		m_oItemGroups = new HashSet<ItemGroupData> (); 
		m_oItemCategories = new HashSet<ItemCategoryData> ();
		m_oVendors = new HashSet<VendorData> ();
	}

	public void setM_nId(int nId) 
	{
		m_nId = nId;
	}

	public int getM_nId() 
	{
		return m_nId;
	}

	public void setM_oItemData(ItemData oItemData) 
	{
		m_oItemData = oItemData;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_oVendorData(VendorData oVendorData) 
	{
		m_oVendorData = oVendorData;
	}

	public VendorData getM_oVendorData() 
	{
		return m_oVendorData;
	}
	
	public float getM_nMinimumPrice () 
	{
		return m_nMinimumPrice;
	}

	public void setM_nMinimumPrice (float nMinimumPrice) 
	{
		m_nMinimumPrice = nMinimumPrice;
	}


	public float getM_nMaximumPrice () 
	{
		return m_nMaximumPrice;
	}


	public void setM_nMaximumPrice (float nMaximumPrice) 
	{
		m_nMaximumPrice = nMaximumPrice;
	}


	public Set<ItemGroupData> getM_oItemGroups () 
	{
		return m_oItemGroups;
	}


	public void setM_oItemGroups (Set<ItemGroupData> oItemGroups) 
	{
		m_oItemGroups = oItemGroups;
	}


	public Set<ItemCategoryData> getM_oItemCategories () 
	{
		return m_oItemCategories;
	}


	public void setM_oItemCategories(Set<ItemCategoryData> oItemCategories) 
	{
		m_oItemCategories = oItemCategories;
	}


	public Set<VendorData> getM_oVendors () 
	{
		return m_oVendors;
	}


	public void setM_oVendors(Set<VendorData> oVendors)
	{
		m_oVendors =oVendors;
	}

	@Override
	public String generateXML() 
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
		Criteria oItemDataCriteria = oCriteria.createCriteria ("m_oItemData");
		Criteria oVendorDataCriteria = oCriteria.createCriteria ("m_oVendorData");
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oCriteria.add (Restrictions.eq ("m_oVendorData", m_oVendorData));
		if (m_oVendorData.isM_bVerified())
			oVendorDataCriteria.add (Restrictions.like ("m_bVerified", m_oVendorData.isM_bVerified()));
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oCriteria.add (Restrictions.eq ("m_oItemData", m_oItemData));
		if(m_oItemData.getM_strItemName() != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.ilike ("m_strItemName", m_oItemData.getM_strItemName().trim(), MatchMode.ANYWHERE));
		if(m_oItemData.getM_strBrand() != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.ilike ("m_strBrand", m_oItemData.getM_strBrand().trim(), MatchMode.ANYWHERE));
		if(m_oItemData.getM_strArticleNumber() != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oItemDataCriteria.add (Restrictions.ilike ("m_strArticleNumber", m_oItemData.getM_strArticleNumber().trim(),MatchMode.ANYWHERE));
		if(m_oItemData.getM_strDetail() != null && !m_oItemData.getM_strDetail().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.ilike ("m_strDetail", m_oItemData.getM_strDetail().trim(), MatchMode.ANYWHERE));
		if (m_oItemData.getM_oItemCategoryData()!= null && m_oItemData.getM_oItemCategoryData().getM_nCategoryId() > 0)
			oItemDataCriteria.add (Restrictions.like ("m_oItemCategoryData", m_oItemData.getM_oItemCategoryData()));
		if (m_oItemData.getM_strFromDate() != null && !m_oItemData.getM_strFromDate().isEmpty())
			oItemDataCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_oItemData.getM_strFromDate())));
		if (m_oItemData.getM_strToDate() != null && !m_oItemData.getM_strToDate().isEmpty())
			oItemDataCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oItemData.getM_strToDate(),true)));
		if (m_oItemData.isM_bPublishOnline())
			oItemDataCriteria.add (Restrictions.like ("m_bPublishOnline", m_oItemData.isM_bPublishOnline()));
		return oCriteria;
	}
	
	public Collection listCustomDataForSearch(VendorItemDataProcessor oProcessor) throws Exception 
	{
		m_oLogger.info("listCustomData");
//		Session oSession = HibernateUtil.getSession();
		EntityManager oEntityManager = HibernateUtil.getTenantEntityManager();
		ArrayList<VendorItemData> arrGenericData = new ArrayList<VendorItemData> ();
		try
		{
/*
 			Criteria oCriteria = oSession.createCriteria(this.getClass().getName());
 			oCriteria = oProcessor.prepareSearchCustomCriteria (oCriteria, this);
			List<?> oResult = oCriteria.list();
			arrGenericData = (ArrayList<GenericData>) oResult;
*/			
 			CriteriaBuilder builder = oEntityManager.getCriteriaBuilder();
 			Class<VendorItemData> oClass = VendorItemData.class;
			CriteriaQuery<VendorItemData> oCriteria = builder.createQuery(oClass);
			Root<VendorItemData> root = oCriteria.from(oClass );
			oCriteria.select( root );
//			oCriteria = oProcessor.prepareCustomCriteria (oCriteria, this);
			arrGenericData = (ArrayList<VendorItemData>) oEntityManager.createQuery( oCriteria ).getResultList();

		}
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException :" +oException);
			throw oException;
		}
		finally
		{
			oEntityManager.close();
			HibernateUtil.removeConnection();
		}
		m_oLogger.debug("list - arrGenericData [OUT] :" +arrGenericData);
		return  arrGenericData;
	}

	public void prepareItemCategorySet() 
	{
		for (int nIndex = 0; m_arrCategories != null && nIndex < m_arrCategories.length; nIndex++)
			m_oItemCategories.add(m_arrCategories[nIndex]);
	}

	public void prepareVendorSet()
	{
		for (int nIndex = 0; m_arrVendors != null && nIndex < m_arrVendors.length; nIndex++)
			m_oVendors.add(m_arrVendors[nIndex]);
	}

	public void prepareGroupSet () 
	{
		for (int nIndex = 0; m_arrItemGroups != null && nIndex < m_arrItemGroups.length; nIndex++)
			m_oItemGroups.add(m_arrItemGroups[nIndex]);
	}

	@Override
	protected Predicate listCriteria(CriteriaBuilder oCriteriaBuilder, Root<GenericData> root)
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
/*	
  		Criteria oItemDataCriteria = oCriteria.createCriteria ("m_oItemData");
		Criteria oVendorDataCriteria = oCriteria.createCriteria ("m_oVendorData");
		if (m_nId > 0)
			oCriteria.add (Restrictions.eq ("m_nId", m_nId));
		if (m_oVendorData != null && m_oVendorData.getM_nClientId() > 0)
			oCriteria.add (Restrictions.eq ("m_oVendorData", m_oVendorData));
		if (m_oVendorData.isM_bVerified())
			oVendorDataCriteria.add (Restrictions.like ("m_bVerified", m_oVendorData.isM_bVerified()));
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oCriteria.add (Restrictions.eq ("m_oItemData", m_oItemData));
		if(m_oItemData.getM_strItemName() != null && !m_oItemData.getM_strItemName().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.ilike ("m_strItemName", m_oItemData.getM_strItemName().trim(), MatchMode.ANYWHERE));
		if(m_oItemData.getM_strBrand() != null && !m_oItemData.getM_strBrand().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.ilike ("m_strBrand", m_oItemData.getM_strBrand().trim(), MatchMode.ANYWHERE));
		if(m_oItemData.getM_strArticleNumber() != null && !m_oItemData.getM_strArticleNumber().trim().isEmpty())
			oItemDataCriteria.add (Restrictions.ilike ("m_strArticleNumber", m_oItemData.getM_strArticleNumber().trim(),MatchMode.ANYWHERE));
		if(m_oItemData.getM_strDetail() != null && !m_oItemData.getM_strDetail().trim().isEmpty())
			oItemDataCriteria.add(Restrictions.ilike ("m_strDetail", m_oItemData.getM_strDetail().trim(), MatchMode.ANYWHERE));
		if (m_oItemData.getM_oItemCategoryData()!= null && m_oItemData.getM_oItemCategoryData().getM_nCategoryId() > 0)
			oItemDataCriteria.add (Restrictions.like ("m_oItemCategoryData", m_oItemData.getM_oItemCategoryData()));
		if (m_oItemData.getM_strFromDate() != null && !m_oItemData.getM_strFromDate().isEmpty())
			oItemDataCriteria.add (Restrictions.ge("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleDateFormat(m_oItemData.getM_strFromDate())));
		if (m_oItemData.getM_strToDate() != null && !m_oItemData.getM_strToDate().isEmpty())
			oItemDataCriteria.add (Restrictions.le ("m_dCreatedOn", GenericIDataProcessor.getDBCompatibleFilterDateFormat(m_oItemData.getM_strToDate(),true)));
		if (m_oItemData.isM_bPublishOnline())
			oItemDataCriteria.add (Restrictions.like ("m_bPublishOnline", m_oItemData.isM_bPublishOnline()));
*/
		return oConjunct;
	}

	@Override
	public Predicate prepareCriteria(Root<GenericData> oRootObject, CriteriaQuery<GenericData> oCriteria, CriteriaBuilder oCriteriaBuilder) 
	{
		Predicate oConjunct = oCriteriaBuilder.conjunction();
		if (m_oItemData != null && m_oItemData.getM_nItemId() > 0)
			oConjunct = oCriteriaBuilder.and(oConjunct, oCriteriaBuilder.equal(oRootObject.get("m_oItemData"), m_oItemData));
		return oConjunct;
	}
}
