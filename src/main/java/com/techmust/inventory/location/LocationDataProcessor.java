package com.techmust.inventory.location;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class LocationDataProcessor extends GenericIDataProcessor<LocationData> 
{

	private static final String Enumbody = null;

	@RequestMapping(value="/LocationDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create (@RequestBody LocationData oLocationData) throws Exception
	{
		m_oLogger.debug("create - oLocationData [IN] :" +oLocationData);
		m_oLogger.debug("create - oLocationData [IN] :" +oLocationData.getM_strName());
		LocationDataResponse oResponse = new LocationDataResponse ();
		try 
		{
			createLog(oLocationData, "LocationDataProcessor.create : " + oLocationData.getM_strName());
			oResponse.m_bSuccess = oLocationData.saveObject();
		} 
		catch (Exception oException)
		{
			m_oLogger.error("create -oException " + oException);
		}
		m_oLogger.error("create - oResponse [OUT] :  " + oResponse);
		return oResponse;
	}

	@RequestMapping(value="/LocationDataDelete", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse deleteData (@RequestBody LocationData oLocationData) throws Exception 
	{
		m_oLogger.debug("deleteData - oLocationData.getM_nLocationId() [IN] :" +oLocationData.getM_nLocationId());
		LocationDataResponse oResponse = new LocationDataResponse ();
		try 
		{
			createLog(oLocationData, "LocationDataProcessor.deleteData : " + oLocationData.getM_strName());
			oResponse.m_bSuccess = oLocationData.deleteObject();
		} 
		catch (Exception oException)
		{
			m_oLogger.error("deleteData -oException " + oException);
		}
		m_oLogger.error("deleteData - oResponse.m_bSuccess [OUT] :  " + oResponse.m_bSuccess);
		return oResponse;
	}

	@RequestMapping(value="/LocationDataGet", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse get (@RequestBody LocationData oLocationData) throws Exception 
	{
		m_oLogger.debug("get - oLocationData.getM_nLocationId() [IN] :" +oLocationData.getM_nLocationId());
		
		LocationDataResponse oResponse = new LocationDataResponse ();
		try 
		{
			oLocationData = (LocationData) populateObject(oLocationData);
			oResponse.m_arrLocations.add(oLocationData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("get -oException " + oException);
		}
		return oResponse;
	}

	@RequestMapping(value="/LocationDataGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML (@RequestBody LocationData oLocationData) throws Exception
	{
		m_oLogger.debug("getXML - oLocationData [IN] : " + oLocationData);
		try
		{
			oLocationData = (LocationData) populateObject(oLocationData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " + oException);
		}
		return oLocationData != null ?oLocationData.generateXML () : ""; 
	}
	
	@RequestMapping(value="/LocationDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		GenericResponse oLocationDataResponse = list(oTradeMustHelper.getM_oLocationData(), oOrderBy, oTradeMustHelper.getM_nPageNo(),oTradeMustHelper.getM_nPageSize());
		return oLocationDataResponse;
	}
	
	public GenericResponse list (LocationData oLocationData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize)
			throws Exception {
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oLocationData [IN] : " +oLocationData);
		LocationDataResponse oResponse = new LocationDataResponse ();
		try 
		{
			oResponse.m_nRowCount = getRowCount(oLocationData);
			oResponse.m_arrLocations = new ArrayList (oLocationData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
			throw oException;
		}
		return oResponse;
	}

	@RequestMapping(value="/LocationDataUpdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse update (@RequestBody LocationData oLocationData) throws Exception
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oLocationData.getM_nLocationId() [IN] : " + oLocationData.getM_nLocationId());
		LocationDataResponse oResponse = new LocationDataResponse ();
		try
		{
			createLog(oLocationData, "LocationDataProcessor.update : " + oLocationData.getM_strName());
			oResponse.m_bSuccess = oLocationData.updateObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
		}
		m_oLogger.debug ("update - oResponse.m_bSuccess [OUT] : " + oResponse.m_bSuccess);
		return oResponse;
	}
	
	@RequestMapping(value="/LocationDataSetDefaultLocation", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse setDefaultLocation (@RequestBody LocationData oLocationData)
	{
		m_oLogger.debug ("setDefaultLocation - oLocationData.getM_nLocationId() [IN] : " + 
				oLocationData.getM_nLocationId());
		LocationDataResponse oResponse = new LocationDataResponse ();
		try
		{
			resetDefaultLocation ();
			oLocationData = (LocationData) populateObject (oLocationData);
			oLocationData.setM_bIsDefault(true);
			oResponse.m_bSuccess = oLocationData.updateObject();
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("setDefaultLocation - oException : " + oException);
		}
		m_oLogger.debug ("setDefaultLocation - oResponse.m_bSuccess [OUT] : " + oResponse.m_bSuccess);
		return oResponse;
	}

	@RequestMapping(value="/LocationDataGetLocationSuggesstions", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getLocationSuggesstions (@RequestBody TradeMustHelper oData) throws Exception
	{
		return getLocationSuggesstions(oData.getM_oLocationData(), oData.getM_strColumn(), oData.getM_strOrderBy());
	}
	
	public GenericResponse getLocationSuggesstions (LocationData oLocationData, String strColumn, String strOrderBy) throws Exception
	{
		m_oLogger.info ("getLocationSuggesstions");
		m_oLogger.debug ("getLocationSuggesstions - LocationData [IN] : " + oLocationData);
		m_oLogger.debug ("getLocationSuggesstions - strColumn [IN] : " + strColumn);
		m_oLogger.debug ("getLocationSuggesstions - strOrderBy [IN] : " + strOrderBy);
		LocationDataResponse oLocationDataResponse = new LocationDataResponse ();
		try 
		{
			oLocationDataResponse.m_arrLocations = new ArrayList (oLocationData.listCustomData(this));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getLocationSuggesstions - oException : " +oException);
			throw oException;
		}
		return oLocationDataResponse;
	}
	
	@Override
	public Criteria prepareCustomCriteria(Criteria oCriteria, LocationData oLocationData) throws RuntimeException 
	{
		if(oLocationData.getM_strName().trim() != "")
			oCriteria.add(Restrictions.ilike("m_strName", oLocationData.getM_strName().trim(), MatchMode.ANYWHERE));
 		oCriteria.setMaxResults(10);
		return oCriteria;
	}
	
	private void resetDefaultLocation () throws Exception
	{
		m_oLogger.info ("resetDefaultLocation");
		LocationDataResponse oResponse = new LocationDataResponse ();
		try 
		{
			LocationData oLocationData = new LocationData ();
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oResponse = (LocationDataResponse)list(oLocationData, oOrderBy);
			for (int nIndex = 0; nIndex < oResponse.m_arrLocations.size(); nIndex++)
			{
				LocationData oData = oResponse.m_arrLocations.get(nIndex);
				oData.setM_bIsDefault(false);
				oData.updateObject();
			}
		}
		catch (Exception oException) 
		{
			throw oException;
		}
	}
	
	private void createLog (LocationData oLocationData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oLocationData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oLocationData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}

	@Override
	public GenericResponse list(LocationData oGenericData, HashMap<String, String> arrOrderBy) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
