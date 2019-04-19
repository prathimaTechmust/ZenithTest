package com.techmust.inventory.returned;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ClientData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.serial.SerialNumberData;
import com.techmust.inventory.serial.SerialType;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class ReturnedDataProcessor extends GenericIDataProcessor<ReturnedData> 
{
	@Override
	@RequestMapping(value="/returnedCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ReturnedDataResponse create(@RequestBody ReturnedData oReturnedData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oSalesData [IN] : " + oReturnedData);
		ReturnedDataResponse oReturnedDataResponse = new ReturnedDataResponse ();
		try
		{
			createLog(oReturnedData, "ReturnedDataProcessor.create");
			oReturnedData.prepareReturnData ();
			oReturnedData.setM_strCreditNoteNumber(SerialNumberData.generateSerialNumber(SerialType.kCreditNoteNumber));
			oReturnedDataResponse.m_bSuccess = oReturnedData.saveObject();
			oReturnedData.returned(true);
			oReturnedData = (ReturnedData) populateObject(oReturnedData);
			oReturnedDataResponse.m_arrReturnedData.add(oReturnedData);
		}
		catch (Exception oException)
		{
			oReturnedDataResponse.m_strError_Desc = oException.getMessage();
			m_oLogger.error ("create - oException : " + oException);
		}
		m_oLogger.debug("create - oReturnedDataResponse [OUT] : " + oReturnedDataResponse.m_bSuccess);
		return oReturnedDataResponse;
	}

	@RequestMapping(value="/returnedDeleteData", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ReturnedDataResponse deleteData(@RequestBody ReturnedData oReturnedData) throws Exception 
	{
		m_oLogger.info ("deleteData");
		m_oLogger.debug ("deleteData - oReturnedData [IN] : " + oReturnedData);
		ReturnedDataResponse oReturnedDataResponse = new ReturnedDataResponse ();
		try
		{
			createLog(oReturnedData, "ReturnedDataProcessor.deleteData");
			oReturnedDataResponse.m_bSuccess = oReturnedData.deleteObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("deleteData - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("deleteData - oReturnedDataResponse.m_bSuccess [OUT] : " + oReturnedDataResponse.m_bSuccess);
		return oReturnedDataResponse;
	}

	@RequestMapping(value="/returnedget", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ReturnedDataResponse get(@RequestBody ReturnedData oReturnedData) throws Exception 
	{
		m_oLogger.info ("get");
		ReturnedDataResponse oReturnedDataResponse = new ReturnedDataResponse ();
		try 
		{
			oReturnedData = (ReturnedData) populateObject (oReturnedData);
			oReturnedDataResponse.m_arrReturnedData.add(oReturnedData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oReturnedDataResponse;
	}

	@RequestMapping(value="/returnedgetXML", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public String getXML(@RequestBody ReturnedData oReturnedData) throws Exception 
	{
		oReturnedData = (ReturnedData) populateObject(oReturnedData);
	    return oReturnedData != null ? oReturnedData.generateXML () : "";
	}

	@RequestMapping(value="/returnedList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ReturnedDataResponse list(@RequestBody TradeMustHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return (ReturnedDataResponse) list(oData.getM_oReturnedData(),oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}
	
	public ReturnedDataResponse list(ReturnedData oReturnedData,HashMap<String, String> arrOrderBy) throws Exception 
	{
		return (ReturnedDataResponse) list(oReturnedData, arrOrderBy, 0, 0);
	}

	@SuppressWarnings("unchecked")
    public GenericResponse list(ReturnedData oReturnedData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oReturnedData [IN] : " +oReturnedData);
		ReturnedDataResponse oReturnedDataResponse = new ReturnedDataResponse ();
		try 
		{
			oReturnedDataResponse.m_nRowCount = getRowCount(oReturnedData);
			oReturnedDataResponse.m_arrReturnedData = new ArrayList (oReturnedData.list (arrOrderBy, nPageNumber, nPageSize));
			oReturnedDataResponse.m_arrReturnedData = buildReturnedData (oReturnedDataResponse.m_arrReturnedData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oReturnedDataResponse;
	}
	
	@RequestMapping(value="/returnedupdate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ReturnedDataResponse update(@RequestBody ReturnedData oReturnedData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oReturnedData [IN] : " + oReturnedData);
		ReturnedDataResponse oReturnedDataResponse = new ReturnedDataResponse ();
		try
		{
			createLog(oReturnedData, "ReturnedDataProcessor.update");
			ReturnedData oDBReturnedData = (ReturnedData) populateObject(oReturnedData);
			oDBReturnedData.returned (false);
			oReturnedData.prepareReturnData ();
			ReturnedData oRemovedReturns = oReturnedData.getRemovedReturns ();
			oReturnedDataResponse.m_bSuccess = oReturnedData.updateObject();
			oReturnedData.returned(true);
			oReturnedData.deleteReturns (oRemovedReturns);
			oReturnedData = (ReturnedData) populateObject(oReturnedData);
			oReturnedDataResponse.m_arrReturnedData.add(oReturnedData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("update - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("update - oReturnedDataResponse.m_bSuccess [OUT] : " + oReturnedDataResponse.m_bSuccess);
		return oReturnedDataResponse;
	}
	
	private ArrayList<ReturnedData> buildReturnedData (ArrayList<ReturnedData> arrReturnedData) 
	{
		m_oLogger.info("buildReturnedData");
		for (int nIndex=0; nIndex < arrReturnedData.size(); nIndex++)
			arrReturnedData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrReturnedData.get(nIndex).getM_dCreatedOn())); 
		return arrReturnedData;
	}
	
	private void createLog (ReturnedData oReturnedData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oReturnedData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oReturnedData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
	
	@RequestMapping(value="/returnedsaveAndPrint", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse saveAndPrint(@RequestBody ReturnedData oReturnedData) throws Exception
	{
		m_oLogger.info("saveAndPrint");
		m_oLogger.debug("saveAndPrint - oSalesData [IN] : " + oReturnedData);
		ReturnedDataResponse oReturnedDataResponse = new ReturnedDataResponse ();
		try
		{
			oReturnedDataResponse = prepareForPrint((ReturnedDataResponse) create(oReturnedData));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("saveAndPrint - oException : " + oException);
		}
		return oReturnedDataResponse;
	}
	
	@RequestMapping(value="/returnedupdateAndPrint", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse updateAndPrint(@RequestBody ReturnedData oReturnedData) throws Exception
	{
		m_oLogger.info("updateAndPrint");
		m_oLogger.debug("updateAndPrint - oSalesData [IN] : " + oReturnedData);
		ReturnedDataResponse oReturnedDataResponse = new ReturnedDataResponse ();
		try
		{
			oReturnedDataResponse = prepareForPrint ((ReturnedDataResponse) update(oReturnedData));
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateAndPrint - oException : " + oException);
		}
		return oReturnedDataResponse;
	}

	protected ReturnedDataResponse prepareForPrint (ReturnedDataResponse oReturnedDataResponse) throws Exception 
	{
		ReturnedData oReturnedData = oReturnedDataResponse.m_arrReturnedData.get(0);
		ClientData oClientData = (ClientData) populateObject(oReturnedData.getM_oClientData());
		oReturnedData.setM_oClientData(oClientData);
		oReturnedDataResponse.m_strXMLData = oReturnedData.generateXML();	
		return oReturnedDataResponse;
	}
}
