package com.techmust.inventory.purchaseorder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.invoice.InvoiceDataResponse;
import com.techmust.inventory.invoice.OnlineVendorInvoiceData;
import com.techmust.inventory.invoice.OnlineVendorInvoiceDataProcessor;
import com.techmust.inventory.invoice.OnlineVendorInvoiceDataResponse;
import com.techmust.inventory.invoice.VendorInvoiceData;
import com.techmust.vendormanagement.VendorData;

public class VendorOrderDataProcessor extends POInvoiceDataProcessor 
{
	private VendorData m_oVendorData;
	
	public GenericResponse listVendorOrder(PurchaseOrderData oData, String strColumn, String strOrderBy, int nPageNumber, int nPageSize)throws Exception
	{
		m_oLogger.info("listVendorOrder");
		m_oLogger.debug("listVendorOrder - oData[IN] :" + oData);
		m_oLogger.debug("listVendorOrder - strColumn :" + strColumn);
		m_oLogger.debug("listVendorOrder -strOrderBy :" + strOrderBy);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try 
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOrderBy.put(strColumn, strOrderBy);
			oPurchaseOrderResponse = (PurchaseOrderResponse) super.list(oData, oOrderBy, nPageNumber, nPageSize);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("listVendorOrder - oException : " +oException);
		}
		return oPurchaseOrderResponse;
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse listOrderItems(PurchaseOrderData oData)throws Exception
	{
		m_oLogger.info("listOrderItems");
		m_oLogger.debug("listOrderItems - oData[IN] :" + oData);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try 
		{
			PurchaseOrderLineItemData oPurchaseOrderLineItemData = new PurchaseOrderLineItemData ();
			oPurchaseOrderLineItemData.setM_oPurchaseOrderData(oData);
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oPurchaseOrderResponse.m_arrPOStockLineItemData = new ArrayList( oPurchaseOrderLineItemData.list(oOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("listOrderItems - oException : " +oException);
		}
		return oPurchaseOrderResponse;
	}
	
	public GenericResponse makeInvoice (PurchaseOrderData oPOData, VendorData oVendorData) throws Exception
	{
		m_oLogger.info ("makeInvoice");
		m_oLogger.debug ("makeInvoice - oPurchaseOrderData [IN] :: " + oPOData);
		InvoiceDataResponse oInvoiceResponse = new InvoiceDataResponse ();
		OnlineVendorInvoiceData oOnlineVendorInvoiceData = new OnlineVendorInvoiceData ();
		OnlineVendorInvoiceDataProcessor oOnlineVendorInvoiceDataProcessor = new OnlineVendorInvoiceDataProcessor ();
		try
		{
			m_oVendorData = oVendorData;
			oInvoiceResponse = (InvoiceDataResponse) super.updateAndMakeInvoice(oPOData);
			oOnlineVendorInvoiceData.getM_oVendorData().setM_nClientId(oVendorData.getM_nClientId());
			oOnlineVendorInvoiceData.getM_oInvoiceData().setM_nInvoiceId(oInvoiceResponse.m_arrInvoice.get(0).getM_nInvoiceId());
			oOnlineVendorInvoiceDataProcessor.create(oOnlineVendorInvoiceData);
			Document oXmlDocument = convertStringToDocument(oInvoiceResponse.m_strXMLData);
			oVendorData = (VendorData) populateObject(oVendorData);
			oInvoiceResponse.m_strXMLData = oPOData.addVendorToXML (oXmlDocument,oVendorData);
		}
		catch (Exception oException)
		{
			m_oLogger.error("makeInvoice - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("makeInvoice - oInvoiceResponse.m_bSuccess [OUT] : " + oInvoiceResponse.m_bSuccess);
		return oInvoiceResponse;
	}
	
	private Document convertStringToDocument(String strXMLData) 
	{
		m_oLogger.info ("convertStringToDocument");
		m_oLogger.debug ("convertStringToDocument - strXMLData [IN] :: " + strXMLData);
		Document oDocument = null;
		DocumentBuilderFactory oDocFactory = DocumentBuilderFactory.newInstance();
        try 
        {  
        	DocumentBuilder oDocBuilder = oDocFactory.newDocumentBuilder();
            oDocument = oDocBuilder.parse(new InputSource(new StringReader(strXMLData))); 
        } 
        catch (Exception oException) 
        {  
        	m_oLogger.error("convertStringToDocument - oException : " + oException);
        } 
        return oDocument;
    }
	
	public GenericResponse cancelOrder (PurchaseOrderData oPurchaseOrderData)
	{
		m_oLogger.info ("cancelOrder");
		m_oLogger.debug ("cancelOrder - oPurchaseOrderData [IN] : " + oPurchaseOrderData);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try
		{
			oPurchaseOrderResponse.m_bSuccess = super.cancelOrder(oPurchaseOrderData).m_bSuccess;
		}
		catch (Exception oException)
		{
			m_oLogger.error("cancelOrder - oException : ", oException);
		}
		
		return oPurchaseOrderResponse;
	}
	
	public GenericResponse reallow (PurchaseOrderData oPurchaseOrderData)
	{
		m_oLogger.info ("reallow");
		m_oLogger.debug ("reallow - oPurchaseOrderData [IN] : " + oPurchaseOrderData);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try
		{
			oPurchaseOrderResponse.m_bSuccess = super.reallow(oPurchaseOrderData).m_bSuccess;
		}
		catch (Exception oException)
		{
			m_oLogger.error("reallow - oException : ", oException);
		}
		
		return oPurchaseOrderResponse;
	}
	
	@SuppressWarnings("static-access")
	protected InvoiceData CreateInvoiceData (PurchaseOrderData oPOData) throws Exception
	{
		VendorInvoiceData oVendorInvoiceData = new VendorInvoiceData ();
		oVendorInvoiceData.setM_oVendorData(m_oVendorData);
		return oVendorInvoiceData.create (oPOData);
	}
}
