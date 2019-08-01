package com.techmust.helper;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.usermanagement.action.ActionData;
import com.techmust.usermanagement.action.ActionDataProcessor;
import com.techmust.usermanagement.action.ActionResponse;

@Controller
public class ActionHelper extends ActionDataProcessor 
{
	/**
	  * Logger variable
	 */
	Logger m_oLogger = Logger.getLogger(ActionHelper.class);
	
	@RequestMapping(value="/actionCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionResponse create (@RequestBody ActionData oData) 
	{
		ActionResponse oActionResponse = super.create(oData);
		return oActionResponse;
	}
	
	@RequestMapping(value="/actionGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionResponse  get(@RequestBody ActionData oData) 
	{
		ActionResponse oActionResponse = super.get(oData);
		return oActionResponse;
	}
	
	@RequestMapping(value="/actionList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionResponse list (@RequestBody ZenithHelper oData) 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strSortColumn(), oData.getM_strOrderBy());
		ActionResponse oActionResponse = super.list(oData.getM_oAction(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
		return oActionResponse;
	}
	
	@RequestMapping(value="/actionUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionResponse update (@RequestBody ActionData oData) 
	{
		ActionResponse oActionResponse = super.update(oData);
		return oActionResponse;
	}
	
	@RequestMapping(value="/actionDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionResponse deleteData (@RequestBody ActionData oData) 
	{
		ActionResponse oActionResponse = super.deleteData(oData);
		return oActionResponse;
	}
	
	@RequestMapping(value="/actionGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML (@RequestBody ActionData oData) 
	{
		String strXML = super.getXML(oData);
		m_oLogger.debug ("getXML - strXML : " +strXML);
		return strXML;
	}

}
