package com.techmust.scholarshipmanagement.institution;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.ZenithHelper;

@Controller
public class InstitutionInformationDataProcessor extends GenericIDataProcessor<InstitutionInformationData>
{

	@Override
	@RequestMapping(value = "/institutionInfoCreate",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody InstitutionInformationData oInstitutionInformationData) throws Exception
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oInstitutionInformationData [IN] : " + oInstitutionInformationData);
		InstitutionDataResponse oInstitutionDataResponse = new InstitutionDataResponse();
		try
		{
			oInstitutionDataResponse.m_bSuccess = oInstitutionInformationData.saveObject();			
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		return oInstitutionDataResponse;
	}

	@Override
	@RequestMapping(value = "/institutionInfoDelete",method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody InstitutionInformationData oInstitutionInformationData) throws Exception
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oInstitutionInformationData.getM_nInstitutionId() [IN] : " + oInstitutionInformationData.getM_nInstitutionId());
		InstitutionDataResponse oInstitutionDataResponse = new InstitutionDataResponse();
		try
		{
			//oInstitutionInformationData = (InstitutionInformationData) populateObject (oInstitutionInformationData);
			oInstitutionDataResponse.m_bSuccess = oInstitutionInformationData.doesInstitutionHaveAcademic(oInstitutionInformationData.getM_nInstitutionId());
			if(oInstitutionDataResponse.m_bSuccess == true)
			{
				oInstitutionDataResponse.m_strError_Desc = "Students are part of in this Institution";
				oInstitutionDataResponse.m_bSuccess = false;
			}				
			else
				oInstitutionDataResponse.m_bSuccess = oInstitutionInformationData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		throw oException;
		}
		return oInstitutionDataResponse;
	}

	@Override
	@RequestMapping(value="/institutionInfoGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody InstitutionInformationData oInstitutionInformationData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oInstitutionInformationData.getM_nInstitutionId() [IN] :" + oInstitutionInformationData.getM_nInstitutionId());
		InstitutionDataResponse oInstitutionDataResponse = new InstitutionDataResponse();
		try 

		{
			oInstitutionInformationData = (InstitutionInformationData) populateObject (oInstitutionInformationData);
			oInstitutionDataResponse.m_arrInstitutionInformationData.add (oInstitutionInformationData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oInstitutionDataResponse;
	}	

	@RequestMapping(value="/institutionInfoList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody ZenithHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list (oData.getM_oInstitutionInformationData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
	private GenericResponse list(InstitutionInformationData oInstitutionInformationData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oInstitutionInformationData [IN] : " + oInstitutionInformationData);
		InstitutionDataResponse oInstitutionDataResponse = new InstitutionDataResponse();
		try 
		{
			oInstitutionDataResponse.m_nRowCount = getRowCount(oInstitutionInformationData);
			oInstitutionDataResponse.m_arrInstitutionInformationData = new ArrayList (oInstitutionInformationData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oInstitutionDataResponse;
	}


	@Override
	@RequestMapping(value="/institutionInfoUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody InstitutionInformationData oInstitutionInformationData) throws Exception
	{
		
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oInstitutionInformationData.getM_nInstitutionId() [IN] : " + oInstitutionInformationData.getM_nInstitutionId());
		InstitutionDataResponse oInstitutionDataResponse = new InstitutionDataResponse();
		try
		{			
			oInstitutionDataResponse.m_bSuccess = oInstitutionInformationData.updateObject();
			oInstitutionDataResponse.m_arrInstitutionInformationData.add(oInstitutionInformationData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		return oInstitutionDataResponse;
	}

	@Override
	@RequestMapping(value="/institutionInfoGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody InstitutionInformationData oInstitutionInformationData) throws Exception 
	{
		m_oLogger.info ("getXML");
		m_oLogger.debug ("getXML - oInstitutionInformationData [IN] : " +oInstitutionInformationData);
		String strXml = "";
		try 
		{	
			oInstitutionInformationData = (InstitutionInformationData) populateObject (oInstitutionInformationData);
			strXml = oInstitutionInformationData != null ? oInstitutionInformationData.generateXML ():"";
		}
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " +oException);
			throw oException;
		}
		m_oLogger.debug ("getXML - strXml [OUT] : " +strXml);
		return strXml;
	}
	
	@RequestMapping(value="/institutionInfoGetSuggestions", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public InstitutionDataResponse getInstitutionSuggestions (@RequestBody ZenithHelper oData) throws Exception
	{
		return getInstitutionsSuggestions(oData.getM_oInstitutionInformationData(),oData.getM_strColumn(),oData.getM_strOrderBy());
	}

	public InstitutionDataResponse getInstitutionsSuggestions(InstitutionInformationData oInstitutionInformationData,String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getInstitutionSuggestions");
		m_oLogger.debug ("getInstitutionSuggestions - oInstitutionInformationData [IN] : " + oInstitutionInformationData);
		m_oLogger.debug ("getInstitutionSuggestions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getInstitutionSuggestions - strOrderBy [IN] : " + strOrderBy);
		InstitutionDataResponse oInstitutionDataResponse = new InstitutionDataResponse();
		try 
		{
			oInstitutionDataResponse.m_arrInstitutionInformationData = new ArrayList(oInstitutionInformationData.listCustomData(this));
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getInstitutionSuggestions - oException : " +oException);
			throw oException;
		}
		return oInstitutionDataResponse;
	}

	@Override
	public GenericResponse list(InstitutionInformationData oGenericData, HashMap<String, String> arrOrderBy)throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

}
