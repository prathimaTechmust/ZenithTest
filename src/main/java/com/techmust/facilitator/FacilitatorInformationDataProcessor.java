package com.techmust.facilitator;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.ItemDataResponse;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class FacilitatorInformationDataProcessor extends GenericIDataProcessor <FacilitatorInformationData>
{

	@Override
	@RequestMapping(value = "/facilitatorInfoCreate",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oOrganization [IN] : " + oFacilitatorInformationData);
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try
		{
			oFacilitatorDataResponse.m_bSuccess = oFacilitatorInformationData.saveObject();			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		return oFacilitatorDataResponse;
	}

	@Override
	@RequestMapping(value = "/facilitatorInfoDelete",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oFacilitatorInformationData.getM_nItemId() [IN] : " + oFacilitatorInformationData.getM_nFacilitatorId());
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try
		{
			oFacilitatorInformationData = (FacilitatorInformationData) populateObject (oFacilitatorInformationData);
			oFacilitatorDataResponse.m_bSuccess = oFacilitatorInformationData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		throw oException;
		}
		return oFacilitatorDataResponse;
	}

	@Override
	@RequestMapping(value="//facilitatorInfoGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oFacilitatorInformationData.getM_nFacilitatorId() [IN] :" + oFacilitatorInformationData.getM_nFacilitatorId());
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try 

		{
			oFacilitatorInformationData = (FacilitatorInformationData) populateObject (oFacilitatorInformationData);
			oFacilitatorDataResponse.m_arrFacilitatorInformationData.add (oFacilitatorInformationData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oFacilitatorDataResponse;
	}

	@RequestMapping(value="/facilitatorInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oFacilitatorInformationData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
	private GenericResponse list(FacilitatorInformationData oFacilitatorInformationData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oFacilitatorInformationData [IN] : " + oFacilitatorInformationData);
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try 
		{
			oFacilitatorDataResponse.m_nRowCount = getRowCount(oFacilitatorInformationData);
			oFacilitatorDataResponse.m_arrFacilitatorInformationData = new ArrayList (oFacilitatorInformationData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oFacilitatorDataResponse;
	}


	@Override
	@RequestMapping(value="/facilitatorInfoUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception
	{
		
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oFacilitatorInformationData.getM_nFacilitatorId() [IN] : " + oFacilitatorInformationData.getM_nFacilitatorId());
		FacilitatorDataResponse oFacilitatorDataResponse = new FacilitatorDataResponse();
		try
		{			
			oFacilitatorDataResponse.m_bSuccess = oFacilitatorInformationData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		return oFacilitatorDataResponse;
	}

	@Override
	@RequestMapping(value="/facilitatorInfoGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody FacilitatorInformationData oFacilitatorInformationData) throws Exception 
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oFacilitatorInformationData [IN] : " +oFacilitatorInformationData);
		String strXml = "";
		try 
		{	
			oFacilitatorInformationData = (FacilitatorInformationData) populateObject (oFacilitatorInformationData);
			strXml = oFacilitatorInformationData != null ? oFacilitatorInformationData.generateXML ():"";
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}

	@Override
	public GenericResponse list(FacilitatorInformationData oGenericData, HashMap<String, String> arrOrderBy)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
