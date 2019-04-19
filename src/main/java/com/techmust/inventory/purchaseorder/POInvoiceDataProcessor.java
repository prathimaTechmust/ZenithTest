package com.techmust.inventory.purchaseorder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.invoice.InvoiceDataResponse;
public class POInvoiceDataProcessor extends POChallanDataProcessor
{
	@RequestMapping(value="/oPOInvoiceDatacreateInvoice", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse createInvoice (@RequestBody PurchaseOrderData oPOData) throws Exception
	{
		m_oLogger.info ("createInvoice");
		m_oLogger.debug ("createInvoice - oPurchaseOrderData [IN] : " + oPOData);
		InvoiceDataResponse oInvoiceResponse = new InvoiceDataResponse ();
		try
		{
			oInvoiceResponse.m_bSuccess = create (oPOData).m_bSuccess;
			if (oInvoiceResponse.m_bSuccess)
			{
				InvoiceData oInvoiceData = CreateInvoiceData (oPOData);
				oInvoiceResponse.m_strXMLData = oInvoiceData.generateXML();
				oInvoiceResponse.m_arrInvoice.add(oInvoiceData);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("createInvoice - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("createInvoice - oInvoiceResponse.m_bSuccess [OUT] : " + oInvoiceResponse.m_bSuccess);
		return oInvoiceResponse;
	}
	@RequestMapping(value="/oPOInvoiceDataupdateAndMakeInvoice", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse updateAndMakeInvoice (@RequestBody PurchaseOrderData oPOData) throws Exception
	{
		m_oLogger.info ("updateAndMakeInvoice");
		m_oLogger.debug ("updateAndMakeInvoice - oPurchaseOrderData [IN] : " + oPOData);
		InvoiceDataResponse oInvoiceResponse = new InvoiceDataResponse ();
		try
		{
			oInvoiceResponse.m_bSuccess = update (oPOData).m_bSuccess;
			if (oInvoiceResponse.m_bSuccess && oPOData.canInvoice ())
			{ 
				InvoiceData oInvoiceData = CreateInvoiceData (oPOData);
				oInvoiceResponse.m_strXMLData = oInvoiceData.generateXML();
				oInvoiceResponse.m_arrInvoice.add(oInvoiceData);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateAndMakeInvoice - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("updateAndMakeInvoice - oInvoiceResponse.m_bSuccess [OUT] : " + oInvoiceResponse.m_bSuccess);
		return oInvoiceResponse;
	}
	@RequestMapping(value="/oPOInvoiceDatagetInvoices", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getInvoices (@RequestBody PurchaseOrderData oPurchaseOrderData)
	{
		m_oLogger.info ("getInvoices");
		m_oLogger.debug ("getInvoices - oPurchaseOrderData [IN] : " + oPurchaseOrderData);
		
		InvoiceDataResponse oInvoiceResponse = new InvoiceDataResponse ();
		try
		{
			oPurchaseOrderData = (PurchaseOrderData) populateObject (oPurchaseOrderData);
			oInvoiceResponse.m_arrInvoice = oPurchaseOrderData.getInvoices ();
		}
		catch (Exception oException)
		{
			m_oLogger.error("getInvoices - oException : ", oException);
		}
		
		return oInvoiceResponse;
	}
	
	protected InvoiceData CreateInvoiceData (PurchaseOrderData oPOData) throws Exception
	{
		InvoiceData oInvoiceData = new InvoiceData ();
		oInvoiceData = oInvoiceData.create (oPOData);
		return oInvoiceData;
	}
}
