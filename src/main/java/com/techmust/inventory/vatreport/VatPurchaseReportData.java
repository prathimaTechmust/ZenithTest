package com.techmust.inventory.vatreport;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.traderp.util.TraderpUtil;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;

public class VatPurchaseReportData extends TenantData 
{
	private static final long serialVersionUID = 1L;

	private float m_nTax;
	private float m_nTaxableAmount;
	private float m_nTaxAmount;
	
	public VatPurchaseReportData ()
	{
		m_nTax = 0;
		m_nTaxableAmount = 0;
		m_nTaxAmount = 0;
	}
	
	public float getM_nTax() 
	{
		return m_nTax;
	}

	public void setM_nTax(float nTax) 
	{
		m_nTax = nTax;
	}

	public float getM_nTaxableAmount() 
	{
		return m_nTaxableAmount;
	}

	public void setM_nTaxableAmount(float nTaxableAmount) 
	{
		m_nTaxableAmount = nTaxableAmount;
	}

	public float getM_nTaxAmount() 
	{
		return m_nTaxAmount;
	}

	public void setM_nTaxAmount(float nTaxAmount) 
	{
		m_nTaxAmount = nTaxAmount;
	}
	
	@Override
	public String generateXML() 
	{
		String strVatPurchaseReportXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "VatPurchaseReportData");
			addChild (oXmlDocument, oRootElement, "m_nTax", TraderpUtil.formatNumber(m_nTax));
			addChild (oXmlDocument, oRootElement, "m_nTaxableAmount", TraderpUtil.formatNumber(m_nTaxableAmount));
			addChild (oXmlDocument, oRootElement, "m_nTaxAmount", TraderpUtil.formatNumber(m_nTaxAmount));
			strVatPurchaseReportXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strVatPurchaseReportXml;
	}

	@Override
	protected Criteria listCriteria(Criteria oCriteria, String strColumn, String strOrderBy) 
	{
		return null;
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
