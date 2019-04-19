package com.techmust.inventory.sales;

import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmust.generic.dataprocessor.GenericIDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.helper.TradeMustHelper;
import com.techmust.inventory.purchaseorder.PurchaseOrderLineItemData;
@Controller
public class NonStockSalesLineItemDataProcessor extends
		GenericIDataProcessor<NonStockSalesLineItemData> {

	@Override
	public GenericResponse create(NonStockSalesLineItemData oNonStockSalesLineItemData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse deleteData(NonStockSalesLineItemData oNonStockSalesLineItemData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericResponse get(NonStockSalesLineItemData oNonStockSalesLineItemData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXML(NonStockSalesLineItemData oNonStockSalesLineItemData) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	} 

	@RequestMapping(value="/NonStockSalesLineItemDataList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse list(@RequestBody TradeMustHelper oTradeMustHelper) throws Exception 
	{
		HashMap<String, String> oOrderBy = new HashMap<String, String> ();
		oOrderBy.put(oTradeMustHelper.getM_strColumn(), oTradeMustHelper.getM_strOrderBy());
		return list(oTradeMustHelper.getM_oNonSalesLineItemData(), oOrderBy, oTradeMustHelper.getM_nPageNo(), oTradeMustHelper.getM_nPageSize());
	}
	
	@SuppressWarnings("unchecked")
	public GenericResponse list(NonStockSalesLineItemData oNonStockSalesLineItemData, HashMap<String, String> arrOrderBy, int nPageNumber, int nPageSize) throws Exception 
	{
		m_oLogger.info ("list");
		m_oLogger.debug ("list - oNonStockSalesLineItemData [IN] : " +oNonStockSalesLineItemData);
		NonStockSalesLineItemResponse oNonStockSalesLineItemResponse = new NonStockSalesLineItemResponse ();
		try 
		{
			oNonStockSalesLineItemResponse.m_nRowCount = getRowCount(oNonStockSalesLineItemData);
			oNonStockSalesLineItemResponse.m_arrNonStockSalesLineItems = new ArrayList (oNonStockSalesLineItemData.list (arrOrderBy, nPageNumber, nPageSize));
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("list - oException : " +oException);
		}
		return oNonStockSalesLineItemResponse;
	}

	@Override
	public GenericResponse update(NonStockSalesLineItemData oNonStockSalesLineItemData) throws Exception 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/NonStockSalesLineItemDatagetUnbilledNonStockItemList", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse getUnbilledNonStockItemList (@RequestBody NonStockSalesLineItemData oNonStockSalesLineItemData) throws Exception
	{
		m_oLogger.info ("getUnbilledNonStockItemList");
		m_oLogger.debug ("getUnbilledNonStockItemList - oNonStockSalesLineItemData [IN] : " + oNonStockSalesLineItemData);
		NonStockSalesLineItemResponse oNonStockSalesLineItemResponse = new NonStockSalesLineItemResponse ();
		try 
		{
			oNonStockSalesLineItemResponse.m_arrPurchaseOrderLineItems = new ArrayList (oNonStockSalesLineItemData.listCustomData(this));
			oNonStockSalesLineItemResponse.m_arrPurchaseOrderLineItems = buildData (oNonStockSalesLineItemResponse.m_arrPurchaseOrderLineItems);
		} 
		catch (Exception oException) 
		{
			m_oLogger.error("getUnbilledNonStockItemList - oException : " +oException);
			throw oException;
		}
		return oNonStockSalesLineItemResponse;
	}
	
	public Criteria prepareCustomCriteria(Criteria oCriteria, NonStockSalesLineItemData oNonStockSalesLineItemData) throws RuntimeException 
	{
		oCriteria.createCriteria("m_oPOLineItem").add(Restrictions.gtProperty("m_nShippedQty", "m_nPurchasedQty"));
		oCriteria.setProjection(Projections.projectionList().add(Projections.groupProperty("m_oPOLineItem")));
		return oCriteria;
	}
	
	private ArrayList<PurchaseOrderLineItemData> buildData(ArrayList<PurchaseOrderLineItemData> arrayList) 
	{
		m_oLogger.info("buildSalesData");
		for (int nIndex=0; nIndex < arrayList.size(); nIndex++)
			arrayList.get(nIndex).getM_oPurchaseOrderData().setM_strPurchaseOrderDate(getClientCompatibleFormat(arrayList.get(nIndex).getM_oPurchaseOrderData().getM_dPurchaseOrderDate())); 
		return arrayList;
	}

	@Override
	public GenericResponse list(NonStockSalesLineItemData oGenericData, HashMap<String, String> arrOrderBy)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
