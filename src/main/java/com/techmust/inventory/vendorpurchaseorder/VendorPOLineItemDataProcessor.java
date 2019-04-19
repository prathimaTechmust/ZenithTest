package com.techmust.inventory.vendorpurchaseorder;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.purchaseorder.PurchaseOrderLineItemDataResponse;

public class VendorPOLineItemDataProcessor extends GenericIDataProcessor<VendorPOLineItemData> 
{
	@Override
	public GenericResponse create(VendorPOLineItemData arg0) throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse deleteData(VendorPOLineItemData arg0) throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse get(VendorPOLineItemData arg0) throws Exception 
	{
		return null;
	}

	@Override
	public String getXML(VendorPOLineItemData arg0) throws Exception 
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericResponse list(VendorPOLineItemData oVendorPOLineItemData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oPurchaseOrderLineItemData [IN] : " + oVendorPOLineItemData.getM_nLineItemId());

		PurchaseOrderLineItemDataResponse oPurchaseOrderLineItemDataResponse = new PurchaseOrderLineItemDataResponse ();
		try 
		{
			oPurchaseOrderLineItemDataResponse.m_arrPurchaseOrderLineItem = new ArrayList (oVendorPOLineItemData.list (arrOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " + oException);
			throw oException;
		}
		return oPurchaseOrderLineItemDataResponse;
	}

	@Override
	public GenericResponse update(VendorPOLineItemData arg0) throws Exception 
	{
		return null;
	}
}
