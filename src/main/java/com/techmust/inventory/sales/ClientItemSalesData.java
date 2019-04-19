package com.techmust.inventory.sales;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class ClientItemSalesData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	private String m_strInvoiceNo;
	private String m_strDate;
	private float m_nQuantity;
	private float m_nPrice;
	private float m_nDiscount;
	private float m_nTax;
	private String m_strTaxName;

	public void setM_strInvoiceNo(String m_strInvoiceNo) 
	{
		this.m_strInvoiceNo = m_strInvoiceNo;
	}

	public String getM_strInvoiceNo() 
	{
		return m_strInvoiceNo;
	}

	public void setM_strDate(String strDate) 
	{
		m_strDate = strDate;
	}

	public String getM_strDate() 
	{
		return m_strDate;
	}

	public void setM_nQuantity(float nQuantity) 
	{
		m_nQuantity = nQuantity;
	}

	public float getM_nQuantity() 
	{
		return m_nQuantity;
	}

	public void setM_nPrice(float m_nPrice) 
	{
		this.m_nPrice = m_nPrice;
	}

	public float getM_nPrice() 
	{
		return m_nPrice;
	}

	public void setM_nDiscount(float m_nDiscount) 
	{
		this.m_nDiscount = m_nDiscount;
	}

	public float getM_nDiscount() 
	{
		return m_nDiscount;
	}

	public void setM_nTax(float m_nTax) 
	{
		this.m_nTax = m_nTax;
	}

	public float getM_nTax() 
	{
		return m_nTax;
	}

	public void setM_strTaxName(String m_strTaxName)
	{
		this.m_strTaxName = m_strTaxName;
	}

	public String getM_strTaxName() 
	{
		return m_strTaxName;
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

	public ClientItemSalesData setData(SalesLineItemData oSalesLineItemData) 
	{
		m_strInvoiceNo = oSalesLineItemData.getM_oSalesData().getM_strInvoiceNo();
		m_nQuantity = oSalesLineItemData.getM_nQuantity();
		m_nPrice = oSalesLineItemData.getM_nPrice();
		m_nDiscount = oSalesLineItemData.getM_nDiscount();
		m_nTax = oSalesLineItemData.getM_nTax();
		m_strTaxName = oSalesLineItemData.getM_strTaxName();
		m_strDate = GenericIDataProcessor.getClientCompatibleFormat(oSalesLineItemData.getM_oSalesData().getM_dDate());
		return this;
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
