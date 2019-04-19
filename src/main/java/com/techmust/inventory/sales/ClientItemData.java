package com.techmust.inventory.sales;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.inventory.items.ItemData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class ClientItemData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	private ItemData m_oItemData;
	private int m_nTotalQuantity;
	public ArrayList<ClientItemSalesData> m_arrClientItemSalesData;
	
	public ClientItemData() 
	{
		m_arrClientItemSalesData = new ArrayList<ClientItemSalesData> ();
	}

	public void setM_oItemData(ItemData oItemData)
	{
		m_oItemData = oItemData;
	}

	public ItemData getM_oItemData() 
	{
		return m_oItemData;
	}

	public void setM_nTotalQuantity(int nTotalQuantity)
	{
		m_nTotalQuantity = nTotalQuantity;
	}

	public int getM_nTotalQuantity() 
	{
		return m_nTotalQuantity;
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

	public ClientItemData setClientItemData(SalesLineItemData oSalesLineItemData) 
	{
		m_oItemData = oSalesLineItemData.getM_oItemData();
		addTotalQuantity (oSalesLineItemData.getM_nQuantity());
		ClientItemSalesData oClientItemSalesData = new ClientItemSalesData ();
		m_arrClientItemSalesData.add(oClientItemSalesData.setData(oSalesLineItemData));
		return this;
	}

	public void updateClientItemData(SalesLineItemData oSalesLineItemData) 
	{
		addTotalQuantity (oSalesLineItemData.getM_nQuantity());
		ClientItemSalesData oClientItemSalesData = new ClientItemSalesData ();
		m_arrClientItemSalesData.add(oClientItemSalesData.setData(oSalesLineItemData));
	}

	private void addTotalQuantity(float nQuantity) 
	{
		m_nTotalQuantity += nQuantity;
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
