package com.techmust.helper;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.data.GenericData;
import com.techmust.generic.response.GenericResponse;
import com.techmust.usermanagement.userinfo.IUserInformationData;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.usermanagement.userinfo.UserInformationDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationResponse;

@Controller
public class UserInformationHelper extends UserInformationDataProcessor<IUserInformationData> {
	public static Logger m_oLogger = Logger.getLogger(GenericData.class);

	@RequestMapping(value="/userInfoCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public UserInformationResponse create (@RequestBody UserInformationData oData) throws Exception 
	{
		UserInformationResponse oUserInformationResponse = super.create(oData);
		return oUserInformationResponse;
	}
	
	@RequestMapping(value="/userInfoGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public UserInformationResponse get (@RequestBody UserInformationData oData) throws Exception 
	{
		UserInformationResponse oUserInformationResponse = super.get(oData);
		return oUserInformationResponse;
	}
	
	@RequestMapping(value="/userInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oData) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		UserInformationResponse oUserInformationResponse = super.list(oData.getM_oUserData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
		return oUserInformationResponse; 
	}
	
	@RequestMapping(value="/userInfoUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public UserInformationResponse update (@RequestBody UserInformationData oData) throws Exception 
	{
		UserInformationResponse oUserInformationResponse = super.update(oData);
		return oUserInformationResponse;
	}
	
	@RequestMapping(value="/userInfoDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public UserInformationResponse deleteData (@RequestBody UserInformationData oData) throws Exception 
	{
		UserInformationResponse oUserInformationResponse = super.deleteData(oData);
		return oUserInformationResponse;
	}
	
	@RequestMapping(value="/userInfoGetImagePreview", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public UserInformationData getImagePreview (@RequestBody UserInformationData oData) throws Exception 
	{
		UserInformationData oUserData = super.getImagePreview(oData);
		return oUserData;
	}
	
	@RequestMapping(value="/userInfoGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML (@RequestBody UserInformationData oData) throws Exception 
	{
		String strXml = super.getXML(oData);
		return strXml;
	}
	
	@RequestMapping(value="/userInfoGetUserSuggestions", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public UserInformationResponse getUserSuggesstions (@RequestBody ZenithHelper oData) throws Exception 
	{
		UserInformationResponse oUserInformationResponse = super.getUserSuggesstions(oData.getM_oUserData(), oData.getM_strColumn (), oData.getM_strOrderBy());
		return oUserInformationResponse;
	}
}
