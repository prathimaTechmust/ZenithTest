package com.techmust.helper;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.usermanagement.actionarea.ActionAreaData;
import com.techmust.usermanagement.actionarea.ActionAreaDataProcessor;
import com.techmust.usermanagement.actionarea.ActionAreaResponse;

@Controller
public class ActionAreaHelper extends ActionAreaDataProcessor {

	@RequestMapping(value="/actionAreaCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionAreaResponse create (@RequestBody ActionAreaData oData) 
	{
		ActionAreaResponse oActionAreaResponse = super.create(oData);
		return oActionAreaResponse;
	}
	
	@RequestMapping(value="/actionAreaGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionAreaResponse  get(@RequestBody ActionAreaData oData) 
	{
		ActionAreaResponse oActionAreaResponse = super.get(oData);
		return oActionAreaResponse;
	}
	
	@RequestMapping(value="/actionAreaList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionAreaResponse list (@RequestBody TradeMustHelper oData) 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		ActionAreaResponse oActionAreaResponse = super.list(oData.getM_oActionArea(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
		return oActionAreaResponse;
	}
	
	@RequestMapping(value="/actionAreaUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionAreaResponse update (@RequestBody ActionAreaData oData) 
	{
		ActionAreaResponse oActionAreaResponse = super.update(oData);
		return oActionAreaResponse;
	}
	
	@RequestMapping(value="/actionAreaDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionAreaResponse deleteData (@RequestBody ActionAreaData oData) 
	{
		ActionAreaResponse oActionAreaResponse = super.deleteData(oData);
		return oActionAreaResponse;
	}
	
	@RequestMapping(value="/actionAreaGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML (@RequestBody ActionAreaData oData) 
	{
		String strXML = super.getXML(oData);
		return strXML;
	}
}
