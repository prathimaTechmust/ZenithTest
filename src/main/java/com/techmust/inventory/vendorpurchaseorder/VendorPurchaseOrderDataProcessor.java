package com.techmust.inventory.vendorpurchaseorder;

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
import com.techmust.inventory.serial.SerialNumberData;
import com.techmust.inventory.serial.SerialType;

@Controller
public class VendorPurchaseOrderDataProcessor extends GenericIDataProcessor<VendorPurchaseOrderData> 
{
	@RequestMapping(value="/vendorPOCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	@Override
	public GenericResponse create(@RequestBody VendorPurchaseOrderData oPOData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oPOData);
		VendorPurchaseOrderDataResponse oVendorPurchaseOrderDataResponse = new VendorPurchaseOrderDataResponse ();
		try
		{
			oPOData.prepareForUpdate ();
			if(oPOData.getM_strPurchaseOrderNumber().isEmpty())
				oPOData.setM_strPurchaseOrderNumber(SerialNumberData.generateSerialNumber(SerialType.kVendorPONumber));
			oVendorPurchaseOrderDataResponse.m_bSuccess = oPOData.saveObject();
			oVendorPurchaseOrderDataResponse.m_arrVendorPurchaseOrderData.add(oPOData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oPurchaseOrderResponse.m_bSuccess [OUT] : " + oVendorPurchaseOrderDataResponse.m_bSuccess);
		return oVendorPurchaseOrderDataResponse;
	}
	
	@RequestMapping(value="/vendorPOSaveAndPrint", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse saveAndPrint(@RequestBody VendorPurchaseOrderData oPOData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oPOData);
		VendorPurchaseOrderDataResponse oVendorPurchaseOrderDataResponse = new VendorPurchaseOrderDataResponse ();
		try
		{
			oVendorPurchaseOrderDataResponse = oPOData.getM_nPurchaseOrderId() > 0 ?(VendorPurchaseOrderDataResponse) update(oPOData) : (VendorPurchaseOrderDataResponse) create(oPOData);
			oPOData = (VendorPurchaseOrderData) populateObject (oVendorPurchaseOrderDataResponse.m_arrVendorPurchaseOrderData.get(0));
			oVendorPurchaseOrderDataResponse.m_arrVendorPurchaseOrderData.add(oPOData);
			oVendorPurchaseOrderDataResponse.m_strXMLData = getXML(oPOData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oPurchaseOrderResponse.m_bSuccess [OUT] : " + oVendorPurchaseOrderDataResponse.m_bSuccess);
		return oVendorPurchaseOrderDataResponse;
	}

	@Override
	@RequestMapping(value="/vendorPODelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody VendorPurchaseOrderData oPOData)throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oData [IN] : " + oPOData);
		VendorPurchaseOrderDataResponse oVendorPurchaseOrderDataResponse = new VendorPurchaseOrderDataResponse ();
		try
		{
			oVendorPurchaseOrderDataResponse.m_bSuccess = oPOData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oVendorPurchaseOrderDataResponse.m_bSuccess [OUT] : " + oVendorPurchaseOrderDataResponse.m_bSuccess);
		return oVendorPurchaseOrderDataResponse;
	}

	@Override
	@RequestMapping(value="/vendorPOGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody VendorPurchaseOrderData oPOData) throws Exception 
	{
		m_oLogger.info ("get");
		VendorPurchaseOrderDataResponse oVendorPurchaseOrderDataResponse = new VendorPurchaseOrderDataResponse ();
		try 
		{
			oPOData = (VendorPurchaseOrderData) populateObject (oPOData);
			oPOData.setM_strPurchaseOrderDate(getClientCompatibleFormat(oPOData.getM_dPurchaseOrderDate()));
			oVendorPurchaseOrderDataResponse.m_arrVendorPurchaseOrderData.add(oPOData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  + oException);
			throw oException;
		}
		return oVendorPurchaseOrderDataResponse;
	}

	@Override
	@RequestMapping(value="/vendorPOGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody VendorPurchaseOrderData oPOData) throws Exception 
	{
		oPOData = (VendorPurchaseOrderData) populateObject(oPOData);
	    return oPOData != null ? oPOData.generateXML () : "";
	}
	
	@RequestMapping(value="/vendorPOList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list(oData.getM_oVPOrderData(),oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}

	@Override
	public GenericResponse list(VendorPurchaseOrderData oPOData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oPOData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")	
	public GenericResponse list(VendorPurchaseOrderData oPOData,HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)throws Exception 
	{
		m_oLogger.info("list");
		m_oLogger.debug("list - oPOData[IN] :" + oPOData);
		VendorPurchaseOrderDataResponse oVendorPurchaseOrderDataResponse = new VendorPurchaseOrderDataResponse ();
		try 
		{
			oVendorPurchaseOrderDataResponse.m_nRowCount = getRowCount(oPOData);
			oVendorPurchaseOrderDataResponse.m_arrVendorPurchaseOrderData = new ArrayList (oPOData.list (arrOrderBy, nPageNumber, nPageSize));
			oVendorPurchaseOrderDataResponse.m_arrVendorPurchaseOrderData = buildPurchaseOrderDate (oVendorPurchaseOrderDataResponse.m_arrVendorPurchaseOrderData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oVendorPurchaseOrderDataResponse;
	}

	@Override
	@RequestMapping(value="/vendorPOUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody VendorPurchaseOrderData oPOData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oPOData [IN] : " + oPOData);
		VendorPurchaseOrderDataResponse oVendorPurchaseOrderDataResponse = new VendorPurchaseOrderDataResponse ();
		try
		{
			oPOData.prepareForUpdate ();
			oVendorPurchaseOrderDataResponse.m_bSuccess = oPOData.updateObject();
			oVendorPurchaseOrderDataResponse.m_arrVendorPurchaseOrderData.add(oPOData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oVendorPurchaseOrderDataResponse.m_bSuccess [OUT] : " + oVendorPurchaseOrderDataResponse.m_bSuccess);
		return oVendorPurchaseOrderDataResponse;
	}
	
	private ArrayList<VendorPurchaseOrderData> buildPurchaseOrderDate(ArrayList<VendorPurchaseOrderData> arrPurchaseOrder) 
	{
		m_oLogger.info("buildPurchaseOrderDate");
		for (int nIndex=0; nIndex < arrPurchaseOrder.size(); nIndex++)
			arrPurchaseOrder.get(nIndex).setM_strPurchaseOrderDate(getClientCompatibleFormat(arrPurchaseOrder.get(nIndex).getM_dPurchaseOrderDate())); 
		return arrPurchaseOrder;
	}
}
