package com.techmust.inventory.purchaseorder;

import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.purchase.PurchaseDataResponse;
import com.techmust.inventory.serial.SerialNumberData;
import com.techmust.inventory.serial.SerialType;
import com.techmust.usermanagement.userinfo.UserInformationData;

public class PurchaseOrderDataProcessor extends GenericIDataProcessor<PurchaseOrderData> 
{
	public GenericResponse create(PurchaseOrderData oPOData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oPOData);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try
		{
			isCreditLimitExceeded(oPOData);
			oPOData.prepareForUpdate ();
			if(oPOData.getM_strPurchaseOrderNumber().isEmpty())
				oPOData.setM_strPurchaseOrderNumber(SerialNumberData.generateSerialNumber(SerialType.kOrderNumber));
			oPurchaseOrderResponse.m_bSuccess = oPOData.saveObject();
//			oPurchaseOrderResponse.m_arrPurchaseOrder.add(oPOData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oPurchaseOrderResponse.m_bSuccess [OUT] : " + oPurchaseOrderResponse.m_bSuccess);
		return oPurchaseOrderResponse;
	}
	
	
	
	public GenericResponse deleteData(PurchaseOrderData oData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " + oData);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try
		{
			createLog(oData, "PurchaseOrderDataProcessor.deleteData : PurchaseOrderNo - " + oData.getM_oClientData().getM_strCompanyName());
			oPurchaseOrderResponse.m_bSuccess = oData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oPurchaseOrderResponse.m_bSuccess [OUT] : " + oPurchaseOrderResponse.m_bSuccess);
		return oPurchaseOrderResponse;
	}

	public GenericResponse get(PurchaseOrderData oData) throws Exception 
	{
		m_oLogger.info ("get");
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try 
		{
			oData = (PurchaseOrderData) populateObject (oData);
			oData.customizePOLineItems ();
			oData.setM_strPurchaseOrderDate(getClientCompatibleFormat(oData.getM_dPurchaseOrderDate()));
			oPurchaseOrderResponse.m_arrPurchaseOrder.add(oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oPurchaseOrderResponse;
	}


	public String getXML(PurchaseOrderData oData) throws Exception 
	{
		oData = (PurchaseOrderData) populateObject(oData);
	    return oData != null ? oData.generateXML () : "";
	}

	public GenericResponse list(TradeMustHelper oTradeMustHelper) throws Exception 
	{
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
			oPurchaseOrderResponse = (PurchaseOrderResponse) list(oTradeMustHelper.getM_oPurchaseOrderData(),oOrderBy, oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
		}
		catch(Exception oException)
		{
			m_oLogger.error ("list - oException : "  +oException);
		}
		return oPurchaseOrderResponse;
	}
	
	public GenericResponse list(PurchaseOrderData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)throws Exception 
	{
		m_oLogger.info("list");
		m_oLogger.debug("list - oData[IN] :" + oData);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try 
		{
			oPurchaseOrderResponse.m_nRowCount = getRowCount(oData);
			oPurchaseOrderResponse.m_arrPurchaseOrder = new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize));
			oPurchaseOrderResponse.m_arrPurchaseOrder = buildPurchaseOrderDate (oPurchaseOrderResponse.m_arrPurchaseOrder);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oPurchaseOrderResponse;
	}

	
	public GenericResponse update(PurchaseOrderData oPOData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oPOData [IN] : " + oPOData);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try
		{
			createLog(oPOData, "PurchaseOrderDataProcessor.update : PurchaseOrderNo - " + oPOData.getM_strPurchaseOrderNumber());
			isCreditLimitExceeded(oPOData);
			oPOData.prepareForUpdate ();
			oPurchaseOrderResponse.m_bSuccess = oPOData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oPurchaseOrderResponse.m_bSuccess [OUT] : " + oPurchaseOrderResponse.m_bSuccess);
		return oPurchaseOrderResponse;
	}
	
	public GenericResponse deletePurchaseOrderLineItems (PurchaseOrderData oPurchaseOrderData) throws Exception
	{
		m_oLogger.info ("deletePurchaseOrderLineItems");
		m_oLogger.debug ("deletePurchaseOrderLineItems - oPurchaseOrderData [IN] : " + oPurchaseOrderData);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try
		{
			for (int nIndex=0; nIndex < oPurchaseOrderData.m_arrPurchaseOrderLineItems.length; nIndex++)
			{
				PurchaseOrderLineItemData oPurchaseOrderLineItem = (PurchaseOrderLineItemData) oPurchaseOrderData.m_arrPurchaseOrderLineItems[nIndex];
				oPurchaseOrderLineItem = (PurchaseOrderLineItemData) populateObject(oPurchaseOrderLineItem);
				oPurchaseOrderResponse.m_bSuccess = oPurchaseOrderLineItem.deleteObject();
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("deletePurchaseOrderLineItems - oException : " + oException);
			throw oException;
		}
		return oPurchaseOrderResponse;
	}

	public GenericResponse getPurchases (PurchaseOrderData oPurchaseOrderData)
	{
		m_oLogger.info ("getPurchases");
		m_oLogger.debug ("getPurchases - oPurchaseOrderData [IN] : " + oPurchaseOrderData);
		
		PurchaseDataResponse oPurchaseDataResponse = new PurchaseDataResponse ();
		try
		{
			oPurchaseOrderData = (PurchaseOrderData) populateObject (oPurchaseOrderData);
			oPurchaseDataResponse.m_arrPurchase = oPurchaseOrderData.getPurchases ();
		}
		catch (Exception oException)
		{
			m_oLogger.error("getPurchases - oException : ", oException);
		}
		
		return oPurchaseDataResponse;
	}
	
	public GenericResponse cancelOrder (PurchaseOrderData oPurchaseOrderData)
	{
		m_oLogger.info ("cancelOrder");
		m_oLogger.debug ("cancelOrder - oPurchaseOrderData [IN] : " + oPurchaseOrderData);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try
		{
			oPurchaseOrderData = (PurchaseOrderData) populateObject (oPurchaseOrderData);
			oPurchaseOrderData.setM_nPurchaseOrderStatus(PurchaseOrderStatus.kCancelled);
			oPurchaseOrderResponse.m_bSuccess = oPurchaseOrderData.updateObject();
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
			oPurchaseOrderData = (PurchaseOrderData) populateObject (oPurchaseOrderData);
			oPurchaseOrderData.setM_nPurchaseOrderStatus(PurchaseOrderStatus.kPending);
			oPurchaseOrderResponse.m_bSuccess = oPurchaseOrderData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error("reallow - oException : ", oException);
		}
		
		return oPurchaseOrderResponse;
	}
	
	public PurchaseOrderResponse validateOrderNumber (PurchaseOrderData oPurchaseOrderData) throws Exception
	{
		m_oLogger.info ("validateOrderNumber");
		m_oLogger.debug ("validateOrderNumber - oPurchaseOrderData [IN] : " + oPurchaseOrderData);
		PurchaseOrderResponse oPurchaseOrderResponse = new PurchaseOrderResponse ();
		try 
		{
			oPurchaseOrderResponse.m_arrPurchaseOrder = new ArrayList (oPurchaseOrderData.listCustomData(this));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("validateOrderNumber - oException : " +oException);
			throw oException;
		}
		return oPurchaseOrderResponse;
	}
	
	public Criteria prepareCustomCriteria(Criteria oCriteria, PurchaseOrderData oPurchaseOrderData) throws RuntimeException 
	{
		oCriteria.add(Restrictions.eq("m_oClientData", oPurchaseOrderData.getM_oClientData()));
		oCriteria.add (Restrictions.eq ("m_strPurchaseOrderNumber", oPurchaseOrderData.getM_strPurchaseOrderNumber().trim()));
		oCriteria.setMaxResults(1);
		return oCriteria;
	}
	
	private ArrayList<PurchaseOrderData> buildPurchaseOrderDate(ArrayList<PurchaseOrderData> arrPurchaseOrder) 
	{
		m_oLogger.info("buildPurchaseOrderDate");
		for (int nIndex=0; nIndex < arrPurchaseOrder.size(); nIndex++)
			arrPurchaseOrder.get(nIndex).setM_strPurchaseOrderDate(getClientCompatibleFormat(arrPurchaseOrder.get(nIndex).getM_dPurchaseOrderDate())); 
		return arrPurchaseOrder;
	}
	
	private void createLog (PurchaseOrderData oPurchaseOrderData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oPurchaseOrderData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oPurchaseOrderData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void isCreditLimitExceeded(PurchaseOrderData oData) throws Exception
	{
		m_oLogger.debug("isCreditLimitExceeded - oSalesData : " + oData);
		InvoiceData oInvoiceData = new InvoiceData ();
		oInvoiceData.setM_oClientData(oData.getM_oClientData());
		oInvoiceData.setM_bIsForClientOutstanding(true);
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		ArrayList arrInvoice = new ArrayList (oInvoiceData.list (oOrderBy));
		m_oLogger.debug("isCreditLimitExceeded - arrInvoice.size() : " + arrInvoice.size());
		if (arrInvoice.size() > 0)
		  checkCreditExceeded (arrInvoice, oData);
	}
	
	@SuppressWarnings("unchecked")
	private void checkCreditExceeded(ArrayList arrInvoice, PurchaseOrderData oData) throws Exception
    {
		m_oLogger.info("checkCreditExceeded :: " + oData);
		Object[] arrInvoiceObject = (Object[])arrInvoice.get(0);
		ClientData oClientData = (ClientData) arrInvoiceObject[0];
		if(oClientData.getM_nCreditLimit() > 0)
		{
			double nBalanceAmount = (Double) arrInvoiceObject[3];
			double nOrderAmount = oData.calculateOrderTotal(oData.m_arrPurchaseOrderLineItems);
			double nExceededAmount = (nBalanceAmount + nOrderAmount) - oClientData.getM_nCreditLimit();
			m_oLogger.debug("checkCreditExceeded - nExceededAmount : " + nExceededAmount);
			if(nExceededAmount > 0)
						throw new Exception ("Client Credit Limit Exceeded! \n" +
						"Client Credit limit - " + oClientData.getM_nCreditLimit()+ "\n" +
 						"Client Balance Amount - " + nBalanceAmount + "\n" +
						"This Order Amount - " + nOrderAmount + "\n" +
						"Exceeded Amount - " + nExceededAmount);
		}
    }
	@Override
	public GenericResponse list(PurchaseOrderData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}