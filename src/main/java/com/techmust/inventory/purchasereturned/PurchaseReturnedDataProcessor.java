package com.techmust.inventory.purchasereturned;

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
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.vendormanagement.VendorData;

@Controller
public class PurchaseReturnedDataProcessor extends GenericIDataProcessor<PurchaseReturnedData> 
{

	@Override
	@RequestMapping(value="/purchaseReturnedCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody PurchaseReturnedData oPurchaseReturnedData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oPurchaseReturnedData [IN] : " + oPurchaseReturnedData);
		PurchaseReturnedDataResponse oResponse = new PurchaseReturnedDataResponse ();
		try
		{
			createLog(oPurchaseReturnedData, "PurcahseReturnedDataProcessor.create");
			oPurchaseReturnedData.prepareReturnData ();
			oPurchaseReturnedData.setM_strDebitNoteNumber(SerialNumberData.generateSerialNumber(SerialType.kDebitNoteNumber));
			oResponse.m_bSuccess = oPurchaseReturnedData.saveObject();
			oPurchaseReturnedData.returned(true);
			oPurchaseReturnedData = (PurchaseReturnedData) populateObject(oPurchaseReturnedData);
			oResponse.m_arrPurchaseReturnedData.add(oPurchaseReturnedData);
		}
		catch (Exception oException)
		{
			oResponse.m_strError_Desc = oException.getMessage();
			m_oLogger.error ("create - oException : " + oException);
		}
		m_oLogger.debug("create - oResponse [OUT] : " + oResponse.m_bSuccess);
		return oResponse;
	}

	@Override
	@RequestMapping(value="/purchaseReturnedDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody PurchaseReturnedData oPurchaseReturnedData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oPurchaseReturnedData [IN] : " + oPurchaseReturnedData);
		PurchaseReturnedDataResponse oResponse = new PurchaseReturnedDataResponse ();
		try
		{
			oPurchaseReturnedData = (PurchaseReturnedData) populateObject (oPurchaseReturnedData);
			oResponse.m_bSuccess = oPurchaseReturnedData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oResponse.m_bSuccess [OUT] : " + oResponse.m_bSuccess);
		return oResponse;
	}

	@Override
	@RequestMapping(value="/purchaseReturnedGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody PurchaseReturnedData oPurchaseReturnedData) throws Exception 
	{
		m_oLogger.info ("get");
		PurchaseReturnedDataResponse oResponse = new PurchaseReturnedDataResponse ();
		try 
		{
			oPurchaseReturnedData = (PurchaseReturnedData) populateObject (oPurchaseReturnedData);
			oResponse.m_arrPurchaseReturnedData.add(oPurchaseReturnedData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oResponse;
	}

	@Override
	@RequestMapping(value="/purchaseReturnedGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody PurchaseReturnedData oPurchaseReturnedData) throws Exception 
	{
		oPurchaseReturnedData = (PurchaseReturnedData) populateObject(oPurchaseReturnedData);
	    return oPurchaseReturnedData != null ? oPurchaseReturnedData.generateXML () : "";
	}
	
	@RequestMapping(value="/purchaseReturnedList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list(oData.getM_oPReturnedData(),oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}

	@Override
	public GenericResponse list(PurchaseReturnedData oPurchaseReturnedData,HashMap<String, String> arrOrderBy) throws Exception 
	{
		return (PurchaseReturnedDataResponse) list(oPurchaseReturnedData,arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
    public GenericResponse list(PurchaseReturnedData oPurchaseReturnedData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oPurchaseReturnedData [IN] : " +oPurchaseReturnedData);
		PurchaseReturnedDataResponse oResponse = new PurchaseReturnedDataResponse ();
		try 
		{
			oResponse.m_nRowCount = getRowCount(oPurchaseReturnedData);
			oResponse.m_arrPurchaseReturnedData = new ArrayList (oPurchaseReturnedData.list (arrOrderBy, nPageNumber, nPageSize));
			oResponse.m_arrPurchaseReturnedData = buildPurchaseReturnedData (oResponse.m_arrPurchaseReturnedData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oResponse;
	}

	private ArrayList<PurchaseReturnedData> buildPurchaseReturnedData (ArrayList<PurchaseReturnedData> arrPurchaseReturnedData) 
	{
		m_oLogger.info("buildPurchaseReturnedData");
		for (int nIndex=0; nIndex < arrPurchaseReturnedData.size(); nIndex++)
			arrPurchaseReturnedData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrPurchaseReturnedData.get(nIndex).getM_dCreatedOn())); 
		return arrPurchaseReturnedData;
	}

	@Override
	@RequestMapping(value="/purchaseReturnedUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody PurchaseReturnedData oPurchaseReturnedData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oPurchaseReturnedData [IN] : " + oPurchaseReturnedData);
		PurchaseReturnedDataResponse oResponse = new PurchaseReturnedDataResponse ();
		try
		{
			createLog(oPurchaseReturnedData, "PurcahseReturnedDataProcessor.create");
			PurchaseReturnedData oDBReturnedData = (PurchaseReturnedData) populateObject(oPurchaseReturnedData);
			oDBReturnedData.returned (false);
			oPurchaseReturnedData.prepareReturnData ();
			PurchaseReturnedData oRemovedReturns = oPurchaseReturnedData.getRemovedReturns ();
			oResponse.m_bSuccess = oPurchaseReturnedData.updateObject();
			oPurchaseReturnedData.returned(true);
			oPurchaseReturnedData.deleteReturns (oRemovedReturns);
			oPurchaseReturnedData = (PurchaseReturnedData) populateObject(oPurchaseReturnedData);
			oResponse.m_arrPurchaseReturnedData.add(oPurchaseReturnedData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oResponse.m_bSuccess [OUT] : " + oResponse.m_bSuccess);
		return oResponse;
	}
	
	@RequestMapping(value="/saveAndPrint", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse saveAndPrint(@RequestBody PurchaseReturnedData oReturnedData) throws Exception
	{
		m_oLogger.info("saveAndPrint");
		m_oLogger.debug("saveAndPrint - oReturnedData [IN] : " + oReturnedData);
		PurchaseReturnedDataResponse oResponse = new PurchaseReturnedDataResponse ();
		try
		{
			oResponse = prepareForPrint((PurchaseReturnedDataResponse) create(oReturnedData));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("saveAndPrint - oException : " + oException);
		}
		return oResponse;
	}
	
	@RequestMapping(value="/updateAndPrint", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse updateAndPrint(@RequestBody PurchaseReturnedData oReturnedData) throws Exception
	{
		m_oLogger.info("updateAndPrint");
		m_oLogger.debug("updateAndPrint - oReturnedData [IN] : " + oReturnedData);
		PurchaseReturnedDataResponse oResponse = new PurchaseReturnedDataResponse ();
		try
		{
			oResponse = prepareForPrint ((PurchaseReturnedDataResponse) update(oReturnedData));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateAndPrint - oException : " + oException);
		}
		return oResponse;
	}

	protected PurchaseReturnedDataResponse prepareForPrint (PurchaseReturnedDataResponse oReturnedDataResponse) throws Exception 
	{
		PurchaseReturnedData oReturnedData = oReturnedDataResponse.m_arrPurchaseReturnedData.get(0);
		VendorData oVendorData = (VendorData) populateObject(oReturnedData.getM_oVendorData());
		oReturnedData.setM_oVendorData(oVendorData);
		oReturnedDataResponse.m_strXMLData = oReturnedData.generateXML();	
		return oReturnedDataResponse;
	}

	private void createLog (PurchaseReturnedData oPurchaseReturnedData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oPurchaseReturnedData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oPurchaseReturnedData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
}
