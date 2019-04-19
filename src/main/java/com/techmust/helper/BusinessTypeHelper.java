package com.techmust.helper;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.master.businesstype.BusinessTypeData;
import com.techmust.master.businesstype.BusinessTypeDataProcessor;
import com.techmust.master.businesstype.BusinessTypeResponse;

@Controller
public class BusinessTypeHelper extends BusinessTypeDataProcessor {
	
	@RequestMapping(value="/businessTypeDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public BusinessTypeResponse create(@RequestBody BusinessTypeData oData)
	{
		
		BusinessTypeResponse oBusinessTypeResponse = super.create(oData);
		return oBusinessTypeResponse;
		
	}
	@RequestMapping(value="/businessTypeDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public BusinessTypeResponse list(@RequestBody TradeMustHelper oTradeMustHelper)
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		BusinessTypeResponse oBusinessTypeResponse = super.list(oTradeMustHelper.getM_oBusinessTypeData(), oOrderBy, oTradeMustHelper.getM_nPageNo(), oTradeMustHelper.getM_nPageSize());
		return oBusinessTypeResponse;
	}
	
	@RequestMapping(value="/businessTypeDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public BusinessTypeResponse get(@RequestBody BusinessTypeData oBusinessTypeData)
	{
		BusinessTypeResponse oBusinessTypeResponse = super.get(oBusinessTypeData);
		return oBusinessTypeResponse;
	}
	
	@RequestMapping(value="/businessTypeDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public BusinessTypeResponse update(@RequestBody BusinessTypeData oBusinessTypeData)
	{
		BusinessTypeResponse oBusinessTypeResponse = super.update(oBusinessTypeData);
		return oBusinessTypeResponse;
	}
	
	@RequestMapping(value="/businessTypeDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody BusinessTypeData oBusinessTypeData)
	{
		String oBusinessTypeResponse = super.getXML(oBusinessTypeData);
		return oBusinessTypeResponse;
	}
	
	@RequestMapping(value="/businessTypeDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public BusinessTypeResponse deleteData(@RequestBody BusinessTypeData oBusinessTypeData)
	{
		BusinessTypeResponse oBusinessTypeResponse = super.deleteData(oBusinessTypeData);
		return oBusinessTypeResponse;
	}

}
