package com.techmust.inventory.stocktransfer;

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
public class StockTransferMemoDataProcessor extends GenericIDataProcessor<StockTransferMemoData> 
{

	@Override
	public GenericResponse create(StockTransferMemoData oStockTransferMemoData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse deleteData(StockTransferMemoData oStockTransferMemoData) throws Exception
	{
		return null;
	}

	@Override
	@RequestMapping(value="/stockTransferMemoGet", method = RequestMethod.POST, headers = {"Content-type=application/json; charset=UTF-8"})
	@ResponseBody
	public GenericResponse get(@RequestBody StockTransferMemoData oStockTransferMemoData) throws Exception 
	{
		m_oLogger.debug("get - oStockTransferMemoData.getM_nStockTransferMemoId() [IN] : " + oStockTransferMemoData.getM_nStockTransferMemoId());
		
		StockTransferMemoDataResponse oResponse = new StockTransferMemoDataResponse ();
		try 
		{
			oStockTransferMemoData = (StockTransferMemoData) populateObject(oStockTransferMemoData);
			oResponse.m_arrStockTransferMemo.add(oStockTransferMemoData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("get -oException " + oException);
		}
		m_oLogger.error("get - oResponse [OUT] :  " + oResponse);
		return oResponse;
	}

	@Override
	@RequestMapping(value="/stockTransferMemoGetXML", method = RequestMethod.POST, headers = {"Content-type=application/json; charset=UTF-8"})
	@ResponseBody
	public String getXML(@RequestBody StockTransferMemoData oStockTransferMemoData) throws Exception
	{
		m_oLogger.debug ("getXML - oStockTransferData [IN] : "+ oStockTransferMemoData);
		try
		{
			oStockTransferMemoData = (StockTransferMemoData) populateObject(oStockTransferMemoData);
		} 
		catch (Exception oException)
		{
			m_oLogger.error("getXML - oException : " , oException);
		}
		return oStockTransferMemoData != null ? oStockTransferMemoData.generateXML () : ""; 
	}
	
	@RequestMapping(value="/stockTransferMemoList", method = RequestMethod.POST, headers = {"Content-type=application/json; charset=UTF-8"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oData)throws Exception
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oData.getM_strColumn(), oData.getM_strOrderBy());
		return list(oData.getM_oStockTransferMemoData(),oOrderBy, oData.getM_nPageNo(), oData.getM_nPageSize());
	}

	@Override
	public GenericResponse list(StockTransferMemoData oStockTransferMemoData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		return list (oStockTransferMemoData,arrOrderBy, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse list(StockTransferMemoData oStockTransferMemoData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception
	 {
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oStockTransferData [IN] : " +oStockTransferMemoData);
		StockTransferMemoDataResponse oResponse = new StockTransferMemoDataResponse ();
		try 
		{
			oResponse.m_nRowCount = getRowCount(oStockTransferMemoData);
			oResponse.m_arrStockTransferMemo = buildStockTransfetMemoData (new ArrayList (oStockTransferMemoData.list (arrOrderBy, nPageNumber, nPageSize)));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
			throw oException;
		}
		return oResponse;
	}

	@Override
	public GenericResponse update(StockTransferMemoData oStockTransferMemoData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	private ArrayList<StockTransferMemoData> buildStockTransfetMemoData(ArrayList<StockTransferMemoData> arrStockTransferMemoData) 
	{
		m_oLogger.info("buildStockTransfetMemoData");
		for (int nIndex=0; nIndex < arrStockTransferMemoData.size(); nIndex++)
			arrStockTransferMemoData.get(nIndex).setM_strDate(getClientCompatibleFormat(arrStockTransferMemoData.get(nIndex).getM_dTransferredOn()));
		return arrStockTransferMemoData;
	}

}
