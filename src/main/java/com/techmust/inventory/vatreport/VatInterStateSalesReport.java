package com.techmust.inventory.vatreport;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.techmust.traderp.util.TraderpUtil;

public class VatInterStateSalesReport extends VatLocalSalesReportData 
{
	private static final long serialVersionUID = 1L;
	private boolean m_bIsCFormProvided;
	
	public VatInterStateSalesReport ()
	{
		m_bIsCFormProvided = false;
	}
	
	public void setM_bIsCFormProvided(boolean bIsCFormProvided) 
	{
		this.m_bIsCFormProvided = bIsCFormProvided;
	}

	public boolean isM_bIsCFormProvided() 
	{
		return m_bIsCFormProvided;
	}

	@Override
	public String generateXML() 
	{
		String strVatInterStateReportXml = "";
		m_oLogger.info("generateXML");
		try 
		{
			Document oXmlDocument = createNewXMLDocument ();
			Element oRootElement = createRootElement(oXmlDocument, "VatInterStateSalesReport");
			addChild (oXmlDocument, oRootElement, "m_nTax", TraderpUtil.formatNumber(m_nTax));
			addChild (oXmlDocument, oRootElement, "m_nTaxableAmount", TraderpUtil.formatNumber(m_nTaxableAmount));
			addChild (oXmlDocument, oRootElement, "m_nTaxAmount", TraderpUtil.formatNumber(m_nTaxAmount));
			addChild (oXmlDocument, oRootElement, "m_bIsCFormProvided", isM_bIsCFormProvided () ? "true" : "false");
			strVatInterStateReportXml = getXmlString (oXmlDocument);
		}
		catch (Exception oException) 
		{
			m_oLogger.error("generateXML - oException" + oException);
		}
		return strVatInterStateReportXml;
	}

}
