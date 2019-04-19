package com.techmust.helper;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ClientDataProcessor;
import com.techmust.clientmanagement.ClientDataResponse;

@Controller 
public class ClientHelper extends ClientDataProcessor {

	@RequestMapping(value="/clientDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ClientDataResponse create(@RequestBody ClientData oClientData)
	{
		ClientDataResponse oClientDataResponse = super.create(oClientData);
		return oClientDataResponse;
	}
	
	@RequestMapping(value="/clientDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ClientDataResponse list(@RequestBody TradeMustHelper oTradeMustHelper)
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		ClientDataResponse oClientDataResponse = super.list(oTradeMustHelper.getM_oClientData(), oOrderBy, oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
		return oClientDataResponse;
	  
	}
	
	@RequestMapping(value="/clientDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ClientDataResponse get(@RequestBody ClientData oClientData)
	{
		ClientDataResponse oClientDataResponse = super.get(oClientData);
		return oClientDataResponse;
	}
	
	@RequestMapping(value="/clientDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ClientDataResponse update(@RequestBody ClientData oClientData)
	{
		ClientDataResponse oClientDataResponse = super.update(oClientData);
		return oClientDataResponse;
	}
	
	@RequestMapping(value="/clientDeleteData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ClientDataResponse deleteData(@RequestBody ClientData oClientData)
	{
		ClientDataResponse oClientDataResponse = super.deleteData(oClientData);
		return oClientDataResponse;
	}
	
	@RequestMapping(value="/updateClientBalanceData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ClientDataResponse updateClientBalanceData(@RequestBody ClientData oClientData)
	{
		ClientDataResponse oClientDataResponse = super.updateClientBalanceData(oClientData);
		return oClientDataResponse;
	}
}
