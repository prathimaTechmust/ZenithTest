package com.techmust.helper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.purchaseorder.POChallanDataProcessor;
import com.techmust.inventory.purchaseorder.PurchaseOrderData;

@Controller
public class POChallanDataHelper 
{
	POChallanDataProcessor oDataProcessor = new POChallanDataProcessor ();
	@RequestMapping(value="/POChallanDatacreateChallan", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse createChallan (@RequestBody PurchaseOrderData oPOData) throws Exception
	{
		return oDataProcessor.createChallan(oPOData);
	}
	
	@RequestMapping(value="/POChallanDataupdateAndMakeChallan", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse updateAndMakeChallan (@RequestBody PurchaseOrderData oPOData) throws Exception
	{
		return oDataProcessor.updateAndMakeChallan(oPOData);
	}
	
	@RequestMapping(value="/POChallanDataunBilledChallanChallan", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getUnbilledChallans (@RequestBody PurchaseOrderData oPurchaseOrderData)
	{
		return oDataProcessor.getUnbilledChallans(oPurchaseOrderData);
	}
	
	@RequestMapping(value="/POChallanDatagetChallan", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getChallans (@RequestBody PurchaseOrderData oPurchaseOrderData)
	{
		return oDataProcessor.getChallans(oPurchaseOrderData);
	}
	
}
