package com.techmust.master.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.techmust.constants.Constants;
import com.techmust.utils.Utils;


public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver 
{
	private static final Logger LOG = LoggerFactory.getLogger(CurrentTenantIdentifierResolverImpl.class);
	@Override
	public String resolveCurrentTenantIdentifier() 
	{
		String strTenantId = "";
		do
		{
			ServletRequestAttributes oServletRequestAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (oServletRequestAttr == null)
			{
				LOG.debug ("** NO ServletRequestAttr ***");
				break;
			}
			HttpServletRequest oHttpServletRequest = oServletRequestAttr.getRequest();
			HttpSession oSession = oHttpServletRequest.getSession();
			strTenantId = (String) oSession.getAttribute(Constants.TENANT_ID);
//			strTenantId = Utils.getCookie(oHttpServletRequest, Constants.TENANT_COOKIE_NAME);
		} while (false);
		return StringUtils.isNotBlank(strTenantId) ? strTenantId : String.valueOf(Constants.DEFAULT_TENANT_ID);
	}
	
	@Override
	public boolean validateExistingCurrentSessions()
	{
		return true;
	}
}