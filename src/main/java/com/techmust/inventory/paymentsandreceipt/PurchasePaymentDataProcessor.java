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
import com.techmust.inventory.purchase.PurchaseDataResponse;
@Controller
public class PurchasePaymentDataProcessor extends GenericIDataProcessor<PurchasePaymentData> {

	@RequestMapping(value="/PurchasePaymentDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody PurchasePaymentData arg0) throws Exception 
	{
		return null;
	}

	@RequestMapping(value="/PurchasePaymentDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody PurchasePaymentData arg0) throws Exception 
	{
		return null;
	}

	@RequestMapping(value="/PurchasePaymentDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody PurchasePaymentData arg0) throws Exception
	{
		return null;
	}

	@RequestMapping(value="/PurchasePaymentDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody PurchasePaymentData arg0) throws Exception 
	{
		return null;
	}

	@RequestMapping(value="/PurchasePaymentDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		GenericResponse oPurchasePaymentDataResponse = list(oTradeMustHelper.getM_oPurchasePaymentData(), oOrderBy);
		return oPurchasePaymentDataResponse;
	}
	
	public GenericResponse list(PurchasePaymentData oPurchasePaymentData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oPurchasePaymentData [IN] : " + oPurchasePaymentData);
		PurchasePaymentDataResponse oPurchasePaymentDataResponse = new PurchasePaymentDataResponse ();
		try 
		{
			oPurchasePaymentDataResponse.m_arrPurchasePaymentData = new ArrayList (oPurchasePaymentData.list (arrOrderBy));
			oPurchasePaymentDataResponse.m_arrPurchasePaymentData = buildPaymentData (oPurchasePaymentDataResponse.m_arrPurchasePaymentData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		m_oLogger.debug("list - oPurchasePaymentDataResponse.m_arrPurchasePaymentData.size () [OUT] : " + oPurchasePaymentDataResponse.m_arrPurchasePaymentData.size());
		return oPurchasePaymentDataResponse;
	}

	private ArrayList<PurchasePaymentData> buildPaymentData (ArrayList<PurchasePaymentData> arrPurchasePaymentData)
	{
		m_oLogger.info("buildPaymentData");
		for (int nIndex=0; nIndex < arrPurchasePaymentData.size(); nIndex++)
			arrPurchasePaymentData.get(nIndex).getM_oPaymentData().setM_strDate(getClientCompatibleFormat(arrPurchasePaymentData.get(nIndex).getM_oPaymentData().getM_dCreatedOn())); 
		return arrPurchasePaymentData;
	}

	@RequestMapping(value="/PurchasePaymentDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody PurchasePaymentData arg0) throws Exception 
	{
		return null;
	}

}
