package com.techmust.property.propertytype;

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

@Controller
public class PropertyTypeDataProcessor extends GenericIDataProcessor<PropertyTypeData> 
{
	@RequestMapping(value="/propertyTypeDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody PropertyTypeData oPropertyTypeData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oPropertyTypeData [IN] : " + oPropertyTypeData);
		PropertyTypeDataResponse oPropertyTypeDataResponse = new PropertyTypeDataResponse ();
		try
		{
			oPropertyTypeDataResponse.m_bSuccess = oPropertyTypeData.saveObject ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
		}
		return oPropertyTypeDataResponse;
	}

	@RequestMapping(value="/propertyTypeDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData(@RequestBody PropertyTypeData oPropertyTypeData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oPropertyTypeData [IN] : " + oPropertyTypeData);
		PropertyTypeDataResponse oPropertyTypeDataResponse = new PropertyTypeDataResponse ();
		try
		{
			oPropertyTypeDataResponse.m_bSuccess = oPropertyTypeData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
		}
		return oPropertyTypeDataResponse;
	}

	@RequestMapping(value="/propertyTypeDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get(@RequestBody PropertyTypeData oPropertyTypeData) throws Exception 
	{
		m_oLogger.info ("get");
		m_oLogger.debug ("get - oPropertyTypeData [IN] : " + oPropertyTypeData);
		PropertyTypeDataResponse oPropertyTypeDataResponse = new PropertyTypeDataResponse ();
		PropertyTypeData oData = new PropertyTypeData ();
		try
		{
			oData.setM_nPropertyTypeId (oPropertyTypeData.getM_nPropertyTypeId());
			oData = (PropertyTypeData)populateObject (oData);
			oPropertyTypeDataResponse.m_arrPropertyTypeData.add (oData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("get - oException : " + oException);
		}
		return oPropertyTypeDataResponse;
	}

	@RequestMapping(value="/propertyTypeDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody PropertyTypeData oPropertyTypeData) throws Exception
{
		String strXML = "";
		try
		{
			oPropertyTypeData = (PropertyTypeData)populateObject (oPropertyTypeData);
			strXML = oPropertyTypeData.generateXML ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getXML - oException : " + oException);
		}
		m_oLogger.debug ("getXML - strXML [OUT] : " + strXML);
		return strXML;
	}
	

//	@RequestMapping(value="/propertyTypeDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
//	@ResponseBody
//	public GenericResponse list(@RequestBody TradeMustHelper oData) throws Exception 
//	{
//		return list (oData.getM_oPropertyTypeData(), oData.getM_strColumn(), oData.getM_strOrderBy(), 0, 0);
//	}
	
	@SuppressWarnings("unchecked")
	public PropertyTypeDataResponse list (@RequestBody PropertyTypeData oPropertyTypeData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oPropertyTypeData [IN] : " + oPropertyTypeData);
		PropertyTypeDataResponse oPropertyTypeDataResponse = new PropertyTypeDataResponse ();
		try
		{
			oPropertyTypeDataResponse.m_nRowCount = getRowCount(oPropertyTypeData);
			oPropertyTypeDataResponse.m_arrPropertyTypeData = new ArrayList (oPropertyTypeData.list (arrOrderBy, nPageNumber, nPageSize));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("list - oException : " + oException);
		}
		return oPropertyTypeDataResponse;
	}

	@RequestMapping(value="/propertyTypeDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update(@RequestBody PropertyTypeData oPropertyTypeData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oPropertyTypeData [IN] : " + oPropertyTypeData);
		PropertyTypeDataResponse oPropertyTypeDataResponse = new PropertyTypeDataResponse ();
		try
		{
			oPropertyTypeDataResponse.m_bSuccess = oPropertyTypeData.updateObject ();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		return oPropertyTypeDataResponse;
	}

	@Override
	public GenericResponse list(PropertyTypeData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
