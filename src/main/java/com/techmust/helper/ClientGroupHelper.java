package com.techmust.helper;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ClientGroupData;
import com.techmust.clientmanagement.ClientGroupDataProcessor;
import com.techmust.generic.response.GenericResponse;

@Controller
public class ClientGroupHelper extends ClientGroupDataProcessor {

	@RequestMapping(value="/clientGroupDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	 public GenericResponse create(@RequestBody ClientGroupData oData) throws Exception
	 {
		 GenericResponse oGenericResponse = super.create(oData);
		 return oGenericResponse;
	 }
	
	@RequestMapping(value="/clientGroupDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		return super.list(oTradeMustHelper.getM_oClientGroup (), oOrderBy, oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
	}
	
	@RequestMapping(value="/clientGroupDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody ClientGroupData oData) throws Exception
	{
	  GenericResponse oGenericResponse = super.get(oData);
		return oGenericResponse;
	  
	}
	
	@RequestMapping(value="/clientGroupDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody ClientGroupData oData) throws Exception
	{
	  GenericResponse oGenericResponse = super.update(oData);
		return oGenericResponse;
	  
	}
	@RequestMapping(value="/clientGroupDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody ClientGroupData oData) throws Exception
	{
	  String oGenericResponse = super.getXML(oData);
		return oGenericResponse;
	  
	}
	
	@RequestMapping(value="/clientGroupDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody ClientGroupData oData) throws Exception
	{
	  GenericResponse oGenericResponse = super.deleteData(oData);
		return oGenericResponse;
	  
	}
}
