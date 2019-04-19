package com.techmust.inventory.purchaseorder;

import java.util.ArrayList;
import java.util.HashMap;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;

public class PurchaseOrderLineItemDataProcessor extends GenericIDataProcessor<PurchaseOrderLineItemData> 
{
	@Override
	public GenericResponse create(PurchaseOrderLineItemData oPurchaseOrderLineItemData)throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse deleteData(PurchaseOrderLineItemData oPurchaseOrderLineItemData)throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse get(PurchaseOrderLineItemData oPurchaseOrderLineItemData) throws Exception
	{
		return null;
	}

	@Override
	public String getXML(PurchaseOrderLineItemData oPurchaseOrderLineItemData) throws Exception 
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericResponse list(PurchaseOrderLineItemData oPurchaseOrderLineItemData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oPurchaseOrderLineItemData [IN] : " +oPurchaseOrderLineItemData);
		PurchaseOrderLineItemDataResponse oPurchaseOrderLineItemDataResponse = new PurchaseOrderLineItemDataResponse ();
		try 
		{
			oPurchaseOrderLineItemDataResponse.m_arrPurchaseOrderLineItem = new ArrayList (oPurchaseOrderLineItemData.list (arrOrderBy));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
			throw oException;
		}
		return oPurchaseOrderLineItemDataResponse;
	}

	@Override
	public GenericResponse update(PurchaseOrderLineItemData oPurchaseOrderLineItemData) throws Exception 
	{
		return null;
	}

}
