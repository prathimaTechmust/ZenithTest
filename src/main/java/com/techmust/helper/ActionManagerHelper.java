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

@Controller
public class ActionManagerHelper extends ActionManagerDataProcessor 
{

	@RequestMapping(value="/actionManagerGet", method = RequestMethod.POST, headers = {"Content-type=application/json", "Accept=application/json"})
	@ResponseBody
	public ActionManagerResponse get (@RequestBody ActionManagerData oData) 
	{
		m_oLogger.info ("get");
		ActionManagerResponse oActionManagerResponse = super.get(oData);
		if(oActionManagerResponse.m_bSuccess == true)
			SetTenantToSession ();
		return oActionManagerResponse;
	}
	
	private void SetTenantToSession() 
	{
		m_oLogger.info ("SetTenantToSession");
		ServletRequestAttributes oServletRequestAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest oHttpServletRequest = oServletRequestAttr.getRequest();
		String strTenantId = oHttpServletRequest.getParameter(Constants.TENANT_ID);
		HttpSession oSession = oHttpServletRequest.getSession();
		oSession.setAttribute(Constants.TENANT_ID, strTenantId);
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
