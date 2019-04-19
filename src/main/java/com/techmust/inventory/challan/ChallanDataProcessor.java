package com.techmust.inventory.challan;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.invoice.InvoiceData;
import com.techmust.inventory.invoice.InvoiceDataResponse;
import com.techmust.inventory.purchaseorder.PurchaseOrderData;
import com.techmust.inventory.sales.SalesData;
import com.techmust.inventory.sales.SalesDataProcessor;
import com.techmust.inventory.sales.SalesDataResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class ChallanDataProcessor extends SalesDataProcessor
{
	@RequestMapping(value="/challanCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody SalesData oSalesData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oSalesData);
		ChallanDataResponse oChallanDataResponse = new ChallanDataResponse ();
		SalesDataResponse oSalesDataResponse = new SalesDataResponse ();
		try
		{
			createLog(oSalesData, "ChallanDataProcessor.create : " + oSalesData.getM_strTo());
			oSalesDataResponse = (SalesDataResponse) super.create(oSalesData);
			oChallanDataResponse.m_bSuccess = oSalesDataResponse.m_bSuccess;
			oSalesData = oSalesDataResponse.m_arrSales.get(0);
			oSalesData = (SalesData) populateObject (oSalesData);
			ChallanData oChallanData = new ChallanData (); 
			oSalesData.createChallan ();
			oChallanDataResponse.m_strXMLData = oChallanData.generateXML();
			oChallanDataResponse.m_arrChallan.add(oChallanData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oChallanDataResponse.m_bSuccess [OUT] : " + oChallanDataResponse.m_bSuccess);
		return oChallanDataResponse;
	}
	@RequestMapping(value="/challanDeleteData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody ChallanData oData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " + oData);
		ChallanDataResponse oChallanDataResponse = new ChallanDataResponse ();
		try
		{
			createLog(oData.getM_oSalesData(), "ChallanDataProcessor.deleteData : " + oData.getM_strChallanNumber());
			oChallanDataResponse.m_bSuccess = oData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oChallanDataResponse.m_bSuccess [OUT] : " + oChallanDataResponse.m_bSuccess);
		return oChallanDataResponse;
	}
	@RequestMapping(value="/challanGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody ChallanData oData) throws Exception 
	{
		m_oLogger.info ("get");
		ChallanDataResponse oChallanDataResponse = new ChallanDataResponse ();
		try 
		{
			oData = (ChallanData) populateObject (oData);
			oChallanDataResponse.m_arrChallan.add(oData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oChallanDataResponse;
	}

	@RequestMapping(value="/challanGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody ChallanData oData) throws Exception 
	{
		oData = (ChallanData) populateObject(oData);
		oData.setM_strDate(getClientCompatibleFormat(oData.getM_dCreatedOn()));
		oData.setM_strTime(oData.getM_dCreatedOn());
	    return oData != null ? oData.generateXML () : "";
	}
	
	@RequestMapping(value="/challanList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list(oData.getM_oChallanData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	public GenericResponse list(ChallanData oData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse list(ChallanData oData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oData [IN] : " +oData);

		ChallanDataResponse oChallanDataResponse = new ChallanDataResponse ();
		try 
		{
			oChallanDataResponse.m_nRowCount = getRowCount(oData);
			oChallanDataResponse.m_arrChallan = buildChallanData (new ArrayList (oData.list (arrOrderBy, nPageNumber, nPageSize)));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oChallanDataResponse;
	}
	@RequestMapping(value="/challanUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody SalesData oSalesData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oData [IN] : " + oSalesData);
		ChallanDataResponse oChallanDataResponse = new ChallanDataResponse ();
		try
		{
			createLog(oSalesData, "ChallanDataProcessor.update : " + oSalesData.getM_strChallanNumber());
			oChallanDataResponse.m_bSuccess = super.update(oSalesData).m_bSuccess;
			oSalesData = (SalesData) populateObject (oSalesData);
			ChallanData oChallanData = new ChallanData (); //oSalesData.createChallan();
			oChallanDataResponse.m_strXMLData = oChallanData.generateXML();
			oChallanDataResponse.m_arrChallan.add(oChallanData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oChallanDataResponse.m_bSuccess [OUT] : " + oChallanDataResponse.m_bSuccess);
		return oChallanDataResponse;
	}
	
	public GenericResponse makeInvoice (ChallanData oChallanData, UserInformationData oCreatedBy) throws Exception
    {
    	m_oLogger.info ("makeInvoice");
		m_oLogger.debug ("makeInvoice - oChallanData [IN] : " + oChallanData);
		InvoiceDataResponse oInvoiceDataResponse = new InvoiceDataResponse ();
		try
		{
			InvoiceData oInvoiceData = new InvoiceData ();
			oChallanData = (ChallanData) populateObject(oChallanData);
			if (oChallanData.getM_oSalesData().getM_oPOData()!= null && oChallanData.getM_oSalesData().getM_oPOData().getM_nPurchaseOrderId() > 0)
			{
				PurchaseOrderData oPurchaseOrderData = (PurchaseOrderData) populateObject(oChallanData.getM_oSalesData().getM_oPOData());
				oPurchaseOrderData.m_arrChallans = new ChallanData [1];
				oPurchaseOrderData.m_arrChallans[0] =  oChallanData;
				oPurchaseOrderData.setM_nCreatedBy(oCreatedBy.getM_nUserId());
				oInvoiceData = oInvoiceData.create(oPurchaseOrderData);
			}
			else
			{
				oInvoiceData = oInvoiceData.create(oChallanData.getM_oSalesData(), oCreatedBy.getM_nUserId());
				oChallanData.setM_oInvoiceData(oInvoiceData);
				oChallanData.updateObject();
			}
			oInvoiceDataResponse.m_bSuccess = true;
			oInvoiceDataResponse.m_arrInvoice.add(oInvoiceData);
			oInvoiceDataResponse.m_strXMLData = oInvoiceData.generateXML();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("makeInvoice - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("makeInvoice - oInvoiceDataResponse.m_bSuccess [OUT] : " + oInvoiceDataResponse.m_bSuccess);
		return oInvoiceDataResponse;
    }
	
	private ArrayList<ChallanData> buildChallanData(ArrayList<ChallanData> arrChallanData) 
	{
		m_oLogger.info("buildChallanData");
		for (int nIndex=0; nIndex < arrChallanData.size(); nIndex++)
		{
//			arrChallanData.get(nIndex).getM_oSalesData().setM_strDate(getClientCompatibleFormat(arrChallanData.get(nIndex).getM_dCreatedOn()));
			arrChallanData.get(nIndex).setM_nChallanAmount(arrChallanData.get(nIndex).getM_oSalesData().getSalesTotal ());
		}
		return arrChallanData;
	}
	
	private void createLog (SalesData oSalesData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oSalesData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oSalesData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
}
