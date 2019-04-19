package com.techmust.inventory.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.clientmanagement.ClientData;
import com.techmust.clientmanagement.ClientGroupData;
import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.items.ItemData;
import com.techmust.inventory.items.ItemGroupData;
import com.techmust.usermanagement.userinfo.UserInformationData;

@Controller
public class DiscountStructureDataProcessor extends GenericIDataProcessor<DiscountStructureData>
{
	@RequestMapping(value="/discountStructureCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody DiscountStructureData oDiscountStructureData) throws Exception 
	{
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oDiscountStructureData [IN] : " + oDiscountStructureData);
		DiscountStructureDataResponse oDiscountStructureDataResponse = new DiscountStructureDataResponse ();
		try
		{
			createLog(oDiscountStructureData, "DiscountStructureDataProcessor.create : ");
			isvalidUser (oDiscountStructureData);
			oDiscountStructureDataResponse.m_bSuccess = oDiscountStructureData.saveObject();
		}
		catch (Exception oException)
		{
			oDiscountStructureDataResponse.m_strError_Desc = oException.getMessage();
			m_oLogger.error ("create - oException : " + oException);
		}
		m_oLogger.debug("create - oDiscountStructureDataResponse [OUT] : " + oDiscountStructureDataResponse.m_bSuccess);
		return oDiscountStructureDataResponse;
	}

	@Override
	public GenericResponse deleteData(DiscountStructureData oDiscountStructureData) throws Exception 
	{
		return null;
	}

	@Override
	public GenericResponse get(DiscountStructureData oDiscountStructureData) throws Exception 
	{
		m_oLogger.info ("get");
		DiscountStructureDataResponse oDiscountStructureDataResponse = new DiscountStructureDataResponse ();
		try 
		{
			isvalidUser (oDiscountStructureData);
			oDiscountStructureData = (DiscountStructureData) populateObject (oDiscountStructureData);
			oDiscountStructureData.setM_strDate (getClientCompatibleFormat (oDiscountStructureData.getM_dCreatedOn()));
			oDiscountStructureDataResponse.m_arrDiscountStructureData.add(oDiscountStructureData);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("get - oException : "  +oException);
			throw oException;
		}
		return oDiscountStructureDataResponse;
	}

	@Override
	public String getXML(DiscountStructureData oDiscountStructureData) throws Exception 
	{
		m_oLogger.debug ("getXML - oDiscountStructureData [IN] : "+ oDiscountStructureData);
		try
		{
			isvalidUser (oDiscountStructureData);
			oDiscountStructureData = (DiscountStructureData) populateObject(oDiscountStructureData);
			oDiscountStructureData.setM_strDate(getClientCompatibleFormat(oDiscountStructureData.getM_dCreatedOn()));
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " , oException);
		}
		return oDiscountStructureData != null ?oDiscountStructureData.generateXML () : ""; 
	}
	
	@RequestMapping(value="/discountStructureList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData) throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list(oData.getM_oDiscountStructureData(), oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}

	@Override
	public GenericResponse list(DiscountStructureData oDiscountStructureData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oDiscountStructureData, arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
    public GenericResponse list(DiscountStructureData oDiscountStructureData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oDiscountStructureData [IN] : " + oDiscountStructureData);

		DiscountStructureDataResponse oDiscountStructureDataResponse = new DiscountStructureDataResponse ();
		try 
		{
			oDiscountStructureDataResponse.m_nRowCount = getRowCount(oDiscountStructureData);
			oDiscountStructureDataResponse.m_arrDiscountStructureData = new ArrayList (oDiscountStructureData.list (arrOrderBy, nPageNumber, nPageSize));
			oDiscountStructureDataResponse.m_arrDiscountStructureData = buildDiscountStructureData (oDiscountStructureDataResponse.m_arrDiscountStructureData);
		
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oDiscountStructureDataResponse;
	}

	@Override
	public GenericResponse update(DiscountStructureData oDiscountStructureData) throws Exception 
	{
		m_oLogger.info ("update");
		m_oLogger.debug ("update - oDiscountStructureData [IN] : " + oDiscountStructureData);
		DiscountStructureDataResponse oDiscountStructureDataResponse = new DiscountStructureDataResponse ();
		try
		{
			createLog(oDiscountStructureData, "DiscountStructureDataProcessor.create : ");
			isvalidUser (oDiscountStructureData);
			oDiscountStructureDataResponse.m_bSuccess = oDiscountStructureData.updateObject();
		}
		catch (Exception oException)
		{
			oDiscountStructureDataResponse.m_strError_Desc = oException.getMessage();
			m_oLogger.error ("update - oException : " + oException);
		}
		m_oLogger.debug("update - oDiscountStructureDataResponse [OUT] : " + oDiscountStructureDataResponse.m_bSuccess);
		return oDiscountStructureDataResponse;
	}
	
	@RequestMapping(value="/discountStructureSave", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse save(@RequestBody DiscountList oData) throws Exception 
	{
		m_oLogger.info ("save");
		DiscountStructureDataResponse oDiscountStructureDataResponse = new DiscountStructureDataResponse ();
		List<DiscountStructureData>arrDiscountStructureData = oData.getArrDiscountStructureData();
		try
		{
			if (arrDiscountStructureData.size() > 0)
			{
				DiscountStructureData oDiscountStructureData = (DiscountStructureData) arrDiscountStructureData.get(0);
				createLog(oDiscountStructureData, "DiscountStructureDataProcessor.create : ");
				isvalidUser (oDiscountStructureData);
				for (int nIndex = 0; nIndex < arrDiscountStructureData.size(); nIndex++)
					oDiscountStructureDataResponse.m_bSuccess = arrDiscountStructureData.get(nIndex).saveOrUpdateObject ();
			}
		}
		catch (Exception oException)
		{
			oDiscountStructureDataResponse.m_strError_Desc = oException.getMessage();
			m_oLogger.error ("save - oException : " + oException);
		}
		m_oLogger.debug("save - oDiscountStructureDataResponse [OUT] : " + oDiscountStructureDataResponse.m_bSuccess);
		return oDiscountStructureDataResponse;
	}
	
	@RequestMapping(value="/getDiscount", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public float getDiscount(@RequestBody TradeMustHelper oData)
	{
		return getDiscount(oData.getM_oClientData(), oData.getM_oItemData());
	}
	public float getDiscount (ClientData oClientData, ItemData oItemData)
	{
		m_oLogger.info ("getDiscount");
		float nDiscount = 0;
		try
		{
			ArrayList<ClientGroupData> arrClientGroupData = ClientGroupData.getClientGroups (oClientData);
			ArrayList<ItemGroupData> arrItemGroupData = ItemGroupData.getItemGroups (oItemData);
			for (int nIndex=0; nDiscount <= 0 && nIndex < arrClientGroupData.size(); nIndex++)
				nDiscount = getDiscount (arrClientGroupData.get(nIndex) , arrItemGroupData);
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getDiscount - oException : " + oException);
		}
		return nDiscount;
	}
	
	private  float getDiscount (ClientGroupData oClientGroupData, ArrayList<ItemGroupData> arrItemGroupData) throws Exception
	{
		float nDiscount = 0;
		DiscountStructureData oDiscountStructureData = new DiscountStructureData ();
		DiscountStructureDataResponse oDiscountStructureDataResponse = new DiscountStructureDataResponse ();
		oDiscountStructureData.setM_oClientGroupData(oClientGroupData);
		for (int nItemIndex=0; nDiscount <= 0.0 && nItemIndex < arrItemGroupData.size(); nItemIndex++)
		{
			oDiscountStructureData.setM_oItemGroupData(arrItemGroupData.get(nItemIndex));
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oDiscountStructureDataResponse = (DiscountStructureDataResponse) list(oDiscountStructureData, oOrderBy);
			if(oDiscountStructureDataResponse.m_arrDiscountStructureData.size() > 0)
				nDiscount = oDiscountStructureDataResponse.m_arrDiscountStructureData.get(0).getM_nDiscount();
		}
		return nDiscount;
	}
	
	private ArrayList<DiscountStructureData> buildDiscountStructureData(ArrayList<DiscountStructureData> arrDiscountStructureData) 
	{
		m_oLogger.info("buildDiscountStructureData");
		for (int nIndex=0; nIndex < arrDiscountStructureData.size(); nIndex++)
			arrDiscountStructureData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrDiscountStructureData.get(nIndex).getM_dCreatedOn())); 
		return arrDiscountStructureData;
	}
	
	private void isvalidUser (DiscountStructureData oDiscountStructureData) throws Exception
	{
		m_oLogger.info("isvalidUser");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oDiscountStructureData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oDiscountStructureData.getM_oUserCredentialsData().getM_nUserId());
			if (!isValidUser(oUserData))
				throw new Exception (kUserCredentialsFailed);
		}
		catch (Exception oException)
		{
			m_oLogger.error("isvalidUser - oException : " + oException);
			throw oException;
		}
	}
	
	private void createLog (DiscountStructureData oDiscountStructureData, String strFunctionName) 
	{
		m_oLogger.info("createLog");
		try
		{
			UserInformationData oUserData = new UserInformationData ();
			oUserData.setM_nUID(oDiscountStructureData.getM_oUserCredentialsData().getM_nUID());
			oUserData.setM_nUserId(oDiscountStructureData.getM_oUserCredentialsData().getM_nUserId());
			createLog (oUserData, strFunctionName);
		}
		catch (Exception oException)
		{
			m_oLogger.error("createLog - oException : " + oException);
		}
	}
}
