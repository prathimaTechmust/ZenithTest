package com.techmust.inventory.paymentsandreceipt;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;

@Controller
public class InvoiceReceiptDataProcessor extends GenericIDataProcessor<InvoiceReceiptData> 
{

	@Override
	public GenericResponse create(InvoiceReceiptData oInvoiceReceiptData) throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse deleteData(InvoiceReceiptData oInvoiceReceiptData) throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse get(InvoiceReceiptData oInvoiceReceiptData) throws Exception 
	{
		return null;
	}

	@Override
	public String getXML(InvoiceReceiptData oInvoiceReceiptData) throws Exception 
	{
		return null;
	}

	@RequestMapping(value="/InvoiceReceiptList", method = RequestMethod.POST, headers ={"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list(oData.getM_oInvoiceReceiptData(), oOrderBy);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericResponse list(InvoiceReceiptData oInvoiceReceiptData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oInvoiceReceiptData [IN] : " +oInvoiceReceiptData);
		InvoiceReceiptDataResponse oInvoiceReceiptDataResponse = new InvoiceReceiptDataResponse ();
		try 
		{
			oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData = new ArrayList (oInvoiceReceiptData.list (arrOrderBy));
			oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData = buildReceiptData(oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		m_oLogger.debug("list - oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData.size () [OUT] : " + oInvoiceReceiptDataResponse.m_arrInvoiceReceiptData.size());
		return oInvoiceReceiptDataResponse;
	}
	
	private ArrayList<InvoiceReceiptData> buildReceiptData(ArrayList<InvoiceReceiptData> arrInvoiceReceiptData) 
	{
		m_oLogger.info("buildSalesData");
		for (int nIndex=0; nIndex < arrInvoiceReceiptData.size(); nIndex++)
			arrInvoiceReceiptData.get(nIndex).getM_oReceiptData().setM_strDate(getClientCompatibleFormat(arrInvoiceReceiptData.get(nIndex).getM_oReceiptData().getM_dCreatedOn())); 
		return arrInvoiceReceiptData;
	}

	@Override
	public GenericResponse update(InvoiceReceiptData oInvoiceReceiptData) throws Exception 
	{
		return null;
	}

}
