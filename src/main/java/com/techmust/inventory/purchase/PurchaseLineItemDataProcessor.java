package com.techmust.inventory.purchase;

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
public class PurchaseLineItemDataProcessor extends GenericIDataProcessor<PurchaseLineItem> 
{

	@Override
	public GenericResponse create(PurchaseLineItem oPurchaseLineItem) throws Exception 
	{
	
		return null;
	}

	@Override
	public GenericResponse deleteData(PurchaseLineItem oPurchaseLineItem) throws Exception 
	{
		
		return null;
	}

	@Override
	public GenericResponse get(PurchaseLineItem oPurchaseLineItem) throws Exception
	{
		
		return null;
	}

	@Override
	public String getXML(PurchaseLineItem oPurchaseLineItem) throws Exception 
	{
		
		return null;
	}

	@RequestMapping(value="/PurchaseLineItemDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		GenericResponse oDataResponse = list(oTradeMustHelper.getM_oPurchaseLineItem(), oOrderBy);
		return oDataResponse;
	}
	public GenericResponse list(PurchaseLineItem oPurchaseLineItem, HashMap<String, String> arrOrderBy)
			throws Exception
	{
		m_oLogger.debug("list - oPurchaseLineItem [IN]: " + oPurchaseLineItem);
		PurchaseLineItemDataResponse oDataResponse = new PurchaseLineItemDataResponse ();
		try 
		{
			oDataResponse.m_arrPurchaseLineItem = new ArrayList (oPurchaseLineItem.list (arrOrderBy));
			for (int nIndex = 0;nIndex < oDataResponse.m_arrPurchaseLineItem.size () ; nIndex++)
				oDataResponse.m_arrPurchaseLineItem.get(nIndex).getM_oPurchaseData().setM_strDate(getClientCompatibleFormat(oDataResponse.m_arrPurchaseLineItem.get(nIndex).getM_oPurchaseData().getM_dDate()));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException" + oException);
		}
		return oDataResponse;
	}

	@Override
	public GenericResponse update(PurchaseLineItem oPurchaseLineItem) throws Exception 
	{
		return null;
	}
}
