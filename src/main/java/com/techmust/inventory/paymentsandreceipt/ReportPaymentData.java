package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

public class ReportPaymentData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	private VendorData m_oVendorData;
	private float m_nAmount;
	private ArrayList<PaymentData> m_arrPaymentData;
	
	public ReportPaymentData()
	{
		m_arrPaymentData = new ArrayList<PaymentData> ();
		m_nAmount = 0;
	}

	public VendorData getM_oVendorData() 
	{
		return m_oVendorData;
	}

	public void setM_oVendorData(VendorData oVendorData) 
	{
		m_oVendorData = oVendorData;
	}

	public float getM_nAmount() 
	{
		return m_nAmount;
	}

	public void setM_nAmount(float nAmount) 
	{
		m_nAmount = nAmount;
	}

	public ArrayList<PaymentData> getM_arrPaymentData() 
	{
		return m_arrPaymentData;
	}

	public void setM_arrPaymentData(ArrayList<PaymentData> arrPaymentData) 
	{
		m_arrPaymentData = arrPaymentData;
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
	
	public void setReportData(PaymentData oPaymentData) 
	{
		m_nAmount += oPaymentData.getM_nAmount();
		m_oVendorData = oPaymentData.getM_oVendorData();
		m_arrPaymentData.add(oPaymentData);
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
