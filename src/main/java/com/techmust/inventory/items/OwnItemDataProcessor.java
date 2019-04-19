package com.techmust.inventory.items;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.techmust.inventory.items.ItemDataProcessor;
import com.techmust.generic.response.GenericResponse;
import com.techmust.inventory.configuration.ConfigurationData;

@Controller
public class OwnItemDataProcessor extends ItemDataProcessor
{
	@RequestMapping(value="/ownItemDataCreate", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public GenericResponse create(@RequestBody ItemData oItemData) throws Exception
   {
		m_oLogger.info ("create");
		m_oLogger.debug ("create - oItemData - m_strArticleNumber [IN] : " + oItemData.getM_strArticleNumber());
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		VendorItemData oVendorItemData = new VendorItemData ();
		try
		{	
			oItemData.setDefaultChildImage(oItemData); 
			oVendorItemData.getM_oVendorData().setM_nClientId(getDefaultVendorId ());
			oItemDataResponse = (ItemDataResponse) super.create(oItemData);
			oVendorItemData.getM_oItemData().setM_nItemId(oItemDataResponse.m_arrItems.get(0).getM_nItemId());
			oItemDataResponse.m_bSuccess = oVendorItemData.saveObject();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("create - oException : " + oException);
			throw oException;
		}
		m_oLogger.debug ("create - oItemDataResponse.m_bSuccess [OUT] : " + oItemDataResponse.m_bSuccess);
		return oItemDataResponse;
   }
	
	public GenericResponse updateVendorItemTable() throws Exception 
	{
		m_oLogger.info ("updateVendorItemTable");
		ItemDataResponse oItemDataResponse = new ItemDataResponse ();
		VendorItemData oVendorItemData = new VendorItemData ();
		ItemData oItemData = new ItemData ();
		try
		{
			HashMap<String, String> oOrderBy = new HashMap<String, String> ();
			oItemDataResponse = (ItemDataResponse) super.list(oItemData, oOrderBy);
			int nVendorId = getDefaultVendorId();
			oItemDataResponse.m_nRowCount = 0;
			for (int nIndex = 0; nIndex < oItemDataResponse.m_arrItems.size(); nIndex++)
			{
				oVendorItemData.getM_oItemData().setM_nItemId(oItemDataResponse.m_arrItems.get(nIndex).getM_nItemId());
				oVendorItemData.getM_oVendorData().setM_nClientId(nVendorId);
				oItemDataResponse.m_bSuccess = oVendorItemData.saveObject();
				oItemDataResponse.m_nRowCount++;
			}
		}
		catch (Exception oException)
		{
			m_oLogger.error ("updateVendorItemTable - oException : " + oException);
			throw oException;
		}
		return oItemDataResponse;
	}

	private int getDefaultVendorId() throws Exception
	{
		m_oLogger.info ("getDefaultVendorId");
		int nVendorId = -1;
		ConfigurationData oData = new ConfigurationData ();
		try
		{
			oData.setM_strKey("kDefaultVendor");
			oData = (ConfigurationData) populateObject(oData);
			if(oData == null)
				throw new Exception ("please set the Default Vendor.");
			else
				nVendorId = oData.getM_nIntValue();
		}
		catch (Exception oException)
		{
			m_oLogger.error ("getDefaultVendorId - oException : " + oException);
			throw oException;
		}
		return nVendorId;
	}

}
