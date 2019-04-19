package com.techmust.helper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.purchaseorder.PurchaseOrderData;
import com.techmust.inventory.purchaseorder.PurchaseOrderDataProcessor;
import com.techmust.inventory.purchaseorder.PurchaseOrderResponse;

@Controller
public class PurchaseOrderHelper extends PurchaseOrderDataProcessor
{
	@RequestMapping(value="/PurchaseOrderDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create( @RequestBody PurchaseOrderData oPOData) throws Exception 
	{
		return super.create(oPOData);
	}
	
	@RequestMapping(value="/PurchaseOrderDataValidateOrderNumber", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public PurchaseOrderResponse validateOrderNumber (@RequestBody PurchaseOrderData oPurchaseOrderData) throws Exception
	{
		return super.validateOrderNumber(oPurchaseOrderData);
	}

	@RequestMapping(value="/PurchaseOrderDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody PurchaseOrderData oData) throws Exception 
	{
		return super.deleteData(oData);
	}
	
	@RequestMapping(value="/PurchaseOrderDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody PurchaseOrderData oData) throws Exception 
	{
	return super.get(oData);
	}
	
	@RequestMapping(value="/PurchaseOrderDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody PurchaseOrderData oData) throws Exception 
	{
		return super.getXML(oData);
	}
	
	@RequestMapping(value="/PurchaseOrderDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		return super.list(oTradeMustHelper);
	}
	
	@RequestMapping(value="/PurchaseOrderDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody PurchaseOrderData oPOData) throws Exception 
	{
		return super.update(oPOData);
	}
	
	@RequestMapping(value="/PurchaseOrderDataDeletePurchaseOrderLineItems", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deletePurchaseOrderLineItems (@RequestBody PurchaseOrderData oPurchaseOrderData) throws Exception
	{
	 return super.deletePurchaseOrderLineItems(oPurchaseOrderData);
	}
	
	@RequestMapping(value="/PurchaseOrderDataGetPurchases", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getPurchases (@RequestBody PurchaseOrderData oPurchaseOrderData)
	{
		return super.getPurchases(oPurchaseOrderData);
	}
	
	@RequestMapping(value="/PurchaseOrderDataCancelOrder", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse cancelOrder (@RequestBody PurchaseOrderData oPurchaseOrderData)
	{
		return super.cancelOrder(oPurchaseOrderData);
	}
	
	@RequestMapping(value="/PurchaseOrderDataReallow", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse reallow (@RequestBody PurchaseOrderData oPurchaseOrderData)
	{
		return super.reallow(oPurchaseOrderData);
	}
	
}
