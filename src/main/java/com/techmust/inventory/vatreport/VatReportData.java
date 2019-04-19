package com.techmust.inventory.vatreport;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.data.TenantData;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class VatReportData extends TenantData 
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<VatLocalSalesReportData> m_arrVatLocalSalesData;
	private ArrayList<VatInterStateSalesReport> m_arrVatInterStateSalesData;
	private ArrayList<VatPurchaseReportData> m_arrVatPurchaseData;

	public VatReportData ()
	{
		m_arrVatLocalSalesData = new ArrayList<VatLocalSalesReportData> ();
		m_arrVatInterStateSalesData = new ArrayList<VatInterStateSalesReport> ();
		m_arrVatPurchaseData = new ArrayList<VatPurchaseReportData> ();
	}
	
	public ArrayList<VatLocalSalesReportData> getM_arrVatLocalSalesData() 
	{
		return m_arrVatLocalSalesData;
	}

	public void setM_arrVatLocalSalesData(ArrayList<VatLocalSalesReportData> arrVatLocalSalesData) 
	{
		m_arrVatLocalSalesData = arrVatLocalSalesData;
	}

	public ArrayList<VatInterStateSalesReport> getM_arrVatInterStateSalesData() 
	{
		return m_arrVatInterStateSalesData;
	}

	public void setM_arrVatInterStateSalesData(ArrayList<VatInterStateSalesReport> arrVatInterStateSalesData) 
	{
		m_arrVatInterStateSalesData = arrVatInterStateSalesData;
	}
	
	public ArrayList<VatPurchaseReportData> getM_arrVatPurchaseData() 
	{
		return m_arrVatPurchaseData;
	}

	public void setM_arrVatPurchaseData(ArrayList<VatPurchaseReportData> arrVatPurchaseData) 
	{
		m_arrVatPurchaseData = arrVatPurchaseData;
	}

	@Override
	public String generateXML() 
	{
		String strVatReportDataXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "VatReportData");
			Document oLoacalSalesDoc = getXmlDocument ("<VatLocalSalesReportList>" + buildLocalSalesXml (m_arrVatLocalSalesData) + "</VatLocalSalesReportList>");
			Node oLocalSalesNode = oXmlDocument.importNode (oLoacalSalesDoc.getFirstChild (), true);
			oRootElement.appendChild (oLocalSalesNode);
			Document oInterStateSalesDoc = getXmlDocument ("<VatInterStateSalesReportList>" + buildInterStateSalesXml (m_arrVatInterStateSalesData) + "</VatInterStateSalesReportList>");
			Node oInteStateSalesNode = oXmlDocument.importNode (oInterStateSalesDoc.getFirstChild (), true);
			oRootElement.appendChild (oInteStateSalesNode);
			Document oPurchaseReportDoc = getXmlDocument ("<VatPurchaseReportList>" + buildPurchaseReportXml (m_arrVatPurchaseData) + "</VatPurchaseReportList>");
			Node oPurchaseReportNode = oXmlDocument.importNode (oPurchaseReportDoc.getFirstChild (), true);
			oRootElement.appendChild (oPurchaseReportNode);
			strVatReportDataXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strVatReportDataXml;
	}

	private String buildLocalSalesXml(ArrayList<VatLocalSalesReportData> arrVatLocalSalesData) 
	{
		String strXML = "";
		for(int nIndex = 0; nIndex < arrVatLocalSalesData.size(); nIndex++)
			strXML += arrVatLocalSalesData.get(nIndex).generateXML();
		return strXML;
	}

	private String buildInterStateSalesXml(ArrayList<VatInterStateSalesReport> arrVatInterStateSalesData) 
	{
		String strXML = "";
		for(int nIndex = 0; nIndex < arrVatInterStateSalesData.size(); nIndex++)
			strXML += arrVatInterStateSalesData.get(nIndex).generateXML();
		return strXML;
	}
	
	private String buildPurchaseReportXml(ArrayList<VatPurchaseReportData> arrVatPurchaseData) 
	{
		String strXML = "";
		for(int nIndex = 0; nIndex < arrVatPurchaseData.size(); nIndex++)
			strXML += arrVatPurchaseData.get(nIndex).generateXML();
		return strXML;
	}

	
	@Override
	protected Criteria listCriteria(Criteria arg0, String arg1, String arg2) 
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
