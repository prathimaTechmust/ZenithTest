package com.techmust.helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.sales.ReportClientData;
import com.techmust.inventory.sales.SalesData;
import com.techmust.inventory.sales.SalesDataProcessor;
import com.techmust.vendormanagement.VendorData;
import com.techmust.vendormanagement.VendorDataResponse;

@Controller
public class SalesHelper extends SalesDataProcessor
{
	@RequestMapping(value="/SalesDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody SalesData oData) throws Exception 
	{
		return super.create(oData);
	}
	
	@RequestMapping(value="/salesDataList", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		return super.list (oTradeMustHelper.getM_oSalesData(), oOrderBy, oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
	}

	@RequestMapping(value="/saleslistClientSales", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
	@ResponseBody
	public GenericResponse listClientSales(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		return super.listClientSales (oTradeMustHelper.getM_oSalesData(), oOrderBy);
	}
	
	@RequestMapping(value="/salesGetXML", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody SalesData oData) throws Exception 
	{
	return super.getXML(oData);
	}

	@RequestMapping(value="/salesGet", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
	@ResponseBody
	public GenericResponse get (@RequestBody SalesData oData) throws Exception 
	{
	return super.get(oData);
	}
	
	@RequestMapping(value="/salesUpdateAndPrint", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
	@ResponseBody
	public GenericResponse updateAndPrint (@RequestBody SalesData oData) throws Exception 
	{
		return super.updateAndPrint(oData);
	}
	
	@RequestMapping(value="/salesSaveAndPrint", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
	@ResponseBody
	public GenericResponse saveAndPrint (@RequestBody SalesData oData) throws Exception 
	{
		return super.saveAndPrint(oData);
	}
	
	@RequestMapping(value="/salesgetClientReport", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
	@ResponseBody
	public ArrayList <ReportClientData> getClientReport (@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
	    return super.getClientReport(oTradeMustHelper.getM_oSalesData(), oOrderBy);
	}

	@RequestMapping(value="/salesDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update (@RequestBody SalesData oData) throws Exception 
	{
		return super.update(oData);
	}

}
