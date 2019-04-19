package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;

import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class ReportReceiptData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	private ClientData m_oClientData;
	private float m_nAmount;
	private ArrayList<ReceiptData> m_arrReceiptData;
	
	public ReportReceiptData() 
	{
		m_arrReceiptData = new ArrayList<ReceiptData> ();
		m_nAmount = 0;
	}

	public ClientData getM_oClientData() 
	{
		return m_oClientData;
	}

	public void setM_oClientData(ClientData clientData) 
	{
		m_oClientData = clientData;
	}

	public float getM_nAmount() 
	{
		return m_nAmount;
	}

	public void setM_nAmount(float amount)
	{
		m_nAmount = amount;
	}

	public ArrayList<ReceiptData> getM_arrReceiptData() 
	{
		return m_arrReceiptData;
	}

	public void setM_arrReceiptData(ArrayList<ReceiptData> receiptData)
	{
		m_arrReceiptData = receiptData;
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

	public void setReportData(ReceiptData oReceiptData) 
	{
		m_nAmount += oReceiptData.getM_nAmount();
		m_oClientData = oReceiptData.getM_oClientData();
		m_arrReceiptData.add(oReceiptData);
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
