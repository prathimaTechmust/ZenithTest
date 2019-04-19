package com.techmust.organization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.master.model.tenant.MasterTenant;
import com.techmust.master.model.tenant.UserTenant;
import com.techmust.usermanagement.role.RoleData;
import com.techmust.usermanagement.role.RoleDataProcessor;
import com.techmust.usermanagement.role.RoleResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;
import com.techmust.usermanagement.userinfo.UserInformationDataProcessor;
import com.techmust.usermanagement.userinfo.UserInformationResponse;
import com.techmust.utils.DBUtil;

@Controller
public class OrganizationDataProcessor extends GenericIDataProcessor <OrganizationInformationData>
{

	@Override
	@RequestMapping(value = "/organizationInfoCreate",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody OrganizationInformationData oOrganizationInformationData) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oOrganization [IN] : " + oOrganizationInformationData);
		OrganizationDataResponse oOrganizationDataResponse = new OrganizationDataResponse();
		try
		{
			oOrganizationDataResponse.m_bSuccess = oOrganizationInformationData.saveObject();
			if(oOrganizationDataResponse.m_bSuccess)
				insertTenantUser (oOrganizationInformationData);
			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oTaxDataResponse.m_bSuccess [OUT] : " + oOrganizationDataResponse.m_bSuccess);
		return oOrganizationDataResponse;
	}

	private void insertTenantUser(OrganizationInformationData oOrganizationInformationData) 
	{
		m_oLogger.info ("insertTenantUser");
		try
		{
			UserInformationDataProcessor oDataProcessor = new UserInformationDataProcessor ();
			UserInformationData oUserInformationData = new UserInformationData();
			oUserInformationData.setM_strLoginId(oOrganizationInformationData.getM_strLoginID());
			oUserInformationData.setM_strPassword(oOrganizationInformationData.getM_strPassword());
			oUserInformationData.setM_strUserName(oOrganizationInformationData.getM_strLoginID());
			oUserInformationData.setM_strEmailAddress(oOrganizationInformationData.getM_strEmailAddress());
			oUserInformationData.setm_oRole(getUserRole ());
			UserInformationResponse oResponse =oDataProcessor.create(oUserInformationData);
			if(oResponse.m_bSuccess)
				insertToUserTenant (oResponse.m_arrUserInformationData.get(0), oOrganizationInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error(oException.toString());
		}		
	}

	private void insertToUserTenant(UserInformationData userInformationData, OrganizationInformationData oOrganizationInformationData) 
	{
		m_oLogger.info ("insertToUserTenant");
		try
		{
			UserTenant oUserTenant = new UserTenant ();
			oUserTenant.setM_oUser(userInformationData);
			oUserTenant.setM_oOrganization(oOrganizationInformationData);
			boolean bSucces = oUserTenant.saveObject();
			if(bSucces)
				insertTenantDBConnectionDetails (oOrganizationInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error(oException.toString());
		}
	}

	private void insertTenantDBConnectionDetails(OrganizationInformationData oOrganizationInformationData) 
	{
		m_oLogger.info ("insertTenantDBConnectionDetails");
		try
		{
			MasterTenant oMasterTenant = new MasterTenant();
			oMasterTenant.setM_oOrgData(oOrganizationInformationData);
			oMasterTenant.setM_strConnectionUsername(Constants.DBUSERNAME);
			oMasterTenant.setM_strConnectionPassword(Constants.DBPASSWORD);
			String strDBURL = buildTenantDBURL(oOrganizationInformationData);
			oMasterTenant.setM_strUrl(strDBURL);
			boolean bSuccess = oMasterTenant.saveObject();
			if(bSuccess)
			{
				initializeTenantDB (oMasterTenant, strDBURL);
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error(oException.toString());
		}
	}

	private void initializeTenantDB(MasterTenant oMasterTenant, String strDBURL) 
	{
		ServletRequestAttributes oServletRequestAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest oHttpServletRequest = oServletRequestAttr.getRequest();
		HttpSession oSession = oHttpServletRequest.getSession();
		oSession.setAttribute(Constants.TENANT_ID, oMasterTenant.getM_oOrgData().getM_nOrganizationId());
		try 
		{
				String strDBName = "tenant_"+ String.valueOf(oMasterTenant.getM_oOrgData().getM_nOrganizationId());
				String oQuery = "CREATE SCHEMA " + strDBName;
				String strDatabase = Constants.ORGANIZATION_CREATION_DB_URL;
				strDatabase = String.format(strDatabase, "");
				Connection oConnection = DriverManager.getConnection(strDatabase, Constants.DBUSERNAME, Constants.DBPASSWORD);
				Statement oStatement = oConnection.createStatement();
				oStatement.executeUpdate(oQuery);
				DBUtil.createTable(strDBURL);
		}
		catch (Exception oException) 
		{
			oException.printStackTrace();
		}
	}

	private String buildTenantDBURL(OrganizationInformationData oOrganizationInformationData) 
	{
		String strURl = Constants.ORGANIZATION_CREATION_DB_URL;
		String strDBName = "tenant_"+ String.valueOf(oOrganizationInformationData.getM_nOrganizationId());
		strURl = strURl.replace("%s", strDBName);
		return strURl;
	}

	private RoleData getUserRole() 
	{
		RoleData oRoleData = new RoleData ();
		try
		{
			oRoleData.setM_strRoleName(Constants.kUserRole);
			RoleDataProcessor oRoleDataProcessor = new RoleDataProcessor();
			RoleResponse oResponse = oRoleDataProcessor.get(oRoleData);
			if(oResponse.m_arrRoleData.size() > 0)
				oRoleData.setM_nRoleId(oResponse.m_arrRoleData.get(0).getM_nRoleId());
		}
		catch (Exception oException)
		{
			
		}
		return oRoleData;
	}

	@Override
	public GenericResponse deleteData(OrganizationInformationData oGenericData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(OrganizationInformationData oGenericData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value="/organizationInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData)	throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oOrganizationData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
	private GenericResponse list(OrganizationInformationData oOrganizationInformationData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oOrganizationInformationData [IN] : " + oOrganizationInformationData);
		OrganizationDataResponse oOrganizationDataResponse = new OrganizationDataResponse();
		try 
		{
			oOrganizationDataResponse.m_nRowCount = getRowCount(oOrganizationInformationData);
			oOrganizationDataResponse.m_arrOrganizationInformationData = new ArrayList (oOrganizationInformationData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oOrganizationDataResponse;
	}

	@Override
	public GenericResponse update(OrganizationInformationData oGenericData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping(value="/organizationInfoGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody OrganizationInformationData oOrganizationInformationData) throws Exception
	{
		oOrganizationInformationData = (OrganizationInformationData) populateObject(oOrganizationInformationData);
	    return oOrganizationInformationData != null ? oOrganizationInformationData.generateXML () : "";
	}

	@Override
	public GenericResponse list(OrganizationInformationData oGenericData, HashMap<String, String> arrOrderBy)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
