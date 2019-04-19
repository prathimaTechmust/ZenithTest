package com.techmust.inventory.sales;

import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;

@Controller
public class SalesLineItemDataProcessor extends GenericIDataProcessor<SalesLineItemData> 
{
	public static final int kMaxPreviousSalesResult = 3;

	@Override
	public GenericResponse create (SalesLineItemData oSalesLineItemData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse deleteData (SalesLineItemData oSalesLineItemData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get (SalesLineItemData oSalesLineItemData) throws Exception
	{
		return null;
	}

	@Override
	public String getXML(SalesLineItemData oSalesLineItemData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}
	@RequestMapping(value="/saleslineitemDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		GenericResponse oResponse = list(oTradeMustHelper.getM_oSalesLineItemData(), oOrderBy);
		return oResponse;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public GenericResponse list (SalesLineItemData oSalesLineItemData, HashMap<String, String> arrOrderBy) throws Exception 
	{
		m_oLogger.debug("list - oSalesLineItemData [IN]: " + oSalesLineItemData);
		SalesLineItemDataResponse oDataResponse = new SalesLineItemDataResponse ();
		try 
		{
			oDataResponse.m_arrSalesLineItem = new ArrayList (oSalesLineItemData.list (arrOrderBy));
			for (int nIndex = 0;nIndex < oDataResponse.m_arrSalesLineItem.size () ; nIndex++)
				oDataResponse.m_arrSalesLineItem.get(nIndex).getM_oSalesData().setM_strDate(getClientCompatibleFormat(oDataResponse.m_arrSalesLineItem.get(nIndex).getM_oSalesData().getM_dDate()));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException" + oException);
		}
		return oDataResponse;
	}

	@Override
	public GenericResponse update(SalesLineItemData arg0) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@RequestMapping(value="/getPreviousSales", method = RequestMethod.POST, headers = {"Content-type=application/json; charset=UTF-8"})
	@ResponseBody
	public GenericResponse getPreviousSales (@RequestBody TradeMustHelper oData)
	{
		return getPreviousSales (oData.getM_strArticleNumber(), oData.getM_nClientId());
	}
	
	public GenericResponse getPreviousSales(String strArticleNumber, int nClientId)
	{
		SalesLineItemDataResponse oSalesDataLineItemResponse = new SalesLineItemDataResponse ();
		try 
		{
			oSalesDataLineItemResponse.m_arrSalesLineItem = buildSalesLineItemArray(new ArrayList (SalesLineItemData.getPreviousSales (strArticleNumber, nClientId, this)));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error ("getPreviousSales - oException : " + oException);
		}
		return oSalesDataLineItemResponse;
	}
	
	private ArrayList<SalesLineItemData> buildSalesLineItemArray(ArrayList<SalesLineItemData> arrSalesLineItem) 
	{
		for (int nIndex = 0; nIndex < arrSalesLineItem.size(); nIndex++)
			arrSalesLineItem.get(nIndex).setM_strDate(getClientCompatibleFormat(arrSalesLineItem.get(nIndex).getM_dCreatedOn()));
		return arrSalesLineItem;
	}

	@Override
	public Criteria prepareCustomCriteria(Criteria oCriteria, SalesLineItemData oSalesLineItemData) throws RuntimeException 
	{
		if(oSalesLineItemData.getM_oItemData() != null && oSalesLineItemData.getM_oItemData().getM_nItemId() > 0)
			oCriteria.add(Restrictions.eq ("m_oItemData", oSalesLineItemData.getM_oItemData()));
		if(oSalesLineItemData.getM_oSalesData().getM_oClientData() != null && oSalesLineItemData.getM_oSalesData().getM_oClientData().getM_nClientId() > 0)
			oCriteria.createCriteria ("m_oSalesData").add(Restrictions.eq ("m_oClientData", oSalesLineItemData.getM_oSalesData().getM_oClientData()));
		oCriteria.setMaxResults(kMaxPreviousSalesResult);
		oCriteria.addOrder(Order.desc("m_dCreatedOn"));
		return oCriteria;
	}
}
