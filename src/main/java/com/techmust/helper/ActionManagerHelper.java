package com.techmust.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.techmust.constants.Constants;
import com.techmust.usermanagement.actionmanager.ActionManagerData;
import com.techmust.usermanagement.actionmanager.ActionManagerDataProcessor;
import com.techmust.usermanagement.actionmanager.ActionManagerResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.utils.Utils;

@Controller
public class ActionManagerHelper extends ActionManagerDataProcessor 
{
	static HttpSession m_oSession;
	@RequestMapping(value="/actionManagerGet", method = RequestMethod.POST, headers = {"Content-type=application/json", "Accept=application/json"})
	@ResponseBody
	public ActionManagerResponse get (@RequestBody ActionManagerData oData) 
	{
		
		m_oLogger.info ("get");
		ActionManagerResponse oActionManagerResponse = super.get(oData);
		if(oActionManagerResponse.m_bSuccess == true)
		{
			UserInformationData oLoginUserData = getLoginUserInfpormationData(oActionManagerResponse.m_strUser);
			m_oSession = setLoginUserToSession (oLoginUserData);
			Utils.createActivityLog("ActionManagerDataProcessor::login",oLoginUserData);
		}
			
		return oActionManagerResponse;
	}
	
	public static HttpSession getLoginUserData()
	{
		return m_oSession;		
	}

	private UserInformationData getLoginUserInfpormationData(String strLoginUser)
	{
		UserInformationData oInformationData = new UserInformationData();
		try
		{
			oInformationData.setM_strUserName(strLoginUser);
			oInformationData = (UserInformationData) populateObject(oInformationData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getLoginUserData - oException"+oException);
		}		
		return oInformationData;
	}

	private static HttpSession setLoginUserToSession(UserInformationData oLoginUserData) 
	{
		m_oLogger.info ("SetLoginUserToSession");
		ServletRequestAttributes oServletRequestAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest oHttpServletRequest = oServletRequestAttr.getRequest();		
		HttpSession oSession = oHttpServletRequest.getSession();
		m_oLogger.info("Session Id"+oSession.getId());
		oSession.setAttribute(Constants.LOGINUSERNAME, oLoginUserData.getM_strUserName());
		String strLoginUserName = (String) oSession.getAttribute(Constants.LOGINUSERNAME);
		Utils.saveCookie(Constants.LOGINUSERNAME, strLoginUserName, oServletRequestAttr.getResponse());
		String strUser = Utils.getCookie(oHttpServletRequest, Constants.LOGINUSERNAME);
		return oSession;	
	}

	@RequestMapping(value="/actionManagerChangePassword", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionManagerResponse changePassword (@RequestBody ActionManagerData oData) 
	{
		m_oLogger.info ("changePassword");
		ActionManagerResponse oActionManagerResponse = super.changePassword(oData);
		return oActionManagerResponse;
	}
	
	@RequestMapping(value="/actionManagerLogout", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public void logOut (UserInformationData oUserInfoData)
	{
		Utils.createActivityLog("ActionManagerDataProcessor::logOut", oUserInfoData);
		super.logOut(oUserInfoData);
	}
	
	@RequestMapping(value="/actionManagerForgotPassword", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ActionManagerResponse processForgotPassword (@RequestBody String strLoginId, String strEmail) 
	{
		m_oLogger.info("processForgotPassword");
		ActionManagerResponse oActionManagerResponse = super.processForgotPassword(strLoginId, strEmail);
		return oActionManagerResponse;
	}
}
