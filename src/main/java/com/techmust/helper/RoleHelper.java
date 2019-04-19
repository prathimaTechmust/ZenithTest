package com.techmust.helper;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.techmust.usermanagement.role.RoleData;
import com.techmust.usermanagement.role.RoleDataProcessor;
import com.techmust.usermanagement.role.RoleResponse;

@Controller
public class RoleHelper extends RoleDataProcessor{

	private static final long serialVersionUID = 1L;

	@RequestMapping(value="/roleCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public RoleResponse create (@RequestBody RoleData oData) 
	{
		RoleResponse oRoleResponse = super.create(oData);
		return oRoleResponse;
	}
	
	@RequestMapping(value="/roleGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public RoleResponse get (@RequestBody RoleData oData) 
	{
		RoleResponse oRoleResponse = super.get(oData);
		return oRoleResponse;
	}
	
	@RequestMapping(value="/roleListActionData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public RoleResponse listActionData () 
	{
		RoleResponse oRoleResponse = super.listActionData();
		return oRoleResponse;
	}
	
	@RequestMapping(value="/roleList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public RoleResponse list (@RequestBody TradeMustHelper oData) 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		RoleResponse oRoleResponse = super.list(oData.getM_oRoleData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
		return oRoleResponse;
	}
	
	@RequestMapping(value="/roleUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public RoleResponse update (@RequestBody RoleData oData) 
	{
		RoleResponse oRoleResponse = super.update(oData);
		return oRoleResponse;
	}
	
	@RequestMapping(value="/roleDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public RoleResponse deleteData (@RequestBody RoleData oData) 
	{
		RoleResponse oRoleResponse = super.deleteData(oData);
		return oRoleResponse;
	}
	
	@RequestMapping(value="/roleGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML (@RequestBody RoleData oData) 
	{
		String strXml = super.getXML(oData);
		return strXml;
	}
}
