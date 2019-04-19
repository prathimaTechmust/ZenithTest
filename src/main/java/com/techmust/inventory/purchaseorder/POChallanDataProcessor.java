package com.techmust.inventory.purchaseorder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.challan.ChallanData;
import com.techmust.inventory.challan.ChallanDataResponse;
import com.techmust.inventory.sales.SalesData;

public class POChallanDataProcessor extends PurchaseOrderDataProcessor
{
	public GenericResponse createChallan (PurchaseOrderData oPOData) throws Exception
	{
		m_oLogger.info ("createChallan");
		m_oLogger.debug ("createChallan - oPurchaseOrderData [IN] : " + oPOData);
		ChallanDataResponse oChallanDataResponse = new ChallanDataResponse ();
		try
		{
			oChallanDataResponse.m_bSuccess = create (oPOData).m_bSuccess;
			if (oChallanDataResponse.m_bSuccess)
			{
				SalesData oSalesData = oPOData.createSalesData();
				ChallanData oChallanData = new ChallanData (); //(ChallanData)populateObject(oSalesData.createChallan());
				oPOData.addChallan (oChallanData);
				oChallanDataResponse.m_strXMLData = oChallanData.generateXML();
				oChallanDataResponse.m_arrChallan.add(oChallanData);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("createChallan - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("createChallan - oChallanDataResponse.m_bSuccess [OUT] : " + oChallanDataResponse.m_bSuccess);
		return oChallanDataResponse;
	}
	
	
	public GenericResponse updateAndMakeChallan (PurchaseOrderData oPOData) throws Exception
	{
		m_oLogger.info ("updateAndMakeChallan");
		m_oLogger.debug ("updateAndMakeChallan - oPurchaseOrderData [IN] : " + oPOData);
		ChallanDataResponse oChallanDataResponse = new ChallanDataResponse ();
		try
		{
			oChallanDataResponse.m_bSuccess = update (oPOData).m_bSuccess;
			if (oChallanDataResponse.m_bSuccess)
			{
				SalesData oSalesData = oPOData.createSalesData ();
				ChallanData oChallanData = new ChallanData (); //(ChallanData)populateObject(oSalesData.createChallan());
				oPOData.addChallan (oChallanData);
				oChallanDataResponse.m_strXMLData = oChallanData.generateXML();
			oChallanDataResponse.m_arrChallan.add(oChallanData);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error("updateAndMakeChallan - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("updateAndMakeChallan - oChallanDataResponse.m_bSuccess [OUT] : " + oChallanDataResponse.m_bSuccess);
		return oChallanDataResponse;
	}
	
	public GenericResponse getUnbilledChallans (PurchaseOrderData oPurchaseOrderData)
	{
		m_oLogger.info ("getUnbilledChallan");
		m_oLogger.debug ("getUnbilledChallan - oPurchaseOrderData [IN] : " + oPurchaseOrderData);
		
		POChallanDataResponse oPOChallanDataResponse = new POChallanDataResponse ();
		try
		{
			oPurchaseOrderData = (PurchaseOrderData) populateObject (oPurchaseOrderData);
			oPOChallanDataResponse.m_arrChallanData = oPurchaseOrderData.getUnbilledChallans ();
		}
		catch (Exception oException)
		{
			m_oLogger.error("getUnbilledChallans - oException : ", oException);
		}
		
		return oPOChallanDataResponse;
	}

	public GenericResponse getChallans (PurchaseOrderData oPurchaseOrderData)
	{
		m_oLogger.info ("getChallans");
		m_oLogger.debug ("getChallans - oPurchaseOrderData [IN] : " + oPurchaseOrderData);
		
		POChallanDataResponse oPOChallanDataResponse = new POChallanDataResponse ();
		try
		{
			oPurchaseOrderData = (PurchaseOrderData) populateObject (oPurchaseOrderData);
			oPOChallanDataResponse.m_arrChallanData = oPurchaseOrderData.getChallans ();
		}
		catch (Exception oException)
		{
			m_oLogger.error("getChallans - oException : ", oException);
		}
		return oPOChallanDataResponse;
	}
}
