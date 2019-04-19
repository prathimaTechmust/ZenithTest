package com.techmust.inventory.supply;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.purchase.PurchaseData;
import com.techmust.inventory.purchase.PurchaseDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class SupplyDataProcessor extends PurchaseDataProcessor
{

	@Override
	@RequestMapping(value="/supplyCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody PurchaseData oPurchaseData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oData [IN] : " + oPurchaseData);
		SupplyDataResponse oSupplyDataResponse = new SupplyDataResponse ();
		try
		{
			createLog(oPurchaseData, "SupplyDataProcessor.create : " + oPurchaseData.getM_strFrom());
			oPurchaseData.prepareSupplyData ();
			oSupplyDataResponse.m_bSuccess = oPurchaseData.saveObject();
			oPurchaseData.addPurchase();
			oSupplyDataResponse.m_arrPurchase.add(oPurchaseData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oSupplyDataResponse.m_bSuccess [OUT] : " + oSupplyDataResponse.m_bSuccess);
		return oSupplyDataResponse;
	}

	@Override
	public GenericResponse update(PurchaseData oPurchaseData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oPOData [IN] : " + oPurchaseData);
		SupplyDataResponse oSupplyDataResponse = new SupplyDataResponse ();
		try
		{
			createLog(oPurchaseData, "SupplyDataProcessor.update : " + oPurchaseData.getM_strFrom());
			oPurchaseData.prepareSupplyData ();
			oPurchaseData.removeOldPurchaseQty();
//			ArrayList<NonStockPurchaseLineItem> arrDeletedLineItems =  oPurchaseData.getDeletedNSLineItems();
			oSupplyDataResponse.m_bSuccess = oPurchaseData.updateObject();
			oPurchaseData.updatePurchaseQty();
//			oPurchaseData.removeNSItems (arrDeletedLineItems);
			oSupplyDataResponse.m_arrPurchase.add(oPurchaseData);	
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oSupplyDataResponse.m_bSuccess [OUT] : " + oSupplyDataResponse.m_bSuccess);
		return oSupplyDataResponse;
	}
	
	private void createLog (PurchaseData oData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
}
