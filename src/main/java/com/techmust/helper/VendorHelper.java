package com.techmust.helper;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ClientDataResponse;
import com.techmust.generic.response.GenericResponse;
import com.techmust.vendormanagement.VendorData;
import com.techmust.vendormanagement.VendorDataProcessor;
import com.techmust.vendormanagement.VendorDataResponse;

@Controller
public class VendorHelper extends VendorDataProcessor
{
	@RequestMapping(value="/vendorDataListVendor", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public VendorDataResponse listVendor(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception
	{
		VendorDataResponse oVendorDataResponse=super.listVendor(oTradeMustHelper.getM_oVendorData(), oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		return oVendorDataResponse;
	}
	
	Logger oLogger = Logger.getLogger(VendorHelper.class.getName());
	@RequestMapping(value="/vendorDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ClientDataResponse createVendor(@RequestBody VendorData oVendorData)
	{
		return  super.createVendor(oVendorData);
		
	}
	
	@RequestMapping(value="/vendorDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public VendorDataResponse updateVendor(@RequestBody VendorData oVendorData)
	{
		VendorDataResponse oVendorDataResponse = (VendorDataResponse) super.updateVendor(oVendorData);
		return oVendorDataResponse;
	}
	
	@RequestMapping(value="/getVendorSuggesstions", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public VendorDataResponse getVendorSuggesstions(@RequestBody TradeMustHelper oData)throws Exception
	{
		return super.getVendorSuggesstions(oData.getM_oVendorData(), oData.getM_strColumn(), oData.getM_strOrderBy());
	}
	
	@RequestMapping(value="/updateVendorBalanceData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public VendorDataResponse updateVendorBalanceData (@RequestBody VendorData oVendorData)
	{
		return super.updateVendorBalanceData(oVendorData);
	}
	
	@RequestMapping(value="/getVendor", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public VendorDataResponse getVendor (@RequestBody VendorData oVendorData)
	{
		return super.getVendor(oVendorData);
	}
	
	@RequestMapping(value="/deleteVendorData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteVendorData (@RequestBody VendorData oVendorData)
	{
		return super.deleteVendorData(oVendorData);
	}
}
